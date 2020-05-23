package com.example.cvkinematics;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class StartFragment extends Fragment {
    public StartFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        View camera = view.findViewById(R.id.camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((CameraStart)getActivity()).cameraStart();
            }
        });
        View file = view.findViewById(R.id.file_button);
        file.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((FileStart)getActivity()).fileStart();
            }
        });
    }
}
