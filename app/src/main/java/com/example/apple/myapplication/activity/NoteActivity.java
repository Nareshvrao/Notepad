package com.example.apple.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apple.myapplication.util.DatabaseHandler;
import com.example.apple.myapplication.util.NoteEntitity;
import com.example.apple.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    EditText title;
    EditText note;

    Button save;

    DatabaseHandler db;

    Context mContext;

    Intent intent;

    String ID;

    String image_url;

    Button add_image_btn;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy kk:mm aa");

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;

    Activity noteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        noteActivity = this;
        mContext = this;

        title = (EditText) findViewById(R.id.title);

        note = (EditText) findViewById(R.id.note);

        save = (Button) findViewById(R.id.save);

        add_image_btn = (Button) findViewById(R.id.add_image);

        mImageView = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        String title2 = intent.getStringExtra("title");
        String note2 = intent.getStringExtra("note");
        image_url = intent.getStringExtra("image_url");


        ID = intent.getStringExtra("ID");


        if (ID != null) {

            title.setText(title2);

            note.setText(note2);

            save.setText("Edit");

            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image_url));
                mImageView.setImageBitmap(mImageBitmap);
                mImageView.setVisibility(View.VISIBLE);
                add_image_btn.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        db = new DatabaseHandler(this);

        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("mundo", " tag 1 ");
                if ((ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ) || (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ) || (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ) ) {

                    // Should we show an explanation?
                    if (
                            ActivityCompat.shouldShowRequestPermissionRationale(noteActivity,
                            Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (noteActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            ||
                            ActivityCompat.shouldShowRequestPermissionRationale
                                    (noteActivity, Manifest.permission.READ_EXTERNAL_STORAGE)

                            ) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(noteActivity,
                                new String[]{Manifest.permission.CAMERA ,Manifest.permission
                                        .READ_EXTERNAL_STORAGE , Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {

                      camerapic();

                }


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title1 = title.getText().toString();

                String note1 = note.getText().toString();

                image_url = mCurrentPhotoPath;

        //        Log.v("image_url" ,mCurrentPhotoPath ) ;

                if (title1.length() > 0) {

                    if (note1.length() > 0) {

                        if (ID != null) {

                            NoteEntitity c = db.getNote(Integer.valueOf(ID));


                            System.out.println("title4 " + c.getTitle() + " , " + c.getText() + " , " + c.getID());

                            //          System.out.println("title3 " + title1 + " , " + note1 + " , " + Integer.valueOf(ID) );

                            c.setTitle(title1);

                            c.setText(note1);

                            c.setImageUrl(image_url);

                            String currentDateTime = dateFormat.format(new Date()); // Find todays date


                            c.setTime(currentDateTime);

                            c.setID(Integer.valueOf(ID));

                            db.updateContact(c);

                        } else {

                            //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentDateTime = dateFormat.format(new Date()); // Find todays date

                            db.addNoteDetail(new NoteEntitity(title1, note1, image_url, currentDateTime));

                        }

                        Intent intent = new Intent(mContext.getApplicationContext(), HomeActivity.class);

                        mContext.startActivity(intent);

                        finish();

                    } else {

                        Toast.makeText(mContext, "Please make a note ", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(mContext, "Please enter the title of your note ", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    camerapic();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void camerapic(){
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(mContext.getApplicationContext(), HomeActivity.class);

        mContext.startActivity(intent);

        finish();
    }

   /* private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (data.hasExtra("data")) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
              Uri  uri = getImageUri(NoteActivity.this, bitmap);
                File finalFile = new File(getRealPathFromUri(uri));

                mCurrentPhotoPath =  uri.toString();
                mImageView.setImageBitmap(bitmap);
                mImageView.setVisibility(View.VISIBLE);
                add_image_btn.setVisibility(View.INVISIBLE);

            } else if (data.getExtras() == null) {

                Toast.makeText(getApplicationContext(),
                        "No extras to retrieve!", Toast.LENGTH_SHORT)
                        .show();

                BitmapDrawable thumbnail = new BitmapDrawable(
                        getResources(), data.getData().getPath());
                mImageView.setImageDrawable(thumbnail);
                mImageView.setVisibility(View.VISIBLE);

                add_image_btn.setVisibility(View.INVISIBLE);

            }
        }
    }
    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private Uri getImageUri(NoteActivity youractivity, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(youractivity.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
