package rw.kanis.shop.Presentation.ui.activities.impl;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;

public class ProductDescriptionActivity extends BaseActivity {
    private  String product_name, description;
    private WebView product_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight <= AppConfig.convertDpToPixel(24))
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_product_description);

        product_name = getIntent().getStringExtra("product_name");
        description = getIntent().getStringExtra("description");

        initializeActionBar();
        setTitle(product_name);

        initviews();
    }

    private void initviews(){
       /* product_description = findViewById(R.id.product_description);
        product_description.setInitialScale(1);
        //product_description.getSettings().setNeedInitialFocus(true);
       product_description.getSettings().setUseWideViewPort(true);
        product_description.getSettings().setLoadWithOverviewMode(true);
      //  product_description.getSettings().setSupportZoom(true);
        product_description.getSettings().setBuiltInZoomControls(true);
        product_description.getSettings().setDisplayZoomControls(true);
        //product_description.getSettings().setDefaultFontSize(32);

        product_description.loadDataWithBaseURL(null,description,"text/html", "UTF-8",null);*/

        product_description = findViewById(R.id.product_description);
        product_description.loadDataWithBaseURL(null,description,"text/html", "UTF-8",null);
       // product_description.loadData(description, "text/html", "UTF-8");
    }
}
