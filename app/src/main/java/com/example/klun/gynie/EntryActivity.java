package com.example.klun.gynie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EntryActivity extends AppCompatActivity {

    String diaryID;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Intent intent = getIntent();
        diaryID = intent.getStringExtra("diaryID");
        date = intent.getStringExtra("date");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("diarys/" + diaryID + "/Entries/" + date);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String content = snapshot.getValue(String.class);
                EditText dateText = (EditText) findViewById(R.id.entry_date);
                dateText.setText(date);
                EditText entryText = (EditText) findViewById(R.id.entry_content);
                entryText.setText(content);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("HI");
            }
        });
    }
}
