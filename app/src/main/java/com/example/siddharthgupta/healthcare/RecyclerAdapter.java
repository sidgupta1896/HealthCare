package com.example.siddharthgupta.healthcare;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by siddharth gupta on 12-04-2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    String[] disease_names;
    int[] disease_img;
    Context context;
    String[] color_code={"#ff5079","#5079ff","#ffd750","#ff7f50","#bf8bff","#120A8F"};
    String[] i_color;
    public RecyclerAdapter(String[] disease_names, Context context,int[] disease_img){
        this.disease_names = disease_names;
        this.context=context;
        this.disease_img=disease_img;
        i_color=new String[disease_names.length];
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_view,parent,false);
//        for(int i=0;i<disease_names.length;i++){
//            String c=color_code[(int)(Math.random()*(color_code.length-1))];
//            i_color[i]=c;
//        }
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view,disease_names,context,i_color,parent);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv.setText(disease_names[position]);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),"fonts/gooddog.otf");
        holder.tv.setTypeface(custom_font);
        holder.tv.setTransitionName(position+"text");
        holder.iv.setImageResource(disease_img[position]);
        holder.iv.setTransitionName(position+"img");
    //    holder.rl_dname.setBackgroundColor(Color.parseColor(i_color[position]));

    }

    @Override
    public int getItemCount() {
        return disease_names.length;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv;
        ImageView iv;
//        RelativeLayout rl_dname;
        String[] disease_names;
        Context context;
        String[] i_color;
        ViewGroup parent;

        private Boolean isFabOpen = false;
        private FloatingActionButton fab,fab_contacts,fab_heartbeat;
        private Animation fab_open,fab_close,rotate_forward,rotate_backward;

        public RecyclerViewHolder(View itemView,String[] disease_names, Context context,String[] i_color,ViewGroup parent) {
            super(itemView);
            this.disease_names = disease_names;
            this.i_color=i_color;
            this.parent=parent;
            this.context=context;
            itemView.setOnClickListener(this);
            tv= (TextView) itemView.findViewById(R.id.disease_names);
            iv= (ImageView) itemView.findViewById(R.id.diseaseImageView);
  //          rl_dname= (RelativeLayout) itemView.findViewById(R.id.rl_dname);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Intent intent;
           /* if(pos==0){
                intent=new Intent(this.context,Emergency_SMS.class);
               // this.context.startActivity(intent);
            } else {*/
        //    setFABproperties(v);
         //   fab.animate().scaleX(0f).scaleY(0f);

/*
            intent=new Intent(this.context,InputOutputActivity.class);
            this.context.startActivity(intent);
*/


                intent=new Intent(this.context,diabetes.class);
              //  intent.putExtra("toolbar_color", i_color[pos]);
                intent.putExtra("diseaseName",disease_names[pos]);
            intent.putExtra("transimg",pos+"img");
            intent.putExtra("transdname",pos+"text");
                View view= LayoutInflater.from(context).inflate(R.layout.home_row_view,parent,false);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Pair<View,String> p1=Pair.create((View)view.findViewById(R.id.rl_dname),pos+"text");
                Pair<View,String> p2=Pair.create((View)view.findViewById(R.id.diseaseImageView),pos+"img");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) this.context,p1,p2);
//                ActivityOptions.makeSceneTransitionAnimation((Activity) this.context,p1,p2);
                this.context.startActivity(intent,options.toBundle());
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
               // Pair<View,String> p1=Pair.create((View)view.findViewById(R.id.rl_dname),"MyDiseaseName");
               // Pair<View,String> p2=Pair.create((View)view.findViewById(R.id.diseaseImageView),"MyDiseaseImage");
               // ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) this.context,p1);
                //this.context.startActivity(intent,optionsCompat.toBundle());
            }
            else{
                this.context.startActivity(intent);
            }


        }

        private void setFABproperties(View v) {

            fab= (FloatingActionButton) v.findViewById(R.id.fab);
            fab_contacts= (FloatingActionButton) v.findViewById(R.id.fab_contacts);
            fab_heartbeat= (FloatingActionButton) v.findViewById(R.id.fab_heartbeat);
            fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
            fab_close = AnimationUtils.loadAnimation(context,R.anim.fab_close);
            rotate_forward = AnimationUtils.loadAnimation(context,R.anim.rotate_forward);
            rotate_backward = AnimationUtils.loadAnimation(context,R.anim.rotate_backward);
            if (fab_contacts.isClickable()){
                isFabOpen=true;
                animateFAB();
            }
           /*
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateFAB();
                }
            });
            fab_contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fab.animate().scaleX(0f).scaleY(0f);
                    fab_contacts.animate().scaleX(0f).scaleY(0f);
                    fab_heartbeat.animate().scaleX(0f).scaleY(0f);
                    Intent intent=new Intent(context,Emergency_SMS.class)
                            .putExtra("activityFROM","main2");
                    context.startActivity(intent);
                }
            });
            fab_heartbeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            */
        }

        public void animateFAB() {

            if (isFabOpen) {

                fab.startAnimation(rotate_backward);
                fab_contacts.startAnimation(fab_close);
                fab_heartbeat.startAnimation(fab_close);
                fab_contacts.setClickable(false);
                fab_heartbeat.setClickable(false);
                isFabOpen = false;

            }/* else {

                fab.startAnimation(rotate_forward);
                fab_contacts.startAnimation(fab_open);
                fab_heartbeat.startAnimation(fab_open);
                fab_contacts.setClickable(true);
                fab_heartbeat.setClickable(true);
                isFabOpen = true;

            }*/
        }
    }
}
