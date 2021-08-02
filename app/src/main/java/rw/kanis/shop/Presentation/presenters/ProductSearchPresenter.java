package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Network.response.ProductSearchResponse;
import rw.kanis.shop.Presentation.ui.fragments.ProductSearchView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.ProductSearchInteractor;
import rw.kanis.shop.domain.interactors.impl.ProductSearchInteractorImpl;

public class ProductSearchPresenter extends AbstractPresenter implements ProductSearchInteractor.CallBack {
    private ProductSearchView ProductSearchView;

    public ProductSearchPresenter(Executor executor, MainThread mainThread, ProductSearchView ProductSearchView) {
        super(executor, mainThread);
        this.ProductSearchView = ProductSearchView;
    }

    public void getSearchedProducts(String url) {
        new ProductSearchInteractorImpl(mExecutor, mMainThread, this, url).execute();
    }


    @Override
    public void onProductSearched(ProductSearchResponse productSearchResponse) {
        if (ProductSearchView != null){
            ProductSearchView.setSearchedProduct(productSearchResponse);
        }
    }

    @Override
    public void onProductSearchedError() {

    }
}
