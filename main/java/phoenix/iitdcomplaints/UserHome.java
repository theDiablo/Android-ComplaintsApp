package phoenix.iitdcomplaints;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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

import java.util.ArrayList;

/**
 * Created by Gautam on 3/27/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
 * Created by Gautam on 2/20/2016.
 */
//main home page of the user with navigation drawer to access Fragments Grades, Notifications, Courses, Profile and Logout option

public class UserHome extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private JSONObject profileResponse;
    private JSONObject notificationResponse;
    private JSONObject complaintsResponse;
    private JSONObject myComplaintsResponse;
    private JSONObject peopleResponse;
    private FloatingActionButton fab;

    private int numberOfNotifi;
    AlertDialog ad;

    // nav drawer title
    private CharSequence mDrawerTitle;
    private Toolbar toolbar;
    // used to store app title
    private CharSequence mTitle;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hv_navigator);
        //  pd=ProgressDialog.show(this,"Loading Data","Please Wait",true,true);
        //pd= new ProgressDialog(NavHome.this, ProgressDialog.STYLE_SPINNER);
        //   pd.setIndeterminate(true);
        //pd.show(NavHome.this, "PROG_DIALOG", "Getting machine status...");


        getResponses();

        mTitle = mDrawerTitle = getTitle();

       mDrawerLayout = (DrawerLayout) findViewById(R.id.hv_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        addItems();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        //   getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //  getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setActionBar();
        /*/  setSupportActionBar(toolbar);
        Button probtn=(Button) findViewById(R.id.probutton);
        ///titleText.setText("MoodlePlus");
        toolbar.findViewById(R.id.probutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavHome.this, Home.class);
                startActivity(intent);
            }
        });*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
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

                // update selected item and title, then close the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });


    }




    private void addItems() {
        navDrawerItems.add(new NavDrawerItem("Overview", R.drawable.ic_home));
        navDrawerItems.add(new NavDrawerItem("Profile",R.drawable.student));
        navDrawerItems.add(new NavDrawerItem("Logout",R.drawable.ic_menu_send));

        navDrawerItems.add(new NavDrawerItem("Notifications["+numberOfNotifi+"]", R.drawable.rsz_notifications_icon));

       navDrawerItems.add(new NavDrawerItem("My Complaints", R.drawable.student));
        navDrawerItems.add(new NavDrawerItem("File new Complaint", R.drawable.student));

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    private void displayView(int position) {
        // update the main content by replacing fragments



        Fragment fragment = null;
        if(position==0) {
            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("profileResponse", profileResponse.toString());
            bundle.putString("peopleResponse", peopleResponse.toString());

            fragment.setArguments(bundle);

        }

        if(position==2){


            ad= new AlertDialog.Builder(this)
                    .setTitle("Wait..")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           letslogout();


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ad.dismiss();          // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if(position==1) {
            fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("response", profileResponse.toString());
            fragment.setArguments(bundle);

        }



        if(position==3) {
            fragment = new NotificationFragment();
        }

        if(position==4) {
            fragment = new MyComplaint();
          }

        if(position==5) {
            fragment = new NewComplaint();
            Bundle bundle = new Bundle();
            bundle.putString("peopleResponse", peopleResponse.toString());
            bundle.putString("profileResponse",profileResponse.toString());
            fragment.setArguments(bundle);
            }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();

           fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private void letslogout() {

        String initial = getResources().getString(R.string.server_host);
        String url = initial + "/default/logout.json";

        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {

                Intent i=new Intent(UserHome.this,Login.class);
                ad.dismiss();
                startActivity(i);
                finish();

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Error) {


                        Toast.makeText(UserHome.this, Error.toString().trim(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);



    }


    private void getResponses() {
        Intent intent=getIntent();
        try {
            profileResponse=new JSONObject(intent.getStringExtra("response"));
            notificationResponse=new JSONObject(intent.getStringExtra("notiResponse"));
            numberOfNotifi=notificationResponse.getJSONArray("notifications").length();

            Toast.makeText(UserHome.this, numberOfNotifi+" New Notifications", Toast.LENGTH_LONG).show();


            myComplaintsResponse=new JSONObject(intent.getStringExtra("myComplaintsResponse"));
            complaintsResponse=new JSONObject(intent.getStringExtra("complaintsResponse"));
            peopleResponse=new JSONObject(intent.getStringExtra("peopleResponse"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }




}
