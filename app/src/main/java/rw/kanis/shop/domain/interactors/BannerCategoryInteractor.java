package rw.kanis.shop.domain.interactors;

import java.util.List;

import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Models.Product;

public interface BannerCategoryInteractor {
    public interface CallBack {

        void onBannerDataDownloaded(List<BannerData> bannerData);

        void onBannerDataDownloadError(Throwable T);
    }
}
