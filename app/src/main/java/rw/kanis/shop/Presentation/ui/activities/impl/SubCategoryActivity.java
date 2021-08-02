package rw.kanis.shop.Presentation.ui.activities.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Category;
import rw.kanis.shop.Models.SubCategory;
import rw.kanis.shop.Presentation.presenters.SubSubCategoryPresenter;
import rw.kanis.shop.Presentation.ui.activities.SubCategoryView;
import rw.kanis.shop.Presentation.ui.adapters.SubCategoryAdapter;
import rw.kanis.shop.Presentation.ui.listeners.SubCategoryClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.List;

public class SubCategoryActivity extends BaseActivity implements SubCategoryView, SubCategoryClickListener {

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
        setContentView(R.layout.activity_sub_sub_category);

        Category category = (Category) getIntent().getSerializableExtra("category");

        initializeActionBar();
        setTitle(category.getName());

        SubSubCategoryPresenter subSubCategoryPresenter = new SubSubCategoryPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);
        subSubCategoryPresenter.getSubSubCategories(category.getLinks().getSubCategories());
    }

    @Override
    public void setSubCategories(List<SubCategory> subCategories) {
        RecyclerView recyclerView = findViewById(R.id.subcategory_list);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getApplicationContext(), 10)) );
        SubCategoryAdapter adapter = new SubCategoryAdapter(this, subCategories, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSubCategoryItemClick(SubCategory subCategory) {
        Intent intent = new Intent(this, ProductListingActivity.class);
        intent.putExtra("title", subCategory.getName());
        intent.putExtra("url", subCategory.getLinks().getProducts());
        startActivity(intent);
    }
}
