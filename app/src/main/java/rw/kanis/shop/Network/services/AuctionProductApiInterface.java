package rw.kanis.shop.Network.services;

import rw.kanis.shop.Network.response.AuctionResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuctionProductApiInterface {
    @GET("auctions")
    Call<AuctionResponse> getAuctionProducts();
}
