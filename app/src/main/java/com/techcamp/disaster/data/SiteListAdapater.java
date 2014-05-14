package com.techcamp.disaster.data;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.techcamp.disaster.DisplayDetailsActivity;
import com.techcamp.disaster.R;
import com.techcamp.disaster.models.Site;

import java.util.List;

/**
 * Created by joseph on 5/10/14.
 */
public class SiteListAdapater implements ListAdapter {

    private final List<Site> sites;
    Activity context;

    public SiteListAdapater(Activity context, List<Site> sites) {
        this.sites = sites;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int i) {
        return sites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sites.get(i).getRowId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Site site = sites.get(i);
        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.list_item_defails, null);
        }
        TextView siteId = (TextView)view.findViewById(R.id.siteId);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView address = (TextView)view.findViewById(R.id.address);
        Button button = (Button)view.findViewById(R.id.buttonInspectSite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisplayDetailsActivity.class);
                intent.putExtra("id", site.getRowId());
                context.startActivity(intent);
            }
        });
        address.setText(site.getAddress());
        name.setText(site.getName());

        siteId.setText(site.getId());
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
