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



public class NotificationFragment extends Fragment {
   TextView text;
    View view;
    JSONObject response;
    JSONArray noArray;
    ListView lv;
    ArrayList<Row2Item> results;
    ProgressDialog pd;

    public NotificationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.notification, container, false);
        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

        text=(TextView) view.findViewById(R.id.notification_text);
//notifications of threads

        getRequest();



     return view;

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


                        Toast.makeText(view.getContext(), Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void executeNotifications() {

        int n = 0;
        try {
            noArray = response.getJSONArray("notifications");
            n = noArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        text.setText("  "+n+" New Notifications");
// if there is a new notification
        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv = (ListView) view.findViewById(R.id.custom_list);
        lv.setAdapter(new CustomListAdapterNo(view.getContext(), image_details));
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
        JSONArray no = response.getJSONArray("notifications");
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


                        Toast.makeText(view.getContext(), Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );

        String tag_json_obj = "json_obj_req";
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);




    }

    private void executeResponse(JSONObject response, String url,JSONObject userComplaint) {
        Intent intent = new Intent(view.getContext(),ViewComplaint.class);
        intent.putExtra("complaintInfo",response.toString());
        intent.putExtra("userComplaint",userComplaint.toString());
        pd.dismiss();
        startActivity(intent);

    }


    private ArrayList getListData() throws JSONException {
         results = new ArrayList<Row2Item>();
        JSONArray no = response.getJSONArray("notifications");
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
            data.description=n.getString("notification_message");
            results.add(data);
        }
        // Add some more dummy data for testing
        return results;
    }





}