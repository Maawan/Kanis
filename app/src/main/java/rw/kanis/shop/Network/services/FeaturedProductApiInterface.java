package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeaturedProductApiInterface {
    @GET("products/featured")
    Call<ProductResponse> getFeaturedPrdocuts();
}
