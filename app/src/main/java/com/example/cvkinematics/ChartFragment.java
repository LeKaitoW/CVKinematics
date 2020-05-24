package com.example.cvkinematics;

import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.Fragment;

import org.opencv.core.Point;

import java.util.ArrayList;

public class ChartFragment extends Fragment {
    private final ArrayList<Point> points = null;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            //hello points
        }
    }

    public static ChartFragment newInstance(final ArrayList<Pair<Integer, Integer>> points) {
        ChartFragment chartFragment = new ChartFragment();
        Bundle bundle = new Bundle();
        //bundle.putParcelableArrayList("points", points);
        chartFragment.setArguments(bundle);
        return chartFragment;
    }
}
