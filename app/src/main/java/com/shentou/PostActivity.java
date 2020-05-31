package com.shentou;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.shentou.beans.PostDetail;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {

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

    showContent();
  }

  public void showContent(){
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
            .url("http://shentou.sweetysoft.com/api/bugs/88821")
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
                final PostDetail postDetail = gson.fromJson(result, PostDetail.class);

                runOnUiThread(new Runnable() {

                  @Override
                  public void run() {
                    webView.loadData(HTML_HEAD_CONTENT + postDetail.wybug_detail, "text/html; charset=utf-8", "UTF-8");
                  }
                });
              }
            });


  }
}
