package com.aic.khidmanow;

public class detailitemVO {
    private String detailq, detailr, option;
    private int itemfree;

    public detailitemVO() {

    }

    public detailitemVO(String detailq, String detailr, String option, int itemfree) {
        this.detailq = detailq;
        this.detailr = detailr;
        this.option = option;
        this.itemfree = itemfree;
    }

    public String getDetailq() {
        return detailq;
    }

    public void setDetailq(String detailq) {
        this.detailq = detailq;
    }

    public String getDetailr() {
        return detailr;
    }

    public void setDetailr(String detailr) {
        this.detailr = detailr;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getItemFree() {
        return itemfree;
    }

    public void setItemFree(int option) {
        this.itemfree = itemfree;
    }
}
