package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.Models.Wallet;
import rw.kanis.shop.Presentation.ui.activities.WalletView;
import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;
import rw.kanis.shop.domain.interactors.WalletBalanceInteractor;
import rw.kanis.shop.domain.interactors.WalletHistoryInteractor;
import rw.kanis.shop.domain.interactors.impl.WalletBalanceInteractorImpl;
import rw.kanis.shop.domain.interactors.impl.WalletHistoryInteractorImpl;

import java.util.List;

public class WalletPresenter extends AbstractPresenter implements WalletBalanceInteractor.CallBack, WalletHistoryInteractor.CallBack {
    private WalletView walletView;

    public WalletPresenter(Executor executor, MainThread mainThread, WalletView walletView) {
        super(executor, mainThread);
        this.walletView = walletView;
    }

    public void getWalletBalance(int id, String token) {
        new WalletBalanceInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    public void getWalletHistory(int id, String token) {
        new WalletHistoryInteractorImpl(mExecutor, mMainThread, this, id, token).execute();
    }

    @Override
    public void onWalletBalanceLodaded(Double balance) {
        if (walletView != null){
            walletView.setWalletBalance(balance);
        }
    }

    @Override
    public void onWalletBalanceLoadError() {

    }

    @Override
    public void onWalletHistoryLodaded(List<Wallet> walletList) {
        if(walletView != null){
            walletView.setWalletHistory(walletList);
        }
    }

    @Override
    public void onWalletHistoryLoadError() {

    }
}
