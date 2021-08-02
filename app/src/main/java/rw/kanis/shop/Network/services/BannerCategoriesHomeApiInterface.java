package rw.kanis.shop.Network.services;

import retrofit2.Call;
import retrofit2.http.GET;
import rw.kanis.shop.Network.response.BannerCategoryResponse;

public interface BannerCategoriesHomeApiInterface {
    @GET("category/home")
    Call<BannerCategoryResponse> getBannerResponse();
}
