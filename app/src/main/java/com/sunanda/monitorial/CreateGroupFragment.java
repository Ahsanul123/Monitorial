package com.sunanda.monitorial;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunanda.monitorial.mModel.Teacher_info;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends Fragment implements  View.OnClickListener {


    Button btnAdd;
    EditText create;
    String id,owner;
    View view;
    String group_name;
    private DatabaseReference mDatabase;

    public CreateGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_create_group, container, false);

        btnAdd = (Button)view.findViewById(R.id.btnAdd);
        create = (EditText)view.findViewById(R.id.create);



        btnAdd.setOnClickListener(this);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");




        return view;
    }

    @Override
    public void onClick(View v) {







        DatabaseReference newPost = mDatabase.push();

        group_name = create.getText().toString();




//        Intent intent = getActivity().getIntent();
//        id = intent.getStringExtra("ID");
//        owner=intent.getStringExtra("OWNER");


        Teacher_info ti = Teacher_info.getInstance();
        id = ti.getId();
        owner = ti.getName();





        newPost.child("id").setValue(id);
        newPost.child("owner").setValue(owner);
        newPost.child("group_name").setValue(group_name);







        Intent in = new Intent(getActivity(),NavigationActivity.class);
        startActivity(in);




    }
}
