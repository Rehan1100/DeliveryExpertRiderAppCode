package com.DeliverExpertRiderApp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderItem;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.activities.ConfirmOrdersActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.expertorider.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConfrimOrderAdapter extends RecyclerView.Adapter<ConfrimOrderAdapter.ViewHolder> {

    ConfirmOrdersActivity confirmOrdersActivity;
    private ArrayList<OrderItem> orderDetailArrayList;
    private Context context;
    double totalPrice = 0;


    public ConfrimOrderAdapter(ArrayList<OrderItem> arrayList , Context context ){
        this.orderDetailArrayList=arrayList;
        this.context=context;
        confirmOrdersActivity = (ConfirmOrdersActivity) context;
    }

    @NonNull
    @Override
    public ConfrimOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_order_adapter_layout, parent, false);

        return new ConfrimOrderAdapter.ViewHolder(itemView,confirmOrdersActivity);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ConfrimOrderAdapter.ViewHolder holder, int position) {
        final OrderItem orderDetail = orderDetailArrayList.get(position);


        holder.OrderTitle.setText(orderDetail.getTitle());
        holder.OrderQty.setText("Qty : "+orderDetail.getQuantity());
        holder.OrderQrPrice.setText(CommonUtil.getCurrency() + NumberFormat.getNumberInstance(Locale.getDefault()).format((Double.parseDouble(orderDetail.getPrice())*Double.parseDouble(orderDetail.getQuantity()))));
        holder.OrderQrCutPrice.setText("Unit Price : "+NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(orderDetail.getPrice())));
        holder.OrderQrCutPrice.setVisibility(View.VISIBLE);
//        holder.OrderQrCutPrice.setPaintFlags(holder.OrderQrCutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.drawable.no_img);

        totalPrice = totalPrice + (Double.parseDouble(orderDetail.getPrice())*Double.parseDouble(orderDetail.getQuantity())) ;

        //confirmOrdersActivity.setTotal_item_price(totalPrice+"");

        Glide.with(context)
                .load(ApiConstants.BASE_IMAGE_URL+orderDetail.getImage())
                .apply(requestOptions)
                .into(holder.imageView);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent(context, DashboardActivity.class)
//                    .putExtra("order",orderDetail)
//                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView OrderTitle , OrderQrPrice ,OrderQrCutPrice,OrderQty ;
        public ImageView imageView;
        public LinearLayout parentLayout;
        public ConfirmOrdersActivity confirmOrdersActivity;


        public ViewHolder(@NonNull View itemView, ConfirmOrdersActivity confirmOrdersActivity) {
            super(itemView);

            OrderQrCutPrice =  itemView.findViewById(R.id.cut_price);
            OrderTitle =  itemView.findViewById(R.id.order_title);
            OrderQrPrice =  itemView.findViewById(R.id.qr_price);
            OrderQty =  itemView.findViewById(R.id.order_qty);
            imageView =  itemView.findViewById(R.id.order_image_title);
            parentLayout =  itemView.findViewById(R.id.order_confirm_parrent);
            this.confirmOrdersActivity = confirmOrdersActivity;
        }
    }

    public void filterList(ArrayList<OrderItem> filterdNames) {
        this.orderDetailArrayList = filterdNames;
        notifyDataSetChanged();
    }
}