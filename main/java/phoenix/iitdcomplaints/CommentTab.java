package phoenix.iitdcomplaints;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by Gautam on 3/29/2016.
 */
public class CommentTab extends Fragment {
    View view;
    TextView title;
    private JSONObject complaintResponse;
    private JSONObject userComplaintResponse;
    ProgressDialog pd;
    TextView comment;
    Button btn;
    ListView lv;
    JSONObject o;
    String complaint_id;
    private JSONArray commentResponse;
 private JSONArray userResponse;



    public CommentTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.comment, container, false);
        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

        title=(TextView) view.findViewById(R.id.textView1);
        comment=(TextView) view.findViewById(R.id.textView5);
        btn=(Button) view.findViewById(R.id.post_comment);
        lv=(ListView)view.findViewById(R.id.comment_list);
        try {
            complaintResponse=new JSONObject(getArguments().getString("response"));
            userComplaintResponse=new JSONObject(getArguments().getString("userComplaint"));
            o=complaintResponse.getJSONArray("complaint").getJSONObject(0);
            title.setText(o.getString("title"));
            complaint_id=o.getString("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });




        getComments();
     pd.dismiss();


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


    private void postComment() {
        if(comment.getText().toString().trim().isEmpty()){
            Toast.makeText(view.getContext(), "Enter Comment", Toast.LENGTH_LONG).show();
            return;
        }//when a thread is submitted the list adapter for that particular course is refreshed
        pd= ProgressDialog.show(view.getContext(), "Loading...", "Please Wait", true, true);
        String initial = getResources().getString(R.string.server_host);
        //the list view of all courses is also refreshed because info of one of them is changed
        String des=comment.getText().toString().trim();

        String url = initial +"/comments/post_comment.json?complaint_id="+Integer.valueOf(complaint_id)+"&description="+help(des);

        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {

                try {
                    if(Response.getString("success").equals("false")){
                        Toast.makeText(view.getContext(),"Sorry,Something went wrong!", Toast.LENGTH_LONG).show();

                    }
                    else{
                           getComments();
                        Toast.makeText(view.getContext(),"Comment Posted!", Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(view.getContext(),"No Its From Here!"+ Error.toString().trim(), Toast.LENGTH_LONG).show();
                      pd.dismiss();
                    }
                }
        );
        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);





    }


    private void getComments() {

        String initial = getResources().getString(R.string.server_host);
        String url = initial +"/comments/comments.json?complaint_id="+Integer.valueOf(complaint_id);

        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    commentResponse=new JSONArray(Response.getJSONArray("comments").toString());
                   userResponse=new JSONArray(Response.getJSONArray("comment_users").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showThings();

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(view.getContext(),"No Its From Here!"+ Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );
        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);




    }

    private void showThings() {
        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(image_details==null){
            Log.d("Error", "Wrong");
            return;
        }


        lv = (ListView) view.findViewById(R.id.comment_list);

        lv.setAdapter(new CustomListAdapterNo3(view.getContext(), image_details));

        title.setText("");
        pd.dismiss();





    }


    private ArrayList getListData() throws JSONException {
        ArrayList<Row3Item> results = new ArrayList<Row3Item>();

        if(commentResponse.length()==0){
            Row3Item data = new Row3Item();

            results.add(data);
            return results;
        }
        for(int i=0;i<commentResponse.length();i++) {
            Row3Item data = new Row3Item();
           JSONObject n=commentResponse.getJSONObject(i);
           data.description=n.getString("description");
            data.created=n.getString("createdat");
            data.name=userResponse.getJSONObject(i).getString("first_name")+" "+userResponse.getJSONObject(i).getString("last_name");
            results.add(data);
        }
        // Add some more dummy data for testing
        return results;
    }




}