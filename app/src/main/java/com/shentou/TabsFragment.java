package com.shentou;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class TabsFragment extends Fragment {

  TabPagerAdapter tabPagerAdapter;
  ViewPager viewPager;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.my_view_pager, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
    viewPager = view.findViewById(R.id.pager);
    viewPager.setAdapter(tabPagerAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    tabLayout.setupWithViewPager(viewPager);

  }
}