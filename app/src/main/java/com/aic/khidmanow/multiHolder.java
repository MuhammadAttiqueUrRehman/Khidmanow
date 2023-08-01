package com.aic.khidmanow;

public class multiHolder {
    private String title;
    private String name;
    private int qty;
    private Double amount,total;

    public multiHolder(){

    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public int getQty(){
        return qty;
    }

    public void setQty(int qty){
        this.qty=qty;
    }

    public Double getAmount(){
        return amount;
    }

    public void setAmount(Double amount){
        this.amount=amount;
    }

    public Double getTotal(){
        return total;
    }

    public void setTotal(Double total){
        this.total=total;
    }
}
