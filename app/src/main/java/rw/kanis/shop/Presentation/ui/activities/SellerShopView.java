package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.Shop;

import java.util.List;

public interface SellerShopView {
    void onShopDetailsLoaded(Shop shop);
    void setFeaturedProducts(List<Product> products);
    void setTopSellingProducts(List<Product> products);
    void setNewProducts(List<Product> products);
}
