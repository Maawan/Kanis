package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Brand;

import java.util.List;

public interface BrandInteractor {
    interface CallBack {

        void onBrandsDownloaded(List<Brand> brands);

        void onBrandsDownloadError();
    }
}
