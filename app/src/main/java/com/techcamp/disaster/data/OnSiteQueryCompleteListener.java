package com.techcamp.disaster.data;

import com.techcamp.disaster.models.Site;

import java.util.List;

/**
 * Created by joseph on 5/10/14.
 */
public interface OnSiteQueryCompleteListener {
    void onQueryComplete(List<Site> sites);

    void onFailure();
}
