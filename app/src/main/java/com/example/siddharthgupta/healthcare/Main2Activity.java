package com.example.siddharthgupta.healthcare;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Main2Activity extends AppCompatActivity {
    Toolbar toolbar;TextView toolbar_title;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String name,email,imgurl;
    private GoogleApiClient mGoogleApiClient;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab_contacts,fab_heartbeat;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    View fragview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Healthcare");
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setTitle("Health Care");
     //   getSupportActionBar().setSubtitle("");



//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Explode explode=new Explode();
//            //explode.excludeTarget(R.id.toolbar,true);
//            getWindow().setExitTransition(explode);
//        }
//        getWindow().setSharedElementEnterTransition(enterTransition());
//        getWindow().setSharedElementReturnTransition(returnTransition());
/*
        FloatingActionButton add_contact_fab= (FloatingActionButton) findViewById(R.id.add_contact_fab);
        add_contact_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Emergency_SMS.class)
                        .putExtra("activityFROM","main2");
                startActivity(intent);
            }
        });
*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();

        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        //navigationView.addHeaderView();

        //View vi = LayoutInflater.from(getBaseContext()).inflate(R.layout.navigationview_header,null);

        SharedPreferences sharedPreferences=Main2Activity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        if(getIntent().getStringExtra("name").equalsIgnoreCase("")){
            name   = sharedPreferences.getString(getString(R.string.PROFILE_NAME),"apka naam");
            email  = sharedPreferences.getString(getString(R.string.PROFILE_EMAIL),"apka email");
            imgurl = sharedPreferences.getString(getString(R.string.PROFILE_URL),"");
        }
        else{
            name=getIntent().getStringExtra("name");
            email=getIntent().getStringExtra("email");
            imgurl=getIntent().getStringExtra("imgurl");
           // editor.clear();
           // editor.commit();
            //editor=sharedPreferences.edit();
            editor.putString(getString(R.string.PROFILE_NAME),name);
            editor.putString(getString(R.string.PROFILE_EMAIL),email);
            editor.putString(getString(R.string.PROFILE_URL),imgurl);
            editor.commit();


        }


        Log.e("shared name",sharedPreferences.getString(getString(R.string.PROFILE_NAME),"apka naam"));

        View navigationViewHeaderView = navigationView.getHeaderView(0);
        TextView ntv= (TextView) navigationViewHeaderView.findViewById(R.id.username);
        ntv.setText(name);
        TextView etv= (TextView) navigationViewHeaderView.findViewById(R.id.useremail);
        etv.setText(email);
        CircularImageView civ = (CircularImageView) navigationViewHeaderView.findViewById(R.id.profileimg);
        if(!imgurl.equalsIgnoreCase("")) {
            Glide.with(this).load(imgurl).into(civ);
        }
        else{
            civ.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.man_navigation));
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_id:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
                        fragview=findViewById(R.id.main_container);
                        int x=fragview.getRight();
                        int y=fragview.getBottom();
                        float finalRadius=(float)Math.max(fragview.getWidth(),fragview.getHeight());
                        Animator anim= ViewAnimationUtils.createCircularReveal(fragview,x,y,0,finalRadius);
                        drawerLayout.closeDrawers();
                        fragmentTransaction.commit();
                        anim.start();
                        getSupportActionBar().setTitle("Health Care");
                        item.setChecked(true);

                        break;
                    case R.id.about_id:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new AboutFragment());
                        fragview=findViewById(R.id.main_container);
                        x=fragview.getRight();
                        y=fragview.getBottom();
                        finalRadius=(float)Math.max(fragview.getWidth(),fragview.getHeight());
                         anim= ViewAnimationUtils.createCircularReveal(fragview,x,y,0,finalRadius);
                        drawerLayout.closeDrawers();
                        anim.start();
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Health Care");
                        item.setChecked(true);

                        break;
                    case R.id.valid_input_list_id:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new ValidInputFragment());
                        fragview=findViewById(R.id.main_container);
                        x=fragview.getRight();
                        y=fragview.getBottom();
                        finalRadius=(float)Math.max(fragview.getWidth(),fragview.getHeight());
                        anim= ViewAnimationUtils.createCircularReveal(fragview,x,y,0,finalRadius);
                        drawerLayout.closeDrawers();
                        anim.start();
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Health Care");
                        item.setChecked(true);

                        break;
                    case R.id.logout_id:
                        mAuth.signOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        break;
                }
                return true;
            }
        });

        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(Main2Activity.this,LoginActivity.class));
                    finish();
                }
            }
        };
        setFABproperties();
        fab.animate().scaleX(1f).scaleY(1f);

    }

    private void setFABproperties() {
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab_contacts= (FloatingActionButton) findViewById(R.id.fab_contacts);
        fab_heartbeat= (FloatingActionButton) findViewById(R.id.fab_heartbeat);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });
        fab_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                fab.animate().scaleX(0f).scaleY(0f);
                fab_contacts.animate().scaleX(0f).scaleY(0f);
                fab_heartbeat.animate().scaleX(0f).scaleY(0f);*/
                animateFAB();
                Intent intent=new Intent(getApplicationContext(),Emergency_SMS.class)
                        .putExtra("activityFROM","main2");
                startActivity(intent);
            }
        });
        fab_heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
                Intent intent=new Intent(getApplicationContext(),HeartRateMonitor.class);
                startActivity(intent);
            }
        });
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab_contacts.startAnimation(fab_close);
            fab_heartbeat.startAnimation(fab_close);
            fab_contacts.setClickable(false);
            fab_heartbeat.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab_contacts.startAnimation(fab_open);
            fab_heartbeat.startAnimation(fab_open);
            fab_contacts.setClickable(true);
            fab_heartbeat.setClickable(true);
            isFabOpen = true;

        }
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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isFabOpen=true;
//        animateFAB();
        fab.animate().scaleY(1f).scaleX(1f);
    }
}
