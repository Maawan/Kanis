package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.PurchaseHistory;

import java.util.List;

public interface PurchaseHistoryView {
    void onPurchaseHistoryLoaded(List<PurchaseHistory> purchaseHistoryList);
}
