package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.SubCategory;

import java.util.List;

public interface SubSubCategoryInteractor {
    interface CallBack {

        void onSubSubCategoriesDownloaded(List<SubCategory> subCategories);

        void onSubSubCategoriesDownloadError();
    }
}
