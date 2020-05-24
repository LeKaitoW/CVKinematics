package com.example.cvkinematics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class StartFragment extends Fragment {
    final static int REQUEST_TAKE_GALLERY_VIDEO = 0;
    final static int REQUEST_TAKE_CAMERA_VIDEO = 1;

    public StartFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        View opencv = view.findViewById(R.id.file_button);
        opencv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraStart)getActivity()).cameraStart();
            }
        });
        View camera = view.findViewById(R.id.camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, REQUEST_TAKE_CAMERA_VIDEO);
            }
        });
        View video = view.findViewById(R.id.first_video_button);
        video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == Activity.RESULT_OK) {
            ((FileStart)getActivity()).fileStart(data.getData());
        }
        if (requestCode == REQUEST_TAKE_CAMERA_VIDEO && resultCode == Activity.RESULT_OK) {
            ((CameraStart)getActivity()).cameraStart();
        }
    }
}
