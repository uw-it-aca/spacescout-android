package edu.uw.spacescout_android;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
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
public class CustomClusterRenderer extends DefaultClusterRenderer<Space> implements
        GoogleMap.OnCameraChangeListener {

    private static final String TAG = "CustomClusterRenderer";

    private Context mContext;
    private GoogleMap map;

    //TODO: drawing custom markers & clustered markers
    /**
     * Draw markers using IconGenerator.
     */
//    private final IconGenerator mIconGenerator;
//    private final IconGenerator mClusterIconGenerator;
//    private final ImageView mImageView;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Space> clusterManager) {
        super(context, map, clusterManager);

        this.mContext = context;
        this.map = map;

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

    // TODO: Maybe should consider not sending another request after zooming out a certain distance
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        dismissDefaultDialog();

        // TODO: May want to look at ThreadPoolExecutor & SynchronousQueue for a different impl
        // Cancel any running AsyncTask before we start a new request
        // MainActivity.currItem tracks the current item we're requesting for - "buidlings" or "spaces"
        if (((MainActivity) mContext).currItem != null) {
            if (((MainActivity) mContext).currItem.equals("spaces")) {
                if (((MainActivity) mContext).getJson.getStatus() == AsyncTask.Status.PENDING ||
                        ((MainActivity) mContext).getJson.getStatus() == AsyncTask.Status.RUNNING) {
                    ((MainActivity) mContext).getJson.cancel(true);
                }
            }
        }

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
//        }
    }

    private void dismissDefaultDialog() {
        AlertDialog dialog = ((MainActivity) mContext).getUsedDialogue("Connection Issue");
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                ((MainActivity) mContext).alertDialogues.clear();
            }
        }
        // resets the alertDialogues weakHashMap
    }
}
