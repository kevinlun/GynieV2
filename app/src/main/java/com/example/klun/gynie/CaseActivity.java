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

public class CaseActivity extends AppCompatActivity {

    String caseID;
    Boolean isWoman;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);

        Intent intent = getIntent();
        caseID = intent.getStringExtra("caseID");
        isWoman = intent.getBooleanExtra("isWoman", false);
        user = intent.getStringExtra("user");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final String diaryID = snapshot.child("cases").child(caseID).child("diaryID").getValue(String.class);
                DataSnapshot diary = snapshot.child("diarys").child(diaryID);
                String userID = diary.child("userID").getValue(String.class);
                TextView userText = (TextView) findViewById(R.id.user_text_view);
                userText.setText(userID);

                ArrayList<String> entryDates = new ArrayList<String>();
                for (DataSnapshot dss : diary.child("Entries").getChildren()) {
                    entryDates.add(dss.getKey());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CaseActivity.this, android.R.layout.simple_list_item_1, entryDates);
                ListView listView = (ListView) findViewById(R.id.diary_list_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt = (TextView) view;
                        Intent intent = new Intent(CaseActivity.this, EntryActivity.class);
                        intent.putExtra("date", txt.getText().toString());
                        intent.putExtra("diaryID", diaryID);
                        startActivity(intent);
                    }
                });

                ArrayList<String> comments = new ArrayList<String>();
                for (DataSnapshot dss : snapshot.child("cases").child(caseID).child("comments").getChildren()) {
                    comments.add(dss.child("text").getValue() + " - " + dss.child("user").getValue());
                }
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CaseActivity.this, android.R.layout.simple_list_item_1, comments);
                ListView listView2 = (ListView) findViewById(R.id.comments_list_view);
                listView2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("HI");
            }
        });
    }
}
