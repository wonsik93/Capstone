package com.example.wonsi.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.wonsi.ListViewItemClass.Fit;
import com.example.wonsi.ListViewItemClass.Review;
import com.example.wonsi.R;

import java.util.List;

/**
 * Created by wonsi on 2017-12-11.
 */

public class ReviewListViewAdapter extends BaseAdapter {
    private List<Review> reviewList;
    public ReviewListViewAdapter(List<Review> list){
        this.reviewList = list;

    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return reviewList.get(i);
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
        TextView tv_username = (TextView) view.findViewById(R.id.reviewItem_TV_username);
        TextView tv_body = (TextView) view.findViewById(R.id.reviewItem_TV_Body);
        RatingBar rb_star = (RatingBar) view.findViewById(R.id.reviewItem_Rating_star);
        Review review= reviewList.get(i);
        tv_username.setText(review.getUserName());
        tv_body.setText(review.getBody());
        rb_star.setRating(review.getRating());
        return view;
    }
    public void addItem(Review review){
        reviewList.add(review);
    }

}
