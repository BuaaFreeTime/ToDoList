/*
 * Copyright 2019 by BuaaFreeTime
 */

package comp5216.sydney.edu.au.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoItemDao {

    @Query("SELECT * FROM todolist")
    List<ToDoItemDF> listAll();

    @Insert
    void insert(ToDoItemDF toDoItemDF);

    @Update
    public void update(ToDoItemDF toDoItemDF);


    @Delete
    public void delete(ToDoItemDF toDoItemDF);
}
