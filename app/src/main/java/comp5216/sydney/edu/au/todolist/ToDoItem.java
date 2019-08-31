/*
 * Copyright 2019 by BuaaFreeTime
 */

package comp5216.sydney.edu.au.todolist;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ToDoItem {
    // A item class

    // Define variables
    String dateSpring; // date in String form
    Date date;         // date in Date form
    String text;       // Item's text part
    String title;      // Item's title part
    int id;           // Item's id

    public ToDoItem(int id, String title, String addText, Date addTime) {
        this.id = id;
        this.title = title;
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        this.dateSpring = ft.format(addTime);
        this.date = addTime;
        this.text = addText;
    }

    public ToDoItem(String title, String addText, Date addTime) {
        this.title = title;
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        this.dateSpring = ft.format(addTime);
        this.date = addTime;
        this.text = addText;
    }

    public ToDoItem(int id, String title, String addText, String addTime) {
        this.id = id;
        this.title = title;
        this.dateSpring = addTime;
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            this.date = ft.parse(addTime);
        } catch (ParseException e) {
            Log.e("ToDoItem", "DatatoSpring error");
        }
        this.text = addText;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateSpring() {
        return dateSpring;
    }

    @Override
    public String toString() {
        return(title+"\r\n" + dateSpring);
    }

}
