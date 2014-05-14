package com.techcamp.disaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.techcamp.disaster.base.BaseActivity;
import com.techcamp.disaster.data.DataManager;
import com.techcamp.disaster.data.OnSiteQueryCompleteListener;
import com.techcamp.disaster.data.SiteListAdapater;
import com.techcamp.disaster.models.Site;
import com.techcamp.disaster.utils.Utils;

import java.util.List;


public class MainActivity extends BaseActivity {

    private ListView siteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        siteListView = (ListView) findViewById(R.id.site_list);
        if (Utils.getUser(this)== null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            querySiteList();
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, new MyLocationListener());
    }

    private void querySiteList() {
        DataManager.getInstance().querySiteList(this, new OnSiteQueryCompleteListener() {

            @Override
            public void onQueryComplete(List<Site> sites) {
                siteListView.setAdapter(new SiteListAdapater(MainActivity.this, sites));
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.refresh:
                querySiteList();
                break;
            case R.id.logout:
                Utils.clearPreferences(this);
                finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
