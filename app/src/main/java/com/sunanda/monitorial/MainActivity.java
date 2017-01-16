package com.sunanda.monitorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button btnTeacher , btnStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Animation animTranslate = AnimationUtils.loadAnimation(this,R.anim.anim_translate);




        btnTeacher=(Button)findViewById(R.id.btnTeacher);
        btnStudent=(Button)findViewById(R.id.btnStudent);





//        String sharedPrefId     = "MyApp";
//        final SharedPreferences prefs = getSharedPreferences(sharedPrefId, 0);
//
//        boolean isLogged = prefs.getBoolean("isLogged",false);
//        if(isLogged)
//        {
//
//            //mainActivity
//        }
//
//        else
//        {
//            //navigation Activity
//
//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putBoolean("isLogged",true).commit();
//
//        }









        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent teacherActivity = new Intent(MainActivity.this,TeacherActivity.class);
//                String str = "Teacher";
//                teacherActivity.putExtra("post", str);
//                startActivity(teacherActivity);

              //v.startAnimation(animTranslate);







                startActivity(new Intent(MainActivity.this,TeacherActivity.class));













            }
        });



        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // v.startAnimation(animTranslate);

                startActivity(new Intent(MainActivity.this,StudentActivity.class));





            }
        });



    }
}
