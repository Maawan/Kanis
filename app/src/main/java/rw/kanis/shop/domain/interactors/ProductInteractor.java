package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Product;

import java.util.List;

public interface ProductInteractor {
    interface CallBack {

        void onProductDownloaded(List<Product> products);

        void onProductDownloadError();
    }
}
