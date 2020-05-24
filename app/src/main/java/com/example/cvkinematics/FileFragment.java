package com.example.cvkinematics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

public class FileFragment extends Fragment {

    private Uri video = null;
    public FileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            video = Uri.parse(arguments.getParcelable("uri").toString());
            VideoView videoField = getActivity().findViewById(R.id.videoView);
            videoField.setVideoURI(video);
            videoField.start();
        }
        View videoButton = view.findViewById(R.id.video_button);
        videoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, StartFragment.REQUEST_TAKE_GALLERY_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            video = data.getData();
            VideoView videoField = getActivity().findViewById(R.id.videoView);
            videoField.setVideoURI(video);
            videoField.start();
        }
    }

    public static FileFragment newInstance(Uri video) {
        FileFragment fileFragment = new FileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", video);
        fileFragment.setArguments(bundle);
        return fileFragment;
    }

}
