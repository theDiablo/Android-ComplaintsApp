package phoenix.iitdcomplaints;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam on 3/28/2016.
 */
public class ViewComplaint extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
     private JSONObject complaintResponse;
    private JSONObject userComplaintResponse;

    ProgressDialog pd;
    Toolbar toolbar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd= ProgressDialog.show(this, "Loading Data", "Please Wait", true, true);
        setContentView(R.layout.activity_action_bar_tabs);

        try {
            complaintResponse=new JSONObject(getIntent().getStringExtra("complaintInfo"));
            userComplaintResponse=new JSONObject(getIntent().getStringExtra("userComplaint"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        pd.dismiss();




    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //creating the action tabs dynamically
        {
            Fragment f = new ComplaintTab();
            Bundle b= new Bundle();
            b.putString("response", complaintResponse.toString());
            b.putString("userComplaint",userComplaintResponse.toString());

            f.setArguments(b);
            adapter.addFragment(f, "Complaint");
        }
        {
            Fragment f = new CommentTab();
            Bundle b= new Bundle();

            b.putString("response", complaintResponse.toString());
            b.putString("userComplaint",userComplaintResponse.toString());

            f.setArguments(b);
            adapter.addFragment(f, "Comments");
        }

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }





}
