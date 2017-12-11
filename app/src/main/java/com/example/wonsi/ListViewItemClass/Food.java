package com.example.wonsi.ListViewItemClass;

/**
 * Created by wonsi on 2017-12-09.
 */

public class Food {
    String barcode;
    int foodimage;
    String foodname;
    String review;

    public Food(String barcode, int foodimage, String foodname, String review) {
        this.barcode = barcode;
        this.foodimage = foodimage;
        this.foodname = foodname;
        this.review = review;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getFoodimage() {
        return foodimage;
    }

    public void setFoodimage(int foodimage) {
        this.foodimage = foodimage;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


}

