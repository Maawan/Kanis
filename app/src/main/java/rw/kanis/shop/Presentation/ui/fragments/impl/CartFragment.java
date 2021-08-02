package rw.kanis.shop.Presentation.ui.fragments.impl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.CartModel;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Network.response.CartQuantityUpdateResponse;
import rw.kanis.shop.Network.response.RemoveCartResponse;
import rw.kanis.shop.Presentation.presenters.CartPresenter;
import rw.kanis.shop.Presentation.ui.activities.impl.ShippingActivity;
import rw.kanis.shop.Presentation.ui.adapters.CartListAdapter;
import rw.kanis.shop.Presentation.ui.fragments.CartView;
import rw.kanis.shop.Presentation.ui.listeners.CartItemListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.SwipeToDeleteCallback;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

import static com.facebook.FacebookSdk.getApplicationContext;
import static rw.kanis.shop.Presentation.ui.activities.impl.MainActivity.navView;

public class CartFragment extends Fragment implements CartView, CartItemListener {
    private View v;
    private CartPresenter cartPresenter;
    private AuthResponse authResponse;
    private Button btnCheckout;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private TextView total_amount,base_amount , shipping_amount , tax_amount;
    private double total = 0.0;
    private double shipping = 0.0;
    //private double tax = 0.0;
    private int qty = 0;
    private double tax2 = 0.0;
    private double basePrice = 0.0;
   // private LinearLayout billLayout;
    private TextView cart_empty_text;
    private double MINIMUM_CART_VALUE = 0.0;
    static ArrayList<Integer> products;
    private static final String MINIMUM_CART_VALUE_API_LINK = "https://kanis.rw/api/v1/minimum_order_value";
    //

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cart, null);
        products = new ArrayList<>();
        btnCheckout = v.findViewById(R.id.checkout);
        progressBar = v.findViewById(R.id.item_progress_bar);
        linearLayout = v.findViewById(R.id.checkout_button);
        total_amount = v.findViewById(R.id.total_amount);
        //billLayout = v.findViewById(R.id.checkout_button);
        base_amount = v.findViewById(R.id.base_amount);
        shipping_amount = v.findViewById(R.id.shipping_amount);
        tax_amount = v.findViewById(R.id.tax_amount);
        cart_empty_text = v.findViewById(R.id.cart_empty_text);

        linearLayout.setVisibility(View.GONE);
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(MINIMUM_CART_VALUE_API_LINK)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MINIMUM_CART_VALUE = response.getDouble("minimum_order_value");
                           // Toast.makeText(getContext(), "Value is " + MINIMUM_CART_VALUE, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        cartPresenter = new CartPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);

        authResponse = new UserPrefs(getActivity()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            cartPresenter.getCartItems(authResponse.getUser().getId(), authResponse.getAccessToken());
            //Toast.makeText(getContext(), "ID -> " + authResponse.getAccessToken(), Toast.LENGTH_SHORT).show();
            Log.e("Access Token" ,authResponse.getAccessToken());

            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            cart_empty_text.setVisibility(View.VISIBLE);
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MINIMUM_CART_VALUE < total) {
                    Intent intent = new Intent(getActivity(), ShippingActivity.class);
                    intent.putExtra("total", total);
                    intent.putExtra("shipping", shipping);
                    intent.putExtra("tax", tax2);
                    intent.putExtra("basePrice", basePrice);
                    intent.putExtra("products" , products);
                    //Toast.makeText(getContext(), "Shipping Cost -> " + shipping + "\n Tax -> " + tax + "\n Total " + total, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else {
                    if(MINIMUM_CART_VALUE == 0){
                        Toast.makeText(getContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Your Cart Value should be Greater than RF " + MINIMUM_CART_VALUE, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }

    private void updateCartBadge(List<CartModel> cartItems){
        if (cartItems.size() > 0){
            //linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            total = 0;
            qty = 0;
            basePrice = 0;
            shipping = 0;
            tax2 = 0;
            products.removeAll(products);
            for (CartModel cartModel: cartItems){
                total += (cartModel.getPrice()+cartModel.getTax())*cartModel.getQuantity();
                //shipping += cartModel.getShippingCost()*cartModel.getQuantity();
                //tax = cartModel.getTax()+cartModel.getQuantity();
                qty += cartModel.getQuantity();
                tax2 += (cartModel.getTax() * cartModel.getQuantity());
                for(int i = 0 ; i < cartModel.getQuantity() ; i++){
                    int id = cartModel.getId();
                    products.add(cartModel.getId());
                }
                basePrice += cartModel.getPrice() * cartModel.getQuantity();
            }
            base_amount.setText(AppConfig.convertPrice(getContext(),basePrice));
            shipping_amount.setText(AppConfig.convertPrice(getContext(),shipping));
            tax_amount.setText(AppConfig.convertPrice(getContext() , tax2));
            total_amount.setText(AppConfig.convertPrice(getContext(), total));

            BottomNavigationMenuView bottomNavigationMenuView =
                    (BottomNavigationMenuView) navView.getChildAt(0);
            View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
            new QBadgeView(getActivity()).bindTarget(v).setBadgeText(String.valueOf(qty)).setShowShadow(false);
            //billLayout.setVisibility(View.GONE);
            if(cartItems.size() == 0) linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCartItems(List<CartModel> cartItems) {
        progressBar.setVisibility(View.GONE);
        if (cartItems.size() > 0){
            RecyclerView recyclerView = v.findViewById(R.id.product_list);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(horizontalLayoutManager);
            CartListAdapter adapter = new CartListAdapter(getActivity(), cartItems, this);
            recyclerView.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallback(adapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);


            updateCartBadge(cartItems);
        }
        else {
            linearLayout.setVisibility(View.GONE);
            basePrice = 0 ;
            tax2 = 0 ;
            cart_empty_text.setVisibility(View.VISIBLE);

            BottomNavigationMenuView bottomNavigationMenuView =
                    (BottomNavigationMenuView) navView.getChildAt(0);
            View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
            new QBadgeView(getActivity()).bindTarget(v).setBadgeText(String.valueOf(0)).setShowShadow(false);
        }
    }

    @Override
    public void showRemoveCartMessage(RemoveCartResponse removeCartResponse) {
        CustomToast.showToast(getActivity(), removeCartResponse.getMessage(), 1);
        cartPresenter.getCartItems(authResponse.getUser().getId(), authResponse.getAccessToken());
    }

    @Override
    public void showCartQuantityUpdateMessage(CartQuantityUpdateResponse cartQuantityUpdateResponse) {
        String message = cartQuantityUpdateResponse.getMessage();
        if(cartQuantityUpdateResponse.getMessage().equals("Cart updated")) {

            CustomToast.showToast(getActivity(), cartQuantityUpdateResponse.getMessage(), 1);
        }else if(cartQuantityUpdateResponse.getMessage().equals("Something went wrong")){
            CustomToast.showToast(getActivity(), cartQuantityUpdateResponse.getMessage(), R.color.colorSuccess);
        }else if(cartQuantityUpdateResponse.getMessage().equals("Maximum available quantity reached")){
            CustomToast.showToast(getActivity(), cartQuantityUpdateResponse.getMessage(), R.color.colorSuccess);
        }
        cartPresenter.getCartItems(authResponse.getUser().getId(), authResponse.getAccessToken());
    }

    @Override
    public void onCartRemove(CartModel cartModel) {
        cartPresenter.removeCartItem(cartModel.getId(), authResponse.getAccessToken());
    }

    @Override
    public void onQuantityUpdate(int qty, CartModel cartModel) {
        cartPresenter.updateCartQuantity(cartModel.getId(), qty, authResponse.getAccessToken());
    }
}
