package com.melekalyaprak.myrealprojectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class feedActivity extends AppCompatActivity {
    ListView listView;
    WorkerClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String>  userEmailFromFB;
    ArrayList<String> userNameFromFB;
    ArrayList<String> userSurnameFromFB;
    ArrayList<String> userAgeFromFB;
    ArrayList<String> userPositionFB;
    ArrayList<String> userImageFB;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.calisan_ekle,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.calisan_ekle){
            Intent intent=new Intent(getApplicationContext(),uploadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView=findViewById(R.id.listView);
        userEmailFromFB=new ArrayList<String>();
        userNameFromFB=new ArrayList<String>();
        userSurnameFromFB=new ArrayList<String>();
        userAgeFromFB=new ArrayList<String>();
        userPositionFB=new ArrayList<String>();
        userImageFB=new ArrayList<String>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        adapter=new WorkerClass(userEmailFromFB,userNameFromFB,userSurnameFromFB,userAgeFromFB,userPositionFB,userImageFB,this,buttonListener);
        listView.setAdapter(adapter);
        getDataFromFirebase();


    }


    public WorkerClass.ButtonListener buttonListener = new WorkerClass.ButtonListener() {
        @Override
        public void onClickListener(int position) {
            adapter.updateData(userEmailFromFB.get(position),position);


        }
    };

    public void getDataFromFirebase(){
        DatabaseReference newReference =firebaseDatabase.getReference("Workers");
        newReference.addValueEventListener(new ValueEventListener() { //Database de değişiklik olduğunda bana haber ver
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap= (HashMap<String, String>) ds.getValue();
                    userEmailFromFB.add(hashMap.get("userEmail"));
                    userNameFromFB.add(hashMap.get("userName"));
                    userSurnameFromFB.add(hashMap.get("userSurname"));
                    userAgeFromFB.add(hashMap.get("userAge"));
                    userPositionFB.add(hashMap.get("userPosition"));
                    userImageFB.add(hashMap.get("downloadURL"));
                    adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
