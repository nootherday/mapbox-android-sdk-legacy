package com.mapbox.mapboxsdk.views;

import com.almeros.android.multitouch.RotateGestureDetector;
import com.mapbox.mapboxsdk.views.util.OnMapOrientationChangeListener;

/**
 * A custom rotate gesture detector that processes gesture events and dispatches them
 * to the map's overlay system.
 */
public class MapViewRotateGestureDetectorListener implements RotateGestureDetector.OnRotateGestureListener {

    private static String TAG = "MapViewRotateListener";

    private final MapView mapView;
    /**
     * This holds a reference to the first rotation angle
     */
    private float firstAngle;
    private float currentDelta;

    private boolean real = false;
    private final MapViewScaleGestureDetectorListener mScaleGestureDetectorListener;

    /**
     * Bind a new gesture detector to a map
     *
     * @param mv a map view
     */
    public MapViewRotateGestureDetectorListener(final MapView mv, final MapViewScaleGestureDetectorListener sgdl) {
        this.mapView = mv;
        this.mScaleGestureDetectorListener = sgdl;
    }

    @Override
    public boolean onRotate(RotateGestureDetector detector) {
        float delta = detector.getRotationDegreesDelta();
        currentDelta += delta;

        if (!real && Math.abs(this.currentDelta) >= 7.5) {
            this.currentDelta = 0;
            if (mScaleGestureDetectorListener.isInProgress()) return true;
            real = true;
            return true;
        }

        if (real) {
            float newAngle = firstAngle - currentDelta;
            mapView.setMapOrientation(newAngle);

            // If a listener has been set, callback
            OnMapOrientationChangeListener l = mapView.getOnMapOrientationChangeListener();
            if (l != null) {
                l.onMapOrientationChange(newAngle);
            }
        }

        return true;
    }

    @Override
    public boolean onRotateBegin(RotateGestureDetector detector) {
        firstAngle = mapView.getMapOrientation();
        currentDelta = 0;
        real = false;
        return true;
    }

    @Override
    public void onRotateEnd(RotateGestureDetector detector) {

    }
}
