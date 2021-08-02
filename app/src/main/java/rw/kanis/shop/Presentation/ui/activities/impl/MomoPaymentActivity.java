package rw.kanis.shop.Presentation.ui.activities.impl;

import androidx.appcompat.app.AppCompatActivity;

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

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;

import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.UserPrefs;

public class MomoPaymentActivity extends BaseActivity {
    private Stripe stripe;
    private static ProgressDialog progressDialog;
    private String paymentIntentClientSecret;
    private double total, shipping, tax, coupon_discount;
    private TextView tvSub_total, tvTax, tvShipping, tvCoupon_discount, tvGrand_total;
    private AuthResponse authResponse;
    private EditText transactionID;
    private TextView descText;

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
        setContentView(R.layout.activity_momo_payment);
        transactionID = findViewById(R.id.transactionID);
        descText = findViewById(R.id.descText);
        total = getIntent().getDoubleExtra("total", 0.0);
        shipping = getIntent().getDoubleExtra("shipping", 0.0);
        tax = getIntent().getDoubleExtra("tax", 0.0);
        descText.setText(getIntent().getStringExtra("desc"));
        coupon_discount = getIntent().getDoubleExtra("coupon_discount", 0.0);

        initializeActionBar();
        setTitle("MTN Money");

        initviews();

        authResponse = new UserPrefs(this).getAuthPreferenceObjectJson("auth_response");

        PaymentConfiguration.init(getApplicationContext(), AppConfig.STRIPE_KEY);

        startCheckout();
    }

    private void initviews(){
        progressDialog = new ProgressDialog(this);
        tvSub_total = findViewById(R.id.sub_total);
        tvTax = findViewById(R.id.tax);
        tvShipping = findViewById(R.id.shipping);
        tvCoupon_discount = findViewById(R.id.coupon_discount);
        tvGrand_total = findViewById(R.id.grand_total);

        tvSub_total.setText(AppConfig.convertPrice(this,total-tax-shipping+coupon_discount));
        tvTax.setText(AppConfig.convertPrice(this,tax));
        tvShipping.setText(AppConfig.convertPrice(this , shipping));
        tvCoupon_discount.setText(AppConfig.convertPrice(this , coupon_discount));
        tvGrand_total.setText(AppConfig.convertPrice(this , total));
    }

    private void startCheckout() {
        // Hook up the pay button to the card widget and stripe instance
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
