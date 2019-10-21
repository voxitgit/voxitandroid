package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.InsertPointData;
import com.triton.voxit.model.InsertPointsRequest;
import com.triton.voxit.model.OTPRequest;
import com.triton.voxit.model.OTPResult;
import com.triton.voxit.model.SignUpRequest;
import com.triton.voxit.model.SingupData;
import com.triton.voxit.model.singupDatabeen;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends Activity implements OnClickListener {
    private CheckBox validate_cb,cb_M,cb_F,cb_O;
    private LinearLayout back;
    private Button login_btn;
    private EditText first_name,last_name,age,email,phome_no,country,state,city,otp,etPassword,etRenterPassword,referal_codeEt;
    private APIInterface apiInterface;
    private String checkvalue;
    private String email_val, name_val, flag;
    private String[] firstname;

    private static final String PREFERENCES = "";
    private SharedPreferences.Editor LoginString;
    private Context context;
    private ProgressDialog pDialog;
    private boolean fbval,gmailval;
    String opt_number;
    TextInputLayout first_name_layout,email_layout,phone_no_layout,otp_layout,text_input_layout_password,re_enter_layout_password;
    LinearLayout send_btn;
    SessionManager session;
    private TextView sendtext;
    private TextView checktext,helpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        pDialog = new ProgressDialog(Registration.this);
        pDialog.setMessage(Registration.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        initViews();
        clickMethod();
    }

    private void initViews() {
//        first_name_layout = (TextInputLayout)findViewById(R.id.first_name_layout);
//        email_layout = (TextInputLayout)findViewById(R.id.email_layout);
//        phone_no_layout = (TextInputLayout)findViewById(R.id.phone_no_layout);
//        otp_layout = (TextInputLayout)findViewById(R.id.otp_layout);
//        text_input_layout_password = (TextInputLayout)findViewById(R.id.text_input_layout_password);
//        re_enter_layout_password = (TextInputLayout)findViewById(R.id.re_enter_layout_password);
        first_name =(EditText) findViewById(R.id.first_name_edit);
        referal_codeEt = (EditText) findViewById(R.id.referal_codeEt);
        last_name = (EditText) findViewById(R.id.last_name_edit);
        age = (EditText) findViewById(R.id.age_edit);
        helpTv = (TextView) findViewById(R.id.helpTv);
        helpTv.setOnClickListener(this);
        cb_M = (CheckBox) findViewById(R.id.cb_M);
        cb_F = (CheckBox) findViewById(R.id.cb_F);
        cb_O = (CheckBox) findViewById(R.id.cb_O);
        email = (EditText) findViewById(R.id.email_edit);
        phome_no = (EditText) findViewById(R.id.phome_no_edit);
        country = (EditText) findViewById(R.id.country_edit);
        state = (EditText) findViewById(R.id.state_edit);
        city = (EditText) findViewById(R.id.city_edit);
        otp = (EditText) findViewById(R.id.otp_edit);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRenterPassword = (EditText) findViewById(R.id.etRenterPassword);
        send_btn = (LinearLayout) findViewById(R.id.send_btn);
        sendtext = (TextView) findViewById(R.id.sendtext);
        send_btn.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        back = (LinearLayout) findViewById(R.id.back);
        validate_cb = (CheckBox) findViewById(R.id.validate_checkbox);
        checktext = (TextView)findViewById(R.id.checktext);
        String text = "<font color=#000></font> I agree to the <font color=#00aee0>terms and conditions</font>";
        checktext.setText(Html.fromHtml(text));

        checktext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://voxitworld.com/privacypolicy.html"));
                startActivity(intent);
            }
        });

        //intent values get
        Intent i = getIntent();
        flag = i.getStringExtra("flag");
        if (flag.equals("R")) {
            gmailval = false;
            fbval = false;
        }else if (flag.equals("fb")){
            email_val = i.getStringExtra("currentemail");
            name_val = i.getStringExtra("name");

//            firstname = name_val.split(" ");
//            first_name.setText(firstname[0]);
//            last_name.setText(firstname[1]);
            first_name.setText(name_val);
            email.setText(email_val);
            first_name.setEnabled(false);
//            last_name.setEnabled(false);
            email.setEnabled(false);
            gmailval = true;
        }

        login_btn.setEnabled(true);
