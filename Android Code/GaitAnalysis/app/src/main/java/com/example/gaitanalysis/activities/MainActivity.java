package com.example.gaitanalysis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gaitanalysis.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "onCreate: created");
    }

    public void leftCardClick(View view) {
        openActivity(BluetoothConnectActivity.class);
    }

    public void openActivity(Class<?> otherActivityClass) {
        Intent intent = new Intent(getApplicationContext(), otherActivityClass);
        startActivity(intent);
    }

}
