package com.example.siddharthgupta.healthcare;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weka.classifiers.meta.Vote;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

public class diabetes extends AppCompatActivity implements Animation.AnimationListener {

    String[] result1;
    int i;
    String dname;
    //Toolbar toolbar;
    TextView toolbar_title;
    MyEditText fname1, fname2, fname3;
    TextInputLayout lNameLayout1, lNameLayout2, lNameLayout3;
    Drawable errorIcon;
    Button diabetes_btn;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    //    Button btnShowProgress;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    // File url to download
    private static String file_url = "https://firebasestorage.googleapis.com/v0/b/healthcare-156400.appspot.com/o/diabetes_vote_j48_ibk%5B1%5D.model?alt=media&token=a7e8ff71-6c5d-4e58-810d-a43485dac9cc";

    private Animation popupShow;
    private Animation popupHide;
    private LinearLayout linearLayoutPopup,allFieldsRequired;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] hint;// = {"No. of times Pregnant", "Plasma Glucose Concentration", "Diabetes Pedigree Function"};
    String[] inputtype;// = {"number", "number", "numberDecimal"};

    TextView disease_name,disease_accuracy;
    View llview;

    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes);
//for not showing keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar == null){
//            Log.e("toolbar is", "null");
//        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
//        setSupportActionBar(toolbar);
//         }
        // CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolber);
/*
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Diagnosis");
            getSupportActionBar().setSubtitle("");
        }
*/
        llview=findViewById(R.id.result_ll);

        dname=getIntent().getStringExtra("diseaseName");
        disease_name= (TextView) findViewById(R.id.disease_name);
        disease_name.setText(dname);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/gooddog.otf");
        disease_name.setTypeface(custom_font);
        disease_name.setTransitionName(getIntent().getStringExtra("transdname"));

        HashMap<String,Double> hmap1=new HashMap<String, Double>();
        hmap1.put("Diabetes",81.17);
        hmap1.put("Heart Disease",88.52);
        hmap1.put("Breast Cancer",97.85);
        hmap1.put("HIV",95.69);
        hmap1.put("Hepatitis",96.67);
        hmap1.put("Dermatology",100.0);
        disease_accuracy= (TextView) findViewById(R.id.disease_accuracy);
        disease_accuracy.setText("Accuracy : "+hmap1.get(dname)+"%");
      //  disease_accuracy.setTypeface(custom_font);

        HashMap<String,Integer> hmap=new HashMap<String, Integer>();
        hmap.put("Diabetes",R.drawable.disease_diabetes);
        hmap.put("Heart Disease",R.drawable.disease_heart);
        hmap.put("Breast Cancer",R.drawable.disease_breasts);
        hmap.put("HIV",R.drawable.disease_hiv);
        hmap.put("Hepatitis",R.drawable.disease_hepatitis);
        hmap.put("Dermatology",R.drawable.disease_dermatology);
        ImageView imv= (ImageView) findViewById(R.id.diseaseImageView);
        imv.setImageResource(hmap.get(dname));
        imv.setTransitionName(getIntent().getStringExtra("transimg"));

