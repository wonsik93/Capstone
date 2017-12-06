package com.example.wonsi.ListViewItemClass;

import android.graphics.drawable.Drawable;

/**
 * Created by wonsi on 2017-11-26.
 */

public class DogType {
    int TypeID;
    String TypeName;
    Drawable drawable;

    public DogType(int typeID, String typeName, Drawable drawable) {
        this.TypeID = typeID;
        this.TypeName = typeName;
        this.drawable = drawable;
    }

    public int getTypeID() {

        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
