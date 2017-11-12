package com.example.siddharthgupta.healthcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class InputOutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_output);
        CardView bcv= (CardView) findViewById(R.id.backcardView);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.cardview_rotate);
        anim.setRepeatCount(Animation.INFINITE);
        bcv.startAnimation(anim);
    }
}
