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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.ui.activities.DashboardActivity;
import com.DeliverExpertRiderApp.Communications.response.UserDataResponse;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    @BindView(R.id.btn_login)  Button buttonLogin;
    @BindView(R.id.editText_email_login) EditText editTextEmail;
    @BindView(R.id.editText_password_login) EditText editTextPassword;
    @BindView(R.id.call_signUp_fragment) LinearLayout call_signUp_fragment;


    Animation animation;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        ButterKnife.bind(this,view);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        buttonLogin.setAnimation(animation);
        editTextEmail.setAnimation(animation);
        editTextPassword.setAnimation(animation);
        call_signUp_fragment.setAnimation(animation);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email_ = editTextEmail.getText().toString().trim();
                String pass_ = editTextPassword.getText().toString().trim();

                if (email_.isEmpty()){
                    Toast.makeText(getContext(), "Empty Email !!", Toast.LENGTH_SHORT).show();
                }else {
                    if (CommonUtil.isValidEmail(email_)){
                        if (pass_.isEmpty()){
                            editTextPassword.setError("Empty Password");
                        }else {
                            if (pass_.length()>3){
                                //  Sucess Login
                              if (CommonUtil.IsInternetConnected(getContext())){
                                 showProgress("Signing ...");
                                  CallLoginApi(email_,pass_);
                              }else {
                                  Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                              }

                          //      CallLoginApi(email_,pass_);
                            }else {
                                editTextPassword.setError("Invalid Password ");
                            }
                        }
                    }else {
                        editTextEmail.setError("Invalid Email!!");
                    }
                }
            }
        });

        call_signUp_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUp();
            }
        });
        return view;
    }

    private void goToSignUp(){
        Fragment fragment = new ForgotFragment();
        Bundle args = new Bundle();
        args.putString("title", "Forgot");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainfragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    void CallLoginApi(String email , String pass){
        Call<UserDataResponse> call = RestClient.getClient().login(email,pass);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()&&response.body().getStatus()==1)
                {
                    AppTypeDetails.getInstance(getContext()).setUserName(response.body().Userdata.getName());
                    AppTypeDetails.getInstance(getContext()).setUserEmail(response.body().Userdata.getEmail());
                    AppTypeDetails.getInstance(getContext()).setUserPhoneNumber(response.body().Userdata.getPhone());
                    AppTypeDetails.getInstance(getContext()).setUserAddress(response.body().Userdata.getAddress());
                    AppTypeDetails.getInstance(getContext()).setUserID(response.body().Userdata.getUserId());
//                    AppTypeDetails.getInstance(getContext()).setUserPassword(response.body().Userdata.getPassword());
                    AppTypeDetails.getInstance(getContext()).setUserPassword(pass);
                    AppTypeDetails.getInstance(getContext()).setUserImage(response.body().Userdata.getImage());
                    Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    hideProgress();
                    startActivity(new Intent(getActivity(), DashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    );
                } else {
                    Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   hideProgress();

                }
            }
            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
               hideProgress();
            }
        });
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
}
