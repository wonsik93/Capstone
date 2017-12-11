package com.example.wonsi.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wonsi.ListViewItemClass.Fit;
import com.example.wonsi.ListViewItemClass.FoodContain;
import com.example.wonsi.R;

import java.util.List;

/**
 * Created by wonsi on 2017-12-10.
 */

public class FitListViewAdapter extends BaseAdapter {
    private List<Fit> fitList;
    public FitListViewAdapter(List<Fit> list){
        this.fitList = list;

    }

    @Override
    public int getCount() {
        return fitList.size();
    }

    @Override
    public Object getItem(int i) {
        return fitList.get(i);
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
            view = inflater.inflate(R.layout.item_fitablelist,viewGroup,false);
        }
        ImageView iv_grade = (ImageView) view.findViewById(R.id.FitItem_IV_image);
        TextView tv_name = (TextView) view.findViewById(R.id.FitItem_TV_name);
        Fit fit = fitList.get(i);
        if(fit.getFitImage() == 1) iv_grade.setImageResource(R.drawable.containgood);
        if(fit.getFitImage() == 0) iv_grade.setImageResource(R.drawable.containbad);
        tv_name.setText(fit.getFitText());
        return view;
    }
    public void addItem(Fit fit){
        fitList.add(fit);
    }

}
