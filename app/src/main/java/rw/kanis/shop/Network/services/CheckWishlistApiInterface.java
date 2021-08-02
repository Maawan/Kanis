package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.CheckWishlistResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CheckWishlistApiInterface {
    @Headers("Content-Type: application/json")
    @POST("wishlists/check-product")
    Call<CheckWishlistResponse> checkWishlistRequest(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
