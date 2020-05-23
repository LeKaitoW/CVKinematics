package com.example.cvkinematics;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements CameraStart, FileStart {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            StartFragment fragment = new StartFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    public void cameraStart() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,
                new CameraFragment()).commit();
    }

    @Override
    public void fileStart() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,
                new FileFragment()).commit();
    }
}
