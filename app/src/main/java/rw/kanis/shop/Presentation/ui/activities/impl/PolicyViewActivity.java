package rw.kanis.shop.Presentation.ui.activities.impl;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import rw.kanis.shop.Models.PolicyContent;
import rw.kanis.shop.Presentation.presenters.PolicyPresenter;
import rw.kanis.shop.Presentation.ui.activities.PolicyView;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

public class PolicyViewActivity extends BaseActivity implements PolicyView {
    private String title, url;
    private TextView policy_content;

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
        setContentView(R.layout.activity_web_view);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        initializeActionBar();
        setTitle(title);

        policy_content = findViewById(R.id.policy_content);

        new PolicyPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this).getPolicy(url);
    }

    @Override
    public void onPolicyContentLoaded(PolicyContent policyContent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            policy_content.setText(Html.fromHtml(policyContent.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            policy_content.setText(Html.fromHtml(policyContent.getContent()));
        }
    }
}
