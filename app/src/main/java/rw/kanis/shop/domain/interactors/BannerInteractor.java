package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Banner;

import java.util.List;

public interface BannerInteractor {
    interface CallBack {

        void onBannersDownloaded(List<Banner> banners);

        void onBannersDownloadError();
    }
}
