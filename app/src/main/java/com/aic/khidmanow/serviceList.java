package com.aic.khidmanow;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

public class serviceList extends RecyclerView.Adapter<serviceList.ViewHolder> {
    Context ctx;
    ArrayList<spinClass> items = new ArrayList<spinClass>();
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    //    ArrayList<Integer> intpos = new ArrayList<>();
    String formtype;

    public serviceList(Context context, ArrayList<spinClass> items, String formtype) throws HMOwnException {
        this.ctx = context;
        this.items = items;
        this.formtype = formtype;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.service_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
        //Add item

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View linqty;
        TextView itemprice, numqty;
        AppCompatImageView plus, minus;
        CheckedTextView mCheckedTextView;
        //        LinearLayout.LayoutParams params1;
        AppCompatTextView tv_total_amount;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = itemView.findViewById(R.id.check);
            linqty = itemView.findViewById(R.id.changeinfo);
            itemprice = itemView.findViewById(R.id.itemprice);
            numqty = itemView.findViewById(R.id.numqty);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            if (formtype.equals("0")) {
                linqty.setVisibility(View.GONE);
                tv_total_amount.setVisibility(View.GONE);
                itemprice.setVisibility(View.GONE);
//                params1 = (LinearLayout.LayoutParams) mCheckedTextView.getLayoutParams();
//                params1.weight = 1f;
//                mCheckedTextView.setLayoutParams(params1);
            } else if (formtype.equals("1")) {
                tv_total_amount.setVisibility(View.GONE);
                linqty.setVisibility(View.GONE);
//                params1 = (LinearLayout.LayoutParams) mCheckedTextView.getLayoutParams();
//                params1.weight = 0.8f;
//                mCheckedTextView.setLayoutParams(params1);
            }
            itemView.setOnClickListener(this);


        }

        void bind(int position) {
            // use the sparse boolean array to check
            final spinClass spin = items.get(position);
            final providerInfo providerinfo = new providerInfo(ctx);
            //loading

            /*if (!intpos.contains(position)) {
                Log.i("debugging", "Service list Selected " + String.valueOf(spin.getId()) + "    " + spin.getChecked() + "    " + spin.getPrice().toString() + "    " + spin.getQuantity());
                itemStateArray.put(position, spin.getChecked());
                intpos.add(position);
            }*/
            ArrayList<multiHolder> serviceItems = providerinfo.getAllServiceItems();
            if (serviceItems.size() > 0)
                for (multiHolder item : serviceItems) {
                    if (item.getName().equals(spin.getName())) {
                        itemStateArray.put(position, true);
                        break;
                    }
                }


            if (!itemStateArray.get(position, false)) {
                mCheckedTextView.setChecked(false);
            } else {
                mCheckedTextView.setChecked(true);
            }
            mCheckedTextView.setText(String.valueOf(spin.getName()));
            itemprice.setText(spin.getPrice().toString());
            numqty.setText(Integer.toString(spin.getQuantity()));
            tv_total_amount.setText((spin.getPrice() * spin.getQuantity()) + "");
        }

        @Override
        public void onClick(View v) {
            final multiHolder m = new multiHolder();

            final int adapterPosition = getAdapterPosition();
            final spinClass spin = items.get(adapterPosition);
            final providerInfo providerinfo = new providerInfo(ctx);
            if (!itemStateArray.get(adapterPosition, false)) {
                mCheckedTextView.setChecked(true);
                m.setName(spin.getName());
                m.setTitle(spin.getId());
                m.setQty(spin.quantity);
                m.setAmount(spin.price);
                m.setTotal(Integer.parseInt(numqty.getText().toString()) * Double.parseDouble(itemprice.getText().toString()));
//                Log.i("debugging", "Multichoice list Clicked  " + m.getQty() + "   " + m.getAmount() + "   " + m.getTitle() + "  " + m.getTotal());
                providerinfo.addServiceItem(m);
                String mitem = "";
                for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                    multiHolder n = providerinfo.getAllServiceItems().get(i);
                    mitem = mitem + n.getName() + "  " + n.getAmount() + "*" + n.getQty() + "=" + n.getTotal() + "\n";
                    //  Log.i("debugging","Items :" + mitem);

                }

                itemStateArray.put(adapterPosition, true);
            } else {
                mCheckedTextView.setChecked(false);
                providerinfo.removeServiceItem(spin.getId());
                itemStateArray.put(adapterPosition, false);
            }

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer x = Integer.valueOf(numqty.getText().toString());
                    numqty.setText(String.valueOf((x > 100) ? 100 : x + 1));
                    providerinfo.removeServiceItem(spin.getId());

                    mCheckedTextView.setChecked(true);
                    m.setName(spin.getName());
                    m.setTitle(spin.getId());
                    m.setQty(Integer.parseInt(numqty.getText().toString()));
                    spin.quantity = (Integer.parseInt(numqty.getText().toString()));
                    m.setAmount(spin.price);
                    m.setTotal(Integer.parseInt(numqty.getText().toString()) * Double.parseDouble(itemprice.getText().toString()));
                    //   Log.i("debugging", "Multichoice list Clicked  " + m.getQty() + "   " + m.getTitle() + "  " + m.getTotal());
                    providerinfo.addServiceItem(m);
                    String mitem = "";
                    for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                        multiHolder n = providerinfo.getAllServiceItems().get(i);
                        mitem = mitem + n.getName() + "  " + n.getAmount() + "*" + n.getQty() + "=" + n.getTotal() + "\n";
                        //  Log.i("debugging","Items :" + mitem);

                    }
                    if (!itemStateArray.get(adapterPosition, false)) {
                        itemStateArray.put(adapterPosition, true);
                    }

                    Log.d("debugging", "x = " + ((x > 100) ? 100 : x + 1) + "   qty = " + m.getQty());
                    tv_total_amount.setText((spin.getPrice() * ((x > 100) ? 100 : x + 1)) + "");
                }
            });

            //subtract item
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer x = Integer.valueOf(numqty.getText().toString());
                    numqty.setText(String.valueOf((x < 2) ? 1 : x - 1));
                    providerinfo.removeServiceItem(spin.getId());
                    mCheckedTextView.setChecked(true);
                    m.setName(spin.getName());
                    m.setTitle(spin.getId());
                    m.setQty(Integer.parseInt(numqty.getText().toString()));
                    spin.quantity = (Integer.parseInt(numqty.getText().toString()));
                    m.setAmount(spin.price);
                    m.setTotal(Integer.parseInt(numqty.getText().toString()) * Double.parseDouble(itemprice.getText().toString()));
                    //   Log.i("debugging", "Multichoice list Clicked  " + m.getQty() + "   " + m.getTitle() + "  " + m.getTotal());
                    providerinfo.addServiceItem(m);
                    String mitem = "";
                    for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                        multiHolder n = providerinfo.getAllServiceItems().get(i);
                        mitem = mitem + n.getName() + "  " + n.getAmount() + "*" + n.getQty() + "=" + n.getTotal() + "\n";
                        //  Log.i("debugging","Items :" + mitem);

                    }
                    if (!itemStateArray.get(adapterPosition, false)) {
                        itemStateArray.put(adapterPosition, true);
                    }

                    Log.d("debugging", "x = " + ((x < 2) ? 1 : x - 1));
                    tv_total_amount.setText((spin.getPrice() * ((x < 2) ? 1 : x - 1)) + "");
                }
            });
            Log.i("debugging", "Multichoice list Clicked  " + m.getQty() + "   " + m.getAmount() + "   " + m.getTitle() + "  " + m.getTotal());
        }
    }
}