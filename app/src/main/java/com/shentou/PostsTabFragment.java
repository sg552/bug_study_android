package com.shentou;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsTabFragment extends Fragment {
  public static final String ARG_OBJECT = "object";
  public static final String SERVER = "http://localhost:6600";
  public static final String POSTS_URL =  SERVER + "/api/bugs";


  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_posts_tab, container, false);
  }

  private RecyclerView myRecyclerView ;

  private void initAskOrderBook(){

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    myRecyclerView.setLayoutManager(layoutManager);

    String[] data = { "aaa", "bbb", "ccc"};
    RecyclerView.Adapter myAdapter = new PostListAdapter(data);
    myRecyclerView.setAdapter(myAdapter);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    Bundle args = getArguments();
//    TextView text = (TextView) view.findViewById(R.id.text1);
//    Log.i("== text: ", text.toString());
//    text.setText(Integer.toString(args.getInt(ARG_OBJECT)));
    myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
    initAskOrderBook();
    /*
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

              }
            });

     */
  }
}