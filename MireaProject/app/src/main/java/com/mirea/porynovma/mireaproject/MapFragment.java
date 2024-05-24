package com.mirea.porynovma.mireaproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirea.porynovma.mireaproject.databinding.FragmentMapBinding;

import org.json.JSONObject;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private MapView mapView;
    private boolean isWork = false;
    private final int REQUEST_PERMISSION_CODE = 204;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GeoPoint[] establishmentPoints = {
            new GeoPoint(55.662987, 37.480609),
            new GeoPoint(55.664116, 37.482277),
            new GeoPoint(55.661892, 37.477624),
            new GeoPoint(55.665142, 37.478603),
    };

    private String[] establishmentAddresses = {
            "Пр-кт Вернадского, 86А",
            "Улица Покрышкина, 2к1",
            "Проспект Вернадского, 86",
            "Проспект Вернадского, 84с1",
    };

    private String[] establishmentDescriptions = {
            "Торгово-развлекательный центр Avenue Southwest",
            "Ресторан Фастфуда ROSTIC's, лучшая курица в России!",
            "РТУ МИРЭА, корпус МИТХТ - здесь творится химия.",
            "РАНХиГС корпус №1 - неплохо, но МИРЭА лучше.",
    };

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mapView = binding.mapview;


        String[] permissions = {
                  android.Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (
                Arrays.stream(permissions)
                        .map(elem -> ContextCompat.checkSelfPermission(view.getContext(), elem))
                        .allMatch(elem -> elem == PackageManager.PERMISSION_GRANTED)
        ) {
            isWork = true;
        } else {
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }

        GeoPoint startPoint = establishmentPoints[0];
        mapInit(startPoint);

        for (int i = 0; i < establishmentPoints.length; i++) {
            setMarker(
                    establishmentPoints[i],
                    establishmentAddresses[i],
                    establishmentDescriptions[i]
            );
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(
                getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext())
        );
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(
                getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext())
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

    private void mapInit(GeoPoint startPoint) {
        Context context = getContext();
        Configuration.getInstance().load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
        );
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(16f);
        mapController.setCenter(startPoint);

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
    }

    private void setMarker(GeoPoint point, String address, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                binding.addressTextView.setText("Адрес: " + address);
                binding.descriptionTextView.setText("Описание: " + description);
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