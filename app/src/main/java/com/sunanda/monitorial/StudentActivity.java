package com.sunanda.monitorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunanda.monitorial.mModel.Student_info;





public class StudentActivity extends AppCompatActivity {


    private EditText editName, editRoll, editEmail,editPhone,editInstitution;
    private Button btnNext;
     private  boolean isValid = true;

    private Vibrator vib;
    Animation animShake;
    private  String name, roll, email,phone,institution;
    private DatabaseReference mDatabase;
    private TextInputLayout inputLayout_name,inputLayout_roll,inputLayout_email,inputLayout_phone,inputLayout_institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Student");





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
                     roll = editRoll.getText().toString();
                     email = editEmail.getText().toString();
                     phone = editPhone.getText().toString();
                    institution = editInstitution.getText().toString();


                        String sharedPrefId     = "MyAppPreference";
                        final SharedPreferences prefs = getSharedPreferences(sharedPrefId, 0);

                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("name", name);
                        editor.putString("roll", roll);
                        editor.commit();

                     DatabaseReference newPost = mDatabase.push();
                    String key = newPost.getKey();

                     newPost.child("name").setValue(name);
                     newPost.child("roll").setValue(roll);
                     newPost.child("email").setValue(email);
                     newPost.child("institution").setValue(institution);


                    Student_info student_info = Student_info.getInstance();

                    student_info.setId(key);
                    student_info.setName(name);
                    student_info.setRoll(roll);





                    Intent studentActivity = new Intent(StudentActivity.this,NavigationActivity.class);

                    studentActivity.putExtra("KEY",key);
                    studentActivity.putExtra("NAME",name);
                    studentActivity.putExtra("ROLL",roll);



//for join group fragment

                    studentActivity.putExtra("KEY11",key);
                    studentActivity.putExtra("NAME11",name);
                    studentActivity.putExtra("ROLL11",roll);




                    startActivity(studentActivity);











                   // startActivity(new Intent(StudentActivity.this,NavigationActivity.class));



                    }

                }
            });









    }

  private void initializeWidgets()
  {
      editName= (EditText)findViewById(R.id.tName);
      editRoll = (EditText)findViewById(R.id.tRoll);
      editEmail= (EditText)findViewById(R.id.tEmail);
      editPhone= (EditText)findViewById(R.id.tPhone);
      editInstitution= (EditText)findViewById(R.id.tInstitution);


      inputLayout_name =(TextInputLayout)findViewById(R.id.inputLayoutName);
      inputLayout_roll = (TextInputLayout)findViewById(R.id.inputLayoutRoll);
      inputLayout_email = (TextInputLayout)findViewById(R.id.inputLayoutEmail);
      inputLayout_phone = (TextInputLayout)findViewById(R.id.inputLayoutPhone);
      inputLayout_institution = (TextInputLayout)findViewById(R.id.inputLayoutInstitution);





      btnNext = (Button)findViewById(R.id.tNext);


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

        if(!checkRoll())
        {

            editRoll.setAnimation(animShake);
            editRoll.startAnimation(animShake);
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
        inputLayout_roll.setErrorEnabled(false);
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




    private boolean checkRoll()
    {

        if(editRoll.getText().toString().isEmpty())
        {
            isValid=false;
            inputLayout_roll.setErrorEnabled(true);
            inputLayout_roll.setError("Enter Roll");
            editRoll.setError("Roll is Required");
            requestFocus(editRoll);
            return false;
        }
        else
        {
            isValid = true;
            inputLayout_roll.setErrorEnabled(false);
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
