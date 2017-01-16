package com.sunanda.monitorial.mUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sunanda.monitorial.R;
import com.sunanda.monitorial.mModel.Assignment;
import com.sunanda.monitorial.mModel.JoinedGroup;

import java.util.List;

/**
 * Created by Sunanda1 on 12/1/2017.
 */
public class AdapterForAssignment extends ArrayAdapter<Assignment> {

    public AdapterForAssignment(Context context, int resource, List<Assignment> objects) {
        super(context, resource, objects);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.model_find_assignment2, parent, false);
        }

        TextView file_name = (TextView) convertView.findViewById(R.id.fileNameTextView);
        TextView file_link = (TextView) convertView.findViewById(R.id.fileLinkTextView);

        Assignment assignment = getItem(position);


        file_name.setVisibility(View.VISIBLE);
        file_link.setVisibility(View.VISIBLE);


        file_name.setText(assignment.getFile_name());

        file_link.setText(assignment.getFile_link());

        return convertView;
    }



}



