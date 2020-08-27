package com.melekalyaprak.myrealprojectt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class uploadActivity extends AppCompatActivity {
    ImageView postImage;
    EditText nameText;
    EditText surnameText;
    EditText ageText;
    EditText jobText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        postImage=findViewById(R.id.postimageView);
        nameText=findViewById(R.id.nameText);
        surnameText=findViewById(R.id.surnameText);
        ageText=findViewById(R.id.ageText);
        jobText=findViewById(R.id.jobText);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();
    }

    public void upload(View view){
        UUID uuid=UUID.randomUUID();
        final String imageName="images/"+uuid+".jpg";
        StorageReference storageReference=mStorageRef.child(imageName);
        storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newReference=FirebaseStorage.getInstance().getReference(imageName);
                newReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL=uri.toString();
                       // System.out.println("downloadURL: " + downloadURL);
                        FirebaseUser user=mAuth.getCurrentUser();
                        String userEmail=user.getEmail();
                        String userName=nameText.getText().toString();
                        String userSurname=surnameText.getText().toString();
                        String userAge=ageText.getText().toString();
                        String userPosition=jobText.getText().toString();
                        UUID uuid1=UUID.randomUUID();
                        String uuidString=uuid1.toString();
                        myRef.child("Workers").child(uuidString).child("userEmail").setValue(userEmail);
                        myRef.child("Workers").child(uuidString).child("downloadURL").setValue(downloadURL);
                        myRef.child("Workers").child(uuidString).child("userName").setValue(userName);
                        myRef.child("Workers").child(uuidString).child("userSurname").setValue(userSurname);
                        myRef.child("Workers").child(uuidString).child("userAge").setValue(userAge);
                        myRef.child("Workers").child(uuidString).child("userPosition").setValue(userPosition);
                        Toast.makeText(uploadActivity.this, "Çalışan Oluşturuldu", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),feedActivity.class);
                        startActivity(intent);




                    }
                });


            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1 ){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            selectedImage=data.getData();
            try {

                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);

                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);

                    postImage.setImageBitmap(bitmap);

                } else {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    postImage.setImageBitmap(bitmap);

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
