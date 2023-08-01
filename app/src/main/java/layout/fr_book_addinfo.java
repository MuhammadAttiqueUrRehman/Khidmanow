package layout;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_book_addinfo extends Fragment {
    private static final int REQUEST_PERMISSION_LOCATION = 1;
    private static final int REQUEST_PERMISSION_CAMERA1 = 1;
    private static final int REQUEST_PERMISSION_CAMERA2 = 2;
    private static final int REQUEST_PERMISSION_CAMERA3 = 3;
    private static final int REQUEST_PERMISSION_CAMERA4 = 4;
    private static final int REQUEST_PERMISSION_STORAGE = 4;
    private static final int CAMERA_PIC_REQUEST1 = 1;
    private static final int CAMERA_PIC_REQUEST2 = 2;
    private static final int CAMERA_PIC_REQUEST3 = 3;
    private static final int CAMERA_PIC_REQUEST4 = 4;
    HMCoreData myDB;
    Toolbar mtoolbar;
    //   private TextView mTitle,mSubTitle;
    //  private RecyclerView recyclerView;
//    private questionAdapter serviceadapter;
    View v;
    Button next;
    //    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    IntroManager intromanager;
    EditText txtAddinfo;
    ImageButton camera1, camera2, camera3, camera4;
    Bitmap bmap;
    Fragment myFragment;

    public fr_book_addinfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB = new HMCoreData(getActivity());
        intromanager = new IntroManager(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_book_addinfo, container, false);
        TextView quest = (TextView) v.findViewById(R.id.quest1);

        //     recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        new Util().setUpStepsBar(v, intromanager.getPageCount(), intromanager.getStepNo());
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_01)).setImageResource(R.drawable.ic_step_done);
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_02)).setImageResource(R.drawable.ic_step_done);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
  /*      mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Additional Info");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);*/


        txtAddinfo = (EditText) v.findViewById(R.id.txtAddinfo);
        //    previous=(Button) v.findViewById(R.id.btnprev);
        next = (Button) v.findViewById(R.id.btnnext);

        camera1 = (ImageButton) v.findViewById(R.id.camera1);
        camera2 = (ImageButton) v.findViewById(R.id.camera2);
        camera3 = (ImageButton) v.findViewById(R.id.camera3);
        camera4 = (ImageButton) v.findViewById(R.id.camera4);
        camera1.setBackgroundColor(Color.WHITE);
        camera2.setBackgroundColor(Color.WHITE);
        camera3.setBackgroundColor(Color.WHITE);
        camera4.setBackgroundColor(Color.WHITE);

        JSONObject myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
        }

        //saveAddInfo();
        intromanager.setAdditionalInfo(txtAddinfo.getText().toString());
        bmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera_logo);
        if (intromanager.getServiceImage1().length() > 0) {
            final byte[] decodedBytes = Base64.decode(intromanager.getServiceImage1(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera1.setImageBitmap(decodedByte);
        } else {
            intromanager.setServiceImage1(getEncoded64ImageStringFromBitmap(bmap));
        }


        if (intromanager.getServiceImage2().length() > 0) {
            final byte[] decodedBytes = Base64.decode(intromanager.getServiceImage2(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera2.setImageBitmap(decodedByte);
        } else {
            intromanager.setServiceImage2(getEncoded64ImageStringFromBitmap(bmap));
        }


        if (intromanager.getServiceImage3().length() > 0) {
            final byte[] decodedBytes = Base64.decode(intromanager.getServiceImage3(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera3.setImageBitmap(decodedByte);
        } else {
            intromanager.setServiceImage3(getEncoded64ImageStringFromBitmap(bmap));
        }

        if (intromanager.getServiceImage4().length() > 0) {
            final byte[] decodedBytes = Base64.decode(intromanager.getServiceImage4(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera4.setImageBitmap(decodedByte);
        } else {
            intromanager.setServiceImage4(getEncoded64ImageStringFromBitmap(bmap));
        }


        camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA1);
                }
            }
        });


        camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST2);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA2);
                }
            }
        });

        camera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST3);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA3);
                }
            }
        });

        camera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST4);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA4);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveAddInfo();
                //  Fragment myFragment;
                intromanager.setAdditionalInfo(txtAddinfo.getText().toString());
             /*   if(intromanager.getQtyDesc().equals("")) {
                     myFragment = new fr_book_address();
                }else{
                     myFragment = new fr_book_qty();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

                /*<telerik:RadComboBoxItem runat="server" Text="Hourly" Value="fr_hourpage" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 1)" Value="fr_qapage1" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 2)" Value="fr_qapage2" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 3)" Value="fr_qapage3" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 4)" Value="fr_qapage4" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 5)" Value="fr_qapage5" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add info only" Value="fr_book_addinfotext" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add Info with images" Value="fr_book_addinfo" />
			                                     <telerik:RadComboBoxItem runat="server" Text="End Date" Value="fr_book_enddate" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Customer or Other Location" Value="fr_book_address" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Vendor Location" Value="fr_book_vendorlocation" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Destination Location" Value="fr_book_toaddress" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice Page only" Value="fr_book_multionly" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice page with Price" Value="fr_book_multiPrice" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi Choice Page with Qty and Price" Value="fr_book_multiPriceqty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Quantity Page" Value="fr_book_qty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Schedule/Appointment Page" Value="fr_book_time" />
			                                                                <telerik:RadComboBoxItem runat="server" Text="Payment Page" Value="fr_makepayment" />*/
                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    if ((json_array_pageflow.length()) == intromanager.getPageSequence()) {
                        if (!MainActivity.slogged) {
                            Log.i("Debugging", "Came to Login Form " + MainActivity.slogged);
                            Fragment myFragment = new fr_login();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        } else {
                            Fragment myFragment = new fr_book_checkout();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
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
                            } else if (objects.getString("pagecode").equals("fr_book_address")) {
                                myFragment = new fr_book_address();
                            } else if (objects.getString("pagecode").equals("fr_book_toaddress")) {
                                myFragment = new fr_book_toaddress();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfotext")) {
                                myFragment = new fr_bookaddinfotext();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfo")) {
                                myFragment = new fr_book_addinfo();
                            } else if (objects.getString("pagecode").equals("fr_book_qty")) {
                                myFragment = new fr_book_qty();
                            } else if (objects.getString("pagecode").equals("fr_book_time")) {
                                myFragment = new fr_book_time();
                            }

                        }
                    }
                    intromanager.stepNoIncrement();
                    intromanager.setPageSequence(intromanager.getPageSequence() + 1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });


 /*       mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goprev(v);
            }
        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  saveAddInfo();
                goprev(v);
            }
        });*/

        return v;
    }

  /*  private void goprev(View v) {
        intromanager.setAdditionalInfo(txtAddinfo.getText().toString());
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment myFragment = null;
        JSONObject myjson = null;
        JSONArray newArray5, newArray4, newArray3, newArray2, newArray1 = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            newArray5 = myjson.getJSONArray("question5");
            newArray4 = myjson.getJSONArray("question4");
            newArray3 = myjson.getJSONArray("question3");
            newArray2 = myjson.getJSONArray("question2");
            newArray1 = myjson.getJSONArray("question1");
            if (newArray5.length() > 0) {
                myFragment = new fr_qapage5();
            } else if (newArray4.length() > 0) {
                myFragment = new fr_qapage4();
            } else if (newArray3.length() > 0) {
                myFragment = new fr_qapage3();
            } else if (newArray2.length() > 0) {
                myFragment = new fr_qapage2();
            } else if (newArray1.length() > 0) {
                myFragment = new fr_qapage1();
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }*/
   /* private void saveAddInfo(){
        //Get the default image to base64 encoded and additional info
        intromanager.setAdditionalInfo(txtAddinfo.getText().toString());
        camera1.buildDrawingCache();
        bmap = camera1.getDrawingCache();
        intromanager.setServiceImage1(getEncoded64ImageStringFromBitmap(bmap));

        camera2.buildDrawingCache();
        bmap = camera2.getDrawingCache();
        intromanager.setServiceImage2(getEncoded64ImageStringFromBitmap(bmap));

        camera3.buildDrawingCache();
        bmap = camera1.getDrawingCache();
        intromanager.setServiceImage3(getEncoded64ImageStringFromBitmap(bmap));

        camera4.buildDrawingCache();
        bmap = camera1.getDrawingCache();
        intromanager.setServiceImage4(getEncoded64ImageStringFromBitmap(bmap));
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Debugging", "Camera pERMISSION GIVEN");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
            }
        }
        if (requestCode == REQUEST_PERMISSION_CAMERA2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST2);
            }
        }

        if (requestCode == REQUEST_PERMISSION_CAMERA3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST3);
            }
        }

        if (requestCode == REQUEST_PERMISSION_CAMERA4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST4);
            }
        }
      /*  if (requestCode == REQUEST_PERMISSION_LOCATION) {
            setLocation();
        }
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            Log.i("Debugging","Camera Result Executed " + REQUEST_PERMISSION_STORAGE);
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, SELECT_PICTURE);

        }*/
    }

    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Debugging", "requestCode" + requestCode);
        Log.i("Debugging", "resultCode" + resultCode);
        Log.i("Debugging", "data" + data);
        if (data == null) {
            myDB.showToast(getActivity(), getString(R.string.camera_activity_cancelled));
            return;
        }

        if (Integer.toString(resultCode).equals("0")) {
            myDB.showToast(getActivity(), getString(R.string.camera_activity_cancelled));
            return;
        }

     /*   if (requestCode == SELECT_PICTURE){
            try {
                // When an Image is picked
                if (requestCode == SELECT_PICTURE  && null != data) {
                    // Get the Image from data
                    Log.i("Debugging","Camera Result Executed");
                    //    Bitmap image = (Bitmap) data.getExtras().get("data");
                    //       Log.i("Debugging",image.toString());
                    //     profileimage.setImageBitmap(image);
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    profileimage.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    saveImage();
                } else {
                    myDB.showToast(getContext(),"You haven't picked Image");
                    return;
                }
            } catch (Exception e) {
                myDB.showToast(getContext(),"Cannot load image due to an error.");            }
            return;
        }*/
        if (requestCode == CAMERA_PIC_REQUEST1) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera1.setImageBitmap(image);
            camera1.buildDrawingCache();
            bmap = camera1.getDrawingCache();
            intromanager.setServiceImage1(getEncoded64ImageStringFromBitmap(bmap));
            Log.i("Debugging", "Bitmap 1 " + intromanager.getServiceImage1());
        }
        if (requestCode == CAMERA_PIC_REQUEST2) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera2.setImageBitmap(image);
            camera2.buildDrawingCache();
            bmap = camera2.getDrawingCache();
            intromanager.setServiceImage2(getEncoded64ImageStringFromBitmap(bmap));
            Log.i("Debugging", "Bitmap 2 " + intromanager.getServiceImage2());
        }
        if (requestCode == CAMERA_PIC_REQUEST3) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera3.setImageBitmap(image);
            camera3.buildDrawingCache();
            bmap = camera3.getDrawingCache();
            intromanager.setServiceImage3(getEncoded64ImageStringFromBitmap(bmap));
            Log.i("Debugging", "Bitmap 3 " + intromanager.getServiceImage3());
        }
        if (requestCode == CAMERA_PIC_REQUEST4) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera4.setImageBitmap(image);
            camera4.buildDrawingCache();
            bmap = camera4.getDrawingCache();
            intromanager.setServiceImage4(getEncoded64ImageStringFromBitmap(bmap));
            Log.i("Debugging", "Bitmap 4 " + intromanager.getServiceImage4());
        }
    }

  /*  private void saveImage(){
        profileimage.buildDrawingCache();
        Bitmap bmap = profileimage.getDrawingCache();
        String encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("merchantcode", HMConstants.appmerchantid);
            jsonParam.put("mdevice", hmAppVariables.udevicecomboined);
            jsonParam.put("password", hmAppVariables.ucertificate);
            jsonParam.put("customerid", hmAppVariables.ucustomerid);
            jsonParam.put("image1", encodedImageData);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return;
        }

        String[] myTaskParams = {"/updateimage.aspx", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar17, "changeimage");
            aasyncTask.delegate = (AsyncResponse) fr_profile.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return;
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        intromanager.stepNoDecrement();
        intromanager.setPageSequence(intromanager.getPageSequence() - 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
}
