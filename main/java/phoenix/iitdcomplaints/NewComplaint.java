package phoenix.iitdcomplaints;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewComplaint extends Fragment implements AdapterView.OnItemSelectedListener {
      Toolbar toolbar;
      View view;
    Spinner spinner;
    EditText title;
    EditText description;
    EditText tags;
    Button btn;
    String complaintLevel;
    String concerned_id;
  String concerned;
    int level;
    Spinner spinner2;
    private JSONObject profileResponse;
    private JSONObject peopleResponse;

ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.new_complaint, container, false);

      setSpinner();
        concerned_id=0+"";
        getResponse();
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);

        try {
            setSpinner2();
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //      spinner2.setOnTouchListener(Spinner_OnTouch);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                concerned = spinner2.getSelectedItem().toString();
                try {
                    concerned_id=getid(concerned);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        title=(EditText)  view.findViewById(R.id.title);
        description=(EditText) view.findViewById(R.id.description);
        tags=(EditText) view.findViewById(R.id.tags);
 btn=(Button) view.findViewById(R.id.submit_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if( submitComplaint()){
                   fileComplaint();
               }
            }
        });

        return view;


    }
    private String help(String s){
        int n=s.length();
        String o="";
        for(int i=0;i<n;i++){
            char c=s.charAt(i);
            if(c==' '){
                o=o+"%20";
            }
            else o=o+c;
        }
        return o;
    }



    private void fileComplaint() {

        pd= ProgressDialog.show(view.getContext(), "Submitting...", "Please Wait", true, true);

        String t=title.getText().toString().trim();
      String des=description.getText().toString().trim();
        String ta=tags.getText().toString().trim();

        String initial = getResources().getString(R.string.server_host);
        final String url = initial + "/complaints/new.json?title="+ help(t)+"&description="+help(des)+"&concerneduserid="+Integer.valueOf(concerned_id)+"&complaintlevel="+Integer.valueOf(level)+"&tags="+help(ta);

        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeResponse(Response, url);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(view.getContext(), Error.toString().trim(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
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
                Toast.makeText(view.getContext(),"Try Again!!",Toast.LENGTH_LONG).show();

              pd.dismiss();
            }

            else {
                Toast.makeText(view.getContext(),"Submitted,Well Done!!",Toast.LENGTH_LONG).show();
                  pd.dismiss();
                title.setText("");
                description.setText("");



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private String getid(String concerned) throws JSONException {
        JSONArray ar=peopleResponse.getJSONArray("people");
        int n=ar.length();
        for(int i=0;i<n;i++){
            JSONObject o=ar.getJSONObject(i);
            String a=o.getString("position_");
            if(a.equals(concerned)){
                return o.getString("userid");
            }

        }
     return 0+"";

    }

    private boolean submitComplaint() {
        if (title.getText().toString().trim().isEmpty() ) {
            Toast.makeText(view.getContext(),"Enter Title", Toast.LENGTH_LONG).show();

            return false;
        }
        if (description.getText().toString().trim().isEmpty() ) {
            Toast.makeText(view.getContext(),"Enter Description", Toast.LENGTH_LONG).show();

            return false;
        }
        if(concerned_id.equals("0")){
            Toast.makeText(view.getContext(),"Enter Concerned To", Toast.LENGTH_LONG).show();

            return false;

        }
        return true;




    }

    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                try {
                    setSpinner2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }
    };






    private void setSpinner2() throws JSONException {


            List<String> categories = new ArrayList<String>();

            JSONArray ar=peopleResponse.getJSONArray("people");
          int n=ar.length();
            for(int i=0;i<n;i++){
                JSONObject o=ar.getJSONObject(i);
                String a=o.getString("position_");
                    categories.add(a);

            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner2.setAdapter(dataAdapter);


    }




    private void getResponse() {
        try {
            peopleResponse=new JSONObject(getArguments().getString("peopleResponse"));
            profileResponse=new JSONObject(getArguments().getString("profileResponse"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void setSpinner() {
        spinner = (Spinner) view.findViewById(R.id.spinner1);

       level=1;
        complaintLevel="Individual Level";
        List<String> categories = new ArrayList<String>();
        categories.add("Individual Level");
        categories.add("Hostel Level");
        categories.add("Institute Level");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                level = spinner.getSelectedItemPosition() + 1;
                complaintLevel = spinner.getSelectedItem().toString();
                try {
                    setSpinner2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



}
