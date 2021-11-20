package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Network.response.CouponResponse;
import rw.kanis.shop.Network.response.OrderResponse;
import rw.kanis.shop.Presentation.presenters.PaymentPresenter;
import rw.kanis.shop.Presentation.ui.activities.PaymentView;
import rw.kanis.shop.Presentation.ui.adapters.PaymentSelectAdapter;
import rw.kanis.shop.Presentation.ui.listeners.PaymentSelectListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

//import com.flutterwave.raveandroid.RavePayActivity;
//import com.flutterwave.raveandroid.RaveUiManager;
//import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
//import com.flutterwave.raveandroid.rave_presentation.RavePayManager;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.gson.JsonObject;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
//import com.flutterwave.raveandroid.RavePayManager;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends BaseActivity implements PaymentSelectListener, PaymentView, PaymentResultListener {
    private String payment_method,payment_details="Not Received",manual_payment="0";
    private Button place_order;
    private EditText coupon_code;
    private Button apply_coupon;
    private String airtelMoneyDesc = "";
    private String momoMTNDesc = "";
    private String RAVE_ENCRY_KEY="";
    private Double total, shipping_cost, tax, coupon_discount = 0.0;
    private String shipping_address;
    private TextView total_amount;
    private String city;
    private String RAVE_PUBLIC_KEY = "";
    private String RAVE_SECRET_KEY = "";
    private ProgressDialog progressDialog;
    private AuthResponse authResponse;
    private JSONObject jsonObject;
    private RecyclerView recyclerView;
    private String name,email,phone;
    private PaymentSelectAdapter adapter;
    private ProgressBar progressBar;
    private TextView shipping_amount , base_amount;
    private static final int AIRTEL_REQUEST_CODE = 500;
    private static final int MOMO_REQUEST_CODE = 600;
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
        setContentView(R.layout.activity_payment);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        shipping_amount = findViewById(R.id.shipping_amount);
        base_amount = findViewById(R.id.base_amount);
        total = getIntent().getDoubleExtra("total", 0.0);
        shipping_cost = getIntent().getDoubleExtra("shipping", 0.0);
        tax = getIntent().getDoubleExtra("tax", 0.0);
        city = getIntent().getStringExtra("city");

        shipping_address = getIntent().getStringExtra("shipping_address");
        Log.e("PaymentActivity","shipping address"+shipping_address);
        try {
            jsonObject = new JSONObject(shipping_address);
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            phone = jsonObject.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        authResponse = new UserPrefs(this).getAuthPreferenceObjectJson("auth_response");
        Checkout.preload(getApplicationContext());
        initializeActionBar();
        setTitle("Checkout");
        AndroidNetworking.initialize(getApplicationContext());
        //Toast.makeText(this, "You have Reached to Checkout Module", Toast.LENGTH_SHORT).show();
        setPaymentOptions();

        progressDialog = new ProgressDialog(this);
        place_order = findViewById(R.id.place_order);
        coupon_code = findViewById(R.id.coupon_code);
        apply_coupon = findViewById(R.id.apply_coupon);
        total_amount = findViewById(R.id.total_amount);

        total_amount.setText(AppConfig.convertPrice(this, total));
        base_amount.setText(AppConfig.convertPrice(this , total - shipping_cost));
        if(shipping_cost > 0) {
            shipping_amount.setText(AppConfig.convertPrice(this, shipping_cost));
        }else {
            shipping_amount.setText("FREE");
        }

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment_method != null){
                    if (payment_method.equals("paypal")){
                        //setupBraintreeAndStartExpressCheckout();
                    }
//
                    else if(payment_method.equals("razorpay")){
                        startRazorPayment();
                    }
                    else if(payment_method.equals("cod")){
                        checkout_done(null);
                    }
                    else if(payment_method.equals("Airtel Money")){
                        Intent intent = new Intent(PaymentActivity.this, AirtelPaymentActivity.class);
                        intent.putExtra("total", total);
                        intent.putExtra("shipping", shipping_cost);
                        intent.putExtra("tax", tax);
                        intent.putExtra("desc" , airtelMoneyDesc);
                        intent.putExtra("coupon_discount", coupon_discount);
                        startActivityForResult(intent, AIRTEL_REQUEST_CODE);

                        //checkout_done(null);
                    }else if(payment_method.equals("MTN Money")){
                        Intent intent = new Intent(PaymentActivity.this, MomoPaymentActivity.class);
                        intent.putExtra("total", total);
                        intent.putExtra("shipping", shipping_cost);
                        intent.putExtra("tax", tax);
                        intent.putExtra("desc",momoMTNDesc);
                        intent.putExtra("coupon_discount", coupon_discount);
                        startActivityForResult(intent, MOMO_REQUEST_CODE);

                    }

                    else if(payment_method.equals("flutterwave")){
                        initialiseFlutterGateway();
                        //Toast.makeText(PaymentActivity.this, "FlutterWave is Selected as Payment Method", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    CustomToast.showToast(PaymentActivity.this, "Please select a payment method", R.color.colorWarning);
                }
            }
        });

        apply_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = coupon_code.getText().toString();
                if(code.length() > 0){
                    AuthResponse authResponse = new UserPrefs(PaymentActivity.this).getAuthPreferenceObjectJson("auth_response");
                    if(authResponse != null && authResponse.getUser() != null){
                        new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), PaymentActivity.this).applyCoupon(authResponse.getUser().getId(), code, authResponse.getAccessToken());
                    }
                    else {

                    }
                }
                else {
                    CustomToast.showToast(PaymentActivity.this, "Please enter coupon code first", R.color.colorWarning);
                }
            }
        });
    }

    private void initialiseFlutterGateway() {
        new RaveUiManager(this)
                .setAmount(total)
                .setCurrency("RWF")
                .setEmail(email)
                .setfName(name)
                .setlName("")
                .setPublicKey(RAVE_PUBLIC_KEY)
                .setEncryptionKey(RAVE_ENCRY_KEY)
                .setTxRef(System.currentTimeMillis() + "REF")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .onStagingEnv(false)
                .acceptBarterPayments(true)
                .acceptMpesaPayments(true)
                .acceptBankTransferPayments(true)
                .acceptRwfMobileMoneyPayments(true)
                .acceptFrancMobileMoneyPayments(true)
                .acceptSaBankPayments(true)
                .acceptUssdPayments(true)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();





    }

    private void setPaymentOptions(){
        List<PaymentModel> paymentModels = new ArrayList<>();







        recyclerView = findViewById(R.id.payment_select_list);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PaymentSelectAdapter(this, paymentModels, this);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(AppConfig.PAYMENT_API)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       //Toast.makeText(PaymentActivity.this, "Response -> " + response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject ob = response.getJSONObject("flutterwave");
                            //Toast.makeText(PaymentActivity.this, ob.toString(), Toast.LENGTH_SHORT).show();
                            if(ob.getInt("flutterwave_payment_active") == 1){
                                RAVE_PUBLIC_KEY = ob.getString("RAVE_PUBLIC_KEY");
                                RAVE_SECRET_KEY = ob.getString("RAVE_SECRET_KEY");
                                RAVE_ENCRY_KEY = ob.getString("RAVE_ENCRY_KEY");
                               // Toast.makeText(PaymentActivity.this, RAVE_PUBLIC_KEY + "  " + RAVE_SECRET_KEY, Toast.LENGTH_SHORT).show();
                                paymentModels.add(new PaymentModel(R.drawable.flutterwave,"flutterwave","Debit/Credit Card or Account"));
                            }
                            if(response.getInt("cash_payment") == 1){
                                paymentModels.add(new PaymentModel(R.drawable.cod, "cod", "Cash on Delivery"));
                            }
                            JSONObject ob1 = response.getJSONObject("offline_payment");
                            if(ob1.getInt("offline_payment_active") == 1){
                                JSONArray array = ob1.getJSONArray("payment_name");
                                for(int i = 0 ; i < array.length() ; i++){
                                    JSONObject object = array.getJSONObject(i);
                                    if(object.getString("method_name").equals("Airtel Money")){
                                        paymentModels.add(new PaymentModel(R.drawable.airtelmoney, "Airtel Money", "Airtel Money"));
                                        airtelMoneyDesc = object.getString("method_desc");
                                    }
                                    if(object.getString("method_name").equals("MTN Money")){
                                        paymentModels.add(new PaymentModel(R.drawable.momo, "MTN Money", "MTN Money"));
                                        momoMTNDesc = object.getString("method_desc");
                                    }
                                }



                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentActivity.this, "Exception -> "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

//    public void setupBraintreeAndStartExpressCheckout() {
//        try {
//            mBraintreeFragment = BraintreeFragment.newInstance(this, AppConfig.BRAINTREE_KEY);
//            // mBraintreeFragment is ready to use!
//        } catch (InvalidArgumentException e) {
//            // There was an issue with your authorization string.
//            Log.d("Braintree", e.getMessage());
//        }
//
//
//        PayPalRequest request = new PayPalRequest(String.valueOf(total))
//                .currencyCode("USD")
//                .intent(PayPalRequest.INTENT_AUTHORIZE);
//
//        PayPal.requestOneTimePayment(mBraintreeFragment, request);
//
//    }

//    @Override
//    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//        // Send nonce to server
//        String nonce = paymentMethodNonce.getNonce();
//        checkout_done(nonce);
//    }
//
//    @Override
//    public void onError(Exception error) {
//        Log.d("PayPal", error.getMessage());
//        if (error instanceof ErrorWithResponse) {
//            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
//            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
//            if (cardErrors != null) {
//                // There is an issue with the credit card.
//                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
//                if (expirationMonthError != null) {
//                    // There is an issue with the expiration month.
//                    //setErrorMessage(expirationMonthError.getMessage());
//                }
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            String paymentIntentID = data.getStringExtra("paymentIntentID");
//            checkout_done(paymentIntentID);
//        }
        if (resultCode == RESULT_CANCELED) {
        }

        if(requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null){
            if(resultCode == RavePayActivity.RESULT_SUCCESS){

                String JSON_RESPONSE = data.getStringExtra("response");
                String paymentIntentID = getIDfromJSON(JSON_RESPONSE);
              //  Toast.makeText(this, paymentIntentID, Toast.LENGTH_SHORT).show();
                checkout_done(paymentIntentID);
            }else if(resultCode == RavePayActivity.RESULT_CANCELED){
                Toast.makeText(this, "Transaction could not be completed", Toast.LENGTH_SHORT).show();
            }else if(requestCode == RavePayActivity.RESULT_ERROR){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == AIRTEL_REQUEST_CODE && resultCode == RESULT_OK){
            checkout_done(data.getStringExtra("transID"));
        }
        if(requestCode == MOMO_REQUEST_CODE && resultCode == RESULT_OK){
            checkout_done(data.getStringExtra("transID"));
        }

    }
    public String getIDfromJSON(String Json){
        try {
            JSONObject responseObject = new JSONObject(Json);
//            JSONArray responseArray = responseObject.getJSONArray("data");
//            JSONObject currentBook = responseArray.getJSONObject(3);
//            String reference = currentBook.getString("flwRef");
            String reference = responseObject.getJSONObject("data").getString("flwRef");
            return reference;

        }catch (Exception e){
          //  Toast.makeText(this, "Error -> " + e, Toast.LENGTH_SHORT).show();
            Log.e("Error",e+" ");
        }




        return "";
    }

    private void checkout_done(String paymentID){
        progressDialog.setMessage("Checkout is processing. Please wait.");
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shipping_address", shipping_address);
        jsonObject.addProperty("payment_type", payment_method);
        if(payment_method.equals("cod") || payment_method.equals("Airtel Money") || payment_method.equals("MTN Money")){
            jsonObject.addProperty("payment_status","Unpaid");

        }else {
            jsonObject.addProperty("payment_status","Paid");
        }
//        jsonObject.addProperty("payment_status", (payment_method == "cod" ? "unpaid" : (payment_method == "Airtel Money" ? "unpaid" :"paid")));
       // jsonObject.addProperty("payment_status", payment_method == "Airtel Money" ? "unpaid" : "paid");
        if(payment_method.equals("Airtel Money") || payment_method.equals("MTN Money")){
            jsonObject.addProperty("manual_payment", 1);
        }else {
            jsonObject.addProperty("manual_payment", 0);
        }
        jsonObject.addProperty("payment_details", payment_details);



        jsonObject.addProperty("user_id", authResponse.getUser().getId());
        jsonObject.addProperty("grand_total", total);
        jsonObject.addProperty("coupon_discount", coupon_discount);
        jsonObject.addProperty("coupon_code", "");
        jsonObject.addProperty("shipping_cost",shipping_cost);
        jsonObject.addProperty("tax",tax);
        jsonObject.addProperty("city" , city);
        jsonObject.addProperty("transactionID" , paymentID);
      //  Toast.makeText(this, "Grand Total -> " + total + " shipping cost " + shipping_cost + "  Tax" + tax, Toast.LENGTH_SHORT).show();

        if (payment_method.equals("paypal")){
           // jsonObject.addProperty("nonce", paymentID);
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitPaypalOrder(authResponse.getAccessToken(), jsonObject);
        }
        else if(payment_method.equals("card")){
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitOrder(authResponse.getAccessToken(), jsonObject);
        }
        else if (payment_method.equals("cod")){
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitCODOrder(authResponse.getAccessToken(), jsonObject);
        }
        else if(payment_method.equals("flutterwave")){
           // jsonObject.addProperty("nonce", paymentID);
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitOrder(authResponse.getAccessToken(), jsonObject);
        }

        else if(payment_method.equals("Airtel Money")){
            //jsonObject.addProperty("nonce", paymentID);
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitCODOrder(authResponse.getAccessToken(), jsonObject);
        }
        else if(payment_method.equals("MTN Money")){
           // jsonObject.addProperty("nonce", paymentID);
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitCODOrder(authResponse.getAccessToken(), jsonObject);
        }
        /*else if(payment_method.equals("razorpay")){
            jsonObject.addProperty("nonce", paymentID);
            new PaymentPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).submitPaypalOrder(authResponse.getAccessToken(), jsonObject);
        }*/
    }

    @Override
    public void onPaymentItemClick(PaymentModel paymentModel) {
        payment_method = paymentModel.getPayment_method();
    }

    @Override
    public void onCouponApplied(CouponResponse couponResponse) {
        if (couponResponse.getSuccess()){
            apply_coupon.setEnabled(false);
            apply_coupon.setText("COUPON APPLIED");
            coupon_code.setEnabled(false);
            CustomToast.showToast(this, couponResponse.getMessage(), 1);
            coupon_discount = couponResponse.getDiscount();
            total -= coupon_discount;
            total_amount.setText(AppConfig.convertPrice(this, total));
        }
        else {
            CustomToast.showToast(this, couponResponse.getMessage(), R.color.colorWarning);
        }
    }

    @Override
    public void onOrderSubmitted(OrderResponse orderResponse) {
        progressDialog.dismiss();
        if (orderResponse.getSuccess()){
            CustomToast.showToast(this, orderResponse.getMessage(), 1);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("message", orderResponse.getMessage());
            intent.putExtra("position", "cart");
            startActivity(intent);
            finish();
        }
        else {
            CustomToast.showToast(this, orderResponse.getMessage(), R.color.colorDanger);
        }
    }
    public void startRazorPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "Kanis");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://abc.com/ppp.png");
            options.put("currency", "INR");

            double payment = total;

//            double total = payment);
            total = payment * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("PaymentActivity","Success "+s.toString());
        checkout_done( s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("PaymentActivity","Failed "+s.toString());
    }

    public class PaymentModel{
        int drawable;
        String payment_method, payment_text;

        public PaymentModel(int drawable, String payment_method, String payment_text) {
            this.drawable = drawable;
            this.payment_method = payment_method;
            this.payment_text = payment_text;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_text() {
            return payment_text;
        }

        public void setPayment_text(String payment_text) {
            this.payment_text = payment_text;
        }
    }
}
