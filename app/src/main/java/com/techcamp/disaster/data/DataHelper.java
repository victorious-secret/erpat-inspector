package com.techcamp.disaster.data;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.disaster.models.Site;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 5/10/14.
 */
public class DataHelper {


    public static List<Site> getSitesFromJson(JSONArray jsonArray) {
        ArrayList<Site> results = new ArrayList<Site>();
        try {
            for (int i =0 ; i < jsonArray.length(); i++) {
                JSONObject siteData = jsonArray.getJSONObject(i);
                Site site = new Site();
                site.setRowId(siteData.getInt("id"));
                site.setId(siteData.getString("id"));
                site.setAddress(siteData.getString("address"));
                site.setStatus(siteData.getString("status"));
                site.setName(siteData.getString("full_name"));
                site.setPhone(siteData.getString("contact_number"));
                if (site.getStatus().equals("queued")) {
                    results.add(site);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;

    }

    public static Site getSiteFromJson(JSONObject siteData) {
        try {
            Site site = new Site();
            site.setRowId(siteData.getInt("id"));
            site.setId(siteData.getString("id"));
            site.setAddress(siteData.getString("address"));
            LatLng location = new LatLng(siteData.getDouble("lat"), siteData.getDouble("long"));
            site.setLocation(location);
            site.setName(siteData.getString("full_name"));
            site.setPhone(siteData.getString("contact_number"));
            return site;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Site getSiteFromJson(String response) {
        JSONObject siteData = null;
        try {
            siteData = new JSONObject(response);
            Site site = new Site();
            site.setRowId(siteData.getInt("id"));
            site.setId(siteData.getString("site_id"));
            site.setAddress(siteData.getString("address"));

            JSONObject locationData = siteData.getJSONObject("location");
            LatLng location = new LatLng(locationData.getDouble("lat"), locationData.getDouble("long"));
            site.setLocation(location);
            JSONObject contact = siteData.getJSONObject("contact");
            site.setName(contact.getString("name"));
            site.setPhone(contact.getString("phone"));
            return site;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return null;
    }
}
