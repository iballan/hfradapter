package com.mbh.hfadapter.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.mbh.hfradapter.sample.R;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void btn_hfrAdapterClicked(View view) {
        HFRAdapterActivity.start(MainActivity.this);
    }

    public void btn_phfrAdapterClicked(View view) {
        ParallaxHFRAdapterActivity.start(MainActivity.this);
    }
}
