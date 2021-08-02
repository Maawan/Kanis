package rw.kanis.shop.Presentation.ui.activities.impl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import okhttp3.OkHttpClient;
import rw.kanis.shop.Models.ShippingAddress;
import rw.kanis.shop.Models.User;
import rw.kanis.shop.Network.response.AuthResponse;
import rw.kanis.shop.Network.response.ProfileInfoUpdateResponse;
import rw.kanis.shop.Network.response.ShippingInfoResponse;
import rw.kanis.shop.Presentation.presenters.AccountInfoPresenter;
import rw.kanis.shop.Presentation.ui.activities.AccountInfoView;
import rw.kanis.shop.Presentation.ui.adapters.ShippingAddressSelectAdapter;
import rw.kanis.shop.Presentation.ui.listeners.ShippingAddressSelectListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.Utils.CustomToast;
import rw.kanis.shop.Utils.UserPrefs;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
//import static rw.kanis.shop.Presentation.ui.fragments.impl.CartFragment.

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ShippingActivity extends BaseActivity implements AccountInfoView, ShippingAddressSelectListener {
    private AuthResponse authResponse;
    private Button payment;
    private Double total = 0.0, shipping = 0.0, tax= 0.0;
    private double basePrice = 0;
    private RecyclerView recyclerView;
    private ShippingAddress shippingAddress = null;
    private CardView card_new_address;
    AlertDialog.Builder builder;
    private String country = "" , city = "";
    private AlertDialog alert;

    private ArrayList<String> cities;
    private static final String CITIES_API = "https://kanis.rw/api/v1/cities";
    private ArrayList<String> countries;
    private ArrayList<Integer> products;
    double tempTax = 0.0;
    private ProgressDialog progressDialog;
    private static final String COUNTRIES_API = "https://kanis.rw/api/v1/countries";


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
        setContentView(R.layout.activity_shipping);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        cities = new ArrayList<>();
        countries = new ArrayList<>();
        products = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        total = getIntent().getDoubleExtra("total", 0.0);
        shipping = getIntent().getDoubleExtra("shipping", 0.0);
        tax = getIntent().getDoubleExtra("tax", 0.0);
        basePrice = getIntent().getDoubleExtra("basePrice",0.0);
        products = (ArrayList<Integer>) getIntent().getSerializableExtra("products");

        initializeActionBar();
        setTitle("Shipping Information");
        initviews();


        authResponse = new UserPrefs(this).getAuthPreferenceObjectJson("auth_response");
        if(authResponse != null && authResponse.getUser() != null){
            new AccountInfoPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getShippingAddresses(authResponse.getUser().getId(), authResponse.getAccessToken());
        }
    }

    private void initviews(){
        recyclerView = findViewById(R.id.rv_shipping_addresses);
        payment = findViewById(R.id.payment);

        card_new_address = findViewById(R.id.card_new_address);
        builder = new AlertDialog.Builder(this);




        card_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uncomment the below code to Set the message and title from the strings.xml file
                LayoutInflater inflater = getLayoutInflater();
                cities.removeAll(cities);
                countries.removeAll(countries);
                View dialogLayout = inflater.inflate(R.layout.shipping_address_add, null);
                builder.setView(dialogLayout);
                Spinner input_city = dialogLayout.findViewById(R.id.input_city);

                AndroidNetworking.get(CITIES_API)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arrray = response.getJSONArray("data");
                                    for(int i = 0 ; i < arrray.length() ; i++){
                                        JSONObject object = arrray.getJSONObject(i);
                                        //Toast.makeText(ShippingActivity.this, object.toString() + " ",  Toast.LENGTH_SHORT).show();
                                        cities.add(object.getString("name"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayAdapter<String> adapter =new ArrayAdapter<String>(ShippingActivity.this,android.R.layout.simple_spinner_item, cities);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                input_city.setAdapter(adapter);
                                input_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        city = cities.get(i);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        if(countries.size() > 0){
                                            country = countries.get(0);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(ANError anError) {

                            }

                        });
                Spinner input_country = dialogLayout.findViewById(R.id.input_country);
                AndroidNetworking.get(COUNTRIES_API)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arrray = response.getJSONArray("data");
                                    for(int i = 0 ; i < arrray.length() ; i++){
                                        JSONObject object = arrray.getJSONObject(i);
                                       // Toast.makeText(ShippingActivity.this, object.toString() + " ",  Toast.LENGTH_SHORT).show();
                                        countries.add(object.getString("name"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayAdapter<String> adapter =new ArrayAdapter<String>(ShippingActivity.this,android.R.layout.simple_spinner_item, countries);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                input_country.setAdapter(adapter);
                                input_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        country = countries.get(i);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        if(countries.size() > 0){
                                            country = countries.get(0);
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onError(ANError anError) {

                            }

                        });





                //Setting message manually and performing action on button click
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText input_address = dialogLayout.findViewById(R.id.input_address);

                        Spinner input_country = dialogLayout.findViewById(R.id.input_country);
                        EditText input_postal_code = dialogLayout.findViewById(R.id.input_postal_code);
                        EditText input_phone = dialogLayout.findViewById(R.id.input_phone);

                        Boolean isValid = true;

                        if(input_address.getText().toString().length() <= 0){
                            TextInputLayout til = dialogLayout.findViewById(R.id.input_address_layout);
                            til.setError("Address is required");
                            isValid = false;
                        }
//                        if(input_city.getText().toString().length() <= 0){
//                            TextInputLayout til = dialogLayout.findViewById(R.id.input_city_layout);
//                            til.setError("City is required");
//                            isValid = false;
//                        }

                        if(input_postal_code.getText().toString().length() <= 0){
                            TextInputLayout til = dialogLayout.findViewById(R.id.input_postal_code_layout);
                            til.setError("Mobile number is required");
                            isValid = false;
                        }

//                        if(input_country.getText().toString().length() <= 0){
//                            TextInputLayout til = dialogLayout.findViewById(R.id.input_country_layout);
//                            til.setError("Country is required");
//                            isValid = false;
//                        }

                        if(input_phone.getText().toString().length() <= 0){
                            TextInputLayout til = dialogLayout.findViewById(R.id.input_phone_layout);
                            til.setError("Phone number is required");
                            isValid = false;
                        }

                        if (isValid){
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("address", input_address.getText().toString());
                            jsonObject.addProperty("city", city);
                            jsonObject.addProperty("country", country);
                            jsonObject.addProperty("phone", input_phone.getText().toString());
                            jsonObject.addProperty("postal_code", input_postal_code.getText().toString());
                            jsonObject.addProperty("user_id", authResponse.getUser().getId());

                            new AccountInfoPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), ShippingActivity.this).addNewShippingRequest(jsonObject, authResponse.getAccessToken());

                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();


                            }
                        });

                alert = builder.create();
                alert.setTitle("Shipping Information");
                alert.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });

                alert.show();
            }
        });






        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shippingAddress != null){
                    tempTax = 0;
                    progressDialog.setMessage("Processing your Shipping Cost...");
                    progressDialog.show();
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);


                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", authResponse.getUser().getName());
                    jsonObject.addProperty("email", authResponse.getUser().getEmail());
                    jsonObject.addProperty("address", shippingAddress.getAddress());
                    jsonObject.addProperty("country", shippingAddress.getCountry());
                    jsonObject.addProperty("city", shippingAddress.getCity());
                    jsonObject.addProperty("postal_code", shippingAddress.getPostalCode());
                    jsonObject.addProperty("phone", shippingAddress.getPhone());
                    jsonObject.addProperty("checkout_type", "logged");

                    intent.putExtra("shipping_address", jsonObject.toString());
                    intent.putExtra("city", shippingAddress.getCity());

                    for(int i = products.size() - 1 ; i < products.size() ; i++) {
                        int finalI = i;

                       // Toast.makeText(ShippingActivity.this, String.valueOf(products.get(i)) + " " + shippingAddress.getCity() + " " + shippingAddress.getCountry(), Toast.LENGTH_SHORT).show();
                        AndroidNetworking.post(AppConfig.SHIPPING_RATE_API)
                                .addQueryParameter("product_id",String.valueOf(products.get(finalI)))
                                .addQueryParameter("city_name" , shippingAddress.getCity().trim())
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                      //  Toast.makeText(ShippingActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                                      //  Toast.makeText(ShippingActivity.this, "ID - > " + String.valueOf(products.get(finalI)), Toast.LENGTH_SHORT).show();
                                        try{
                                            JSONObject object = response.getJSONObject("shipping_cost");
                                            String type = response.getString("shipping_method");
                                            tempTax += object.getDouble("cost");
                                            if(type.equals("Product Wise Shipping") || type.equals("Product Wise Shipping")){

                                            }else {
                                                progressDialog.cancel();
                                                intent.putExtra("total", total + tempTax);
                                                intent.putExtra("shipping", tempTax);
                                                intent.putExtra("tax", tax);
                                                startActivity(intent);
                                            }


                                        }catch (JSONException e){
                                            try {
                                                tempTax += response.getDouble("shipping_cost");
                                            } catch (JSONException jsonException) {
                                                jsonException.printStackTrace();
                                            }
                                        }


                                        if(finalI == products.size()-1){
                                            progressDialog.dismiss();
                                            intent.putExtra("total", total + tempTax);
                                            intent.putExtra("shipping", tempTax);
                                            intent.putExtra("tax", tax);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                    //sendRequest();
                }
                else {

                    CustomToast.showToast(ShippingActivity.this, "Please choose shipping address.", R.color.colorWarning);
                }
            }
        });
    }

    @Override
    public void onProfileInfoUpdated(ProfileInfoUpdateResponse profileInfoUpdateResponse) {

    }

    @Override
    public void setShippingAddresses(List<ShippingAddress> shippingAddresses) {
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getApplicationContext(), 10)) );
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        ShippingAddressSelectAdapter adapter = new ShippingAddressSelectAdapter(getApplicationContext(), shippingAddresses, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onShippingInfoCreated(ShippingInfoResponse shippingInfoResponse) {

    }

    @Override
    public void onShippingInfoDeleted(ShippingInfoResponse shippingInfoResponse) {

    }

    @Override
    public void setUpdatedUserInfo(User user) {

    }

    @Override
    public void onShippingAddressItemClick(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
