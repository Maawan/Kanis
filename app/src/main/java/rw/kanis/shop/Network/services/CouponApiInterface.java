package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.CouponResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CouponApiInterface {
    @Headers("Content-Type: application/json")
    @POST("coupon/apply")
    Call<CouponResponse> getCouponResponse(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
