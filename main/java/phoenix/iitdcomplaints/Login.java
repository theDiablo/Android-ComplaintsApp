package phoenix.iitdcomplaints;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*      Activity is called after Login Button is Pressed in Home Activity and it hastextViews to take username and password
     info .After logging in all the information is displayed
    //  checks if valid username or password
    //  Sends a JSON POST request to the server and Cookie session is created with this request and  takes action accordingly
    If a positive response is obtained then takes the grades , courses and Notification responses
    of the user using the JSON POST request and also takes the advantage of cookies enabled.
    //  Dashboard has navigation bar which further has action tabs.

*/






public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText inputName, inputPasword;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private Button btnRegister;
    ProgressDialog pd;
    private JSONObject profileResponse;
    private JSONObject myComplaintsResponse;
    private JSONObject complaintsResponse;

    private JSONObject notificationResponse;
    private JSONObject peopleResponse;

  private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        TextView titleText=(TextView) findViewById(R.id.toolbar_title);
        ///titleText.setText("MoodlePlus");
        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);


            }
        });

        AppController s=new AppController(getApplicationContext());


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.input_name);
        inputPasword = (EditText) findViewById(R.id.input_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPasword.addTextChangedListener(new MyTextWatcher(inputPasword));

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        btnRegister.setOnClickListener(this);



    }
    @Override
    public void onClick(View v)  {
        if(v == btnRegister){
            if (submitForm()==true)
            {
                registerUser();
            }
        }
    }
    private boolean submitForm(){
        if (inputName.getText().toString().trim().isEmpty() ) {
            inputLayoutName.setError(getString(R.string.empty_error_msg));
            requestFocus(inputName);

            return false;
        }
        if (inputPasword.getText().toString().trim().isEmpty() ) {
            inputLayoutPassword.setError("Enter Password");
            requestFocus(inputPasword);

            return false;
        }
        return true;

    }
    private void registerUser() {
        final String username = inputName.getText().toString().trim();
        final String password = inputPasword.getText().toString().trim();
        String initial = getResources().getString(R.string.server_host);
        final String url = initial + "/default/login.json?userid=" + username + "&password=" + password;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeResponse(Response, url);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Login.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );

        String tag_json_obj = "json_obj_req";
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }

    private void executeResponse(JSONObject response, String url) {
        try {
            if(response.getString("success").equals("false")){
                Toast.makeText(Login.this,R.string.invalidInput,Toast.LENGTH_LONG).show();


            }

            else {
                pd=ProgressDialog.show(this,"Loading Data...","Please Wait",true,true);
                profileResponse=new JSONObject(response.toString());

                getMyComplaints();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getMyComplaints() {
        String initial =getResources().getString(R.string.server_host);
        String url = initial + "/default/mycomplaints.json";
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeMyComplaints(Response);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Login.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executeMyComplaints(JSONObject response) {
        try {
            if(response.getString("success").equals("false")){
                Toast.makeText(Login.this,"Problem!!!",Toast.LENGTH_LONG).show();

          pd.dismiss();
            }
           else {

                myComplaintsResponse = new JSONObject(response.toString());

                getComplaints();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getComplaints() {
        String initial =getResources().getString(R.string.server_host);
        String url = initial + "/default/complaints.json";
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeComplaints(Response);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Login.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executeComplaints(JSONObject response) {
        try {
            complaintsResponse = new JSONObject(response.toString());

            getNotifications();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getNotifications() {
        String initial =getResources().getString(R.string.server_host);
        String url = initial + "/default/notifications.json";
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeNotifications(Response);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Login.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executeNotifications(JSONObject response) {
        try {
            notificationResponse = new JSONObject(response.toString());

            getPeople();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getPeople() {
        String initial =getResources().getString(R.string.server_host);
        String hostel="";
        try {
             hostel=profileResponse.getJSONObject("user").getString("hostel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = initial + "/default/searchpeople.json?hostel="+hostel;
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executepeople(Response);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Login.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executepeople(JSONObject response) {
        try {
            peopleResponse= new JSONObject(response.toString());
            Intent intent = new Intent(Login.this,UserHome.class);
            intent.putExtra("response",profileResponse.toString());
            intent.putExtra("myComplaintsResponse", myComplaintsResponse.toString());
            intent.putExtra("complaintsResponse",complaintsResponse.toString());

            intent.putExtra("notiResponse", notificationResponse.toString());

            intent.putExtra("peopleResponse", peopleResponse.toString());
            pd.dismiss();

            startActivity(intent);
            finish();






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    /**
     * My Text Watcher class mainely has fuction afterTextChanged which changes the focus and gives the effects of Floating
     * Input Layouts.
     *
     *
     *
     *
     */


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:

                    inputLayoutName.setErrorEnabled(false);

                    break;

                case R.id.input_password:
                    inputLayoutPassword.setErrorEnabled(false);

                    break;
            }
        }

    }

}
