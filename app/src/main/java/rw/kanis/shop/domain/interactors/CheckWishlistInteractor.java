package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.CheckWishlistResponse;

public interface CheckWishlistInteractor {
    interface CallBack {

        void onWishlistChecked(CheckWishlistResponse checkWishlistResponse);

        void onWishlistCheckedError();
    }
}
