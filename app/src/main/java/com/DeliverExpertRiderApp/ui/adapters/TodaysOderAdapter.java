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
import com.DeliverExpertRiderApp.ui.fragments.TodayOrdersFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TodaysOderAdapter extends RecyclerView.Adapter<TodaysOderAdapter.ViewHolder> {

    private ArrayList<OrderDetail> orderDetailArrayList;
    private ArrayList<Integer> colorArray = new ArrayList<>();
    private Context context;
    int count = 0;
    ReportFragment reportFragment = null;
    private boolean isFromReport = false;
    TodayOrdersFragment todayOrdersFragment = null;

    double totalPrice = 0;


    public TodaysOderAdapter(ArrayList<OrderDetail> arrayList , Context context ){
        this.orderDetailArrayList=arrayList;
        this.context=context;
    }

    public TodaysOderAdapter(ArrayList<OrderDetail> arrayList , Context context , TodayOrdersFragment todayOrdersFragment ){
        this.orderDetailArrayList=arrayList;
        this.context=context;
        this.todayOrdersFragment=todayOrdersFragment;
    }


    @NonNull
    @Override
    public TodaysOderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.todays_order_adapter_layout, parent, false);

        return new TodaysOderAdapter.ViewHolder(itemView,todayOrdersFragment);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull TodaysOderAdapter.ViewHolder holder, int position) {
        final OrderDetail orderDetail = orderDetailArrayList.get(position);


        holder.OrderNo.setText("Order # "+orderDetail.getId());
        //Comment by Rehan

        /*        holder.address.setText("Address "+orderDetail.getVendor_info().getHotel_name());*/
        holder.Ammount.setText("Amount :  "+ CommonUtil.getCurrency()+ NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.valueOf(orderDetail.getTotal_order_price())));


        totalPrice = totalPrice + Double.parseDouble(orderDetail.getTotal_order_price());

        todayOrdersFragment.setTotal_item_price(totalPrice+"");

        if (count>=0  && count<=6) {
            holder.color.setBackgroundResource(colorArray.get(count));
            count++;
        } else {
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
        public TodayOrdersFragment todayOrdersFragment_;
        public ViewHolder(@NonNull View itemView ,TodayOrdersFragment todayOrdersFragment ) {
            super(itemView);

            colorArray.add(R.color.remaining_color);
            colorArray.add(R.color.droped_color);
            colorArray.add(R.color.picked_color);
            colorArray.add(R.color.colorblue);
            colorArray.add(R.color.collect_cash_color);
            colorArray.add(R.color.edit_profile_color);

            todayOrdersFragment_ = todayOrdersFragment;

            OrderNo =  itemView.findViewById(R.id.today_orders_order_no);
            address =  itemView.findViewById(R.id.today_orders_address);
            Ammount =  itemView.findViewById(R.id.today_orders_price);
            color =  itemView.findViewById(R.id.order_ad_color);
            parentLayout =  itemView.findViewById(R.id.today_orders_parrent);
        }
    }

    public void filterList(ArrayList<OrderDetail> filterdNames) {
        this.orderDetailArrayList = filterdNames;
        notifyDataSetChanged();
    }

}
