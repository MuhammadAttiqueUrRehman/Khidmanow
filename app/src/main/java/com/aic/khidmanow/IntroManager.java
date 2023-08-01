package com.aic.khidmanow;

import android.content.Context;
import android.content.SharedPreferences;

import layout.utils.LocalManager;

public class IntroManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("first1", 0);
        editor = pref.edit();
    }

    public void setProfile(Boolean profile) {
        editor.putBoolean("profile", profile);
        editor.commit();
    }

    public boolean getProfile() {
        return pref.getBoolean("profile", false);
    }


    public void setFirst1(Boolean first1) {
        editor.putBoolean("first1", first1);
        editor.commit();
    }

    public boolean getFirst1() {
        return pref.getBoolean("first1", true);
    }

    public void setFirst2(Boolean first2) {
        editor.putBoolean("first2", first2);
        editor.commit();
    }

    public boolean getFirst2() {
        return pref.getBoolean("first2", true);
    }

    public void setLocation(String location) {
        editor.putString("location", location);
        editor.commit();
    }

    public void setFirst3(Boolean first3) {
        editor.putBoolean("first3", first3);
        editor.commit();
    }

    public boolean getFirst3() {
        return pref.getBoolean("first3", true);
    }

    public String getLocation() {
        return pref.getString("location", "");
    }

    public void setLatLon(String latlon) {
        editor.putString("latlon", latlon);
        editor.commit();
    }

    public String getLatLon() {
        return pref.getString("latlon", "");
    }

    public void setGeoLocationT(String geolocation) {
        editor.putString("geolocationt", geolocation);
        editor.commit();
    }

    public String getGeoLocationT() {
        return pref.getString("geolocationt", "");
    }


    public void setAddressLandMarkT(String addresslandmark) {
        editor.putString("addresslandmarkt", addresslandmark);
        editor.commit();
    }

    public String getAddressLandMarkT() {
        return pref.getString("addresslandmarkt", "");
    }

    public void setHomeOP(String output) {
        editor.putString("output", output);
        editor.commit();
    }

    public void isTemporary(Boolean temporaryAddress) {
        editor.putBoolean("temporaryAddress", temporaryAddress);
        editor.commit();
    }



    public boolean getIsTemporary() {
        return pref.getBoolean("temporaryAddress", false);
    }

    public String getHomeOP() {
        /*String lang = LocalManager.getLanguagePref(context);
        if (lang.equals(LocalManager.ARABIC))
            return pref.getString("output", "").replace("_a\"", "\"");
        else*/
        return new Util().processJsonForLanguage(pref.getString("output", ""), context);
    }

    public void setServiceOP(String output) {
        editor.putString("serviceoutput", output);
        editor.commit();
    }

    public String getServiceOP() {
        return pref.getString("serviceoutput", "");
    }

    public void setPageCount(int page_count) {
        editor.putInt("booking_page_count", page_count);
        editor.commit();
    }

    public int getPageCount() {
        return pref.getInt("booking_page_count", -1);
    }

    public void setStepNo(int step_no) {
        editor.putInt("booking_step_no", step_no);
        editor.commit();
    }

    public void stepNoIncrement() {
        editor.putInt("booking_step_no", getStepNo() + 1);
        editor.commit();
    }

    public void stepNoDecrement() {
        editor.putInt("booking_step_no", getStepNo() - 1);
        editor.commit();
    }

    public int getStepNo() {
        return pref.getInt("booking_step_no", -1);
    }

    //Device ID
    public void setDevice(String device) {
        editor.putString("device", device);
        editor.commit();
    }

    public String getDevice() {
        return pref.getString("device", "");
    }


    //Catalog Code
    public void setCatalogcode(String catalogcode) {
        editor.putString("catalogcode", catalogcode);
        editor.commit();
    }

    public String getCatalogcode() {
        return pref.getString("catalogcode", "");
    }

    //City Code
    public void setCityOnce(String cityonce) {
        editor.putString("cityonce", cityonce);
        editor.commit();
    }

    public String getCityOnce() {
        return pref.getString("cityonce", "");
    }

    //City Code
    public void setCity(String city) {
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity() {
        return pref.getString("city", "");
    }


    //City Name
    public void setCityName(String cityname) {
        editor.putString("cityname", cityname);
        editor.commit();
    }

    public String getCityName() {
        return pref.getString("cityname", "");
    }


    //Address Landmark
    public void setAddressLandMark(String addresslandmark) {
        editor.putString("addresslandmark", addresslandmark);
        editor.commit();
    }

    public String getAddressLandMark() {
        return pref.getString("addresslandmark", "");
    }


    //Address Register or New
    public void setAddressType(String addresstype) {
        editor.putString("addresstype", addresstype);
        editor.commit();
    }

    public String getAddressType() {
        return pref.getString("addresstype", "");
    }

    //Book Date Time
    public void setBookTime(String booktime) {
        editor.putString("booktime", booktime);
        editor.commit();
    }

    public String getBookTime() {
        return pref.getString("booktime", "");
    }

    //Geolocation
    public void setGeoLocation(String geolocation) {
        editor.putString("geolocation", geolocation);
        editor.commit();
    }

    public String getGeoLocation() {
        return pref.getString("geolocation", "");
    }

    //QA1
    public void setQAOption1(String qaoption1) {
        editor.putString("qaoption1", qaoption1);
        editor.commit();
    }

    public String getQAOption1() {
        return pref.getString("qaoption1", "");
    }

    //QA2
    public void setQAOption2(String qaoption2) {
        editor.putString("qaoption2", qaoption2);
        editor.commit();
    }

    public String getQAOption2() {
        return pref.getString("qaoption2", "");
    }

    //QA3
    public void setQAOption3(String qaoption3) {
        editor.putString("qaoption3", qaoption3);
        editor.commit();
    }

    public String getQAOption3() {
        return pref.getString("qaoption3", "");
    }

    //QA4
    public void setQAOption4(String qaoption4) {
        editor.putString("qaoption4", qaoption4);
        editor.commit();
    }

    public String getQAOption4() {
        return pref.getString("qaoption4", "");
    }

    //QA5
    public void setQAOption5(String qaoption5) {
        editor.putString("qaoption5", qaoption5);
        editor.commit();
    }

    public String getQAOption5() {
        return pref.getString("qaoption5", "");
    }

    //Additional Info
    public void setAdditionalInfo(String addinfo) {
        editor.putString("addinfo", addinfo);
        editor.commit();
    }

    public String getAdditionalInfo() {
        return pref.getString("addinfo", "");
    }


    //ServiceImage 1
    public void setServiceImage1(String serviceimage1) {
        editor.putString("serviceimage1", serviceimage1);
        editor.commit();
    }

    public String getServiceImage1() {
        return pref.getString("serviceimage1", "");
    }

    //ServiceImage 2
    public void setServiceImage2(String serviceimage2) {
        editor.putString("serviceimage2", serviceimage2);
        editor.commit();
    }

    public String getServiceImage2() {
        return pref.getString("serviceimage2", "");
    }

    //ServiceImage 3
    public void setServiceImage3(String serviceimage3) {
        editor.putString("serviceimage3", serviceimage3);
        editor.commit();
    }

    public String getServiceImage3() {
        return pref.getString("serviceimage3", "");
    }

    //ServiceImage 4
    public void setServiceImage4(String serviceimage4) {
        editor.putString("serviceimage4", serviceimage4);
        editor.commit();
    }

    public String getServiceImage4() {
        return pref.getString("serviceimage4", "");
    }

    //Item Code
    public void setItemCode(String itemcode) {
        editor.putString("itemcode", itemcode);
        editor.commit();
    }

    public String getItemCode() {
        return pref.getString("itemcode", "");
    }

    //Mobile Number
    public void setMobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString("mobile", "");
    }

    //Visiting Charges
    public void setVisitingCharges(String charges) {
        editor.putString("charges", charges);
        editor.commit();
    }

    public String getVisitingCharges() {
        return pref.getString("charges", "0");
    }

    //Firebase Key
    public void setFCMKey(String fcmkey) {
        editor.putString("fcmkey", fcmkey);
        editor.commit();
    }

    public String getFCMKey() {
        return pref.getString("fcmkey", "");
    }

    //Item Subcategory
    public void setSubcategory(String subcategory) {
        editor.putString("subcategory", subcategory);
        editor.commit();
    }

    public String getSubcategory() {
        return pref.getString("subcategory", "");
    }

    //Item Subcategory
    public void setOfferCode(String offercode) {
        editor.putString("offercode", offercode);
        editor.commit();
    }

    public String getOfferCode() {
        return pref.getString("offercode", "");
    }

    //Item Quantity
    public void setQty(int qty) {
        editor.putInt("qty", qty);
        editor.commit();
    }

    public int getQty() {
        return pref.getInt("qty", 1);
    }

    //SGST
    public void setSGST(float sgst) {
        editor.putFloat("sgst", sgst);
        editor.commit();
    }

    public float getSGST() {
        return pref.getFloat("sgst", 0);
    }

    //CGST
    public void setCGST(float cgst) {
        editor.putFloat("cgst", cgst);
        editor.commit();
    }

    public float getCGST() {
        return pref.getFloat("cgst", 0);
    }

    //App sharing text
    public void setSharingText(String sharingtext) {
        editor.putString("sharingtext", sharingtext);
        editor.commit();
    }

    public String getSharingText() {
        return pref.getString("sharingtext", "");
    }

    //App sharing url
    public void setSharingURL(String sharingurl) {
        editor.putString("sharingurl", sharingurl);
        editor.commit();
    }

    public String getSharingURL() {
        return pref.getString("sharingurl", "");
    }

    //Referrer Mobile
    public void setReferMobile(String refermobile) {
        editor.putString("refermobile", refermobile);
        editor.commit();
    }

    public String getReferMobile() {
        return pref.getString("refermobile", "");
    }

    //Referrer Device Id
    public void setReferDevice(String referdevice) {
        editor.putString("referdevice", referdevice);
        editor.commit();
    }

    public String getReferDevice() {
        return pref.getString("referdevice", "");
    }

    //New Customer Device Id
    public void setNewDevice(String newdevice) {
        editor.putString("newdevice", newdevice);
        editor.commit();
    }

    public String getNewDevice() {
        return pref.getString("newdevice", "");
    }

    public void setFirstTime(Boolean firsttime) {
        editor.putBoolean("firsttime", firsttime);
        editor.commit();
    }

    public boolean getFirstTime() {
        return pref.getBoolean("firsttime", true);
    }

    //Share Link
    public void setSharingLink(String sharinglink) {
        editor.putString("sharinglink", sharinglink);
        editor.commit();
    }

    public String getSharingLink() {
        return pref.getString("sharinglink", "");
    }


    //Qty Description
    public void setQtyDesc(String qtydesc) {
        editor.putString("qtydesc", qtydesc);
        editor.commit();
    }

    public String getQtyDesc() {
        return pref.getString("qtydesc", "");
    }

    public void setAppStoreURL(String appstoreurl) {
        editor.putString("appstoreurl", appstoreurl);
        editor.commit();
    }

    public String getAppStoreURL() {
        return pref.getString("appstoreurl", "");
    }

    public void setSupportEmail(String supportemail) {
        editor.putString("supportemail", supportemail);
        editor.commit();
    }

    public String getSupportEmail() {
        return pref.getString("supportemail", "");
    }

    public void setSupportCall(String supportcall) {
        editor.putString("supportcall", supportcall);
        editor.commit();
    }

    public String getSupportCall() {
        return pref.getString("supportcall", "");
    }

    public void setFuncCallId(String funccallid) {
        editor.putString("funccallid", funccallid);
        editor.commit();
    }

    public String getFuncCallId() {
        return pref.getString("funccallid", "");
    }

    public void setGender(String gender) {
        editor.putString("gender", gender);
        editor.commit();
    }

    public String getGender() {
        return pref.getString("gender", "");
    }

    public void setCouponRef(String couponref) {
        editor.putString("couponref", couponref);
        editor.commit();
    }

    public String getCouponRef() {
        return pref.getString("couponref", "");
    }

    public void setLandmark(String landmark) {
        editor.putString("landmark", landmark);
        editor.commit();
    }

    public String getLandmark() {
        return pref.getString("landmark", "");
    }

    public void setIOSStoreURL(String iosstoreurl) {
        editor.putString("iosstoreurl", iosstoreurl);
        editor.commit();
    }

    public String getIOSStoreURL() {
        return pref.getString("iosstoreurl", "");
    }

    public void setProviderCount(int providercount) {
        editor.putInt("providercount", providercount);
        editor.commit();
    }

    public int getProviderCount() {
        return pref.getInt("providercount", 0);
    }

    public void setServiceRating(int servicerating) {
        editor.putInt("servicerating", servicerating);
        editor.commit();
    }

    public int getServiceRating() {
        return pref.getInt("servicerating", 0);
    }

    public void setPersonLimit(int personlimit) {
        editor.putInt("personlimit", personlimit);
        editor.commit();
    }

    public int getPersonLimit() {
        return pref.getInt("personlimit", 0);
    }

    public void setMultiChoicePage(String multichoicepage) {
        editor.putString("multichoicepage", multichoicepage);
        editor.commit();
    }

    public String getMultiChoicePage() {
        return pref.getString("multichoicepage", "");
    }

    public void setPriorAppointment(int priorappointment) {
        editor.putInt("priorappointment", priorappointment);
        editor.commit();
    }

    public int getPriorAppointment() {
        return pref.getInt("priorappointment", 0);
    }

    public void setAdvBookingDays(int advbookingdays) {
        editor.putInt("advbookingdays", advbookingdays);
        editor.commit();
    }

    public int getAdvBookingDays() {
        return pref.getInt("advbookingdays", 0);
    }

    public void setMultiChoiceQuestion(String multichoicequestion) {
        editor.putString("multichoicequestion", multichoicequestion);
        editor.commit();
    }

    public String getMultiChoiceQuestion() {
        return pref.getString("multichoicequestion", "");
    }

    public void setDisableSchedule(String disableschedule) {
        editor.putString("disableschedule", disableschedule);
        editor.commit();
    }

    public String getDisableSchedule() {
        return pref.getString("disableschedule", "");
    }

    public void setLocationType(String locationtype) {
        editor.putString("locationtype", locationtype);
        editor.commit();
    }

    public String getLocationType() {
        return pref.getString("locationtype", "");
    }

    public void setPricingType(String pricingtype) {
        editor.putString("pricingtype", pricingtype);
        editor.commit();
    }

    public String getPricingType() {
        return pref.getString("pricingtype", "");
    }

    public void setPage1(String page1) {
        editor.putString("page1", page1);
        editor.commit();
    }

    public String getPage1() {
        return pref.getString("page1", "");
    }

    public void setPage2(String page2) {
        editor.putString("page2", page2);
        editor.commit();
    }

    public String getPage2() {
        return pref.getString("page2", "");
    }

    public void setPage3(String page3) {
        editor.putString("page3", page3);
        editor.commit();
    }

    public String getPage3() {
        return pref.getString("page3", "");
    }

    public void setPage4(String page4) {
        editor.putString("page4", page4);
        editor.commit();
    }

    public String getPage4() {
        return pref.getString("page4", "");
    }

    public void setPage5(String page5) {
        editor.putString("page5", page5);
        editor.commit();
    }

    public String getPage5() {
        return pref.getString("page5", "");
    }

    public void setPage6(String page6) {
        editor.putString("page6", page6);
        editor.commit();
    }

    public String getPage6() {
        return pref.getString("page6", "");
    }

    public void setPage7(String page7) {
        editor.putString("page7", page7);
        editor.commit();
    }

    public String getPage7() {
        return pref.getString("page7", "");
    }

    public void setPage8(String page8) {
        editor.putString("page8", page8);
        editor.commit();
    }

    public String getPage8() {
        return pref.getString("page8", "");
    }

    public void setShowEndDate(String showenddate) {
        editor.putString("showenddate", showenddate);
        editor.commit();
    }

    public String getShowEndDate() {
        return pref.getString("showenddate", "");
    }

    public void setShowPaymentPage(String showpaymentpage) {
        editor.putString("showpaymentpage", showpaymentpage);
        editor.commit();
    }

    public String getShowPaymentPage() {
        return pref.getString("showpaymentpage", "");
    }

    public void setCarryLocation(String carrylocation) {
        editor.putString("carrylocation", carrylocation);
        editor.commit();
    }

    public String getCarryLocation() {
        return pref.getString("carrylocation", "");
    }

    public void setPageSequence(int pagesequence) {
        editor.putInt("pagesequence", pagesequence);
        editor.commit();
    }

    public int getPageSequence() {
        return pref.getInt("pagesequence", 0);
    }

    public void setShowAddress(String showaddress) {
        editor.putString("showaddress", showaddress);
        editor.commit();
    }

    public String getShowAddress() {
        return pref.getString("showaddress", "");
    }

    public void setEndDate(String enddate) {
        editor.putString("enddate", enddate);
        editor.commit();
    }

    public String getEndDate() {
        return pref.getString("enddate", "");
    }

    public void setEstimate(String estimate) {
        editor.putString("estimate", estimate);
        editor.commit();
    }

    public String getEstimate() {
        return pref.getString("estimate", "");
    }

    public void setBookEndDate(String bookenddate) {
        editor.putString("bookenddate", bookenddate);
        editor.commit();
    }

    public String getBookEndDate() {
        return pref.getString("bookenddate", "");
    }

    public void setMultiFormType(String multiformtype) {
        editor.putString("multiformtype", multiformtype);
        editor.commit();
    }

    public String getMultiFormType() {
        return pref.getString("multiformtype", "");
    }

    public void setItemName(String itemname) {
        editor.putString("itemname", itemname);
        editor.commit();
    }

    public String getItemName() {
        return pref.getString("itemname", "");
    }

    public void setTotalAmount(Float totalamount){
        editor.putFloat("totalamount",totalamount);
        editor.commit();
    }
    public float getTotalAmount(){
        return pref.getFloat("totalamount",0);
    }
}
