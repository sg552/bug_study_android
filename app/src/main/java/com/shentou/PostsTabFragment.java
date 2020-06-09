package com.shentou;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shentou.beans.PostResult;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostsTabFragment extends Fragment {
  public static final String ARG_OBJECT = "object";
  public static final String SERVER = "http://shentou.sweetysoft.com";
  public static final String POSTS_URL =  SERVER + "/api/bugs";
  public static final String BOOKMARKS_URL = SERVER + "/api/bugs/bookmarks";
  public static final String TAG = "PostsTabFragment";

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_posts_tab, container, false);
  }

  private RecyclerView myRecyclerView ;

  private void initPosts(List<PostResult.Post> posts){

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    myRecyclerView.setLayoutManager(layoutManager);

    RecyclerView.Adapter myAdapter = new PostListAdapter(posts, myRecyclerView);
    myRecyclerView.setAdapter(myAdapter);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    Bundle args = getArguments();
    int myIndex = args.getInt(PostsTabFragment.ARG_OBJECT);
    myRecyclerView = view.findViewById(R.id.my_recycler_view);

    Log.i(TAG, "== in onViewCreated, myIndex: " + myIndex);
    // 分别创建第一个和第二个tab
    if(myIndex <= 2 ){

      String is_studied = myIndex == 1 ? "0" : "1";

      OkHttpClient client = new OkHttpClient();
      String url = POSTS_URL + "?user_id=1&is_studied="+is_studied;
      Log.i(TAG, "== index = 0,1 , urL " + url);

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
                  final PostResult thePostResult = gson.fromJson(result, PostResult.class);

                  getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                      initPosts(thePostResult.result);
                    }
                  });
                }
              });
    // 加载收藏
    }else if(myIndex == 3){

      OkHttpClient client = new OkHttpClient();
      String url = BOOKMARKS_URL + "?user_id=1";
      Log.i(TAG, "== index = 2, urL " + url);
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
                  final PostResult thePostResult = gson.fromJson(result, PostResult.class);

                  getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                      initPosts(thePostResult.result);
                    }
                  });
                }
              });
    }else {
      // 啥也不做
    }

  }
}