package com.shentou;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    SharedPreferences settings = getSharedPreferences(Constants.KEY_USER_ID, 0);
    int userId = settings.getInt(Constants.KEY_USER_ID, 0);
    return userId + "";
  }

  public static final String TAG = "PostDetailsActivity";
  WebView webView;
  // somewhere on your code...
  WebViewClient myWebClient = new WebViewClient(){
    // you tell the webclient you want to catch when a url is about to load
    @Override
    public boolean shouldOverrideUrlLoading(WebView  view, String  url){
      return true;
    }
    // here you execute an action when the URL you want is about to load
    @Override
    public void onLoadResource(WebView  view, String  url){
      Log.i(TAG, "== in onLoadResource, url: " + url);
      if( url.matches(".*(jpg|png|jpeg|gif|bmp)$") ){
        // 在新的activity中打开
//        openImage(url);
      }
    }
  };

  public void openImage(String image_url){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.parse(image_url), "image/*"); // The Mime type can actually be determined from the file
    startActivity(intent);
  }

  String HTML_HEAD_CONTENT = "  <head>\n" +
          "    <meta charset=\"utf-8\" />\n" +
          "    <meta name=\"apple-mobile-web-app-title\" content=\"\">\n" +
          "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\n" +
          "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=3.0, minimum-scale=1.0, user-scalable=yes\"/>\n" +

          "    <style type=\"text/css\">\n" +
          "      p { word-break: break-all }\n" +
          "    </style>" +
          "    <script> " +
          "    </script>" +
          "  </head>";
  @SuppressLint("JavascriptInterface")
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

    JsInterface jsInterface = new JsInterface(this);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.addJavascriptInterface(jsInterface, "android");

    // 下面这三个使用的时候，需要HTML的内容的header   <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2, user-scalable=yes">
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);

//    webView.setWebChromeClient(myWebClient);
    webView.setWebViewClient(myWebClient);

    Intent intent = getIntent();
    int id = intent.getIntExtra("id", 0);
    showContent(id + "", getUserId());

    findViewById(R.id.submit).setOnClickListener(this);
    findViewById(R.id.bookmark).setOnClickListener(this);
    findViewById(R.id.next_post).setOnClickListener(this);
    findViewById(R.id.previous_post).setOnClickListener(this);
  }

  public void showContent(String id, String user_id){
    OkHttpClient client = new OkHttpClient();
    String url = Constants.URL_BUG_DETAILS + "/" + id +"?user_id=" + user_id;
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

    Log.i(TAG, "== current post id : " + id);
    switch(v.getId()){
      case R.id.submit:
        String comment = ((EditText)findViewById(R.id.comment)).getText().toString();
        submitComment(id, user_id, comment);
        break;
      case R.id.bookmark:
        updateBookmark(id, user_id);
        break;
      case R.id.next_post:
        showNextPost(id);
        break;
      case R.id.previous_post:
        showPreviousPost(id);
        break;
      default:
        break;
    }
  }

  private void updateBookmark(int id, String user_id) {
    final PostDetailsActivity that = this;
    Log.i(TAG, "== in updateBookmark");
    OkHttpClient httpClient = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", String.valueOf(id))
            .addFormDataPart("user_id", user_id)
            .build();

    Request request = new Request.Builder().
            url(Constants.URL_UPDATE_BOOKMARK)
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
                    Toast.makeText(that, "成功添加到收藏", Toast.LENGTH_LONG).show();
                  }
                });
              }
            });
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

    Request request = new Request.Builder().
            url(Constants.URL_UPDATE_COMMENT)
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
  private void showNextPost(int id){
    Log.d(TAG, "== in showNextPost");
    finish();
    Intent intent = new Intent(this, PostDetailsActivity.class);
    intent.putExtra("id", id - 1);
    startActivity(intent);
  }

  private void showPreviousPost(int id){
    Log.d(TAG, "== in showPreviousPost");
    finish();
    Intent intent = new Intent(this, PostDetailsActivity.class);
    intent.putExtra("id", id + 1);
    startActivity(intent);
  }
  
}
