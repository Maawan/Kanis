package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.AppSettingsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppSettingsApiInterface {
    @GET("settings")
    Call<AppSettingsResponse> getAppSettings();
}