//        String toolbar_color = getIntent().getStringExtra("toolbar_color");
//        if (toolbar_color != null)
//            toolbar.setBackgroundColor(Color.parseColor(toolbar_color));
        //toolbar_title.setText("Diabetes");
        String[] diseases_names = {"Diabetes", "Heart disease", "Breast Cancer", "Lung Cancer", "HIV", "Thyroid", "Liver Disorder", "Hepatitis"};
        switch (dname) {
            case "Diabetes":
                hint=new String[6];inputtype=new String[6];
                hint[0]="No. of times pregnant";inputtype[0]="number";
                hint[1]="Plasma glucose concentration";inputtype[1]="number";
                hint[2]="Diastolic blood pressure(mm Hg)";inputtype[2]="number";
                hint[3]="2-Hour serum insulin(mu U/ml)";inputtype[3]="number";
                hint[4]="Body mass index(Kg/m^2)";inputtype[4]="number";
                hint[5]="Diabetes pedigree function";inputtype[5]="number";
                break;
            case "Heart Disease":
                hint = new String[8];
                hint[0]="Sex";
                hint[1]="Chest Pain";
                hint[2]="Serum Cholestoral(mg/dl)";
                hint[3]="Maximum heart rate achieved";
                hint[4]="Exercise induced angina";
                hint[5]="slope of the peak exercise ST segment";
                hint[6]="number of major vessels (0-3)";
                hint[7]="Thalassemia";
                inputtype=new String[8];
                inputtype[0]="alphabetic";
                inputtype[1]="alphabetic";
                inputtype[2]="number";
                inputtype[3]="number";
                inputtype[4]="alphabetic";
                inputtype[5]="alphabetic";
                inputtype[6]="number";
                inputtype[7]="alphabetic";
                break;
            case "Breast Cancer":
                hint=new String[7];
                hint[0]="Clump Thickness";
                hint[1]="Uniformity of Cell Size";
                hint[2]="Uniformity of Cell Shape";
                hint[3]="Marginal Adhesion";
                hint[4]="Bare Nuclei";
                hint[5]="Bland Chromatin";
                hint[6]="Normal Nucleoli";
                inputtype=new String[7];
                inputtype[0]="number";
                inputtype[1]="number";
                inputtype[2]="number";
                inputtype[3]="number";
                inputtype[4]="number";
                inputtype[5]="number";
                inputtype[6]="number";
                break;
            case "HIV":
                hint=new String[7];
                hint[0]="aminoacid-1";
                hint[1]="aminoacid-2";
                hint[2]="aminoacid-3";
                hint[3]="aminoacid-4";
                hint[4]="aminoacid-5";
                hint[5]="aminoacid-6";
                hint[6]="aminoacid-7";
                inputtype=new String[7];
                inputtype[0]="alphabetic";
                inputtype[1]="alphabetic";
                inputtype[2]="alphabetic";
                inputtype[3]="alphabetic";
                inputtype[4]="alphabetic";
                inputtype[5]="alphabetic";
                inputtype[6]="alphabetic";
                break;
            case "Hepatitis":
                hint=new String[2];
                hint[0]="ASCITES";
                hint[1]="BILIRUBIN";
                inputtype=new String[2];
                inputtype[0]="alphabetic";
                inputtype[1]="number";
                break;
            case "Dermatology":
                hint=new String[15];inputtype=new String[15];
                hint[0]="Erythema";inputtype[0]="number";
                hint[1]="Scaling";inputtype[1]="number";
                hint[2]="Definite borders";inputtype[2]="number";
                hint[3]="Itching";inputtype[3]="number";
                hint[4]="Koebner phenomenon";inputtype[4]="number";
                hint[5]="Follicular papules";inputtype[5]="number";
                hint[6]="Oral mucosal involvement";inputtype[6]="number";
                hint[7]="Knee and elbow involvement";inputtype[7]="number";
                hint[8]="PNL infiltrate";inputtype[8]="number";
                hint[9]="Fibrosis of the papillary dermis";inputtype[9]="number";
                hint[10]="Elongation of the rete ridges";inputtype[10]="number";
                hint[11]="Disappearance of the granular layer";inputtype[11]="number";
                hint[12]="Spongiosis";inputtype[12]="number";
                hint[13]="Saw tooth appearance of retes";inputtype[13]="number";
                hint[14]="Inflammatory monoluclear inflitrate";inputtype[14]="number";
                break;
            default:
                hint=new String[0];inputtype=new String[0];
                break;
        }

        popupShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        popupShow.setAnimationListener(this);
        popupHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);
        popupHide.setAnimationListener(this);

        linearLayoutPopup = (LinearLayout) findViewById(R.id.linearLayoutPopUp);
        linearLayoutPopup.setVisibility(View.GONE);

        allFieldsRequired= (LinearLayout) findViewById(R.id.allFieldsRequired);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.diabetes_recyclerview);
        adapter = new edittext_recycler_adapter(hint, this, inputtype,allFieldsRequired);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_actionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                 String[] result;
                 result = ((edittext_recycler_adapter) recyclerView.getAdapter()).check();


                    if (result!=null) {

                        result1=new String[result.length+1];
                        result1[0]=dname;
                        for(int j=0;j<result.length;j++){
                            result1[j+1]=result[j];
                        }
/*
                        recyclerView.setEnabled(false);
                        floatingActionButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                floatingActionButton.setVisibility(View.VISIBLE);
                                startresultanimaton();
                            }
                        },3000);
                        //asynctask
*/

                        new MyAsyncTask().execute(result1);
                    }
                } else {
                    if (linearLayoutPopup.getVisibility() == View.GONE) {
                        linearLayoutPopup.startAnimation(popupShow);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                linearLayoutPopup.startAnimation(popupHide);
                            }
                        }, 2000);
                    }
                    //else {
                    //    linearLayoutPopup.startAnimation(popupHide);
                    //}

                }
            }
        });

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            final android.transition.Transition fade=getWindow().getEnterTransition();
//            fade.addListener(new Transition.TransitionListener() {
//                @Override
//                public void onTransitionStart(Transition transition) {
//
//                }
//                @Override
//                public void onTransitionEnd(Transition transition) {
////                    floatingActionButton.setVisibility(View.VISIBLE);
//                    floatingActionButton.animate().scaleX(1f).scaleY(1f);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        fade.removeListener(this);
//                    }
//                }
//                @Override
//                public void onTransitionCancel(Transition transition) {
//
//                }
//                @Override
//                public void onTransitionPause(Transition transition) {
//
//                }
//                @Override
//                public void onTransitionResume(Transition transition) {
//
//                }
//            });
//            getWindow().setAllowEnterTransitionOverlap(false);
//            Slide slide=new Slide(Gravity.RIGHT);
//            getWindow().setReturnTransition(slide);
//            getWindow().setSharedElementEnterTransition(enterTransition());
//            getWindow().setSharedElementReturnTransition(returnTransition());
//        }

    }

    private void startresultanimaton() {
        Log.e("","inside startresultanimaton");
        int transitionId=R.transition.changebounds_with_arcmotion;
        TransitionInflater inflater=TransitionInflater.from(this);
        Transition transition=inflater.inflateTransition(transitionId);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }
            @Override
            public void onTransitionEnd(Transition transition) {
                Log.e("","transition ended");
                floatingActionButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                createRevealforView();
            }
            @Override
            public void onTransitionCancel(Transition transition) {

            }
            @Override
            public void onTransitionPause(Transition transition) {

            }
            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.root_RL);
        TransitionManager.beginDelayedTransition(viewGroup,transition);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        floatingActionButton.setLayoutParams(layoutParams);

    }

    public Animator createRevealforView(){
        Log.e("","inside createRevealforView");
        int x=(llview.getLeft()+llview.getRight())/2;
        int y=llview.getBottom();
        float finalRadius=(float)Math.max(llview.getWidth(),llview.getHeight());
        Animator anim= ViewAnimationUtils.createCircularReveal(llview,x,y,0,finalRadius);
        llview.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        anim.start();
        return anim;
    }

    private Transition enterTransition() {
        ChangeBounds bounds = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            bounds = new ChangeBounds();
            bounds.setDuration(3000);
        }


        return bounds;
    }

    private Transition returnTransition() {
        ChangeBounds bounds = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            bounds = new ChangeBounds();
            bounds.setInterpolator(new DecelerateInterpolator());
            bounds.setDuration(3000);
        }


        return bounds;
    }

    @Override
    public void onBackPressed() {
//        floatingActionButton.setVisibility(View.INVISIBLE);
       /* if (recyclerView.getVisibility()==View.GONE){
            recyclerView.setVisibility(View.VISIBLE);
            llview.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.animate().scaleX(1f).scaleY(1f);
        }*/
            floatingActionButton.animate().scaleX(0f).scaleY(0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                               // finishAfterTransition();
                            }
                        }
                    });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/*
    public void checkforOK() {
        if (lNameLayout1.isErrorEnabled() || lNameLayout2.isErrorEnabled() || lNameLayout3.isErrorEnabled()
                || fname1.getText().toString().isEmpty() || fname2.getText().toString().isEmpty() || fname3.getText().toString().isEmpty()) {
            diabetes_btn.setEnabled(false);
        } else {
            diabetes_btn.setEnabled(true);
        }
    }

    public void getOutputForDiabetes() {
        try {
            String[] out = {"tested positive", "tested negetive"};
            int input1, input2;
            double input3;
            input1 = Integer.parseInt(fname1.getText().toString());
            input2 = Integer.parseInt(fname2.getText().toString());
            input3 = Double.parseDouble(fname3.getText().toString());
            Vote vote = (Vote) SerializationHelper.read(Environment.getExternalStorageDirectory().getPath() + "/diabetes_file.model");
            Instance instance = new Instance(4);
            ConverterUtils.DataSource dataSource = new ConverterUtils.DataSource(Environment.getExternalStorageDirectory().getPath() + "/diabetesTest.arff");
            Instances instances = dataSource.getDataSet();
            Log.e("aaa", "aaa");
            instance.setValue(instances.attribute(0), input1);
            instance.setValue(instances.attribute(1), input2);
            instance.setValue(instances.attribute(2), input3);
            instance.setValue(instances.attribute(3), 0);
            Log.e("aaa", "bbb");
            instance.setDataset(instances);
            Log.e("aaa", "ccc");
            Log.e("vote = ", vote.toString());
            double pred = -1;
            try {
                pred = vote.classifyInstance(instance);
            } catch (Exception ex) {
                Log.e("Error = ", ex.getMessage());
            }//Log.e("path",Environment.getExternalStorageDirectory().getPath());
            Log.e("predicted class = ", "" + pred);
            Toast.makeText(getApplicationContext(), "" + pred, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
*/
    /**
     * Showing Dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation.equals(popupShow)) {
            linearLayoutPopup.setVisibility(View.VISIBLE);
        } else if (animation.equals(popupHide)) {
            linearLayoutPopup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation.equals(popupShow)) {
            // buttonShowHidePopup.setText(getString(R.string.btn_hide_txt));
        } else if (animation.equals(popupHide)) {
            // buttonShowHidePopup.setText(getString(R.string.btn_show_txt));
        }
    }

    boolean checkConnection() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] mnetworks = connectivitymanager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network netInfo : mnetworks) {
                networkInfo = connectivitymanager.getNetworkInfo(netInfo);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }

            }
        } else {
            if (connectivitymanager != null) {
                NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();
                for (NetworkInfo netInfo : networkInfo) {

                    if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                        if (netInfo.isConnected()) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            recyclerView.setEnabled(false);
            floatingActionButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
           // String s=postData(params);
            //return s;
            String[] valueIWantToSend=params;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://myhealthbuddy1.cloud.cms500.com/HBC/AndroidServlet");//"http://10.0.2.2:8080/ServletParams/AndroidServlet");
            String origresponseText="";
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for(int i=0;i<hint.length+1;i++){

                    nameValuePairs.add(new BasicNameValuePair("param"+i,valueIWantToSend[i]));
                }
//                nameValuePairs.add(new BasicNameValuePair("param1",valueIWantToSend[0]));
//                nameValuePairs.add(new BasicNameValuePair("param2", valueIWantToSend[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
         /* execute */
                ResponseHandler<String> rh=new BasicResponseHandler();
                String response = httpclient.execute(httppost,rh);
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity rp = response.getEntity();
                //String response=httpclient.execute(httppost);
                origresponseText=response;//readContent(response);
                //Log.e("aaa",rp.toString());
            }
            catch (ClientProtocolException e) {}
            catch (IOException e) {}
            String responseText = origresponseText.substring(0, origresponseText.length());
            return responseText;

        }
        @Override
        protected void onPostExecute(String result){

            final String r=result;
            if (r.contains("Invalid input")){
                floatingActionButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                dialog=new Dialog(diabetes.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Invalid input for");
                TextView alerttv= (TextView) dialog.findViewById(R.id.alertTextView);
                alerttv.setText(r.substring(18,r.length()));
                dialog.show();
            }
            else if (r.contains(">")||r.contains("<")){
                progressBar.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Servers might be closed or connectivity problem", Toast.LENGTH_LONG).show();
            }
            else {
/*
                final int max=1024;
                for(i=0;i<max;i++){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i*100/max);
                        }
                    },200);
//                    publishProgress(i*100/max);
                }*/
                LinearLayout final_result_ll= (LinearLayout) findViewById(R.id.final_result_ll);
                for(int i=1;i<result1.length;i++){
                    TextView tv=new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tv.setLayoutParams(params);
                    tv.setText(hint[i-1]+" : "+result1[i]);
                    tv.setTextSize(15);
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(15,5,15,5);
                    final_result_ll.addView(tv);
                }
                ObjectAnimator progressAnimator=ObjectAnimator.ofInt(progressBar,"progress",0,100);
                progressAnimator.setDuration(3000);
                progressAnimator.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButton.setVisibility(View.VISIBLE);
                        startresultanimaton();
                        TextView tv = (TextView) findViewById(R.id.returned_result);
                        tv.setText(r);

                    }
                },3000);


                //Toast.makeText(getApplicationContext(),"pro ="+progressBar.getProgress(), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... progress){
           // for(int i=0;i<progress.length;i++)
            super.onProgressUpdate(progress);
                progressBar.setProgress(progress[0]);
           // Log.e("progress = ",progress[0]);
        }

        public String postData(String valueIWantToSend[]) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://healthbudd.cloud.cms500/HBC/AndroidServlet");//"http://10.0.2.2:8080/ServletParams/AndroidServlet");
            String origresponseText="";
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for(int i=0;i<hint.length;i++){
                    nameValuePairs.add(new BasicNameValuePair("param"+(i+1),valueIWantToSend[i]));
                }
//                nameValuePairs.add(new BasicNameValuePair("param1",valueIWantToSend[0]));
//                nameValuePairs.add(new BasicNameValuePair("param2", valueIWantToSend[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
         /* execute */
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity rp = response.getEntity();

                origresponseText=readContent(response);
                Log.e("aaa",rp.toString());
            }
            catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
            }
            String responseText = origresponseText;//.substring(7, origresponseText.length());
            return responseText;
        }

        String readContent(HttpResponse response) {
            String text = "";
            InputStream in =null;

            try {
                in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                text = sb.toString();
            }
            catch (IllegalStateException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            finally {
                try {in.close();}
                catch (Exception ex) {}
            }

            return text;
        }


    }



    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * Background Async Task to download file
     */

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 10000);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/diabetes_file.model");

                byte data[] = new byte[10000];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard/HealthCare
//            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
            Toast.makeText(getApplicationContext(), "downloaded", Toast.LENGTH_SHORT).show();
        }

    }

}
