package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import java.lang.ref.WeakReference;

import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Network.response.CouponResponse;
import rw.kanis.shop.Network.response.OrderResponse;

import rw.kanis.shop.Presentation.presenters.PaymentPresenter;
import rw.kanis.shop.Presentation.ui.activities.PaymentView;

import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

public class AirtelPaymentActivity extends BaseActivity {

    private static ProgressDialog progressDialog;
    private String paymentIntentClientSecret;
    private double total, shipping, tax, coupon_discount;
    private TextView tvSub_total, tvTax, tvShipping, tvCoupon_discount, tvGrand_total;
    private AuthResponse authResponse;
    private EditText transactionID;
    private TextView desc;

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
        setContentView(R.layout.activity_airtel_payment);
        desc = findViewById(R.id.descText);
        desc.setText(getIntent().getStringExtra("desc"));
        transactionID = findViewById(R.id.transactionID);
        total = getIntent().getDoubleExtra("total", 0.0);
        shipping = getIntent().getDoubleExtra("shipping", 0.0);
        tax = getIntent().getDoubleExtra("tax", 0.0);
        coupon_discount = getIntent().getDoubleExtra("coupon_discount", 0.0);

        initializeActionBar();
        setTitle("Airtel Payment Activity");

        initviews();

        authResponse = new UserPrefs(this).getAuthPreferenceObjectJson("auth_response");



        startCheckout();
    }

    private void initviews(){
        progressDialog = new ProgressDialog(this);
        tvSub_total = findViewById(R.id.sub_total);
        tvTax = findViewById(R.id.tax);
        tvShipping = findViewById(R.id.shipping);
        tvCoupon_discount = findViewById(R.id.coupon_discount);
        tvGrand_total = findViewById(R.id.grand_total);

        tvSub_total.setText(String.valueOf(Math.round(total-tax-shipping-coupon_discount)));
        tvTax.setText(String.valueOf(Math.round(tax)));
        tvShipping.setText(String.valueOf(Math.round(shipping)));
        tvCoupon_discount.setText(String.valueOf(coupon_discount));
        tvGrand_total.setText(String.valueOf(Math.round(total)));
    }

    private void startCheckout() {

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            String transID = transactionID.getText().toString();
            if(!transID.equals("")) {

                progressDialog.setMessage("Please wait. Your transaction is processing.");
                progressDialog.show();
                Intent intent = new Intent();
                intent.putExtra("transID" , transID);
                setResult(RESULT_OK , intent);
                finish();
            }else {
                Toast.makeText(this, "Transaction ID can't be Empty", Toast.LENGTH_SHORT).show();
            }
        });

    }




}
