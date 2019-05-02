package com.wenwanggarzagao.beeline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.Time;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class BeelineAdaptor extends RecyclerView.Adapter<com.wenwanggarzagao.beeline.BeelineAdaptor.beeViewHolder> {

    private final ClickListener listener;
    private final List<Beeline> beelineList;

    //private RecyclerView participantListView;




    public BeelineAdaptor(List<Beeline> beelineList, ClickListener listener) {
        this.listener = listener;
        this.beelineList = beelineList;
    }

    @Override
    public beeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new beeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.beeline_layout, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(beeViewHolder holder, int position) {
        // bind layout and data etc..
        Beeline b = beelineList.get(position);
        String locationsTxt = b.from + " > " + b.to;
        holder.locTxt.setText(locationsTxt);
        String meetTxt = b.meet_date.toString() + " | " + b.meet_time.toString();
        holder.meetTxt.setText(meetTxt);


    }

    @Override public int getItemCount() {
        return beelineList.size();
    }

    public class beeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView interestIcon;
        private TextView locTxt;
        private TextView meetTxt;
        private WeakReference<ClickListener> listenerRef;

        //Interest shortcut button
        private boolean interested = true;

        //participantListView = findViewById(R.id.participant_list);




        public beeViewHolder(final View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            interestIcon = (ImageView) itemView.findViewById(R.id.interest_icon);
            locTxt = (TextView) itemView.findViewById(R.id.origin);
            meetTxt = (TextView) itemView.findViewById(R.id.date_time_info);
            itemView.setOnClickListener(this);
            interestIcon.setOnClickListener(this);


        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {


            //participantListView.setLayoutManager(new LinearLayoutManager(v.getContext()));
            //List<SavedUserData> participantList= new ArrayList<SavedUserData>();
            if (v.getId() == interestIcon.getId()) {
                if (interested) {
                    interestIcon.setImageResource(R.drawable.target_flowers);

                    DatabaseUtils.bl = (Beeline) beelineList.get(getAdapterPosition());
                    DatabaseUtils.bl.join(DatabaseUtils.me);

                    // Add to participants list
                    //SavedUserData u = DatabaseUtils.bl.participants.get()
                    //DatabaseUtils.bl.participants.add(u);

                    //notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Joined Beeline", Toast.LENGTH_SHORT).show();

                    interested = false;

                } else if (!interested) {
                    interestIcon.setImageResource(R.drawable.gray_flowers);

                    DatabaseUtils.bl = (Beeline) beelineList.get(getAdapterPosition());
                    DatabaseUtils.bl.leave(DatabaseUtils.me);

                    //notifyDataSetChanged();
                    // Remove from participants list
                    //SavedUserData u = DatabaseUtils.bl.participants.get(getAdapterPosition());
                    //DatabaseUtils.bl.participants.remove(u);



                    Toast.makeText(v.getContext(), "Left Beeline", Toast.LENGTH_SHORT).show();

                    interested = true;
                }
            } else {
                Intent intent = new Intent(v.getContext(), BeelineDetails.class);
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }

            listenerRef.get().onPositionClicked(getAdapterPosition());
        }


        //onLongClickListener for view
        @Override
        public boolean onLongClick(View v) {

            /*final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Hello Dialog")
                    .setMessage("LONG CLICK DIALOG WINDOW FOR ICON " + String.valueOf(getAdapterPosition()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.create().show();*/
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;
        }
    }
}
