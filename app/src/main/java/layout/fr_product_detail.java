package layout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.HMProductList;
import com.aic.khidmanow.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_product_detail extends Fragment {
    private ImageButton plus, minus, btncart, isfavbtn, closebtn;
    private ImageView imageView;
    private TextView numqty, itemnamenorm, itemnamedet, itemprice, itemqtysel;
    private ProgressBar pgBar;
    private HMAppVariables appVariables = new HMAppVariables();
    private JSONObject myjson;
    private HMCoreData coreData;
    private HMProductList hmProductList;
    //  private String pref;
    private LinearLayout ll, ll1;
//public ViewGroup viewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        coreData = new HMCoreData(getContext());
        try {
            appVariables = coreData.getUserData();
        } catch (HMOwnException e) {
            e.printStackTrace();
            coreData.showToast(getContext(), e.getMessage());
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_fr_product_detail, container, false);
        // This shows the title, replace My Dialog Title. You can use strings too.
        imageView = (ImageView) view.findViewById(R.id.imageDet);
        itemnamenorm = (TextView) view.findViewById(R.id.itemnamenorm);
        itemnamedet = (TextView) view.findViewById(R.id.itemnamedet);
        itemqtysel = (TextView) view.findViewById(R.id.numqtyd);
        itemprice = (TextView) view.findViewById(R.id.unitprice);
        //     isfavbtn = (ImageButton) view.findViewById(R.id.imagefavorite);
        btncart = (ImageButton) view.findViewById(R.id.btncart);
        closebtn = (ImageButton) view.findViewById(R.id.imageclose);
        pgBar = (ProgressBar) view.findViewById(R.id.progressBar5);
        plus = (ImageButton) view.findViewById(R.id.plusd);
        minus = (ImageButton) view.findViewById(R.id.minusd);
        numqty = (TextView) view.findViewById(R.id.numqtyd);
        ll = (LinearLayout) view.findViewById(R.id.linearLayout);
        ll1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        try {
            Log.i("Debugging", "Json in cart " + getArguments().getString("myjson"));
            myjson = new JSONObject(getArguments().getString("myjson"));
        } catch (JSONException e) {
            e.printStackTrace();
            coreData.showToast(getContext(), e.getMessage());
            return null;
        }
        //   viewGroup=(ViewGroup) view.findViewById(R.id.holder1);
        Log.i("Debugging", "Favourite :" + getArguments().getString("isfav"));
        //  pref=getArguments().getString("isfav");
        //  isfavbtn.setBackground(pref.equals("1") ? getResources().getDrawable(R.drawable.ic_heart_filled) : getResources().getDrawable(R.drawable.ic_tab_heart_green));
        //  isfavbtn.setColorFilter(Color.parseColor("#ffffff"));
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Picasso.get()
                .load(getArguments().getString("imagelargeurl"))
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .into(imageView);
        itemnamenorm.setText(getArguments().getString("itemname"));
        itemnamedet.setText(getArguments().getString("itemdescription"));
        itemqtysel.setText(getArguments().getString("selectedqty").length() == 0 ? "1" : getArguments().getString("selectedqty"));
        float myFloat = Float.parseFloat(getArguments().getString("unitprice"));
        String formattedString = String.format("%.02f", myFloat);
        itemprice.setText("Price :" + appVariables.ucurrency + " " + formattedString);

        Integer i = coreData.getItemCountExists(getArguments().getString("itemcode"));
        if (i > 0) {
            ll.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.INVISIBLE);
        } else {
            ll.setVisibility(View.INVISIBLE);
            ll1.setVisibility(View.VISIBLE);
        }

        //Add item
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.valueOf(numqty.getText().toString());
                numqty.setText(String.valueOf((i > 100) ? 100 : i + 1));
                try {
                    coreData.updateCartItem(numqty.getText().toString(), myjson.getString("itemcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //      Log.i("Debugging","Item in Cart Plus :" + holder.numqty.getText().toString());
                //      totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(),holder.unitprice.getText().toString()));
            }
        });

        //subtract item
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.valueOf(numqty.getText().toString());
                numqty.setText(String.valueOf((i < 2) ? 1 : i - 1));
                try {
                    coreData.updateCartItem(numqty.getText().toString(), myjson.getString("itemcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //      Log.i("Debugging","Item in Cart Minus :" + holder.numqty.getText().toString());
                //      holder.totprice.setText("Amount :" + ccy + " " + computeTotal(holder.numqty.getText().toString(),holder.unitprice.getText().toString()));
            }
        });


        //subtract item
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        //add to cart
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.INVISIBLE);
                hmProductList = new HMProductList();
                try {
                    hmProductList.itemname = myjson.getString("itemname");
                    hmProductList.itemcode = myjson.getString("itemcode").toString();
                    hmProductList.itemdescription = myjson.getString("itemdescription").toString();
                    hmProductList.manufacturercode = myjson.getString("manufacturercode").toString();
                    hmProductList.manufacturername = myjson.getString("manufacturername").toString();
                    hmProductList.categorycode = myjson.getString("categorycode").toString();
                    hmProductList.categoryname = myjson.getString("categoryname").toString();
                    hmProductList.subcategorycode = myjson.getString("subcategorycode").toString();
                    hmProductList.subcategoryname = myjson.getString("subcategoryname").toString();
                    hmProductList.sizedetail = myjson.getString("sizedetail").toString();
                    hmProductList.colordetail = myjson.getString("colordetail").toString();
                    hmProductList.measurementtyp = myjson.getString("measurementtyp").toString();
                    hmProductList.measurementunit = myjson.getString("measurementunit").toString();
                    hmProductList.unitprice = myjson.getString("unitprice").toString();
                    hmProductList.availableqty = numqty.getText().toString();
                    hmProductList.rewardpercent = myjson.getString("rewardpercent").toString();
                    hmProductList.barcode = myjson.getString("barcode").toString();
                    hmProductList.imagesmallurl = myjson.getString("imagesmallurl").toString();
                    hmProductList.imagelargeurl = myjson.getString("imagelargeurl").toString();
                    hmProductList.isfav = myjson.getString("isfav").toString();
                    hmProductList.recurring = myjson.getString("recurring").toString();
                    hmProductList.isoffer = myjson.getString("isoffer").toString();
                    hmProductList.offerprice = myjson.getString("offerprice").toString();
                    hmProductList.offerdescription = myjson.getString("offerdescription").toString();
                    hmProductList.itemtype = HMConstants.itemtypeproduct;
                    hmProductList.outletcode = myjson.getString("outletcode").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    coreData.addCart(hmProductList);
                    String count;
                    count = String.valueOf(coreData.getItemCount());
                    Log.i("Debugging", "Item in Cart Add :" + count);

                    /*if (fr_itemlist.textCartItemCount != null) {
                        fr_itemlist.textCartItemCount.setText(count);
                    } else if(fr_homepage.textCartItemCount !=null) {
                            fr_homepage.textCartItemCount.setText(count);
                        }*/

                } catch (HMOwnException e) {
                    e.printStackTrace();
                    return;
                }

            }
        });
        return view;

    }

}