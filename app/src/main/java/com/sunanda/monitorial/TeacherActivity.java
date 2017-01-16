package com.sunanda.monitorial;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunanda.monitorial.mModel.Teacher_info;

public class TeacherActivity extends AppCompatActivity {



    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editInstitution;
    private Button btnNext;
    private  boolean isValid = true;
    private Vibrator vib;
    Animation animShake;

    private TextInputLayout inputLayout_name,inputLayout_email,inputLayout_phone,inputLayout_institution;

    private  String name,email,phone,institution;


    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Teacher");




        initializeWidgets();

        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);







        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                submitForm();


                if(isValid==true)

                {


                    name = editName.getText().toString();
                    email = editEmail.getText().toString();
                    phone = editPhone.getText().toString();
                    institution = editInstitution.getText().toString();


                    //Toast.makeText(TeacherActivity.this,name,Toast.LENGTH_SHORT).show();


                    DatabaseReference newPost = mDatabase.push();


                    String key = newPost.getKey();
                    newPost.child("name").setValue(name);
                    newPost.child("email").setValue(email);
                    newPost.child("phone").setValue(phone);
                    newPost.child("institution").setValue(institution);


                    Teacher_info ti = Teacher_info.getInstance();
                    ti.setId(key);
                    ti.setName(name);


                    Intent teacherActivity = new Intent(TeacherActivity.this, NavigationActivity.class);

                    teacherActivity.putExtra("ID", key);
                    teacherActivity.putExtra("OWNER", name);


                    teacherActivity.putExtra("KEY", key);
                    teacherActivity.putExtra("NAME", name);


                    //for send file fragment

                    teacherActivity.putExtra("KEY_send", key);
                    teacherActivity.putExtra("NAME_send", name);


                    startActivity(teacherActivity);


                    //  startActivity(new Intent(TeacherActivity.this,NavigationActivity.class));


                }

            }
        });





    }







    private void initializeWidgets()
    {
        editName= (EditText)findViewById(R.id.editName);
        editEmail= (EditText)findViewById(R.id.editEmail);
        editPhone= (EditText)findViewById(R.id.editPhone);
        editInstitution= (EditText)findViewById(R.id.editInstitution);



        btnNext = (Button)findViewById(R.id.btnNext);


        inputLayout_name =(TextInputLayout)findViewById(R.id.inputLayoutName);
        inputLayout_email = (TextInputLayout)findViewById(R.id.inputLayoutEmail);
        inputLayout_phone = (TextInputLayout)findViewById(R.id.inputLayoutPhone);
        inputLayout_institution = (TextInputLayout)findViewById(R.id.inputLayoutInstitution);




    }






    private void submitForm()
    {



        if(!checkName())
        {

            editName.setAnimation(animShake);
            editName.startAnimation(animShake);
            vib.vibrate(120);
            return ;
        }




        if(!checkEmail())
        {

            editEmail.setAnimation(animShake);
            editEmail.startAnimation(animShake);
            vib.vibrate(120);
            return ;
        }



        if(!checkPhone())
        {

            editPhone.setAnimation(animShake);
            editPhone.startAnimation(animShake);
            vib.vibrate(120);
            return ;
        }


        if(!checkInstitute())
        {

            editInstitution.setAnimation(animShake);
            editInstitution.startAnimation(animShake);
            vib.vibrate(120);
            return ;
        }





        inputLayout_name.setErrorEnabled(false);
        inputLayout_email.setErrorEnabled(false);
        inputLayout_phone.setErrorEnabled(false);
        inputLayout_institution.setErrorEnabled(false);




    }



    private boolean checkName()
    {

        if(editName.getText().toString().isEmpty())
        {
            isValid=false;
            inputLayout_name.setErrorEnabled(true);
            inputLayout_name.setError("Enter Name");
            editName.setError("Name is Required");
            return false;
        }
        else
        {
            isValid = true;
            inputLayout_name.setErrorEnabled(false);
            return true;
        }

    }








    private boolean checkEmail()
    {

        String email = editEmail.getText().toString();

        if(email.isEmpty() || !isValidEmail(email))
        {
            isValid=false;
            inputLayout_email.setErrorEnabled(true);
            inputLayout_email.setError("Enter Email");
            editEmail.setError("Valid Email is Required");
            requestFocus(editEmail);
            return false;
        }
        else
        {
            isValid = true;
            inputLayout_email.setErrorEnabled(false);
            return true;
        }

    }



    private boolean checkPhone()
    {

        if(editPhone.getText().toString().isEmpty())
        {
            isValid=false;
            inputLayout_phone.setErrorEnabled(true);
            inputLayout_phone.setError("Enter Phone Number");
            editPhone.setError("Phone Number is Required");
            requestFocus(editPhone);
            return false;
        }
        else
        {
            isValid = true;
            inputLayout_phone.setErrorEnabled(false);
            return true;
        }

    }

    private boolean checkInstitute()
    {

        if(editInstitution.getText().toString().isEmpty())
        {
            isValid=false;
            inputLayout_institution.setErrorEnabled(true);
            inputLayout_institution.setError("Enter Institution");
            editInstitution.setError("Institution is Required");
            requestFocus(editInstitution);
            return false;
        }
        else
        {
            isValid = true;
            inputLayout_institution.setErrorEnabled(false);
            return true;
        }

    }



    private void requestFocus(View view)
    {
        if(view.requestFocus())
        {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    private static boolean isValidEmail(String email)
    {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }






}
