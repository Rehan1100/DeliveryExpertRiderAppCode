package com.DeliverExpertRiderApp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.CommonResponse;
import com.DeliverExpertRiderApp.ui.activities.DashboardActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.expertorider.R;
import com.DeliverExpertRiderApp.Utils.AppTypeDetails;
import com.DeliverExpertRiderApp.Utils.CommonUtil;
import com.DeliverExpertRiderApp.Utils.VolleyMultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_SHORT;
import static com.DeliverExpertRiderApp.Utils.CommonUtil.getFileDataFromDrawable;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 123;
    private String mParam1;
    private String mParam2 , UserId = "";

    private Bitmap bitmap=null;
    @BindView(R.id.edit_img_circle) CircleImageView edit_img_circle;

    CircleImageView imageViewEdit;

    @BindView(R.id.fullname_edit_profile) TextView fullname_edit_profile;
    @BindView(R.id.email_edit_profile) TextView email_edit_profile;
    @BindView(R.id.address_edit_profile) TextView address_edit_profile;
    @BindView(R.id.phone_number_edit_profile) TextView phone_number_edit_profile;
    @BindView(R.id.change_password_edit_profile) TextView change_password;

    @BindView(R.id.edit_btn_save) CardView btn_save;

    File file = null;
    public AppCompatActivity context;
    Uri imageUri;
    String encodedImageString;
    private String img_path;

    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        context= (AppCompatActivity) getContext();

        UserId = AppTypeDetails.getInstance(getContext()).getUserID().trim();

        address_edit_profile.setText(AppTypeDetails.getInstance(getContext()).getUserAddress());
        fullname_edit_profile.setText(AppTypeDetails.getInstance(getContext()).getUserName());
        email_edit_profile.setText(AppTypeDetails.getInstance(getContext()).getUserEmail());
        phone_number_edit_profile.setText(AppTypeDetails.getInstance(getContext()).getUserPhoneNumber());



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.mipmap.ic_launcher);

        Glide.with(this)
                .load(ApiConstants.BASE_IMAGE_URL+AppTypeDetails.getInstance(getContext()).getUserImage())
                .apply(requestOptions)
                .into(edit_img_circle);




        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPasswordDialog();
            }
        });
        edit_img_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditProfilePhotoDialog();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = fullname_edit_profile.getText().toString().trim();
                String email_ = email_edit_profile.getText().toString().trim();
                String phoneNumber_ = phone_number_edit_profile.getText().toString().trim();
                String address_ = address_edit_profile.getText().toString().trim();
                String password_ = AppTypeDetails.getInstance(getContext()).getUserPassword().trim();

                if (name_.isEmpty()) {
                    Toast.makeText(getContext(), "Empty Name Field!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (CommonUtil.isValidEmail(email_)) {
                        if (CommonUtil.isNumberValid(phoneNumber_)) {
                            if (address_.isEmpty()) {
                                Toast.makeText(getContext(), "Empty Address Field !!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (CommonUtil.IsInternetConnected(getContext())) {
                                    showProgress("Updating ...");
                                    CallUpdateApi(name_, email_, phoneNumber_, address_, password_);
                                }else {
                                    Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            Toast.makeText(getContext(), "Invalid Phone Number", LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Invalid Email Address !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void CallUpdateApi(String name_, String email_, String phoneNumber_, String address_, String password_) {
        Call<CommonResponse> call = RestClient.getClient().update_profile(""+UserId,
                ""+name_,""+email_,""+phoneNumber_,""+address_ , ""+password_);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        hideProgress();
                        AppTypeDetails.getInstance(getContext()).setUserAddress(address_);
                        AppTypeDetails.getInstance(getContext()).setUserName(name_);
                        AppTypeDetails.getInstance(getContext()).setUserEmail(email_);
                        AppTypeDetails.getInstance(getContext()).setUserPhoneNumber(phoneNumber_);
                        AppTypeDetails.getInstance(getContext()).setUserPassword(password_);
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        CallFragment(new ProfileFragment(),"Profile");
                    }else {
                        hideProgress();
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    hideProgress();
                    Toast.makeText(getContext(), "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void EditPasswordDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_password_dialog_layout, null);
        dialog.setView(dialogView);

        final EditText CurrentPassword = dialogView.findViewById(R.id.current_password_text_input_layout_change_pass);
        final EditText NewPassword = dialogView.findViewById(R.id.new_password_text_input_layout_change_pass);
        final EditText ConfirmNewPassword = dialogView.findViewById(R.id.confirm_password_text_input_layout_change_pass);

        final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel_change_pass);
        final Button btn_submit = dialogView.findViewById(R.id.btn_update_change_pass);


        dialog.setCancelable(false);

        final AlertDialog alert = dialog.create();
        alert.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_ = AppTypeDetails.getInstance(getContext()).getUserName().trim();
                String email_ = AppTypeDetails.getInstance(getContext()).getUserEmail().trim();
                String phoneNumber_ = AppTypeDetails.getInstance(getContext()).getUserPhoneNumber().trim();
                String address_ = AppTypeDetails.getInstance(getContext()).getUserAddress().trim();


                String current_ = CurrentPassword.getText().toString().trim();
                String new_pass = NewPassword.getText().toString().trim();
                String confirm_pass = ConfirmNewPassword.getText().toString().trim();

                if (current_.isEmpty() || current_ == "") {
                    CurrentPassword.setError("Empty Field !!!");
                } else {
                    if (current_.equals(AppTypeDetails.getInstance(getContext()).getUserPassword())
                            || current_ == AppTypeDetails.getInstance(getContext()).getUserPassword()) {
                        if (new_pass.isEmpty() || new_pass == "") {
                            NewPassword.setError("Empty Field !!!");
                        } else {

                            if (new_pass.length() > 4) {
                                if (new_pass.equals(confirm_pass)) {
                                    showProgress("Updating Password...");
                                    CallUpdateApi(name_, email_, phoneNumber_, address_, new_pass);
                                } else {
                                    ConfirmNewPassword.setError("Re-Enter password Not Match !");
                                    Toast.makeText(getContext(), "Re-Enter password Not Match !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                NewPassword.setError("Password minimum character 4 !!!");
                            }
                        }
                    } else {
                        CurrentPassword.setError("Current Password not match");
                        Toast.makeText(getContext(), "Current Password not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

    }


    private void EditProfilePhotoDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_image_profile_dialog_layout, null);
        dialog.setView(dialogView);

        imageViewEdit = dialogView.findViewById(R.id.edit_img_dialog);
        Button CallGallery = dialogView.findViewById(R.id.btn_call_galary);


        CallGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        dialog.setCancelable(false);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int id) {

                if (bitmap != null) {
                    showProgress("Updating Image ...");
                    UpdateProfileImageThroughVolley();
                }
                    /*RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", file.getName(), requestFile);

// add another part within the multipart request
                    RequestBody fullName =
                            RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");
*/
//                    service.updateProfile(id, fullName, body, other);


//                    Call<CommonResponse> call = RestClient.getClient().update_profile_image(UserId+"",body);
//                    call.enqueue(new Callback<CommonResponse>() {
//                        @Override
//                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//                            if (response.isSuccessful()){
//                                if (response.body().getStatus()==1){
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<CommonResponse> call, Throwable t) {
//
//                        }
//                    });

//                    CallUpdateImage()
                else {
                    Toast.makeText(getContext(), "Image not select", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            imageViewEdit.setImageBitmap(bitmap);





        }
    }




    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
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


//    private File createTempFile() {
//        return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + pic + "_image.jpeg");
//    }

    private void CallFragment(Fragment fragment , String title){
//        Fragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dashboardfragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DashboardActivity.fragment = fragment;
        getActivity().setTitle(title);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }




    private void UpdateProfileImageThroughVolley() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                ApiConstants.BASE_SERVER_URL + ApiConstants.UpdateProfile,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //  JSONObject obj = new JSONObject(new String(response.data));
                      //  submitBtn.setEnabled(true);
                        hideProgress();
                        CallFragment(new ProfileFragment(),"Profile");

                        Toast.makeText(getContext(), "Udate Sucess", LENGTH_SHORT).show();


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error : exp " + error.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        }) {
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("files", new DataPart(imagename + ".png", getFileDataFromDrawable(getBitmap())));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("rider_id",UserId);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }

}
