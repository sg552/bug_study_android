package com.shentou;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

class TabPagerAdapter extends FragmentStatePagerAdapter {
  public TabPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    Fragment fragment = new PostsTabFragment();
    Bundle args = new Bundle();
    // Our object is just an integer :-P
    args.putInt(PostsTabFragment.ARG_OBJECT, i + 1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    CharSequence title = "";
    if(position == 0){
      title = "未学习";
    }else if (position == 1){
      title = "已学习";
    }else {
      title = "";
    }
    return title;
  }
}
