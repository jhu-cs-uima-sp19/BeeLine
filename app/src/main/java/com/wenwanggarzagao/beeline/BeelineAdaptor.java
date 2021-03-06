package com.wenwanggarzagao.beeline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.Time;
import com.wenwanggarzagao.beeline.data.Updatable;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class BeelineAdaptor extends RecyclerView.Adapter<com.wenwanggarzagao.beeline.BeelineAdaptor.beeViewHolder> implements Filterable {

    private final ClickListener listener;
    private final List<Beeline> beelineList;
    private Activity activity;

    private List<Beeline> beelineListFull;

    /*
    BeelineAdaptor(List<Beeline> beelineList) {
        this.beelineList = beelineList;

    }
    */




    public BeelineAdaptor(Activity activity, List<Beeline> beelineList, ClickListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.beelineList = beelineList;
        beelineListFull = new ArrayList<>(beelineList);
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

        if (b.participantIds.contains(DatabaseUtils.me.saveData.userId)) {
            holder.interestIcon.setImageResource(R.drawable.target_flowers);
            holder.interested = true;
        }


    }

    @Override public int getItemCount() {
        return beelineList.size();
    }

    @Override
    public Filter getFilter() {
        return beelineFilter;
    }

    private Filter beelineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Beeline> filteredList = new ArrayList<>();
            if (beelineListFull == null || beelineListFull.isEmpty()) {
                return null;
            }
            if (constraint == null || constraint.length() == 0) {
                //Show all results
                filteredList.addAll(beelineListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                //Iterate through items in full list through filter
                for (Beeline b : beelineListFull) {

                    String entry = b.from + " > " + b.to;
                    if (entry.toLowerCase().contains(filterPattern)) {
                        filteredList.add(b);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values  = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            beelineList.clear();
            beelineList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    public class beeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView interestIcon;
        private TextView locTxt;
        private TextView meetTxt;
        private WeakReference<ClickListener> listenerRef;

        //Interest shortcut button
        private boolean interested = false;

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
                if (!interested) {
                    DatabaseUtils.bl = (Beeline) beelineList.get(getAdapterPosition());

                    interestIcon.setImageResource(R.drawable.target_flowers);
                    DatabaseUtils.bl.join(DatabaseUtils.me);

                    //notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Joined Beeline", Toast.LENGTH_SHORT).show();
                    interested = true;

                } else if (interested) {
                    DatabaseUtils.bl = (Beeline) beelineList.get(getAdapterPosition());
                    if (!DatabaseUtils.bl.valid)
                        return;

                    interestIcon.setImageResource(R.drawable.gray_flowers);
                    if (DatabaseUtils.bl.participantIds.size() == 1)
                        DatabaseUtils.bl.valid = false;
                    DatabaseUtils.bl.leave(DatabaseUtils.me, new Runnable() {
                        @Override
                        public void run() {
                            if (activity instanceof MainActivity) {
                                System.out.println("is an updatable! updating...");
                                ((Updatable) activity).update();
                            }
                        }
                    });

                    Toast.makeText(v.getContext(), "Left Beeline", Toast.LENGTH_SHORT).show();
                    interested = false;
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
