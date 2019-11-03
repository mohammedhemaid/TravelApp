package com.mohammedhemaid.travelapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.adapters.RecycleViewAdapter;
import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.util.UIUtil;
import com.mohammedhemaid.travelapp.viewmodel.NoteViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.mohammedhemaid.travelapp.ui.AddNoteActivity.EXTRA_ID;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.Listener {

    private static final String TAG = "MainActivity";

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final String NOTE_EXTRAS = "note";

    RecycleViewAdapter mAdapter;
    NoteViewModel mNoteViewModel;
    @ViewById(R.id.mNote_rv) RecyclerView mRecyclerView;

    @AfterViews
    public void after() {

        initRecycleView();
        showData();
    }

    private void initRecycleView() {

        mAdapter = new RecycleViewAdapter(this, this);
        mAdapter.setRecycleViewRes(R.layout.row_note);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void showData() {

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNote().observe(this,
                (List<Note> notes) -> mAdapter.setData(notes));
    }


    @Click(R.id.mAddNote_fab)
    public void addNote() {
        AddNoteActivity_.intent(this).startForResult(ADD_NOTE_REQUEST);

    }


    @OnActivityResult(ADD_NOTE_REQUEST)
    public void addNoteOnResult(int resultCode, @OnActivityResult.Extra Note note) {

        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "addNoteOnResult: " + note);
            mNoteViewModel.insert(note);
        }
    }

    @OnActivityResult(EDIT_NOTE_REQUEST)
    public void editNoteOnResult(int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Note note = data.getParcelableExtra(NOTE_EXTRAS);

            int id = data.getIntExtra(EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            note.setId(id);
            mNoteViewModel.update(note);

        }
    }

    @Override
    public void onEditClick(Object o) {
        Note note = (Note) o;
        AddNoteActivity_
                .intent(this)
                .noteIntent(note)
                .noteId(note.getId())
                .startForResult(EDIT_NOTE_REQUEST);
    }

    @Override
    public void onDeleteClick(Object o) {
        mNoteViewModel.delete((Note) o);

    }

    @Override
    public void onError() {

        UIUtil.showLongToast(getString(R.string.some_thing_wrong_happend), this);
    }
}
