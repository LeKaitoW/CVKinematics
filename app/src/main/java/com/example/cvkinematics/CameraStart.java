package com.example.cvkinematics;

import android.util.Pair;

import org.opencv.core.Point;

import java.util.ArrayList;

public interface CameraStart {
    void cameraStart();
    void getChart(final ArrayList<Pair<Integer, Integer>> points);
}
