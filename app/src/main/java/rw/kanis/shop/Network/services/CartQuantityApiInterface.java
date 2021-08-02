package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.CartQuantityUpdateResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CartQuantityApiInterface {
    @Headers("Content-Type: application/json")
    @POST("carts/change-quantity")
    Call<CartQuantityUpdateResponse> sendUpdateCartQuantutyRequest(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
