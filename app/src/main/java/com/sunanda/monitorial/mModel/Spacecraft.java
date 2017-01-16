package com.sunanda.monitorial.mModel;

/**
 * Created by Sunanda1 on 23/12/2016.
 */
public class Spacecraft {
    private String group_name , id, owner;


    public Spacecraft(){}


    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
