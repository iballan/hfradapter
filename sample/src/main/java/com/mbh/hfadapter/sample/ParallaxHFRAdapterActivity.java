package com.mbh.hfadapter.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.mbh.hfadapter.sample.adapters.ParallaxHFRAdapterTest;
import com.mbh.hfradapter.ALinearLayoutManager;
import com.mbh.hfradapter.sample.R;

import java.util.Arrays;
import java.util.List;

public class ParallaxHFRAdapterActivity extends AppCompatActivity {

    private ParallaxHFRAdapterTest parallaxHfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfradapter);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // I used ALinearLayoutManager to avoid and ignore bugs in recycler view
        recyclerView.setLayoutManager(new ALinearLayoutManager(this));
        prepareAdapter();
        recyclerView.setAdapter(parallaxHfAdapter);
    }

    private void prepareAdapter() {
        parallaxHfAdapter = new ParallaxHFRAdapterTest(getItems());
        parallaxHfAdapter.addHeader(this, R.layout.v_header);
        parallaxHfAdapter.addFooter(this, R.layout.v_footer);
        parallaxHfAdapter.setLoadingView(this, R.layout.v_loading);
        parallaxHfAdapter.setParallaxHeader(true);
        parallaxHfAdapter.setParallaxFooter(true);
    }


    public List<String> getItems() {
        return Arrays.asList(
                "This", "is",
                "test", "for",
                "header", "footer",
                "only", "for", "testing", "1",
                "1", "2", "3", "4", "5", "6", "7", "8", "9"
        );
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, ParallaxHFRAdapterActivity.class);
        context.startActivity(starter);
    }
}
