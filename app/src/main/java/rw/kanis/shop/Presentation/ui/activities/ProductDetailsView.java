package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.Product;
import rw.kanis.shop.Models.ProductDetails;
import rw.kanis.shop.Network.response.AddToCartResponse;
import rw.kanis.shop.Network.response.AddToWishlistResponse;
import rw.kanis.shop.Network.response.CheckWishlistResponse;
import rw.kanis.shop.Network.response.RemoveWishlistResponse;

import java.util.List;

public interface ProductDetailsView {
    void setProductDetails(ProductDetails productDetails);
    void setRelatedProducts(List<Product> relatedProducts);
    void setTopSellingProducts(List<Product> topSellingProducts);
    void setAddToCartMessage(AddToCartResponse addToCartResponse);
    void setAddToWishlistMessage(AddToWishlistResponse addToWishlistMessage);
    void onCheckWishlist(CheckWishlistResponse checkWishlistResponse);
    void onRemoveFromWishlist(RemoveWishlistResponse removeWishlistResponse);
}
