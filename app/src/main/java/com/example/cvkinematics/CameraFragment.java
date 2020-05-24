package com.example.cvkinematics;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

public class CameraFragment extends Fragment implements CvCameraViewListener2 {
    private static final String  TAG              = "CameraFragment";

    private JavaCameraView mOpenCvCameraView;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    public CameraFragment() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        FragmentActivity activity = getActivity();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mOpenCvCameraView = view.findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getActivity(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat input = inputFrame.gray();
        Mat mRGBa = inputFrame.rgba();
        Mat circles = new Mat();
        Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));
        Imgproc.HoughCircles(input, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100, 100, 90, 0, 1000);

        Log.i(TAG, String.valueOf("size: " + circles.cols()) + ", " + String.valueOf(circles.rows()));

        if (circles.cols() > 0) {
            for (int x=0; x < Math.min(circles.cols(), 5); x++ ) {
                double circleVec[] = circles.get(0, x);

                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];
                int cols = mRGBa.cols();
                int rows = mRGBa.rows();
                int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
                int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;
                Rect roi = new Rect();
                int x_rec = (int) (circleVec[0]-circleVec[2]/2);
                int y_rec = (int) (circleVec[1]-circleVec[2]/2);
                if ((x_rec < 0) || (y_rec < 0) || (x_rec > cols) || (y_rec > rows)) return inputFrame.rgba();
                roi.x = (x_rec>4) ? x_rec-4 : 0;
                roi.y = (y_rec>4) ? y_rec-4 : 0;

                roi.width = (x_rec+4 < cols) ?x_rec + 4 - roi.x : cols - roi.x;
                roi.height = (y_rec+4 < rows) ? y_rec + 4 - roi.y : rows - roi.y;

                Imgproc.rectangle(input, new Point(roi.x, roi.y), new Point(roi.x+roi.width, roi.y+roi.height),new Scalar(255, 255, 255));

                //roi.x = x_rec < 0 ? 0 : x_rec;
                //roi.y = y_rec < 0 ? 0 : y_rec;
                //roi.width = (int)circleVec[2]*2;
               // roi.height = (int)circleVec[2]*2;
                Mat roiRGBa = mRGBa.submat(roi);
                Mat roiHsv = new Mat();
                Imgproc.cvtColor(roiRGBa, roiHsv, Imgproc.COLOR_RGB2HSV_FULL);
                Scalar colorHsv = Core.sumElems(roiHsv);
                int pointCount = roi.width*roi.height;
                for (int i = 0; i < colorHsv.val.length; i++)
                    colorHsv.val[i] /= pointCount;
                Log.i("color", Double.toString(colorHsv.val[0]));
                if (0 < colorHsv.val[0] && colorHsv.val[0] < 20) {
                    Imgproc.circle(input, center, 3, new Scalar(255, 255, 255), 5);
                    Imgproc.circle(input, center, radius, new Scalar(255, 255, 255), 2);
                }


                roiRGBa.release();
                roiHsv.release();
            }
        }

        circles.release();
        input.release();
        return inputFrame.rgba();
    }
}
