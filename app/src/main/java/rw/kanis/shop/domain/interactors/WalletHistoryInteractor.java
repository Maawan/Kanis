package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Wallet;

import java.util.List;

public interface WalletHistoryInteractor {
    interface CallBack {

        void onWalletHistoryLodaded(List<Wallet> walletList);

        void onWalletHistoryLoadError();
    }
}
