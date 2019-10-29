package com.mohammedhemaid.travelapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.model.Note;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static com.mohammedhemaid.travelapp.ui.MainActivity.NOTE_EXTRAS;

@EActivity(R.layout.activity_add_note)
public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";
    public static final String EXTRA_ID = "com.mohammedhemaid.travelapp.ui.EXTRA_ID";

    public static final int ADD_LOCATION = 145;

    @Extra
    Note noteIntent;

    @Extra
    int noteId;

    @ViewById(R.id.title_til)
    TextInputLayout mTitleTextInputLayout;

    @ViewById(R.id.title_ted)
    TextInputEditText mTitleTextInputEditText;

    @ViewById(R.id.location_ted)
    TextInputEditText mLocationTextInputEditText;

    @ViewById(R.id.time_ted)
    TextInputEditText mTimeTextInputEditText;

    @ViewById(R.id.description_ted)
    TextInputEditText mDescriptionTextInputEditText;


    @AfterViews
    public void after() {


        if (noteIntent != null) {

            setTitle("Edit Note");

            mTitleTextInputEditText.setText(noteIntent.getTitle());
            mDescriptionTextInputEditText.setText(noteIntent.getDescription());
            mLocationTextInputEditText.setText(noteIntent.getLocation());
        } else
            setTitle("Add Note");


    }

    @Click({R.id.location_til, R.id.location_ted})
    public void locationClick() {

        MapsActivity_.intent(this)
                .mLatLng(String.valueOf(mLocationTextInputEditText.getText()))
                .startForResult(ADD_LOCATION);

    }


    @OnActivityResult(ADD_LOCATION)
    public void onResult(int resultCode, @OnActivityResult.Extra String mLatLngText) {

        if (resultCode == Activity.RESULT_OK) {

            mLocationTextInputEditText.setText(mLatLngText);
        }
    }


    @Click(R.id.mSave_button)
    public void saveNote() {

        String title = String.valueOf(mTitleTextInputEditText.getText());
        String description = String.valueOf(mDescriptionTextInputEditText.getText());
        String latLng = String.valueOf(mLocationTextInputEditText.getText());
        String time = String.valueOf(mTimeTextInputEditText.getText());

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            mTitleTextInputLayout.setError("Please insert a Title and Description");
            return;
        }
        Intent data = new Intent();

        Note note = new Note(null, title, latLng, time, description);
        data.putExtra(NOTE_EXTRAS, note);


        if (noteIntent != null) {
            data.putExtra(EXTRA_ID, noteIntent.getId());
        }

        setResult(RESULT_OK, data);
        finish();


    }

}
