package com.example.wonsi.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wonsi.ListViewItemClass.Food;
import com.example.wonsi.R;

import java.util.List;

/**
 * Created by wonsi on 2017-12-09.
 */

public class FoodListViewAdapter extends BaseAdapter {
    private List<Food> foodList;
    public FoodListViewAdapter(List<Food> list){
        this.foodList = list;

    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodList.get(i);
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
            view = inflater.inflate(R.layout.item_recommendfoodlist,viewGroup,false);
        }
        ImageView iv_foodimage = (ImageView) view.findViewById(R.id.foodItem_IV_image);
        TextView tv_foodname = (TextView) view.findViewById(R.id.foodItem_TV_name);
        TextView tv_review = (TextView) view.findViewById(R.id.foodItem_TV_review);
        Food food = foodList.get(i);

        iv_foodimage.setImageResource(android.R.drawable.ic_dialog_alert);
        tv_foodname.setText(food.getFoodname());
        tv_review.setText(food.getReview());
        return view;
    }
    public void addItem(Food food){
        foodList.add(food);
    }
}
