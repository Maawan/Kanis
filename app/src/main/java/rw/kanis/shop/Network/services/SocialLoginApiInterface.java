package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.AuthResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SocialLoginApiInterface {

    @POST("auth/social-login")
    Call<AuthResponse> sendSocialLoginCredentials(@Body JsonObject jsonObject);
}
