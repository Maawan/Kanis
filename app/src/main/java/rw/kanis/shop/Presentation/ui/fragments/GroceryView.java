package rw.kanis.shop.Presentation.ui.fragments;

import java.util.List;

import rw.kanis.shop.Models.BannerData;

public interface GroceryView {

    void setBannerCategories(List<BannerData> list);

    void setBannerCategoriesError(Throwable t);
}
