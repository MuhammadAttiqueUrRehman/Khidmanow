package com.aic.khidmanow;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.fr_coupondetail;
import layout.utils.LocalManager;

/**
 * Created by Administrator on 19-06-2017.
 */

public class couponAdapter extends RecyclerView.Adapter<couponAdapter.MyViewHolder> {
    Context ctx;
    ArrayList<String> productList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private String ccy;
    private HMProductList hmProductList = new HMProductList();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        private TextView itemcode, itemname, categoryname, unitprice;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.listImage);
            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemcode = (TextView) itemView.findViewById(R.id.itemcode);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            unitprice = (TextView) itemView.findViewById(R.id.unitprice);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public couponAdapter(Context context, ArrayList<String> productList, String ccy) {
        this.ctx = context;
        this.productList = productList;
        this.ccy = ccy;
        myDB = new HMCoreData(context);
    }

    @Override
    public couponAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.coupon_cell, parent, false);
        Log.i("Debugging", "Item Created in View Holder");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            final JSONObject myjson = new JSONObject(productList.get(position));

            Picasso.get()
                    .load(myjson.getString("imageurls").toString())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .noFade()
                    .into(holder.imageView);

            holder.itemname.setText(myjson.getString("itemname"));
//            holder.itemcode.setText(myjson.getString("itemcode").toString());
            holder.categoryname.setText(myjson.getString("category").toString());
            if (LocalManager.getLanguagePref(ctx) == LocalManager.ENGLISH) {
                holder.unitprice.setText(ctx.getString(R.string.value) + ctx.getString(R.string.Rs) + " " + myjson.getString("offervalue"));
            } else {
                holder.unitprice.setText(ctx.getString(R.string.value) + myjson.getString("offervalue") + " " + ctx.getString(R.string.Rs));
            }

            //open info window
            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentActivity activity = (FragmentActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("itemcode", myjson.getString("itemcode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(ctx, e.getMessage());
                        return;
                    }
                    Fragment myFragment = new fr_coupondetail();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(ctx, e.getMessage());
            return;
        }
        Log.i("Debugging", "Binding in view holder " + position);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
