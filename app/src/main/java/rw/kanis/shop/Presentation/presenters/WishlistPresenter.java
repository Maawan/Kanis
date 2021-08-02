package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Models.WishlistModel;
import rw.kanis.shop.Presentation.ui.activities.WishlistView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.WishlistInteractor;
import rw.kanis.shop.domain.interactors.impl.WishlistInteractorImpl;

import java.util.List;

public class WishlistPresenter extends AbstractPresenter implements WishlistInteractor.CallBack {
    private WishlistView wishlistView;

    public WishlistPresenter(Executor executor, MainThread mainThread, WishlistView wishlistView) {
        super(executor, mainThread);
        this.wishlistView = wishlistView;
    }

    public void getWishlistItems(int id, String token) {
        new WishlistInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    public void removeWishlistItem(int id, String token){
        new WishlistInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    @Override
    public void onWishlistLodaded(List<WishlistModel> wishlistModels) {
        if(wishlistView != null){
            wishlistView.setWishlistProducts(wishlistModels);
        }
    }

    @Override
    public void onWishlistError() {

    }
}
