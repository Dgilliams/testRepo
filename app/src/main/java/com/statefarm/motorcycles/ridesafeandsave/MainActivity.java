package com.statefarm.motorcycles.ridesafeandsave;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.percent.PercentFrameLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener {
    private DatabaseReference mDatabase;
    private TextView view;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private View lastRideCard;
    private ScrollView scrollView;

    private PercentFrameLayout badge;
    private CardView overlay;
    private CardView dashboard;

    private TextView money;
    private int actualMoney;

    private TextView scoreText;
    private int scoreValue;



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
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        overlay = (CardView) findViewById(R.id.dashboard_totals_card_overlay);
        dashboard = (CardView) findViewById(R.id.dashboard_totals_card);

        badge = (PercentFrameLayout) findViewById(R.id.dashboard_user_badge);

        money = (TextView) findViewById(R.id.money_saved);
        actualMoney = 162;

        scoreText = (TextView) findViewById(R.id.dashboard_points_accumulated);
        scoreValue = 2415;

        initOnClickListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        incrementMoney();
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) listView.getLayoutParams();
        param.height = (3 * listView.getHeight());
        listView.setLayoutParams(param);
        listView.refreshDrawableState();
        scrollView.refreshDrawableState();
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) listView.getLayoutParams();
        param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        listView.setLayoutParams(param);
        listView.refreshDrawableState();
        scrollView.refreshDrawableState();
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

    public void initOnClickListeners() {

        badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularReveal(R.id.dashboard_totals_card_overlay);
                incrementScore();
            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularHide(R.id.dashboard_totals_card_overlay);
                incrementMoney();
            }
        });


        lastRideCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GraphActivity.class));
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //incrementMoney();
            }
        });
    }


    public void retrieveDataFromFirebase() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child speedData
        listDataHeader.add("6/15/2016 \t\t ");
        listDataHeader.add("6/14/2016 \t\t ");
        listDataHeader.add("6/13/2016 \t\t ");

        // Adding child speedData
        List<String> header1 = new ArrayList<String>();
        header1.add("Start Time:\t\t5:00 PM ");
        header1.add("End Time:\t\t\t\t5:15 PM ");
        header1.add("Trip Length:\t\t2 miles ");
        header1.add("Trip Length:\t\t15 minutes");
        header1.add("Top Speed:\t\t35 MPH");
        header1.add("Top RPM:\t\t\t\t6578");

        List<String> header2 = new ArrayList<String>();
        header2.add("Start Time:\t\t5:45 PM");
        header2.add("End Time:\t\t\t\t6:30 PM");
        header2.add("Trip Length:\t\t5 Miles");
        header2.add("Trip Length:\t\t45 Minutes");
        header2.add("Top Speed:\t\t60 MPH");
        header2.add("Top RPM:\t\t\t\t8987");

        List<String> header3 = new ArrayList<String>();
        header3.add("Start Time:\t\t6:40 PM");
        header3.add("End Time:\t\t\t\t7:00 PM");
        header3.add("Trip Length:\t\t2 Miles");
        header3.add("Trip Length:\t\t20 Minutes");
        header3.add("Top Speed:\t\t35 MPH");
        header3.add("Top RPM:\t\t\t\t5098");

        listDataChild.put(listDataHeader.get(0), header1); // Header, Child speedData
        listDataChild.put(listDataHeader.get(1), header2);
        listDataChild.put(listDataHeader.get(2), header3);

    }

    public void circularReveal(int id) {
        // previously invisible view
        View myView = findViewById(id);

// get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

// get the final radius for the clipping circle
        float finalRadius = (float) cx * 2;

// create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, 0, 0, 0, finalRadius);

// make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    public void circularHide(int id) {
        // previously visible view
        final View myView = findViewById(id);

// get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

// get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

// create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, 0, 0, initialRadius, 0);

// make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

// start the animation
        anim.start();

    }


    private void countingMoneyAnimation(final TextView textView, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, to);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("$" + (int) animation.getAnimatedValue() + ".20");
            }
        });
        animator.start();
    }

    private void scoreAnimation(final TextView textView, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, to);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue() + "pts");
            }
        });
        animator.start();
    }

    public void incrementMoney(){
        actualMoney += 4;
        countingMoneyAnimation(money, actualMoney);
    }

    public void incrementScore(){
        scoreValue += 15;
        scoreAnimation(scoreText, scoreValue);
    }



}
