package com.example.wonsi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonsi on 2017-11-27.
 */

public class DogTypeListViewAdapter extends BaseAdapter {
    private ArrayList<DogType> originalDogTypes = new ArrayList<>();
    private ArrayList<DogType> copyDogTypes = originalDogTypes;
    Filter filter;

    public DogTypeListViewAdapter(){

    }

    @Override
    public int getCount() {
        return copyDogTypes.size();
    }

    @Override
    public Object getItem(int i) {
        return copyDogTypes.get(i);
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

        DogType newDogType = copyDogTypes.get(i);
        iv_typeimage.setImageDrawable(newDogType.getDrawable());
        tv_typename.setText(newDogType.getTypeName());
        return view;
    }
    public void addItem(DogType dogType){
        originalDogTypes.add(dogType);
    }

    public Filter getFilter(){
        if (filter == null){
            filter = new ListFilter();
        }
        return filter;
    }

    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.values = originalDogTypes;
                results.count = originalDogTypes.size();
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }
    }

}
