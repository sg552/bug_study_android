package com.shentou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shentou.beans.PostDetail;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailsActivity extends AppCompatActivity implements View.OnClickListener {

  public String getUserId(){
    return 1 + "";
  }

  public static final String TAG = "PostDetailsActivity";
  WebView webView;

  String HTML_HEAD_CONTENT = "  <head>\n" +
          "    <meta charset=\"utf-8\" />\n" +
          "    <meta name=\"apple-mobile-web-app-title\" content=\"\">\n" +
          "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\n" +
          "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=3.0, minimum-scale=1.0, user-scalable=yes\"/>\n" +

          "    <style type=\"text/css\">\n" +
          "      p { word-break: break-all }\n" +
          "    </style>" +
          "  </head>";
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post);
    getSupportActionBar().hide();
    webView = (WebView) findViewById(R.id.webView);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);    //排版适应屏幕
    settings.setLoadWithOverviewMode(true);                             // setUseWideViewPort方法设置webview推荐使用的窗口。setL
    settings.setUseWideViewPort(true);
    settings.setPluginState(WebSettings.PluginState.ON);
    settings.setJavaScriptCanOpenWindowsAutomatically(true);
    settings.setAllowFileAccess(true);
    settings.setDefaultTextEncodingName("UTF-8");

    // 下面这三个使用的时候，需要HTML的内容的header   <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2, user-scalable=yes">
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);

    webView.setWebChromeClient(new WebChromeClient());

    Intent intent = getIntent();
    int id = intent.getIntExtra("id", 0);
    showContent(id + "");

    findViewById(R.id.submit).setOnClickListener(this);
    findViewById(R.id.bookmark).setOnClickListener(this);

  }

  public void showContent(String id){
    OkHttpClient client = new OkHttpClient();
    String url = "http://shentou.sweetysoft.com/api/bugs/" + id + "?user_id=" + getUserId();
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
                Log.i(TAG, "== response: " + result);
                Gson gson = new Gson();
                final PostDetail postDetail = gson.fromJson(result, PostDetail.class);
                Log.i(TAG, "== comment: " + postDetail.comment);

                runOnUiThread(new Runnable() {

                  @Override
                  public void run() {
                    webView.loadData(HTML_HEAD_CONTENT +
                            p(postDetail.wybug_title) +
                            p(postDetail.wybug_author) +
                            p(postDetail.wybug_rank_0)  +
                            p(postDetail.wybug_level) +
                            p(postDetail.wybug_type) +
                            postDetail.wybug_detail +
                            postDetail.wybug_reply +
                            postDetail.replys, "text/html; charset=utf-8", "UTF-8");
                    ((EditText)findViewById(R.id.comment)).setText(postDetail.comment);
                  }
                });
              }
            });
  }
  public String p(String content){
    return "<p>" + content + "</p>";
  }
  
  

  @Override
  public void onClick(View v) {

    Intent intent = getIntent();
    int id = intent.getIntExtra("id", 0);
    String user_id = getUserId();

    switch(v.getId()){
      case R.id.submit:
        String comment = ((EditText)findViewById(R.id.comment)).getText().toString();
        submitComment(id, user_id, comment);
        break;
      case R.id.bookmark:
        updateBookmark(id, user_id);
        break;
      default:
        break;
    }
  }

  private void updateBookmark(int id, String user_id) {
  }

  private void submitComment(int id, String user_id, String comment) {
    final PostDetailsActivity that = this;
    Log.i(TAG, "== in submitComment");
    OkHttpClient httpClient = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", String.valueOf(id))
            .addFormDataPart("user_id", user_id)
            .addFormDataPart("comment", comment)
            .build();
    String url = "http://shentou.sweetysoft.com/api/bugs/update_comment";

    Log.i(TAG, "== request body: " + requestBody.toString());
    Request request = new Request.Builder().
            url(url)
            .post(requestBody)
            .build();

    httpClient.newCall(request)
            .enqueue(new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                e.printStackTrace();
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                String response_result = response.body().string();
                Log.d(TAG, "== response: " + response_result);
                runOnUiThread(new Runnable() {

                  @Override
                  public void run() {
                    Toast.makeText(that, "学习完毕，下一条. ", Toast.LENGTH_LONG).show();

                  }
                });
              }
            });
  }
  
}
