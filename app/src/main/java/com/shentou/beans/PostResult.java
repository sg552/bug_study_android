package com.shentou.beans;

import java.util.Date;
import java.util.List;

public class PostResult {

  public List<Post> result;

  public class Post {
    public int id;
    public String title;
    public String date;
    public String type;
    public String rank;
  }
}

