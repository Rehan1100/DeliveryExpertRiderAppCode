package com.DeliverExpertRiderApp.ui.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.ApisCommon;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.ui.fragments.DashboardFragment;
import com.DeliverExpertRiderApp.ui.fragments.EditProfileFragment;
import com.DeliverExpertRiderApp.ui.fragments.OrdersFragment;
import com.DeliverExpertRiderApp.ui.fragments.ProfileFragment;
import com.DeliverExpertRiderApp.ui.fragments.ReportFragment;
import com.DeliverExpertRiderApp.ui.fragments.TodayOrdersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.right_drawable_orders) TextView right_drawable_orders;
    @BindView(R.id.right_drawable_profile) TextView right_drawable_profile;
    @BindView(R.id.right_drawable_history) TextView right_drawable_history;
    @BindView(R.id.right_drawable_report) TextView right_drawable_report;
    @BindView(R.id.right_drawable_home) TextView right_drawable_home;
    @BindView(R.id.right_drawable_logout) TextView right_drawable_logout;

    @BindView(R.id.header_name) TextView header_name;
    @BindView(R.id.header_email) TextView header_email;
    @BindView(R.id.statusActiveInactive) TextView statusActiveInactive;
    @BindView(R.id.switch_) Switch aSwitch;
    @BindView(R.id.img_profile) ImageView imageView;

    public static Fragment fragment;
    private FragmentManager fragmentManager;

    // badge text view
    public static TextView badgeCounter;
    // change the number to see badge in action
    int pendingNotifications = 0;

    public static boolean isRemainingAvaliable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

//        Toast.makeText(this, "UserId : "+AppTypeDetails.getInstance(getApplicationContext()).getUserID(), Toast.LENGTH_SHORT).show();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.dashboardfragmentContainer);

