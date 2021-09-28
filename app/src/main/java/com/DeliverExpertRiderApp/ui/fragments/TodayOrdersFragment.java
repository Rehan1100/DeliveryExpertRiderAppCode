package com.DeliverExpertRiderApp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.DeliverExpertRiderApp.Communications.response.OrderListResponse;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.adapters.TodaysOderAdapter;
import com.expertorider.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOrdersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    @BindView(R.id.recyclerView_todays)
    RecyclerView recyclerView;
    @BindView(R.id.swiper_todays)
    SwipeRefreshLayout swipeRefreshLayout;
    private TodaysOderAdapter adapter;
    private ArrayList<OrderDetail> arrayList = new ArrayList<>();
    private String status = "all";
    private SearchView searchView;

    TodayOrdersFragment todayOrdersFragment;
    static TextView total_item_price;
    @BindView(R.id.total_item) TextView total_items;
    @BindView(R.id.bot) RelativeLayout relativeLayoutBottom;

    public static TodayOrdersFragment newInstance(String param1, String param2) {
        TodayOrdersFragment fragment = new TodayOrdersFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_orders, container, false);
        ButterKnife.bind(this, view);
        total_item_price = view.findViewById(R.id.total_item_price);
        relativeLayoutBottom.setVisibility(View.INVISIBLE);
//        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

//        recyclerView.setLayoutManager(mGridLayoutManager);
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
            showProgress("Fetching Orders ...");
            CallgetReports(AppTypeDetails.getInstance(getContext()).getUserID(), status);
        } else {
            Toast.makeText(getContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void CallgetReports(String userID, String status) {

        arrayList.clear();

//        Call<OrderListResponse> call = RestClient.getClient().order_list(userID + "",""+status);
        Call<OrderListResponse> call = RestClient.getClient().report_list(userID + "",
                "" + status, CommonUtil.getCurrentDateCalendar()+"",CommonUtil.getTomorrowDateCalendar()+"");

        call.enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {

                if (response.body().getStatus() == 1) {
                    arrayList = response.body().getOrder_List();
                    adapter = new TodaysOderAdapter(response.body().getOrder_List(), getContext(),todayOrdersFragment);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);

                    double totalPrice = 0;
                    for (int i = 0; i < response.body().getOrder_List().size(); i++) {
                        double price = Double.parseDouble(response.body().getOrder_List().get(i).getTotal_order_price());

                        totalPrice = totalPrice + price;
                    }
                    setTotal_item_price(totalPrice + "");
                    relativeLayoutBottom.setVisibility(View.VISIBLE);
                    total_items.setText(arrayList.size() + " items Total : ");
                } else {
                    adapter = new TodaysOderAdapter(response.body().getOrder_List(), getContext(),todayOrdersFragment);
                    adapter.notifyDataSetChanged();
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);
                    setTotal_item_price("0");
                    relativeLayoutBottom.setVisibility(View.INVISIBLE);
                    total_items.setText(arrayList.size() + " items Total : ");
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


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.main, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_order_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search Order");

        }
        if (searchView != null) {

            searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return false;
                }
            });
        }
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
        menu.findItem(R.id.action_order_search).setVisible(true);
        menu.findItem(R.id.action_dropped).setVisible(true);
        menu.findItem(R.id.action_picked).setVisible(true);
        menu.findItem(R.id.action_reached).setVisible(true);
        menu.findItem(R.id.action_notification).setVisible(false);
    }

    private void filter(String text) {

        //new array list that will hold the filtered data
        ArrayList<OrderDetail> filterdNames = new ArrayList<>();
        //looping through existing elements
        for (OrderDetail s : arrayList) {
            //if the existing elements contains the search input
            if (
                    s.getStatus().toString().toLowerCase().trim().contains(text.toLowerCase().trim())
                            || s.getAddress().toString().toLowerCase().trim().contains(text.toLowerCase().trim())
                            || s.getCreated_at().toString().toLowerCase().trim().contains(text.toLowerCase().trim())
            ) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
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

    public static void setTotal_item_price(String total_item_price) {
            TodayOrdersFragment.total_item_price.setText(CommonUtil.getCurrency()+ NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(total_item_price)));
//        TodayOrdersFragment.total_item_price.setText("Today Date : "+CommonUtil.getCurrentDateCalendar()+" Tomorrow Date : "+CommonUtil.getTomorrowDateCalendar());

    }
}
