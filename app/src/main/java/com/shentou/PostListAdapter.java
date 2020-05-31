package com.shentou;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shentou.beans.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder>  {

  private String[] myDataSet;
  private List<Post> posts ;


  public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView row;

    public MyViewHolder(TextView row){
      super(row);
      this.row = row;
    }
  }

  public PostListAdapter(String[] myDataSet) {
    this.myDataSet = myDataSet;
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
    holder.row.setText(this.myDataSet[position]);
  }

  @Override
  public int getItemCount() {
    return myDataSet.length;
  }

}