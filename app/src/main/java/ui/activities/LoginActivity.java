package ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.ResetPasswordActivity;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.response.ResponseLogin;
import com.example.anonymous.catering.response.ResponseLoginDriver;
import com.example.anonymous.catering.response.ResponseLoginMember;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;
import com.example.anonymous.catering.utils.SessionManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    String fbId = "",fbemail="";
    private EditText inputEmail, inputPassword;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    LoginManager loginManager;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private SignInButton loginGoogleButton;
    private LoginButton loginFbButton;
    private CallbackManager mCallbackManager;
    private TextView tv_name;

    SessionManager sessionManager;
    ApiInterface apiService;

    String email, password;

    public final String TAG = LoginActivity.class.getSimpleName();
    public final int REQUEST_PERMISSION = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance

        // set the view now
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        loginFbButton = (LoginButton) findViewById(R.id.loginFbButton);
        loginGoogleButton = (SignInButton) findViewById(R.id.googleBtn);
        //tv_name = (TextView) findViewById(R.id.name_google);
        findViewById(R.id.googleBtn).setOnClickListener((View.OnClickListener) LoginActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.com_facebook_tooltip_default))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        apiService      = ApiClient.getClient(ServerConfig.API_ENDPOINT).create(ApiInterface.class);
        sessionManager  = new SessionManager(this);

        // Status Bar Color
        getWindow().setStatusBarColor(getColor(R.color.backgroundwhite));

        // Login FB
        loginFbButton.setReadPermissions("email");

        loginFbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        loginFbButton.setReadPermissions("email");
        loginFbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG,"Hello"+loginResult.getAccessToken().getToken());
                //  Toast.makeText(MainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //name = user.getDisplayName();
                    Toast.makeText(LoginActivity.this, "" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                    goMainScreen();
                }else {
                    Toast.makeText(LoginActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
                }

            }
        };

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        if (sessionManager.isLoggedIn()){
            Intent i = new Intent(getApplicationContext(), MainTabActivity.class);
            //
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logindriver();
            }
        });

    }
    //membercatering ketika menekan button login
    private void logindriver() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        Log.d(TAG, "loginmember: "+email+ " "+password);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        /*
        -memanggil response member login yang ada di Interface ApiInterface
        -responseLoginMember berasal dari folder Response
         */
        apiService.driverLogin(email, password).enqueue(new Callback<ResponseLoginDriver>() {
            @Override
            public void onResponse(Call<ResponseLoginDriver> call, Response<ResponseLoginDriver> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponses : Dapat terhubung ke server");
                    Log.d(TAG, "onResponses : " + response.body().toString());
                    if (response.body().getCode().equals(200)) {
                        Integer id_driver = response.body().getData().getIdDriver();
                        String nama = response.body().getData().getNama();
                        String jk = response.body().getData().getJk();
                        String no_hp = response.body().getData().getNoHp();
                        String email = response.body().getData().getEmail();
                        String password = response.body().getData().getPassword();
                        String username = response.body().getData().getUsername();
                        String alamat = response.body().getData().getAlamat();
                        System.out.println("Response JK : "+response.body().getData().getJk());
                        //menyimpan session member yang berhasil login
                        sessionManager.createLoginSessionDriver(id_driver,username, email, password, nama, no_hp, jk, alamat);

                        Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_LONG).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal login"+response.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseLoginDriver> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal konek ke server", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });

        //authenticate user
//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
//                                if (!task.isSuccessful()) {
//                                    // there was an error
//                                    if (password.length() < 6) {
//                                        inputPassword.setError(getString(R.string.minimum_password));
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        });
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginFbButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginFbButton.setVisibility(View.GONE);
            }
        });
    }

    public void onClick(View v) {
        int i = v.getId();

        if (i==R.id.googleBtn){
            signIn();
        }

    }

    //sign method for google
    public void signIn(){
        Intent intent =  Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // condision sign for google
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }

    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthGoogle:"+acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Toast.makeText(LoginActivity.this, ""+credential.getProvider(), Toast.LENGTH_LONG).show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "SignInWitCredential:onComplete:"+task.isSuccessful());
                        String name = getData();

                        if (task.isSuccessful()){
                            loginGoogleButton.setVisibility(View.GONE);
                            //tv_name.setText("Welcome"+name);

                        }else{
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public String getData(){
        String name = null;
        String uid = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            // Name, Email address, andd Profile photoUrl
            name = user.getEmail();
            // String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        return name;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}