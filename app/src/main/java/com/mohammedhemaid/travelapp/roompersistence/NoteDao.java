package com.mohammedhemaid.travelapp.roompersistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mohammedhemaid.travelapp.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("UPDATE note_table SET title=:title ,location=:location,time=:time , description =:description")
    void update(String title,
                String location,
                String time,
                String description);

    @Delete
    void delete(Note note);

    @Query("DELETE  FROM  note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();
}