//        String test = first_name.getText();
//        country.setText("India");
//        state.setText("Tamilnadu");
//        city.setText("Chennai");

      //  first_name.addTextChangedListener(new MyTextWatcher(first_name));
        login_btn.setOnClickListener(this);
    }
    public boolean vaildFirstname() {

        if (first_name.getText().toString().trim().isEmpty()) {
           // first_name_layout.setError(getString(R.string.err_msg_first_name));
           // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
           // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_first_name));
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
            first_name.setError(null);
        }

        return true;
    }
    public boolean vaildReferralCode() {

        if (referal_codeEt.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_referral_code));
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
            referal_codeEt.setError(null);
        }

        return true;
    }
    public boolean vaildPhoneNo() {
        String MobilePattern = "^[+]?[0-9]{10}$";
        String s = phome_no.getText().toString().trim();

        if (s.isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
            phome_no.setError(null);
        }

        return true;
    }
    public boolean vaildOTP() {

        if (otp.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_otp));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        }else if (!otp.getText().toString().trim().equals(opt_number)){
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_otp_mismatch));
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
            otp.setError(null);
        }


        return true;
    }
    public boolean vaildPassword() {

        if (etPassword.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
        } else{
            etPassword.setError(null);
        }
//else if ((etPassword.getText().toString().trim().matches("[a-zA-Z 0-9]+"))){
//            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
//            // alertDialog.setTitle(title);
//            alertDialog.setMessage(getString(R.string.err_msg_Password_length));
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // finish();
//                            alertDialog.cancel();
//                        }
//                    });
//            alertDialog.show();
//            return false;
//        }
        return true;
    }
    public boolean vaildReEnterPassword() {

        if (etRenterPassword.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_Re_enter_password));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        }else if (!etPassword.getText().toString().trim().equals(etRenterPassword.getText().toString())){
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_Re_enter_password_worng));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            return false;
        }
            else {
            etRenterPassword.setError(null);
        }

        return true;
    }
    private boolean emailvalidtaion() {
        boolean valid = true;
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";
        // String emailPattern = "^([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})?$";
        String s = email.getText().toString().trim();

        if (s.length() == 0 || s.isEmpty()) {
           // accountEmailAddrLayout.setError(getString(R.string.err_msg_acc_emailid_empty));
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
            email.setError(null);
        }
        return valid;
    }

    private boolean validCheckbox() {
        if (validate_cb.isChecked()){
            return true;
        }else{
            final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_chk_worng));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            return false;
        }

    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Registration.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                if (!vaildFirstname()) {

                    return;
                }
                if (!emailvalidtaion()) {

                    return;
                }
                if (!vaildPhoneNo()) {

                    return;
                }
                if (!vaildOTP()) {

                    return;
                }
                if (!vaildPassword()) {

                    return;
                }
                if (!vaildReEnterPassword()) {

                    return;
                }
                if(!validCheckbox()){
                    return;
                }
