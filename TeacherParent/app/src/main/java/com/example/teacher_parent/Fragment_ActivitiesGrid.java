package com.example.teacher_parent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Fragment_ActivitiesGrid extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Fragment_ActivitiesGrid() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_ActivitiesGrid newInstance(String param1, String param2) {
        Fragment_ActivitiesGrid fragment = new Fragment_ActivitiesGrid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_activities_grid, container, false);

        ImageButton ActivitiesButton = view.findViewById(R.id.imgBtn_Activities);
        ActivitiesButton.setOnClickListener(this);

        ImageButton NapButton = view.findViewById(R.id.imgBtn_Nap);
        NapButton.setOnClickListener(this);


        ImageButton academic_performance = view.findViewById(R.id.academic);
        academic_performance.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgBtn_Activities:
                Fragment ActFrag = Frag_ts_ActivityChildList.newInstance("customActivity");
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.teacher_content_holder, ActFrag).addToBackStack("TagList").commit();
                break;
            case R.id.imgBtn_Nap:
                Fragment NapFrag = Frag_ts_ActivityChildList.newInstance("Nap");
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.teacher_content_holder, NapFrag).addToBackStack("TagList").commit();
                break;
            case R.id.academic:
                Fragment Academic = new Frag_ts_Academic_Performance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.teacher_content_holder, Academic).addToBackStack("ActFrag").commit();
                break;
        }
    }
}
