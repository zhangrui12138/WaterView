package com.office.pc25245.waterview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
 private WaterView waterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waterView = new WaterView(MainActivity.this);
        setContentView(waterView);
    }
}
