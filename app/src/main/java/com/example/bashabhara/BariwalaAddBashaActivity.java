package com.example.bashabhara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BariwalaAddBashaActivity extends AppCompatActivity {

    private EditText mAddressField, mAreaField, mLatField, mLongField, mSquarefeetField, mRoomsField,
            mRentField,mNumberField;

    private Button mCancel, mAdd;

    private ImageView mAdImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mBariwalaDatabase;

    private String userID;
    private String mAddress;
    private String mArea;
    private String mLat;
    private String mLong;
    private String mSquarefeet;
    private String mRooms;
    private String mRent;
    private String mNumber;
    private String mAdImageUrl;

    private Uri resultUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bariwala_add_basha);


        mAddressField = (EditText) findViewById(R.id.address);
        mAreaField = (EditText) findViewById(R.id.area);
        mLatField = (EditText) findViewById(R.id.latitude);
        mLongField = (EditText) findViewById(R.id.longitude);
        mSquarefeetField = (EditText) findViewById(R.id.squarefeet);
        mRoomsField = (EditText) findViewById(R.id.rooms);
        mRentField = (EditText) findViewById(R.id.rent);
        mNumberField = (EditText) findViewById(R.id.number);

        mAdImage = (ImageView) findViewById(R.id.bashaimage);

        mCancel = (Button) findViewById(R.id.cancel);
        mAdd = (Button) findViewById(R.id.add);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mBariwalaDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Location");


        mAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserAd();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });


    }

    private void AddUserAd() {
        //Intent intent = new Intent(getApplicationContext(), BariwalaMapActivity.class);
        mAddress = mAddressField.getText().toString();
        //intent.putExtra("address", mAddress);
        mArea = mAreaField.getText().toString();
        //intent.putExtra("area", mArea);
        mLat =mLatField.getText().toString();
        //intent.putExtra("latitude", mLat);
        mLong = mLongField.getText().toString();
        //intent.putExtra("longitude", mLong);
        mSquarefeet = mSquarefeetField.getText().toString();
        //intent.putExtra("squarefeet", mSquarefeet);
        mRooms = mRoomsField.getText().toString();
        //intent.putExtra("rooms", mRooms);
        mRent = mRentField.getText().toString();
        //intent.putExtra("rent", mRent);
        mNumber = mNumberField.getText().toString();
        //intent.putExtra("number",mNumber);


        Map userInfo = new HashMap();
        userInfo.put("address", mAddress);
        userInfo.put("area", mArea);
        userInfo.put("latitude", mLat);
        userInfo.put("longitude", mLong);
        userInfo.put("squarefeet", mSquarefeet);
        userInfo.put("rooms", mRooms);
        userInfo.put("rent", mRent);
        userInfo.put("number", mNumber);
        mBariwalaDatabase.child(mAuth.getCurrentUser().getUid()).push().setValue(userInfo);

        if(resultUri != null) {

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("basha_images").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri downloadUrl = uri.getResult();

                    Map newImage = new HashMap();
                    newImage.put("BashaImageUrl", downloadUrl.toString());
                    mBariwalaDatabase.updateChildren(newImage);

                    finish();
                    return;
                }
            });
        }else{
            finish();
        }

    }
}
