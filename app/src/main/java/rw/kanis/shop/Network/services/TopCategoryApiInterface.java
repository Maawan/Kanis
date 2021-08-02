package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopCategoryApiInterface {
    @GET("categories/featured")
    Call<CategoryResponse> getTopCategories();
}
