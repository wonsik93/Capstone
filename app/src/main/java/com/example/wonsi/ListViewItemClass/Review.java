package com.example.wonsi.ListViewItemClass;

/**
 * Created by wonsi on 2017-12-11.
 */

public class Review {
    String userName;
    String body;
    Float rating;

    public Review(String userName, String body, Float rating) {
        this.userName = userName;
        this.body = body;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
