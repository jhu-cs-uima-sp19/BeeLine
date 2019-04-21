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
import com.wenwanggarzagao.beeline.data.Notification;
import com.wenwanggarzagao.beeline.data.Time;

import java.util.ArrayList;

public class NotifAdapter extends ArrayAdapter<Notification> {
    int resource;

    public NotifAdapter(Context ctx, int res, ArrayList<Notification> bees) {
        super(ctx, res, bees);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout buzzView;
        Notification n = getItem(position);
        String text = n.text;
        long beelineId = n.assocBeeline;

        if (convertView == null) {
            buzzView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, buzzView, true);
        } else {
            buzzView = (LinearLayout) convertView;
        }

        TextView locView = (TextView) buzzView.findViewById(R.id.notif_text);
        locView.setText(text);

        TextView dateView = (TextView) buzzView.findViewById(R.id.date_time_info_2);
        dateView.setText(n.getTimeSince());

        return buzzView;
    }

}
