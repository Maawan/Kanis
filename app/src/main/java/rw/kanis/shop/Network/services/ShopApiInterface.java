package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.ShopResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ShopApiInterface {
    @GET
    Call<ShopResponse> getShopDetails(@Url String url);
}
