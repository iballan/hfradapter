package com.mbh.hfadapter.sample.adapters;

import android.view.View;

import com.mbh.hfradapter.HFRAdapter;
import com.mbh.hfradapter.sample.R;

import java.util.List;

/**
 * Createdby MBH on 30/04/2017.
 */

public class HFRAdapterTest extends HFRAdapter<String, MyViewHolder> {

    public HFRAdapterTest(List<String> items) {
        super(items);
    }

    public HFRAdapterTest() {}

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
