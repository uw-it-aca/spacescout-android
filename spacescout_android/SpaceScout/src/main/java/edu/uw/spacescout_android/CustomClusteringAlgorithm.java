package edu.uw.spacescout_android;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.StaticCluster;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;
import com.google.maps.android.quadtree.PointQuadTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by gupta37 on 5/16/14.
 *
 * For clustering markers on Google Maps our own way.
 */

public class CustomClusteringAlgorithm <T extends ClusterItem> implements Algorithm<T> {
    public static final int MAX_DISTANCE_AT_ZOOM = 40;

    private final Collection<QuadItem<T>> mItems = new ArrayList<>();
    private final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<>(0, 1, 0, 1);

    // something about the earth being spherical
    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1);

    // adds item (markers) to the map
    @Override
    public void addItem(T item) {
        final QuadItem<T> quadItem = new QuadItem<>(item);
        synchronized (mQuadTree) {
            mItems.add(quadItem);
            mQuadTree.add(quadItem);
        }
    }

    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override
    public void clearItems() {
        synchronized (mQuadTree) {
            mItems.clear();
            mQuadTree.clear();
        }
    }

    // Must be implemented but is never used.
    @Override
    public void removeItem(T item) {
        // TODO: delegate QuadItem#hashCode and QuadItem#equals to its item.
        throw new UnsupportedOperationException("NonHierarchicalDistanceBasedAlgorithm.remove not implemented");
    }

    // This is where the fun's at.

    @Override
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        final int discreteZoom = (int) zoom;

        //
        final double zoomSpecificSpan = MAX_DISTANCE_AT_ZOOM / Math.pow(2, discreteZoom) / 100 /*256*/;

        final Set<QuadItem<T>> visitedCandidates = new HashSet<>();
        final Set<Cluster<T>> results = new HashSet<>();
        final Map<QuadItem<T>, Double> distanceToCluster = new HashMap<>();
        final Map<QuadItem<T>, StaticCluster<T>> itemToCluster = new HashMap<>();

        synchronized (mQuadTree) {
            for (QuadItem<T> candidate : mItems) {
                if (visitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue;
                }

                Bounds searchBounds = createBoundsFromSpan(candidate.getPoint(), zoomSpecificSpan);
                Collection<QuadItem<T>> clusterItems;
                clusterItems = mQuadTree.search(searchBounds);
                if (clusterItems.size() == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    results.add(candidate);
                    visitedCandidates.add(candidate);
                    distanceToCluster.put(candidate, 0d);
                    continue;
                }
                StaticCluster<T> cluster = new StaticCluster<T>(candidate.mClusterItem.getPosition());
                results.add(cluster);

                for (QuadItem<T> clusterItem : clusterItems) {
                    Double existingDistance = distanceToCluster.get(clusterItem);
                    double distance = distanceSquared(clusterItem.getPoint(), candidate.getPoint());
                    if (existingDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {
                            continue;
                        }
                        // Move item to the closer cluster.
                        itemToCluster.get(clusterItem).remove(clusterItem.mClusterItem);
                    }
                    distanceToCluster.put(clusterItem, distance);
                    cluster.add(clusterItem.mClusterItem);
                    itemToCluster.put(clusterItem, cluster);
                }
                visitedCandidates.addAll(clusterItems);
            }
        }
        return results;
    }

    @Override
    public Collection<T> getItems() {
        final List<T> items = new ArrayList<>();
        synchronized (mQuadTree) {
            for (QuadItem<T> quadItem : mItems) {
                items.add(quadItem.mClusterItem);
            }
        }
        return items;
    }

    private double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    private Bounds createBoundsFromSpan(Point p, double span) {
        // TODO: Use a span that takes into account the visual size of the marker, not just its

        double halfSpan = span / 2;
        return new Bounds(
                p.x - halfSpan, p.x + halfSpan,
                p.y - halfSpan, p.y + halfSpan);
    }

    private static class QuadItem<T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private QuadItem(T item) {
            mClusterItem = item;
            mPosition = item.getPosition();
            mPoint = PROJECTION.toPoint(mPosition);
            singletonSet = Collections.singleton(mClusterItem);
        }

        @Override
        public Point getPoint() {
            return mPoint;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public Set<T> getItems() {
            return singletonSet;
        }

        @Override
        public int getSize() {
            return 1;
        }
    }
}