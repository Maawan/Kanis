package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.RemoveWishlistResponse;

public interface RemoveWishlistInteractor {
    interface CallBack {

        void onWishlistItemRemoved(RemoveWishlistResponse removeWishlistResponse);

        void onWishlistItemRemovedError();
    }
}
