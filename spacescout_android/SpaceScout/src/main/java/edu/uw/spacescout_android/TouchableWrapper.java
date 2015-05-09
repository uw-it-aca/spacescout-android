package edu.uw.spacescout_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by gupta37 on 5/23/14.
 *
 * Intercepts touch events on View. Uses Activity for context.
 */

public class TouchableWrapper extends FrameLayout {
    private static final String TAG = "TouchableWrapper";

//    private UpdateMapAfterUserInteraction updateMapAfterUserInteraction;
    private Context mContext;
    private static boolean mapIsTouched = false;

    public TouchableWrapper(Context context) {
        super(context);
        mContext = context;
        // Force the host activity to implement the UpdateMapAfterUserInteraction Interface
//        try {
//            updateMapAfterUserInteraction = (MainActivity) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInteraction");
//        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        int motion = event.getActionMasked();

        // For testing purposes
        if (motion != MotionEvent.ACTION_MOVE)
            Log.d(TAG, "Motion is " + motionToString(motion));

        if (motion == MotionEvent.ACTION_DOWN ||
                motion == MotionEvent.ACTION_HOVER_ENTER ||
                motion == MotionEvent.ACTION_POINTER_DOWN) {
            mapIsTouched = true;
        } else if (motion == MotionEvent.ACTION_UP ||
                motion == MotionEvent.ACTION_HOVER_EXIT ||
                motion == MotionEvent.ACTION_POINTER_UP) {
            mapIsTouched = false;
        }

        return super.dispatchTouchEvent(event);
    }

    public boolean mapIsTouched() {
        return mapIsTouched;
    }

    // Given an action int, returns a string description. For curiosity.
    public static String motionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN: return "Down";
            case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
            case MotionEvent.ACTION_HOVER_ENTER: return "Hover enter";
            case MotionEvent.ACTION_HOVER_EXIT: return "Hover exit";
            case MotionEvent.ACTION_UP: return "Up";
            case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE: return "Outside";
            case MotionEvent.ACTION_CANCEL: return "Cancel";
        }
        return "not listed";
    }

//    public interface UpdateMapAfterUserInteraction {
//        void onUpdateMapAfterUserInteraction();
//    }
}
