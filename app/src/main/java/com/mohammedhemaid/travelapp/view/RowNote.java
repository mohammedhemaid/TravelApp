package com.mohammedhemaid.travelapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.adapters.RecycleViewAdapter;
import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.ui.MainActivity;
import com.mohammedhemaid.travelapp.ui.MapsActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;

@EView
public class RowNote extends CardView implements RecycleViewAdapter.IBind {

    @ViewById(R.id.mNoteTile_tv)
    TextView mNoteTileTextView;

    Note note;
    public RowNote(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void bind(Object o, int position) {

        note = (Note) o ;
        mNoteTileTextView.setText(note.getTitle());
    }

    @Click(R.id.row_note)
    public void noteClick(){

        ((MainActivity) getContext()).onItemClick(note);
    }
}
