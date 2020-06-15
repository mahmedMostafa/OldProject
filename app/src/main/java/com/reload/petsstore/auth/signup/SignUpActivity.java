package com.reload.petsstore.auth.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reload.petsstore.R;
import com.reload.petsstore.auth.AuthModel.SignUpResponse;
import com.reload.petsstore.auth.forgetpass.ForgetPassActivity;
import com.reload.petsstore.auth.login.LoginActivity;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.ActivitySignUpBinding;
import com.reload.petsstore.homecategory.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    SessionMangment mSessionMangment;

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        mSessionMangment = new SessionMangment(this);
        initViews();
    }

    private void initViews() {
        binding.loginBtn.setOnClickListener(this);
        binding.signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.signup_btn:
                if (validateParams())
                    callRegisterApi();
                break;
        }
    }

    boolean validateParams() {
        if (binding.fnameEt.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your first name", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.lnameEt.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your last name", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.emailEt.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your E-mail", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.phoneEt.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your phone", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.passEt.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your pass", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.passEt.getText().toString().length() < 6) {
            binding.passEt.setError("pass < 6");
            Toast.makeText(SignUpActivity.this, "Please enter a pass more than 6 chars", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    void callRegisterApi() {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<SignUpResponse> call = apiService.reigister
                (binding.fnameEt.getText().toString(),
                        binding.lnameEt.getText().toString(),
                        binding.phoneEt.getText().toString(),
                        binding.emailEt.getText().toString(),
                        binding.passEt.getText().toString());

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Log.e("body" ,response.message());
                Log.e("body" ,response.body()+"");
                Log.e("body" ,response.errorBody()+"");
                Log.e("body" ,response.message()+"");

                if (response.body() != null) {

                if (response.body().getStatus()) {
                    mSessionMangment.createLoginSession(
                            response.body().getStatus(),
                            response.body().getResult().get(0).getId(),
                            response.body().getResult().get(0).getFirstName(),
                            response.body().getResult().get(0).getLastName(),
                            response.body().getResult().get(0).getEmail(),

                            response.body().getResult().get(0).getPhone(),
                            response.body().getResult().get(0).getImage());

                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    SignUpActivity.this.finish();
                } else {
                    Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }}
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

            }
        });
    }
}
