package rw.kanis.shop.Presentation.ui.fragments.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rw.kanis.shop.Models.BannerData;
import rw.kanis.shop.Presentation.presenters.GroceryPresenter;
import rw.kanis.shop.Presentation.presenters.HomePresenter;
import rw.kanis.shop.Presentation.ui.adapters.BannerCategoryAdapter;
import rw.kanis.shop.Presentation.ui.fragments.GroceryView;
import rw.kanis.shop.R;
import rw.kanis.shop.Threading.MainThreadImpl;
import rw.kanis.shop.domain.executor.impl.ThreadExecutor;

public class groceryFragment extends Fragment implements GroceryView {
    private RecyclerView recycler;
    private GroceryPresenter groceryPresenter;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.grocery_fragment , container , false);
        recycler = view.findViewById(R.id.recycler);
        groceryPresenter = new GroceryPresenter(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this);
        groceryPresenter.getBannerCategories();

        return view;
    }

    @Override
    public void setBannerCategories(List<BannerData> list) {
        if(list == null){

        }else {

            for(int i = 0 ; i < list.size(); i++){


            }
            List<BannerData> list1 = list;
            for(int i = 0 ; i < list.size() ; i++){
//                if(list.get(i).getSlider_imgs().size() == 0){
//                    list.remove(i);
//                }
            }

            BannerCategoryAdapter bannerCategoryAdapter = new BannerCategoryAdapter(getContext() , list1);
            recycler.setAdapter(bannerCategoryAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        }
    }

    @Override
    public void setBannerCategoriesError(Throwable t) {

    }
}
