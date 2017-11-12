package com.example.siddharthgupta.healthcare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button diabities_btn,emergency_sms_btn,heart_disease_btn,breast_cancer_btn;
    View v;
    Intent intent;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] diseases_names={
            "Diabetes"
            ,"Heart Disease"
            ,"Breast Cancer"
            ,"HIV"
            ,"Hepatitis"
            ,"Dermatology"};//,"HIV","Thyroid","Hepatitis"};
    int[] disease_img={
            R.drawable.disease_diabetes
            ,R.drawable.disease_heart
            ,R.drawable.disease_breasts
            ,R.drawable.disease_hiv
            ,R.drawable.disease_hepatitis
            ,R.drawable.disease_dermatology};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.home_recycler_view);
        adapter=new RecyclerAdapter(diseases_names,getContext(),disease_img);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
