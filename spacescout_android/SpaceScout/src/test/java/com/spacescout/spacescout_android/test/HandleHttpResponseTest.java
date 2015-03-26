package com.spacescout.spacescout_android.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.spacescout.spacescout_android.MainActivity;
import com.spacescout.spacescout_android.SpaceMapActivity;

/*
 * Tests HTTP Response handling in SpaceMapActivity.
 * Test cases: 0 (No response), 401 (Unauthorized), 300 (random other),
 *             200 w/ null json (no Spaces found)
 * Depends on MainActivity as the Activity that accesses SpaceMapActivityFragment.
 */

public class HandleHttpResponseTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private SpaceMapActivity mFragment;

    public HandleHttpResponseTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);
        mActivity = getActivity();
        mFragment = mActivity.fragSpaceMap;
    }

    public void testPreConditions() {
        assertNotNull(mActivity);
        assertNotNull(mFragment);
    }

    public void testStatusZero() throws Throwable {
        mFragment.handleHttpResponse(0, null);
        AlertDialog dialog = mFragment.getUsedDialogue("Connection Issue");
        if (dialog.isShowing()) {
            try {
                performClick(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void testStatus401() throws Throwable {
        mFragment.alertDialogues.clear();
        mFragment.handleHttpResponse(401, null);

        AlertDialog dialog = mFragment.getUsedDialogue("Authentication Issue");
        if (dialog.isShowing()) {
            try {
                performClick(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void testStatus200Null() throws Throwable {
        mFragment.alertDialogues.clear();
        mFragment.handleHttpResponse(200, null);
        assertTrue(mFragment.toasts.containsKey("Sorry, no spaces found"));
    }

    public void testStatus300() throws Throwable {
        mFragment.alertDialogues.clear();
        mFragment.handleHttpResponse(300, null);

        AlertDialog dialog = mFragment.getUsedDialogue("Connection Issue");
        if (dialog.isShowing()) {
            try {
                performClick(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void performClick(final Button button) throws Throwable {
        button.performClick();
        getInstrumentation().waitForIdleSync();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}