package com.mbh.hfadapter.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mbh.hfradapter.sample.R;


/**
 * Createdby MBH on 30/04/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;

    public MyViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv);
    }

    public void bind(String text) {
        textView.setText(text);
    }
}
