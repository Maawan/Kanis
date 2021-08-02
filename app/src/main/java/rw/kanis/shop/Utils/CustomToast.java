package rw.kanis.shop.Utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rw.kanis.shop.R;

public class CustomToast {
    public static void showToast(Activity activity, String msg, int i){

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));

        //LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toast_layout_root);

        ImageView imageView = layout.findViewById(R.id.image1);
        ImageView imageView1 = layout.findViewById(R.id.image2);
        ImageView imageView3 = layout.findViewById(R.id.image3);
        TextView toastMessage = (TextView) layout.findViewById(R.id.toastMessage);

        imageView.setVisibility(View.GONE);
        imageView1.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.GONE);

        if(i == 1){
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.GONE);
            toastMessage.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }else if(i == 2){
            imageView.setVisibility(View.GONE);
            imageView1.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);
            toastMessage.setTextColor(activity.getResources().getColor(R.color.colorDanger));
        }
        else{
            imageView.setVisibility(View.GONE);
            imageView1.setVisibility(View.VISIBLE);
            toastMessage.setTextColor(activity.getResources().getColor(R.color.colorDanger));
        }


        //layout.setBackgroundColor(color);
        toastMessage.setText(msg);

        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setView(layout);
        toast.show();

//        CookieBar.dismiss(activity);
//        CookieBar.build(activity)
//                .setTitle(R.string.app_name)
//                .setMessage(msg)
//                .setBackgroundColor(color)
//                .setAnimationIn(R.anim.slide_in_top, R.anim.slide_in_top)
//                .setAnimationOut(R.anim.slide_out_top, R.anim.slide_out_top)
//                .setDuration(1000)
//                .show();
    }
}
