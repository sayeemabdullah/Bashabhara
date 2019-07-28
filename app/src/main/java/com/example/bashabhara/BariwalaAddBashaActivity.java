package com.example.bashabhara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private String mAdImageUrlImage;

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
        mBariwalaDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Location").child(userID);


        mAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        getBariwalaAd();
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

    private void getBariwalaAd(){
        mBariwalaDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("address")!=null){
                        mAddress = map.get("address").toString();
                        mAddressField.setText(mAddress);
                    }
                    if(map.get("area")!=null){
                        mArea = map.get("area").toString();
                        mAreaField.setText(mArea);
                    }
                    if(map.get("latitude")!=null){
                        mLat = map.get("latitude").toString();
                        mLatField.setText(mLat);
                    }
                    if(map.get("longitude")!=null){
                        mLong = map.get("longitude").toString();
                        mLongField.setText(mLong);
                    }
                    if(map.get("squarefeet")!=null){
                        mSquarefeet = map.get("squarefeet").toString();
                        mSquarefeetField.setText(mSquarefeet);
                    }
                    if(map.get("rooms")!=null){
                        mRooms = map.get("rooms").toString();
                        mRoomsField.setText(mRooms);
                    }
                    if(map.get("rent")!=null){
                        mRent = map.get("rent").toString();
                        mRentField.setText(mRent);
                    }
                    if(map.get("number")!=null){
                        mNumber = map.get("number").toString();
                        mNumberField.setText(mNumber);
                    }
                    if(map.get("BashaImageUrl")!=null){
                        mAdImageUrlImage = map.get("BashaImageUrl").toString();
                        Glide.with(getApplication()).load(mAdImageUrlImage).into(mAdImage);
                    }


                    /*if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(mProfileImageUrl).into(mProfileImage);
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        mBariwalaDatabase.updateChildren(userInfo);
        //mBariwalaDatabase.child(mAuth.getCurrentUser().getUid()).push().setValue(userInfo);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mAdImage.setImageURI(resultUri);
        }
    }

}
