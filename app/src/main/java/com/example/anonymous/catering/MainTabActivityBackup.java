package com.example.anonymous.catering;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ui.activities.LoginActivity;
import ui.fragments.HomeFragment;
import ui.fragments.ThreeFragment;
import ui.fragments.PaketMenuFragment;

public class MainTabActivityBackup extends AppCompatActivity {
    private static final String TAG = "MainTabActivity";
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.home2,
            R.drawable.order,
            R.drawable.user
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "ONE");
        adapter.addFragment(new PaketMenuFragment(), "TWO");
        adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_leave) {
            //Toast.makeText(MainTabActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            Logout();
            return true;
        }

        else if (id == R.id.action_refresh){
            Log.i(TAG, "Refresh menu item selected");
            Toast.makeText(MainTabActivityBackup.this, "Action clicked refresh", Toast.LENGTH_LONG).show();
            // Signal SwipeRefreshLayout to start the progress indicator
            //sl_refresh.setRefreshing(true);

            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            //myUpdateOperation();

            return true;

        }
        else if (id == R.id.action_feedback){
            Log.i(TAG, "Refresh menu item selected");
            Toast.makeText(MainTabActivityBackup.this, "Action clicked feedback", Toast.LENGTH_LONG).show();
            // Signal SwipeRefreshLayout to start the progress indicator
            //sl_refresh.setRefreshing(true);

            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            //myUpdateOperation();

            return true;

        }
        else if (id == R.id.action_settings){
            Log.i(TAG, "Refresh menu item selected");
            Toast.makeText(MainTabActivityBackup.this, "Action clicked settings", Toast.LENGTH_LONG).show();
            // Signal SwipeRefreshLayout to start the progress indicator
            //sl_refresh.setRefreshing(true);

            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            //myUpdateOperation();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void Logout() {
        auth.signOut();
        startActivity(new Intent(MainTabActivityBackup.this, LoginActivity.class));
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
            return null;
        }
    }
}