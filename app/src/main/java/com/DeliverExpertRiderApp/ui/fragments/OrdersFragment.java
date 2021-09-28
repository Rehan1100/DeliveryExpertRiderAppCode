package com.DeliverExpertRiderApp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.DeliverExpertRiderApp.Communications.response.OrderListResponse;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.adapters.OrderAdapter;
import com.expertorider.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    @BindView(R.id.recyclerView_order)
    RecyclerView recyclerView;
    @BindView(R.id.swiper_order)
    SwipeRefreshLayout swipeRefreshLayout;
    private OrderAdapter adapter;
    private ArrayList<OrderDetail> arrayList = new ArrayList<>();

    private String status = "";
    private SearchView searchView;




    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
            status = getArguments().getString("status");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonUtil.IsInternetConnected(getContext())) {

                    swipeRefreshLayout.setRefreshing(true);
                    CallgetOrderList_(AppTypeDetails.getInstance(getContext()).getUserID() , status);
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
              CallgetOrderList_(AppTypeDetails.getInstance(getContext()).getUserID(),status);
        } else {
            Toast.makeText(getContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    void CallgetOrderList_(String id,String status_) {
        arrayList.clear();

        Call<OrderListResponse> call = RestClient.getClient().order_list(id + "",""+status_);
        call.enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {

                if (response.body().getStatus() == 1) {
                    Log.d("response", response.toString());
                    arrayList = response.body().getOrder_List();
                    adapter = new OrderAdapter( response.body().getOrder_List(), getContext());
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);
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


     /*   String url="https://deliveryexpert.com.pk/RiderApi/order_list";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, addJsonParams(id, status_),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response", response.toString());

                    }
                },
                new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;


            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

*/




    }

//    void CallgetOrderList_(String id, String mStatus) {
//        arrayList.clear();
//        arrayList.add(new OrderDetail("2", "12", "2000", "i8 markaz Islamabad"
//                , "qwqwqw", "12", "qweqwe", "droped", "qwew", id));
//        arrayList.add(new OrderDetail("20", "120", "10000", "g10 markaz Islamabad"
//                , "qwqwqw", "12", "qweqwe", "droped", "qwew", id));
//        arrayList.add(new OrderDetail("40", "90", "16600", "g10 markaz goritown Islamabad"
//                , "qwqwqw", "12", "qweqwe", "picked", "qwew", id));
//        arrayList.add(new OrderDetail("2", "12", "2000", "i8 markaz Islamabad"
//                , "qwqwqw", "12", "qweqwe", "droped", "qwew", id));
//        arrayList.add(new OrderDetail("40", "90", "16600", "g10 markaz goritown Islamabad"
//                , "qwqwqw", "12", "qweqwe", "picked", "qwew", id));
//        arrayList.add(new OrderDetail("2", "12", "2000", "i8 markaz Islamabad"
//                , "qwqwqw", "12", "qweqwe", "droped", "qwew", id));
//        arrayList.add(new OrderDetail("20", "120", "10000", "g10 markaz Islamabad"
//                , "qwqwqw", "12", "qweqwe", "droped", "qwew", id));
//        arrayList.add(new OrderDetail("40", "90", "16600", "g10 markaz goritown Islamabad"
//                , "qwqwqw", "12", "qweqwe", "picked", "qwew", id));
//        adapter.notifyDataSetChanged();
//        progressDialogue.dismiss();
//        swipeRefreshLayout.setRefreshing(false);
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
            case R.id.action_order_search:
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_order_search).setVisible(true);
        menu.findItem(R.id.action_dropped).setVisible(false);
        menu.findItem(R.id.action_picked).setVisible(false);
        menu.findItem(R.id.action_reached).setVisible(false);
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

//    public JSONObject addJsonParams(String id,String Status) {
//        JSONObject jsonobject = new JSONObject();
//        try {
//
//            ///***//
//            Log.d("addJsonParams", "addJsonParams");
//            jsonobject.put("rider_id", id+"");
//            jsonobject.put("status", Status+"");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return jsonobject;
//    }


}
