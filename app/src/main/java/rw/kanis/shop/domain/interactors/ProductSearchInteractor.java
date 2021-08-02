package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.ProductSearchResponse;

public interface ProductSearchInteractor {
    interface CallBack {

        void onProductSearched(ProductSearchResponse productSearchResponse);

        void onProductSearchedError();
    }
}
