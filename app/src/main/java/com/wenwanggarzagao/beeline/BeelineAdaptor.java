package com.wenwanggarzagao.beeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.Beeline;

import java.util.ArrayList;

public class BeelineAdaptor extends ArrayAdapter<Beeline> {

    int resource;
    String from;
    String to;

    public BeelineAdaptor(Context ctx, int res, ArrayList<Beeline> bees) {
        super(ctx, res, bees);
        resource = res;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        LinearLayout jobView;
        JobItem jb = getItem(position);

        if (convertView == null) {
            jobView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, jobView, true);
        } else {
            jobView = (LinearLayout) convertView;
        }

        TextView addrView = (TextView) jobView.findViewById(R.id.address_text);
        TextView dateView = (TextView) jobView.findViewById(R.id.date_text);
        TextView paidView = (TextView) jobView.findViewById(R.id.paid_view);

        addrView.setText(jb.getWhere());
        dateView.setText(jb.getWhen());
        paidView.setText(jb.getPaid()==1 ? "PAID" : "unpaid");
        */

        return convertView;
    }




}
