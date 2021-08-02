package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.CartQuantityUpdateResponse;

public interface CartQuantityInteractor {
    interface CallBack {

        void onCartQuantityUpdated(CartQuantityUpdateResponse cartQuantityUpdateResponse);

        void onCartQuantityUpdatedError();
    }
}
