package com.mbh.hfadapter.sample.adapters;

import android.view.View;
import android.widget.TextView;

import com.mbh.hfradapter.sample.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Createdby MBH on 30/04/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;

    public MyViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv);
    }

    public void bind(String text) {
        textView.setText(text);
    }
}
