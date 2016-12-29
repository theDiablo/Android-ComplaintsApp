package phoenix.iitdcomplaints;

/**
 * Created by Gautam on 3/28/2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

//setting the profile info of a person
//if type=1 then display faculty else display student
//similarly user name and email id
public class ProfileFragment extends Fragment {
    View rootView;
    JSONObject response;


    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile, container, false);
        String r = getArguments().getString("response");
        try {
            JSONObject res = new JSONObject(r);
            response = res.getJSONObject("user");

            TextView t = (TextView) rootView.findViewById(R.id.name);
            t.setText("Name : " + response.getString("first_name") + " " +response.getString("last_name"));

            TextView d = (TextView) rootView.findViewById(R.id.username);
            d.setText("Username : " + response.getString("username"));

            TextView u = (TextView) rootView.findViewById(R.id.entry);
            u.setText("Entry number : " + response.getString("entry_no"));
            TextView m = (TextView) rootView.findViewById(R.id.mail);
            m.setText("Email : " + response.getString("email"));
            TextView f = (TextView) rootView.findViewById(R.id.type);
            if (response.getString("type_").equals("student")) {
                f.setText("Type : Student" );
            }
            else if (response.getString("type_").equals("professor"))
            {
                f.setText("Type : Professor" );
            }
            else if (response.getString("type_").equals("warden"))
            {
                f.setText("Type : Warden" );
            }
            else if (response.getString("type_").equals("worker"))
            {
                f.setText("Type : Worker" );
            }
            else
            {
                f.setText("Type : Dean" );
            }

            TextView h = (TextView) rootView.findViewById(R.id.hostel);
            if (response.getString("hostel").equals("None")) {
                h.setText("Hostel : Not applicable");
            }
            else
            {
                h.setText("Hostel : " + response.getString("hostel") );
            }

            TextView p = (TextView) rootView.findViewById(R.id.por);
            if (response.getString("por").equals("None")) {
                p.setText("Position of Responsibility : Not applicable");
            }
            else
            {
                p.setText("Position of Responsibility : " + response.getString("por") );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rootView;
    }
}