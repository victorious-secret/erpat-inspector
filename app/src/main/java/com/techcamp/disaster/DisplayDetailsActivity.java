package com.techcamp.disaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techcamp.disaster.base.BaseActivity;
import com.techcamp.disaster.data.DataManager;
import com.techcamp.disaster.data.OnInspectionListener;
import com.techcamp.disaster.data.OnSiteDetailListener;
import com.techcamp.disaster.models.Site;


public class DisplayDetailsActivity extends BaseActivity {

    private GoogleMap mMap;
    private TextView siteName;
    private TextView siteAdress;
    private Button takePhotoButton;
    private ImageView mImageView;
    private Button approveClearance;
    private Button denyClearance;
    private Site site;
    private EditText notes;
    private View detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        long id = getIntent().getLongExtra("id", 0);

        siteName = (TextView)findViewById(R.id.site_name);
        siteAdress = (TextView)findViewById(R.id.site_address);
        notes = (EditText)findViewById(R.id.inspectionNotes);
        mImageView = (ImageView)findViewById(R.id.imageViewSite);
        mImageView.setVisibility(View.GONE);
        takePhotoButton = (Button) findViewById(R.id.take_photo);
        detailView = (View)findViewById(R.id.detailView);
        approveClearance = (Button) findViewById(R.id.approve);
        denyClearance = (Button) findViewById(R.id.deny);
        detailView.setVisibility(View.GONE);


        approveClearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailsActivity.this);
                builder.setMessage("Are you sure you want to Approve this Site?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataManager.getInstance().postInspectionResults(DisplayDetailsActivity.this, site.getRowId(), notes.getText().toString(), 0, new OnInspectionListener() {
                            @Override
                            public void onQueryComplete() {
                                Toast.makeText(DisplayDetailsActivity.this, "Site Approved", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        denyClearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailsActivity.this);
                builder.setMessage("Are you sure you want to Reject this Site?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataManager.getInstance().postInspectionResults(DisplayDetailsActivity.this, site.getRowId(), notes.getText().toString(), 1, new OnInspectionListener() {
                            @Override
                            public void onQueryComplete() {
                                Toast.makeText(DisplayDetailsActivity.this, "Site Rejected", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });


                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, new MyLocationListener());

        DataManager.getInstance().querySiteDetail(this, id,  new OnSiteDetailListener() {

            @Override
            public void onQueryComplete(Site site) {
                detailView.setVisibility(View.VISIBLE);
                DisplayDetailsActivity.this.site = site;
                siteName.setText(site.getName());
                siteAdress.setText(site.getAddress());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(site.getLocation(),19f);
                mMap.moveCamera(cameraUpdate);

                MarkerOptions options = new MarkerOptions();
                options.title(site.getAddress());
                options.position(site.getLocation());
                Marker marker = mMap.addMarker(options);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=" + marker.getPosition().latitude + ","
                                        + marker.getPosition().longitude + " (" + marker.getTitle()
                                        + ")"));
                        startActivity(intent);
                        return true;
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_details, menu);
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
