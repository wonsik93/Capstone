package com.example.wonsi.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wonsi.ListViewItemClass.FoodContain;
import com.example.wonsi.R;

import java.util.List;

/**
 * Created by wonsi on 2017-12-03.
 */

public class FoodContainListViewAdapter extends BaseAdapter {
    private List<FoodContain> foodContainList;
    public FoodContainListViewAdapter(List<FoodContain> list){
        this.foodContainList = list;

    }

    @Override
    public int getCount() {
        return foodContainList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodContainList.get(i);
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
            view = inflater.inflate(R.layout.item_containlist,viewGroup,false);
        }
        ImageView iv_grade = (ImageView) view.findViewById(R.id.ContainItem_IV_image);
        TextView tv_name = (TextView) view.findViewById(R.id.ContainItem_TV_name);
        FoodContain foodContain = foodContainList.get(i);
        if(foodContain.getGrade() == 2) iv_grade.setImageResource(R.drawable.containgood);
        if(foodContain.getGrade() == 1) iv_grade.setImageResource(R.drawable.containnormal);
        if(foodContain.getGrade() == 0) iv_grade.setImageResource(R.drawable.containbad);
        tv_name.setText(foodContain.getNote());
        return view;
    }
    public void addItem(FoodContain foodContain){
        foodContainList.add(foodContain);
    }


}
