package com.aic.khidmanow;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import layout.fr_book_addinfo;
import layout.fr_book_address;
import layout.fr_book_checkout;
import layout.fr_book_qty;
import layout.fr_book_time;
import layout.fr_book_toaddress;
import layout.fr_book_vendorlocation;
import layout.fr_bookaddinfotext;
import layout.fr_login;
import layout.fr_multionly;
import layout.fr_multiprice;
import layout.fr_multipriceqty;
import layout.fr_qapage1;

public class buttonAdapter  extends RecyclerView.Adapter<buttonAdapter.MyViewHolder> {
    private Context ctx;
    private IntroManager intromanager;
    private List<Album> albumList;
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    JSONArray json_array_pageflow;
    Fragment myFragment;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Button btnitem;
        public MyViewHolder(View itemView) {
            super(itemView);
            btnitem =  itemView.findViewById(R.id.btnnext);

        }
    }

    public buttonAdapter(Context mContext, List<Album> albumList){
        this.ctx=mContext;
        this.albumList=albumList;
    }

    @NonNull
    @Override
    public buttonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        intromanager=new IntroManager(ctx);
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(R.layout.button_list,parent,false);
        return new MyViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(buttonAdapter.MyViewHolder holder, int position) {
        final Album album = albumList.get(position);
        final String tcode = album.getTitle();
        holder.btnitem.setText(album.getSubcategory());

        holder.btnitem.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        intromanager.setQAOption1(tcode);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        try {
                        JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                        json_array_pageflow = myjson.getJSONArray("pageflow");
                            if ((json_array_pageflow.length())==intromanager.getPageSequence()){
                                if (!MainActivity.slogged){
                                    Log.i("Debugging", "Came to Login Form " + MainActivity.slogged);
                                    Fragment myFragment = new fr_login();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                                } else {
                                    Fragment myFragment = new fr_book_checkout();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                                }
                            }
                        for (int i = 0; i < json_array_pageflow.length(); i++) {
                            JSONObject objects = json_array_pageflow.getJSONObject(i);
                            if (intromanager.getPageSequence() == i) {
                             if (objects.getString("pagecode").equals("fr_qapage1")) {
                                    myFragment = new fr_qapage1();
                                } else if (objects.getString("pagecode").equals("fr_book_vendorlocation")) {
                                    myFragment = new fr_book_vendorlocation();
                                } else if (objects.getString("pagecode").equals("fr_book_multionly")) {
                                    myFragment = new fr_multionly();
                                } else if (objects.getString("pagecode").equals("fr_book_multiPrice")) {
                                    myFragment = new fr_multiprice();
                                } else if (objects.getString("pagecode").equals("fr_book_multiPriceqty")) {
                                    myFragment = new fr_multipriceqty();
                                }else if (objects.getString("pagecode").equals("fr_book_address")) {
                                    myFragment = new fr_book_address();
                                }else if (objects.getString("pagecode").equals("fr_book_toaddress")) {
                                    myFragment = new fr_book_toaddress();
                                } else if (objects.getString("pagecode").equals("fr_book_addinfotext")) {
                                    myFragment = new fr_bookaddinfotext();
                                } else if (objects.getString("pagecode").equals("fr_book_addinfo")) {
                                    myFragment = new fr_book_addinfo();
                                }  else if (objects.getString("pagecode").equals("fr_book_qty")) {
                                    myFragment = new fr_book_qty();
                                }  else if (objects.getString("pagecode").equals("fr_book_time")) {
                                    myFragment = new fr_book_time();
                                }
                            }
                        }
                        if (myFragment == null) {
                            myDB.showToast(ctx, "Page flow parameters not set. Unable to proceed with the next page flow");
                        }else{
                            intromanager.setPageSequence(intromanager.getPageSequence() + 1);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            myDB.showToast(ctx, e.getMessage());
                        }
                    }
                }

        );



    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
