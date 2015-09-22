package com.hhua.android.simpletodo.models;

/**
 * Created by ahua on 9/21/15.
 */
public class Task {
    public long id;
    public String title;
    //public Date due;

    public Task(){

    }

    public Task(String title){
        this.title = title;
    }

    public Task(String title, long id){
        this.id = id;
        this.title = title;
    }

    public String toString(){
        return this.title;
    }
}
