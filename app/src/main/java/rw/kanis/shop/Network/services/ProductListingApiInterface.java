package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.ProductListingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ProductListingApiInterface {
    @GET
    Call<ProductListingResponse> getProducts(@Url String url);
}
