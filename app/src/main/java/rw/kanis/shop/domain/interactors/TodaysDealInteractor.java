package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Product;

import java.util.List;

public interface TodaysDealInteractor {
    interface CallBack {

        void onTodaysDealProductDownloaded(List<Product> products);

        void onTodaysDealProductDownloadError();
    }
}
