package com.hyper.ubertransport.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.driver.orders.fragment.DriverOrdersFragment;
import com.hyper.ubertransport.driver.registration.DriverRegistrationActivity;
import com.hyper.ubertransport.login.LoginActivity;
import com.hyper.ubertransport.my_account.fragment.MyAccountFragment;
import com.hyper.ubertransport.my_shipment.fragment.MyShipmentFragment;
import com.hyper.ubertransport.order.fragments.PackageSizeFragment;
import com.hyper.ubertransport.order.usable.OrderInfo;
import com.hyper.ubertransport.registration.usable.UserInfo;
import com.hyper.ubertransport.utils.PermissionUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MapsActivity extends FragmentActivity implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PackageSizeFragment.SummaryFragmentCallback,
        MyAccountFragment.MyAccountFragmentCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private CardView pickUpCardView;
    private CardView destinationCardView;
    private AutocompleteSupportFragment autocompleteFragment;
    private AutocompleteSupportFragment autocompleteFragmentDestination;
    private ImageView menuImageView;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private final String TAG = "MAPS";
    private final HashMap<String, String> locationHashMap = new HashMap<>();
    private final OrderInfo orderInfo = new OrderInfo();
    private UserInfo userInfo;


    private final View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    };

    private final View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeFragment(R.id.summary_container);
            changeNavigationButtonBehaviourOnCloseFragment();
        }
    };
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),
                    getResources().getString(R.string.google_places_key));
        }

        //google map support
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

//        this.autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);
        this.autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

//        this.autocompleteFragmentDestination = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_destination);
        this.autocompleteFragmentDestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_destination);
        autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        this.destinationCardView = findViewById(R.id.container_place_autocomplete_fragment_destination);
        this.pickUpCardView = findViewById(R.id.container_place_autocomplete_fragment);
        this.navigationView = findViewById(R.id.navigation_view);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.menuImageView = findViewById(R.id.menu);

        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if (locationButton != null) {
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 48, 48);
        }


        loadUserInfo();
        initUpNavigationDrawer();
        initAutoCompleteFragments();
        initOnSelectedListener();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void getDeviceLocation() {
        Task locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                // Set the map's camera position to the current location of the device.
                Location mLastKnownLocation = (Location) task.getResult();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Objects.requireNonNull(mLastKnownLocation).getLatitude(),
                                mLastKnownLocation.getLongitude()), 10));
            }
        });
    }

    //using search tool in front end
    protected String search(List<Address> addresses, GoogleMap map) {

        map.clear();

        String addressText;
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());


        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        map.clear();
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));


        return address.getFeatureName();
    }

    //start cord for map
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
//        LatLng warsaw = new LatLng(52.227, 21.021);
//        map.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
//        map.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        updateLocationUI();
        getDeviceLocation();
    }

    //sharing location
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            showMissingPermissionError();
            mPermissionDenied = true;
        }
        updateLocationUI();
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void initOnSelectedListener() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationHashMap.put("from", place.getId());
                orderInfo.setFromName(String.valueOf(place.getName()));
                destinationCardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        autocompleteFragmentDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationHashMap.put("destination", place.getId());
                orderInfo.setDestinationName(String.valueOf(place.getName()));
                openSummaryFragment();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        menuImageView.setOnClickListener(navigationListener);
    }

    private void initAutoCompleteFragments() {

        autocompleteFragment.setHint(getString(R.string.hint_place_autocompletefragment_from));
        autocompleteFragmentDestination.setHint(getString(R.string.hint_place_autocompletefragment_destination));

        ImageView searchIcon = (ImageView) ((LinearLayout) Objects.requireNonNull(autocompleteFragment.getView())).getChildAt(0);
        if (searchIcon != null) {
            searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_location_pin));
        }

        ImageView searchIconDestination = (ImageView) ((LinearLayout) Objects.requireNonNull(autocompleteFragmentDestination.getView())).getChildAt(0);
        if (searchIcon != null) {
            searchIconDestination.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_location_pin_destination));
        }
    }


    /**
     * setting a listener for navigation item click
     */
    private void initUpNavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        onItemSelectedNavigation(menuItem.getItemId());
                        return true;
                    }
                });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.header_textview);
        if (user != null)
            textView.setText(user.getEmail());
    }

    private void onItemSelectedNavigation(int id) {
        Fragment fragment;

        switch (id) {
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(homeIntent);
                finish();
                break;
            case R.id.my_account:
                fragment = new MyAccountFragment();
                openFragment(fragment);
                break;
            case R.id.nav_driver:
                if (userInfo.getDriverAccount()) {
                    fragment = new DriverOrdersFragment();
                    openFragment(fragment);
                } else {
                    Intent driverRegistrationIntent = new Intent(getApplicationContext(), DriverRegistrationActivity.class);
                    startActivity(driverRegistrationIntent);
                    finish();
                }

                break;
            case R.id.nav_my_shipments:
                fragment = new MyShipmentFragment();
                openFragment(fragment);
                break;
        }
    }

    private void openSummaryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        orderInfo.setFrom(locationHashMap.get("from"));
        orderInfo.setDestination(locationHashMap.get("destination"));
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_info", orderInfo);
        PackageSizeFragment fragment = new PackageSizeFragment();
        fragment.setArguments(bundle);
        fragmentTransaction
                .add(R.id.summary_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

        changeNavigationButtonBehaviourOnOpenFragment();
        hidePlaceAutoCompletePickUpFragment();
        hidePlaceAutoCompleteDestinationFragment();
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(null)
                .commit();
        removeFragment(R.id.summary_container);
        changeNavigationButtonBehaviourOnCloseFragment();
        hidePlaceAutoCompleteDestinationFragment();
        hidePlaceAutoCompletePickUpFragment();
    }

    @Override
    public void changeNavigationButtonBehaviourOnCloseFragment() {
        menuImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_black_40dp));
        menuImageView.setOnClickListener(navigationListener);
        showPlaceAutoCompletePickUpFragment();
        hidePlaceAutoCompleteDestinationFragment();
    }

    @Override
    public void changeViewOnOrderSummaryOpen() {
        menuImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_black_40dp));
        menuImageView.setOnClickListener(navigationListener);
        hidePlaceAutoCompleteDestinationFragment();
        hidePlaceAutoCompletePickUpFragment();
    }

    @Override
    public void hidePlaceAutoCompleteDestinationFragment() {
        autocompleteFragmentDestination.setText("");
        destinationCardView.setVisibility(View.GONE);
    }

    @Override
    public void hidePlaceAutoCompletePickUpFragment() {
        autocompleteFragment.setText("");
        pickUpCardView.setVisibility(View.GONE);
    }

    @Override
    public void showPlaceAutoCompleteDestinationFragment() {
        pickUpCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaceAutoCompletePickUpFragment() {
        pickUpCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void changeNavigationButtonBehaviourOnOpenFragment() {
        menuImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_40dp));
        menuImageView.setOnClickListener(backListener);
    }

    private void removeFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment tempFragment = fragmentManager.findFragmentById(id);

        if (tempFragment != null) {
            fragmentTransaction.remove(tempFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStackImmediate();
            showPlaceAutoCompletePickUpFragment();
        } else {
            super.onBackPressed();
        }
    }

    private void loadUserInfo() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) return;

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userInfo = document.toObject(UserInfo.class);

                        Log.d(TAG, userInfo.getName());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
