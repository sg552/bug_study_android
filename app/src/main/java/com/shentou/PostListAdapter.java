package com.shentou;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shentou.beans.PostResult;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> implements View.OnClickListener {

  private List<PostResult.Post> posts ;
  private RecyclerView recyclerView;

  public static final String TAG  = "PostListAdapter";

  @Override
  public void onClick(View v) {
    int itemPosition = recyclerView.getChildLayoutPosition(v);
    Log.i(TAG, "=== itemPosition: " + itemPosition);
    Log.i(TAG, "=== id: " + posts.get(itemPosition).id);

    Intent intent = new Intent(v.getContext(), PostActivity.class);
    v.getContext().startActivity(intent);
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView row;

    public MyViewHolder(TextView row){
      super(row);
      this.row = row;
    }
  }

  public PostListAdapter(List<PostResult.Post> posts, RecyclerView recyclerView) {
    this.posts = posts;
    this.recyclerView = recyclerView;
  }

  /**
   * step1.负责创建list,这个应该是一个
   */
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TextView v = (TextView) (LayoutInflater.from(parent.getContext()).inflate(R.layout.one_row_post, parent, false));
    v.setOnClickListener(this);
    MyViewHolder holder = new MyViewHolder(v);
    return holder;
  }

  /**
   * step2.负责展示数据
   */
  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    PostResult.Post post = posts.get(position);
    holder.row.setText(post.id + " " + post.title);
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

}