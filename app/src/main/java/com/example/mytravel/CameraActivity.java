package com.example.mytravel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.example.mytravel.db.AppDatabase;
import com.example.mytravel.db.TravelDAO;
import com.example.mytravel.models.Travel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity{

    public static String EXTRA_TRAVEL_ID = "travel_id";
    private TravelDAO mTravelDAO;
    private Travel TRAVEL;
    private static final int PERMISSION_CAMERA=1111;
    private static final int REQUEST_TAKE_PHOTO = 1;


    String PhotoPath;
    ImageView iv_view;

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        iv_view = (ImageView) findViewById(R.id.iv_view);

        mTravelDAO = Room.databaseBuilder(this,AppDatabase.class,"db-contacts").allowMainThreadQueries().build().getTravelDAO();
        TRAVEL = mTravelDAO.getTravelWithId(getIntent().getIntExtra(EXTRA_TRAVEL_ID, -1));


        captureCamera();
    }

    private void captureCamera(){
        String state = Environment.getExternalStorageState();
        Log.i("abc","captureCamera in");

        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent Picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(Picture.resolveActivity(getPackageManager())!=null){
                File photo = null;
                try{
                    photo = createImageFile();
                }catch(IOException e){
                    Log.e("Capture error",e.toString());
                }

                if (photo != null){

                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)||(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))){
                            //reference: https://g-y-e-o-m.tistory.com/48
                            new AlertDialog.Builder(this)
                                    .setTitle("alert")
                                    .setMessage("Storage access denied")
                                    .setNeutralButton("setting", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:"+getPackageName()));
                                            startActivity(intent);
                                        }
                                    })
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                            //complete reference
                            Log.i("abc","if");
                        }
                        else{
                            Log.i("abc","else");
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},PERMISSION_CAMERA);
                        }
                    }
                    else{
                        Log.i("abc","success");

                        Uri providerURI = FileProvider.getUriForFile(this,"com.example.mytravel",photo);
                        //FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photo);

                        imageUri = providerURI;

                        Picture.putExtra(MediaStore.EXTRA_OUTPUT,providerURI);

                        startActivityForResult(Picture,REQUEST_TAKE_PHOTO);
                    }

                }
            }
        }
        else{
            Toast.makeText(this,"cannot access to storage",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException{
        //create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = TRAVEL.getCity() + "_" + timeStamp + ".jpg";
        File image =null;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(!storageDir.exists()){
            Log.i("abc",storageDir.toString());
            storageDir.mkdirs();
        }

        image = new File(storageDir,imageFileName);
        Log.i("abc",image.getAbsolutePath());
        PhotoPath = image.getAbsolutePath();

        return image;
    }

//reference:https://g-y-e-o-m.tistory.com/48
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(PhotoPath);
        Log.i("abc",PhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"save successfully",Toast.LENGTH_SHORT).show();
    }
    //complete reference


   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        String k = imageUri.toString();

                        Log.i("abc", k);

                        galleryAddPic();
                        iv_view.setImageURI(imageUri);




                    } catch (Exception e) {
                        Log.e("abc", e.toString());
                    }

                } else {
                    Toast.makeText(CameraActivity.this, "Fail to take photo", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){

        switch(requestCode){
            case PERMISSION_CAMERA:
                for(int i = 0; i< grantResults.length;i++){
                    if(grantResults[i]<0){
                        Toast.makeText(CameraActivity.this,"Activate camera permission",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }
}

