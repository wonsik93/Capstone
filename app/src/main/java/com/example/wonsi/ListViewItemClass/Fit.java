package com.example.wonsi.ListViewItemClass;

/**
 * Created by wonsi on 2017-12-10.
 */

public class Fit {
    int fitImage;
    String fitText;

    public Fit(int fitImage, String fitText) {
        this.fitImage = fitImage;
        this.fitText = fitText;
    }

    public int getFitImage() {

        return fitImage;
    }

    public void setFitImage(int fitImage) {
        this.fitImage = fitImage;
    }

    public String getFitText() {
        return fitText;
    }

    public void setFitText(String fitText) {
        this.fitText = fitText;
    }
}
