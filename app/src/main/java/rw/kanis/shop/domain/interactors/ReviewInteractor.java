package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.Review;

import java.util.List;

public interface ReviewInteractor {
    interface CallBack {

        void onReviewLodaded(List<Review> reviews);

        void onReviewError();
    }
}
