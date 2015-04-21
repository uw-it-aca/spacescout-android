package edu.uw.spacescout_android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aazri3 on 4/21/15.
 *
 * Holds a list of Building(s).
 */
public class Buildings {
    private List<Building> buildings;

    public Buildings() {
        buildings = new ArrayList<>();
    }

    public void add(Building building) {
        buildings.add(building);
    }

    public Building get(int i) {
        return buildings.get(i);
    }

    public int size() {
        return buildings.size();
    }
}
