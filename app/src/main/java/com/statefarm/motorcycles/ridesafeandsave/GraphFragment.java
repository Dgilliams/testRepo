package com.statefarm.motorcycles.ridesafeandsave;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    public static final int TYPE_SPEED = 0;
    public static final int TYPE_ACCELERATION = 1;

    private int type;

    ArrayList<Float> speedData;
    ArrayList<Float> accelData;

    SparkView speedSparkView;
    SparkView accelSparkView;

    public static GraphFragment newInstance(int type) {
        GraphFragment gf = new GraphFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        gf.setArguments(args);
        return gf;
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speedData = new ArrayList<>();
        accelData = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blank, container, false);
        speedSparkView = (SparkView) v.findViewById(R.id.sparkview1);
        accelSparkView = (SparkView) v.findViewById(R.id.sparkview2);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference().child("records");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Double speed = dataSnapshot.child("speed").getValue(Double.class);
                Double accel = dataSnapshot.child("acceleration").child("z").getValue(Double.class);
                Log.d("butts", ""+speed);

                Float y = Float.valueOf(""+speed);
                if(y != null) {
                    speedData.add(y);
                }

                Float z = Float.valueOf(""+accel);
                if(z != null) {
                    accelData.add(z * -1);
                }

                speedSparkView.setAdapter(new MyAdapter(speedData));
                accelSparkView.setAdapter(new MyAdapter2(accelData));
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
        });

    }


    public class MyAdapter extends SparkAdapter {
        private ArrayList<Float> data;

        public MyAdapter(ArrayList<Float> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int index) {
            return data.get(index);
        }

        @Override
        public float getY(int index) {
            return (float) getItem(index);
        }

        @Override
        public boolean hasBaseLine() {
            return true;
        }
    }

    public class MyAdapter2 extends SparkAdapter {
        private ArrayList<Float> data;

        public MyAdapter2(ArrayList<Float> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int index) {
            return data.get(index);
        }

        @Override
        public float getY(int index) {
            return (float) getItem(index);
        }

        @Override
        public boolean hasBaseLine() {
            return true;
        }

        @Override
        public float getBaseLine() {
            return -4.6f;
        }
    }
}
