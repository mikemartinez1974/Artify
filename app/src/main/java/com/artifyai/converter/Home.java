package com.artifyai.converter;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    import androidx.core.content.FileProvider;

    import android.Manifest;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.drawable.BitmapDrawable;
    import android.graphics.drawable.Drawable;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Environment;
    import android.provider.MediaStore;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.HorizontalScrollView;
    import android.widget.ImageView;
    import android.widget.LinearLayout;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.UUID;

    import io.swagger.client.*;
    import io.swagger.client.auth.*;
    import io.swagger.client.model.*;
    import io.swagger.client.api.DefaultApi;


public class Home extends AppCompatActivity {

        private static final int REQUEST_IMAGE_PICK = 1;
        private static final int REQUEST_IMAGE_CAPTURE = 2;
        private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

        private ImageView ivSelfie;
        private Button btnSelect;
        private Button btnSelfie;
        private Button btnUpload;
        private HorizontalScrollView hsvSelectedImages;
        private LinearLayout llThumbnails;
        private ArrayList<String> selected_files = new ArrayList<>();

        Uri imageUri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            ivSelfie = (ImageView)findViewById(R.id.ivSelfie);
            btnSelect = (Button)findViewById(R.id.btnSelect);
            btnSelfie = (Button)findViewById(R.id.btnSelfie);
            btnUpload = (Button)findViewById(R.id.btnUpload);
            hsvSelectedImages = (HorizontalScrollView) findViewById(R.id.hsvThumbnails);
            llThumbnails = (LinearLayout)findViewById(R.id.llThumbnails);
            llThumbnails.removeAllViewsInLayout();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(125,150);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            mlp.setMargins(5,10,5,10);
        }

        public void btnSelect_onClick(View button) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        }

        public void btnSelfie_onClick(View v) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
            } else {
                captureImage();
            }
        }

        private void captureImage() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_PERMISSION_REQUEST_CODE);
        }

    public void btnUpload_onClick(View button) {

        // Create the array to hold the selected files
        File[] selected_files = new File[llThumbnails.getChildCount()];

        // Iterate over the child views of llThumbnails (assuming they are all ImageViews)
        for (int i = 0; i < llThumbnails.getChildCount(); i++) {
            View childView = llThumbnails.getChildAt(i);
            if (childView instanceof ImageView) {
                ImageView imageView = (ImageView) childView;
                // Get the Bitmap from the ImageView
                if (imageView.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    // Convert the Bitmap to a byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    // Create a unique filename with UUID
                    String filename = UUID.randomUUID().toString() + ".jpg";
                    // Create a File object with the filename
                    File file = new File(button.getContext().getFilesDir(), filename);
                    try {
                        // Write the byte array to the file
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(byteArray);
                        fos.close();

                        // Add the file to selected_files array
                        selected_files[i] = file;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // TODO: do something with the selected_files array
        // Pass selected files ( selected_files[] ) to upload service.
        // Or pass to the next activity.

        boolean uploadComplete = upload_files(selected_files);



    }

    private boolean upload_files(File[] selected_files) {
        boolean retval;
        DefaultApi api = new DefaultApi();

        JSONObject result = api.uploadimage(selected_files);
        String result_code;
        try {
            result_code = result.get("code").toString();
        } catch (JSONException e) {
            result_code = "0";
        }

        if (result_code == "200") {
            //Go to wait screen.
        }
        else {
            alert("File upload was unsuccessful.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK || requestCode == REQUEST_IMAGE_CAPTURE) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivSelfie.setImageBitmap(bitmap);
                    addImageToScrollView(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivSelfie.setImageBitmap(bitmap);
                addImageToScrollView(bitmap);
            }
        }
    }

    private void addImageToScrollView(Bitmap bitmap) {
        Drawable selfie = ivSelfie.getDrawable();
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(selfie);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
        params.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(params);
        llThumbnails.addView(imageView);
    }

    public ArrayList<String> getSelectedFiles() {
        return selected_files;
    }

    private void fileSelected(Uri uri) {
        String filepath = uri.getPath();
        Drawable thumbnailicon;


        try {
            InputStream is = getContentResolver().openInputStream(uri);
            thumbnailicon = Drawable.createFromStream(is, uri.toString());
        } catch (FileNotFoundException e) {
            alert("file not found fetching fetching image.");
        }

        //ivSelfie.setBackground(thumbnailicon);
        ivSelfie.setImageURI(uri);

        ImageView ivNewThumbnail = new ImageView(this);
        ivNewThumbnail.setImageURI(uri);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(125,150);
        lp.setMargins(5,10,5,10);

        ivNewThumbnail.setLayoutParams(lp);

        llThumbnails.addView(ivNewThumbnail);
        //llThumbnails.addView(ivNewThumbnail);
        //hsv.arrowScroll(2);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public Uri createUri() {
        File imageFile = new File(getApplicationContext().getFilesDir(),"selfie.jpg");
        return FileProvider.getUriForFile( getApplicationContext(), "com.artifyai.converter.fileProvider",imageFile);
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Show explanation why the app needs the camera permission
            new AlertDialog.Builder(this)
                    .setTitle("Camera permission")
                    .setMessage("This app needs access to your camera to take pictures.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Request the permission
                            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private Intent trainingIntent = null;
    private void switchToTraining(){
        if(trainingIntent == null) {
            trainingIntent = new Intent(this, GeneratingImages.class);
        }

        startActivity(trainingIntent);
    }
    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}