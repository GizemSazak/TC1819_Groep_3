package com.example.loginsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    static GoogleSignInClient mGoogleSignInClient;
    MyTechlab myDb;
    boolean HrEmail;
    View FindEmail;
    static String Email;
    View FindPassword;
    View FindText;
    static boolean logged_in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FindPassword = (EditText) findViewById(R.id.Password);
        FindPassword.setVisibility(View.INVISIBLE);
        FindPassword.setVisibility(View.GONE);
        FindText = (TextView) findViewById(R.id.textView4);
        FindText.setVisibility(View.INVISIBLE);
        FindText.setVisibility(View.GONE);

        /* Initializing Views*/
        signInButton = findViewById(R.id.sign_in_button);

        /* Configure sign-in to request the user's ID, email address, and basic*/
        /* profile. ID and basic profile are included in DEFAULT_SIGN_IN.*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.*/
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        myDb = new MyTechlab(this);
        myDb.insertData("techlabapp00@gmail.com", "test123", "Admin");

        myDb.getWritableDatabase();
    }

   public void inloggen(View view) {
       myDb = new MyTechlab(this);
       FindEmail = (EditText) findViewById(R.id.email);
       Email = ((EditText) FindEmail).getText().toString();
       String Password = ((EditText) FindPassword).getText().toString();
       String Admin = "Admin";
       Boolean Email_Password = myDb.Email_Password(Email, Password);
       Boolean AdminTrue = myDb.Rol(Email ,Admin);
       if (Email.endsWith("@hr.nl")) {
           Toast.makeText(LoginActivity.this, "Kies uw Hogeschool email", Toast.LENGTH_SHORT).show();
           signIn();
       } else if (AdminTrue) {
           FindPassword.setVisibility(View.VISIBLE);
           FindText.setVisibility(View.VISIBLE);
           ((Button)findViewById(R.id.button)).setText(getString(R.string.inloggen));
           if (Email_Password){
               startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
           } else if (!Password.equals("")){
               Toast.makeText(LoginActivity.this, "Fout wachtwoord", Toast.LENGTH_SHORT).show();
           }
       } else{
           Toast.makeText(LoginActivity.this, "Log in met uw Hogeschool email", Toast.LENGTH_SHORT).show();
       }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);*/
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {//check Hr account or not
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String personEmail = account.getEmail();
            if (personEmail != null && personEmail.endsWith("@hr.nl")) {
                HrEmail = true;
                /* Signed in successfully, show authenticated UI. */
                startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Je moet met jouw HR account inloggen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                mGoogleSignInClient.signOut();
            }

        } catch (ApiException e) {
            /* The ApiException status code indicates the detailed failure reason. */
            /* Please refer to the GoogleSignInStatusCodes class reference for more information.*/
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        /* Check for existing Google Google Sign In account, if the user is already signed in
        the GoogleSignInAccount will be non-null
         */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && logged_in) {
//        if (account != null) {
            startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
        }
        super.onStart();
    }
}
