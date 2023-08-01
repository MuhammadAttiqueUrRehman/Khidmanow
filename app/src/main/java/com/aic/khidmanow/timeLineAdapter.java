package com.aic.khidmanow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import layout.fr_cancel;
import layout.fr_complain;
import layout.fr_vendoradmitotp;
import layout.fr_vendorrating;
import layout.utils.LocalManager;


public class timeLineAdapter extends RecyclerView.Adapter<timeLineAdapter.CustomViewHolder> implements AsyncResponse {
    private Context context;
    private ArrayList<timeLineVO> items;
    private int cuslayout;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private HMAppVariables appVariables;
    public AppCompatActivity activity;

    public timeLineAdapter(FragmentActivity activity, ArrayList<timeLineVO> items, int cuslayout) throws HMOwnException {
        this.context = activity;
        this.items = items;
        this.cuslayout = cuslayout;
        this.intromanager = new IntroManager(context);
        this.myDB = new HMCoreData(context);
        this.appVariables = myDB.getUserData();
    }

    @Override
    public timeLineAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new timeLineAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(cuslayout, parent, false));
    }

    @Override
    public void onBindViewHolder(final timeLineAdapter.CustomViewHolder holder, final int position) {
        LinearLayout.LayoutParams params1;
        final timeLineVO timeline = items.get(position);
        /*holder.orderdate.setText(context.getString(R.string.date) + "\n" + timeline.getOrderDate());
        holder.ordernumber.setText(context.getString(R.string.order_ref) + "\n" + timeline.getOrderNumber());
        holder.itemname.setText(context.getString(R.string.desc) + "\n" + timeline.getOrderDes());
        holder.priceamount.setText(context.getString(R.string.price) + ":\n" + context.getString(R.string.Rs) + " " + timeline.getPrice());
        holder.vendorname.setText(context.getString(R.string.vendor_text) + "\n" + timeline.getVendorName());
        holder.orderstatus.setText(context.getString(R.string.status) + "\n" + timeline.getStatusdes());
        holder.remarks.setText(context.getString(R.string.remarks) + (timeline.getStatus().equals("2") ? "" : timeline.getRemarks()));
        holder.materialcharges.setText(context.getString(R.string.material_cost) + "\n" + context.getString(R.string.Rs) + " " + timeline.getMaterialCharges());
        holder.materialgst.setText(context.getString(R.string.material_vat) + "\n" + context.getString(R.string.Rs) + " " + timeline.getMaterialGST());*/

        holder.orderdate.setText(timeline.getOrderDate());
        holder.ordernumber.setText(timeline.getOrderNumber());
        holder.itemname.setText(timeline.getOrderDes());
//        holder.priceamount.setText(context.getString(R.string.Rs) + " " + String.format("%.2f", timeline.getPrice()));
        holder.vendorname.setText(timeline.getVendorName());
        holder.orderstatus.setText(timeline.getStatusdes());
        holder.remarks.setText(context.getString(R.string.remarks) + (timeline.getStatus().equals("2") ? "" : timeline.getRemarks()));

        if (LocalManager.getLanguagePref(context) == LocalManager.ENGLISH) {
            holder.priceamount.setText(context.getString(R.string.Rs) + " " + timeline.getPrice());
            holder.materialcharges.setText(context.getString(R.string.Rs) + " " + timeline.getMaterialCharges());
            holder.materialgst.setText(context.getString(R.string.Rs) + " " + timeline.getMaterialGST());
        } else {
            holder.priceamount.setText(timeline.getPrice() + " " + context.getString(R.string.Rs));
            holder.materialcharges.setText(timeline.getMaterialCharges() + " " + context.getString(R.string.Rs));
            holder.materialgst.setText(timeline.getMaterialGST() + " " + context.getString(R.string.Rs));
        }

        if (timeline.getStatus().equals(timeline.getMaxStatus())) {
            if (timeline.getStatus().equals("8")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btninactive.getLayoutParams();
                params1.weight = 1f;
                holder.btninactive.setLayoutParams(params1);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("7")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btncall.getLayoutParams();
                params1.weight = 1f;
                holder.btncall.setLayoutParams(params1);
            } else if (timeline.getStatus().equals("6")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btncomplain.getLayoutParams();
                params1.weight = 1f;
                holder.btncomplain.setLayoutParams(params1);
            } else if (timeline.getStatus().equals("5")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btninactive.getLayoutParams();
                params1.weight = 1f;
                holder.btninactive.setLayoutParams(params1);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("4")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btnprofile.getLayoutParams();
                params1.weight = 0.34f;
                holder.btnprofile.setLayoutParams(params1);
                params1 = (LinearLayout.LayoutParams) holder.btnclose.getLayoutParams();
                params1.weight = 0.33f;
                holder.btnclose.setLayoutParams(params1);
                params1 = (LinearLayout.LayoutParams) holder.btnreject.getLayoutParams();
                params1.weight = 0.33f;
                holder.btnreject.setLayoutParams(params1);
            } else if (timeline.getStatus().equals("3")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btninactive.getLayoutParams();
                params1.weight = 1f;
                holder.btninactive.setLayoutParams(params1);
                holder.btninactive.setText(timeline.getStatusdes());
            } else if (timeline.getStatus().equals("2")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                if (timeline.getCategoryCode().equals("114")) {
                    params1 = (LinearLayout.LayoutParams) holder.btncancel.getLayoutParams();
                    params1.weight = 0.5f;
                    holder.btncancel.setLayoutParams(params1);
                } else {
                    params1 = (LinearLayout.LayoutParams) holder.btncall.getLayoutParams();
                    params1.weight = 0.5f;
                    holder.btncall.setLayoutParams(params1);
                }
              /*  params1 = (LinearLayout.LayoutParams) holder.btnprofile.getLayoutParams();
                params1.weight = 0.33f;
                holder.btnprofile.setLayoutParams(params1);*/
                params1 = (LinearLayout.LayoutParams) holder.btnotp.getLayoutParams();
                params1.weight = 0.5f;
                holder.btnotp.setLayoutParams(params1);
            } else if (timeline.getStatus().equals("1")) {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btncancel.getLayoutParams();
                params1.weight = 1f;
                holder.btncancel.setLayoutParams(params1);
            } else {
                resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);
                params1 = (LinearLayout.LayoutParams) holder.btninactive.getLayoutParams();
                params1.weight = 1f;
                holder.btninactive.setLayoutParams(params1);
                holder.btninactive.setText(timeline.getStatusdes());
            }
        } else {
            resetButton(holder.btninactive, holder.btnprofile, holder.btnotp, holder.btncancel, holder.btnreject, holder.btncomplain, holder.btnclose, holder.btncall);

            params1 = (LinearLayout.LayoutParams) holder.btninactive.getLayoutParams();
            params1.weight = 1f;
            holder.btninactive.setLayoutParams(params1);
            holder.btninactive.setText(timeline.getStatusdes());
        }


        holder.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.do_you_want_to_cancel).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("customer", appVariables.ucustomerid);
                                    jsonMain.put("certificate", appVariables.ucertificate);
                                    jsonMain.put("orderid", timeline.getOrderNumber());
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(context, e.getMessage());
                                    return;
                                }
                                Log.i("debugging", "Cancel Booking Input :" + jsonParam.toString());
                                String[] myTaskParams = {"/SSMCancelServiceOrderCustomer", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    HMDataAccess aasyncTask = new HMDataAccess(context, holder.progressBar4, "SSMCancelServiceOrderCustomer");
                                    aasyncTask.delegate = (AsyncResponse) timeLineAdapter.this;
                                    aasyncTask.execute(myTaskParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myDB.showToast(context, e.getMessage());
                                    return;
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alert.show();
            }
        });

        holder.btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                activity.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + timeline.getVendorCall())));
            }
        });

        holder.btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.you_have_selected_to_reject).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //   intromanager.setSGST(timeline.getSGST());
                                //   intromanager.setCGST(timeline.getCGST());
                                Bundle bundle = new Bundle();
                                bundle.putString("actiontype", "REJECT");
                                bundle.putString("vendorid", timeline.getVendorid());
                                bundle.putString("charges", timeline.getVouchertype());
                                bundle.putString("orderid", timeline.getOrderNumber());
                                bundle.putString("itemname", timeline.getOrderDes());
                                Fragment myFragment = new fr_vendorrating();
                                myFragment.setArguments(bundle);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                            }
                        })
                        .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });

        holder.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Float serviceamount=(timeline.getPayable()) + (timeline.getPayable()*(timeline.getSGST()/100)) + (timeline.getPayable()*(timeline.getCGST()/100));
                //   final Float total=serviceamount+Float.parseFloat(timeline.getMaterialCharges())+Float.parseFloat(timeline.getMaterialGST());
                //   intromanager.setVisitingCharges(String.valueOf(total));
                //   Log.i("Debugging","Service Amount :" + serviceamount.toString());
                //   Log.i("Debugging","Total Amount :" + total.toString());
                activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("actiontype", "SUCCESS");
                bundle.putString("vendorid", timeline.getVendorid());
                bundle.putString("charges", timeline.getVouchertype());
                bundle.putString("orderid", timeline.getOrderNumber());
                bundle.putString("itemname", timeline.getOrderDes());
                Fragment myFragment = new fr_vendorrating();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        holder.btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", timeline.getOrderNumber());
                bundle.putString("vendorname", timeline.getVendorName());
                bundle.putString("categorycode", timeline.getCategoryCode());
                Fragment myFragment = new fr_vendoradmitotp();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        holder.btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("outlet", timeline.getVendorid());
                Fragment myFragment = new fr_vendorreviewsingle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
                activity = (AppCompatActivity) v.getContext();
                activity.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + timeline.getVendorCall())));
            }
        });

        holder.btncomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("orderref", timeline.getOrderNumber());
                Fragment myFragment = new fr_complain();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<OrderDetail> items = new ArrayList<>();
