package ru.mirea.zaitseviv.yandexdriver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener {
    private MapView mapView;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private final Point START = new Point(55.670005, 37.479894);
    private final Point END = new Point(55.794229, 37.700772);
    private final int[] COLORS = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFA500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("42eed011-2596-475c-a182-bf8d833d1494");
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapview);
        mapView.getMap().move(new CameraPosition(START, 10f, 0f, 0f),
                new Animation(Animation.Type.SMOOTH, 0), null);
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        requestRoute();
    }

    private void requestRoute() {
        DrivingOptions options = new DrivingOptions();
        VehicleOptions vehicle = new VehicleOptions();
        options.setRoutesCount(4);
        ArrayList<RequestPoint> points = new ArrayList<>();
        points.add(new RequestPoint(START, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(END, RequestPointType.WAYPOINT, null));
        drivingSession = drivingRouter.requestRoutes(points, options, vehicle, this);
    }

    @Override
    public void onDrivingRoutes(List<DrivingRoute> routes) {
        for (int i = 0; i < routes.size(); i++) {
            mapView.getMap().getMapObjects()
                    .addPolyline(routes.get(i).getGeometry())
                    .setStrokeColor(COLORS[i % COLORS.length]);
        }
    }

    @Override
    public void onDrivingRoutesError(com.yandex.runtime.Error error) { }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}

