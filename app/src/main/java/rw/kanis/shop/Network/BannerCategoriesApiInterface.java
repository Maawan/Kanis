package rw.kanis.shop.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import rw.kanis.shop.Network.response.BannerCategoryResponse;
import rw.kanis.shop.Network.response.BannerResponse;

public interface BannerCategoriesApiInterface {
    @GET("category/grocery")
    Call<BannerCategoryResponse> getBannerResponse();
}
