package layout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import de.hdodenhof.circleimageview.CircleImageView;

import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.placesActivity;
//import com.facebook.login.LoginManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class fr_profile extends Fragment implements AsyncResponse {
    private static final int RESULT_OK = 5;
    private int PLACE_PICKER_REQUEST = 1;
    final int CAMERA_PIC_REQUEST = 1337;
   // LoginManager loginmanager;
    private static final int SELECT_PICTURE = 3;
    private static final int PLACES_RETURN_RESULT = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private static final int REQUEST_PERMISSION_STORAGE = 4;
    private EditText txtlocation, txtemail, txtfirstname, txtlastname, txtaddress, txtmobile;
    private RadioButton male, female, unspecified;
    private Button btnsave, btnimage, btndelete;
    private ImageButton btnback, btnlocation;
    private ProgressBar progressBar17;
    private HMCoreData myDB;
    private HMAppVariables hmAppVariables;
    private double lat, lon;
    private List<Address> addresses;
    private PopupMenu popup;
    private CircleImageView profileimage;
    private IntroManager intromanager;
  //  placesActivity adapter = new placesActivity();
    Context c;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        try {
            hmAppVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_profile, container, false);
        btnsave = v.findViewById(R.id.btnsave);
        btndelete = v.findViewById(R.id.btndelete);
        btnimage = v.findViewById(R.id.btnimage);
        btnback = v.findViewById(R.id.btnback);
        btnlocation = v.findViewById(R.id.btnlocation);
        txtlocation = v.findViewById(R.id.addressloc);
        txtemail = v.findViewById(R.id.txtemail);
        txtmobile = v.findViewById(R.id.txtmobile);
        txtfirstname = v.findViewById(R.id.txtfirstname);
        txtlastname = v.findViewById(R.id.txtlastname);
        txtaddress = v.findViewById(R.id.address);
        progressBar17 = v.findViewById(R.id.progressBar17);
        profileimage = v.findViewById(R.id.profileimage);
        male = (RadioButton) v.findViewById(R.id.male);
        female = (RadioButton) v.findViewById(R.id.female);
        unspecified = (RadioButton) v.findViewById(R.id.nodisclose);
        txtlocation.setText(intromanager.getLatLon());
        txtaddress.setText(intromanager.getLocation());
        txtfirstname.setText(hmAppVariables.ufirstname);
        txtlastname.setText(hmAppVariables.ulastname);
        txtemail.setText(hmAppVariables.uemailid);
        txtmobile.setText(intromanager.getMobile());
        intromanager.setProfile(true);
     //   adapter.setClickEvent(this);
        if (intromanager.getGender().equals("M")) {
            male.setChecked(true);
        } else if (intromanager.getGender().equals("F")) {
            female.setChecked(true);
        } else {
            unspecified.setChecked(true);
        }

      /*  Picasso.with(getContext())
                .load(hmAppVariables.ucuspic)
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .into(profileimage);*/

        if (!hmAppVariables.ucuspic.contains("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            byte[] decodedString = Base64.decode(hmAppVariables.ucuspic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileimage.setImageBitmap(decodedByte);
        }

        txtfirstname.setEnabled(false);
        txtlastname.setEnabled(false);
        txtmobile.setEnabled(false);
        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), placesActivity.class);
                startActivityForResult(intent,PLACES_RETURN_RESULT);
            }
        });


        btndelete.setOnClickListener(v1 -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                                        alert.setTitle(HMConstants.alertheader);
            alert.setTitle(R.string.app_title);
            alert.setMessage(R.string.this_action_will_delete_the_account_and_all_transactions_related_to_account).setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            purgeAccount();
                        }
                    })
                    .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alert.show();
        });

        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.picture_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnucamera:
                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                                } else {
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                                }
                                return true;
                            case R.id.mnugallery:
                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    // Create intent to Open Image applications like Gallery, Google Photos
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    // Start the Intent
                                    startActivityForResult(galleryIntent, SELECT_PICTURE);
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                                }
                                return true;
                            default:
                                return false;
                        }
                    }

                });
                popup.show();
            }


        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtaddress.getText().toString().trim().length() == 0) {
                    txtaddress.setError("Enter or set a location address landmark");
                    return;
                }
                if (txtfirstname.getText().toString().trim().length() == 0) {
                    txtfirstname.setError("Enter first name");
                    return;
                }

                if (txtlastname.getText().toString().trim().length() == 0) {
                    txtlastname.setError("Enter last name");
                    return;
                }

                if (txtemail.getText().toString().trim().length() == 0) {
                    txtemail.setError("Enter a valid email address");
                    return;
                }

                if (txtlocation.getText().toString().trim().length() == 0) {
                    txtlocation.setError("Select geolocation");
                    return;
                }
                if (!myDB.isValidEmailId(txtemail.getText().toString().trim())) {
                    txtemail.setError("Invalid Email Address");
                    return;
                }

                if (male.isChecked()) {
                    intromanager.setGender("M");
                } else if (female.isChecked()) {
                    intromanager.setGender("F");
                } else {
                    intromanager.setGender("U");
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                hmAppVariables.uareaname = txtaddress.getText().toString();
                hmAppVariables.uemailid = txtemail.getText().toString();
                hmAppVariables.ufirstname = txtfirstname.getText().toString();
                hmAppVariables.ulastname = txtlastname.getText().toString();
                intromanager.setLatLon(txtlocation.getText().toString());
                intromanager.setLocation(txtaddress.getText().toString());
                hmAppVariables.applat = lat;
                hmAppVariables.applon = lon;
                hmAppVariables.firsttimeaccess = "NO";
                hmAppVariables.ufullname = txtfirstname.getText().toString() + " " + txtlastname.getText().toString();
                try {
                    hmAppVariables = myDB.updateUserData(hmAppVariables);

                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }
                Log.i("Debugging", "Geo 1" + hmAppVariables.applat + "," + hmAppVariables.applon);
                Log.i("Debugging", "Geo 2" + lat + "," + lon);

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("firstname", txtfirstname.getText());
                    jsonMain.put("lastname", txtlastname.getText());
                    jsonMain.put("email", txtemail.getText());
                    jsonMain.put("gender", intromanager.getGender());
                    jsonMain.put("certificate", hmAppVariables.ucertificate);
                    jsonMain.put("customer", hmAppVariables.ucustomerid);
                    jsonMain.put("address", txtaddress.getText().toString());
                    jsonMain.put("geolocation", txtlocation.getText().toString());
                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }

                String[] myTaskParams = {"/SSMUpdateCustomerProfile", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar17, "changeprofile");
                    aasyncTask.delegate = (AsyncResponse) fr_profile.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Debugging", "Camera pERMISSION GIVEN");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        }
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            Log.i("Debugging", "Camera Result Executed " + REQUEST_PERMISSION_STORAGE);
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, SELECT_PICTURE);

        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            myDB.showToast(getActivity(), "Camera activity cancelled and closed");
            return;
        }
        if (Integer.toString(resultCode).equals("0")) {
            myDB.showToast(getActivity(), "Camera activity cancelled and closed");
            return;
        }
        if (data != null) {
            if (requestCode == PLACES_RETURN_RESULT) {
            txtlocation.setText(intromanager.getLatLon());
            txtaddress.setText(intromanager.getLocation());
/*                Place place = PlacePicker.getPlace(data, getActivity());
                String address = String.format("%s", place.getAddress());
                String latlng = String.format("%s", place.getLatLng());
                latlng = latlng.substring(latlng.indexOf("(") + 1, latlng.length() - 1);
                txtlocation.setText(latlng);
                txtaddress.setText(address);
                String[] parts = latlng.split(",");
                lat = Double.parseDouble(parts[0]); // 004
                lon = Double.parseDouble(parts[1]); // 034556
*/
                return;
            } else if (requestCode == SELECT_PICTURE) {
                try {
                    // When an Image is picked
                    if (requestCode == SELECT_PICTURE && null != data) {
                        // Get the Image from data
                        Log.i("Debugging", "Camera Result Executed");
                        //    Bitmap image = (Bitmap) data.getExtras().get("data");
                        //       Log.i("Debugging",image.toString());
                        //     profileimage.setImageBitmap(image);
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
                        myDB.showToast(getContext(), "You haven't picked Image");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                return;
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                profileimage.setImageBitmap(image);
                saveImage();
            }
        } else {
            myDB.showToast(getActivity(), "Camera activity cancelled and closed");
            return;
        }
    }

    private void saveImage() {
        profileimage.buildDrawingCache();
        Bitmap bmap = profileimage.getDrawingCache();
        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);
        hmAppVariables.ucuspic = encodedImageData;
        try {
            hmAppVariables = myDB.updateUserData(hmAppVariables);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("image", HMConstants.imagetype + encodedImageData);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }

        String[] myTaskParams = {"/SSMUpdateImage", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar17, "changeimage");
            aasyncTask.delegate = (AsyncResponse) fr_profile.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }


    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (handle == "changeimage") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                myDB.showToast(getContext(), getString(R.string.new_picture_saved));
                return;
            }
        }
        else if (handle == "changeprofile") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.profile_changes_acknoled));
                bundle.putString("message", getString(R.string.your_profile_changes_));
                Fragment myFragment = new fr_settings_confirm();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        }
        else if (handle == "purge") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            }
            else {
                logmeout();
            }
        }
        else if (handle == "logmeout") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                MainActivity.slogged = false;
                hmAppVariables = myDB.getUserData();
                hmAppVariables.ulogged = false;
                hmAppVariables.ualcohol = "";
                hmAppVariables.uautolocation = "";
                hmAppVariables.ubirthdate = "";
                hmAppVariables.ucertificate = "";
                hmAppVariables.ucuspic = "";
                hmAppVariables.ucusqr = "";
                hmAppVariables.ucustomerid = "";
                hmAppVariables.ucustomername = "";
                hmAppVariables.udisplaybirthdate = "";
                hmAppVariables.uemailid = "";
                hmAppVariables.ufbid = "";
                hmAppVariables.ufirstname = "";
                hmAppVariables.ufullname = "";
                hmAppVariables.uhomecountry = "";
                hmAppVariables.uhomecountryname = "";
                hmAppVariables.uinitdate = "";
                hmAppVariables.ulastname = "";
                hmAppVariables.umagicnumber = "";
                hmAppVariables.umaxspend = 0;
                hmAppVariables.umemberexpiry = "";
                hmAppVariables.unationalitycode = "";
                hmAppVariables.upinnumber = "";
                hmAppVariables.upointvalue = 0;
                hmAppVariables.upushoffer = "";
                hmAppVariables.uremindexpiry = "";
                hmAppVariables.uresidentcity = "";
                hmAppVariables.uresidentcountry = "";
                hmAppVariables.uresidentcountryname = "";
                hmAppVariables.usegmentcode = "";
                hmAppVariables.usegmentimage = "";
                hmAppVariables.usegmentname = "";
                hmAppVariables.ushowprofile = "";
                hmAppVariables.uspend = 0;
                intromanager.setMobile("");

                //Print all appvariables
                Log.i("Debugging", hmAppVariables.udevicecomboined);
                Log.i("Debugging", hmAppVariables.appcustomeremailbody);
                Log.i("Debugging", hmAppVariables.appcustomeremailid);
                Log.i("Debugging", hmAppVariables.appcustomeremailsubject);
                Log.i("Debugging", hmAppVariables.appcustomertelephone);
                Log.i("Debugging", String.valueOf(hmAppVariables.appenrolmetnamount));
                Log.i("Debugging", hmAppVariables.appgoogleapikey);
                Log.i("Debugging", String.valueOf(hmAppVariables.applat));
                Log.i("Debugging", hmAppVariables.applocationshort);
                Log.i("Debugging", hmAppVariables.applocationlong);
                Log.i("Debugging", String.valueOf(hmAppVariables.applon));
                Log.i("Debugging", hmAppVariables.appmerchantid);
                Log.i("Debugging", hmAppVariables.appsocialmessagelong);
                Log.i("Debugging", hmAppVariables.appsocialmessageshort);
                Log.i("Debugging", hmAppVariables.appurl);
                Log.i("Debugging", hmAppVariables.ucity);
                Log.i("Debugging", hmAppVariables.ucountry);
                Log.i("Debugging", hmAppVariables.ucurrency);
                Log.i("Debugging", hmAppVariables.appdeviceexist);
                Log.i("Debugging", hmAppVariables.ualcohol);
                Log.i("Debugging", hmAppVariables.uareaname);
                Log.i("Debugging", hmAppVariables.uautolocation);
                Log.i("Debugging", hmAppVariables.ubirthdate);
                Log.i("Debugging", hmAppVariables.ucertificate);
                Log.i("Debugging", hmAppVariables.ucityname);
                Log.i("Debugging", hmAppVariables.ucountryname);
                Log.i("Debugging", hmAppVariables.ucuspic);
                Log.i("Debugging", hmAppVariables.ucusqr);
                Log.i("Debugging", hmAppVariables.ucustomerid);
                Log.i("Debugging", hmAppVariables.ucustomername);
                Log.i("Debugging", hmAppVariables.udisplaybirthdate);
                Log.i("Debugging", hmAppVariables.uemailid);
                Log.i("Debugging", hmAppVariables.ufbid);
                Log.i("Debugging", hmAppVariables.ufirstname);
                Log.i("Debugging", hmAppVariables.ufullname);
                Log.i("Debugging", hmAppVariables.uhomecountry);
                Log.i("Debugging", hmAppVariables.uhomecountryname);
                Log.i("Debugging", hmAppVariables.uinitdate);
                Log.i("Debugging", hmAppVariables.ulastname);
                Log.i("Debugging", hmAppVariables.ulocationname);
                Log.i("Debugging", String.valueOf(hmAppVariables.ulogged));
                Log.i("Debugging", hmAppVariables.umagicnumber);
                Log.i("Debugging", String.valueOf(hmAppVariables.umaxspend));
                Log.i("Debugging", hmAppVariables.umemberexpiry);
                Log.i("Debugging", hmAppVariables.umobilenumber);
                Log.i("Debugging", hmAppVariables.unationalitycode);
                Log.i("Debugging", hmAppVariables.upinnumber);
                Log.i("Debugging", String.valueOf(hmAppVariables.upointvalue));
                Log.i("Debugging", hmAppVariables.upushoffer);
                Log.i("Debugging", hmAppVariables.uremindexpiry);
                Log.i("Debugging", hmAppVariables.uresidentcity);
                Log.i("Debugging", hmAppVariables.uresidentcountry);
                Log.i("Debugging", hmAppVariables.uresidentcountryname);
                Log.i("Debugging", hmAppVariables.usegmentcode);
                Log.i("Debugging", hmAppVariables.usegmentimage);
                Log.i("Debugging", hmAppVariables.usegmentname);
                Log.i("Debugging", hmAppVariables.ushowprofile);
                Log.i("Debugging", String.valueOf(hmAppVariables.uspend));


                //User Data restored even after logout
           /* appVariables.ucity=myjson.getString("city");
            appVariables.ucountry=myjson.getString("country");
            appVariables.ucurrency=myjson.getString("currency");
            appVariables.ulocationname=myjson.getString("locationname");
            appVariables.uareaname=myjson.getString("areaname");
            appVariables.umobilenumber=myjson.getString("mobilenumber");
            appVariables.applat= Double.parseDouble(myjson.getString("userlat"));
            appVariables.applon= Double.parseDouble(myjson.getString("userlon"));*/


                hmAppVariables = myDB.updateUserData(hmAppVariables);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.the_customer_account_has_been_deleted_successfully).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                alert.show();

            }

        }
    }


    private void purgeAccount(){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMSelfPurge", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar17, "purge");
            aasyncTask.delegate = (AsyncResponse) fr_profile.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    private void logmeout() {
        Log.i("Debugging", "Fcaebook Id check when logout " + hmAppVariables.ufbid);
        if (!hmAppVariables.ufbid.equals("")) {
            //loginmanager.getInstance().logOut();
        }
        //Call Logout service
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMlogmeout", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar17, "logmeout");
            aasyncTask.delegate = (AsyncResponse) fr_profile.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

/*
    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        Log.i("Debugging", "Came to restore profile" );
     //   if (visible && isResumed()){   // only at fragment screen is resumed
            txtlocation.setText(intromanager.getLatLon());
            txtaddress.setText(intromanager.getLocation());
      //  }
    }*/
  /*  @Override
    public void clickEventItem(String value, String value1) {
        Log.i("Debugging", "Location------------------->  " + value );
        Log.i("Debugging", "Address-------------------->  " + value1 );
        txtlocation.setText(value);
        txtaddress.setText(value1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.c = context;
    }*/



 /*   @Override
    public void clickEventItem() {

    }*/
}
