package rw.kanis.shop.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BannerData implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("banner")
    private String banner;
    @SerializedName("icon")
    private String icon;
    @SerializedName("slider_imgs")
    private List<BannerDataLinks> slider_imgs = null;
    @SerializedName("products")
    private List<BannerProducts> products = null;
    @SerializedName("links")
    private CategoryLinks categoryLinks = null;

    public CategoryLinks getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(CategoryLinks categoryLinks) {
        this.categoryLinks = categoryLinks;
    }



    public String getName() {
        return name;
    }

    public List<BannerProducts> getProducts(){
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<BannerDataLinks> getSlider_imgs() {
        return slider_imgs;
    }

    public void setProducts(List<BannerProducts> products){
        this.products = products;
    }

    public void setSlider_imgs(List<BannerDataLinks> slider_imgs) {
        this.slider_imgs = slider_imgs;
    }
}
