package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.RemoveCartResponse;

public interface RemoveCartInteractor {
    interface CallBack {

        void onCartItemRemoved(RemoveCartResponse removeCartResponse);

        void onCartItemRemovedError();
    }
}
