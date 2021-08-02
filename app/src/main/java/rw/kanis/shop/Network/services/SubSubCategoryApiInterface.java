package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.SubSubCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SubSubCategoryApiInterface {
    @GET
    Call<SubSubCategoryResponse> getSubSubcategories(@Url String url);
}
