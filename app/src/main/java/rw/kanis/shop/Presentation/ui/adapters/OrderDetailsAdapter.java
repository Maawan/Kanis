package rw.kanis.shop.Presentation.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rw.kanis.shop.Models.OrderDetail;
import rw.kanis.shop.Presentation.ui.activities.OrderStatusDialogView;
import rw.kanis.shop.R;
import rw.kanis.shop.Utils.AppConfig;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private List<OrderDetail> orderDetails;
    private LayoutInflater mInflater;
    private Context context;
    private Dialog deliveryStatus;
    private ImageView closeBtn;
    private OrderStatusDialogView listener;

    // data is passed into the constructor
    public OrderDetailsAdapter(Context context, List<OrderDetail> orderDetails , OrderStatusDialogView listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_details_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview rw each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orderDetails.get(position));

//        deliveryStatus.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.slider_background));
//        confirmDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    // convenience method for getting data at click position
    public OrderDetail getItem(int id) {
        return orderDetails.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, product_variant, product_qty, product_price;
        Button delivery_status;


        ViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_variant = itemView.findViewById(R.id.product_variant);
            product_qty = itemView.findViewById(R.id.product_qty);
            product_price = itemView.findViewById(R.id.product_price);
            delivery_status = itemView.findViewById(R.id.delivery_status);


        }

        public void bind(OrderDetail orderDetail) {
            product_name.setText(orderDetail.getProduct());
            product_variant.setText(orderDetail.getVariation());
            product_qty.setText(orderDetail.getQuantity().toString());
            product_price.setText(AppConfig.convertPrice(context, orderDetail.getPrice()));

            delivery_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, orderDetail.getDeliveryStatus().toUpperCase()+"", Toast.LENGTH_SHORT).show();
                    listener.showDialog(orderDetail.getDeliveryStatus().toUpperCase());





                }
            });
           // delivery_status.setText(orderDetail.getDeliveryStatus().toUpperCase());
        }
    }
}

