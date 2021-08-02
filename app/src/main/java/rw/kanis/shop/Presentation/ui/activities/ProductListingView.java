package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Network.response.ProductListingResponse;

public interface ProductListingView {
    void setProducts(ProductListingResponse productListingResponse);
}
