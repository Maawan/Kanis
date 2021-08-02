package rw.kanis.shop.Presentation.ui.fragments;

import rw.kanis.shop.Models.AuctionProduct;
import rw.kanis.shop.Models.Banner;
import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Models.Brand;
import rw.kanis.shop.Models.Category;
import rw.kanis.shop.Models.FlashDeal;
import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.SliderImage;
import rw.kanis.shop.Network.response.AppSettingsResponse;
import rw.kanis.shop.Network.response.AuctionBidResponse;

import java.util.List;

public interface HomeView {
    void onAppSettingsLoaded(AppSettingsResponse appSettingsResponse);

    void setSliderImages(List<SliderImage> sliderImages);

    void setHomeCategories(List<Category> categories);

    void setTodaysDeal(List<Product> products);

    void setFlashDeal(FlashDeal flashDeal);

    void setBanners(List<Banner> banners);

    void setBestSelling(List<Product> products);

    void setFeaturedProducts(List<Product> products);

    void setTopCategories(List<Category> categories);

    void setPopularBrands(List<Brand> brands);

    void setAuctionProducts(List<AuctionProduct> auctionProducts);

    void onAuctionBidSubmitted(AuctionBidResponse auctionBidResponse);

    void setBannerCategories(List<BannerData> list);

    void setBannerCategoriesError(Throwable t);
}