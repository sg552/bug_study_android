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

    RecyclerView.Adapter myAdapter = new PostListAdapter(posts);
    myRecyclerView.setAdapter(myAdapter);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    Bundle args = getArguments();
    myRecyclerView = view.findViewById(R.id.my_recycler_view);
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
            .url(POSTS_URL)
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

  }
}