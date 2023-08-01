package com.aic.khidmanow;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;



public class detailServiceAdapteri extends RecyclerView.Adapter<detailServiceAdapteri.CustomViewHolder> {
    private Context context;
    private ArrayList<detailitemVO> items;
    private int cuslayout;

    public detailServiceAdapteri(FragmentActivity activity, ArrayList<detailitemVO> items,int cuslayout) {
        this.context = activity;
        this.items = items;
        this.cuslayout=cuslayout;
    }
    @Override
    public detailServiceAdapteri.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final detailitemVO album = items.get(position);
        holder.detailQ.setText(album.getDetailq());
        if(album.getDetailr().equals("I")){
        holder.detailR.setImageResource(R.drawable.ico_done);
        }else{
            holder.detailR.setImageResource(R.drawable.ic_clear);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {


        private TextView detailQ;
        private ImageButton detailR;

        public CustomViewHolder(View view) {
            super(view);
            detailQ = view.findViewById(R.id.detailQ);
            detailR = view.findViewById(R.id.detailR);
        }
    }
}
