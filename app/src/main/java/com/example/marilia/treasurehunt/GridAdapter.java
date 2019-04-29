package com.example.marilia.treasurehunt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {
    private Context mContext;

    // references to images
    private Integer[] imageIds = {
            R.drawable.hiking,
            R.drawable.writing,
            R.drawable.map,
            R.drawable.gears
    };

    public GridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imageIds.length;
    }

    public Object getItem(int position) {
        return imageIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 360));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        }else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds[position]);
        return imageView;
    }


}
