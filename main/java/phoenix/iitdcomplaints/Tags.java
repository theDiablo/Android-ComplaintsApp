package phoenix.iitdcomplaints;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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



public class Tags extends AppCompatActivity {
    TextView text;
    JSONObject response;
    JSONArray Nresponse;
    JSONArray noArray;
    ListView lv;
    ArrayList<Row2Item> results;
    ProgressDialog pd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification);
        pd= ProgressDialog.show(this, "Loading Data", "Please Wait", true, true);

        text=(TextView) findViewById(R.id.notification_text);
    text.setText("Results");
        Intent intent=getIntent();

        try {
            Nresponse=new JSONArray(intent.getStringExtra("response"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        executeNotifications();


//notifications of threads

  //      getRequest();


    }


    private void getRequest(){
        String initial =getResources().getString(R.string.server_host);
        String url = initial + "/default/notifications.json";
// fetching the course information
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    response=new JSONObject(Response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                executeNotifications();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Tags.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executeNotifications() {

            noArray = Nresponse;

     // if there is a new notification
        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv = (ListView) findViewById(R.id.custom_list);
        lv.setAdapter(new CustomListAdapterNo(this, image_details));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                pd = ProgressDialog.show(Tags.this, "Loading Data", "Please Wait", true, true);

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
        JSONArray no = Nresponse;
        JSONObject n = no.getJSONObject(position);
        String id=n.getString("complaint_id");
        getComplaintInfo(id,n);

    }

    private void getComplaintInfo(String id, final JSONObject userComplaint) {

        String initial = getResources().getString(R.string.server_host);
        final String url = initial + "/default/complaintinfo2.json?complaint_id="+id;

        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                executeResponse(Response, url,userComplaint);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(Tags.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );

        String tag_json_obj = "json_obj_req";
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);




    }

    private void executeResponse(JSONObject response, String url,JSONObject userComplaint) {
        Intent intent = new Intent(Tags.this,ViewComplaint.class);
        intent.putExtra("complaintInfo",response.toString());
        intent.putExtra("userComplaint",userComplaint.toString());
        pd.dismiss();
        startActivity(intent);

    }


    private ArrayList getListData() throws JSONException {
        results = new ArrayList<Row2Item>();
        JSONArray no = Nresponse;
        if (no.length() == 0) {
            Row2Item data = new Row2Item();
            data.created = null;
            results.add(data);
            return results;
        }

// if a thread has been seen disabling its notification
        for(int i=0;i<no.length();i++) {

            Row2Item data = new Row2Item();
            JSONObject n = no.getJSONObject(i);
            data.created = n.getString("createdat");
            data.complaint_id=n.getString("complaint_id");
            data.description="Complaint";
            results.add(data);
        }
        // Add some more dummy data for testing
        return results;
    }





}