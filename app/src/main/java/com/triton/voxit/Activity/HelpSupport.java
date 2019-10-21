package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.HelpResponseData;
import com.triton.voxit.model.HelpText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpSupport extends NavigationDrawer implements View.OnClickListener {

    private EditText user_name;
    private EditText email_help;
    private EditText mobile;
    private EditText issue;
    private TextView submit,headertitle;
    private SessionManager session;
    private String id;
    ImageView img;
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    private HelpResponseData helpResponseData;
    String name,email,phoneNo,type_selected_val;
    Spinner type_of_issuesSp;
    TextView text;
    String times[] = {"Select","Technical", "Redemption", "Content Issues", "Suggestions & Feedback"};
    private RelativeLayout spnn;
    private TextView textd;
    private HelpText helpText;

    TextView tv_backtohome;

    //"Select Type of Issues"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        pDialog = new ProgressDialog(HelpSupport.this);
        pDialog.setMessage(HelpSupport.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.JOCKEY_ID);
        name = user.get(SessionManager.KEY_USER_NAME);
        email = user.get(SessionManager.KEY_EMAIL);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        hideLanguageView();
        showTitleView();
        initViews();
    }
    private void typeOfIssuesData() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(HelpSupport.this, R.layout.spinner_item, times);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        type_of_issuesSp.setAdapter(spinnerArrayAdapter);
        type_of_issuesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                if (arg2 != 0) {
                    type_selected_val = type_of_issuesSp.getSelectedItem().toString();
                }
//                if(type_of_issuesSp.getSelectedItem().equals("Select Type Of Issues")){
//
//                    ;/* alert */
//                    ;/* reset selection on this spinner */
//
//            }

//                Toast.makeText(getApplicationContext(), start_time_selected_val ,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



    }

    private void initViews() {
        user_name = (EditText) findViewById(R.id.user_name);
        text = (TextView) findViewById(R.id.text);
        type_of_issuesSp = (Spinner) findViewById(R.id.type_of_issues);
        spnn = (RelativeLayout) findViewById(R.id.spnn);
        email_help = (EditText) findViewById(R.id.email_help);
        mobile = (EditText) findViewById(R.id.mobile_help);
        issue = (EditText) findViewById(R.id.issue);
        submit = (TextView) findViewById(R.id.submit);
        headertitle = (TextView) findViewById(R.id.header_title);
        textd = (TextView) findViewById(R.id.textd);
        img = (ImageView) findViewById(R.id.tvHeaderIcon);
       // img.setVisibility(View.GONE);
        headertitle.setText("Help & Support");
        user_name.setText(name);
        email_help.setText(email);
        mobile.setText(phoneNo);
        typeOfIssuesData();

        if(session.isLoggedIn()){
            disablevalue(user_name,email_help,mobile,spnn);
        }else {
            //startActivity(new Intent(this, LoginActivity.class));
                   // .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            type_of_issuesSp.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }
        submit.setOnClickListener(this);
        textUpdate();

        tv_backtohome = (TextView)findViewById(R.id.tvbacktohome);
        tv_backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void textUpdate() {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<HelpText> call = apiInterface.getHelp();

        call.enqueue(new Callback<HelpText>() {
            @Override
            public void onResponse(Call<HelpText> call, Response<HelpText> response) {
                helpText = response.body();
                if (helpText != null){
                    textd.setText(helpText.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HelpText> call, Throwable t) {

            }
        });

    }

    private void disablevalue(EditText user_name, EditText email_help, EditText mobile, RelativeLayout spnn) {
//        user_name.setFocusable(false);
//        user_name.setEnabled(false);
//        user_name.setCursorVisible(false);
//
//        email_help.setFocusable(false);
//        email_help.setEnabled(false);
//        email_help.setCursorVisible(false);
//
//        mobile.setFocusable(false);
//        mobile.setEnabled(false);
//        mobile.setCursorVisible(false);
        user_name.setVisibility(View.GONE);
        email_help.setVisibility(View.GONE);
        mobile.setVisibility(View.GONE);
        spnn.setVisibility(View.VISIBLE);


//        user_name.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.gray));
//        email_help.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.gray));
//        mobile.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.gray));
//        issue.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.gray));
    }

    private void APIcall() {
        pDialog.show();
        Integer jockeyid = Integer.valueOf(id);

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id",jockeyid);
            json.put("name",user_name.getText().toString());
            json.put("emailid",email_help.getText().toString());
            json.put("phoneno",mobile.getText().toString());
            json.put("support_type",type_selected_val);
            json.put("message",issue.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<HelpResponseData> call = apiInterface.getHelpData(body);

        call.enqueue(new Callback<HelpResponseData>() {
            @Override
            public void onResponse(Call<HelpResponseData> call, Response<HelpResponseData> response) {
                pDialog.dismiss();
                helpResponseData = response.body();
                if (helpResponseData != null){
                    if (helpResponseData.getStatus().equals("Success")){
                        String message = helpResponseData.getResponse().get(0).getDisplayContent();
                        Log.e("msg",message);
                       // String number = helpResponseData.getResponse().getReference_no();
                        user_name.setText("");
                        email_help.setText("");
                        mobile.setText("");
                        issue.setText("");
                        type_of_issuesSp.setSelection(0);
                        showDialogMethod(message);
                        //nextFuntion(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<HelpResponseData> call, Throwable t) {


            }
        });
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
    public boolean vaildTypeOfIssue() {

        if(type_of_issuesSp.getSelectedItem().equals("Select")){
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_type_of_issues));
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
            //type_of_issuesSp.setError(null);
        }

        return true;
    }
    private void showDialogMethod(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
        //alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(HelpSupport.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                    if (!nametest()){
                        return;
                    }
                    if (!emailset()){
                        return;
                    }
                    if (!vaildTypeOfIssue()) {

                        return;
                    }
                    if (!desissue()){
                        return;
                    }

                    APIcall();
                }
                break;
        }
    }

    private boolean emailset() {

        boolean valid = true;
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";
        String s = email_help.getText().toString().trim();

        if (s.length() == 0 || s.isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
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
            final AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_e_mail_worng));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            valid = false;
        } else {
            email_help.setError(null);
        }
        return valid;

    }

    private boolean nametest() {
        if (user_name.getText().toString().trim().isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
            alertDialog.setMessage("Name required");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            return false;
        } else {
            user_name.setError(null);
        }
        return true;
    }

    private boolean desissue() {
        if (issue.getText().toString().trim().isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(HelpSupport.this).create();
            alertDialog.setMessage("Issue required");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            return false;
        } else {
            issue.setError(null);
        }
        return true;
    }
}
