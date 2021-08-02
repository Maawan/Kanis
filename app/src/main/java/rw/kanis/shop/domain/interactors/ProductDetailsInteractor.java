package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.ProductDetails;

public interface ProductDetailsInteractor {
    interface CallBack {

        void onProductDetailsDownloaded(ProductDetails productDetails);

        void onProductDetailsDownloadError();
    }
}
