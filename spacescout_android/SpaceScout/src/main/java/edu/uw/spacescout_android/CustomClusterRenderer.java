package edu.uw.spacescout_android;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import edu.uw.spacescout_android.model.Space;

/**
 * Created by aazri3 on 4/2/15.
 *
 * Custom cluster renderer. Used to override defaults that include:
 *      - minimum number of markers in a cluster
 *      - onCameraChange listener
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<Space> implements GoogleMap.OnCameraChangeListener {
    private static final String TAG = "CustomClusterRenderer";

    private Context mContext;
    private ClusterManager<Space> mClusterManager;
    private GoogleMap map;
    private TouchableWrapper mTouchView;

    //TODO: drawing custom markers & clustered markers
    /**
     * Draw markers using IconGenerator.
     */
//    private final IconGenerator mIconGenerator;
//    private final IconGenerator mClusterIconGenerator;
//    private final ImageView mImageView;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Space> clusterManager, TouchableWrapper mTouchView) {
        super(context, map, clusterManager);

        this.mContext = context;
        this.mClusterManager = clusterManager;
        this.map = map;
        this.mTouchView = mTouchView;

//        mIconGenerator = new IconGenerator(context);
//        mClusterIconGenerator = new IconGenerator(context);

//        View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
//        mClusterIconGenerator.setContentView(multiProfile);

//        mImageView = new ImageView(context);
//        mIconGenerator.setContentView(mImageView);
    }

//    @Override
//    protected void onBeforeClusterItemRendered(Space space, MarkerOptions markerOptions) {
//        // Draw a single person.
//        // Set the info window to show their name.
////        mImageView.setImageResource(person.profilePhoto);
////        Bitmap icon = mIconGenerator.makeIcon();
////        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
//    }

//    @Override
//    protected void onBeforeClusterRendered(Cluster<Space> cluster, MarkerOptions markerOptions) {
//        // Draw multiple people.
//        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
////            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
////            int width = mDimension;
////            int height = mDimension;
////
////            for (Person p : cluster.getItems()) {
////                // Draw 4 at most.
////                if (profilePhotos.size() == 4) break;
////                Drawable drawable = getResources().getDrawable(p.profilePhoto);
////                drawable.setBounds(0, 0, width, height);
////                profilePhotos.add(drawable);
////            }
////            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
////            multiDrawable.setBounds(0, 0, width, height);
////
////            mClusterImageView.setImageDrawable(multiDrawable);
////            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
////            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(TAG, "Camera change detected");
//        ((MainActivity) mContext).updateMap();

        // TODO: All this code can probably be shortened
        if (!mTouchView.mapIsTouched()) {
            Log.d(TAG, "Replacing markers..");
            // Get the latlng bounds to calculate radius distance
            VisibleRegion vr = map.getProjection().getVisibleRegion();
            double rightLat = vr.latLngBounds.northeast.latitude;
            double rightLon = vr.latLngBounds.northeast.longitude;
            // Get the top right corner of the screen
            Location topRightCorner = new Location("topRightCorner");
            topRightCorner.setLatitude(rightLat);
            topRightCorner.setLongitude(rightLon);
            // Get the center
            Location center = new Location("center");
            center.setLatitude(vr.latLngBounds.getCenter().latitude);
            center.setLongitude(vr.latLngBounds.getCenter().longitude);
            // Calculate radius
            int radius = Math.round(center.distanceTo(topRightCorner));
            int smallerRadius = (int) (radius - Math.round(0.045 * radius));

            String baseUrl = mContext.getResources().getString(R.string.baseUrl);
            String url = baseUrl + "spot/?center_latitude=" + center.getLatitude() +
                    "&center_longitude=" + center.getLongitude() + "&distance=" +
                    smallerRadius + "&limit=0";
            ((MainActivity) mContext).connectToServer(url, "spaces");

            // this is how you draw a line from point to point
//        line = new PolylineOptions().add(new LatLng(rightLat, rightLon),
//                new LatLng(center.getLatitude(), center.getLongitude()))
//                .width(5).color(Color.RED);
//        googleMap.addPolyline(line);
        }
    }
}
