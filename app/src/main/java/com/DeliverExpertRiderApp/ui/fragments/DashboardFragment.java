package com.DeliverExpertRiderApp.ui.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.ui.activities.DashboardActivity;
import com.DeliverExpertRiderApp.Communications.response.CountOrderResponse;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    @BindView(R.id.linearLayout_assign_order)
    LinearLayout linearLayout_assign_order;
    @BindView(R.id.linearLayout_collected_cash)
    LinearLayout linearLayout_collected_cash;
    @BindView(R.id.linearLayout_dropped)
    LinearLayout linearLayout_dropped;
    @BindView(R.id.linearLayout_picked)
    LinearLayout linearLayout_picked;
    @BindView(R.id.linearLayout_remaining)
    LinearLayout linearLayout_remaining;
    @BindView(R.id.linearLayout_editProfile)
    LinearLayout linearLayout_editProfile;

    @BindView(R.id.assign_order)
    TextView assigned_order;
    @BindView(R.id.dropped)
    TextView dropped;
    @BindView(R.id.picked)
    TextView picked;
    @BindView(R.id.collected_cash)
    TextView collected_cash;
    @BindView(R.id.remaining)
    TextView remaining;
    @BindView(R.id.reached)
    TextView reached;
    @BindView(R.id.swiper_panel)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isAssignedAvaliable  = true,isReachedAvaliable  = true,
            isDroppedAvaliable  = true,isPickedAvaliable  = true,isRemainingAvaliable  = true;

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        showProgress("Loading...");

        linearLayout_assign_order.setOnClickListener(this::onClick);
        linearLayout_collected_cash.setOnClickListener(this::onClick);
        linearLayout_dropped.setOnClickListener(this::onClick);
        linearLayout_remaining.setOnClickListener(this::onClick);
        linearLayout_picked.setOnClickListener(this::onClick);
        linearLayout_editProfile.setOnClickListener(this::onClick);
        AppCompatActivity context= (AppCompatActivity) getContext();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonUtil.IsInternetConnected(getContext())) {

                    swipeRefreshLayout.setRefreshing(true);
                    DashboardFragment fragment = new DashboardFragment();
                    Bundle args = new Bundle();
                    args.putString("title", "DeliveryExpert");
                    fragment.setArguments(args);
                    FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.dashboardfragmentContainer,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    Toast.makeText(getContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//        if (!AppTypeDetails.getInstance(getContext()).getIsOnline()){
//            getActivity().setTitle("You are Offline");
//        }


        if (CommonUtil.IsInternetConnected(getContext())) {
            showProgress("Loading...");
            CallCountOrderApi(AppTypeDetails.getInstance(getContext()).getUserID());
        }else {
            Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void CallCountOrderApi(String riderID) {

        Call<CountOrderResponse> call = RestClient.getClient().CountOrders(riderID + "");
        call.enqueue(new Callback<CountOrderResponse>() {
            @Override
            public void onResponse(Call<CountOrderResponse> call, Response<CountOrderResponse> response) {
                if (response.body().getStatus() == 1) {
                    hideProgress();
                    if (Double.parseDouble(response.body().getCountOrder().getAssigned_order())<=0){
                        isAssignedAvaliable = false;
                    }
                    if (Double.parseDouble(response.body().getCountOrder().getDroped_order())<=0){
                        isDroppedAvaliable = false;
                    }
                    if (Double.parseDouble(response.body().getCountOrder().getRemaining_order())<=0){
                        isRemainingAvaliable = false;
                    }
                    if (Double.parseDouble(response.body().getCountOrder().getPicked_order())<=0){
                        isPickedAvaliable = false;
                    }
                    if (Double.parseDouble(response.body().getCountOrder().getReached_order())<=0){
                        isReachedAvaliable = false;
                    }
                    assigned_order.setText(response.body().getCountOrder().getAssigned_order());
                    dropped.setText(response.body().getCountOrder().getDroped_order());
                    picked.setText(response.body().getCountOrder().getPicked_order());
                    reached.setText(response.body().getCountOrder().getReached_order());
                    remaining.setText(response.body().getCountOrder().getRemaining_order());
                    DashboardActivity.setBadgeCounter(Integer.parseInt(response.body().getCountOrder().getRemaining_order()));
                    if (response.body().getCountOrder().getCash_collected() == null) {

                    } else {
                        collected_cash.setText(CommonUtil.getCurrency() + NumberFormat.getNumberInstance(Locale.getDefault())
                                .format(Double.parseDouble(response.body().getCountOrder().getCash_collected())));
                        if (response.body().getCountOrder().getCash_collected().isEmpty()) {
                            AppTypeDetails.getInstance(getContext()).setCashCollected("00");
                        } else {
                            AppTypeDetails.getInstance(getContext()).setCashCollected(response.body().getCountOrder().getCash_collected());
                        }
                    }
                } else {
                    hideProgress();
                    isAssignedAvaliable = false;
                    isRemainingAvaliable=false;
                    isReachedAvaliable=false;
                    isPickedAvaliable=false;
                    isDroppedAvaliable=false;
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountOrderResponse> call, Throwable t) {
                hideProgress();
                isAssignedAvaliable = false;
                isRemainingAvaliable=false;
                isReachedAvaliable=false;
                isPickedAvaliable=false;
                isDroppedAvaliable=false;
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.action_notifications);
//        LayerDrawable icon = (LayerDrawable) item.getIcon();
//
//        // Update LayerDrawable's BadgeDrawable
//        Util.setBadgeCount(this, icon, mNotificationsCount);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_order_search).setVisible(false);
        menu.findItem(R.id.action_dropped).setVisible(false);
        menu.findItem(R.id.action_picked).setVisible(false);
        menu.findItem(R.id.action_reached).setVisible(false);
        menu.findItem(R.id.action_notification).setVisible(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout_assign_order:

                if (isAssignedAvaliable) {
                    if (CommonUtil.IsInternetConnected(getContext())) {
                        CallFragment(new OrdersFragment(), "Assigned Orders", "all");
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "No Order Avaliable", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayout_remaining:
                if (isRemainingAvaliable) {
                    if (CommonUtil.IsInternetConnected(getContext())) {
                        CallFragment(new OrdersFragment(), "Remaining Orders", "Pending");
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "No Order Avaliable", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(getApplicationContext(), TestingActivity.class));
                break;
            case R.id.linearLayout_picked:
                if (isPickedAvaliable) {
                    if (CommonUtil.IsInternetConnected(getContext())) {
                        CallFragment(new OrdersFragment(), "Picked Orders", "picked");
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "No Order Avaliable", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(getApplicationContext(), TestingActivity.class));
                break;
            case R.id.linearLayout_dropped:
                if (isDroppedAvaliable) {
                    if (CommonUtil.IsInternetConnected(getContext())) {
                        CallFragment(new OrdersFragment(), "Dropped Orders", "droped");
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "No Order Avaliable", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(getApplicationContext(), TestingActivity.class));
                break;
            case R.id.linearLayout_collected_cash:
//                CallFragment(new ProfileFragment(), "Cash Collected", "");
               // startActivity(new Intent(getContext(), MapsActivity.class));
                break;
            case R.id.linearLayout_editProfile:
                if (isReachedAvaliable) {
                    if (CommonUtil.IsInternetConnected(getContext())) {
                        CallFragment(new OrdersFragment(), "Reached", "reached");
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "No Order Avaliable", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void CallFragment(Fragment fragment, String title, String status) {
//        Fragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("status", status);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dashboardfragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DashboardActivity.fragment = fragment;
        getActivity().setTitle(title);

//        if (!AppTypeDetails.getInstance(getContext()).getIsOnline()){
//            getActivity().setTitle("You are Offline");
//        }else {
//            getActivity().setTitle(title);
//        }
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



    public void updateNotificationsBadge(int count) {
//        mNotificationsCount = count;
        // updateNotifications();

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
//        invalidateOptionsMenu();
    }

}