//                    String multichoicequestion =
                    JSONArray jsonArray = new JSONArray(timeline.getItem_detail());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objects = jsonArray.getJSONObject(i);
                        items.add(new OrderDetail(objects.getString("itemname"),
                                objects.getString("deliverqty"), objects.getString("standardprice"), objects.getString("totalamount")));
                        Log.d("debugging", i + " gere is price = " + timeline.getOrderDate() + "  " + objects.getString("standardprice"));
                    }

                    showDialog(timeline.getMultichoicequestion(), timeline.getOrderDate(), items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {

        Log.i("Debugging", handle);
        //Write local SQL
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, context);
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        Log.i("Debugging", output);
        output = new Util().processJsonForLanguage(output, context);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.equals("000")) {
            myDB.showToast(context, statusdesckey);
            return;
        } else if (handle == "SSMCancelServiceOrderCustomer") {
            Bundle bundle = new Bundle();
            bundle.putString("orderid", myjson.getString("transactionref"));
            Fragment myFragment = new fr_cancel();
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

        }

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView orderdate, ordernumber, itemname, priceamount, vendorname, orderstatus, remarks, materialcharges, materialgst;
        private Button btncancel, btnprofile, btnotp, btnclose, btncomplain, btnreject, btninactive, btncall, btn_detail;
        private ProgressBar progressBar4;

        public CustomViewHolder(View v) {
            super(v);

            orderdate = (TextView) v.findViewById(R.id.orderdate);
            ordernumber = (TextView) v.findViewById(R.id.ordernumber);
            itemname = (TextView) v.findViewById(R.id.itemname);
            priceamount = (TextView) v.findViewById(R.id.priceamount);
            vendorname = (TextView) v.findViewById(R.id.vendorname);
            orderstatus = (TextView) v.findViewById(R.id.orderstatus);
            materialcharges = (TextView) v.findViewById(R.id.materialcharges);
            materialgst = (TextView) v.findViewById(R.id.materialgst);
            remarks = (TextView) v.findViewById(R.id.remarks);
            btncancel = (Button) v.findViewById(R.id.btncancel);
            btnprofile = (Button) v.findViewById(R.id.btnprofile);
            btnotp = (Button) v.findViewById(R.id.btnotp);
            btnclose = (Button) v.findViewById(R.id.btnclose);
            btncomplain = (Button) v.findViewById(R.id.btncomplain);
            btnreject = (Button) v.findViewById(R.id.btnreject);
            btninactive = (Button) v.findViewById(R.id.btninactive);
            btncall = (Button) v.findViewById(R.id.btncall);
            btn_detail = (Button) v.findViewById(R.id.btn_detail);
            progressBar4 = (ProgressBar) itemView.findViewById(R.id.progressBar4);
        }
    }

    private void resetButton(Button btninactive, Button btnprofile, Button btnotp, Button btncancel, Button btnreject, Button btncomplain, Button btnclose, Button btncall) {
        LinearLayout.LayoutParams params1;
        params1 = (LinearLayout.LayoutParams) btninactive.getLayoutParams();
        params1.weight = 0f;
        btninactive.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnprofile.getLayoutParams();
        params1.weight = 0f;
        btnprofile.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnotp.getLayoutParams();
        params1.weight = 0f;
        btnotp.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btncancel.getLayoutParams();
        params1.weight = 0f;
        btncancel.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnreject.getLayoutParams();
        params1.weight = 0f;
        btnreject.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btncomplain.getLayoutParams();
        params1.weight = 0f;
        btncomplain.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btnclose.getLayoutParams();
        params1.weight = 0f;
        btnclose.setLayoutParams(params1);
        params1 = (LinearLayout.LayoutParams) btncall.getLayoutParams();
        params1.weight = 0f;
        btncall.setLayoutParams(params1);
    }

    private void showDialog(String question, String title, ArrayList<OrderDetail> items) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_order_detail);
       /* Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .into(((AppCompatImageView) dialog.findViewById(R.id.iv_image)));*/
