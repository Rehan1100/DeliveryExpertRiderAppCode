package com.DeliverExpertRiderApp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.CommonResponse;
import com.DeliverExpertRiderApp.Communications.response.OrderDetailsResponse;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderItem;
import com.DeliverExpertRiderApp.Communications.response.Model.UserData;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.adapters.ConfrimOrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<OrderItem> orderitemlist = new ArrayList<>();
    ArrayList<OrderDetail> orderdetaillist = new ArrayList<>();
    OrderItem orderItem;
    public OrderDetail orderDetail;

    @BindView(R.id.recylcerView_confirmOrders)
    RecyclerView recyclerView;
    @BindView(R.id.customer_name)
    TextView CustomerName;
    @BindView(R.id.discount)
    TextView discount;
    @BindView(R.id.dropOff_name)
    TextView dropOff_name;
    @BindView(R.id.customer_cell_no)
    TextView CustomerPhoneNo;
    @BindView(R.id.dropOff_cell_no)
    TextView DropOffPhoneNo;
    @BindView(R.id.customer_order_address)
    TextView CustomerOrderAddress;
    @BindView(R.id.order_name)
    TextView Order_Name;
    @BindView(R.id.total_item_price)
    TextView total_item_price;
    @BindView(R.id.total_item)
    TextView total_items;
    @BindView(R.id.customer_order_date)
    TextView customer_order_date;
    @BindView(R.id.call_map)
    ImageView call_map;
    @BindView(R.id.img_empty_cart)
    ImageView img_empty_cart;
    @BindView(R.id.btn_confirm)
    Button btn_confirm_picked;
    @BindView(R.id.btn_reached)
    Button btn_reached;
    @BindView(R.id.btn_cancel)
    Button btn_dropped;
    @BindView(R.id.btn_status)
    Button btn_status;
    @BindView(R.id.layout_status)
    LinearLayout layout_status;


    @BindView(R.id.vender_order_address)
    TextView order_address;
    @BindView(R.id.dropOff_message)
    TextView dropOff_message;

    public ConfrimOrderAdapter adapter;
    public ArrayList<OrderItem> arrayList = new ArrayList<>();

    public OrderDetail adData;

    private boolean isfromReport = false;
    private String PhoneNo_ = " ";
    public UserData userData;
    ArrayList<UserData> userDatalist= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_orders);
        ButterKnife.bind(this);

        btn_confirm_picked.setOnClickListener(this::onClick);
        btn_status.setOnClickListener(this::onClick);
        btn_dropped.setOnClickListener(this::onClick);
        btn_reached.setOnClickListener(this::onClick);
        call_map.setOnClickListener(this::onClick);
        CustomerPhoneNo.setOnClickListener(this::onClick);


        adData = (OrderDetail) getIntent().getSerializableExtra("order");
        isfromReport = getIntent().getExtras().getBoolean("isfromReport");

        if (isfromReport) {
            layout_status.setVisibility(View.INVISIBLE);
        }

        setTotal_item_price(adData.getTotal_order_price());
        customer_order_date.setText("Date : " + adData.getCreated_at());
        if (adData.getStatus().equalsIgnoreCase("pending")) {
            btn_reached.setVisibility(View.GONE);
            btn_confirm_picked.setVisibility(View.VISIBLE);
            btn_dropped.setVisibility(View.GONE);
        } else if (adData.getStatus().equalsIgnoreCase("picked")) {
            btn_reached.setVisibility(View.VISIBLE);
            btn_confirm_picked.setVisibility(View.GONE);
            btn_dropped.setVisibility(View.GONE);
        } else if (adData.getStatus().equalsIgnoreCase("reached")) {
            btn_reached.setVisibility(View.GONE);
            btn_confirm_picked.setVisibility(View.GONE);
            btn_dropped.setVisibility(View.VISIBLE);
        } else if (adData.getStatus().equalsIgnoreCase("droped")) {
            btn_reached.setVisibility(View.GONE);
            btn_confirm_picked.setVisibility(View.GONE);
            btn_dropped.setVisibility(View.GONE);
            btn_status.setVisibility(View.VISIBLE);
        }
        //  CustomerInfo = (UserData) getIntent().getSerializableExtra("CustomerInfo");

