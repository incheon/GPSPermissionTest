package com.permission.gps.test.incheon.gpspermissiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mBtnGrantGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnGrantGPS = (Button) findViewById(R.id.btnGrantGPS);
        mBtnGrantGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Toast.makeText(MainActivity.this, "Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
