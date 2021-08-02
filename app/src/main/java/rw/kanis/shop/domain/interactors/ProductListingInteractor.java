package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Network.response.ProductListingResponse;

public interface ProductListingInteractor {
    interface CallBack {

        void onProductDownloaded(ProductListingResponse productListingResponse);

        void onProductDownloadError();
    }
}
