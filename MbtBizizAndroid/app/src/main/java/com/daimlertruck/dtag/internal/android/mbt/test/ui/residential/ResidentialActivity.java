package com.daimlertruck.dtag.internal.android.mbt.test.ui.residential;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.MeetingRoomAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivityResidentialBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.Building;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.MeetingRoom;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.Residential;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ResidentialActivity extends BaseActivity<ActivityResidentialBinding> implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String KEY_BUILD = "building";
    private static final String KEY_IS_SEARCH = "isSearch";
    private static final String KEY_MEETING_ROOM = "meetingRoom";
    private BottomSheetBehavior mBottomSheetBehavior;
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.residential.VMResidential vmResidential;
    private MapView mapView;
    private GoogleMap gmap;
    private Building building;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private boolean isSearch = false;
    private Building searchBuilding;
    private Marker lastMarker;

    public static void start(Context context) {
        Intent starter = new Intent(context, ResidentialActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, Building building, MeetingRoom meetingRoom) {
        Intent starter = new Intent(context, ResidentialActivity.class);
        starter.putExtra(KEY_BUILD, building);
        starter.putExtra(KEY_IS_SEARCH, true);
        starter.putExtra(KEY_MEETING_ROOM, meetingRoom);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_residential;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            isSearch = getIntent().getExtras().getBoolean(KEY_IS_SEARCH, false);
            searchBuilding = getIntent().getExtras().getParcelable(KEY_BUILD);
            MeetingRoom room = getIntent().getExtras().getParcelable(KEY_MEETING_ROOM);
            for (MeetingRoom rm : searchBuilding.getMeetingRoomList()) {
                if (rm.getId() == room.getId()) {
                    rm.setSelected(true);
                }
            }
        }

        toolbarSetVM();
        listenBottomSheet();
        binding.recyclerRooms.setNestedScrollingEnabled(false);
        vmResidential = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.residential.VMResidential.class);
        observe();
        if (!isSearch) vmResidential.getBuildings();
        else {
            binding.edtSearch.setVisibility(View.GONE);
            binding.linearPlace.setVisibility(View.GONE);
            setBottomAdapter(searchBuilding);
            binding.split.setVisibility(View.GONE);
        }
        setListener();
        binding.tvTabFirst.setActivated(true);
        binding.tvTabSecond.setActivated(false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = binding.mapView;
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);


    }

    private void setListener() {
        View.OnClickListener onClickListener = v -> {
            binding.imageView5.setRotation(binding.imageView5.getRotation() == 0f ? 180f : 0f);
            mBottomSheetBehavior.setState(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);

        };


        binding.tvBuildName.setOnClickListener(onClickListener);
        binding.tvBuildRoomCount.setOnClickListener(onClickListener);
        binding.tvBuildNumber.setOnClickListener(onClickListener);
        binding.imageView5.setOnClickListener(onClickListener);

        View.OnClickListener tabListener = v -> {
            switch (v.getId()) {
                case R.id.tvTabFirst:
                    binding.tvTabFirst.setActivated(true);
                    binding.tvTabSecond.setActivated(false);
                    binding.tvTabThird.setActivated(false);
                    addMarkers();
                    try {
                        setBottomAdapter(getSelectedBuilding().getBuildings().get(0));
                    } catch (Exception e) {
                    }

                    break;

                case R.id.tvTabSecond:
                    binding.tvTabFirst.setActivated(false);
                    binding.tvTabSecond.setActivated(true);
                    binding.tvTabThird.setActivated(false);

                    addMarkers();
                    try {
                        setBottomAdapter(getSelectedBuilding().getBuildings().get(0));
                    } catch (Exception e) {
                    }

                    break;
                case R.id.tvTabThird:
                    binding.tvTabFirst.setActivated(false);
                    binding.tvTabSecond.setActivated(false);
                    binding.tvTabThird.setActivated(true);

                    addMarkers();
                    try {
                        setBottomAdapter(getSelectedBuilding().getBuildings().get(0));
                    } catch (Exception e) {
                    }

                    break;
                case R.id.edtSearch:
                    ArrayList<Residential> list = new ArrayList<>();
                    list.addAll(vmResidential.data.getValue());
                    com.daimlertruck.dtag.internal.android.mbt.test.ui.residential.SearchRoomActivity.start(ResidentialActivity.this, list);
                    break;


            }
        };
        binding.tvTabFirst.setOnClickListener(tabListener);
        binding.tvTabSecond.setOnClickListener(tabListener);
        binding.tvTabThird.setOnClickListener(tabListener);
        binding.edtSearch.setOnClickListener(tabListener);
    }

    private Residential getSelectedBuilding() {
        for (Residential residential : vmResidential.data.getValue()) {
            if (binding.tvTabFirst.isActivated() && residential.getId() == 2) {
                return residential;
            } else if (binding.tvTabSecond.isActivated() && residential.getId() == 1) {
                return residential;
            }
            else if (binding.tvTabThird.isActivated() && residential.getId() == 3) {
                return residential;
            }
        }
        return null;
    }

    private void observe() {
        vmResidential.data.observe(this, buildings -> {
            if (buildings != null) {


                for (Residential building : buildings) {
                    if (building.isDefault()) {
                        setBottomAdapter(building.getBuildings().get(0));
                        if (building.getId() == 1) {
                            binding.tvTabFirst.setActivated(false);
                            binding.tvTabSecond.setActivated(true);
                            binding.tvTabThird.setActivated(false);

                        } else if (building.getId() == 2) {
                            binding.tvTabFirst.setActivated(true);
                            binding.tvTabSecond.setActivated(false);
                            binding.tvTabThird.setActivated(false);

                        } else if (building.getId() == 3) {
                            binding.tvTabFirst.setActivated(false);
                            binding.tvTabSecond.setActivated(false);
                            binding.tvTabThird.setActivated(true);

                        }
                        addMarkers();
                    }
                    if (building.getId() == 1) {
                        binding.tvTabSecond.setText(building.getName());
                    } else if (building.getId() == 2) {
                        binding.tvTabFirst.setText(building.getName());
                    } else if (building.getId() == 3) {
                        binding.tvTabThird.setText(building.getName());
                    }
                }
            }
        });
        vmResidential.loading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setBottomAdapter(Building building) {
        building.setSelected(true);
        if (this.building != null) this.building.setSelected(false);
        this.building = building;
        binding.tvBuildNumber.setText(building.getOrder().toString());
        if (building.isEmergencyMeetingPlace()) binding.tvBuildNumber.setVisibility(View.INVISIBLE); else binding.tvBuildNumber.setVisibility(View.VISIBLE);
        binding.tvBuildName.setText(building.getName());
        binding.tvBuildRoomCount.setText(String.valueOf(building.getMeetingRoomList().size()) + " " + getString(R.string.TXT_LOCATIONS_LOCATIONS_MEETING_ROOM));
        MeetingRoomAdapter adapter = new MeetingRoomAdapter(building.getMeetingRoomList());
        binding.recyclerRooms.setAdapter(adapter);

    }

    private void toolbarSetVM() {
        try {
            if (isSearch) {
                VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOCATIONS_SEARCH_RESULTS_TITLE), searchBuilding.getName());
                binding.toolbar.setVm(vmToolbar);
            } else {
                VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOCATIONS_LOCATIONS_TITLE), null);
                binding.toolbar.setVm(vmToolbar);
            }
        } catch (Exception e) {
        }

    }

    private void listenBottomSheet() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        binding.imageView5.setRotation(180f);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        binding.imageView5.setRotation(0f);

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        binding.imageView5.setRotation(0f);

                       /* if (building != null) {
                            MeetingRoomAdapter adapter = new MeetingRoomAdapter(building.getMeetingRoomList());
                            binding.recyclerRooms.setAdapter(adapter);
                        }*/
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        binding.imageView5.setRotation(180f);


                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap = googleMap;

        gmap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.map_style));
        gmap.setMapType(gmap.MAP_TYPE_SATELLITE);
        //gmap.setMinZoomPreference(12);
        //gmap.setMaxZoomPreference(18);
        gmap.getUiSettings().setZoomControlsEnabled(false);
        addMarkers();
        gmap.setOnMarkerClickListener(this);
        gmap.getUiSettings().setMapToolbarEnabled(false);

    }

    private void addMarkers() {
        lastMarker = null;
        if (isSearch) {
            addMarkersForSearch();
            return;
        } else if (vmResidential.data.getValue() == null || gmap == null) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        gmap.clear();
        //Emergency Point
        for (Building building : getSelectedBuilding().getEmergencyPoints()) {
            building.setEmergencyMeetingPlace(true);
            LatLng latLng = new LatLng(Double.valueOf(building.getLatitude()), Double.valueOf(building.getLongitude()));
            builder.include(latLng);

            MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerEmergencyBitmapFromView(building.getShortName())));
            markerOptions.alpha(0.6f);
            gmap.addMarker(markerOptions).setTag(building);
        }
        //Normal buildings
        for (Building building : getSelectedBuilding().getBuildings()) {
            building.setEmergencyMeetingPlace(false);

            LatLng latLng = new LatLng(Double.valueOf(building.getLatitude()), Double.valueOf(building.getLongitude()));
            builder.include(latLng);
            if (building.getOrder() == 1) {
                lastMarker = gmap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(building.getOrder().toString())))
                );
                lastMarker.setTag(building);
            } else {
                gmap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getBlackMarkerBitmapFromView(building.getOrder().toString())))
                ).setTag(building);
            }

        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 230);
        gmap.moveCamera(cu);


    }


    private void addMarkersForSearch() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        gmap.clear();
        LatLng latLng = new LatLng(Double.valueOf(searchBuilding.getLatitude()), Double.valueOf(searchBuilding.getLongitude()));
        builder.include(latLng);
        gmap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(building.getOrder().toString())))
        ).setTag(searchBuilding);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 230);
        gmap.moveCamera(cu);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private Bitmap getMarkerBitmapFromView(String order) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_custom_marker, null);
        TextView tvOrder = customMarkerView.findViewById(R.id.tvOrder);
        tvOrder.setText(order);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getBlackMarkerBitmapFromView(String order) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_custom_marker_black, null);
        TextView tvOrder = customMarkerView.findViewById(R.id.tvOrder);
        tvOrder.setText(order);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerEmergencyBitmapFromView(String meetingPointName) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_custom_marker_meeting_place, null);
        TextView tvOrder = customMarkerView.findViewById(R.id.tvOrder);
        tvOrder.setText(meetingPointName);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (lastMarker != null && lastMarker.getTag() != null && ((Building) lastMarker.getTag()).isEmergencyMeetingPlace()==false ){
            lastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getBlackMarkerBitmapFromView(((Building) lastMarker.getTag()).getOrder().toString())));
        }else if (lastMarker != null && lastMarker.getTag() != null && ((Building) lastMarker.getTag()).isEmergencyMeetingPlace()==true){
            lastMarker.setAlpha(0.6f);
        }
        setBottomAdapter((Building) marker.getTag());
        if (((Building) marker.getTag()).isEmergencyMeetingPlace()==false){
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(((Building) marker.getTag()).getOrder().toString())));
        }else if(((Building) marker.getTag()).isEmergencyMeetingPlace()==true){
            marker.setAlpha(1f);
        }
        lastMarker = marker;
        return false;
    }
}
