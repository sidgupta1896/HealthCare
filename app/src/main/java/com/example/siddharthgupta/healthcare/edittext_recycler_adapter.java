package com.example.siddharthgupta.healthcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by siddharth gupta on 13-04-2017.
 */
public class edittext_recycler_adapter extends RecyclerView.Adapter<edittext_recycler_adapter.EdittextRecyclerViewHolder> {
    String[] hint,inputtype;
    float[] edittextValues;
    String[] edittextValuesString;
    Context context;
    boolean checkok;
    Drawable errorIcon;

    private Animation popupShow;
    private Animation popupHide;
    private LinearLayout linearLayoutPopup;

    edittext_recycler_adapter(String[] hint,Context context,String[] inputtype,LinearLayout linearLayoutPopup){
        this.hint=hint;
        this.context=context;
        this.inputtype=inputtype;
        this.linearLayoutPopup=linearLayoutPopup;
        checkok=false;
        edittextValues=new float[hint.length];
        edittextValuesString=new String[hint.length];
   //     errorIcon = ContextCompat.getDrawable(context,R.drawable.ic_error_red_48dp);
    //    errorIcon.setBounds(new Rect(0, 0, 50, 50));
        for (int i=0;i<hint.length;i++){
            edittextValues[i]=-1;
            edittextValuesString[i]="";
        }
    }

    @Override
    public EdittextRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_text_view,parent,false);
       EdittextRecyclerViewHolder edittextRecyclerViewHolder=new EdittextRecyclerViewHolder(view);
        return edittextRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final EdittextRecyclerViewHolder holder,final int position) {
        holder.textInputLayout.setHint(hint[position]);
        ///holder.hint_text_view.setText(hint[position]);
        int i = 0;
       // if (inputtype[position].equalsIgnoreCase("number"))
            i=InputType.TYPE_CLASS_NUMBER;
       // else if (inputtype[position].equalsIgnoreCase("numberDecimal"))i=InputType.TYPE_NUMBER_FLAG_DECIMAL;
//        holder.myEditText.setInputType(i);

        holder.correct_wrong_signal.setImageResource(R.drawable.wrong_signal);

        holder.myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (inputtype[position].equalsIgnoreCase("number")){
                    if (holder.myEditText.getText().toString().matches("[0-9]+(.?[0-9]+)?")){
                        holder.correct_wrong_signal.setImageResource(R.drawable.correctsignal64);
                        if (holder.myEditText.getText().toString().isEmpty()) {
                            edittextValues[position]=-1;
                            edittextValuesString[position]="";
                        }
                        else {
                            edittextValues[position]=Float.parseFloat(holder.myEditText.getText().toString());
                            edittextValuesString[position]= holder.myEditText.getText().toString();//String.valueOf(edittextValues[holder.getAdapterPosition()]);
                        }
                    }
                    else{
                        holder.correct_wrong_signal.setImageResource(R.drawable.wrong_signal);
                        edittextValues[position]=-1;
                        edittextValuesString[position]="";
                    }
                }
                else if (inputtype[holder.getAdapterPosition()].equalsIgnoreCase("alphabetic")){
                    if (holder.myEditText.getText().toString().matches("[a-zA-Z_]+")){
                        holder.correct_wrong_signal.setImageResource(R.drawable.correctsignal64);
                        edittextValuesString[position]=holder.myEditText.getText().toString();
                    }
                    else{
                        holder.correct_wrong_signal.setImageResource(R.drawable.wrong_signal);
                        edittextValuesString[position]="";
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /*if (!hasFocus) {
                    if (holder.myEditText.getText().toString().isEmpty()) {

                        holder.textInputLayout.setErrorEnabled(true);
                        holder.textInputLayout.setError("Required");
                        holder.myEditText.setError("", errorIcon);
                    } else {
                        holder.textInputLayout.setErrorEnabled(false);
                        holder.myEditText.setError("", null);
                    }

                }*/
                //checkforOK();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hint.length;
    }

    public String[] check(){
        int i;
       // SharedPreferences sharedPreferences=context.getSharedPreferences("edittextdetails", context.MODE_PRIVATE);
        for (i=0;i<hint.length;i++){
         //   edittextValues[i]=sharedPreferences.getFloat(hint[i],-1);
            if(edittextValuesString[i].equalsIgnoreCase("")){
                checkok=false;
                break;
            }
        }
        if(i==hint.length)checkok=true;

        if (checkok){
            //every thing is fine , proceed further
            ArrayList<String> result=new ArrayList<String>();
            for(i=0;i<hint.length;i++){
                result.add(edittextValuesString[i]);
            }
            return edittextValuesString;
            //Toast.makeText(context,"all ok",Toast.LENGTH_LONG).show();
        }
        else{
            popupShow = AnimationUtils.loadAnimation(context, R.anim.popup_show);
            popupShow.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    linearLayoutPopup.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            popupHide = AnimationUtils.loadAnimation(context, R.anim.popup_hide);
            popupHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    linearLayoutPopup.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (linearLayoutPopup.getVisibility() == View.GONE) {
                linearLayoutPopup.startAnimation(popupShow);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutPopup.startAnimation(popupHide);
                    }
                }, 2000);
            }
            return null;
//            Toast.makeText(context,"all not ok",Toast.LENGTH_LONG).show();
        }
    }

    public static class EdittextRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText myEditText;
        TextInputLayout textInputLayout;
        ImageView correct_wrong_signal;
        public EdittextRecyclerViewHolder(View itemView) {
            super(itemView);
            myEditText= (TextInputEditText) itemView.findViewById(R.id.my_edit_text);
            textInputLayout= (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
            correct_wrong_signal= (ImageView) itemView.findViewById(R.id.correct_wrong_signal);
           // hint_text_view= (TextView) itemView.findViewById(R.id.hint_text_view);
        }
    }
}
