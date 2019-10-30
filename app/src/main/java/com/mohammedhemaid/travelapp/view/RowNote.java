package com.mohammedhemaid.travelapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.adapters.RecycleViewAdapter;
import com.mohammedhemaid.travelapp.common.PhotoViewerActivity_;
import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.adapters.RecycleViewAdapter.Listener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;

@EView
public class RowNote extends CardView implements RecycleViewAdapter.IBind, PopupMenu.OnMenuItemClickListener {


    Listener mListener;

    @ViewById(R.id.mNoteTile_tv)
    TextView mNoteTileTextView;
    @ViewById(R.id.row_note_image_sdv)
    SimpleDraweeView mNoteImageSimpleDraweeView;

    private Note note;
    private int position;

    public RowNote(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void bind(Listener mListener, Object o, int position) {

        this.mListener = mListener;
        note = (Note) o;
        this.position = position;

        mNoteTileTextView.setText(note.getTitle());
        mNoteImageSimpleDraweeView.setImageURI(note.getImage());
    }

    @Click(R.id.context_menu_imb)
    public void noteClick(View clickedView) {

        if (position != RecyclerView.NO_POSITION) {

            PopupMenu popupMenu = new PopupMenu(getContext(), clickedView);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.edit_delete_menu);
            popupMenu.show();
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (position != RecyclerView.NO_POSITION) {


            switch (item.getItemId()) {

                case R.id.edit_Note_button:
                    mListener.onEditClick(note);
                    return true;

                case R.id.delete_Note_button:
                    mListener.onDeleteClick(note);
                    return true;

                default:
                    mListener.onError();
            }
        }

        return false;

    }

    @Click(R.id.row_note_image_sdv)
    public void ImageClick() {

        PhotoViewerActivity_.intent(getContext()).imageUrl(note.getImage()).start();
    }

}
