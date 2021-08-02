package rw.kanis.shop.Models;

import com.google.gson.annotations.SerializedName;

public class BannerDataLinks {
    @SerializedName("url")
    private String url ;
    @SerializedName("data-src")
    private String dataSource ;


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
}
