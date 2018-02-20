package com.p82018.sw806f18.p8androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class GalleryActivity extends AppCompatActivity implements GalleryRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private GalleryRecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    String selectedImage = null;
    int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = (RecyclerView) findViewById(R.id.image_recyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAdapter = new GalleryRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter.setOnItemClickListener(this);

        prepareGallery();

        setDelete();

        setUpload();
    }

    private void setUpload() {
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(selectedIndex == -1){
                    Toast.makeText(GalleryActivity.this, getString(R.string.please_select_image_to_upload), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.PATH_OF_IMAGE_TO_UPLOAD, selectedImage);
                setResult(202, intent);
                selectedImage = null;
                selectedIndex = -1;
                finish();
            }
        });
    }

    private void setDelete() {
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(selectedIndex == -1){
                    Toast.makeText(GalleryActivity.this, getString(R.string.please_select_image_to_delete), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Display alert dialog to confirm delete
                AlertDialog.Builder alertClickImages = new AlertDialog.Builder(GalleryActivity.this);
                alertClickImages.setMessage(getString(R.string.are_you_sure_to_delete));
                alertClickImages.setCancelable(true);

                alertClickImages.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File file = new File(selectedImage);
                                boolean isDelete = file.delete();

                                recyclerViewAdapter.remove(selectedIndex);
                                ImageView imageView = (ImageView) findViewById(R.id.image_preview);
                                imageView.setImageDrawable(null);
                                dialog.cancel();
                                selectedImage = null;
                                selectedIndex = -1;
                            }
                        });

                alertClickImages.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alertClickImages.show();
            }
        });
    }

    private void prepareGallery() {

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/WebviewCameraDemo/");
        String targetPath = directory.getAbsolutePath();

//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();


        for (File file : files) {
            Uri uri = Uri.fromFile(file);
            recyclerViewAdapter.add(
                    recyclerViewAdapter.getItemCount(),
                    uri);
        }
        if (files.length == 0) {
            showNoImagesText();
        }
    }

    public void showNoImagesText() {
        findViewById(R.id.txt_no_images_found).setVisibility(View.VISIBLE);
        findViewById(R.id.image_preview).setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(GalleryRecyclerViewAdapter.ItemHolder item, int position) {
//        Toast.makeText(GalleryActivity.this, item.getItemUri(), Toast.LENGTH_SHORT).show();
        selectedImage = item.getItemUri();
        selectedIndex = position;

        ImageView imageView = (ImageView) findViewById(R.id.image_preview);
        File imgFile = new File(item.getItemUri());

        if (imgFile.exists()) {
            Bitmap myBitmap = Util.convertImageFileToBitmap(item.getItemUri(), 200, 200);
            imageView.setImageBitmap(myBitmap);
        }
    }
}