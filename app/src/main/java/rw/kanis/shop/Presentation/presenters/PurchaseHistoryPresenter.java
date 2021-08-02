package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Models.PurchaseHistory;
import rw.kanis.shop.Presentation.ui.activities.PurchaseHistoryView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.PurchaseHistoryInteractor;
import rw.kanis.shop.domain.interactors.impl.PurchaseHistoryInteractorImpl;

import java.util.List;

public class PurchaseHistoryPresenter extends AbstractPresenter implements PurchaseHistoryInteractor.CallBack {
    private PurchaseHistoryView purchaseHistoryView;

    public PurchaseHistoryPresenter(Executor executor, MainThread mainThread, PurchaseHistoryView purchaseHistoryView) {
        super(executor, mainThread);
        this.purchaseHistoryView = purchaseHistoryView;
    }

    public void getPurchaseHistoryItems(int id, String token) {
        new PurchaseHistoryInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    @Override
    public void onPurchaseHistoryLodaded(List<PurchaseHistory> purchaseHistories) {
        if (purchaseHistoryView != null){
            purchaseHistoryView.onPurchaseHistoryLoaded(purchaseHistories);
        }
    }

    @Override
    public void onPurchaseHistoryLodadedError() {

    }
}
