package phoenix.iitdcomplaints;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class MyComplaint extends Fragment {

   View view;
    ProgressDialog pd;
    ListView lv;
    ArrayList<RowItem> results;


    JSONArray myComplaints;
 TextView text;


    public MyComplaint() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification, container, false);
        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);
        text=(TextView) view.findViewById(R.id.notification_text);
         text.setText("  My Complaints");

        getMyComplaints();

   return view;
    }


    private void getMyComplaints() {
        String initial =getResources().getString(R.string.server_host);
        String url = initial + "/default/mycomplaints.json";
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

       int n=0;
         myComplaints= new JSONArray(response.getJSONArray("complaints").toString());
        n = myComplaints.length();

        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (JSONException e) {
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
       JSONArray no=myComplaints;
        JSONObject n = no.getJSONObject(position);
       JSONArray newAr=new JSONArray();
        newAr=newAr.put(n);
        JSONObject d=new JSONObject();
        d.put("complaint",newAr);
        Intent intent = new Intent(view.getContext(),ViewComplaint.class);

        intent.putExtra("complaintInfo",d.toString());
        String userComplaint="{seen:0,vote:0,user_id:"+n.getString("user_id") + "}";
        intent.putExtra("userComplaint", userComplaint.toString());
        pd.dismiss();
        startActivity(intent);

    }














    private ArrayList getListData() throws JSONException {
        results = new ArrayList<RowItem>();
        JSONArray no = myComplaints;
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





}