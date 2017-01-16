package com.sunanda.monitorial.mModel;

import java.util.jar.Attributes;

/**
 * Created by Sunanda1 on 12/1/2017.
 */
public class Student_info {

    private String id,name,roll=null;
    private static  Student_info student_info = new Student_info();

    private   Student_info() {}



    public  static  Student_info getInstance()
    {
        return student_info;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
