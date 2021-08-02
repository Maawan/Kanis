package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.OrderDetail;

import java.util.List;

public interface PurchaseHistoryDetailsView {
    void onOrderDetailsLoaded(List<OrderDetail> orderDetailList);
}
