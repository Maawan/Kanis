package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.OrderDetail;
import rw.kanis.shop.Models.PurchaseHistory;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Presentation.presenters.PurchaseHistoryDetailsPresenter;
import rw.kanis.shop.Presentation.ui.activities.OrderStatusDialogView;
import rw.kanis.shop.Presentation.ui.activities.PurchaseHistoryDetailsView;
import rw.kanis.shop.Presentation.ui.adapters.OrderDetailsAdapter;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PurchaseHistoryDetailsActivity extends BaseActivity implements PurchaseHistoryDetailsView , OrderStatusDialogView {
    private AuthResponse authResponse;
    private PurchaseHistory mPurchaseHistory;
    private Dialog deliveryStatus;
    private ImageView closeBtn;
    private TextView firstCircle , secondCircle , thirdCircle , fourthCircle;
    private TextView firstText , secondText , thirdText , fourthText;
    private TextView order_code, shipping_method, order_date, payment_method, payment_status, total_amount, shipping_address,
            sub_total, tax, shipping_cost, coupon_discount, grand_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight <= AppConfig.convertDpToPixel(24))
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_purchase_history_details);

        initializeActionBar();
        setTitle("Order Details");
        initviews();
        mPurchaseHistory = (PurchaseHistory) getIntent().getSerializableExtra("purchase_history");
        setDetails();

        authResponse = new UserPrefs(getApplicationContext()).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            new PurchaseHistoryDetailsPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getOrderDetails(authResponse.getAccessToken(), mPurchaseHistory.getLinks().getDetails());
        }
    }

    private void initviews(){
        order_code = findViewById(R.id.order_code);
        shipping_method = findViewById(R.id.shipping_method);
        order_date = findViewById(R.id.order_date);
        payment_method = findViewById(R.id.payment_method);
        payment_status = findViewById(R.id.payment_status);
        total_amount = findViewById(R.id.total_amount);
        shipping_address = findViewById(R.id.shipping_address);

        sub_total = findViewById(R.id.sub_total);
        tax = findViewById(R.id.tax);
        shipping_cost = findViewById(R.id.shipping_cost);
        coupon_discount = findViewById(R.id.coupon_discount);
        grand_total = findViewById(R.id.grand_total);
    }

    private void setDetails(){
        order_code.setText(mPurchaseHistory.getCode());
        shipping_method.setText("Flat Shipping Rate");
        order_date.setText(mPurchaseHistory.getDate());
        payment_method.setText(StringUtils.capitalize(mPurchaseHistory.getPaymentType()));
        payment_status.setText(StringUtils.capitalize(mPurchaseHistory.getPaymentStatus()));
        total_amount.setText(AppConfig.convertPrice(this, mPurchaseHistory.getGrandTotal()));
        shipping_address.setText(mPurchaseHistory.getShippingAddress().getAddress() +", "+mPurchaseHistory.getShippingAddress().getCity()+", "+mPurchaseHistory.getShippingAddress().getCountry());

        sub_total.setText(AppConfig.convertPrice(this, mPurchaseHistory.getSubtotal()));
        tax.setText(AppConfig.convertPrice(this, mPurchaseHistory.getTax()));
        shipping_cost.setText(AppConfig.convertPrice(this, mPurchaseHistory.getShippingCost()));
        coupon_discount.setText(AppConfig.convertPrice(this, mPurchaseHistory.getCouponDiscount()));
        grand_total.setText(AppConfig.convertPrice(this, mPurchaseHistory.getGrandTotal()));
    }

    @Override
    public void onOrderDetailsLoaded(List<OrderDetail> orderDetailList) {
        RecyclerView recyclerView = findViewById(R.id.order_details_list);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        OrderDetailsAdapter adapter = new OrderDetailsAdapter(getApplicationContext(), orderDetailList , this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showDialog(String status) {

        //Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        deliveryStatus = new Dialog(this);
        deliveryStatus.setContentView(R.layout.order_status_dialog);
        deliveryStatus.setCancelable(false);
        closeBtn = deliveryStatus.findViewById(R.id.closeBtn);
        firstCircle = deliveryStatus.findViewById(R.id.firstCircle);
        firstText = deliveryStatus.findViewById(R.id.firstText);
        secondCircle = deliveryStatus.findViewById(R.id.secondCircle);
        secondText = deliveryStatus.findViewById(R.id.secondText);
        thirdCircle = deliveryStatus.findViewById(R.id.thirdCircle);
        thirdText = deliveryStatus.findViewById(R.id.thirdText);
        fourthCircle = deliveryStatus.findViewById(R.id.fourthCircle);
        fourthText = deliveryStatus.findViewById(R.id.fourthText);
       // Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

        if(status.equals("PENDING") || status.equals("pending")){
            firstCircle.setText("✔");
            firstText.setTextColor(getResources().getColor(R.color.status));
        }else if(status.equals("CONFIRMED") || status.equals("confirmed")){
            firstCircle.setText("✔");
            firstText.setTextColor(getResources().getColor(R.color.status));
            secondCircle.setText("✔");
            secondText.setTextColor(getResources().getColor(R.color.status));
        }else if(status.equals("On_delivery") || status.equals("On_Delivery") || status.equals("ON_DELIVERY")){
            firstCircle.setText("✔");
            firstText.setTextColor(getResources().getColor(R.color.status));
            secondCircle.setText("✔");
            secondText.setTextColor(getResources().getColor(R.color.status));
            thirdCircle.setText("✔");
            thirdText.setTextColor(getResources().getColor(R.color.status));
        }else if(status.equals("delivered") || status.equals("Delivered") || status.equals("DELIVERED")){
            firstCircle.setText("✔");
            firstText.setTextColor(getResources().getColor(R.color.status));
            secondCircle.setText("✔");
            secondText.setTextColor(getResources().getColor(R.color.status));
            thirdCircle.setText("✔");
            thirdText.setTextColor(getResources().getColor(R.color.status));
            fourthCircle.setText("✔");
            fourthText.setTextColor(getResources().getColor(R.color.status));
        }
        deliveryStatus.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        deliveryStatus.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deliveryStatus.show();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryStatus.dismiss();
            }
        });
    }
}
