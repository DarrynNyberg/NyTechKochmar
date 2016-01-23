package com.info.post.postinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.info.post.postinfo.network.ConnectionDetector;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {
    private static final String URL = "http://dev-nytechleads.pantheon.io/api/test";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";

    private String name, phone, email;

    @InjectView(R.id.etName)
    EditText etName;
    @InjectView(R.id.etEmail)
    EditText etEmail;
    @InjectView(R.id.etPhone)
    EditText etPhone;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.please_wait));
    }

    public void onClickSend(View v) {
        if(isValid()) {
            sendData();
        }
    }

    private boolean isValid() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

        return true;
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }

    private void sendData(){
        if(ConnectionDetector.isConnection(this)) {
            progress.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.dismiss();
                            Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.successful), Toast.LENGTH_SHORT);
                            toast.show();

                            clearFields();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(NAME, name);
                    params.put(EMAIL, email);
                    params.put(PHONE, phone);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.check_connection), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}