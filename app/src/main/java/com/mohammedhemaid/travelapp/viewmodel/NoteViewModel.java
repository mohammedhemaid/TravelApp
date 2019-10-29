package com.mohammedhemaid.travelapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNote;


    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepository = new NoteRepository(application);

        allNote = noteRepository.getAllNote();
    }


    public void insert(Note note) {
        noteRepository.insert(note);
    }


    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAll() {
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNote() {

        return allNote;
    }


}
