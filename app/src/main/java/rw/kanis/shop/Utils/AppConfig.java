package rw.kanis.shop.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import rw.kanis.shop.Models.AppSettings;
import java.text.DecimalFormat;

public class AppConfig {
    public static AppSettings appSettings;

    public static String BASE_URL = "https://www.kanis.rw/api/v1/";
    public static String ASSET_URL = "https://www.kanis.rw/public/";
    public static String TEMP_SERVER_URL = "https://www.kanis.rw/api/v1/";
    public static String PAYMENT_API = "https://kanis.rw/api/v1/payment_api";

//    public static String BASE_URL = "http://192.168.0.1/pshop/api/v1/";
//    public static String ASSET_URL = "http://192.168.0.1/pshop/public/";


    public static String BRAINTREE_KEY = "sandbox_pghddbzc_h44cx45wt7g27wmc";
    public static final String SHIPPING_RATE_API = "https://www.kanis.rw/api/v1/get_shipping_value";
    public  static boolean CASH_ON_DELIVERY = true;
    public  static boolean WALLET_USE = true;

    //public static String RAZORPAY_KEY = "rzp_live_U4tdfeoXhWGUMi8";
    //public static String Flutterwave_KEY = "flutterwav_stag_ewryhfejf733edw";

    public static String convertPrice(Context context, Double price) {
        appSettings = new UserPrefs(context).getAppSettingsPreferenceObjectJson("app_settings_response").getData().get(0);
        return appSettings.getCurrency().getSymbol() + new DecimalFormat("#,###").format(Double.parseDouble(String.valueOf(price*appSettings.getCurrency().getExchangeRate())));
    }

    public static AppSettings getAppSettings(Context context){
        return new UserPrefs(context).getAppSettingsPreferenceObjectJson("app_settings_response").getData().get(0);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value rw dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public static int convertDpToPixel (float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
