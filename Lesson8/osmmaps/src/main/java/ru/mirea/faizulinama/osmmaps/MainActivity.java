package ru.mirea.faizulinama.osmmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);

        IMapController controller = mapView.getController();
        controller.setZoom(15.0);
        GeoPoint start = new GeoPoint(55.794229, 37.700772);
        controller.setCenter(start);

        CompassOverlay compass = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
        compass.enableCompass();
        mapView.getOverlays().add(compass);

        ScaleBarOverlay scaleBar = new ScaleBarOverlay(mapView);
        scaleBar.setCentred(true);
        mapView.getOverlays().add(scaleBar);

        Marker marker = new Marker(mapView);
        marker.setPosition(start);
        marker.setTitle("РТУ – МИРЭА");
        marker.setSubDescription("Кампус на Стромынка, 20");
        marker.setIcon(getResources().getDrawable(org.osmdroid.library.R.drawable.osm_ic_center_map));
        marker.setOnMarkerClickListener((m, mv) -> {
            Toast.makeText(MainActivity.this, m.getTitle() + "\n" + m.getSubDescription(), Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(marker);

        Marker markerRedSquare = new Marker(mapView);
        markerRedSquare.setPosition(new GeoPoint(55.7539, 37.6208));
        markerRedSquare.setTitle("Красная площадь");
        markerRedSquare.setSubDescription("Сердце Москвы");
        markerRedSquare.setIcon(getResources().getDrawable(org.osmdroid.library.R.drawable.osm_ic_center_map));
        markerRedSquare.setOnMarkerClickListener((m, mv) -> {
            Toast.makeText(MainActivity.this, m.getTitle() + "\n" + m.getSubDescription(), Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(markerRedSquare);

        Marker markerGorky = new Marker(mapView);
        markerGorky.setPosition(new GeoPoint(55.7295, 37.6016));
        markerGorky.setTitle("Парк Горького");
        markerGorky.setSubDescription("Один из самых больших парков Москвы");
        markerGorky.setIcon(getResources().getDrawable(org.osmdroid.library.R.drawable.osm_ic_center_map));
        markerGorky.setOnMarkerClickListener((m, mv) -> {
            Toast.makeText(MainActivity.this, m.getTitle() + "\n" + m.getSubDescription(), Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(markerGorky);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                (ActivityResultCallback<java.util.Map<String, Boolean>>) result -> {
                    Boolean fine = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarse = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (Boolean.TRUE.equals(fine) || Boolean.TRUE.equals(coarse)) {
                        enableMyLocation();
                    } else {
                        Toast.makeText(this, "Нет прав на геолокацию — слой местоположения отключён", Toast.LENGTH_SHORT).show();
                    }
                });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            requestPermissionLauncher.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void enableMyLocation() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) mapView.onPause();
    }
}