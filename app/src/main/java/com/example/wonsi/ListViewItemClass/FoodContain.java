package com.example.wonsi.ListViewItemClass;

/**
 * Created by wonsi on 2017-12-03.
 */

public class FoodContain {
    int IgOrder;
    int IngrdtID;
    String note;
    String information;
    int grade;

    public FoodContain(int igOrder, int ingrdtID, String note, String information, int grade) {
        IgOrder = igOrder;
        IngrdtID = ingrdtID;
        this.note = note;
        this.information = information;
        this.grade = grade;
    }

    public int getIgOrder() {
        return IgOrder;
    }

    public void setIgOrder(int igOrder) {
        IgOrder = igOrder;
    }

    public int getIngrdtID() {
        return IngrdtID;
    }

    public void setIngrdtID(int ingrdtID) {
        IngrdtID = ingrdtID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
