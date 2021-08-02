package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.UserBid;

import java.util.List;

public interface UserBidInteractor {
    interface CallBack {

        void onUserBidLodaded(List<UserBid> userBids);

        void onUserBidError();
    }
}
