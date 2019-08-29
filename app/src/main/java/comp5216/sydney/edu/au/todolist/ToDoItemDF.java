package comp5216.sydney.edu.au.todolist;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "todolist", indices = {@Index("toDoItemID")})
public class ToDoItemDF {
    // A database form class
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemTitle")
    private String toDoItemTitle;

    @ColumnInfo(name = "toDoItemText")
    private String toDoItemText;

    @ColumnInfo(name = "toDoItemDate")
    private String toDoItemDate;

    public ToDoItemDF(String toDoItemTitle, String toDoItemText, String toDoItemDate){
        this.toDoItemTitle = toDoItemTitle;
        this.toDoItemText = toDoItemText;
        this.toDoItemDate = toDoItemDate;
    }

    public ToDoItemDF(ToDoItem item) {
        this.toDoItemTitle = item.getTitle();
        this.toDoItemText = item.getText();
        this.toDoItemDate = item.getDateSpring();
    }

    public void setToDoItemDate(String toDoItemDate) {
        this.toDoItemDate = toDoItemDate;
    }

    public String getToDoItemDate() {
        return toDoItemDate;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemTitle(String toDoItemTitle) {
        this.toDoItemTitle = toDoItemTitle;
    }

    public String getToDoItemTitle() {
        return toDoItemTitle;
    }

    public void setToDoItemText(String toDoItemText) {
        this.toDoItemText = toDoItemText;
    }

    public String getToDoItemText() {
        return toDoItemText;
    }


}
