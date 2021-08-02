package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.SliderImage;

import java.util.List;

public interface SliderInteractor {

    interface CallBack {

        void onSliderDownloaded(List<SliderImage> sliderImages);

        void onSliderDownloadError();
    }
}
