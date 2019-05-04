package com.mbh.hfadapter.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mbh.hfadapter.sample.adapters.HFRAdapterTest;
import com.mbh.hfradapter.ALinearLayoutManager;
import com.mbh.hfradapter.sample.R;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HFRAdapterActivity extends AppCompatActivity {

    private HFRAdapterTest hfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfradapter);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // I used ALinearLayoutManager to avoid and ignore bugs in recycler view
        recyclerView.setLayoutManager(new ALinearLayoutManager(this));
        prepareAdapter();
        recyclerView.setAdapter(hfAdapter);

        hfAdapter.setLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hfAdapter.setLoading(false);
                hfAdapter.setAll(getItems());
            }
        }, 1000);
    }

    private void prepareAdapter() {
        hfAdapter = new HFRAdapterTest();
//        hfAdapter = new HFRAdapterTest(getItems());
        hfAdapter.addHeader(this, R.layout.v_header);
        hfAdapter.addFooter(this, R.layout.v_footer);
        hfAdapter.setLoadingView(this, R.layout.v_loading);
    }


    public List<String> getItems() {
        return Arrays.asList(
                "This", "is",
                "test", "for",
                "header", "footer",
                "only", "for", "testing", "1",
                "2", "3", "4", "5", "6", "7", "8", "9", "once",
                "more", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        );
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, HFRAdapterActivity.class);
        context.startActivity(starter);
    }
}
