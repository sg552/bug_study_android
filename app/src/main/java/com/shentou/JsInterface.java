package com.shentou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class JsInterface {
  Activity activity;

  JsInterface(Activity activity) {
    this.activity = activity;
  }

  public void openImage(String image_url){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.parse(image_url), "image/*"); // The Mime type can actually be determined from the file
    activity.startActivity(intent);
  }

}
