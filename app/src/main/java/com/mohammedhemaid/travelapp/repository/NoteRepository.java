package com.mohammedhemaid.travelapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.roompersistence.NoteDao;
import com.mohammedhemaid.travelapp.roompersistence.NoteDatabase;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> allNote;


    public NoteRepository(Application application) {

        NoteDatabase database = NoteDatabase.getInstance(application);
        mNoteDao = database.noteDao();
        allNote = mNoteDao.getAllNotes();
    }


    public void insert(Note note) {

        new InsertNoteAsyncTask(mNoteDao).execute(note);

    }

    public void update(Note note) {

        new UpdateNoteAsyncTask(mNoteDao).execute(note);

    }

    public void delete(Note note) {

        new DeleteNoteAsyncTask(mNoteDao).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(mNoteDao).execute();

    }

    public LiveData<List<Note>> getAllNote() {
        return allNote;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            noteDao.update(note.getTitle(),note.getLocation(),note.getTime(),note.getDescription());
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.deleteAllNotes();
            return null;
        }
    }
}
