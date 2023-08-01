package com.aic.khidmanow;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class crossCartAdapter extends RecyclerView.Adapter<crossCartAdapter.MyViewHolder> {
    Context ctx;
    ArrayList<String> ar = new ArrayList<String>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    String ccy;
    private HMProductList hmProductList=new HMProductList();

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageButton plus;
        private ImageButton minus;
        private ImageButton addcart;
        private TextView itemcode ,itemname,unitprice,itemqty,numqty,totprice;
        LinearLayout changeinfo;
        CardView card_view;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.listImage);
            itemname=(TextView) itemView.findViewById(R.id.itemname);
            itemqty=(TextView) itemView.findViewById(R.id.itemqty);
            totprice=(TextView) itemView.findViewById(R.id.totprice);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            addcart = (ImageButton) itemView.findViewById(R.id.itemcartbtn);
            numqty=(TextView) itemView.findViewById(R.id.numqty);
            itemcode= (TextView) itemView.findViewById(R.id.itemcode);
            unitprice=(TextView) itemView.findViewById(R.id.unitprice);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            changeinfo = itemView.findViewById(R.id.changeinfo);
        }
    }

    public crossCartAdapter(Context context, ArrayList<String> ar,String ccy){
        this.ctx=context;
        this.ar=ar;
        this.ccy=ccy;
        myDB = new HMCoreData(context);
    }

    @Override
    public crossCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(R.layout.cart_slider,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final JSONObject myjson;
        try {
            myjson = new JSONObject(ar.get(position));
            Picasso.get()
                    .load(myjson.getString("imagesmallurl").toString())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .noFade()
                    .into(holder.imageView);
            float myFloat = Float.parseFloat(myjson.getString("unitprice"));
            String formattedString = String.format("%.02f", myFloat);
            holder.itemname.setText(myjson.getString("itemname"));
            holder.itemqty.setText("Price :" + ccy + " " + formattedString);
            holder.itemcode.setText(myjson.getString("itemcode").toString());
            holder.unitprice.setText(myjson.getString("unitprice").toString());
            holder.totprice.setText("Total :" + ccy + " " + computeTotal(holder.numqty.getText().toString(),holder.unitprice.getText().toString()));
            // Change background color if in cart
            Integer i = myDB.getItemCountExists(holder.itemcode.getText().toString());
            if (i>0) {
              //  holder.totprice.setVisibility(View.VISIBLE);
               holder.addcart.setVisibility(View.INVISIBLE);
           //     holder.changeinfo.setVisibility(View.VISIBLE);
            }else{
                holder.addcart.bringToFront();
             //   holder.totprice.setVisibility(View.INVISIBLE);
                holder.addcart.setVisibility(View.VISIBLE);
                holder.changeinfo.setVisibility(View.INVISIBLE);
            }



            //Add item
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(holder.numqty.getText().toString());
                    holder.numqty.setText(String.valueOf((i>100)?100:i+1));
                    myDB.updateCartItem(holder.numqty.getText().toString(),holder.itemcode.getText().toString());
                    Log.i("Debugging","Item in Cart Plus :" + holder.numqty.getText().toString());
                    holder.totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(),holder.unitprice.getText().toString()));
                }
            });

            //subtract item
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(holder.numqty.getText().toString()) ;
                    holder.numqty.setText(String.valueOf((i<2)?1:i-1));
                    myDB.updateCartItem(holder.numqty.getText().toString(),holder.itemcode.getText().toString());
                    Log.i("Debugging","Item in Cart Minus :" + holder.numqty.getText().toString());
                    holder.totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(),holder.unitprice.getText().toString()));
                }
            });

            //add to cart
            holder.addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  holder.totprice.setVisibility(View.VISIBLE);
                    holder.addcart.setVisibility(View.INVISIBLE);
                //  holder.changeinfo.setVisibility(View.VISIBLE);
                  //  holder.minus.setVisibility(View.VISIBLE);
                  //  holder.numqty.setVisibility(View.VISIBLE);
                  //  holder.plus.setVisibility(View.VISIBLE);
                    //    holder.listll.setBackgroundColor(ctx.getResources().getColor(R.color.grayBG));
                    hmProductList.itemname=holder.itemname.getText().toString();
                    hmProductList.itemcode=holder.itemcode.getText().toString();
                    hmProductList.itemdescription="";
                    hmProductList.manufacturercode="";
                    hmProductList.manufacturername="";
                    hmProductList.categorycode="";
                    hmProductList.categoryname="";
                    hmProductList.subcategorycode="";
                    hmProductList.subcategoryname="";
                    hmProductList.sizedetail="";
                    hmProductList.colordetail="";
                    hmProductList.measurementtyp="";
                    hmProductList.measurementunit="";
                    hmProductList.unitprice=holder.unitprice.getText().toString();
                    hmProductList.availableqty=holder.numqty.getText().toString();
                    hmProductList.rewardpercent="";
                    hmProductList.barcode="";
                    hmProductList.imagesmallurl="";
                    hmProductList.imagelargeurl="";
                    hmProductList.isfav="";
                    hmProductList.recurring="";
                    hmProductList.isoffer="";
                    hmProductList.offerprice="";
                    hmProductList.offerdescription="";
                    hmProductList.itemtype=HMConstants.itemtypeproduct;
                    hmProductList.recurring="";
                    hmProductList.outletcode="";
                    try {
                        myDB.addCart(hmProductList);
                        String count;
                        count= String.valueOf(myDB.getItemCount());
                        Log.i("Debugging","Item in Cart Add :" + count);
                        clickevent.clickEventItem();
                        /*if (fr_itemlist.textCartItemCount !=null) {
                            fr_itemlist.textCartItemCount.setText(count);
                        } else if(fr_homepage.textCartItemCount !=null) {
                            fr_homepage.textCartItemCount.setText(count);
                        }*/

                    } catch (HMOwnException e) {
                        e.printStackTrace();
                        myDB.alertBox(ctx,e.getMessage());
                        return;
                    }

                }
            });


            //open info window
        /*    holder.card_view.setOnClickListener(new View.OnClickListener() {
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
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String computeTotal(String qty,String price){
        Float f = Float.parseFloat(qty)*Float.parseFloat(price);
        String formattedString = String.format("%.02f", f);
        return formattedString;
    }
    @Override
    public int getItemCount() {
        return ar.size();
    }

    public interface ClickEvent {
        void clickEventItem();
    }
    ClickEvent clickevent;
    public void setClickEvent(ClickEvent event) {
        this.clickevent = event;
    }
}
