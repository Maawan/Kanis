package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Network.response.AddToCartResponse;
import rw.kanis.shop.Network.response.VariantResponse;

public interface BuyingOptionView {
    void setVariantprice(VariantResponse variantResponse);
    void setAddToCartMessage(AddToCartResponse addToCartResponse);
}