//        CustomerName.setText(CustomerInfo.getName());
//        CustomerOrderAddress.setText(CustomerInfo.getAddress());
//        CustomerPhoneNo.setText(CustomerInfo.getPhone());


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //   recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        if (CommonUtil.IsInternetConnected(getApplicationContext())) {

            showProgress("Loading...");
            CallgetOrderList(adData.getId());

            //    Toast.makeText(this, "id : "+adData.getId(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Error !!", Toast.LENGTH_SHORT).show();
        }
        // CustomerPhoneNo.setText("Cell : "+PhoneNo_);

    }

    private void CallgetOrderList(String id) {

        arrayList.clear();

        setPhoneNo_(adData.getVendor_info().getPhone());
        CustomerName.setText("Name : " + adData.getVendor_info().getHotel_name());
        CustomerPhoneNo.setText("Mobile # " + adData.getVendor_info().getPhone());
        CustomerOrderAddress.setText("Address : " +  adData.getVendor_info().getAddress());

        String url = "https://deliveryexpert.com.pk/RiderApi/order_details";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, addJsonParams(id),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {


                                orderdetaillist.clear();
                                orderitemlist.clear();
                                JSONObject data = response.getJSONObject("data");
                                JSONObject order_detail = data.getJSONObject("order_detail");
                                Log.d("data", data.toString());
                                for (int i = 0; i < data.length(); i++) {
                                    String id = order_detail.getString("id");
                                    String user_id = order_detail.getString("user_id");
                                    String total_order_price = order_detail.getString("total_order_price");
                                    String message = order_detail.getString("message");
                                    String Address = order_detail.getString("Address");
                                    String status1 = order_detail.getString("status");
                                    String coupon = order_detail.getString("coupon");
                                    String business_id = order_detail.getString("id");
                                    String rider_id = order_detail.getString("id");
                                    String drop_lat = order_detail.getString("id");
                                    String drop_lng = order_detail.getString("id");
                                    String created_at = order_detail.getString("id");

                                    orderDetail = new OrderDetail(user_id, id, total_order_price, message, Address, status1, coupon, business_id, rider_id, created_at, drop_lat, drop_lng, null);

                                    orderdetaillist.add(orderDetail);

                                    order_address.setText("Address : " + orderDetail.getAddress());
                                    dropOff_message.setText(orderDetail.getMessage());
                                    hideProgress();

                                }

                                JSONArray order_item = data.getJSONArray("order_item");

                                for (int i = 0; i < order_detail.length(); i++) {

                                    JSONObject jsonObject = order_item.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String order_id = jsonObject.getString("order_id");
                                    String title = jsonObject.getString("title");
                                    String cat_name = jsonObject.getString("cat_name");
                                    String price = jsonObject.getString("price");
                                    String qty = jsonObject.getString("qty");
                                    String image = jsonObject.getString("image");
                                    String create_at = jsonObject.getString("create_at");
                                    String product_id = jsonObject.getString("product_id");

                                    orderItem = new OrderItem(id, order_id, title, cat_name, price, qty, create_at, image);
                                    orderitemlist.add(orderItem);
                                    adapter = new ConfrimOrderAdapter(orderitemlist, ConfirmOrdersActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();



                                    JSONObject customer = data.getJSONObject("customer");
                                    if (customer!=null)
                                    {



                                    for (int j = 0; j < customer.length(); j++) {
                                        String id1 = customer.getString("id");
                                        String name = customer.getString("name");
                                        String email = customer.getString("email");
                                        String password = customer.getString("password");
                                        String phone = customer.getString("phone");
                                        String Address = customer.getString("Address");
                                        String action = customer.getString("action");
                                        String fb_id = customer.getString("fb_id");
                                        String reset_code = customer.getString("reset_code");
                                        String country = customer.getString("country");
                                        String city = customer.getString("city");
                                        String postcode = customer.getString("postcode");
                                        String firebasetoken = customer.getString("firebasetoken");

                                        userData = new UserData(id1, name, email, phone, Address, password, "", "");

                                        userDatalist.add(userData);
                                        DropOffPhoneNo.setText(phone);
                                    }

                                        }

                                }






                                DropOffPhoneNo.setText("Mobile # " + userData.getPhone());
                                dropOff_name.setText("Name" + userData.getName());
                                total_items.setText(arrayList.size() + " items Total : ");
                                if (!orderDetail.getCoupon().matches("0")) {
                                    discount.setText("Coupon discount : " + orderDetail.getCoupon());

                                } else {
                                    discount.setVisibility(View.GONE);
                                }
                                setTotal_item_price(orderDetail.getTotal_order_price());


                            } else {
                                Toast.makeText(ConfirmOrdersActivity.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgress();
                        Toast.makeText(ConfirmOrdersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


        ///Comment by me

        Call<OrderDetailsResponse> call = RestClient.getClient().order_details(id );
        call.enqueue(new Callback<OrderDetailsResponse>() {
            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        hideProgress();
                        Log.d("response", response.toString());


//                        Order_Name.setText(response.body().getData().getOrder_detail().get());

                             dropOff_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallMessageDialog("Message", "" + orderDetail.getMessage());
                            }
                        });

                        DropOffPhoneNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                CommonUtil.MakePhoneCall(getApplicationContext(),userData.getPhone());


                            }
                        });

                        order_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallMessageDialog("Order Address", "" + orderDetail.getAddress());
                            }
                        });

                        CustomerOrderAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallMessageDialog("Vender Address", "" + adData.getVendor_info().getAddress());
                            }
                        });

                    } else {
                        hideProgress();
                        //            Toast.makeText(ConfirmOrdersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(ConfirmOrdersActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    void CallgetOrderList_() {
//        arrayList.clear();
//
//        arrayList.add(new OrderItem("1", "Roh Afza 5 liter", "166", "10","https://i0.wp.com/www.globalvillagespace.com/wp-content/uploads/2019/05/Twitter-reacts-strongly-to-the-shortage-of-Rooh-Afza-in-India.jpg"));
//        arrayList.add(new OrderItem("2", "Detergent", "130", "3","https://target.scene7.com/is/image/Target/GUEST_748efd0d-fc1d-4f85-a3ec-2aa321eea115?wid=488&hei=488&fmt=pjpeg"));
//        arrayList.add(new OrderItem("3", "Magi Masala", "40", "2","https://www.nestle.com.bd/sites/g/files/pydnoa311/files/8%20Pack%20Masala%20Blast_0.png"));
//        arrayList.add(new OrderItem("4", "Lux Soap ", "65", "7","https://www.beautybulletin.com/media/reviews/photos/original/6e/cd/cb/15276-lux-soft-touch-beauty-bar-78-1446025583.png"));
//        arrayList.add(new OrderItem("5", "whole Grain ", "83", "5","https://www.shieldhealthcare.com/community/wp-content/uploads/2017/09/Whole-Grain-Why-You-Need-It-and-Where-to-Find-It.png"));
//        adapter.notifyDataSetChanged();
////        if (progressDialogue.isShowing()) {
////            progressDialogue.dismiss();
////        }
//        total_items.setText(arrayList.size()+" items Total : ");
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.call_map:
                /*Bundle bundle = new Bundle();
                bundle.putString("DestinationLng", adData.getDrop_lng());
                bundle.putString("DestinationLat", adData.getDrop_lat());
                bundle.putString("SourceLat", adData.getVendor_info().getLat() + "");
                bundle.putString("SourceLng", adData.getVendor_info().getLng() + "");
                startActivity(new Intent(getApplicationContext(), MapsActivity.class)
                        .putExtras(bundle)
                );*/


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" +adData.getAddress()));
                startActivity(intent);

                break;
            case R.id.btn_confirm:
                showProgress("Updating...");
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                btn_reached.setVisibility(View.VISIBLE);
                btn_confirm_picked.setVisibility(View.GONE);
                btn_dropped.setVisibility(View.GONE);
                CallOrderStatusChangeApi(adData.getId() + "", "picked");
                break;
            case R.id.btn_reached:
                showProgress("Updating...");
                btn_reached.setVisibility(View.GONE);
                btn_confirm_picked.setVisibility(View.GONE);
                btn_dropped.setVisibility(View.VISIBLE);
                CallOrderStatusChangeApi(adData.getId(), "reached");
                break;
            case R.id.btn_cancel:
                showProgress("Updating...");
                btn_reached.setVisibility(View.GONE);
                btn_confirm_picked.setVisibility(View.GONE);
                CallOrderStatusChangeApi(adData.getId(), "droped");
                break;
            case R.id.btn_status:

                showProgress("Updating...");
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                btn_reached.setVisibility(View.VISIBLE);
                btn_confirm_picked.setVisibility(View.GONE);
                btn_dropped.setVisibility(View.GONE);
                CallOrderStatusChangeApi(adData.getId() + "", "Order Delivered");



                break;

            case R.id.customer_cell_no:
                CommonUtil.MakePhoneCall(getApplicationContext(), getPhoneNo_());
                break;
        }
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price.setText(CommonUtil.getCurrency() + NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(total_item_price)));

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

    public void setPhoneNo_(String phoneNo_) {
        PhoneNo_ = phoneNo_;
    }

    public String getPhoneNo_() {
        return PhoneNo_;
    }


    void CallOrderStatusChangeApi(String orderId, String status) {
        Call<CommonResponse> call = RestClient.getClient().order_status_change(orderId + "", status + "");
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        hideProgress();
                        finish();
                        Toast.makeText(ConfirmOrdersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ConfirmOrdersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                } else {
                    Toast.makeText(ConfirmOrdersActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(ConfirmOrdersActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        if (CommonUtil.IsInternetConnected(getApplicationContext())) {
//            if (AppTypeDetails.getInstance(getApplicationContext()).getIsOnline()) {
//                ApisCommon.Call_Online_Status_Api(ConfirmOrdersActivity.this, AppTypeDetails.
//                        getInstance(getApplicationContext()).getUserID(), "0", progressCard);
//            }
//        }
//        super.onDestroy();
//    }


    public void CallMessageDialog(String title, String details) {
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(ConfirmOrdersActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.single_click_dialog_layout, null);
        dialog.setView(dialogView);

        final TextView textView = dialogView.findViewById(R.id.title_dialog);
        textView.setText(title);
        final TextView textViewDetail = dialogView.findViewById(R.id.detail_dialog);
        textViewDetail.setText(details);

        final CardView btn_OK = dialogView.findViewById(R.id.btn_ok);
        dialog.setCancelable(true);

        final androidx.appcompat.app.AlertDialog alert = dialog.create();
        alert.show();

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    public JSONObject addJsonParams(String id) {
        JSONObject jsonobject = new JSONObject();
        try {

            ///***//
            Log.d("addJsonParams", "addJsonParams");
            jsonobject.put("order_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonobject;
    }


}
