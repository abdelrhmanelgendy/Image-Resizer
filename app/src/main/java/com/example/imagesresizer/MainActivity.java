package com.example.imagesresizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //[Image{id=1215, uri='default'}, Image{id=0, uri='content://media/external/file/11230'}, Image{id=1, uri='content://media/external/file/11223'}, Image{id=2, uri='content://media/external/file/11231'}, Image{id=3, uri='content://media/external/file/11227'}, Image{id=4, uri='content://media/external/file/11224'}, Image{id=5, uri='content://media/external/file/11229'}, Image{id=6, uri='content://media/external/file/11226'}, Image{id=7, uri='content://media/external/file/11228'}]
    private static final String TAG = "TAG20";
    private static final int PERMISSION_REQ_CODE = 51;
    private static final int INTENT_REQ_CODE = 101;
    Uri uri;
    Button btnChoose, btnReduce;
    TextView txtPath, txtSizeDiff;
    ImageView imgChoosenImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnChoose.setOnClickListener(this);
        btnReduce.setOnClickListener(this);


    }

    void init() {
        btnChoose = findViewById(R.id.btnChooseImage);
        btnReduce = findViewById(R.id.btnReduce);
        txtPath = findViewById(R.id.TV_outPath);
        imgChoosenImage = findViewById(R.id.imgViewImage);
        txtSizeDiff = findViewById(R.id.TV_DiffInSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChooseImage:
                chooseImage();
                break;
            case R.id.btnReduce:
                reduceImageSize();
                break;

        }
    }

    private void reduceImageSize() {
        if (uri == null) {
            Toast.makeText(this, "please Choose Image First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri.toString().length() > 1) {
            Resizer.setContext(getApplicationContext());
            String outImagePath = Resizer.reduceBitmab(uri);
            txtPath.setText("Image Save At: " + outImagePath);


            try {
                long oldSizeInKB = (getContentResolver().openAssetFileDescriptor(uri, "r").getLength() / 1024);
                long newSizeInKB = (getContentResolver().openAssetFileDescriptor(Uri.fromFile(new File(outImagePath)), "r").getLength() / 1024);
                txtSizeDiff.setText("old size : " + oldSizeInKB + " KB" + "\nnew size : " + newSizeInKB + " KB");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void chooseImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, INTENT_REQ_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "accept Permission to open Gallery", Toast.LENGTH_SHORT).show();
            } else {
                chooseImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri imagePath = data.getData();
                    imgChoosenImage.setImageURI(imagePath);
                    uri = imagePath;
                }

            }

        }
    }
}
