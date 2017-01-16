package com.sunanda.monitorial.mUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunanda.monitorial.R;
import com.sunanda.monitorial.mModel.Spacecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunanda1 on 23/12/2016.
 */
public class CustomAdapter extends ArrayAdapter<Spacecraft>{
    public CustomAdapter(Context context, int resource, List<Spacecraft> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.model, parent, false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        Spacecraft message = getItem(position);


        messageTextView.setVisibility(View.VISIBLE);

        messageTextView.setText(message.getGroup_name());

        authorTextView.setText(message.getOwner());

        return convertView;
    }
}