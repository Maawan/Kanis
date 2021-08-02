package rw.kanis.shop.Presentation.presenters;

import java.util.List;

import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Presentation.ui.fragments.GroceryView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.BannerCategoryInteractor;
import rw.kanis.shop.domain.interactors.impl.BannerCategoriesImpl;

public class GroceryPresenter extends AbstractPresenter implements BannerCategoryInteractor.CallBack{
    private GroceryView groceryView;

    public GroceryPresenter(Executor executor, MainThread mainThread, GroceryView groceryView) {
        super(executor, mainThread);
        this.groceryView = groceryView;
    }

    public void getBannerCategories(){
        new BannerCategoriesImpl(mExecutor,mMainThread,this).execute();

    }

    @Override
    public void onBannerDataDownloaded(List<BannerData> bannerData) {
        if(groceryView != null){
            groceryView.setBannerCategories(bannerData);
        }
    }

    @Override
    public void onBannerDataDownloadError(Throwable T) {
        if(groceryView != null){
            groceryView.setBannerCategoriesError(T);
        }

    }
}
