package com.sunanda.monitorial;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindLocationFragment extends Fragment implements View.OnClickListener{


    Button searchButton;
    EditText editText;
    String roll;


    public FindLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find_location, container, false);

        editText = (EditText) rootView.findViewById(R.id.editRoll);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(this);



        return rootView;
    }


    @Override
    public void onClick(View view) {

        roll = editText.getText().toString();
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString("Roll", roll);
        fragment.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.relativelayout_for_fragment, fragment).commit();
//              String value = getArguments().getString("Roll");  //Retrieve value from other fragment.


    }
}
