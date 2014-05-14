package com.techcamp.disaster.data;

import com.techcamp.disaster.models.Site;

/**
 * Created by joseph on 5/10/14.
 */
public interface OnSiteDetailListener {
    void onQueryComplete(Site site);

    void onFailure();
}
