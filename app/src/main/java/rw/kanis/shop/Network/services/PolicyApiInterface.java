package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.PolicyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PolicyApiInterface {
    @GET
    Call<PolicyResponse> getPolicy(@Url String url);
}
