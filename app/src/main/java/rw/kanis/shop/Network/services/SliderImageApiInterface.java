package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.SliderImageResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SliderImageApiInterface {
    @GET("sliders")
    Call<SliderImageResponse> getSliderImages();
}
