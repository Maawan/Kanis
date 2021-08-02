package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Network.response.CouponResponse;
import rw.kanis.shop.Network.response.OrderResponse;

public interface PaymentView {
    void onCouponApplied(CouponResponse couponResponse);
    void onOrderSubmitted(OrderResponse orderResponse);
}
