package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.UserBid;
import rw.kanis.shop.Network.response.AuctionBidResponse;

import java.util.List;

public interface MybidView {
    void setUserBids(List<UserBid> userBids);
    void onAuctionBidSubmitted(AuctionBidResponse auctionBidResponse);
}
