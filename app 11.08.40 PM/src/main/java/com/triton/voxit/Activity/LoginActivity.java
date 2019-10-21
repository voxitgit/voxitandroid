package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.facebook.FacebookSdk;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.ForgotPasswordRequest;
import com.triton.voxit.model.ForgotPwData;
import com.triton.voxit.model.LoginDataRequest;
import com.triton.voxit.model.LoginDatabean;
import com.triton.voxit.model.LoginRequest;
import com.triton.voxit.model.SignUpRequest;
import com.triton.voxit.model.singupDatabeen;
import com.triton.voxit.preference.SFMApp;
import com.triton.voxit.preference.SFMPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callbackManager;
    ImageView facebookImg,googleImg;
    String currentemail;
    private GoogleSignInOptions gso;
    private ProgressDialog pDialog;

    private static GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 0;
    LinearLayout createLt;
    Button login_btn;
    TextView createTv,helpTv;
    private APIInterface apiInterface;
    private EditText user_name,etPassword;
    private SFMPreference sharedpreference;
    private String flag;
    private static final String PREFERENCES = "";
    private SharedPreferences.Editor LoginString;
    SessionManager session;
    LoginRequest loginRequest;
    ArrayList<LoginDatabean> loginRequestArrayList;
    LinearLayout forgot_lt;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage(LoginActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
       // AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        getKeyHash();

        session = new SessionManager(getApplicationContext());
//        HashMap<String, String> user = session.getUserDetails();
//        name = user.get(SessionManager.KEY_EMAIL);
        session.checkLogin();
        initUI();

    }//end of oncreate

    private void initUI(){
        facebookImg = (ImageView)findViewById(R.id.facebook_img);
        googleImg = (ImageView)findViewById(R.id.google_img);
        createLt = (LinearLayout)findViewById(R.id.createLt);
        login_btn = (Button)findViewById(R.id.login_btn);
        createTv = (TextView)findViewById(R.id.createTv);
        user_name = (EditText)findViewById(R.id.user_name_edit);
        etPassword = (EditText)findViewById(R.id.etPassword);
        forgot_lt = (LinearLayout)findViewById(R.id.forgot_lt);
        helpTv = (TextView) findViewById(R.id.help);
        helpTv.setOnClickListener(this);
        forgot_lt.setOnClickListener(this);
        createLt.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        facebookImg.setOnClickListener(this);
        login_btn.setEnabled(true);

            googleImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!App.appUtils.isNetAvailable()) {
                        alertUserP(LoginActivity.this, "Connection Error", "No Internet connection available", "OK");
                    }else {
                        GooglePlusLogin();
                    }
                }
            });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN). requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        Intent i = getIntent();
        flag = i.getStringExtra("login");
        forgot_lt.setEnabled(true);

    }
    private void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);

                try {
                    if (json != null) {
                        currentemail = json.getString("email");
                        String name = json.getString("name");
                        Log.d("email", currentemail);

//                        setProfileToView(object);
//                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");

                        //faceBookLoginTask(currentemail, name);
                        gotoPasswordPage(currentemail, name);

//                        Log.d("fullDetails", text);

//                        details_txt.setText(Html.fromHtml(text));
//                        profile.setProfileId(json.getString("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void gotoPasswordPage(String currentemail, String name){

        Intent i = new Intent(this,Registration.class);
        i.putExtra("currentemail",currentemail);
        i.putExtra("name",name);
        i.putExtra("flag", "fb");
        startActivity(i);
    }

    public void GooglePlusLogin(){

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("connectionResult" , String.valueOf(connectionResult));

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {

                String name = acct.getDisplayName();

                String currentemail = acct.getEmail();
                Log.e( "name,email",name+currentemail);

                // String url = acct.getPhotoUrl().toString();

                String id = acct.getId();

                final Uri uri = acct.getPhotoUrl();

                String sUri = null;

                if (uri != null) {

                    sUri = uri.toString();

                    Log.i( "sUri",sUri);
                }else{

                    sUri = "";
                }


                Log.e("Display Name: ",  name + currentemail + sUri + id);

                gotoPasswordPage(currentemail, name);

            } else {

                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        }else {

            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }

    }



    public void facebookClickAction(){

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
//        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        // App code
                        String f_name = "";
                        String m_name = "";
                        String l_name = "";
                        String full_name = "";
                        String profile_image = "";
                        String facebook_id = "";
                        String f_email = "";

                        if (AccessToken.getCurrentAccessToken() != null) {


                            RequestData();

                            Profile profile = Profile.getCurrentProfile();
                            if (profile != null) {
                                facebook_id = profile.getId();

                                Log.e("facebook_id", facebook_id);
                                Log.e("f_email", f_email);
                                f_name = profile.getFirstName();
                                Log.e("f_name", f_name);
                                m_name = profile.getMiddleName();
                                Log.e("m_name", m_name);
                                l_name = profile.getLastName();
                                Log.e("l_name", l_name);
                                full_name = profile.getName();
                                Log.e("full_name", full_name);
                                profile_image = profile.getProfilePictureUri(400, 400).toString();
                                Log.e("profile_image", profile_image);
                            }

                        }else{
                            showDialogMethod("Warning", "Facebook is not available at the moment, Please try other method to signup");
                        }
                    }

                    @Override
                    public void onCancel()

                    {
                        showDialogMethod("Warning", "Facebook is not available at the moment, Please try other method to signup");
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        // App code
                    }
                });
    }

    private void getKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.triton.voxit", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

