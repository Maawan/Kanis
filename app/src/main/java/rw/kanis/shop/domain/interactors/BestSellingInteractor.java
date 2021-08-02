package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Product;

import java.util.List;

public interface BestSellingInteractor {
    interface CallBack {

        void onBestSellingProductDownloaded(List<Product> products);

        void onBestSellingProductDownloadError();
    }
}
