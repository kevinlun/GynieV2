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
import java.util.HashMap;

public class GynoActivity extends AppCompatActivity {

    String gyno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyno);
        Intent intent = getIntent();
        gyno = intent.getStringExtra("user");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cases");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> cases = new ArrayList<String>();
                final HashMap<String, String> caseMap = new HashMap<>();
                for (DataSnapshot dss : snapshot.getChildren()) {
                    String problem = dss.child("problem").getValue(String.class);
                    String caseID = dss.getKey();
                    dss.child("problem").getValue();
                    caseMap.put(problem, caseID);
                    cases.add(problem);
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(GynoActivity.this, android.R.layout.simple_list_item_1, cases);
                ListView listView = (ListView) findViewById(R.id.cases_list_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt = (TextView) view;
                        Intent intent = new Intent(GynoActivity.this, CaseActivity.class);
                        intent.putExtra("caseID", caseMap.get(txt.getText().toString()));
                        intent.putExtra("user", gyno);
                        intent.putExtra("isWoman", false);
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
