package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.bluetoothchat.R;

public class Tom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom);
        Toast.makeText(getApplicationContext(),"oh my god",Toast.LENGTH_LONG).show();

    }
}
