package com.example.siddharthgupta.healthcare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_about, container, false);
        TextView tv = (TextView) v.findViewById(R.id.websitelink);

        Spanned t= Html.fromHtml("<a href='http://myhealthbuddy.cloud.cms500.com//myhealthbuddy//'>HealthBuddy.com</a>");
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(t);
        return v;
    }

}
