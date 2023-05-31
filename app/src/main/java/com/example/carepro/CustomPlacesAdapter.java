package com.example.carepro;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomPlacesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> placeName;
    private ArrayList<String> ratingText;
    private ArrayList<String> openNowText;

    /**
     * CustomPlacesAdapter Constructor
     * @param context
     * @param placeName arrayList
     * @param ratingText arrayList
     * @param openNowText arrayList
     * */
    public CustomPlacesAdapter(Context context, ArrayList<String> placeName, ArrayList<String> ratingText, ArrayList<String> openNowText) {
        this.context = context;
        this.placeName = placeName;
        this.ratingText = ratingText;
        this.openNowText = openNowText;
    }

    /**
     * override method getCount
     * @return placeName.size
     * */
    @Override
    public int getCount() {
        return placeName.size();
    }

    /**
     * override getItem method
     * @param i
     * @return Object
     * */
    @Override
    public Object getItem(int i) {
        return placeName.get(i);
    }

    /**
     * override getItemId
     * @param i
     * @return long
     * */
    @Override
    public long getItemId(int i) {
        return 3232;
    }

    /**
     * getView override method
     * @param i
     * @param view
     * @param viewGroup
     * @return view
     * */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) view = view.inflate(context, R.layout.health_centers_result_view, null);

        TextView placeTextView = (TextView) view.findViewById(R.id.placeNameTextView);
        TextView ratingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        TextView openNowTextView = (TextView) view.findViewById(R.id.openingTime);

        placeTextView.setText(placeName.get(i));
        ratingTextView.setText(ratingText.get(i));
        openNowTextView.setText("Open now: " + openNowText.get(i));

        return view;
    }
}