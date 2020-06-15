package com.reload.petsstore.auth.login;

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
import com.reload.petsstore.auth.signup.SignUpActivity;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.ActivityLoginBinding;
import com.reload.petsstore.homecategory.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    SessionMangment mSessionMangment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mSessionMangment = new SessionMangment(this);
        initViews();
    }

    private void initViews() {
        binding.loginBtn.setOnClickListener(this);
        binding.signupBtn.setOnClickListener(this);
        binding.forgetPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn:
                if (validateParams())
                    callLoginApi();
                break;

            case R.id.signup_btn:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

                break;

            case R.id.forget_pass:
                startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));

                break;
        }
    }

    boolean validateParams() {
        if (binding.emailEt.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your E-mail", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.passEt.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your pass", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.passEt.getText().toString().length() < 6) {
            Toast.makeText(LoginActivity.this, "Please enter a pass more than 6 chars", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    void callLoginApi() {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<SignUpResponse> call = apiService.login(
                binding.emailEt.getText().toString(),
                binding.passEt.getText().toString());

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Log.e("body", response.message());
                Log.e("body", response.body() + "");
                Log.e("body", response.errorBody() + "");
                Log.e("body", response.message() + "");

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

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

            }
        });

    }
}
