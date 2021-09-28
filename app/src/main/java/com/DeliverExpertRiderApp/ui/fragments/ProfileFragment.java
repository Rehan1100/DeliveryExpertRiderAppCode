package com.DeliverExpertRiderApp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.ui.activities.DashboardActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.expertorider.R;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    @BindView(R.id.profile_address)
    TextView profile_address;
    @BindView(R.id.profile_name)
    TextView profile_name;
    @BindView(R.id.profile_number)
    TextView profile_number;
    @BindView(R.id.profile_email)
    TextView profile_email;
    @BindView(R.id.profile_edit)
    TextView profile_edit;
    @BindView(R.id.rider_profile_CollectedCash)
    TextView rider_profile_CollectedCash;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        profile_email.setText(AppTypeDetails.getInstance(getContext()).getUserEmail());
        profile_name.setText(AppTypeDetails.getInstance(getContext()).getUserName());
        profile_number.setText(AppTypeDetails.getInstance(getContext()).getUserPhoneNumber());
        profile_address.setText(AppTypeDetails.getInstance(getContext()).getUserAddress());
        if (AppTypeDetails.getInstance(getContext()).getCashCollected().isEmpty()) {
            rider_profile_CollectedCash.setText("PKR 0");
        } else {
            rider_profile_CollectedCash.setText("PKR " + NumberFormat.getNumberInstance(Locale.getDefault())
                    .format(Double.parseDouble(AppTypeDetails.getInstance(getContext()).getCashCollected())));
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.mipmap.ic_launcher);

        Glide.with(this)
                .load(ApiConstants.BASE_IMAGE_URL+AppTypeDetails.getInstance(getContext()).getUserImage())
                .apply(requestOptions)
                .into(profile_image);

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          //      startActivity(new Intent(getActivity(), EditProfileActivity.class));
             CallFragment(new EditProfileFragment(),"Edit Profile");

            }
        });


        return view;
    }

    private void CallFragment(Fragment fragment, String title) {
//        Fragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dashboardfragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DashboardActivity.fragment = fragment;
        getActivity().setTitle(title);
    }

}
