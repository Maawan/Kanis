package rw.kanis.shop.Presentation.ui.fragments.impl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.SearchProduct;
import rw.kanis.shop.Network.response.ProductSearchResponse;
import rw.kanis.shop.Presentation.presenters.ProductSearchPresenter;
import rw.kanis.shop.Presentation.ui.activities.impl.ProductDetailsActivity;
import rw.kanis.shop.Presentation.ui.adapters.ProductSearchAdapter;
import rw.kanis.shop.Presentation.ui.fragments.ProductSearchView;
import rw.kanis.shop.Presentation.ui.listeners.EndlessRecyclerOnScrollListener;
import rw.kanis.shop.Presentation.ui.listeners.SearchProductClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.Utils.AppConfig;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

import com.appyvet.materialrangebar.RangeBar;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ProductSearchFragment extends Fragment implements ProductSearchView , SearchProductClickListener {
    private View v;
    private int i =0;
    private ImageView collapseBtn;
    private long initial = 0 , finalValue = 50000000;
    private List<SearchProduct> mProducts = new ArrayList<>();
    private SearchView searchView;
    private RadioGroup radioScope;
    private ProductSearchAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private Button filterButton;
    private ExpandableRelativeLayout filterExpandLayout;
    private ProductSearchPresenter productSearchPresenter;
    private String url = AppConfig.BASE_URL+"products/search?key=&scope=product&page=1";
    private String key = "", scope = "product";
    private ProductSearchResponse mProductSearchResponse = null;
    private RangeBar rangeBar;
    private TextView priceRangeSelector;

    public static HashMap<String,Integer> priceRangeFilter;
    private int[] priceRange = {0,20000,40000,60000,80000,100000,120000,140000,160000,180000,5000000};

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search, null);
        priceRangeSelector = v.findViewById(R.id.priceRangeSelector);
        recyclerView = v.findViewById(R.id.product_list);
        filterButton = v.findViewById(R.id.filterButton);
        filterExpandLayout = v.findViewById(R.id.expandableLayout);
        rangeBar = v.findViewById(R.id.rangeSelector);
        priceRangeFilter = new HashMap<String,Integer>();
        collapseBtn = v.findViewById(R.id.arrowUpBtn);


        recyclerView.addItemDecoration(new LayoutMarginDecoration( 1,  AppConfig.convertDpToPx(getContext(), 10)) );

        progressBar = v.findViewById(R.id.item_progress_bar);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterExpandLayout.toggle();
            }
        });

        searchView = v.findViewById(R.id.search_key);
        //radioScope = v.findViewById(R.id.scope_radio);

        productSearchPresenter = new ProductSearchPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);

        //searchView.setQueryHint("I'm looking for...");
        collapseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterExpandLayout.collapse();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();     // Close keyboard on pressing IME_ACTION_SEARCH option
                key = query;
                searchProduct(key, scope);
                //load search query
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("Test", "QueryTextChange: "+ newText);
                if (newText.length() == 0){
                    key = "";
                    mProducts.clear();
                    searchProduct(key, scope);
                }
                else {
                    key = newText;
                    mProducts.clear();
                    searchProduct(key, scope);
                }
                return true;
            }
        });

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
               priceRangeSelector.setText(" Selected Range RF "+ priceRange[rangeBar.getLeftIndex()] + "  to RF " + priceRange[rangeBar.getRightIndex()]);
               initial = priceRange[rangeBar.getLeftIndex()];
               finalValue = priceRange[rangeBar.getRightIndex()];
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                //Toast.makeText(getContext(), rangeBar.getLeftIndex() + "  " + rangeBar.getRightIndex(), Toast.LENGTH_SHORT).show();
//                priceRangeFilter.put("Initial",priceRange[rangeBar.getLeftIndex()]);
//                priceRangeFilter.put("Final",priceRange[rangeBar.getRightIndex()]);
                initial = priceRange[rangeBar.getLeftIndex()];
                finalValue = priceRange[rangeBar.getRightIndex()];
                searchProduct(key,scope);


                Toast.makeText(getContext(), "Price Range is Selected from RF " + priceRange[rangeBar.getLeftIndex()] + " to RF " + priceRange[rangeBar.getRightIndex()], Toast.LENGTH_SHORT).show();
            }
        });



        searchProduct("", "product");

        return v;
    }

    private void searchProduct(String key, String scope){
        //Log.d("Test", scope);

        url = url.replace("key="+url.split("key=")[1].split("&")[0], "key="+key);
        url = url.replace("scope="+url.split("scope=")[1].split("&")[0], "scope="+scope.toLowerCase());
       // Toast.makeText(getContext(), "Current URL -> " + url, Toast.LENGTH_SHORT).show();
        Log.e("URL ->>>>>" , url);
       // url = "www."

        //Log.d("Test", url);

        mProducts.clear();

        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        adapter = new ProductSearchAdapter(getContext(), mProducts, this);
        adapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                addDataToList(mProductSearchResponse);
            }
        });



        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        productSearchPresenter.getSearchedProducts(url);
    }

    public void addDataToList(ProductSearchResponse productSearchResponse){
        if (productSearchResponse != null && productSearchResponse.getMeta() != null && !productSearchResponse.getMeta().getCurrentPage().equals(productSearchResponse.getMeta().getLastPage())){
            progressBar.setVisibility(View.VISIBLE);
            productSearchPresenter.getSearchedProducts(productSearchResponse.getLinks().getNext());
        }
    }

    @Override
    public void onProductItemClick(SearchProduct product) {
        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
        intent.putExtra("product_name", product.getName());
        intent.putExtra("link", product.getLinks().getDetails());
        intent.putExtra("top_selling", product.getLinks().getRelated());
        startActivity(intent);
    }

    @Override
    public void setSearchedProduct(ProductSearchResponse productSearchResponse) {
        Log.d("Test", String.valueOf(productSearchResponse.getMeta().getTotal()));
        mProducts.clear();
        List<SearchProduct> temp = productSearchResponse.getData();
        //for(int i = 0 ; i < temp.size();i++)
        //Toast.makeText(getContext(), temp.get(1).getName(), Toast.LENGTH_SHORT).show();

//        for(int i = 0 ; i < temp.size();i++){
//            Toast.makeText(getContext(), temp.get(i).getName(), Toast.LENGTH_LONG).show();
//            if(i == 7){
//                break;
//            }
//            temp.remove(i);
//        }
       // Toast.makeText(getContext(), temp.get(0).getName()+ " " + temp.get(0).getBasePrice(), Toast.LENGTH_SHORT).show();
        Iterator itr = temp.iterator();
        while (itr.hasNext()){
            //Toast.makeText(getContext(), "Initial Value -> " + initial + "  Final Value -> "+ finalValue, Toast.LENGTH_SHORT).show();
           SearchProduct obj = (SearchProduct) itr.next();
           if(obj.getBaseDiscountedPrice() < initial || obj.getBaseDiscountedPrice() > finalValue){
               itr.remove();



           }

        }


        mProducts.addAll(temp);

        mProductSearchResponse = productSearchResponse;
        progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }
}
