package com.mist.it.pod_nk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tunyaporn on 7/20/2017.
 */

public class ManageJobAdaptor extends BaseAdapter {
    Context context;
    String dateString,tripNoString, subJobNoString;
    String[] storeStrings, timeStrings, loginStrings;
    String[][] jobNoStrings;
    String[][][] invoiceStrings, amountStrings;
    ManageJobViewHolder manageJobViewHolder;

    public ManageJobAdaptor(Context context, String dateString, String tripNoString, String subJobNoString, String[] storeStrings, String[] timeStrings, String[] loginStrings, String[][] jobNoStrings, String[][][] invoiceStrings, String[][][] amountStrings) {
        this.context = context;
        this.dateString = dateString;
        this.tripNoString = tripNoString;
        this.subJobNoString = subJobNoString;
        this.storeStrings = storeStrings;
        this.timeStrings = timeStrings;
        this.loginStrings = loginStrings;
        this.jobNoStrings = jobNoStrings;
        this.invoiceStrings = invoiceStrings;
        this.amountStrings = amountStrings;
    }

    @Override
    public int getCount() {
        return storeStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.manage_job_listview, null);
            manageJobViewHolder = new ManageJobViewHolder(convertView);
            convertView.setTag(manageJobViewHolder);
        } else {
            manageJobViewHolder = (ManageJobViewHolder) convertView.getTag();
        }

        manageJobViewHolder.storeTextView.setText(storeStrings[position]);
        manageJobViewHolder.timeTextView.setText(timeStrings[position]);

        JobNoAdaptor jobNoAdaptor = new JobNoAdaptor(context, jobNoStrings[position], invoiceStrings[position], amountStrings[position]);
        manageJobViewHolder.jobNoListView.setAdapter(jobNoAdaptor);

        manageJobViewHolder.storeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Position", tripNoString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("SubJobNo", subJobNoString);
                intent.putExtra("Place", storeStrings[position]);
                context.startActivity(intent);
            }
        });

        manageJobViewHolder.timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Position", tripNoString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("SubJobNo", subJobNoString);
                intent.putExtra("Place", storeStrings[position]);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ManageJobViewHolder {
        @BindView(R.id.txtMJLStore)
        TextView storeTextView;
        @BindView(R.id.txtMJLTime)
        TextView timeTextView;
        @BindView(R.id.lisMJLJobNo)
        ListView jobNoListView;

        ManageJobViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

