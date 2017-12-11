package com.example.wonsi.ListViewItemClass;

import android.content.Intent;

import com.example.wonsi.MainActivity;
import com.example.wonsi.ShowPetDataActivity;

/**
 * Created by wonsi on 2017-12-09.
 */

public class PetData {
    String petName;
    String breed;
    String petAge;
    String petWeight;
    String spicies;
    String typeid;
    String typename;
    String pregnant;
    String stoolsmell;
    String teeth;
    String diet;
    String image;

    public PetData(String petName, String breed, String petAge, String petWeight, String spicies, String typeid, String typename, String pregnant, String stoolsmell, String teeth, String diet, String image) {
        this.petName = petName;
        this.breed = breed;
        this.petAge = petAge;
        this.petWeight = petWeight;
        this.spicies = spicies;
        this.typeid = typeid;
        this.typename = typename;
        this.pregnant = pregnant;
        this.stoolsmell = stoolsmell;
        this.teeth = teeth;
        this.diet = diet;
        this.image = image;
    }

    public String getPetName() {

        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(String petWeight) {
        this.petWeight = petWeight;
    }

    public String getSpicies() {
        return spicies;
    }

    public void setSpicies(String spicies) {
        this.spicies = spicies;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPregnant() {
        return pregnant;
    }

    public void setPregnant(String pregnant) {
        this.pregnant = pregnant;
    }

    public String getStoolsmell() {
        return stoolsmell;
    }

    public void setStoolsmell(String stoolsmell) {
        this.stoolsmell = stoolsmell;
    }

    public String getTeeth() {
        return teeth;
    }

    public void setTeeth(String teeth) {
        this.teeth = teeth;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
