/*
 * Copyright (c) 2011-2019 HERE Global B.V. and its affiliate(s).
 * All rights reserved.
 * The use of this software is conditional upon having a separate agreement
 * with a HERE company for the use or utilization of this software. In the
 * absence of such agreement, the use of the software is not allowed.
 */
package com.here.android.tutorial;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapOverlay;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeRequest2;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.Location;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

public class BasicMapActivity extends FragmentActivity {

    private ArrayList<MapObject> markers = new ArrayList<>();
    private ArrayList<MapOverlay> burbujas = new ArrayList<>();
    private PositioningManager positioningManager = null;
    private PositioningManager.OnPositionChangedListener positionListener;
    private GeoCoordinate currentPosition;
    private boolean isMarked = false;
    private SharedPreferences mPrefs;
    private Button button;

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map = null;

    // map fragment embedded in this activity
    private AndroidXMapFragment mapFragment = null;

    private EditText editText = null;
    private MapMarker marker;
    private Integer radioSeleccionado = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //final EditTextPreference victoryMessagePref = (EditTextPreference) findPreference("victory_message");
        String radio = mPrefs.getString("radio",
                getResources().getString(R.string.radio));
        try{
            radioSeleccionado = Integer.parseInt(radio);
        }catch (Exception e){
            System.out.println("Excepcion: " + e);
        }
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    @SuppressWarnings("deprecation")
    private void initialize() {
        setContentView(R.layout.activity_main);

        // Search for the map fragment to finish setup by calling init().
        mapFragment = getMapFragment();

        editText = (EditText) findViewById(R.id.query);

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search(editText.getText().toString());
                    //editText.setText("");
                    return true;
                }
                return false;
            }
        });

        // Set up disk cache path for the map service for this application
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "com.here.android.tutorial.MapService");

        if (!success) {
            Toast.makeText(getApplicationContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG);
        } else {
            mapFragment.init(new OnEngineInitListener() {
                //currentPosition = new GeoCoordinate(37.7397, -121.4252);

                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        map = mapFragment.getMap();

                        mapFragment.getMapGesture().addOnGestureListener(new MyOnGestureListener(),1,true);

                        map.setCenter(new GeoCoordinate(37.7397, -121.4252, 0.0), Map.Animation.NONE);
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                        positioningManager = PositioningManager.getInstance();
                        positionListener = new PositioningManager.OnPositionChangedListener() {
                            @Override
                            public void onPositionUpdated(PositioningManager.LocationMethod method, GeoPosition position, boolean isMapMatched) {
                                if(!isMarked) {
                                    currentPosition = position.getCoordinate();
                                    map.setCenter(position.getCoordinate(), Map.Animation.NONE);
                                }
                            }
                            @Override
                            public void onPositionFixChanged(PositioningManager.LocationMethod method, PositioningManager.LocationStatus status) { }
                        };

                        try {
                            positioningManager.addListener(new WeakReference<>(positionListener));
                            if(!positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK)) {
                                Log.e("HERE", "PositioningManager.start: Failed to start...");
                            }
                        } catch (Exception e) {
                            Log.e("HERE", "Caught: " + e.getMessage());
                        }
                        map.getPositionIndicator().setVisible(true);
                    }
                }
            }

            );
            /*mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    marker = new MapMarker();
                    if (error == OnEngineInitListener.Error.NONE) {
                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();

                        MapMarker defaultMarker = new MapMarker();
                        defaultMarker.setCoordinate(new GeoCoordinate(49.196261, -123.004773, 0.0));
                        map.addMapObject(defaultMarker);

                        // Set the zoom level to the average between min and max
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);

                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                //showDialog(DIALOG_DIFFICULTY_ID);
                return true;
        }
        return false;
    }

    /**
     * Checks the dynamically controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    public void dropMarker(String query) {
        if(marker != null) {
            map.removeMapObject(marker);
        }
        GeoCoordinate tracy = new GeoCoordinate( 37.7397,-121.4252);
        final GeocodeRequest request = new GeocodeRequest(query).setSearchArea(tracy, 5000);

        //GeocodeRequest2 request = new GeocodeRequest2(query).setSearchArea(tracy, 5000);
        request.execute(new ResultListener<List<GeocodeResult>>() {
            @Override
            public void onCompleted(List<GeocodeResult> results, ErrorCode error) {
                if (error != ErrorCode.NONE) {
                    Log.e("HERE !!!!", error.toString());
                } else {
                    for (GeocodeResult result : results) {
                        marker = new MapMarker();
                        marker.setCoordinate(new GeoCoordinate(result.getLocation().getCoordinate().getLatitude(), result.getLocation().getCoordinate().getLongitude(), 0.0));
                        System.out.println(result.getLocation().getCoordinate().getLatitude() + " " + result.getLocation().getCoordinate().getLongitude());
                        map.addMapObject(marker);
                    }
                }
            }
        });
    }

    public void search(String query) {
        if(!markers.isEmpty()) {
            map.removeMapObjects(markers);
            markers.clear();
        }
        try {
            DiscoveryRequest request = new SearchRequest(query).setSearchArea(currentPosition, radioSeleccionado*1000);
            //DiscoveryRequest request = new SearchRequest(query).setSearchCenter(currentPosition);
            request.setCollectionSize(10);
            ErrorCode error = request.execute(new ResultListener<DiscoveryResultPage>() {
                @Override
                public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode error) {
                    if (error != ErrorCode.NONE) {
                        Log.e("HERE", error.toString());
                    } else {
                        if(!discoveryResultPage.getItems().isEmpty())
                            isMarked = true;
                        else
                            isMarked = false;
                        for(DiscoveryResult discoveryResult : discoveryResultPage.getItems()) {
                            if(discoveryResult.getResultType() == DiscoveryResult.ResultType.PLACE) {
                                PlaceLink placeLink = (PlaceLink) discoveryResult;
                                MapMarker marker = new MapMarker();
                                marker.setCoordinate(placeLink.getPosition());
                                marker.setTitle(placeLink.getTitle());
                                markers.add(marker);
                                map.addMapObjects(markers);
                            }
                        }
                    }
                }


            });
            if( error != ErrorCode.NONE ) {
                Log.e("HERE", error.toString());
            }
        } catch (IllegalArgumentException ex) {
            Log.e("HERE", ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings
            String radio  = mPrefs.getString("radio", getResources().getString(R.string.radio));
            try{
                radioSeleccionado = Integer.parseInt(radio);
            }catch (Exception e){
                System.out.println("Excepcion: " + e);
            }

        }
    }

    // Map gesture listener
    private class MyOnGestureListener implements MapGesture.OnGestureListener {

        @Override
        public void onPanStart() {
        }

        @Override
        public void onPanEnd() {
        }

        @Override
        public void onMultiFingerManipulationStart() {
        }

        @Override
        public void onMultiFingerManipulationEnd() {
        }

        @Override
        public boolean onMapObjectsSelected(List<ViewObject> objects) {
            // objects list holds all markers which are clicked.
            for (ViewObject viewObj : objects) {
                if (viewObj.getBaseType() == ViewObject.Type.USER_OBJECT) {
                    if (((MapObject) viewObj).getType() == MapObject.Type.MARKER) {
                        final MapMarker selectedMarker = ((MapMarker) viewObj);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                double distance = Math.round(Math.abs(currentPosition.distanceTo(selectedMarker.getCoordinate())));

                                Toast toast = Toast.makeText(getApplicationContext(), selectedMarker.getTitle() + "\nDistancia: " + distance + " metros", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP|Gravity.LEFT, 200, 600);
                                View toastView = toast.getView();
                                toastView.setBackgroundResource(R.drawable.toast_background);
                                toast.show();
                            }
                        });
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onTapEvent(PointF p) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(PointF p) {
            return false;
        }

        @Override
        public void onPinchLocked() {
        }

        @Override
        public boolean onPinchZoomEvent(float scaleFactor, PointF p) {
            return false;
        }

        @Override
        public void onRotateLocked() {
        }

        @Override
        public boolean onRotateEvent(float rotateAngle) {
            return false;
        }

        @Override
        public boolean onTiltEvent(float angle) {
            return false;
        }

        @Override
        public boolean onLongPressEvent(PointF p) {
            return false;
        }

        @Override
        public void onLongPressRelease() {
        }

        @Override
        public boolean onTwoFingerTapEvent(PointF p) {
            return false;
        }
    }

}

