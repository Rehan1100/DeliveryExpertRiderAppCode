package com.DeliverExpertRiderApp.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.OrderListResponse;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.adapters.ReportsAdapter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.bot) RelativeLayout relativeLayoutBottom;

    @BindView(R.id.recyclerView_report) RecyclerView recyclerView;
    @BindView(R.id.swiper_report) SwipeRefreshLayout swipeRefreshLayout;
    private ReportsAdapter adapter;
    private ArrayList<OrderDetail> arrayList = new ArrayList<>();
    private String status = "all";

    ReportFragment reportFragment;

    private int YEAR,MONTH,DAY;
//    @BindView(R.id.total_item_price) static TextView total_item_price;
    static TextView total_item_price;
    @BindView(R.id.total_item) TextView total_items;

    @BindView(R.id.btn_to) Button buttonTo;
    @BindView(R.id.btn_from) Button buttonFrom;
    @BindView(R.id.text_to) TextView textViewTo;
    @BindView(R.id.text_from) TextView textViewFrom;

    boolean isToDateSelected = false, isFromDateSelected = false;
    String toDate , fromDate;


    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this,view);

        total_item_price = view.findViewById(R.id.total_item_price);
        relativeLayoutBottom.setVisibility(View.INVISIBLE);
        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalenderDialogTo();
            }
        });



        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalenderDialogFrom();
            }
        });


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setLayoutManager(mGridLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonUtil.IsInternetConnected(getContext())) {

                    swipeRefreshLayout.setRefreshing(true);
                    CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(), status);
//                    if (status.equals(null)){  //
//
//                        CallgetOrderList_(AppTypeDetails.getInstance(getContext()).getUserID());
//                    }else {  //   from dashboard ( drop  , picked or assigned )
//                        CallgetOrderList_(AppTypeDetails.getInstance(getContext()).getUserID(),status);
//                    }
                } else {
                    Toast.makeText(getContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (CommonUtil.IsInternetConnected(getContext())) {
            showProgress("Fetching Reports ...");
            CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(),status);
        } else {
            Toast.makeText(getContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void CallgetReports(String userID, String status) {

        arrayList.clear();
        Call<OrderListResponse> call = null;
        if (isToDateSelected){
            if (isFromDateSelected){
                call = RestClient.getClient().report_list(userID + "", "" + status, toDate+"",fromDate+"");
            }else {
                call = RestClient.getClient().report_list(userID + "", "" + status, toDate);
            }
        }else {
            call = RestClient.getClient().report_list(userID + "",""+status);
        }
        call.enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {

                if (response.body().getStatus() == 1) {
                    arrayList = response.body().getOrder_List();
                    adapter = new ReportsAdapter( response.body().getOrder_List(), getContext(),reportFragment);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);

                    double totalPrice = 0;
                    for (int i = 0 ; i<response.body().getOrder_List().size() ; i++){
                        double price = Double.parseDouble(response.body().getOrder_List().get(i).getTotal_order_price());

                        totalPrice = totalPrice+price;

                        relativeLayoutBottom.setVisibility(View.VISIBLE);
                    }
                    setTotal_item_price(totalPrice+"");
                    total_items.setText(arrayList.size()+" items Total : ");
                } else {
                    adapter = new ReportsAdapter( response.body().getOrder_List(), getContext(),reportFragment);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);
                    setTotal_item_price("0");
                    relativeLayoutBottom.setVisibility(View.INVISIBLE);
                    total_items.setText(arrayList.size()+" items Total : ");
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                hideProgress();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reached:
//                Toast.makeText(getContext(), "Reached Clicked", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(), "reached");
                return false;
            case R.id.action_dropped:
//                Toast.makeText(getContext(), "Dropped Clicked", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(), "droped");
                return false;
            case R.id.action_picked:
//                Toast.makeText(getContext(), "Picked Clicked", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(), "picked");
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_order_search).setVisible(false);
        menu.findItem(R.id.action_dropped).setVisible(true);
        menu.findItem(R.id.action_picked).setVisible(true);
        menu.findItem(R.id.action_reached).setVisible(true);
        menu.findItem(R.id.action_notification).setVisible(false);
    }


    @BindView(R.id.message)
    TextView messageTv;
    @BindView(R.id.pr)
    View progressCard;

//    public void setupProgress(View view) {
//        messageTv = view.findViewById(R.id.message);
//        progressCard = view;
//        progressCard.setVisibility(View.GONE);
//    }

    public void hideProgress() {
        try {
            progressCard.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    public void showProgress(String message) {
        try {
            messageTv.setText(message);
            progressCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
    }


    private void ShowCalenderDialogTo() {

        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("To");

            final DatePicker input = new DatePicker(getContext());
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    YEAR = input.getYear();
                    MONTH = input.getMonth();
                    DAY = input.getDayOfMonth();
                    textViewTo.setText( input.getYear()+"-"+(input.getMonth()+1)+"-"+input.getDayOfMonth());
                    toDate = input.getYear()+"-"+(input.getMonth()+1)+"-"+input.getDayOfMonth();
                    isToDateSelected = true;
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
         //           Toast.makeText(getContext(),"Operation Failed",Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();

        }
        catch (Exception ex)
        {
    //        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowCalenderDialogFrom() {

        try
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("From");

            final DatePicker input = new DatePicker(getContext());

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = formatter.parse(toDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            input.setMinDate(cal.getTimeInMillis());

            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textViewFrom.setText( input.getYear()+"-"+(input.getMonth()+1)+"-"+input.getDayOfMonth());
                    isFromDateSelected = true;
                    fromDate = input.getYear()+"-"+(input.getMonth()+1)+"-"+input.getDayOfMonth();
                    swipeRefreshLayout.setRefreshing(true);
                    CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(),status);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
       //             Toast.makeText(getContext(),"Operation Failed",Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();

        }
        catch (Exception ex)
        {
     //       Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void setTotal_item_price(String total_item_price) {
        ReportFragment.total_item_price.setText(CommonUtil.getCurrency()+ NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(total_item_price)));

    }


}
