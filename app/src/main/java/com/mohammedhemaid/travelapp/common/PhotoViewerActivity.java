package com.mohammedhemaid.travelapp.common;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.telephony.mbms.FileInfo;
import android.view.MenuItem;
import android.view.View;

import com.mohammedhemaid.travelapp.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;


@EActivity(R.layout.activity_photo_viewer)
public class PhotoViewerActivity extends AppCompatActivity {


    @ViewById(R.id.image_SimpleDraweeView)
    PhotoDraweeView mPhotoDraweeView;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Extra
    String imageUrl;

    @AfterViews
    public void after() {

      //  toolbar.setTitle(FileInfo.getFileInfo(imageUrl.toString()).get(Const.FILE_NAME));

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mPhotoDraweeView.setPhotoUri(Uri.parse(imageUrl));
        mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }
        });
        mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
            }
        });

        mPhotoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
