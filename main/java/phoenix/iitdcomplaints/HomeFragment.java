package phoenix.iitdcomplaints;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gautam on 3/28/2016.
 */
public class HomeFragment extends Fragment {

    private FloatingActionButton fab;
   private JSONObject peopleResponse;
    private JSONObject profileResponse;
    View view;
    ProgressDialog pd;
    ListView lv;
    ArrayList<RowItem> results;
    private FloatingActionButton fab2;

    int n;
    int flag;
    JSONArray scomplaints;
    JSONArray complaints;
    TextView text;
    EditText input;
    Button b;





    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.app_bar_navigation_home, container, false);
        try {
            getResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    input=(EditText) view.findViewById(R.id.input_name);
        Button b=(Button) view.findViewById(R.id.btn_register);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                work();

            }
        });











        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new NewComplaint();
                Bundle bundle = new Bundle();
                bundle.putString("peopleResponse", peopleResponse.toString());
                bundle.putString("profileResponse", profileResponse.toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

            }
        });
String special="0";

        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        fab2.setVisibility(View.INVISIBLE);
        try {
             special=profileResponse.getJSONObject("user").getString("special");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Integer.valueOf(special)==1){
            fab2.setVisibility(View.VISIBLE);
        }


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new NewUser();
                Bundle bundle = new Bundle();
                bundle.putString("peopleResponse", peopleResponse.toString());
                bundle.putString("profileResponse", profileResponse.toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

            }
        });











        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);
        text=(TextView) view.findViewById(R.id.notification_text);
        text.setText("   All Complaints");

        getMyComplaints();










        return view;
    }

    private void work(){
        pd = ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

        String t=input.getText().toString().trim();
        String initial = getResources().getString(R.string.server_host);
        String url = initial + "/default/search.json?tag="+help(t);
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {


                try {
                    JSONArray a=Response.getJSONArray("complaints");
                    if(a.length()==0){

                        Toast.makeText(view.getContext(), "No Match Found", Toast.LENGTH_LONG).show();
                       pd.dismiss();

                    }

                  else  {

                        Intent intent = new Intent(view.getContext(), Tags.class);
                        String r=Response.getJSONArray("complaints").toString();
                        intent.putExtra("response",r);
                        startActivity(intent);


                    }


                } catch (JSONException e) {
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);






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



    private void getMyComplaints() {
        String initial = getResources().getString(R.string.server_host);
        String url = initial + "/default/complaints.json";
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    executeMyComplaints(Response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }



    private void executeMyComplaints(JSONObject response) throws JSONException {

         n=0;
        scomplaints= new JSONArray(response.getJSONArray("complaints").toString());
        n = scomplaints.length();
         flag=n;
       complaints=new JSONArray();
        line();







    }



    private void line() throws JSONException {
        if(flag==0){
            showThingsReally();
            return;
        }

        String initial = getResources().getString(R.string.server_host);
        String id=scomplaints.getJSONObject(n-flag).getString("complaint_id");
        String url = initial + "/default/complaintinfo.json?complaint_id="+id;
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {

                try {
                    JSONObject newObj=new JSONObject(Response.getJSONArray("complaint").getJSONObject(0).toString());
                    complaints.put(newObj);

                    flag--;
                    line();

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(view.getContext(),"Or This"+ Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );
        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void showThingsReally() {
        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (JSONException e) {
            Toast.makeText(view.getContext(),"Or This"+e.toString() , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        lv = (ListView) view.findViewById(R.id.custom_list);
        lv.setAdapter(new CustomListAdapterNo2(view.getContext(), image_details));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                pd = ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

                try {
                    showComplaint(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        pd.dismiss();





    }

    private void showComplaint(int position) throws JSONException {
        JSONArray no=complaints;
        JSONObject n = no.getJSONObject(position);
        JSONArray newAr=new JSONArray();
        newAr=newAr.put(n);
        JSONObject d=new JSONObject();
        d.put("complaint", newAr);
        Intent intent = new Intent(view.getContext(),ViewComplaint.class);

        intent.putExtra("complaintInfo", d.toString());
        JSONObject userComplaint=new JSONObject(scomplaints.getJSONObject(position).toString());
        intent.putExtra("userComplaint", userComplaint.toString());
        pd.dismiss();
        startActivity(intent);

    }













    private ArrayList getListData() throws JSONException {
        results = new ArrayList<RowItem>();
        JSONArray no = complaints;
        if (no.length() == 0) {
            RowItem data = new RowItem();
            data.description="null";
            results.add(data);
            return results;
        }

// if a thread has been seen disabling its notification
        for(int i=0;i<no.length();i++) {

            RowItem data = new RowItem();
            JSONObject n = no.getJSONObject(i);
            data.created = n.getString("createdat");
            data.complaint_id=n.getString("id");
            data.description=n.getString("title");
            data.tags=n.getString("tags");
            if(n.getString("status").equals("0")){
                data.status="Pending";
            }
            else data.status="Resolved";
            if(n.getString("complaintslevel").equals("1")){
                data.level="Individual";
            }
            else if(n.getString("complaintslevel").equals("2")){
                data.level="Hostel";
            }
            else data.level="Institute";

            results.add(data);
        }
        // Add some more dummy data for testing
        return results;
    }








    private void getResponse() throws JSONException {
        peopleResponse=new JSONObject(getArguments().getString("peopleResponse"));

        profileResponse=new JSONObject(getArguments().getString("profileResponse"));


    }
}

