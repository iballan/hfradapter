package com.mbh.hfadapter.sample.adapters;

import android.view.View;

import com.mbh.hfradapter.ParallaxHFRAdapter;
import com.mbh.hfradapter.sample.R;

import java.util.List;

/**
 * Createdby MBH on 30/04/2017.
 */

public class ParallaxHFRAdapterTest extends ParallaxHFRAdapter<String, MyViewHolder> {

    public ParallaxHFRAdapterTest(List<String> items) {
        super(items);
    }

    public ParallaxHFRAdapterTest() {}

    @Override
    protected void onBindItemViewHolder(MyViewHolder viewHolder, int position, int type) {
        viewHolder.bind(getItem(position));
    }

    @Override
    protected MyViewHolder viewHolder(View view, int type) {
        return new MyViewHolder(view);
    }

    @Override
    protected int layoutId(int type) {
        return R.layout.item_text;
    }
}
