package com.sunanda.monitorial.mModel;

/**
 * Created by Sunanda1 on 12/1/2017.
 */
public class Teacher_info {
    private String id ,name;

    private static  Teacher_info  teacher_info= new Teacher_info();

    private  Teacher_info(){}



    public static Teacher_info getInstance()
    {
        return  teacher_info;
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


}
