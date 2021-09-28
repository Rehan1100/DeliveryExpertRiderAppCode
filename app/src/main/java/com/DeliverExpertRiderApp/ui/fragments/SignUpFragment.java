package com.DeliverExpertRiderApp.ui.fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.expertorider.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.btn_signUp) Button buttonSignUp;
    @BindView(R.id.editText_name_signup) EditText editTextName;
    @BindView(R.id.editText_email_signup) EditText editTextEmail;
    @BindView(R.id.editText_password_signup) EditText editTextPassword;
    @BindView(R.id.call_login_fragment) LinearLayout call_login_fragment;
    @BindView(R.id.imagebacklogin) ImageView imageViewBack;


    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ButterKnife.bind(this,view);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        editTextEmail.setAnimation(animation);
        editTextName.setAnimation(animation);
        editTextPassword.setAnimation(animation);
        buttonSignUp.setAnimation(animation);
        call_login_fragment.setAnimation(animation);
        call_login_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = editTextName.getText().toString().trim();
                String email_ = editTextEmail.getText().toString().trim();
                String pass_ = editTextPassword.getText().toString().trim();

                if (name_.isEmpty()){
                    Toast.makeText(getContext(), "Name is Empty!!", Toast.LENGTH_SHORT).show();
                }else {
                    if (email_.isEmpty()) {
                        editTextEmail.setError("Empty Email !!");
                    } else {
                        if (CommonUtil.isValidEmail(email_)) {
                            if (pass_.isEmpty()) {
                                editTextPassword.setError("Empty Password");
                            } else {
                                if (pass_.length() > 5) {
                                    //  Sucess SignUp
                                } else {
                                    editTextPassword.setError("Weak Password (min 6 character)*");
                                }
                            }
                        } else {
                            editTextEmail.setError("Invalid Email!!");
                        }
                    }
                }
            }
        });

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
}
