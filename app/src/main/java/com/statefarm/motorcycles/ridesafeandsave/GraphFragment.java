package com.statefarm.motorcycles.ridesafeandsave;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    public static final int TYPE_SPEED = 0;
    public static final int TYPE_ACCELERATION = 1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blank, container, false);

        return v;
    }

}
