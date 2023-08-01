package com.aic.khidmanow;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<detailitemVO> items;
    private int cuslayout;
    private int lastSelectedPosition = -1;
    private int formoption;
    private String qadata, stats = "";
    private HMCoreData hmcoredata;
    private IntroManager intromanager;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    ArrayList<Integer> intpos = new ArrayList<>();

    private String firsttime = "";

    public questionAdapter(FragmentActivity activity, ArrayList<detailitemVO> items, int cuslayout, int formoption, String qadata) {
        this.context = activity;
        this.items = items;
        this.cuslayout = cuslayout;
        this.formoption = formoption;
        this.qadata = qadata;
        hmcoredata = new HMCoreData(this.context);
        intromanager = new IntroManager(this.context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(questionAdapter.CustomViewHolder holder, int position) {
        final detailitemVO album = items.get(position);

        //    hmcoredata.showToast(context,Integer.toString(position));
        Log.i("debugging", "Location Type " + intromanager.getLocationType());
        if (intromanager.getLocationType().equals("2") && formoption == 10) {
            if (album.getOption().equals("0")) {
                stats = "(slots Full)";
            } else {
                stats = "(" + Integer.toString(intromanager.getPersonLimit() - album.getItemFree()) + " slots free)";
            }


            if (album.getOption().equals("0")) {
                //   holder.detailQ.setEnabled(false);
                //   holder.detailR.setEnabled(false);
                if (!intpos.contains(position)) {
                    itemStateArray.put(position, true);
                    intpos.add(position);
                }

            }
            if (itemStateArray.get(position, false)) {
                holder.detailQ.setEnabled(false);
                holder.detailR.setEnabled(false);
            } else {
                holder.detailQ.setEnabled(true);
                holder.detailR.setEnabled(true);
            }


        }
        Log.i("Debugging", "Booking Slots Status " + stats);
        holder.detailQ.setText(album.getDetailq() + " " + stats);
        holder.detailR.setText(album.getDetailr());
        if (album.getDetailr().equals(qadata) && firsttime.equals("")) {
            holder.detailQ.setChecked(true);
            firsttime = "1";
        } else {
            holder.detailQ.setChecked(lastSelectedPosition == position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private RadioButton detailQ;
        private TextView detailR;

        public CustomViewHolder(View view) {
            super(view);
            detailQ = view.findViewById(R.id.questcode);
            detailR = view.findViewById(R.id.optionvalue);
            Log.i("Debugging", "Question Adapter Initiated");
            detailQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRangeChanged(0, items.size());
                    lastSelectedPosition = getAdapterPosition();
                    //hmcoredata.alertBox(context,detailR.getText().toString());
                    if (formoption == 1) {
                        intromanager.setQAOption1(detailR.getText().toString());
                    } else if (formoption == 2) {
                        intromanager.setQAOption2(detailR.getText().toString());
                    } else if (formoption == 3) {
                        intromanager.setQAOption3(detailR.getText().toString());
                    } else if (formoption == 4) {
                        intromanager.setQAOption4(detailR.getText().toString());
                    } else if (formoption == 5) {
                        intromanager.setQAOption5(detailR.getText().toString());
                    } else if (formoption == 9) {
                        intromanager.setCarryLocation(detailR.getText().toString());
                    } else if (formoption == 10) {
                        String bookingdate = detailR.getText().toString().substring(0, 2) + ":" + detailR.getText().toString().substring(2, 4) + ":00";
                        Log.i("Debugging", "Set Formatted Date " + detailR.getText().toString());
                        Log.i("Debugging", "Set Formatted Date " + detailR.getText().toString().substring(0, 2));
                        Log.i("Debugging", "Set Formatted Date " + detailR.getText().toString().substring(2, 4));
                        Log.i("Debugging", "Set Formatted Date " + bookingdate);
                        intromanager.setBookTime(bookingdate);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

}
