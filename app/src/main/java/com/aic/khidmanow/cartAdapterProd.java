package com.aic.khidmanow;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.fr_book_checkout;

public class cartAdapterProd extends RecyclerView.Adapter<cartAdapterProd.MyViewHolder> {

    Context ctx;
    ArrayList<String> productList = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private String ccy;
    private HMProductList hmProductList = new HMProductList();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton plus, minus, recur, info, deletebtn;
        private TextView itemcode, itemname, itemdescription, manufacturercode, manufacturername, categorycode, categoryname, subcategorycode, subcategoryname, sizedetail, colordetail, measurementtyp;
        private TextView measurementunit, unitprice, availableqty, rewardpercent, barcode, imagesmallurl, imagelargeurl, isfav, recurring, isoffer, offerprice, offerdescription, itemqty, numqty, totprice;
        private LinearLayout changeinfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.listImage);
            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemqty = (TextView) itemView.findViewById(R.id.itemqty);
            totprice = (TextView) itemView.findViewById(R.id.totprice);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            // recur = (ImageButton) itemView.findViewById(R.id.recurbtn);
            //  info = (ImageButton) itemView.findViewById(R.id.infobtn);
            changeinfo = (LinearLayout) itemView.findViewById(R.id.changeinfo);
            deletebtn = (ImageButton) itemView.findViewById(R.id.deletebtn);
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
            imagesmallurl = (TextView) itemView.findViewById(R.id.imagesmallurl);
            recurring = (TextView) itemView.findViewById(R.id.recurring);
            isoffer = (TextView) itemView.findViewById(R.id.isoffer);
            offerprice = (TextView) itemView.findViewById(R.id.offerprice);
            offerdescription = (TextView) itemView.findViewById(R.id.offerdescription);
        }
    }

    public cartAdapterProd(Context context, ArrayList<String> productList, String ccy) {
        this.ctx = context;
        this.productList = productList;
        this.ccy = ccy;
        myDB = new HMCoreData(context);
    }


    @Override
    public cartAdapterProd.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflator.inflate(R.layout.cart_cell, parent, false);
        return new cartAdapterProd.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cartAdapterProd.MyViewHolder holder, final int position) {
        try {
            final JSONObject myjson = new JSONObject(productList.get(position));
    /*        Picasso.with(ctx)
                    .load(myjson.getString("itemimge").toString())
                    .placeholder(R.drawable.haal_meer_large)
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .noFade()
                    .into(holder.imageView);*/
            float myFloat = Float.parseFloat(myjson.getString("price"));
            String formattedString = String.format("%.02f", myFloat);
            holder.itemname.setText(myjson.getString("itemname"));
            holder.itemqty.setText(ctx.getString(R.string.price) + " : " + ccy + " " + formattedString);
            holder.itemcode.setText(myjson.getString("itemcode").toString());
            holder.itemdescription.setText(myjson.getString("itemdescription").toString());
            holder.categorycode.setText(myjson.getString("category").toString());
            holder.sizedetail.setText(myjson.getString("outletcode").toString());
            holder.colordetail.setText(myjson.getString("deviceid").toString());
            holder.measurementtyp.setText(myjson.getString("expiry").toString());
            holder.unitprice.setText(myjson.getString("price").toString());
            holder.numqty.setText(myjson.getString("itemqty").toString());
            holder.imagesmallurl.setText(myjson.getString("imageurll").toString());
            holder.imagelargeurl.setText(myjson.getString("imageurls").toString());
            holder.isfav.setText(myjson.getString("isfav").toString());
            holder.recurring.setText(myjson.getString("recur").toString());
            holder.offerprice.setText(myjson.getString("smessage").toString());
            holder.offerdescription.setText(myjson.getString("remark").toString());
            holder.totprice.setText(ctx.getString(R.string.total) + " : " + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
            Log.i("Debugging", "Is this a Service Item :" + myjson.getString("recur"));
            //Change Background color if is recurring
            if (myjson.getString("recur").equals("M")) {
                holder.changeinfo.setVisibility(View.GONE);
            }


            //Add item
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(holder.numqty.getText().toString());
                    holder.numqty.setText(String.valueOf((i > 100) ? 100 : i + 1));
                    myDB.updateCartItem(holder.numqty.getText().toString(), holder.itemcode.getText().toString());
                    Log.i("Debugging", "Item in Cart Plus :" + holder.numqty.getText().toString());
                    holder.totprice.setText("Total :" + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
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
                    holder.totprice.setText("Total :" + ccy + " " + computeTotal(holder.numqty.getText().toString(), holder.unitprice.getText().toString()));
                }
            });


            //Add item
            holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDB.deleteCartItem(holder.itemcode.getText().toString());
                    removeAt(position);
                    String count;
                    count = String.valueOf(myDB.getItemCount());
                    /*if (fr_itemlist.textCartItemCount !=null) {
                        fr_itemlist.textCartItemCount.setText(count);
                    } else if(fr_homepage.textCartItemCount !=null) {
                        fr_homepage.textCartItemCount.setText(count);
                    }*/
                    clickevent.clickEventItem();
                    Log.i("Debugging", "Item in Cart Delete :" + count);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeAt(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
        if (myDB.getItemCount() == 0) {
            fr_book_checkout.fillcart.setVisibility(View.INVISIBLE);
            fr_book_checkout.emptycart.setVisibility(View.VISIBLE);
        }

    }

    private String computeTotal(String qty, String price) {
        Double f = Double.parseDouble(qty) * Double.parseDouble(price);
        String formattedString = String.format("%.02f", f);
        String s = null;
        try {
            s = myDB.getTotalAmount(ccy);
            fr_book_checkout.dpriceamount = f;
        } catch (HMOwnException e) {
            e.printStackTrace();
        }
        return formattedString;
    }

    public interface ClickEvent {
        void clickEventItem();
    }

    ClickEvent clickevent;

    public void setClickEvent(ClickEvent event) {
        this.clickevent = event;
    }

}

