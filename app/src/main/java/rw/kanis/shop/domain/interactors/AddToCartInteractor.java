package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.AddToCartResponse;

public interface AddToCartInteractor {
    interface CallBack {

        void onCartItemAdded(AddToCartResponse addToCartResponse);

        void onCartItemAddedError();
    }
}