//        gallery=(TextView) MenuItemCompat.getActionView(drawer.getMenu().
//                findItem(R.id.nav_gallery));
//        slideshow=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
//                findItem(R.id.nav_slideshow));


        right_drawable_report.setOnClickListener(this::onClick);
        right_drawable_history.setOnClickListener(this::onClick);
        right_drawable_profile.setOnClickListener(this::onClick);
        right_drawable_orders.setOnClickListener(this::onClick);
        right_drawable_home.setOnClickListener(this::onClick);
        right_drawable_logout.setOnClickListener(this::onClick);

        header_email.setText(AppTypeDetails.getInstance(getApplicationContext()).getUserEmail());
        header_name.setText(AppTypeDetails.getInstance(getApplicationContext()).getUserName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.mipmap.ic_launcher);

        Glide.with(this)
                .load(ApiConstants.BASE_IMAGE_URL+AppTypeDetails.getInstance(getApplicationContext()).getUserImage())
                .apply(requestOptions)
                .into(imageView);

        if (AppTypeDetails.getInstance(getApplicationContext()).getIsOnline()){
            statusActiveInactive.setText("Online");
            aSwitch.setChecked(true);
//            setToolbarTitle("You are Offline");
            statusActiveInactive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgreen));
        }else {
            aSwitch.setChecked(false);
            statusActiveInactive.setText("Offine");
            setToolbarTitle("You are Offline");
            statusActiveInactive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()){

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }

                    if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                        showProgress("Status Updating...");
                        AppTypeDetails.getInstance(getApplicationContext()).setIsOnline(true);
                        statusActiveInactive.setText("Online");
                        statusActiveInactive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgreen));
                        ApisCommon.Call_Online_Status_Api(DashboardActivity.this, AppTypeDetails.
                                getInstance(getApplicationContext()).getUserID(), "1", progressCard);
                    }else {
                        Toast.makeText(DashboardActivity.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else if(!aSwitch.isChecked()){

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                        showProgress("Status Updating...");
                        AppTypeDetails.getInstance(getApplicationContext()).setIsOnline(false);
                        statusActiveInactive.setText("Offine");
                        statusActiveInactive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        ApisCommon.Call_Online_Status_Api(DashboardActivity.this, AppTypeDetails.
                                getInstance(getApplicationContext()).getUserID(), "0", progressCard);
                    } else {
                        Toast.makeText(DashboardActivity.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        CallFragment(new DashboardFragment(),"Delivery Expert");


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment instanceof DashboardFragment && !(fragment instanceof EditProfileFragment)) {
              //  super.onBackPressed();
                finish();
            } else if (fragment instanceof EditProfileFragment) {
                CallFragment(new ProfileFragment(),"Profile");
            }else {
               CallFragment(new DashboardFragment(),"Delivery Expert");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

//        menuItem = menu.findItem(R.id.action_notification);
//
//        // if notification than set the badge icon layout
//        menuItem.setActionView(R.layout.custom_notification_layout);
//        // get the view from the nav item
//        View view = menuItem.getActionView();
//        // get the text view of the action view for the nav item
//        badgeCounter = view.findViewById(R.id.badge_counter);
//        // set the pending notifications value
//        badgeCounter.setText(String.valueOf(pendingNotifications));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_notification) {
            if (isRemainingAvaliable) {
                //   Toast.makeText(this, "Notification Click", Toast.LENGTH_SHORT).show();
                OrdersFragment ordersFragment = new OrdersFragment();
                Bundle args = new Bundle();
                args.putString("title", "Remaining Orders");
                args.putString("status", "Pending");
                ordersFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.dashboardfragmentContainer, ordersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                this.fragment = ordersFragment;
                setToolbarTitle("Remaining Orders");
            }else {
                Toast.makeText(this, "No Order Avaliable", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_order_search).setVisible(false);
        menu.findItem(R.id.action_dropped).setVisible(false);
        menu.findItem(R.id.action_picked).setVisible(false);
        menu.findItem(R.id.action_reached).setVisible(false);
        menu.findItem(R.id.action_notification).setVisible(true);

        menu.findItem(R.id.action_notification).setActionView(R.layout.custom_notification_layout);
        // get the view from the nav item
        View view = menu.findItem(R.id.action_notification).getActionView();
        // get the text view of the action view for the nav item
        badgeCounter = view.findViewById(R.id.badge_counter);
        // set the pending notifications value
        badgeCounter.setText(String.valueOf(pendingNotifications));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menu.findItem(R.id.action_notification));
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_drawable_home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                    CallFragment(new DashboardFragment(), "Delivery Expert");
                }else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.right_drawable_profile:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                    CallFragment(new EditProfileFragment(), "Profile");
                }else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
                //startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
//                CallFragment(new ProfileFragment(),"Profile");
                break;
            case R.id.right_drawable_report:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                    CallFragment(new ReportFragment(), "Report");
                }else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.right_drawable_history:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
//                startActivity(new Intent(getApplicationContext(), TestingActivity.class));
                break;
            case R.id.right_drawable_orders:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (CommonUtil.IsInternetConnected(getApplicationContext())) {
                    CallFragment(new TodayOrdersFragment(), "Todays Order");
                }else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.right_drawable_logout:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                AppTypeDetails.getInstance(getApplicationContext()).Logout();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                break;
        }
    }

    private void CallFragment(Fragment fragment , String title){
        this.fragment = fragment;

        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dashboardfragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        setToolbarTitle(title);

    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    @BindView(R.id.message)
    TextView messageTv;
    @BindView(R.id.pr) View progressCard;

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

//    @Override
//    protected void onDestroy() {
//        if (CommonUtil.IsInternetConnected(getApplicationContext())) {
//            if (AppTypeDetails.getInstance(getApplicationContext()).getIsOnline()) {
//                ApisCommon.Call_Online_Status_Api(DashboardActivity.this, AppTypeDetails.
//                        getInstance(getApplicationContext()).getUserID(), "0", progressCard);
//            }
//        }
//        super.onDestroy();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        CallFragment(new DashboardFragment(),"Delivery Expert");
    }

    public static void setBadgeCounter(int count) {
        if (count>0){
            isRemainingAvaliable=true;
        }
        badgeCounter.setText(""+count);
    }
}
