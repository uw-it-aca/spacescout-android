package edu.uw.spacescout_android.test;

import android.app.AlertDialog;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import edu.uw.spacescout_android.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests HTTP Response handling in SpaceMapActivity.
 * Test cases: 0 (No response), 401 (Unauthorized), 300 (random other),
 *             200 w/ null json (no Spaces found).
 * Depends on MainActivity as the Activity that accesses SpaceMapFragment.
 */

// TODO: Test breaks when conenction is successful. Need to change.

@RunWith(AndroidJUnit4.class)
public class HandleHttpResponseTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;

    public HandleHttpResponseTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        setActivityInitialTouchMode(false);
        mActivity = getActivity();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mActivity);
        getActivity().finish();
    }

    @Test
    public void testStatusZero() throws Throwable {
        dismissDefaultDialog();
        // calls method with dummy status & null JSON
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                checkDialog(0, "Connection Issue");
                getActivity().finish();
            }
        });
    }

    @Test
    public void testStatus401() throws Throwable {
        dismissDefaultDialog();
//        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                checkDialog(401, "Authentication Issue");
                getActivity().finish();
            }
        });
    }

    @Test
    public void testStatus200Null() throws Throwable {
        dismissDefaultDialog();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivity.handleHttpResponse(200, null);
                // check that toast with given text key exists
                assertTrue(mActivity.toasts.containsKey("Sorry, no spaces found"));
                getActivity().finish();
                getActivity().finish();
            }
        });
    }

    @Test
    public void testStatus300() throws Throwable {
        dismissDefaultDialog();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                checkDialog(300, "Connection Issue");
                getActivity().finish();
            }
        });
    }

    // Dismisses the default dialogue when the activity fails to connect to the real server
    private void dismissDefaultDialog() {
        AlertDialog dialog = mActivity.getUsedDialogue("Connection Issue");
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                mActivity.alertDialogues.clear();
            }
        }
        // resets the alertDialogues weakHashMap
    }

    // Asserts whether the correct dialogue is shown given the status codes
    private void checkDialog(int status, String title) {
        mActivity.handleHttpResponse(status, null);
        // gets the alert dialogue with given title (will error out if doesn't exist)
        AlertDialog dialog = mActivity.getUsedDialogue(title);
        assertTrue (dialog.isShowing());
        dialog.dismiss();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}