package com.example.siddharthgupta.healthcare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ValidInputFragment extends Fragment {

    View v;
    public ValidInputFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_valid_input, container, false);
        TextView vitv= (TextView) v.findViewById(R.id.validinputdetails);
        String str="<h7><b>DIABETES:</b></h7>" +
                "<br>\t\u2022No. of times pregnant :0 to 17<br>\t\u2022Plasma glucose concentration :0 to 199<br>\t\u2022Diastolic blood pressure :0 to 122" +
                "<br>\t\u2022Serum insulin :0 to 846<br>\t\u2022Body mass index : 0 to 67.1<br>\t\u2022Diabetes pedigree function :0.078 to 2.42" +
                "<br><br><h7><b>HIV:</b></h7>" +
                "<br>\t\u2022For all input fields : A, R, D, N, C, E, O, G, H, I, L, K,\t\t M, F, P, Q, S, T, W, Y, V" +
                "<br><br><h7><b>HEART DISEASE:</b></h7>" +
                "<br>\t\u2022Sex :male,female<br>\t\u2022Chest Pain : typ_angina, asympt, non_anginal,\t\t atyp_angina<br>\t\u2022Serum Cholestrol :126 to 564" +
                "<br>\t\u2022Maximum Heartrate achieved :71 to 202<br>\t\u2022Exercise induced angina :no,yes<br>\t\u2022Slope of peak Exercise :up,down,flat" +
                "<br>\t\u2022no. of major vessels :0 to 3<br>\t\u2022Thalassemia : fixed_defect, normal,\t\t reversable_defect" +
                "<br><br><h7><b>BREAST CANCER:</b></h7>" +
                "<br>\t\u2022Clump thickness :1 to 10<br>\t\u2022Uniformity of cell size :1 to 10<br>\t\u2022Uniformity of cell shape :1 to 10" +
                "<br>\t\u2022Mariginal Adhesion :1 to 10<br>\t\u2022Bare nuclei :1 to 10<br>\t\u2022Bland chromatin :1 to 10<br>\t\u2022Normal Nucleoli :1 to 10" +
                "<br><br><h7><b>HEPATITIS:</b></h7>" +
                "<br>\t\u2022ASCITES :no,yes<br>\t\u2022BILIRUBIN :0.3 to 8" +
                "<br><br><h7><b>DERMATOLOGY:</b></h4>" +
                "<br>\t\u2022For all input fields :0,1,2,3";
        vitv.setText(Html.fromHtml(str));
        return v;
    }
}
