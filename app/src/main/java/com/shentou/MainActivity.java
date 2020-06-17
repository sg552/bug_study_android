package com.shentou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(isNotLoggedIn()){
      openLoginPage();
    }else{
      loadView(savedInstanceState);
    }

  }

  public boolean isNotLoggedIn(){
    SharedPreferences settings = getSharedPreferences(Constants.KEY_USER_ID, 0);
    int userId = settings.getInt(Constants.KEY_USER_ID, 0);
    return userId == 0 ;
  }

  public void openLoginPage(){
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }

  public void loadView(Bundle bundle){
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();
  }
}
