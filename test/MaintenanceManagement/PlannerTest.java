/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.sql.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author saby_
 */
public class PlannerTest {

    Planner planner;
    Connection conn;
    String url = "jdbc:postgresql://localhost:5432/locale2";
    String pwd = "kekkeroni";
    String user = "kek";
    Procedure procedure = new Procedure("pr1");

    @Before
    public void setUp() throws SQLException {
// apertura connessione usando driver di postgresql
        conn = DriverManager.getConnection(url, user, pwd);
        planner = new Planner("PLANNER", "TEST", "planner", conn);
    }

    @After
    public void tearDown() throws SQLException {
        Statement op = conn.createStatement();
        planner.deleteActivity(1000);
        planner.deleteActivity(1001);
        op.executeUpdate("delete from maintainer where username = 'usTest'");
        conn.close();
    }

    /**
     * Tests of addActivity method, of class Planner.
     */
    @Test
    public void testAddActivity() throws SQLException {
        Statement op = conn.createStatement();
        boolean bool = planner.addActivity(new Activity(1000, "aa", "bb", "electrical", "dd", 30, true, "", 1, "qq", procedure,"planned"));
        assertTrue(bool);
        ResultSet rst = op.executeQuery("select activityId from activity where activityId= 1000");
        rst.next();
        assertEquals(1000, rst.getInt("activityId"));
    }

    @Test
    public void testAddActivityWithoutMaterialsAndNotes() throws SQLException {
        Statement op = conn.createStatement();
        boolean bool = planner.addActivity(new Activity(1000, "site", "area", "hydraulic", "description", 70, true, null, 2, "", procedure,"planned"));
        assertTrue(bool);
        ResultSet rst = op.executeQuery("select * from activity where activityId= 1000");
        rst.next();
        assertEquals(rst.getInt("activityId"), 1000);
        assertEquals("", rst.getString("workspaceNotes"));
        assertEquals("", rst.getString("materials"));

    }

