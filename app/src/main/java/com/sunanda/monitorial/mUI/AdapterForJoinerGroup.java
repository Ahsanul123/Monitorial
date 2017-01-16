package com.sunanda.monitorial.mUI;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sunanda.monitorial.R;
import com.sunanda.monitorial.mModel.JoinedGroup;
import com.sunanda.monitorial.mModel.Spacecraft;

import java.util.List;

/**
 * Created by Sunanda1 on 5/1/2017.
 */
public class AdapterForJoinerGroup extends ArrayAdapter<JoinedGroup> {

    public AdapterForJoinerGroup(Context context, int resource, List<JoinedGroup> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.model_find_assignment, parent, false);
        }

        TextView GroupName = (TextView) convertView.findViewById(R.id.groupNameTextView);
        TextView GroupOwner = (TextView) convertView.findViewById(R.id.ownerTextView);

        JoinedGroup join = getItem(position);


        GroupName.setVisibility(View.VISIBLE);
        GroupOwner.setVisibility(View.VISIBLE);


        GroupName.setText(join.getGroup_name());

        GroupOwner.setText(join.getGroup_owner());

        return convertView;
    }



}
