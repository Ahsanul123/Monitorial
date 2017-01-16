package com.sunanda.monitorial;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunanda.monitorial.mModel.Assignment;
import com.sunanda.monitorial.mModel.Assignment_info;
import com.sunanda.monitorial.mModel.JoinedGroup;
import com.sunanda.monitorial.mModel.Spacecraft;
import com.sunanda.monitorial.mUI.AdapterForAssignment;
import com.sunanda.monitorial.mUI.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class FindAssignment2 extends AppCompatActivity {


    private ListView assignment2;
    private DatabaseReference mDatabase;
    private AdapterForAssignment mMessageAdapter;
    ChildEventListener mChildEventListener;


    private  String teacher_ID,teacher_Name,Group_name;
    DownloadManager downloadManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_assignment2);


        assignment2 = (ListView)findViewById(R.id.nextAssignmentListView);


        Assignment_info in = Assignment_info.getInstance();
        teacher_ID = in.getTeacherId();
        teacher_Name = in.getTeacherName();
        Group_name = in.getGroupName();








        mDatabase = FirebaseDatabase.getInstance().getReference().child("Teacher").child(teacher_ID).child("Assignment").child(Group_name);





        List<Assignment> info = new ArrayList<>();
        mMessageAdapter = new AdapterForAssignment(this, R.layout.model_find_assignment2, info);
        assignment2.setAdapter(mMessageAdapter);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assignment assignment = dataSnapshot.getValue(Assignment.class);
                mMessageAdapter.add(assignment);
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







        assignment2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Object object = mMessageAdapter.getItem(position);
                Assignment assignment  = (Assignment)object;


                String link = assignment.getFile_link();


                downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(link);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadManager.enqueue(request);

            }
        });



















    }
}