//         Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.e("Google sign", "Google sign in failed");
//                // ...
//            }
//        }
//
//        if (requestCode == REQ_CODE) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleResult(result);
//        }
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);

        }
    }


    @Override
    public void onClick(View v) {

        switch ((v.getId())){
            case R.id.facebook_img:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(LoginActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                    facebookClickAction();
                }
               // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                break;

            case R.id.createLt:
                Intent i = new Intent(this,Registration.class);
                i.putExtra("flag", "R");
                startActivity(i);
                break;
            case R.id.help:
                Intent i1 = new Intent(this,HelpSupport.class);

                startActivity(i1);
                break;
            case R.id.login_btn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(LoginActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
//                if (!emailvalidtaion()) {
//                    return;
//                }
                    
                    if (!vaildPhoneNo()) {

                        return;
                    }
                if (!vaildPassword()){
                   return;
                 }

                    loginIntegration();
                }
//                Intent i1 = new Intent(this,Dashboard.class);
//                startActivity(i1);
                break;
            case R.id.forgot_lt:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(LoginActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                if(user_name.getText().toString().equals("")){
                    showDialogMethod("Warning", getString(R.string.forget_alert));
                }else {

                        gotoForgetPw();
                    }
                }

                break;
        }
    }
    public void alertUserP(Context context, String title, String msg, String btn) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title).setCancelable(false)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean vaildPassword() {

        if (etPassword.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_Password));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        } else {
            etPassword.setError(null);
        }

        return true;
    }

    public boolean vaildPhoneNo() {
        String MobilePattern = "^[+]?[0-9]{10}$";
        String s = user_name.getText().toString().trim();

        if (s.matches(MobilePattern)) {
            if (s.isEmpty()) {
                // first_name_layout.setError(getString(R.string.err_msg_first_name));
                // requestFocus(first_name);
                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                // alertDialog.setTitle(title);
                alertDialog.setMessage(getString(R.string.err_msg_phone_number));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                alertDialog.cancel();
                            }
                        });
                alertDialog.show();

                return false;
            } else if (s.length() < 10) {
                // mobileLayout.setError(getString(R.string.err_msg_mobileno));
                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                // alertDialog.setTitle(title);
                alertDialog.setMessage(getString(R.string.err_msg_phone_number_no_worng));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                alertDialog.cancel();
                            }
                        });
                alertDialog.show();

                return false;
            } else if (!s.matches(MobilePattern)) {
                //mobileLayout.setError(getString(R.string.err_msg_mobileno));
                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                // alertDialog.setTitle(title);
                alertDialog.setMessage(getString(R.string.err_msg_phone_number_worng));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                alertDialog.cancel();
                            }
                        });
                alertDialog.show();

                return false;
            } else {
                user_name.setError(null);
            }
        }else{
                boolean valid = true;
                String emailPattern = "^[_A-Za-z0-9-\\+]+()+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";
                if (s.length() == 0 || s.isEmpty()) {
                    // accountEmailAddrLayout.setError(getString(R.string.err_msg_acc_emailid_empty));
                    final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    // alertDialog.setTitle(title);
                    alertDialog.setMessage(getString(R.string.err_msg_e_mail));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish();
                                    alertDialog.cancel();
                                }
                            });
                    alertDialog.show();
                    valid = false;
                } else if (!s.matches(emailPattern)) {
                    //accountEmailAddrLayout.setError(getString(R.string.err_msg_acc_emailid));
                    final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    // alertDialog.setTitle(title);
                    alertDialog.setMessage(getString(R.string.err_msg_e_mail_worng));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish();
                                    alertDialog.cancel();
                                }
                            });
                    alertDialog.show();
                    valid = false;
                } else {
                    user_name.setError(null);
                }
            return valid;
            }

        return true;
    }

    private boolean emailvalidtaion() {
        boolean valid = true;
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";
        // String emailPattern = "^([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})?$";
        String s = user_name.getText().toString().trim();

        if (s.length() == 0 || s.isEmpty()) {
            // accountEmailAddrLayout.setError(getString(R.string.err_msg_acc_emailid_empty));
            final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_e_mail));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            valid = false;
        } else if (!s.matches(emailPattern)) {
            //accountEmailAddrLayout.setError(getString(R.string.err_msg_acc_emailid));
            final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_e_mail_worng));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            valid = false;
        } else {
            user_name.setError(null);
        }
        return valid;
    }


    private void gotoForgetPw() {
        pDialog.show();

        JSONObject json = new JSONObject();
        try {

            if(user_name.getText().toString().equals("")){
                name = "";
                json.put("username", name);
                Log.e("if jsonArray", String.valueOf(json));
            }else {
                name = user_name.getText().toString();
                json.put("username", name);
                Log.e("else jsonArray", String.valueOf(json));
            }


        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ForgotPasswordRequest> call = apiInterface.ForgotRequestTask(json.toString());

        call.enqueue(new Callback<ForgotPasswordRequest>() {
            @Override
            public void onResponse(Call<ForgotPasswordRequest> call, Response<ForgotPasswordRequest> response) {
                // pDialog.show();

                forgot_lt.setEnabled(false);
                Log.e("response", String.valueOf(response.body()));
                if (response.body() != null){

                    ForgotPasswordRequest resource = response.body();
                    if (resource.getStatus().equals("Success")){
                        ForgotPwData data = resource.getData();
                        Log.e("data", String.valueOf(data));

                        if(data.getStatus().equals("Success")){
                            user_name.setText("");
                            String name =data.getDetails().get(0).getName();
                            Log.e("name",name);
                            String email = data.getDetails().get(0).getEmailid();
                            Log.e("email",email);
                            String phone_no = data.getDetails().get(0).getPhoneno();
                            showDialogMethod("",getString(R.string.forgot_otp_msg));
                            //session.createLoginSession(name,email,phone_no);
                        }else if(data.getStatus().equals("Failed")){
                            user_name.setText("");
                            showDialogMethod("Alert", data.getDetails().get(0).getError());
                        }

                    }else if (resource.getStatus().equals("Failure")){
                        showDialogMethod("Alert", resource.getMessage());
//                        if(resource.getData().getDetails() != null){
//                            showDialogMethod("Alert", String.valueOf(resource.getData().getDetails()));
//                        }else {
//                            showDialogMethod("Alert", String.valueOf(resource.getData().getDetails()));
//                        }
                    }
                }else{
                    showDialogMethod("Failed","Something went wrong");
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ForgotPasswordRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert",t.getMessage());
            }
        });
    }

    private void loginIntegration() {
        pDialog.show();
        String username= user_name.getText().toString();
        String password= etPassword.getText().toString();

        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody pword = RequestBody.create(MediaType.parse("text/plain"), password);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LoginRequest> call = apiInterface.LoginRequestTask(uname, pword);

        call.enqueue(new Callback<LoginRequest>() {
            @Override
            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
               // pDialog.show();
               // loginRequestArrayList= response.body();
                loginRequest = response.body();
                loginRequestArrayList = loginRequest.getResponse();
                Log.e("response", String.valueOf(loginRequestArrayList));

                login_btn.setEnabled(true);
                if (response.body() != null){
                    LoginRequest resource = response.body();
                    if (resource.getStatus().equals("Success")){

                        Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                        intent.putExtra("login", flag);
                        startActivity(intent);

                        int jockey_id = loginRequestArrayList.get(0).getJockey_id();
                        String userName = loginRequestArrayList.get(0).getName();
                        String email = loginRequestArrayList.get(0).getEmailid();

                        String phone_no = loginRequestArrayList.get(0).getPhoneno();
                        String referral_code = loginRequestArrayList.get(0).getReferral_code();
                        String referred_code = loginRequestArrayList.get(0).getReferred_code();
                        String image_path = loginRequestArrayList.get(0).getImage_path();
                        String user_mode = loginRequestArrayList.get(0).getUser_mode();
                        Boolean applied_jockey = loginRequestArrayList.get(0).getAppliedForJockey();
                        Log.e("applied_jockey", String.valueOf(applied_jockey));
                        String update_status = "Success";
                        session.createLoginSession(jockey_id,userName,email,phone_no,referral_code,update_status,referred_code,
                                image_path,user_mode,applied_jockey);
                        session.createRegisterSession(userName,email,phone_no,referral_code,referred_code);
//                        ArrayList<LoginDatabean> responsebean = resource.getResponse();
//                        for (int i=0;i<responsebean.size();i++){
//                            sharedpreference.setSINGER_RESPONSE(responsebean.get(i).getEmailid());
//                        }

//                        SharedPreferences settings = LoginActivity.this.getSharedPreferences(PREFERENCES, 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        LoginString = editor.putBoolean("isLogged", true);
//                        editor.commit();
//                        for(int i=0;i<resource.size();i++){
//                            ticketCategories =resource.get(i).getCategory();
//
//                        }

                        Log.i("image_path",image_path);
                        login_btn.setEnabled(false);
                    }else if (resource.getStatus().equals("Failure")){
                        showDialogMethod("Alert", resource.getMessage());
                    }
                }else{
                    showDialogMethod("Failed","No Data Available");
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Failed","Something went wrong");
            }
        });
    }

    private void showDialogMethod(String title, String message) {
        KeyboardHide();
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }

    private void KeyboardHide() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etRenterPassword.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
//        finish();
//        Intent intent = new Intent(LoginActivity.this,Dashboard.class);
//        startActivity(intent);
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }

}

