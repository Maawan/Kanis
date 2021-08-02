package rw.kanis.shop.Network.services;

import rw.kanis.shop.Models.Banner;
import rw.kanis.shop.Network.response.BannerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerApiInterface {
    @GET("banners")
    Call<BannerResponse> getBanners();
}
