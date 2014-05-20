package com.spacescout.spacescout_android;

import android.support.v4.util.LongSparseArray;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.StaticCluster;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gupta37 on 5/16/14.
 */
public class ClusterByBuilding <MyItem extends ClusterItem> implements Algorithm<MyItem> {
    private static final int GRID_SIZE = 100;

    private final Set<MyItem> mItems = Collections.synchronizedSet(new HashSet<MyItem>());
    ArrayList<String> buildings = new ArrayList<String>();

    @Override
    public void addItem(MyItem item) {
        mItems.add(item);
    }


    @Override
    public void addItems(Collection<MyItem> items) {
        mItems.addAll(items);
    }

    @Override
    public void clearItems() {
        mItems.clear();
    }

    @Override
    public void removeItem(MyItem item) {
        mItems.remove(item);
    }

    @Override
    public Set<? extends Cluster<MyItem>> getClusters(double zoom) {
        zoom = 17;
        long numCells = (long) Math.ceil(256 * Math.pow(2, zoom) / GRID_SIZE);
        SphericalMercatorProjection proj = new SphericalMercatorProjection(numCells);

        HashSet<Cluster<MyItem>> clusters = new HashSet<Cluster<MyItem>>();
        LongSparseArray<StaticCluster<MyItem>> sparseArray = new LongSparseArray<StaticCluster<MyItem>>();

        synchronized (mItems) {
            for (MyItem item : mItems) {
                Point p = proj.toPoint(item.getPosition());
                //String building_name = item.getBuilding();

                long coord = getCoord(numCells, p.x, p.y);

                StaticCluster<MyItem> cluster = sparseArray.get(coord);
                if (cluster == null) {
//                    cluster = new StaticCluster<MyItem>(proj.toLatLng(new Point(Math.floor(p.x) + .5, Math.floor(p.y) + .5)));
//                    sparseArray.put(coord, cluster);
//                    clusters.add(cluster);
                    cluster = new StaticCluster<MyItem>(proj.toLatLng(p));
                    clusters.add(cluster);
                }

                cluster.add(item);
            }
        }

        return clusters;
    }

    @Override
    public Collection<MyItem> getItems() {
        return mItems;
    }

    private static long getCoord(long numCells, double x, double y) {
        return (long) (numCells * Math.floor(x) + Math.floor(y));
    }
}
