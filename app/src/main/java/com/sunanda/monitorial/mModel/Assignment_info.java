package com.sunanda.monitorial.mModel;

/**
 * Created by Sunanda1 on 12/1/2017.
 */
public class Assignment_info {

    private  String teacherId,teacherName,groupName;
    private  static  Assignment_info assignment_info = new Assignment_info();

    private  Assignment_info(){}



    public  static  Assignment_info getInstance()
    {    return  assignment_info;       }


    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
