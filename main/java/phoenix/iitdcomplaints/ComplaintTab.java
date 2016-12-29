package phoenix.iitdcomplaints;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gautam on 3/29/2016.
 */
public class ComplaintTab extends Fragment {
    View view;
    TextView title;
    TextView des;
    TextView status;
    TextView level;
    TextView tags;
    TextView upvote;
    TextView downvote;
    TextView filed_by;
    TextView concerned;
    TextView resolved;
    TextView created;
    private JSONObject complaintResponse;
    private JSONObject userComplaintResponse;
    int upvotes;
    int downvotes;
    ImageView like;
    ImageView dislike;
    ProgressDialog pd;
    int vote;
    String complaint_id;
    String createdName;
    String concernedName;
    String resolvedName;
    String user_id;
    int sta=0;
    int i;


    public ComplaintTab() {
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

        view= inflater.inflate(R.layout.view_complaint, container, false);
        pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

        title=(TextView) view.findViewById(R.id.title);
       des=(TextView) view.findViewById(R.id.description);
       status= (TextView) view.findViewById(R.id.status);
        level=(TextView) view.findViewById(R.id.level);
        tags=(TextView) view.findViewById(R.id.tags);
        upvote=(TextView) view.findViewById(R.id.upvote);
        downvote=(TextView) view.findViewById(R.id.downvote);
        filed_by=(TextView) view.findViewById(R.id.filedby);
        concerned=(TextView) view.findViewById(R.id.concerned);
        resolved=(TextView) view.findViewById(R.id.resolved);
        created=(TextView) view.findViewById(R.id.created_at);
        like=(ImageView) view.findViewById(R.id.like);

        dislike=(ImageView) view.findViewById(R.id.dislike);
        i=0;

        try {
            complaintResponse=new JSONObject(getArguments().getString("response"));
            userComplaintResponse=new JSONObject(getArguments().getString("userComplaint"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            showThings();
        } catch (JSONException e) {
            Toast.makeText(view.getContext(),e.toString()+"No,Its from here" , Toast.LENGTH_LONG).show();


            e.printStackTrace();
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=1;
                like();
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = -1;
                like();
            }
        });
        pd.dismiss();

        try {
            String r=   complaintResponse.getJSONArray("complaint").getJSONObject(0).getString("resolvedby_id");

            if(Integer.valueOf(user_id)==Integer.valueOf(r)){
                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     change();
                    }
                });
            }

          sta=Integer.valueOf( complaintResponse.getJSONArray("complaint").getJSONObject(0).getString("status"));




        } catch (JSONException e) {
            e.printStackTrace();
        }


    return view;
    }

    private void change() {

        pd= ProgressDialog.show(view.getContext(), "Loading..", "Please Wait", true, true);

        String initial = getResources().getString(R.string.server_host);
        int z=0;
        if(sta==0){
            z=1;
        }


        final String url = initial + "/default/changeStatus.json?status=" + z + "&complaint_id=" +complaint_id ;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    if(Response.getString("success").equals("true")){

                        if(sta==1){
                             sta=0;
                            status.setText("Pending");
                        }
                        else{
                            sta=0;
                            status.setText("Resolved");


                        }

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


    private void like() {
             if(vote!=0){
                 Toast.makeText(view.getContext(),"Already voted!!", Toast.LENGTH_LONG).show();

             }
  else {
                 pd= ProgressDialog.show(view.getContext(), "Loading Data", "Please Wait", true, true);

                 String initial = getResources().getString(R.string.server_host);
                 final String url = initial + "/default/vote.json?vote=" + i + "&complaint_id=" +complaint_id ;


                 JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject Response) {

                             if(i==1){
                                 int s=upvotes+1;
                                 upvote.setText(" Upvotes "+s);
                             }
                             else{
                                  int s=downvotes+1;
                                 downvote.setText(" Downvotes " + s);

                             }


                         vote=i;

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


    }

    private void showThings() throws JSONException {
        JSONObject o=complaintResponse.getJSONArray("complaint").getJSONObject(0);

        JSONObject su=userComplaintResponse;
        title.setText(o.getString("title"));
        des.setText(o.getString("description"));
        if(o.getString("status").equals("0")) {
            status.setText("Status:-Pending");
        }
       else status.setText("Status:-Resolved");
        int f=Integer.valueOf(o.getString("complaintslevel"));
      if(f==1){
          level.setText("Complaint Level:- Individual");
      }
    else if(f==2){
          level.setText("Complaint Level:- Hostel");
      }
        else{
            level.setText("Complaint Level:- Institute");
        }
        tags.setText("Tags:-"+o.getString("tags"));
         created.setText("Created at:-"+o.getString("createdat"));
        upvotes=Integer.valueOf(o.getString("up_votes"));
        upvote.setText(" Upvotes "+o.getString("up_votes"));
          downvotes=Integer.valueOf(o.getString("down_votes"));
        downvote.setText(" Downvotes " + o.getString("down_votes"));
       vote=Integer.valueOf(su.getString("vote"));
      complaint_id=o.getString("id");
     user_id=su.getString("user_id");


        String initial = getResources().getString(R.string.server_host);
        final String url = initial + "/default/userDetails.json?user_id=" +o.getString("user_id") ;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    JSONObject object =Response.getJSONObject("user");
                      createdName=object.getString("first_name")+object.getString("last_name");
                      filed_by.setText("Filed by:-"+createdName);
                    resolvedName();

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
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);




    }

    private void resolvedName() throws JSONException {
        String initial = getResources().getString(R.string.server_host);
        JSONObject o=complaintResponse.getJSONArray("complaint").getJSONObject(0);
        final String url = initial + "/default/userDetails.json?user_id=" +o.getString("resolvedby_id") ;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    JSONObject object=Response.getJSONObject("user");
                    resolvedName=object.getString("first_name")+object.getString("last_name");
                    resolved.setText("To be Resolved by:-" + resolvedName);
                    conName();

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
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);





    }

    private void conName() throws JSONException {
        String initial = getResources().getString(R.string.server_host);
        JSONObject o=complaintResponse.getJSONArray("complaint").getJSONObject(0);
        final String url = initial + "/default/userDetails.json?user_id=" +o.getString("concerned_user_id") ;


        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    JSONObject object=Response.getJSONObject("user");
                    concernedName=object.getString("first_name")+object.getString("last_name");
                    concerned.setText("Concerned to:-" + concernedName);
                    pd.dismiss();

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
        AppController.getInstance().

                addToRequestQueue(jsonObjectRequest, tag_json_obj);







    }


}
