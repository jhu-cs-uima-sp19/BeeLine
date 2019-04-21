package com.wenwanggarzagao.beeline;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.Time;
import com.wenwanggarzagao.beeline.data.User;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsAdaptor extends ArrayAdapter<User> {
    int resource;


    public ParticipantsAdaptor(Context ctx, int res, List<User> users) {
        super(ctx, res, users);
        resource = res;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LinearLayout participantView;

        Beeline b = DatabaseUtils.bl;


        List<User> participants = new ArrayList<User>();

        if (b.participants != null) {
            participants = b.participants;
        }
        if (convertView == null) {
            participantView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            holder = new ViewHolder();
            // (holder) = (TextView) convertView.findViewById();

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, participantView, true);
        } else {
            participantView = (LinearLayout) convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        User people = (User) participants.get(position);
        holder.participaters.setText(people.getUsername());


        return participantView;
    }

    static class ViewHolder {
        TextView participaters;
    }



}