//        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.tv_question)).setText(question);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((RecyclerView) dialog.findViewById(R.id.rv_detail)).setAdapter(new OrderDetailAdapter(context,
                items));

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
        private Context ctx;

        private List<OrderDetail> albumList;
        private LayoutInflater layoutInflator;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public View rl_qty, rl_price;
            public TextView title, quantity, price;

            public MyViewHolder(View itemView) {
                super(itemView);
                rl_qty = itemView.findViewById(R.id.rl_qty);
                rl_price = itemView.findViewById(R.id.rl_price);
                title = (TextView) itemView.findViewById(R.id.tv_name);
                quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
                price = (TextView) itemView.findViewById(R.id.tv_price);
            }
        }

        public OrderDetailAdapter(Context mContext, List<OrderDetail> albumList) {
            this.ctx = mContext;
            this.albumList = albumList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = layoutInflator.inflate(R.layout.order_detail_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final OrderDetail album = albumList.get(position);
            holder.title.setText(album.name);
            if (Double.parseDouble(album.totalamount) > 0) {
                holder.quantity.setText(album.quantity);
                holder.price.setText(album.price);
                holder.rl_qty.setVisibility(View.VISIBLE);
                holder.rl_price.setVisibility(View.VISIBLE);
            } else {
                holder.rl_qty.setVisibility(View.GONE);
                holder.rl_price.setVisibility(View.GONE);
            }
            //  myDB.showToast(ctx,album.getTitle());
        }

        @Override
        public int getItemCount() {
            return albumList.size();
        }
    }

    public class OrderDetail {
        String name, quantity, price, totalamount;

        public OrderDetail(String name, String quantity, String price, String totalamount) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.totalamount = totalamount;
        }
    }
}
