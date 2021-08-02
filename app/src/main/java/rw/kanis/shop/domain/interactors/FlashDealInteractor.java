package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.FlashDeal;

public interface FlashDealInteractor {
    interface CallBack {

        void onFlashDealProductDownloaded(FlashDeal flashDeal);

        void onFlashDealProductDownloadError();
    }
}