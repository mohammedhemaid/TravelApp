package com.mohammedhemaid.travelapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.common.PhotoTakerManager;
import com.mohammedhemaid.travelapp.model.Note;
import com.mohammedhemaid.travelapp.util.PermissionUtil;
import com.mohammedhemaid.travelapp.util.UIUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static com.mohammedhemaid.travelapp.ui.MainActivity.NOTE_EXTRAS;

@EActivity(R.layout.activity_add_note)
public class AddNoteActivity extends AppCompatActivity implements PhotoTakerManager.Listener {

    private static final String TAG = "AddNoteActivity";
    public static final String EXTRA_ID = "com.mohammedhemaid.travelapp.ui.EXTRA_ID";
    private static final int CAMERA_CODE = 2;
    public static final int ADD_LOCATION = 145;
    private PhotoTakerManager photoTakerManager;
    private MaterialDialog progressDialog;


    @Extra Note noteIntent;
    @Extra int noteId;

    @ViewById(R.id.title_til) TextInputLayout mTitleTextInputLayout;
    @ViewById(R.id.title_ted) TextInputEditText mTitleTextInputEditText;
    @ViewById(R.id.location_ted) TextInputEditText mLocationTextInputEditText;
    @ViewById(R.id.time_ted) TextInputEditText mTimeTextInputEditText;
    @ViewById(R.id.description_ted) TextInputEditText mDescriptionTextInputEditText;
    @ViewById(R.id.mCamera_imageButton) ImageButton mCameraImageButton;
    @ViewById(R.id.taken_image_sdv) SimpleDraweeView mTakenImageSimpleDraweeView;

    @AfterViews
    public void after() {

        //This is test
        photoTakerManager = new PhotoTakerManager(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (noteIntent != null) {

            setTitle("Edit Note");

            mTitleTextInputEditText.setText(noteIntent.getTitle());
            mDescriptionTextInputEditText.setText(noteIntent.getDescription());
            mLocationTextInputEditText.setText(noteIntent.getLocation());
            mTakenImageSimpleDraweeView.setImageURI(noteIntent.getImage());
            mCameraImageButton.setVisibility(View.GONE);
        } else
            setTitle("Add Note");

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.processing_image)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    @Click({R.id.location_til, R.id.location_ted})
    public void locationClick() {

        MapsActivity_.intent(this)
                .mLatLng(String.valueOf(mLocationTextInputEditText.getText()))
                .startForResult(ADD_LOCATION);

    }

    @Click(R.id.mCamera_imageButton)
    public void takePicture() {
        maybeStartCameraPage();

    }

    private void maybeStartCameraPage() {
        if (PermissionUtil.hasPermissions(this, Manifest.permission.CAMERA)) {
            Intent takePhotoIntent = photoTakerManager.getPhotoTakingIntent(this);
            if (takePhotoIntent == null) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                startActivityForResult(takePhotoIntent, CAMERA_CODE);
            }
        } else {
            PermissionUtil.requestPermission(this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    }


    @OnActivityResult(ADD_LOCATION)
    public void onLocationResult(int resultCode, @OnActivityResult.Extra String mLatLngText) {

        if (resultCode == Activity.RESULT_OK) {

            mLocationTextInputEditText.setText(mLatLngText);
        }
    }

    @OnActivityResult(CAMERA_CODE)
    public void onCameraResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            progressDialog.show();
            photoTakerManager.processTakenPhoto(this);
        } else if (resultCode == RESULT_CANCELED) {
            photoTakerManager.deleteLastTakenPhoto();
        }
    }


    @Click(R.id.mSave_button)
    public void saveNote() {

        String title = String.valueOf(mTitleTextInputEditText.getText()).trim();
        String description = String.valueOf(mDescriptionTextInputEditText.getText()).trim();
        String latLng = String.valueOf(mLocationTextInputEditText.getText()).trim();
        String time = String.valueOf(mTimeTextInputEditText.getText()).trim();

        if (title.trim().isEmpty()) {
            mTitleTextInputLayout.setError(getString(R.string.please_insert_a_title));
            return;
        }

        Uri photoUri = photoTakerManager.getCurrentPhotoUri();
        if (photoUri == null && mTakenImageSimpleDraweeView.getDrawable() == null) {
            UIUtil.showLongToast(R.string.no_image_attached, this);
            return;
        }

        Note note = new Note(String.valueOf(photoUri),title, latLng, time, description);

        Intent data = new Intent();
        data.putExtra(NOTE_EXTRAS, note);

        if (noteIntent != null) {
            data.putExtra(EXTRA_ID, noteIntent.getId());
        }
        setResult(RESULT_OK, data);

        finish();


    }

    @Override
    public void onTakePhotoFailure() {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            UIUtil.showLongToast(R.string.take_photo_with_camera_failed, this);
        });
    }

    @Override
    public void onTakePhotoSuccess(Bitmap bitmap) {

        runOnUiThread(() -> {
            mCameraImageButton.setVisibility(View.GONE);
            mTakenImageSimpleDraweeView.setImageBitmap(bitmap);
            mTakenImageSimpleDraweeView.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
