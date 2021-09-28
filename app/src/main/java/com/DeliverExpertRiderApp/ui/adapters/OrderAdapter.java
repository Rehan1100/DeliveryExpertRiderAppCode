package com.DeliverExpertRiderApp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.activities.ConfirmOrdersActivity;
import com.expertorider.R;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Serializable {

    private ArrayList<OrderDetail> orderDetailArrayList;
    private ArrayList<Integer> colorArray = new ArrayList<>();
    private Context context;
    int count = 0;

    private boolean isFromReport = false;

    public OrderAdapter(ArrayList<OrderDetail> arrayList, Context context, boolean isFromReport) {
        this.orderDetailArrayList = arrayList;
        this.context = context;
        this.isFromReport = isFromReport;
    }

    public OrderAdapter(ArrayList<OrderDetail> arrayList, Context context) {
        this.orderDetailArrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.order_adapter_layout, parent, false);

        return new OrderAdapter.ViewHolder(itemView);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderDetail orderDetail = orderDetailArrayList.get(position);


        holder.OrderNo.setText(orderDetail.getStatus());
        holder.VendorNo.setText(orderDetail.getVendor_info().getAddress());
        holder.address.setText(orderDetail.getCreated_at());
        if (orderDetail.getTotal_order_price().isEmpty()) {
            holder.Ammount.setText("Amount : " + CommonUtil.getCurrency() + " 0");
        } else {
            holder.Ammount.setText("Amount : " + CommonUtil.getCurrency() + NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.valueOf(orderDetail.getTotal_order_price())));
        }
        if (orderDetail.getStatus().equalsIgnoreCase("droped")) {
            holder.OrderNo.setText("Dropped");
            holder.OrderNo.setBackgroundResource(R.color.lightgreen);
        } else if (orderDetail.getStatus().equalsIgnoreCase("picked")) {
            holder.OrderNo.setText("Picked");
            holder.OrderNo.setBackgroundResource(R.color.edit_profile_color);
        } else if (orderDetail.getStatus().equalsIgnoreCase("reached")) {
            holder.OrderNo.setText("Reached");
            holder.OrderNo.setBackgroundResource(R.color.remaining_color);
        } else if (orderDetail.getStatus().equalsIgnoreCase("vendor_cancel")) {
            holder.OrderNo.setText("Vender Cancel");
            holder.OrderNo.setBackgroundResource(R.color.red);
        } else if (orderDetail.getStatus().equalsIgnoreCase("vendor_complete")) {
            holder.OrderNo.setText("Vender Complete");
            holder.OrderNo.setBackgroundResource(R.color.collect_cash_color);
        }
        if (count >= 0 && count <= 6) {
            holder.color.setBackgroundResource(colorArray.get(count));
            count++;
        } else {
            count = 0;
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderDetail.getStatus().equalsIgnoreCase("vendor_cancel")
                        || !orderDetail.getStatus().equalsIgnoreCase("vendor_confirm")
                        || !orderDetail.getStatus().equalsIgnoreCase("pending")
                        || !orderDetail.getStatus().equalsIgnoreCase("droped")
                ) {
                    if (orderDetail.getStatus().equalsIgnoreCase("vendor_complete")
                            || orderDetail.getStatus().equalsIgnoreCase("reached")
                            || orderDetail.getStatus().equalsIgnoreCase("picked")

                    ) {
                        context.startActivity(new Intent(context, ConfirmOrdersActivity.class)
                                .putExtra("order", orderDetail)
                        );
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView OrderNo, VendorNo, address, color, Ammount;
        public CardView parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            colorArray.add(R.color.remaining_color);
            colorArray.add(R.color.droped_color);
            colorArray.add(R.color.picked_color);
            colorArray.add(R.color.colorblue);
            colorArray.add(R.color.collect_cash_color);
            colorArray.add(R.color.edit_profile_color);

            OrderNo = itemView.findViewById(R.id.order_ad_order_no);
            VendorNo = itemView.findViewById(R.id.order_ad_vendor_no);
            address = itemView.findViewById(R.id.order_ad_address);
            Ammount = itemView.findViewById(R.id.order_ad_price);
            color = itemView.findViewById(R.id.order_ad_color);
            parentLayout = itemView.findViewById(R.id.order_ad_parrent);
        }
    }

    public void filterList(ArrayList<OrderDetail> filterdNames) {
        this.orderDetailArrayList = filterdNames;
        notifyDataSetChanged();
    }
}
