package com.example.cvkinematics;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.opencv.core.Point;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartFragment extends Fragment {
    private ArrayList<Point> points = null;
    LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (points == null) {
            Log.d("Chart","Error");
        }
        mChart = view.findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        Log.i("Points", Arrays.toString(points.toArray()));
        renderData();
    }

    public static ChartFragment newInstance(final ArrayList<Point> points) {
        ChartFragment chartFragment = new ChartFragment();
        chartFragment.points = points;
        return chartFragment;
    }

    public void renderData() {
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);

        setData();
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();
        int xMax = 0, xMin = 700, yMax = 0, yMin = 700;
        for (Point point : points) {
            values.add(new Entry((int)point.y, (int)point.x));
            if (point.x > xMax) xMax = (int)point.x;
            if (point.x < xMin) xMin = (int)point.x;
            if (point.y > yMax) yMax = (int)point.y;
            if (point.y < yMin) yMin = (int)point.y;
        }

        /*mChart.getXAxis().setAxisMaximum(yMax);
        mChart.getXAxis().setAxisMinimum(yMin);
        mChart.getAxisLeft().setAxisMaximum(xMax);
        mChart.getAxisLeft().setAxisMinimum(xMin);
        mChart.getAxisRight().setAxisMaximum(xMax);
        mChart.getAxisRight().setAxisMinimum(xMin);*/
        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Data");
            set1.setDrawIcons(false);
            set1.setDrawCircles(false);
            set1.setColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.color.colorPrimary);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

}
