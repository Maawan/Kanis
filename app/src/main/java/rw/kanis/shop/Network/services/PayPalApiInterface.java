package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.OrderResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PayPalApiInterface {
    @Headers("Content-Type: application/json")
    @POST("payments/pay/paypal")
    Call<OrderResponse> sendPlaceOrderRequest(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
