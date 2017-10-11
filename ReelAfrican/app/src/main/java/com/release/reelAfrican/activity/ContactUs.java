package com.release.reelAfrican.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment {
    Context context;
    ProgressBarHandler pDialog;

    String regEmailStr, regNameStr, regMessageStr;
    EditText editEmailStr, editNameStr, editMessageStr;
    TextView contactFormTitle;
    Button submit;
    String sucessMsg, statusmsg;
    String contEmail;
    AsynContactUs asynContactUs;
    boolean validate = true;


    public ContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        context = getActivity();



        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        final Intent startIntent = new Intent(getActivity(), MainActivity.class);

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                getActivity().startActivity(startIntent);
                                getActivity().finish();

                            }
                        });
                    }
                }
                return false;
            }
        });




        TextView categoryTitle = (TextView) v.findViewById(R.id.categoryTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText(getArguments().getString("title"));

        contactFormTitle = (TextView) v.findViewById(R.id.contactFormTitle);
        Typeface contactFormTitleTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.light_fonts));
        contactFormTitle.setTypeface(contactFormTitleTypeface);
        contactFormTitle.setText(Util.getTextofLanguage(context, Util.FILL_FORM_BELOW, Util.DEFAULT_FILL_FORM_BELOW));


        editEmailStr = (EditText) v.findViewById(R.id.contact_email);
        Typeface editEmailStrTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);
        editEmailStr.setHint(Util.getTextofLanguage(context, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));

        editNameStr = (EditText) v.findViewById(R.id.contact_name);
        Typeface editNameStrTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.light_fonts));
        editNameStr.setTypeface(editNameStrTypeface);
        editNameStr.setHint(Util.getTextofLanguage(context, Util.NAME_HINT, Util.DEFAULT_NAME_HINT));

        editMessageStr = (EditText) v.findViewById(R.id.contact_msg);
        Typeface editMessageStrTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.light_fonts));
        editMessageStr.setTypeface(editMessageStrTypeface);
        editMessageStr.setHint(Util.getTextofLanguage(context, Util.MESSAGE, Util.DEFAULT_MESSAGE));

        submit = (Button) v.findViewById(R.id.submit_cont);
        Typeface submitTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
        submit.setTypeface(submitTypeface);
        submit.setText(Util.getTextofLanguage(context, Util.BTN_SUBMIT, Util.DEFAULT_BTN_SUBMIT));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Submitted successfully", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                SubmmitClicked();

            }
        });

        return v;
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void SubmmitClicked() {

        regEmailStr = editEmailStr.getText().toString().trim();
        regNameStr = editNameStr.getText().toString().trim();
        regMessageStr = editMessageStr.getText().toString().trim();

        boolean isNetwork = Util.checkNetwork(context);
        if (isNetwork == true) {
            if (!regNameStr.matches("") && (!regEmailStr.matches("")) && (!regMessageStr.matches(""))) {
                boolean isValidEmail = Util.isValidMail(regEmailStr);
                if (isValidEmail) {
                    if (validate) {


                        asynContactUs = new AsynContactUs();
                        asynContactUs.execute();

                    } else {
                        validate = true;
                        return;
                    }

                } else {
                    Toast.makeText(context, Util.getTextofLanguage(context, Util.OOPS_INVALID_EMAIL, Util.DEFAULT_OOPS_INVALID_EMAIL), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, Util.getTextofLanguage(context, Util.ENTER_REGISTER_FIELDS_DATA, Util.DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(context, Util.getTextofLanguage(context, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }

    }


    private class AsynContactUs extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;
        String contMessage;
        String responseStr;


        @Override
        protected Void doInBackground(String... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.ContactUs.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("name", String.valueOf(regNameStr));
                httppost.addHeader("email", String.valueOf(regEmailStr));
                regMessageStr = regMessageStr.replaceAll("(\r\n|\n\r|\r|\n|<br />)", " ");
                httppost.addHeader("message", String.valueOf(regMessageStr));
                httppost.addHeader("lang_code", Util.getTextofLanguage(context, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
                statusmsg = myJson.optString("status");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, sucessMsg, Toast.LENGTH_LONG).show();

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;

            }

            editMessageStr.setText("");
            editNameStr.setText("");
            editEmailStr.setText("");
            editMessageStr.setError(null);
            editNameStr.setError(null);
            editEmailStr.setError(null);

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(context);
            pDialog.show();


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }
    }
}