//                if(!vaildReferralCode()){
//                        return;
//                    }
                    dataIntegration();
                }
                break;
            case R.id.send_btn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Registration.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                if (!vaildFirstname()) {

                    return;
                }
                if (!emailvalidtaion()) {

                    return;
                }
                if (!vaildPhoneNo()) {

                    return;
                }

                    getOTP();
                }
                break;
            case R.id.helpTv:
                Intent i = new Intent(Registration.this,HelpSupport.class);
                startActivity(i);
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
    private void getOTP() {
        pDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("emailid", email.getText().toString());
            json.put("phoneno", phome_no.getText().toString());
            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

       // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OTPRequest> call = apiInterface.OTPRequestTask(json.toString());

        call.enqueue(new Callback<OTPRequest>() {
            @Override
            public void onResponse(Call<OTPRequest> call, Response<OTPRequest> response) {
//                send_btn.setEnabled(false);
                sendtext.setText("Resend OTP");

                OTPRequest resource = response.body();

                if(resource.getStatus().equals("Success")){
                    OTPResult Otpdata = resource.getData();
                    pDialog.dismiss();
                    if (Otpdata.getStatus().equals("Success")){
                        opt_number = Otpdata.getOTP();
                        Log.e("opt_number",opt_number);
//                        otp.setText(opt_number);
                        showDialogMethodFail("Success", getString(R.string.otp_msg));
                    }else if(Otpdata.getStatus().equals("Failed")){
                        showDialogMethodFail("Warning", Otpdata.getDetails());
                    }

                }else if(resource.getStatus().equals("Failure")){
                    pDialog.dismiss();
                    OTPResult Otpdata = resource.getData();
                    if (Otpdata.getStatus().equals("Success")){
                        opt_number = Otpdata.getOTP();
                        otp.setText(opt_number);
                        showDialogMethodFail("Success", Otpdata.getDetails());
                    }else if(Otpdata.getStatus().equals("Failed")){
                        showDialogMethodFail("Warning", Otpdata.getDetails());
                    }
                }

            }

            @Override
            public void onFailure(Call<OTPRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }

    private class MyTextWatcher implements TextWatcher {

    private View view;

    public MyTextWatcher(View view) {
        this.view = view;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0 && s.length() <= 0) {

            first_name_layout.setErrorEnabled(true);
        }
        else {
            first_name_layout.setErrorEnabled(false);
        }
//        if (s.length() > 0 && s.length() <= 0) {
//
//            companyNameLayout.setErrorEnabled(true);
//        } else {
//            companyNameLayout.setErrorEnabled(false);
//        }



//        if (s.length() > 0 && s.length() <= 0) {
//
//            passwordLayout.setErrorEnabled(true);
//        } else {
//            passwordLayout.setErrorEnabled(false);
//        }

    }

    @Override
    public void afterTextChanged(Editable s) {


//            switch (view.getId()){
//
//                case R.id.company_name:
//                    vaildCompanyname();
//                    break;
//
//                case R.id.password:
//                    validPassword();
//                    break;
//                case R.id.company_size:
//                    vaildateCompanysize();
//                    break;
//                case R.id.company_location:
//                    validateCompanylocation();
//                    break;
//
//                case R.id.account_email_addr:
//                    if (s.length()== 0) {
//
//                        emailvalidtaion();
//                    }
//
//                    break;
//
//            }

    }
}
    private void clickMethod() {

         //chnages app now this code not worked in app
        cb_M.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_F.setChecked(false);
                    cb_O.setChecked(false);
                    checkvalue = cb_M.getText().toString();
                }
            }
        });
        cb_F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_M.setChecked(false);
                    cb_O.setChecked(false);
                    checkvalue = cb_F.getText().toString();
                }
            }
        });
        cb_O.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_F.setChecked(false);
                    cb_M.setChecked(false);
                    checkvalue = cb_O.getText().toString();

                }
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    private void dataIntegration() {
        pDialog.show();
        String name = first_name.getText().toString() + " " + last_name.getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
//            json.put("age", age.getText().toString());
//            json.put("gender", checkvalue);
            json.put("emailid", email.getText().toString());
            json.put("phoneno", phome_no.getText().toString());
            json.put("password", etPassword.getText().toString());
            json.put("referred_code", referal_codeEt.getText().toString());
            json.put("is_facebook", fbval);
            json.put("is_gmail", gmailval);
//            json.put("location","Chennai");
//            json.put("country","India");
//            json.put("state","Tamilnadu");
            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<SignUpRequest> call = apiInterface.SignupRequestTask(new SingupData(name,"",
                "",email.getText().toString(),phome_no.getText().toString(), etPassword.getText().toString(),fbval,
                gmailval, referal_codeEt.getText().toString(),"", "",""));

        call.enqueue(new Callback<SignUpRequest>() {
            @Override
            public void onResponse(Call<SignUpRequest> call, Response<SignUpRequest> response) {
               // pDialog.show();
                login_btn.setEnabled(true);
                if (response.body() != null){
                    SignUpRequest resource = response.body();
                    if (resource.getStatus().equals("Success")){
                        singupDatabeen data = resource.getData();
                        if(data.getSuccess().equals("Success")){
                            int jockey_id =data.getDetails().getJockey_id();
                            String name =data.getDetails().getName();
                            Log.e("name",name);
                            String email = data.getDetails().getEmailid();
                            Log.e("email",email);
                            String phone_no = data.getDetails().getPhoneno();
                            String referral_code = data.getDetails().getReferral_code();
                            String referred_code = data.getDetails().getReferred_code();
                            String image_path =data.getDetails().getImage_path();
                            String user_mode = data.getDetails().getUser_mode();
                            Boolean applied_jockey = data.getDetails().getAppliedForJockey();
                            String update_status = "Success";
                            session.createLoginSession(jockey_id, name,email,phone_no,referral_code,update_status,referred_code,
                                    image_path,user_mode,applied_jockey);
                           // session.createRegisterSession(name,email,phone_no,referral_code,referred_code);
                            login_btn.setEnabled(false);
                            insertPointsData(referred_code);
                        }

                        SharedPreferences settings = Registration.this.getSharedPreferences(PREFERENCES, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        LoginString = editor.putBoolean("isLogged", true);
                        editor.commit();

                        if (flag.equals("fb")){
                            showDialogMethodFB("","Successfully Registered");
                        }else{
                            showDialogMethod("","Successfully Registered");
                        }

                    }else if (resource.getStatus().equals("Failure")){
                        if(resource.getError() != null){
                            showDialogMethodFail("Alert",resource.getError());
                        }else if(resource.getData().getError() != null){
                            showDialogMethodFail("Invalid",resource.getData().getError());
                        }else {
                            showDialogMethodFail("Alert", "Not Registered");
                        }
                    }
                }else{
                    showDialogMethodFail("Failed","Something went wrong");
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SignUpRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void insertPointsData(String referred_code) {
        pDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("referred_code", referred_code);
            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<InsertPointsRequest> call = apiInterface.InsertPointRequest(json.toString());

        call.enqueue(new Callback<InsertPointsRequest>() {
            @Override
            public void onResponse(Call<InsertPointsRequest> call, Response<InsertPointsRequest> response) {
//                send_btn.setEnabled(false);
                pDialog.dismiss();
                InsertPointsRequest resource = response.body();

                if (response.body() != null) {
                if (resource.getStatus().equals("Success")) {
                    InsertPointData data = resource.getData();
                    pDialog.dismiss();
                    if (data.getStatus().equals("Success")) {
                        String status = data.getStatus();
                        Log.e("status", status);

                        showDialogMethod("Success", data.getStatus());
                    } else if (data.getStatus().equals("Failed")) {
                        showDialogMethod("Warning", data.getStatus());
                    }

                } else if (resource.getStatus().equals("Failure")) {
                    pDialog.dismiss();
                    InsertPointData data = resource.getData();
                    if (data.getStatus().equals("Success")) {
                        String status = data.getStatus();
                        Log.e("status", status);
                        showDialogMethod("Success", data.getStatus());
                    } else if (data.getStatus().equals("Failed")) {
                        showDialogMethod("Warning", data.getStatus());
                    }
                }
            }

            }

            @Override
            public void onFailure(Call<InsertPointsRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void showDialogMethod(String title, final String message) {
        KeyboardHide();
        AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      //  finish();
                        if(message.equals("New User")){
                            otp.setText(opt_number);
                        }else {
                            Intent intent = new Intent(Registration.this,Dashboard.class);
                            startActivity(intent);
                        }

                    }
                });
        alertDialog.show();

    }

    private void showDialogMethodFail(String title, String message) {
        KeyboardHide();
        final AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // finish();
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();

    }

    private void showDialogMethodFB(String title, String message) {
        KeyboardHide();
        AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // finish();
                        Intent intent = new Intent(Registration.this,Dashboard.class);
                       // intent.putExtra("login", "F");
                        startActivity(intent);
                    }
                });
        alertDialog.show();

    }

    private void KeyboardHide() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etRenterPassword.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Registration.this,LoginActivity.class);
        startActivity(intent);
    }
}
