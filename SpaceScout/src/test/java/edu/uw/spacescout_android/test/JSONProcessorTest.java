package edu.uw.spacescout_android.test;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uw.spacescout_android.model.Building;
import edu.uw.spacescout_android.model.Buildings;
import edu.uw.spacescout_android.util.JSONProcessor;

import static org.junit.Assert.assertTrue;

/**
 * Created by aazri3 on 5/29/15.
 *
 * Test case:
 * Use: Demo JSON
 */

@RunWith(AndroidJUnit4.class)
public class JSONProcessorTest extends TestCase{

//    @BeforeClass
//    public static void oneTimeSetup() throws Exception {
//
//    }

//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void testModelSpaces() throws Exception {
        Boolean test = true;
        assertTrue(test);
    }

    @Test
    public void testModelBuildings() throws Exception {

        List<String> list = new ArrayList<>();
        list.add("Odegaard");
        list.add("Suzallo");
        list.add("Paccar");
        JSONArray jsonBuildings = new JSONArray(list);

        Buildings buildings = JSONProcessor.modelBuildings(jsonBuildings);
        assertTrue(buildings.size() == 3);

        Building firstBuilding = buildings.get(0);
        assertTrue(firstBuilding.getName().equals("Odegaard"));
    }
}