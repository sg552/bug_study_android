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
import com.shentou.DemoCollectionPagerAdapter;
import com.shentou.R;

public class CollectionDemoFragment extends Fragment {

  // When requested, this adapter returns a DemoObjectFragment,
  // representing an object in the collection.
  DemoCollectionPagerAdapter demoCollectionPagerAdapter;
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
    demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getChildFragmentManager());
    viewPager = view.findViewById(R.id.pager);
    viewPager.setAdapter(demoCollectionPagerAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    tabLayout.setupWithViewPager(viewPager);

  }
}