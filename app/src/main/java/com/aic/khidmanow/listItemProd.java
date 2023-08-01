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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.fr_product_detail;


public class listItemProd extends RecyclerView.Adapter<listItemProd.MyViewHolder> {
    Context ctx;
    ArrayList<String> productList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private String ccy;
    private HMProductList hmProductList = new HMProductList();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton plus;
        private ImageButton minus;
        private ImageButton addcart;
        private ImageButton recur;
        private ImageButton info;
        private RelativeLayout listll;
        private TextView itemcode, itemname, itemdescription, manufacturercode, manufacturername, categorycode, categoryname, subcategorycode, subcategoryname, sizedetail, colordetail, measurementtyp, outlet;
        private TextView measurementunit, unitprice, availableqty, rewardpercent, barcode, imagesmallurl, imagelargeurl, isfav, recurring, isoffer, offerprice, offerdescription, itemqty, numqty, totprice, ratingdetail;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            listll = (RelativeLayout) itemView.findViewById(R.id.listll);
            imageView = (ImageView) itemView.findViewById(R.id.listImage);
            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemqty = (TextView) itemView.findViewById(R.id.itemqty);
            totprice = (TextView) itemView.findViewById(R.id.totprice);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            addcart = (ImageButton) itemView.findViewById(R.id.itemcartbtn);
            recur = (ImageButton) itemView.findViewById(R.id.recurbtn);
            info = (ImageButton) itemView.findViewById(R.id.infobtn);
            numqty = (TextView) itemView.findViewById(R.id.numqty);
            itemcode = (TextView) itemView.findViewById(R.id.itemcode);
            itemdescription = (TextView) itemView.findViewById(R.id.itemdescription);
            manufacturercode = (TextView) itemView.findViewById(R.id.manufacturercode);
            manufacturername = (TextView) itemView.findViewById(R.id.manufacturername);
            categorycode = (TextView) itemView.findViewById(R.id.categorycode);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            subcategorycode = (TextView) itemView.findViewById(R.id.subcategorycode);
            subcategoryname = (TextView) itemView.findViewById(R.id.subcategoryname);
            sizedetail = (TextView) itemView.findViewById(R.id.sizedetail);
            colordetail = (TextView) itemView.findViewById(R.id.colordetail);
            measurementtyp = (TextView) itemView.findViewById(R.id.measurementtyp);
            measurementunit = (TextView) itemView.findViewById(R.id.measurementunit);
            unitprice = (TextView) itemView.findViewById(R.id.unitprice);
            availableqty = (TextView) itemView.findViewById(R.id.availableqty);
            rewardpercent = (TextView) itemView.findViewById(R.id.rewardpercent);
            barcode = (TextView) itemView.findViewById(R.id.barcode);
            imagelargeurl = (TextView) itemView.findViewById(R.id.imagelargeurl);
            isfav = (TextView) itemView.findViewById(R.id.isfav);
            recurring = (TextView) itemView.findViewById(R.id.recurring);
            isoffer = (TextView) itemView.findViewById(R.id.isoffer);
            offerprice = (TextView) itemView.findViewById(R.id.offerprice);
            offerdescription = (TextView) itemView.findViewById(R.id.offerdescription);
            outlet = (TextView) itemView.findViewById(R.id.outlet);
            ratingdetail = (TextView) itemView.findViewById(R.id.ratingdetail);
            imagesmallurl = (TextView) itemView.findViewById(R.id.imagesmallurl);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            Log.i("Debugging", "Item defined in Adapter for Favourites");
        }
    }

    public listItemProd(Context context, ArrayList<String> productList, String ccy) {
        this.ctx = context;
        this.productList = productList;
        this.ccy = ccy;
        myDB = new HMCoreData(context);
    }

    @Override
    public listItemProd.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.item_cell, parent, false);
        Log.i("Debugging", "Item Created in View Holder");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final JSONObject myjson;
        try {
            myjson = new JSONObject(productList.get(position));

            Log.i("Debugging", "Item Details " + myjson.getString("imagesmallurl").toString() + " " + myjson.getString("itemname").toString() + " " + myjson.getString("unitprice").toString());
            Picasso.get()
                    .load(myjson.getString("imagesmallurl").toString())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .noFade()
                    .into(holder.imageView);
            float myFloat = Float.parseFloat(myjson.getString("unitprice"));
            String formattedString = String.format("%.02f", myFloat);
            holder.itemname.setText(myjson.getString("itemname"));
            holder.itemqty.setText("Price :" + ccy + " " + formattedString);
            holder.itemcode.setText(myjson.getString("itemcode").toString());
            holder.itemdescription.setText(myjson.getString("itemdescription").toString());
            holder.manufacturercode.setText(myjson.getString("manufacturercode").toString());
            holder.manufacturername.setText(myjson.getString("manufacturername").toString());
            holder.categorycode.setText(myjson.getString("categorycode").toString());
            holder.categoryname.setText(myjson.getString("categoryname").toString());
            holder.subcategorycode.setText(myjson.getString("subcategorycode").toString());
            holder.subcategoryname.setText(myjson.getString("subcategoryname").toString());
            holder.sizedetail.setText(myjson.getString("sizedetail").toString());
            holder.colordetail.setText(myjson.getString("colordetail").toString());
            holder.measurementtyp.setText(myjson.getString("measurementtyp").toString());
            holder.measurementunit.setText(myjson.getString("measurementunit").toString());
            holder.unitprice.setText(myjson.getString("unitprice").toString());
            holder.availableqty.setText(myjson.getString("availableqty").toString());
            holder.rewardpercent.setText(myjson.getString("rewardpercent").toString());
            holder.barcode.setText(myjson.getString("barcode").toString());
            holder.imagelargeurl.setText(myjson.getString("imagelargeurl").toString());
            holder.isfav.setText(myjson.getString("isfav").toString());
            holder.recurring.setText(myjson.getString("recurring").toString());
            holder.isoffer.setText(myjson.getString("isoffer").toString());
            holder.offerprice.setText(myjson.getString("offerprice").toString());
            holder.offerdescription.setText(myjson.getString("offerdescription").toString());
            holder.outlet.setText(myjson.getString("outletcode").toString());
            //     holder.totprice.setText(myjson.getString("unitprice").toString());
//            holder.ratingdetail.setText(myjson.getString("availableprovider") + " providers    " + myjson.getString("servicerating") + "  Rating");

            holder.totprice.setText("Total :" + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
            // Change background color if in cart
            Integer i = myDB.getItemCountExists(holder.itemcode.getText().toString());
            if (i > 0) {
                holder.totprice.setVisibility(View.VISIBLE);
                holder.addcart.setVisibility(View.INVISIBLE);
                // holder.listll.setBackgroundColor(ctx.getResources().getColor(R.color.grayBG));
                //holder.addcart.setBackgroundColor(Color.parseColor("#491690"));
                //holder.addcart.setColorFilter(Color.parseColor("#ffffff"));
            } else {
                holder.addcart.bringToFront();
                holder.totprice.setVisibility(View.INVISIBLE);
                holder.addcart.setVisibility(View.VISIBLE);
                //     holder.listll.setBackgroundColor(ctx.getResources().getColor(R.color.white));
                //     holder.addcart.setBackgroundColor(Color.parseColor("#f5f5f5"));
                //     holder.addcart.setColorFilter(Color.parseColor("#491690"));
            }


            //Add item
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(holder.numqty.getText().toString());
                    holder.numqty.setText(String.valueOf((i > 100) ? 100 : i + 1));
                    myDB.updateCartItem(holder.numqty.getText().toString(), holder.itemcode.getText().toString());
                    Log.i("Debugging", "Item in Cart Plus :" + holder.numqty.getText().toString());
                    holder.totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
                }
            });

            //subtract item
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(holder.numqty.getText().toString());
                    holder.numqty.setText(String.valueOf((i < 2) ? 1 : i - 1));
                    myDB.updateCartItem(holder.numqty.getText().toString(), holder.itemcode.getText().toString());
                    Log.i("Debugging", "Item in Cart Minus :" + holder.numqty.getText().toString());
                    holder.totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
                }
            });

            //add to cart
            holder.addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.totprice.setVisibility(View.VISIBLE);
                    holder.addcart.setVisibility(View.INVISIBLE);
                    holder.minus.setVisibility(View.VISIBLE);
                    holder.numqty.setVisibility(View.VISIBLE);
                    holder.plus.setVisibility(View.VISIBLE);
                    //    holder.listll.setBackgroundColor(ctx.getResources().getColor(R.color.grayBG));
                    hmProductList.itemname = holder.itemname.getText().toString();
                    hmProductList.itemcode = holder.itemcode.getText().toString();
                    hmProductList.itemdescription = holder.itemdescription.getText().toString();
                    hmProductList.manufacturercode = holder.manufacturercode.getText().toString();
                    hmProductList.manufacturername = holder.manufacturername.getText().toString();
                    hmProductList.categorycode = holder.categorycode.getText().toString();
                    hmProductList.categoryname = holder.categoryname.getText().toString();
                    hmProductList.subcategorycode = holder.subcategorycode.getText().toString();
                    hmProductList.subcategoryname = holder.subcategoryname.getText().toString();
                    hmProductList.sizedetail = holder.sizedetail.getText().toString();
                    hmProductList.colordetail = holder.colordetail.getText().toString();
                    hmProductList.measurementtyp = holder.measurementtyp.getText().toString();
                    hmProductList.measurementunit = holder.measurementunit.getText().toString();
                    hmProductList.unitprice = holder.unitprice.getText().toString();
                    hmProductList.availableqty = holder.numqty.getText().toString();
                    hmProductList.rewardpercent = holder.rewardpercent.getText().toString();
                    hmProductList.barcode = holder.barcode.getText().toString();
                    hmProductList.imagesmallurl = holder.imagesmallurl.getText().toString();
                    hmProductList.imagelargeurl = holder.imagelargeurl.getText().toString();
                    hmProductList.isfav = holder.isfav.getText().toString();
                    hmProductList.recurring = holder.recurring.getText().toString();
                    hmProductList.isoffer = holder.isoffer.getText().toString();
                    hmProductList.offerprice = holder.offerprice.getText().toString();
                    hmProductList.offerdescription = holder.offerdescription.getText().toString();
                    hmProductList.itemtype = HMConstants.itemtypeproduct;
                    hmProductList.recurring = holder.recurring.getText().toString();
                    hmProductList.outletcode = holder.outlet.getText().toString();
                    Log.i("Debugging", "Recurring :" + hmProductList.recurring);
                    try {
                        myDB.addCart(hmProductList);
                        String count;
                        count = String.valueOf(myDB.getItemCount());
                        Log.i("Debugging", "Item in Cart Add :" + count);

                        /*if (fr_itemlist.textCartItemCount !=null) {
                            fr_itemlist.textCartItemCount.setText(count);
                        } else if(fr_homepage.textCartItemCount !=null) {
                            fr_homepage.textCartItemCount.setText(count);
                        }*/

                    } catch (HMOwnException e) {
                        e.printStackTrace();
                        myDB.alertBox(ctx, e.getMessage());
                        return;
                    }

                }
            });


            //open info window
            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentActivity activity = (FragmentActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putString("itemcode", holder.itemcode.getText().toString());
                    bundle.putString("itemname", holder.itemname.getText().toString());
                    bundle.putString("itemdescription", holder.itemdescription.getText().toString());
                    bundle.putString("unitprice", holder.unitprice.getText().toString());
                    bundle.putString("isfav", holder.isfav.getText().toString());
                    bundle.putString("recurring", holder.recurring.getText().toString());
                    bundle.putString("imagelargeurl", holder.imagelargeurl.getText().toString());
                    bundle.putString("selectedqty", holder.numqty.getText().toString());
                    bundle.putString("myjson", myjson.toString());

                    Fragment ff = new fr_product_detail();
                    ff.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, ff).addToBackStack(null).commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String computeTotal(String qty, String price) {
        Float f = Float.parseFloat(qty) * Float.parseFloat(price);
        String formattedString = String.format("%.02f", f);
        return formattedString;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

