package edu.uw.spacescout_android;

import android.content.Context;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import edu.uw.spacescout_android.model.Space;

/**
 * Created by aazri3 on 4/2/15.
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<Space> {
    //TODO: drawing custom markers & clustered markers
    /**
     * Draws markers using IconGenerator.
     */
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager mClusterManager) {
        super(context, map, mClusterManager);
        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);
//        View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
//        mClusterIconGenerator.setContentView(multiProfile);

        mImageView = new ImageView(context);
//        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(Space space, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
//        mImageView.setImageResource(person.profilePhoto);
//        Bitmap icon = mIconGenerator.makeIcon();
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Space> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
//            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
//            int width = mDimension;
//            int height = mDimension;
//
//            for (Person p : cluster.getItems()) {
//                // Draw 4 at most.
//                if (profilePhotos.size() == 4) break;
//                Drawable drawable = getResources().getDrawable(p.profilePhoto);
//                drawable.setBounds(0, 0, width, height);
//                profilePhotos.add(drawable);
//            }
//            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
//            multiDrawable.setBounds(0, 0, width, height);
//
//            mClusterImageView.setImageDrawable(multiDrawable);
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
