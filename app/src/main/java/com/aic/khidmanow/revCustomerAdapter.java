package com.aic.khidmanow;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class revCustomerAdapter extends RecyclerView.Adapter<revCustomerAdapter.CustomViewHolder> {
    private int cuslayout;
    private Context context;
    private ArrayList<reviewVO> items;

    public revCustomerAdapter(FragmentActivity activity, ArrayList<reviewVO> items, int reslayout) {
        this.context = activity;
        this.items = items;
        this.cuslayout=reslayout;
    }


    @Override
    public revCustomerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new revCustomerAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(revCustomerAdapter.CustomViewHolder holder, int position) {
        final reviewVO reviewvo = items.get(position);
        holder.itemStar.setText(reviewvo.getStarrating() + " Stars");
        holder.itemText.setText(reviewvo.getReviewtext());
        holder.itemCustomer.setText(reviewvo.getCustomer());
        holder.itemCity.setText(reviewvo.getCity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView itemStar;
        private TextView itemText;
        private TextView itemCustomer;
        private TextView itemCity;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemStar = itemView.findViewById(R.id.reviewstar);
            itemText = itemView.findViewById(R.id.reviewtext);
            itemCustomer = itemView.findViewById(R.id.reviewcustomer);
            itemCity = itemView.findViewById(R.id.reviewcity);
        }
    }
}
