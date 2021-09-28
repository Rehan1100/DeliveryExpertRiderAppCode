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
import com.DeliverExpertRiderApp.ui.activities.ConfirmOrdersActivity;
import com.DeliverExpertRiderApp.ui.fragments.ReportFragment;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.CommonUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private ArrayList<OrderDetail> orderDetailArrayList;
    private ArrayList<Integer> colorArray = new ArrayList<>();
    private Context context;
    int count = 0;
    ReportFragment reportFragment = null;
    private boolean isFromReport = false;

    double totalPrice = 0;


    public ReportsAdapter(ArrayList<OrderDetail> arrayList , Context context ){
        this.orderDetailArrayList=arrayList;
        this.context=context;
    }
    public ReportsAdapter(ArrayList<OrderDetail> arrayList , Context context ,ReportFragment reportFragment){
        this.orderDetailArrayList=arrayList;
        this.context=context;
        this.reportFragment = reportFragment;
    }

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.report_adapter_layout, parent, false);

        return new ReportsAdapter.ViewHolder(itemView,reportFragment);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder holder, int position) {
        final OrderDetail orderDetail = orderDetailArrayList.get(position);


        holder.OrderNo.setText("Order # "+orderDetail.getId());
        holder.address.setText("Date "+orderDetail.getCreated_at());
        holder.Ammount.setText("Amount : "+ CommonUtil.getCurrency()+ NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.valueOf(orderDetail.getTotal_order_price())));


        totalPrice = totalPrice + Double.parseDouble(orderDetail.getTotal_order_price());

//        reportFragment.setTotal_item_price(totalPrice+"");


//        if (orderDetail.getStatus().equalsIgnoreCase("droped")){
//            holder.OrderNo.setText("Dropped");
//            holder.OrderNo.setBackgroundResource(R.color.lightgreen);
//        } else  if (orderDetail.getStatus().equalsIgnoreCase("picked")){
//            holder.OrderNo.setText("Picked");
//            holder.OrderNo.setBackgroundResource(R.color.edit_profile_color);
//        } else  if (orderDetail.getStatus().equalsIgnoreCase("reached")){
//            holder.OrderNo.setText("Reached");
//            holder.OrderNo.setBackgroundResource(R.color.remaining_color);
//        }

        if (count>=0  && count<=6){
            holder.color.setBackgroundResource(colorArray.get(count));
            count++;
        }else {
            count = 0;
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ConfirmOrdersActivity.class)
                        .putExtra("order",orderDetail)
                        .putExtra("isfromReport",true)
                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView OrderNo  ,address,color , Ammount;
        public CardView parentLayout;
        public ReportFragment reportFragment;

        public ViewHolder(@NonNull View itemView ,ReportFragment reportFragment) {
            super(itemView);

            colorArray.add(R.color.remaining_color);
            colorArray.add(R.color.droped_color);
            colorArray.add(R.color.picked_color);
            colorArray.add(R.color.colorblue);
            colorArray.add(R.color.collect_cash_color);
            colorArray.add(R.color.edit_profile_color);

            this.reportFragment = reportFragment;

            OrderNo =  itemView.findViewById(R.id.report_order_no);
            address =  itemView.findViewById(R.id.report_address);
            Ammount =  itemView.findViewById(R.id.report_order_ad_price);
            color =  itemView.findViewById(R.id.order_ad_color);
            parentLayout =  itemView.findViewById(R.id.report_ad_parrent);
        }
    }

    public void filterList(ArrayList<OrderDetail> filterdNames) {
        this.orderDetailArrayList = filterdNames;
        notifyDataSetChanged();
    }
}