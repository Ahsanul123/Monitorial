package com.sunanda.monitorial;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sunanda.monitorial.mModel.Assignment_info;
import com.sunanda.monitorial.mModel.JoinedGroup;
import com.sunanda.monitorial.mModel.Spacecraft;
import com.sunanda.monitorial.mModel.Student_info;
import com.sunanda.monitorial.mUI.AdapterForJoinerGroup;
import com.sunanda.monitorial.mUI.CustomAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindAssignmentFragment extends Fragment {


    private View view;
    private DatabaseReference mDatabase;
    private String StudentId;


    private ListView assignListView;
    private AdapterForJoinerGroup mMessageAdapter;
    ChildEventListener mChildEventListener;



    final List<String> group_owner_id_list = new ArrayList<String>();






    public FindAssignmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_find_assignment, container, false);



//        Intent intent = getActivity().getIntent();
//        StudentId = intent.getStringExtra("member_id");

        Student_info sd = Student_info.getInstance();
        StudentId = sd.getId();





        mDatabase = FirebaseDatabase.getInstance().getReference().child("Student").child(StudentId).child("Joined_Group");

        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {






//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.child("group_owner_id").getValue(String.class);
//                    group_owner_id_list.add(areaName);
//                }




/////////////////////////////////////////////////////////////////////////////////////////////////////////

                assignListView= (ListView)view.findViewById(R.id.assignmentListView);

                List<JoinedGroup> friendlyMessages = new ArrayList<>();

                mMessageAdapter = new AdapterForJoinerGroup(getActivity(), R.layout.model_find_assignment, friendlyMessages);
                assignListView.setAdapter(mMessageAdapter);










                mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        JoinedGroup friendlyMessage = dataSnapshot.getValue(JoinedGroup.class);
                        mMessageAdapter.add(friendlyMessage);
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                mDatabase.addChildEventListener(mChildEventListener);








                assignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Object object = mMessageAdapter.getItem(position);
                        JoinedGroup joinedGroup = (JoinedGroup)object;

                        String key  = joinedGroup.getGroup_owner_id();
                        String grp_name = joinedGroup.getGroup_name();
                        String teacher_name = joinedGroup.getGroup_owner();


                        Assignment_info a = Assignment_info.getInstance();
                        a.setTeacherId(key);
                        a.setTeacherName(teacher_name);
                        a.setGroupName(grp_name);


                        Intent intent = new Intent(getActivity(),FindAssignment2.class);
                        startActivity(intent);



                    }
                });














































            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        }
        );






        return view;
    }

}
