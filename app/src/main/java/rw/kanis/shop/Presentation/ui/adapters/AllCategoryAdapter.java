package rw.kanis.shop.Presentation.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.Category;
import rw.kanis.shop.Presentation.ui.listeners.AllCategoryClickListener;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;
import com.bumptech.glide.Glide;

import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> mCategories;
    private LayoutInflater mInflater;
    private AllCategoryClickListener mClickListener;

    // data is passed into the constructor
    public AllCategoryAdapter(Context context, List<Category> categories, AllCategoryClickListener listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mCategories = categories;
        this.mClickListener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mCategories.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_icon);
            textView = itemView.findViewById(R.id.category_name);
        }

        public void bind(final Category category) {
            Glide.with(context).load(AppConfig.ASSET_URL + category.getIcon()).into(imageView);
            textView.setText(category.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onCategoryClick(category);
                }
            });
        }
    }
}
