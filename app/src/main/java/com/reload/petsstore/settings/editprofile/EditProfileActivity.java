package com.reload.petsstore.settings.editprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reload.petsstore.R;
import com.reload.petsstore.auth.AuthModel.SignUpResponse;
import com.reload.petsstore.auth.signup.SignUpActivity;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.ActivityEditProfileBinding;
import com.reload.petsstore.fav.Model.AddFav;
import com.reload.petsstore.homecategory.HomeActivity;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    SessionMangment mSessionMangment;
    int PICKFILE_RESULT_CODE = 2;

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        mSessionMangment = new SessionMangment(this);
        initViews();
        setDataFromSharedPrefences();
    }

    private void initViews() {
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateParams())
                    callEditProfileApi();
            }
        });
        binding.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
    }


    void setDataFromSharedPrefences() {
        binding.fnameEt.setText(mSessionMangment.getUserDetails().get(SessionMangment.KEY_FNAME));
        binding.lnameEt.setText(mSessionMangment.getUserDetails().get(SessionMangment.KEY_LNAME));
        binding.emailEt.setText(mSessionMangment.getUserDetails().get(SessionMangment.KEY_EMAIL));
        binding.phoneEt.setText(mSessionMangment.getUserDetails().get(SessionMangment.KEY_PHONE));
        Glide.with(EditProfileActivity.this).load(mSessionMangment.getUserDetails().get(SessionMangment.KEY_IMAGE))
                .placeholder(getResources().getDrawable(R.drawable.avatar))
                .into(binding.userProfile);
    }

    boolean validateParams() {
        if (binding.fnameEt.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please enter your first name", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.lnameEt.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please enter your last name", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.emailEt.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please enter your E-mail", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.phoneEt.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please enter your phone", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    void callEditProfileApi() {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<SignUpResponse> call = apiService.editProfile(
                mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID),
                binding.fnameEt.getText().toString(),
                binding.lnameEt.getText().toString(),
                binding.phoneEt.getText().toString(),
                binding.emailEt.getText().toString());

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Log.e("body", response.message());
                Log.e("body", response.body() + "");
                Log.e("body", response.errorBody() + "");
                Log.e("body", response.message() + "");


                if (response.body().getStatus()) {
                    mSessionMangment.clearData();
                    mSessionMangment.createLoginSession(
                            response.body().getStatus(),
                            response.body().getResult().get(0).getId(),
                            response.body().getResult().get(0).getFirstName(),
                            response.body().getResult().get(0).getLastName(),
                            response.body().getResult().get(0).getEmail(),
                            response.body().getResult().get(0).getPhone(),
                            response.body().getResult().get(0).getImage());
                    Toast.makeText(EditProfileActivity.this, "Profile Updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

            }
        });

    }


    //-----------------------------------------------------------------------------------------


    private void openFile() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
        String[] galleryPermissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            galleryPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }
    }

    private String filePath;
    ArrayList<String> ImagePaths = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICKFILE_RESULT_CODE) {

                Uri picUri = data.getData();
                filePath = getPath(picUri, this);
                if (ImagePaths.size() != 0) {
                    ImagePaths.add(ImagePaths.size(), filePath);

                } else {
                    ImagePaths.add(filePath);
                }
                Bitmap bitmapImage = BitmapFactory.decodeFile(ImagePaths.get(0));
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                binding.userProfile.setImageBitmap(scaled);
                UploadImage();


            } else {
                //     Toast.makeText(getActivity(), "يجب عليك إختيار الملف أولا", Toast.LENGTH_SHORT).show();


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Choose Canceld", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadImage() {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        ArrayList<String> StringImage = new ArrayList<>();
        for (int x = 0; x < ImagePaths.size(); x++) {
            ImageView myImage = new ImageView(this);
            if (!ImagePaths.get(x).contains("assets")) {
                Bitmap bitmapImage = BitmapFactory.decodeFile(ImagePaths.get(x));
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                myImage.setImageBitmap(scaled);
                imageViews.add(myImage);
                StringImage.add(ImagePaths.get(x));
            }
        }
        if (StringImage.get(0).isEmpty()) {
        } else {
            uploadImageToServer(StringImage.get(0));
        }
    }

    public static String getPath(Uri contentUri, Activity v) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(v, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    public void uploadImageToServer(String filePath) {

        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);

        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            Log.i("okhttp", "uploadVideoToServer: " + imageFile.getName());
        }
        MultipartBody.Part imagePart = getImageRequestPart(imageFile);
        retrofit2.Call<AddFav> videoResponseCall = apiService.uploadImage(mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID), imagePart);
        videoResponseCall.enqueue(new Callback<AddFav>() {
            @Override
            public void onResponse(retrofit2.Call<AddFav> call, Response<AddFav> response) {
                try {
                    // if response is not like Response pojo so it will throw exception
                    Log.i("okhttp", "onResponse: " + response.body().getMsg());
                    Log.i("okhttp", "onResponse: " + response.body().getResult() + "");
                } catch (Exception e) {
                    Log.i("okhttp", "onResponse: exception " + e.getMessage());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AddFav> call, Throwable t) {
                Log.i("okhttp", "onFailure: " + t.getMessage());
                Log.i("okhttp", "onFailure: " + t.toString());
                t.printStackTrace();
            }
        });

    }

    private MultipartBody.Part getImageRequestPart(File imageFile) {
        RequestBody imageParam = RequestBody.create(MediaType.parse("image/*"), imageFile);
        CountingRequestBody.Listener imageListener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                float percentage = 100f * bytesWritten / contentLength;
                Log.i("okhttp", "onRequestProgress: image= " + percentage);
            }
        };

        CountingRequestBody countingRequestImage = new CountingRequestBody(imageParam, imageListener);
        return MultipartBody.Part.createFormData("image", imageFile.getName(), countingRequestImage);
    }

}
