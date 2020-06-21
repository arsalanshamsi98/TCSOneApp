package com.example.tcsoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tcsoneapp.retrofit.RequestBuilder;
import com.example.tcsoneapp.retrofit.RetrofitService;
import com.example.tcsoneapp.utils.AppConstants;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    MainActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText inputEmpId;
    CardView cardViewLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        inputEmpId = findViewById(R.id.input_emp_id);
        cardViewLogin = findViewById(R.id.btn_login);
        cardViewLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            loginUser();
        }
    }

    private void loginUser() {
        RequestBuilder requestBuilder = new RequestBuilder()
                .setJsonObjest(new JSONObject())
                .createJsonObject(AppConstants.AUTHENTICATION)
                .addBody();

        requestBuilder.create_authenticateUserRequest(inputEmpId.getText().toString());
        JsonObject request = requestBuilder.build();

        Call<ResponseBody> call = RetrofitService.getService(this).loginUser(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Log.i("Success" , Objects.requireNonNull(response.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}