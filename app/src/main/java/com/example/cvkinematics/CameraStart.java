package com.example.cvkinematics;

import org.opencv.core.Point;

import java.util.ArrayList;

public interface CameraStart {
    void cameraStart();
    void getChart(final ArrayList<Point> points);
}
