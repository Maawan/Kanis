package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.Wallet;

import java.util.List;

public interface WalletView {
    void setWalletBalance(Double balance);
    void setWalletHistory(List<Wallet> walletList);
}