    @Test(expected = SQLException.class)
    public void testAddActivitySameId() throws SQLException {
        planner.addActivity(new Activity(1000, "aa", "bb", "electrical", "dd", 315, true, null, 52, "qq", procedure,"planned"));
        planner.addActivity(new Activity(1000, "aa", "bb", "electrical", "dd", 60, true, null, 3, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityZeroId() throws SQLException {
        planner.addActivity(new Activity(0, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityNegativeId() throws SQLException {
        planner.addActivity(new Activity(-1, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityWrongTypology() throws SQLException {
        planner.addActivity(new Activity(99, "aa", "bb", "ELECTRICAL", "dd", 60, true, "xd", 8, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityZeroWeek() throws SQLException {
        planner.addActivity(new Activity(98, "aa", "bb", "electrical", "dd", 60, true, "xd", 0, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityOutOfIntervalWeek() throws SQLException {
        planner.addActivity(new Activity(97, "aa", "bb", "electrical", "dd", 60, true, "xd", 100, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityNegativeWeek() throws SQLException {
        planner.addActivity(new Activity(96, "aa", "bb", "electrical", "dd", 60, true, "xd", -1, "qq", procedure,"planned"));
    }

    @Test(expected = SQLException.class)
    public void testAddActivityNegativeEstimatedTime() throws SQLException {
        planner.addActivity(new Activity(96, "aa", "bb", "electrical", "dd", -3, true, "xd", 50, "qq", procedure,"planned"));
    }

    /**
     * Tests of deleteActivity method, of class Planner.
     */
    @Test(expected = SQLException.class)
    public void testDeleteActivityExists() throws SQLException {
        Statement op = conn.createStatement();
        planner.addActivity(new Activity(1000, "aa", "bb", "electrical", "dd", 30, true, "materials", 1, "wsnotes", procedure,"planned"));
        boolean bool = planner.deleteActivity(1000);
        assertTrue(bool);
        ResultSet rst = op.executeQuery("select * from activity where activityId=1000");
        rst.next();
        int x = rst.getInt("activityId");

    }

    /**
     * Tests of modifyActivity method, of class Planner.
     */
    @Test
    public void testModifyActivity() throws SQLException {
        Statement op = conn.createStatement();
        planner.addActivity(new Activity(1000, "aa", "bb", "electrical", "dd", 30, true, "materials", 1, "wsnotes", procedure,"planned"));
        boolean bool = planner.modifyActivity(1000, "ciao");
        assertTrue(bool);
        ResultSet rst = op.executeQuery("select * from activity where activityId=1000");
        rst.next();
        assertEquals("ciao", rst.getString("workspaceNotes"));

    }

    @Test
    public void testGetActivities() throws SQLException {
        ResultSet rst = null;
        rst = planner.getActivities("1");
        assertNotNull(rst);
    }

    @Test(expected = SQLException.class)
    public void testGetActivitiesWrongTypeWeek() throws SQLException {
        ResultSet rst = null;
        rst = planner.getActivities("a");
    }

    @Test
    public void testGetActivity() throws SQLException {
        Statement op = conn.createStatement();
        planner.addActivity(new Activity(1000, "factory", "area", "electrical", "desc", 315, true, "materials", 1, null, procedure,"planned"));
        Activity a = planner.getActivity(1000);
        assertEquals(1000, a.getActivityId());
        assertEquals("factory", a.getFactorySite());
        assertEquals("area", a.getArea());
        assertEquals("electrical", a.getTypology());
        assertEquals("desc", a.getDescription());
        assertEquals(315, a.getEstimatedTime());
        assertTrue(a.isInterruptible());
        assertEquals("materials", a.getMaterials());
        assertEquals(1, a.getWeek());
        assertEquals("", a.getWorkspaceNotes());
        assertEquals("pr1", a.getProcedure().getNome());

    }

    @Test
    public void testGetArrayNoActivities() throws SQLException {
        Statement op = conn.createStatement();
        op.executeUpdate("insert into maintainer values('usTest','pwTest')");
        int[] testarray = {60, 60, 60, 60, 60, 60, 60};
        Activity a = planner.createActivity(1000, "factory", "area", "electrical", "desc", 100, true, "materials", 1, "wsnotes", procedure,"planned");
        planner.addActivity(a);
        int[] array = planner.getArray("usTest", a, 1);
        assertArrayEquals(testarray, array);

    }

    @Test
    public void testGetArrayWithActivity() throws SQLException, InsertException {
        Statement op = conn.createStatement();
        op.executeUpdate("insert into maintainer values('usTest','pwTest')");
        int[] testarray = {0, 60, 60, 60, 60, 60, 60};
        Activity a = planner.createActivity(1000, "factory", "area", "electrical", "desc", 60, true, "materials", 1, "wsnotes", procedure,"planned");
        planner.addActivity(a);
        int[] array = planner.getArray("usTest", a, 1);
        planner.manageAvailability(array, "usTest", 1, 1, a);
        array = planner.getArray("usTest", a, 1);
        assertArrayEquals(testarray, array);
    }

    @Test
    public void testManageAvailabilityOneActivity() throws SQLException, InsertException {
        Statement op = conn.createStatement();
        op.executeUpdate("insert into maintainer values('usTest','pwTest')");
        Activity a = planner.createActivity(1000, "factory", "area", "electrical", "desc", 300, true, "materials", 1, "wsnotes", procedure,"planned");
        planner.addActivity(a);
        int[] array = planner.getArray("usTest", a, 2);
        planner.manageAvailability(array, "usTest", 2, 4, a);
        assertEquals(240,a.getEstimatedTime());
    }

    @Test(expected = InsertException.class)
    public void testManageAvailabilitySlotOccupied() throws SQLException, InsertException {
        Statement op = conn.createStatement();
        op.executeUpdate("insert into maintainer values('usTest','pwTest')");
        Activity a = planner.createActivity(1000, "factory", "area", "electrical", "desc", 60, true, "materials", 1, "wsnotes", procedure,"planned");
        planner.addActivity(a);
        Activity b = planner.createActivity(1001, "factory", "area", "electrical", "desc", 5, true, "materials", 1, "wsnotes", procedure,"planned");
        planner.addActivity(b);
        int[] array = planner.getArray("usTest", a, 5);
        planner.manageAvailability(array, "usTest", 5, 1, a);
        array = planner.getArray("usTest", b, 5);
        planner.manageAvailability(array, "usTest", 5, 1, b);

    }

}
