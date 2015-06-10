package edu.uw.spacescout_android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aazri3 on 4/1/15.
 *
 * Holds a list of Space(s).
 */

public class Spaces {
    private List<Space> spaces;

    public Spaces() {
        spaces = new ArrayList<>();
    }

    public void add(Space space) {
        spaces.add(space);
    }

    public Space get(int i) {
        return spaces.get(i);
    }

    public int size() {
        return spaces.size();
    }
}
