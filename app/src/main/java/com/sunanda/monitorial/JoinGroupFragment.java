package com.sunanda.monitorial;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sunanda.monitorial.mModel.Spacecraft;
import com.sunanda.monitorial.mModel.Student_info;
import com.sunanda.monitorial.mUI.CustomAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class JoinGroupFragment extends Fragment {


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFirebaseReference;
    ChildEventListener mChildEventListener;
    private ListView mMessageListView;
    private CustomAdapter mMessageAdapter;

    private String selectedItem;
    private String memberIdWhoJoined;
    private String memberNameWhoJoined;
    private String memberRollWhoJoined = null;
    private String groupIdWhereJoined;
    private String groupOwnerId;
    private String groupOwnerName;
    private String grp_name;



    private DatabaseReference mDatabase;
    private DatabaseReference databaseForStudent;
    private  Intent intent;



    public JoinGroupFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_join_group, container, false);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseReference = mFirebaseDatabase.getReference().child("Group");






















//
//         intent = getActivity().getIntent();
//        memberIdWhoJoined = intent.getStringExtra("KEY11");
//        memberNameWhoJoined = intent.getStringExtra("NAME11");
//        memberRollWhoJoined = intent.getStringExtra("ROLL11");

        Student_info sd = Student_info.getInstance();
        memberIdWhoJoined = sd.getId();
        memberNameWhoJoined = sd.getName();
        memberRollWhoJoined = sd.getRoll();
















        databaseForStudent =  FirebaseDatabase.getInstance().getReference().child("Student").child(memberIdWhoJoined).child("Joined_Group");


        mMessageListView = (ListView) rootView.findViewById(R.id.messageListView);

        List<Spacecraft> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new CustomAdapter(getActivity(), R.layout.model, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Spacecraft friendlyMessage = dataSnapshot.getValue(Spacecraft.class);
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
        mFirebaseReference.addChildEventListener(mChildEventListener);
      




        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Object object = mMessageAdapter.getItem(position);
                Spacecraft spacecraft = (Spacecraft)object;

                String id_value = spacecraft.getId();

                grp_name = spacecraft.getGroup_name();
                groupOwnerId = spacecraft.getId();
                groupOwnerName = spacecraft.getOwner();









                mDatabase =   FirebaseDatabase.getInstance().getReference().child("Teacher").child(groupOwnerId).child("Owner_Group").child(grp_name);



                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("JoinGroup Alart");
                builder.setMessage("Are you sure to join the group ?");
                builder.setCancelable(false);


                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//give a group name;



                        DatabaseReference newPost = mDatabase.push();

                        newPost.child("owner").setValue(groupOwnerName);
                        newPost.child("member_id").setValue(memberIdWhoJoined);
                        newPost.child("member_name").setValue(memberNameWhoJoined);
                        newPost.child("member_roll").setValue(memberRollWhoJoined);










                        DatabaseReference Post = databaseForStudent.push();

                        Post.child("group_name").setValue(grp_name);
                        Post.child("group_owner").setValue(groupOwnerName);
                        Post.child("group_owner_id").setValue(groupOwnerId);








                        Intent joinGroup = new Intent(getActivity(),NavigationActivity.class);

                        joinGroup.putExtra("member_id",memberIdWhoJoined);
                       // joinGroup.putExtra("NAME",name);






                        startActivity(joinGroup);




                        //startActivity(new Intent(getActivity(),NavigationActivity.class));

                    }
                }
                );



                builder.setNegativeButton("No",null);


               // AlertDialog ad = new AlertDialog.Builder(getActivity()).create();

                AlertDialog alert = builder.create();
                alert.show();













                //Toast.makeText(getActivity(),name,Toast.LENGTH_LONG).show();
            }
        });





        return rootView;
    }


}
