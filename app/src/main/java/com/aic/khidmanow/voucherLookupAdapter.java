package com.aic.khidmanow;

import android.content.Context;

import androidx.fragment.app.Fragment;
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

public class voucherLookupAdapter extends RecyclerView.Adapter<voucherLookupAdapter.MyViewHolder> {
    Context ctx;
    ArrayList<String> productList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private Fragment ccy;
    private HMProductList hmProductList = new HMProductList();
    private IntroManager intromanager;

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

    public voucherLookupAdapter(Context context, ArrayList<String> productList, Fragment ccy) {
        this.ctx = context;
        this.productList = productList;
        this.ccy = ccy;
        myDB = new HMCoreData(context);
        intromanager = new IntroManager(ctx);
    }


    @Override
    public voucherLookupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.voucher_cell, parent, false);
        Log.i("debugging", "Item Created in View Holder");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final voucherLookupAdapter.MyViewHolder holder, int position) {
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
            holder.expiry.setText(ctx.getString(R.string.expiry_date) + myjson.getString("couponexpirydate"));
            holder.categoryname.setText(myjson.getString("category").toString());


            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // holder.card_view.setBackgroundColor(Color.GRAY);
                    try {
                        intromanager.setOfferCode(myjson.getString("itemcode"));

                        Log.i("debugging", "Came to Button Click ffffffff " + myjson.getString("itemcode"));
                        clickevent.clickEventItem(myjson.getString("itemcode"), myjson.getString("coupontype"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(ctx, e.getMessage());
                        return;
                    }

                 /*   FragmentActivity activity = (FragmentActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putString("itemcode", holder.itemcode.getText().toString());
                    Fragment myFragment = new fr_book_checkout();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss()();*/
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(ctx, e.getMessage());
            return;
        }
    }

    public interface ClickEvent {
        void clickEventItem(String value, String value1);
    }

    ClickEvent clickevent;

    public void setClickEvent(ClickEvent event) {
        this.clickevent = event;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}