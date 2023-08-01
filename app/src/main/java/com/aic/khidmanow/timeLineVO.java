package com.aic.khidmanow;

public class timeLineVO {
    private String orderdate, ordernumber, orderdes, status, statusdes, vendorid, price, vendorname, statusid, statusname, maxstatus, remarks, materialcharges, materialgst, vouchertype, vendorcall, customercall, multichoicequestion, categorycode, item_detail;
    private float sgst, cgst, payable;

    public timeLineVO(String orderdate, String ordernumber, String orderdes, String status, String statusdes, String vendorid, String price, String vendorname, String maxstatus, String remarks, String materialcharges, String materialgst, String vouchertype, float sgst, float cgst, float payable, String vendorcall, String customercall, String multichoicequestion, String categorycode) {
        this.orderdate = orderdate;
        this.ordernumber = ordernumber;
        this.orderdes = orderdes;
        this.status = status;
        this.statusdes = statusdes;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.maxstatus = maxstatus;
        this.remarks = remarks;
        this.materialcharges = materialcharges.equals("") ? "0" : materialcharges;
        this.materialgst = materialgst.equals("") ? "0" : materialgst;
        this.price = price.equals("") ? "0" : price;
        this.vouchertype = vouchertype;
        this.sgst = sgst;
        this.cgst = cgst;
        this.payable = payable;
        this.vendorcall = vendorcall;
        this.customercall = customercall;
        this.multichoicequestion = multichoicequestion;
        this.categorycode = categorycode;
    }

    public String getOrderDate() {
        return orderdate;
    }

    public void setOrderDate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderNumber() {
        return ordernumber;
    }

    public void setOrderNumber(String ordernumber) {
        this.orderdate = ordernumber;
    }

    public String getOrderDes() {
        return orderdes;
    }

    public void setOrderDes(String orderdes) {
        this.orderdes = orderdes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusdes() {
        return statusdes;
    }

    public void setStatusdes(String statusdes) {
        this.statusdes = statusdes;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String vendorid) {
        this.price = price;
    }

    public String getVendorName() {
        return vendorname;
    }

    public void setVendorName(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getMaxStatus() {
        return maxstatus;
    }

    public void setMaxStatus(String maxstatus) {
        this.maxstatus = maxstatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMaterialCharges() {
        return materialcharges;
    }

    public void setMaterialCharges(String materialcharges) {
        this.materialcharges = materialcharges;
    }

    public String getMaterialGST() {
        return materialgst;
    }

    public void setMaterialGST(String materialgst) {
        this.materialgst = materialgst;
    }

    public String getVouchertype() {
        return vouchertype;
    }

    public void setVouchertype(String vouchertype) {
        this.vouchertype = vouchertype;
    }

    public float getSGST() {
        return sgst;
    }

    public void setSGST(float sgst) {
        this.sgst = sgst;
    }

    public float getCGST() {
        return cgst;
    }

    public void setCGST(float cgst) {
        this.cgst = cgst;
    }

    public float getPayable() {
        return payable;
    }

    public void setPayable(float cgst) {
        this.payable = payable;
    }

    public String getVendorCall() {
        return vendorcall;
    }

    public void setVendorCall(String vendorcall) {
        this.vendorcall = vendorcall;
    }

    public String getCustomerCall() {
        return customercall;
    }

    public void setCustomerCall(String customercall) {
        this.customercall = customercall;
    }

    public String getMultichoicequestion() {
        return multichoicequestion;
    }

    public void setMultichoicequestion(String multichoicequestion) {
        this.multichoicequestion = multichoicequestion;
    }

    public String getCategoryCode() {
        return categorycode;
    }

    public void setCategoryCode(String categorycode) {
        this.customercall = categorycode;
    }

    public String getItem_detail() {
        return item_detail;
    }

    public void setItem_detail(String item_detail) {
        this.item_detail = item_detail;
    }
}
