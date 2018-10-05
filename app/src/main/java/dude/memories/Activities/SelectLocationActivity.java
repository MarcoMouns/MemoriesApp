package dude.memories.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.R;
import es.dmoral.toasty.Toasty;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.select_bar)
    Toolbar toolbar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        ButterKnife.bind(this);
        toolbar.setTitle("Select Location Memory");
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.select_location);

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                SharedPreferences.Editor sh = getSharedPreferences("sh", MODE_PRIVATE).edit();
                sh.remove("new_location_lat").apply();
                sh.remove("new_location_long").apply();
                sh.putString("new_location_lat", String.valueOf(latLng.latitude));
                sh.putString("new_location_long", String.valueOf(latLng.longitude));
                sh.apply();
                Toasty.success(SelectLocationActivity.this, "Success !!").show();
                finish();
            }
        });

    }
}
