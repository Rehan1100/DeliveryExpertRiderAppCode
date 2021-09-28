package com.DeliverExpertRiderApp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.expertorider.R;
import com.DeliverExpertRiderApp.ui.fragments.LoginFragment;
import com.DeliverExpertRiderApp.ui.fragments.SplashFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.mainfragmentContainer);
        addFragment();
    }

    private void addFragment() {


        fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putString("title", "Splash");
        fragment.setArguments(args);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainfragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (fragment instanceof LoginFragment) {
            finish();
        } else {
            fragment = new LoginFragment();

            Bundle args = new Bundle();
            args.putString("title", "Dashboard");
            fragment.setArguments(args);

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainfragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
