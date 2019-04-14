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

public class BeelineAdaptor extends ArrayAdapter<Beeline.Builder> {

    int resource;
    String from;
    String to;

    public BeelineAdaptor(Context ctx, int res, ArrayList<Beeline.Builder> bees) {
        super(ctx, res, bees);
        resource = res;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout beeView;
        Beeline.Builder b = getItem(position);


        if (convertView == null) {
            beeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, beeView, true);
        } else {
            beeView = (LinearLayout) convertView;
        }


        TextView fromView = (TextView) beeView.findViewById(R.id.origin);
        TextView toView = (TextView) beeView.findViewById(R.id.destination);

        //TODO Get Origin and Destination from Beeline
        fromView.setText("Wolman");
        toView.setText("Druid Hills");



        return beeView;
    }




}
