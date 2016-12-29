package phoenix.iitdcomplaints;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devang on 29
 * -03-2016.
 */
public class NewUser extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    TextView title;
    View line;
    EditText username;
    EditText firstname;
    EditText lastname;
    ProgressDialog pd;
    EditText email;
    EditText entryno;
    EditText password;
    EditText por;
    Spinner type;
    Spinner hostel;
    Spinner special;
    ImageButton i1;
    ImageButton i2;
    ImageButton i3;
    ImageButton i4;
    ImageButton i5;
    ImageButton i6;
    ImageButton i7;
    ImageButton i8;
    ImageButton i9;
    ImageButton i10;
    Button submit;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.add_user, container, false);


        title=(TextView)view.findViewById(R.id.textViewItemTitle);
        line=(View)view.findViewById(R.id.line);
        username=(EditText)view.findViewById(R.id.username);
        firstname=(EditText)view.findViewById(R.id.first_name);
        lastname=(EditText)view.findViewById(R.id.last_name);
        email=(EditText)view.findViewById(R.id.email);
        entryno=(EditText)view.findViewById(R.id.entry_no);
        password=(EditText)view.findViewById(R.id.password);
        por=(EditText)view.findViewById(R.id.por);
        type=(Spinner)view.findViewById(R.id.type);
        hostel=(Spinner)view.findViewById(R.id.hostel);
        special=(Spinner)view.findViewById(R.id.special);
        i1=(ImageButton)view.findViewById(R.id.image1);
        i2=(ImageButton)view.findViewById(R.id.image2);
        i3=(ImageButton)view.findViewById(R.id.image3);
        i4=(ImageButton)view.findViewById(R.id.image4);
        i5=(ImageButton)view.findViewById(R.id.image5);
        i6=(ImageButton)view.findViewById(R.id.image6);
        i7=(ImageButton)view.findViewById(R.id.image7);
        i8=(ImageButton)view.findViewById(R.id.image8);
        i9=(ImageButton)view.findViewById(R.id.image9);
        i10=(ImageButton)view.findViewById(R.id.image10);
        submit=(Button)view.findViewById(R.id.submit_button);

        type.setOnItemSelectedListener(this);

        List<String> types = new ArrayList<String>();
        types.add("student");
        types.add("professor");
        types.add("warden");
        types.add("dean");
        types.add("worker");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type.setAdapter(dataAdapter1);

        hostel.setOnItemSelectedListener(this);

        List<String> hostels = new ArrayList<String>();
        hostels.add("Aravali");
        hostels.add("Girnar");
        hostels.add("Himadri");
        hostels.add("Jwalamukhi");
        hostels.add("Kailash");
        hostels.add("Karakoram");
        hostels.add("Kumaon");
        hostels.add("Nilgiri");
        hostels.add("Satpura");
        hostels.add("Shivalik");
        hostels.add("Udaigiri");
        hostels.add("Vindhyanchal");
        hostels.add("Zanskar");
        hostels.add("None");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, hostels);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        hostel.setAdapter(dataAdapter2);

        special.setOnItemSelectedListener(this);

        List<String> answer = new ArrayList<String>();
        answer.add("Yes");
        answer.add("No");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, answer);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        special.setAdapter(dataAdapter3);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitForm()){
                    register();
                }
            }
        });

 return view;
    }

    private void register() {

        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

         String name = username.getText().toString().trim();
         String first = firstname.getText().toString().trim();
        String last = lastname.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String entry = entryno.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String po = por.getText().toString().trim();
        String p=po;
        if(po.equals("")){
            p="None";
        }
        String t = type.getSelectedItem().toString().trim();
        String h = hostel.getSelectedItem().toString().trim();
        String sr= special.getSelectedItem().toString().trim();

        String s="0";
        if(sr.equals("Yes")){
            s="1";
        }



        String initial = getResources().getString(R.string.server_host);
        final String url = initial +"/default/new_user.json?username="+name+"&first_name="+first+"&last_name="+last+"&email="+mail+"&entry_no="+entry+"&por="+p+"&password="+pass+"&special="+s+"&type_="+t+"&hostel="+h;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    if(Response.getString("success").equals("true")){

                        Toast.makeText(view.getContext(),"User Added" , Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(view.getContext(),Response.toString() , Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(view.getContext(), Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );

        String tag_json_obj = "json_obj_req";
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);









    }


    private boolean submitForm() {

        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(view.getContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if (firstname.getText().toString().trim().isEmpty()) {
            Toast.makeText(view.getContext(), "First name cannot be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if (lastname.getText().toString().trim().isEmpty()) {
            Toast.makeText(view.getContext(), "Last name cannot be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if (email.getText().toString().trim().isEmpty()) {
            Toast.makeText(view.getContext(), "Email cannot be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(view.getContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }















    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {

    }
}