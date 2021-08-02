package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.AuctionBidResponse;

public interface AuctionBidInteractor {
    interface CallBack {

        void onBidSubmitted(AuctionBidResponse auctionBidResponse);

        void onBidSubmittedError();
    }
}
