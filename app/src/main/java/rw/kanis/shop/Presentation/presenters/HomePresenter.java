package rw.kanis.shop.Presentation.presenters;

import android.widget.Toast;

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
import rw.kanis.shop.Presentation.ui.fragments.HomeView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.AppSettingsInteractor;
import rw.kanis.shop.domain.interactors.AuctionBidInteractor;
import rw.kanis.shop.domain.interactors.AuctionProductInteractor;
import rw.kanis.shop.domain.interactors.BannerCategoryInteractor;
import rw.kanis.shop.domain.interactors.BannerInteractor;
import rw.kanis.shop.domain.interactors.BestSellingInteractor;
import rw.kanis.shop.domain.interactors.BrandInteractor;
import rw.kanis.shop.domain.interactors.FeaturedProductInteractor;
import rw.kanis.shop.domain.interactors.FlashDealInteractor;
import rw.kanis.shop.domain.interactors.HomeCategoriesInteractor;
import rw.kanis.shop.domain.interactors.SliderInteractor;
import rw.kanis.shop.domain.interactors.TodaysDealInteractor;
import rw.kanis.shop.domain.interactors.TopCategoryInteractor;
import rw.kanis.shop.domain.interactors.impl.AppSettingsInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.AuctionBidInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.AuctionProductInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.BannerCategoriesHomeImpl;
import rw.kanis.shop.domain.interactors.impl.BannerCategoriesImpl;
import rw.kanis.shop.domain.interactors.impl.BannerInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.BestSellingInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.BrandInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.FeaturedProductInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.FlashDealInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.HomeCategoriesInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.SliderInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.TodaysDealInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.TopCategoriesInteractorImpl;
import com.google.gson.JsonObject;

import java.util.List;

public class HomePresenter extends AbstractPresenter implements AppSettingsInteractor.CallBack, SliderInteractor.CallBack, HomeCategoriesInteractor.CallBack, TodaysDealInteractor.CallBack, FlashDealInteractor.CallBack, BestSellingInteractor.CallBack, BannerInteractor.CallBack, FeaturedProductInteractor.CallBack, BrandInteractor.CallBack, TopCategoryInteractor.CallBack, AuctionProductInteractor.CallBack, AuctionBidInteractor.CallBack, BannerCategoryInteractor.CallBack {
    private HomeView homeView;

    public HomePresenter(Executor executor, MainThread mainThread, HomeView homeView) {
        super(executor, mainThread);
        this.homeView = homeView;
    }

    public void getAppSettings(){
        new AppSettingsInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getSliderImages() {
        new SliderInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getHomeCategories() {
        new HomeCategoriesInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getTodaysDeal() {
        new TodaysDealInteractorImpl(mExecutor, mMainThread, this).execute();
    }
    public void getBannerCategories(){
        new BannerCategoriesHomeImpl(mExecutor,mMainThread,this).execute();

    }

    public void getFlashDeal() {
        new FlashDealInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getBestSelling() {
        new BestSellingInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getBanners() {
        new BannerInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getFeaturedProducts() {
        new FeaturedProductInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getTopCategories() {
        new TopCategoriesInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getPopularbrands() {
        new BrandInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void getAuctionProducts() {
        new AuctionProductInteractorImpl(mExecutor, mMainThread, this).execute();
    }

    public void submitBid(JsonObject jsonObject, String token){
        new AuctionBidInteractorImpl(mExecutor, mMainThread, this, jsonObject, token).execute();
    }
    public void getCategoriesBanner(){

    }

    @Override
    public void onAppSettingsLoaded(AppSettingsResponse appSettingsResponse) {
        if (homeView != null){
            homeView.onAppSettingsLoaded(appSettingsResponse);
        }
    }

    @Override
    public void onAppSettingsLoadedError() {

    }

    @Override
    public void onSliderDownloaded(List<SliderImage> sliderImages) {
        if (homeView != null) {
            homeView.setSliderImages(sliderImages);
        }
    }

    @Override
    public void onSliderDownloadError() {

    }

    @Override
    public void onHomeCategoriesDownloaded(List<Category> categories) {
        if (homeView != null) {
            homeView.setHomeCategories(categories);
        }
    }

    @Override
    public void onHomeCategoriesDownloadError() {

    }

    @Override
    public void onTodaysDealProductDownloaded(List<Product> products) {
        if (homeView != null) {
            homeView.setTodaysDeal(products);
        }
    }

    @Override
    public void onTodaysDealProductDownloadError() {

    }

    @Override
    public void onBestSellingProductDownloaded(List<Product> products) {
        if (homeView != null) {
            homeView.setBestSelling(products);
        }
    }

    @Override
    public void onBestSellingProductDownloadError() {

    }

    @Override
    public void onFeaturedProductDownloaded(List<Product> products) {
        if (homeView != null) {
            homeView.setFeaturedProducts(products);
        }
    }

    @Override
    public void onFeaturedProductDownloadError() {

    }

    @Override
    public void onTopCategoriesDownloaded(List<Category> categories) {
        if (homeView != null) {
            homeView.setTopCategories(categories);
        }
    }

    @Override
    public void onTopCategoriesDownloadError() {

    }

    @Override
    public void onAuctionProductDownloaded(List<AuctionProduct> auctionProducts) {
        if (homeView != null) {
            homeView.setAuctionProducts(auctionProducts);
        }
    }

    @Override
    public void onAuctionProductDownloadError() {

    }

    @Override
    public void onBannersDownloaded(List<Banner> banners) {
        if (homeView != null){
            homeView.setBanners(banners);
        }
    }

    @Override
    public void onBannersDownloadError() {

    }

    @Override
    public void onBrandsDownloaded(List<Brand> brands) {
        if (homeView != null){
            homeView.setPopularBrands(brands);
        }
    }

    @Override
    public void onBrandsDownloadError() {

    }

    @Override
    public void onBidSubmitted(AuctionBidResponse auctionBidResponse) {
        if (homeView != null){
            homeView.onAuctionBidSubmitted(auctionBidResponse);
        }
    }

    @Override
    public void onBidSubmittedError() {

    }

    @Override
    public void onFlashDealProductDownloaded(FlashDeal flashDeal) {
        if (homeView != null) {
            homeView.setFlashDeal(flashDeal);
        }
    }

    @Override
    public void onFlashDealProductDownloadError() {

    }

    @Override
    public void onBannerDataDownloaded(List<BannerData> bannerData) {
        if(homeView != null){
            homeView.setBannerCategories(bannerData);
        }

    }

    @Override
    public void onBannerDataDownloadError(Throwable T) {
        if(homeView != null){
            homeView.setBannerCategoriesError(T);
        }
    }


}