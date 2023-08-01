package com.aic.khidmanow;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.fr_voucherdetail;
import layout.utils.LocalManager;

public class postVoucherLookupAdapter extends RecyclerView.Adapter<postVoucherLookupAdapter.MyViewHolder> {
    Context ctx;
    ArrayList<String> productList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private Fragment ccy;
    private HMProductList hmProductList = new HMProductList();
    private IntroManager intromanager;
    private String type;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView itemcode, itemname, categoryname, expiry;
        private CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.listImage);
            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemcode = (TextView) itemView.findViewById(R.id.itemcode);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            expiry = (TextView) itemView.findViewById(R.id.expiry);
        }
    }

    public postVoucherLookupAdapter(Context context, ArrayList<String> productList, Fragment ccy, String type) {
        this.ctx = context;
        this.productList = productList;
        this.ccy = ccy;
        this.type = type;
        myDB = new HMCoreData(context);
        intromanager = new IntroManager(ctx);
    }


    @Override
    public postVoucherLookupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.voucher_cell, parent, false);
        Log.i("Debugging", "Item Created in View Holder");
        return new postVoucherLookupAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final postVoucherLookupAdapter.MyViewHolder holder, int position) {
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
            holder.expiry.setText("Expiry Date: " + myjson.getString("couponexpirydate"));
            holder.categoryname.setText(myjson.getString("category").toString());


            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // holder.card_view.setBackgroundColor(Color.GRAY);
                    // try {
                   /*     intromanager.setOfferCode(myjson.getString("itemcode"));

                        Log.i("Debugging","Came to Button Click ffffffff " + myjson.getString("itemcode"));
                        clickevent.clickEventItem(myjson.getString("itemcode"),myjson.getString("coupontype"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    FragmentActivity activity = (FragmentActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("couponcode", myjson.getString("itemcode"));
                        bundle.putString("itemname", myjson.getString("itemname"));
                        bundle.putString("itemdescription", myjson.getString("itemdescription"));
                        bundle.putString("itemtandc", myjson.getString("remark"));
                        bundle.putString("itemimage", myjson.getString("imageurls"));
                        bundle.putString("category", myjson.getString("category"));
                        if (LocalManager.getLanguagePref(ctx) == LocalManager.ENGLISH) {
                            bundle.putString("itemprice", activity.getString(R.string.Rs) + " " + myjson.getString("offervalue"));
                        } else {
                            bundle.putString("itemprice", myjson.getString("offervalue") + " " + activity.getString(R.string.Rs));
                        }
                        bundle.putString("isfav", type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(ctx, e.getMessage());
                        return;
                    }
                    Fragment myFragment = new fr_voucherdetail();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(ctx, e.getMessage());
            return;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}