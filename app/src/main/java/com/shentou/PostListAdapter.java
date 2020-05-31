package com.shentou;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shentou.beans.PostResult;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder>  {

  private List<PostResult.Post> posts ;


  public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView row;

    public MyViewHolder(TextView row){
      super(row);
      this.row = row;
    }
  }

  public PostListAdapter(List<PostResult.Post> posts) {
    this.posts = posts;
    Log.i("==", this.posts.size() + "");
  }

  /**
   * step1.负责创建list,这个应该是一个
   */
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TextView v = (TextView) (LayoutInflater.from(parent.getContext())
            .inflate(R.layout.one_row_post, parent, false));
    MyViewHolder holder = new MyViewHolder(v);
    return holder;
  }

  /**
   * step2.负责展示数据
   */
  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    String title = posts.get(position).title;
    holder.row.setText(title);
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

}