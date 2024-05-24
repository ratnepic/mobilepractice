package ru.mirea.porynovma.osmmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Arrays;

import ru.mirea.porynovma.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MapView mapView;
    private boolean isWork;
    private final int REQUEST_PERMISSION_CODE = 203;
    private MyLocationNewOverlay locationNewOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Context context = getApplicationContext();


        String[] permissions = {
                  android.Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (
                Arrays.stream(permissions)
                        .map(elem -> ContextCompat.checkSelfPermission(this, elem))
                        .allMatch(elem -> elem == PackageManager.PERMISSION_GRANTED)
        ) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);
        }


        Configuration.getInstance().load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
        );
        mapView = binding.mapview;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(12f);
        GeoPoint startPoint = new GeoPoint(55.714318, 37.5584365);
        mapController.setCenter(startPoint);

        locationNewOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(context),
                mapView
        );
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(
                context,
                new InternalCompassOrientationProvider(context),
                mapView
        );
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        GeoPoint poi_1 = new GeoPoint(55.794259, 37.701448); // Strominka
        GeoPoint poi_2 = new GeoPoint(55.731582, 37.574840); // Pirogovka
        GeoPoint poi_3 = new GeoPoint(55.661445, 37.477049); // Vernadka 86
        GeoPoint poi_4 = new GeoPoint(55.669986, 37.480409); // Vernadka 78

        setMarker(poi_1, "МИРЭА, корпус на Стромынка 20");
        setMarker(poi_2, "МИРЭА, корпус на Малая Пироговсакая 1");
        setMarker(poi_3, "МИРЭА, корпус на Проспект Вернадского 86");
        setMarker(poi_4, "МИРЭА, корпус на Проспект Вернадского 78");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.getInstance().load(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Configuration.getInstance().save(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void setMarker(GeoPoint point, String onClickMessage) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), onClickMessage, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        marker.setIcon(ResourcesCompat.getDrawable(
                getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on,
                null
        ));
        marker.setTitle("Title");
        mapView.getOverlays().add(marker);
    }
}