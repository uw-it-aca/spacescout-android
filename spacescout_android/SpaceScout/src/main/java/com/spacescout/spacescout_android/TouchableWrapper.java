package com.spacescout.spacescout_android;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by gupta37 on 5/23/14.
 *
 * Intercepts touch events on Google map.
 */

public class TouchableWrapper extends FrameLayout {
    private UpdateMapAfterUserInteraction updateMapAfterUserInteraction;

    public TouchableWrapper(Context context) {
        super(context);
        // Force the host activity to implement the UpdateMapAfterUserInteraction Interface
        try {
            updateMapAfterUserInteraction = new SpaceMapFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInteraction");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                SpaceMapFragment.mMapIsTouched = true;
                break;
            case MotionEvent.ACTION_UP:
                SpaceMapFragment.mMapIsTouched = false;
                updateMapAfterUserInteraction.onUpdateMapAfterUserInteraction();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public interface UpdateMapAfterUserInteraction {
        public void onUpdateMapAfterUserInteraction();
    }
}
