package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.SubCategory;
import rw.kanis.shop.Presentation.ui.listeners.SubCategoryClickListener;
import rw.kanis.shop.R;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private List<SubCategory> subCategories;
    private LayoutInflater mInflater;
    private SubCategoryClickListener subCategoryClickListener;
    private ImageButton cart, search;



    // data is passed into the constructor
    public SubCategoryAdapter(Context context, List<SubCategory> subCategories, SubCategoryClickListener subCategoryClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.subCategories = subCategories;
        this.subCategoryClickListener = subCategoryClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sub_category_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(subCategories.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.subcategory_name);
//            recyclerView = itemView.findViewById(R.id.subsubcategory_list);
        }

        public void bind(final SubCategory subCategory) {
            textView.setText(subCategory.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subCategoryClickListener.onSubCategoryItemClick(subCategory);
                }
            });
//            LinearLayoutManager linearLayoutManager
//                    = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            SubSubCategoryAdapter adapter = new SubSubCategoryAdapter(context, subCategory.getSubSubCategories().getData());
//            recyclerView.setAdapter(adapter);
        }
    }
}
