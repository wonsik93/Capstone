package com.example.wonsi.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wonsi.ListViewItemClass.DogType;
import com.example.wonsi.R;

import java.util.List;

/**
 * Created by wonsi on 2017-11-27.
 */

public class DogTypeListViewAdapter extends BaseAdapter  {
    private List<DogType> originalDogTypes;
    public DogTypeListViewAdapter(List<DogType> list){
        this.originalDogTypes = list;

    }

    @Override
    public int getCount() {
        return originalDogTypes.size();
    }

    @Override
    public Object getItem(int i) {
        return originalDogTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context myContext = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_pettypelist,viewGroup,false);
        }
        ImageView iv_typeimage = (ImageView) view.findViewById(R.id.Item_LV_image);
        TextView tv_typename = (TextView) view.findViewById(R.id.Item_TV_type);

        DogType newDogType = originalDogTypes.get(i);
        iv_typeimage.setImageDrawable(newDogType.getDrawable());
        tv_typename.setText(newDogType.getTypeName());
        return view;
    }
    public void addItem(DogType dogType){
        originalDogTypes.add(dogType);
    }



}
