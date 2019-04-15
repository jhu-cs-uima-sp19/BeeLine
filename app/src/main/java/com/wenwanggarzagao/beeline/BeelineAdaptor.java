package com.wenwanggarzagao.beeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.Time;

import java.util.ArrayList;

public class BeelineAdaptor extends ArrayAdapter<Beeline> {

    int resource;

    public BeelineAdaptor(Context ctx, int res, ArrayList<Beeline> bees) {
        super(ctx, res, bees);
        resource = res;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout beeView;
        Location from;
        Location to;
        Date meet_date;
        Time meet_time;
        Beeline b = getItem(position);

        from = b.from;
        to = b.to;
        meet_date = b.meet_date;
        meet_time = b.meet_time;

        if (convertView == null) {
            beeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, beeView, true);
        } else {
            beeView = (LinearLayout) convertView;
        }


        TextView locView = (TextView) beeView.findViewById(R.id.origin);

        String locationsTxt = from + " > " + to;
        locView.setText(locationsTxt);

        String meetTxt = meet_date.toString() + " " + meet_time.toString();
        TextView dateView = (TextView) beeView.findViewById(R.id.date_time_info);
        dateView.setText(meetTxt);


        return beeView;
    }




}
