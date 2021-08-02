package rw.kanis.shop.Presentation.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.CartModel;
import rw.kanis.shop.Presentation.ui.listeners.CartItemListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CartModel> cartItems;
    private LayoutInflater mInflater;
    private Context context;
    private CartItemListener cartItemListener;

    public Context getContext(){
        return this.context;
    }

    // data is passed into the constructor
    public CartListAdapter(Context context, List<CartModel> cartItems, CartItemListener cartItemListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.cartItems = cartItems;
        this.cartItemListener = cartItemListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // convenience method for getting data at click position
    public CartModel getItem(int id) {
        return cartItems.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView variant;
        TextView shipping_cost;
        TextView quantity;
        TextView price;
        TextView taxprice;
        Button qtyIncrease, qtyDecrease;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_product_image);
            name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_price);
            taxprice = itemView.findViewById(R.id.taxprice);
            variant = itemView.findViewById(R.id.cart_variant);
            shipping_cost = itemView.findViewById(R.id.cart_shipping_cost);
            shipping_cost.setVisibility(View.GONE);
            quantity = itemView.findViewById(R.id.cart_quantity);
            qtyIncrease = itemView.findViewById(R.id.cart_quantity_increase);
            qtyDecrease = itemView.findViewById(R.id.cart_quantity_decrease);
        }

        public void bind(CartModel product) {
            Glide.with(context).load(AppConfig.ASSET_URL+product.getProduct().getImage()).into(image);
            name.setText(product.getProduct().getName());
            price.setText("Price - "+AppConfig.convertPrice(context, product.getPrice()));
            taxprice.setText("Tax   - "+AppConfig.convertPrice(context,product.getTax()));
            variant.setText(product.getVariation());
            shipping_cost.setText("Shipping Cost - "+ AppConfig.convertPrice(context, product.getShippingCost()));
            quantity.setText(product.getQuantity().toString());

            if(Integer.parseInt(quantity.getText().toString()) <= 1){
                qtyDecrease.setEnabled(false);
            }

            qtyIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product.getCurrentStock() > product.getQuantity()) {
                        int qty = Integer.parseInt(quantity.getText().toString()) + 1;
                        quantity.setText(String.valueOf(qty));
                        qtyDecrease.setEnabled(true);
                        cartItemListener.onQuantityUpdate(qty, product);
                    }else{
//                        Toast.makeText(context, "Stock Limit Reached !!!", Toast.LENGTH_SHORT).show();
                        CustomToast.showToast((Activity) getContext() , "Stock Limit Reached !!!" , 2);
                    }
                }
            });

            qtyDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(quantity.getText().toString()) > 1){
                        int qty = Integer.parseInt(quantity.getText().toString()) - 1;
                        quantity.setText(String.valueOf(qty));
                        cartItemListener.onQuantityUpdate(qty, product);
                        if(qty == 1){
                            qtyDecrease.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    public void deleteItem(int position) {
        cartItemListener.onCartRemove(cartItems.get(position));
        cartItems.remove(position);
        notifyItemRemoved(position);
    }
}

