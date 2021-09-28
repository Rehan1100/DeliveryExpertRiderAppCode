package com.DeliverExpertRiderApp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.Model.SettingsResponse;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.ui.activities.DashboardActivity;
import com.expertorider.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashFragment extends Fragment {


    @BindView(R.id.img_splash) ImageView imageView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Animation uptodown, downtoup;


    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        ButterKnife.bind(this,view);


        uptodown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        downtoup = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        imageView.setAnimation(downtoup);
        CallgetSettings();
        Thread timer  = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                    if (AppTypeDetails.getInstance(getContext()).isLoggedIn()==true){
                        startActivity(new Intent(getActivity(), DashboardActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        );
                    }else {
                        goToLogin();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        timer.start();


        return view;
    }


    private void goToLogin(){
        Fragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("title", "Login");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainfragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    void CallgetSettings() {

        Call<SettingsResponse> call = RestClient.getClient().get_settings("");
        call.enqueue(new Callback<SettingsResponse>() {

            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {

                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    ApiConstants.settings=response.body().getSettings();

                } else {

                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
