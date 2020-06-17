package com.shentou;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.shentou.beans.LoginResult;
import com.shentou.beans.PostResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
  public static final String TAG = "LoginActivity";
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getSupportActionBar().hide();

    findViewById(R.id.login_submit).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.login_submit:
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        validateLogin(email, password);
        break;
      default:
        break;

    }

  }

  public void validateLogin(String email, String password){
    OkHttpClient client = new OkHttpClient();
    String url = Constants.URL_VALIDATE_LOGIN + "?email="+email+"&password="+password;
    final Activity that = this;
    Log.i(TAG, "== in validateLogin, urL " + url);
    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

    client.newCall(request)
            .enqueue(new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                e.printStackTrace();
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Gson gson = new Gson();
                final LoginResult loginResult = gson.fromJson(result, LoginResult.class);

                runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    if(loginResult.result.equals("success") ){
                      saveUserId(loginResult.user_id);
                      Intent intent = new Intent(that, MainActivity.class);
                      startActivity(intent);
                    }else{
                      Toast.makeText(that, "用户名与密码不匹配", Toast.LENGTH_SHORT).show();
                    }
                  }
                });
              }
            });
  }

  public void saveUserId(int userId){
    SharedPreferences settings = getSharedPreferences(Constants.KEY_USER_ID, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(Constants.KEY_USER_ID, userId);
    editor.commit();
  }
}
