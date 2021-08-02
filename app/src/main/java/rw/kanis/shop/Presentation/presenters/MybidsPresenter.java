package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Models.UserBid;
import rw.kanis.shop.Network.response.AuctionBidResponse;
import rw.kanis.shop.Presentation.ui.activities.MybidView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.AuctionBidInteractor;
import rw.kanis.shop.domain.interactors.UserBidInteractor;
import rw.kanis.shop.domain.interactors.impl.AuctionBidInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.UserBidInteractorImpl;
import com.google.gson.JsonObject;

import java.util.List;

public class MybidsPresenter extends AbstractPresenter implements UserBidInteractor.CallBack, AuctionBidInteractor.CallBack {
    private MybidView mybidView;

    public MybidsPresenter(Executor executor, MainThread mainThread, MybidView mybidView) {
        super(executor, mainThread);
        this.mybidView = mybidView;
    }

    public void getUserBidsItems(int id, String token) {
        new UserBidInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    public void submitBid(JsonObject jsonObject, String token){
        new AuctionBidInteractorImpl(mExecutor, mMainThread, this, jsonObject, token).execute();
    }

    @Override
    public void onUserBidLodaded(List<UserBid> userBids) {
        if (mybidView != null){
            mybidView.setUserBids(userBids);
        }
    }

    @Override
    public void onUserBidError() {

    }

    @Override
    public void onBidSubmitted(AuctionBidResponse auctionBidResponse) {
        if (mybidView != null){
            mybidView.onAuctionBidSubmitted(auctionBidResponse);
        }
    }

    @Override
    public void onBidSubmittedError() {

    }
}
