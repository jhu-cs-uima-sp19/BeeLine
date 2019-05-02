package com.wenwanggarzagao.beeline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.Time;
import com.wenwanggarzagao.beeline.data.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticipantsAdaptor extends RecyclerView.Adapter<ParticipantsAdaptor.MyViewHolder> {

    private final ClickListener listener;
    private final List<SavedUserData> usersList;
    private final Beeline beeline;


    public ParticipantsAdaptor(Beeline beeline, List<SavedUserData> usersList, ClickListener listener) {
        this.listener = listener;
        this.usersList = usersList;
        this.beeline = beeline;
        for (Iterator<SavedUserData> it = this.usersList.iterator(); it.hasNext();) {
            String id = it.next().userId;
            if (!this.beeline.participantIds.contains(id)) {
                it.remove();
                this.beeline.participantIds.remove(id);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_layout, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SavedUserData userInfo = usersList.get(position);
        holder.userName.setText(userInfo.username);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView userPropic;
        private TextView userName;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(final View itemView, ClickListener listener) {

            super(itemView);

            listenerRef = new WeakReference<>(listener);

            userPropic = (ImageView) itemView.findViewById(R.id.profile_icon);
            userName = (TextView) itemView.findViewById(R.id.participant_name);

            itemView.setOnClickListener(this);
            userName.setOnClickListener(this);
            userPropic.setOnClickListener(this);
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(getAdapterPosition());
            System.out.println("Clicked on user");
            SavedUserData savedUserData = usersList.get(getAdapterPosition());
            Context context = itemView.getContext();
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("userUID", savedUserData.userId);
            context.startActivity(intent);
        }


        //onLongClickListener for view
        @Override
        public boolean onLongClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Hello Dialog")
                    .setMessage("LONG CLICK DIALOG WINDOW FOR ICON " + String.valueOf(getAdapterPosition()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.create().show();
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;
        }
    }
}




