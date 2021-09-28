package com.DeliverExpertRiderApp.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.DeliverExpertRiderApp.Communications.response.UserDataResponse;
import com.DeliverExpertRiderApp.Utils.VolleyMultipartRequest;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_SHORT;
import static com.DeliverExpertRiderApp.Utils.CommonUtil.getFileDataFromDrawable;

public class EditProfileActivity extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 123;
    private Bitmap bitmap;
    @BindView(R.id.edit_img_circle)
    CircleImageView edit_img_circle;

    CircleImageView imageViewEdit;

    @BindView(R.id.fullname_edit_profile)
    TextView fullname_edit_profile;
    @BindView(R.id.email_edit_profile)
    TextView email_edit_profile;
    @BindView(R.id.address_edit_profile)
    TextView address_edit_profile;
    @BindView(R.id.phone_number_edit_profile)
    TextView phone_number_edit_profile;
    @BindView(R.id.change_password_edit_profile)
    TextView change_password;

    @BindView(R.id.edit_btn_save)
    CardView btn_save;

    File file = null;
    String UserId = "";

    AppCompatActivity context= (AppCompatActivity) getContext();
    public Uri imageURI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        ButterKnife.bind(this, view);


        UserId = AppTypeDetails.getInstance(context).getUserID().trim();

        address_edit_profile.setText(AppTypeDetails.getInstance(context).getUserAddress());
        fullname_edit_profile.setText(AppTypeDetails.getInstance(context).getUserName());
        email_edit_profile.setText(AppTypeDetails.getInstance(context).getUserEmail());
        phone_number_edit_profile.setText(AppTypeDetails.getInstance(context).getUserPhoneNumber());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.drawable.no_img);

        Glide.with(context)
                .load(ApiConstants.BASE_IMAGE_URL + AppTypeDetails.getInstance(context).getUserImage())
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
                String password_ = AppTypeDetails.getInstance(context).getUserPassword().trim();

                if (name_.isEmpty()) {
                    Toast.makeText(context, "Empty Name Field!!", Toast.LENGTH_SHORT).show();

                } else {
                    if (CommonUtil.isValidEmail(email_)) {
                        if (CommonUtil.isNumberValid(phoneNumber_)) {
                            if (address_.isEmpty()) {
                                Toast.makeText(context, "Empty Address Field !!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (CommonUtil.IsInternetConnected(context)) {
                                    showProgress("Updating ...");
                                    CallUpdateApi(name_, email_, phoneNumber_, address_, password_, false);
                                } else {
                                    Toast.makeText(context, "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(context, "Invalid Phone Number", LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Invalid Email Address !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }


    private void CallUpdateApi(String name_, String email_, String phoneNumber_, String address_, String password_, boolean isPasswordReset) {
        Call<CommonResponse> call = null;

        if (isPasswordReset) {
            call = RestClient.getClient().update_profile("" + UserId,
                    "" + name_, "" + email_, "" + phoneNumber_, "" + address_, "" + password_);
        } else {
            call = RestClient.getClient().update_profile("" + UserId,
                    "" + name_, "" + email_, "" + phoneNumber_, "" + address_);
        }


        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        hideProgress();
                        AppTypeDetails.getInstance(context).setUserAddress(address_);
                        AppTypeDetails.getInstance(context).setUserName(name_);
                        AppTypeDetails.getInstance(context).setUserEmail(email_);
                        AppTypeDetails.getInstance(context).setUserPhoneNumber(phoneNumber_);
                        if (isPasswordReset) {
                            AppTypeDetails.getInstance(context).setUserPassword(password_);
                        }
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(context, DashboardActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        );
//                        CallFragment(new ProfileFragment(),"Profile");
                    } else {
                        hideProgress();
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgress();
                    Toast.makeText(context, "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(context, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void EditPasswordDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
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

                String name_ = AppTypeDetails.getInstance(context).getUserName().trim();
                String email_ = AppTypeDetails.getInstance(context).getUserEmail().trim();
                String phoneNumber_ = AppTypeDetails.getInstance(context).getUserPhoneNumber().trim();
                String address_ = AppTypeDetails.getInstance(context).getUserAddress().trim();


                String current_ = CurrentPassword.getText().toString().trim();
                String new_pass = NewPassword.getText().toString().trim();
                String confirm_pass = ConfirmNewPassword.getText().toString().trim();

                if (current_.isEmpty() || current_ == "") {
                    CurrentPassword.setError("Empty Field !!!");
                } else {
                    if (current_.equals(AppTypeDetails.getInstance(context).getUserPassword())
                            || current_ == AppTypeDetails.getInstance(context).getUserPassword()) {
                        if (new_pass.isEmpty() || new_pass == "") {
                            NewPassword.setError("Empty Field !!!");
                        } else {

                            if (new_pass.length() > 4) {
                                if (new_pass.equals(confirm_pass)) {
                                    if (CommonUtil.IsInternetConnected(context)) {
                                        showProgress("Updating Password...");
                                        CallUpdateApi(name_, email_, phoneNumber_, address_, new_pass, true);
                                    }else {
                                        Toast.makeText(context, "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ConfirmNewPassword.setError("Re-Enter password Not Match !");
                                    Toast.makeText(context, "Re-Enter password Not Match !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                NewPassword.setError("Password minimum character 4 !!!");
                            }
                        }
                    } else {
                        CurrentPassword.setError("Current Password not match");
                        Toast.makeText(context, "Current Password not match", Toast.LENGTH_SHORT).show();
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
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_image_profile_dialog_layout, null);
        dialog.setView(dialogView);

        imageViewEdit = dialogView.findViewById(R.id.edit_img_dialog);
        Button CallGallery = dialogView.findViewById(R.id.btn_call_galary);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loadimg);
        requestOptions.error(R.drawable.no_img);
        Glide.with(context)
                .load(ApiConstants.BASE_IMAGE_URL + AppTypeDetails.getInstance(context).getUserImage())
                .apply(requestOptions)
                .into(imageViewEdit);

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

                if (getFile() != null) {
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), getFile());

// MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("files", file.getName(), requestFile);

// add another part within the multipart request
                    RequestBody fullName =
                            RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");

                    RequestBody UserIdRequest =
                            RequestBody.create(MediaType.parse("multipart/form-data"), UserId);

//                    service.updateProfile(id, fullName, body, other);

                    if (CommonUtil.IsInternetConnected(context)) {
                        showProgress("Updating Image ...");
//                    UpdateProfileImageThroughVolley();

                        Call<UserDataResponse> call = RestClient.getClient().update_profile_image(UserIdRequest, body);
                        call.enqueue(new Callback<UserDataResponse>() {
                            @Override
                            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus() == 1) {
                                        hideProgress();
                                        startActivity(new Intent(context, DashboardActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        );
                                        AppTypeDetails.getInstance(context).setUserImage(response.body().Userdata.getImage());
                                        Toast.makeText(context, "" + response.body().getMessage(), LENGTH_SHORT).show();
                                    } else {
                                        hideProgress();
                                        Toast.makeText(context, "" + response.body().getMessage(), LENGTH_SHORT).show();
                                    }
                                } else {
                                    hideProgress();
                                    Toast.makeText(context, "Check Internet Connection", LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                                hideProgress();
                                Toast.makeText(context, "" + t, LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(context, "Check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    }

//                    CallUpdateImage()
                } else {
                    Toast.makeText(context, "Image not select", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             imageURI = data.getData();

            CropImage.activity(imageURI)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(context);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

//                edited_pic_uri = resultUri + "";
//                imageViewEdit.setImageURI(imageURI);

                File thumb_filePath = new File(resultUri.getPath());

                Bitmap bitmap = new Compressor(context)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                setFile(thumb_filePath);

                //getting bitmap object from uri
                //     final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                //displaying selected image to imageview
                imageViewEdit.setImageBitmap(bitmap);
                setBitmap(bitmap);
                //   Toast.makeText(this, "onActivity Bitmap : "+bitmap, Toast.LENGTH_SHORT).show();
                this.bitmap = bitmap;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
    @BindView(R.id.pr_edit)
    public View progressCard;

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
                        startActivity(new Intent(context, DashboardActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        );

                        Toast.makeText(context, "Udate Sucess : ", LENGTH_SHORT).show();


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error : exp " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("rider_id", UserId);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }



}
