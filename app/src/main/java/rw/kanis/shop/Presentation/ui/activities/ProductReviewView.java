package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.Review;

import java.util.List;

public interface ProductReviewView {
    void onReviewsLoaded(List<Review> reviews);
}
