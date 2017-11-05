package com.example.klun.gynie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WomanActivity extends AppCompatActivity {
    String user;
    String caseID;

    public void onCaseClick(View view) {
        Intent intent = new Intent(this, CaseActivity.class);
        intent.putExtra("caseID", caseID);
        intent.putExtra("user", user);
        intent.putExtra("isWoman", true);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woman);
        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final String diaryID = snapshot.child("users").child(user).child("diaryID").getValue(String.class);
                caseID = diaryID;
                DataSnapshot ds = snapshot.child("diarys").child(diaryID).child("Entries");
                ArrayList<String> entryDates = new ArrayList<String>();
                for (DataSnapshot dss : ds.getChildren()) {
                    entryDates.add(dss.getKey());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(WomanActivity.this, android.R.layout.simple_list_item_1, entryDates);
                ListView listView = (ListView) findViewById(R.id.entries_list_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt = (TextView) view;
                        Intent intent = new Intent(WomanActivity.this, EntryActivity.class);
                        intent.putExtra("date", txt.getText().toString());
                        intent.putExtra("diaryID", diaryID);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("HI");
            }
        });
    }
}
