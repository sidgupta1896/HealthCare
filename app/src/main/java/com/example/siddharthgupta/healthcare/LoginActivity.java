package com.example.siddharthgupta.healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SignInButton mGooglebtn;
    private static final int RC_SIGN_IN=100;
    private  GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final String TAG="Login Activity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    String name="",email="",imgurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences("ContactDetails",MODE_PRIVATE);
        final int no_contact_added=sharedPreferences.getInt("NoContacts",0);
        Log.e("NOContacts : ",""+no_contact_added);

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    if(no_contact_added>0) {
                        startActivity(
                                new Intent(LoginActivity.this, Main2Activity.class)
                                .putExtra("name", name)
                                .putExtra("email", email)
                                .putExtra("imgurl", imgurl)
                        );
                        finish();
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, Emergency_SMS.class)
                                .putExtra("name", name)
                                .putExtra("email", email)
                                .putExtra("imgurl", imgurl)
                                .putExtra("activityFROM","Login")
                        );
                        finish();
                    }
                }
            }
        };

        mGooglebtn= (SignInButton) findViewById(R.id.googleloginbtn);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGooglebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                getAndSetDetails(account);
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "could not login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getAndSetDetails(GoogleSignInAccount account) {/*
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.navigationview_header,null);*/
        //View vi = LayoutInflater.from(getBaseContext()).inflate(R.layout.navigationview_header,null);

//        SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
 //       SharedPreferences.Editor editor=sharedPreferences.edit();
        name=account.getDisplayName();

        //TextView ntv=(TextView)vi.findViewById(R.id.username);
        //ntv.setText(name);
        Log.e("name=",name);
        email=account.getEmail();

        //TextView etv=(TextView)vi.findViewById(R.id.useremail);
        //etv.setText(email);
        Log.e("email=",email);
        try {
            imgurl = account.getPhotoUrl().toString();

          //  CircularImageView civ = (CircularImageView) vi.findViewById(R.id.profileimg);
          //  Glide.with(this).load(imgurl).into(civ);
        }catch (NullPointerException e){
            Toast.makeText(this,"no profile pic",Toast.LENGTH_LONG).show();
            //editor.putString(getString(R.string.PROFILE_URL),"");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "could not login", Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void finish() {
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    */
}
