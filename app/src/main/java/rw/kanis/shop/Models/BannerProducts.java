package rw.kanis.shop.Models;

import com.google.gson.annotations.SerializedName;

public class BannerProducts {
    @SerializedName("url")
    private String url;
    @SerializedName("data-src")
    private String dataSource;
    @SerializedName("price")
    private String price;
    @SerializedName("name")
    private String name;

    @SerializedName("details")


    public String specificAPIurl;

    @SerializedName("base_discounted_price")
    private double base_discounted_price;

    public double getBase_discounted_price() {
        return base_discounted_price;
    }

    public void setBase_discounted_price(double base_discounted_price) {
        this.base_discounted_price = base_discounted_price;
    }



    public String getSpecificAPIurl(){
        return specificAPIurl;
    }

    public void setSpecificAPIurl(String url){
        this.specificAPIurl = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
