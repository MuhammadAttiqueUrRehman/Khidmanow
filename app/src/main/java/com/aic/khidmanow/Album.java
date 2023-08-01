package com.aic.khidmanow;

/**
 * Created by Administrator on 19-06-2017.
 */

public class Album {
    private String title;
    private int numOfItems;
    private String subcategory;
    private String url;

    public Album(){

    }

    public Album(String title,int numOfItems,String subcategory,String url){
        this.title=title;
        this.numOfItems=numOfItems;
        this.subcategory=subcategory;
        this.url=url;
            }

            public String getTitle(){
                return title;
            }

            public void setTitle(String title){
                this.title=title;
            }

    public String getSubcategory(){
        return subcategory;
    }

    public void setSubcategory(String subcategory){
        this.subcategory=subcategory;
    }

    public int getNumOfItems(){
        return numOfItems;
    }

    public void setNumOfItems(int numOfItems){
        this.numOfItems=numOfItems;
    }



    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.title=url;
    }
}
