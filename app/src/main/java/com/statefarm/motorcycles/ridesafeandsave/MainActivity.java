package com.statefarm.motorcycles.ridesafeandsave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView view;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private View lastRideCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);
        listView = (ExpandableListView) findViewById(R.id.list_view);
        retrieveDataFromFirebase();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listView.setAdapter(listAdapter);
        lastRideCard = findViewById(R.id.last_ride_card);

        lastRideCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HERE
                Toast.makeText(getApplicationContext(), "CLICK", Toast.LENGTH_LONG).show();
            }
        });

    }

    ChildEventListener childEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            String value = (String) dataSnapshot.getValue();
            view.setText("key: " + key + " value: " + value);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void retrieveDataFromFirebase() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("6/15/2016");
        listDataHeader.add("6/15/2016");
        listDataHeader.add("6/15/2016");

        // Adding child data
        List<String> header1 = new ArrayList<String>();
        header1.add("Start Time: 5:00 PM\t ");
        header1.add("End Time: 5:15 PM\t ");
        header1.add("Trip Length: 2 miles\t ");
        header1.add("Trip Length: 15 minutes\t");
        header1.add("Top Speed: 35 MPH\t");
        header1.add("Top RPM: 6578\t");

        List<String> header2 = new ArrayList<String>();
        header2.add("Start Time: 5:45 PM\t");
        header2.add("End Time: 6:30 PM\t");
        header2.add("Trip Length: 5 Miles\t");
        header2.add("Trip Length: 45 Minutes\t");
        header2.add("Top Speed: 60 MPH\t");
        header2.add("Top RPM: 8987\t");

        List<String> header3 = new ArrayList<String>();
        header3.add("Start Time: 6:40 PM\t");
        header3.add("End Time: 7:00 PM\t");
        header3.add("Trip Length: 2 Miles\t");
        header3.add("Trip Length: 20 Minutes\t");
        header3.add("Top Speed: 35 MPH\t");
        header3.add("Top RPM: 5098\t");

        listDataChild.put(listDataHeader.get(0), header1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), header2);
        listDataChild.put(listDataHeader.get(2), header3);

    }

}
