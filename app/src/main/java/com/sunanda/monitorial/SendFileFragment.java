package com.sunanda.monitorial;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunanda.monitorial.mModel.Spacecraft;
import com.sunanda.monitorial.mModel.Teacher_info;
import com.sunanda.monitorial.mUI.CustomAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFileFragment extends Fragment implements  View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 234 ;
    View view;

  private Button buttonChoose,buttonUpload;
    private ImageView imageView;
    private Uri filePath;
    private TextView fileTextView;
    private CardView cardView;
    private Spinner spinner;
    private String photoUrl;
    private String spinnerText;

    private String teacherId,teacherName;





    private  String displayName = null;


    private StorageReference storageReference;
    private DatabaseReference fDatabaseRoot;
    private DatabaseReference mDatabase;



    public SendFileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_send_file, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
         fileTextView = (TextView)view.findViewById(R.id.fileName);


        imageView = (ImageView)view.findViewById(R.id.imageView);
        buttonChoose = (Button)view.findViewById(R.id.buttonChoose);
        buttonUpload = (Button)view.findViewById(R.id.buttonUpload);

        cardView = (CardView)view.findViewById(R.id.idImage);







//for storing  photourl











        fDatabaseRoot = FirebaseDatabase.getInstance().getReference();

        fDatabaseRoot.child("Group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("group_name").getValue(String.class);
                    areas.add(areaName);
                }

                spinner = (Spinner)view.findViewById(R.id.spinner);




                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);








                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        spinnerText = spinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);




        return view;
    }




    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);


        startActivityForResult(Intent.createChooser(intent,"Select a File"),PICK_IMAGE_REQUEST);
    }




    private  void uploadFile()
    {


        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference riversRef = storageReference.child("Files").child(displayName);



            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog

                            photoUrl = taskSnapshot.getDownloadUrl().toString();


                            //Toast.makeText(getActivity(), spinnerText, Toast.LENGTH_LONG).show();

///////////////////////////////////////////////////////////////////////////////////////////////////



//                            Intent intent = getActivity().getIntent();
//
//                            teacherId = intent.getStringExtra("KEY_send");
//                            teacherName=intent.getStringExtra("NAME_send");



                            Teacher_info ti = Teacher_info.getInstance();
                            teacherId = ti.getId();
                            teacherName = ti.getName();






                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Teacher").child(teacherId).child("Assignment").child(spinnerText);






                            DatabaseReference newPost = mDatabase.push();
                            String key = newPost.getKey();


                            newPost.child("file_name").setValue(displayName);
                            newPost.child("file_link").setValue(photoUrl);


























                            progressDialog.dismiss();








                            //and displaying a success toast
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                           // Toast.makeText(getActivity(), photoUrl, Toast.LENGTH_LONG).show();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }





    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data !=null && data.getData()!=null)
        {


            filePath = data.getData();
            String uriString = filePath.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();

            Bitmap bitmap = null;
            try {
                cardView.setVisibility(View.INVISIBLE);


                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                if(bitmap!=null)
                {
                    cardView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);
                }



                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(filePath, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            fileTextView.setText(displayName);
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    fileTextView.setText(displayName);
                }



            } catch (IOException e) {
                e.printStackTrace();
            }






        }



    }





    @Override
    public void onClick(View view) {


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,null);
//        //intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        startActivityForResult(intent, REQUEST_CODE);




         if(view == buttonChoose)
         {
             showFileChooser();
         }
        else if(view== buttonUpload)
         {
              uploadFile();

         }


    }



}
