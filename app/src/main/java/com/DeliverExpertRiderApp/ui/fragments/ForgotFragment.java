package com.DeliverExpertRiderApp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.CommonResponse;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.expertorider.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    @BindView(R.id.btn_proceed_forgot) Button buttonProceed;
    @BindView(R.id.editText_email_forgot) EditText editTextEmail;
    @BindView(R.id.call_login_fragment) LinearLayout call_login_fragment;


    public static ForgotFragment newInstance(String param1, String param2) {
        ForgotFragment fragment = new ForgotFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        ButterKnife.bind(this,view);

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_=editTextEmail.getText().toString().trim();

                if (CommonUtil.isValidEmail(email_)){

                    if (CommonUtil.IsInternetConnected(getContext())) {

                        showProgress("Loading...");
                        Call<CommonResponse> call = RestClient.getClient().forget_password(email_+"");
                        call.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus() == 1) {
                                        hideProgress();
                                        CallFragment(new LoginFragment(), "Login");
                                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        hideProgress();
                                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    hideProgress();
                                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                hideProgress();
                                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Invalid Email Address!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        call_login_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallFragment(new LoginFragment(), "Login");
            }
        });

        return view;
    }


    private void CallFragment(Fragment fragment , String title){
//        Fragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainfragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

//        if (!AppTypeDetails.getInstance(getContext()).getIsOnline()){
//            getActivity().setTitle("You are Offline");
//        }else {
//            getActivity().setTitle(title);
//        }
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
