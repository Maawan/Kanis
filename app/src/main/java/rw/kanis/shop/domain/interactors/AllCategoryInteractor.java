package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Category;

import java.util.List;

public interface AllCategoryInteractor {
    interface CallBack {

        void onAllCategoriesDownloaded(List<Category> categories);

        void onAllCategoriesDownloadError();
    }
}
