package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Models.SubCategory;
import rw.kanis.shop.Presentation.ui.activities.SubCategoryView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.SubSubCategoryInteractor;
import rw.kanis.shop.domain.interactors.impl.SubSubCategoryInteractorImpl;

import java.util.List;

public class SubSubCategoryPresenter extends AbstractPresenter implements SubSubCategoryInteractor.CallBack {
    private SubCategoryView subSubCategoryView;

    public SubSubCategoryPresenter(Executor executor, MainThread mainThread, SubCategoryView subSubCategoryView) {
        super(executor, mainThread);
        this.subSubCategoryView =subSubCategoryView;
    }

    public void getSubSubCategories(String url){
        new SubSubCategoryInteractorImpl(mExecutor, mMainThread, this, url).execute();
    }

    @Override
    public void onSubSubCategoriesDownloaded(List<SubCategory> subCategories) {
        if (subSubCategoryView != null){
            subSubCategoryView.setSubCategories(subCategories);
        }
    }

    @Override
    public void onSubSubCategoriesDownloadError() {

    }
}
