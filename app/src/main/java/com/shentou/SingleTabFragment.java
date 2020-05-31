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

public class SingleTabFragment extends Fragment {
  public static final String ARG_OBJECT = "object";

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_collection_object, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    Bundle args = getArguments();
    TextView text = (TextView) view.findViewById(R.id.text1);
    Log.i("== text: ", text.toString());
    text.setText(Integer.toString(args.getInt(ARG_OBJECT)));
  }
}