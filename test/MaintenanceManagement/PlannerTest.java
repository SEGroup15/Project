/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import Activities.EWOActivity;
import Activities.Activity;
import Activities.ExtraActivity;
import Users.Planner;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author saby_
 */
public class PlannerTest {

    Planner planner;
    Connection conn;
    Statement op;
    String url = "jdbc:postgresql://localhost:5432/locale2";
    String pwd = "kek2";
    String user = "kek2";
    LinkedList<String> materials = new LinkedList<>();
    LinkedList<String> competencies = new LinkedList<>();

    @Before
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection(url, user, pwd);
        op = conn.createStatement();
        planner = new Planner("kek", "kekkeroni");
        Statement op = conn.createStatement();
        op.executeUpdate("insert into site values ('factTest','areaTest')");
        op.executeUpdate("insert into material values ('materialTest')");
        op.executeUpdate("insert into typology values ('typologyTest')");
        op.executeUpdate("insert into competence values ('competenceTest')");
        materials.add("materialTest");
        competencies.add("competenceTest");
    }

    @After
    public void tearDown() throws SQLException {
        op.executeUpdate("delete from users where username = 'usTest'");
        op.executeUpdate("delete from site where area='areaTest'");
        op.executeUpdate("delete from material where name_material='materialTest'");
        op.executeUpdate("delete from typology where name_typology='typologyTest'");
        op.executeUpdate("delete from competence where name_competence= 'competenceTest'");
        materials.remove();
        competencies.remove();

        conn.close();
    }

    private int takeIndex() throws SQLException {
        ResultSet r = op.executeQuery("select max(id) as max from activity");
        r.next();
        return r.getInt("max");

    }

    /**
     * Tests of CreateActivity method, of class Planner.
     */
    @Test
    public void testCreateActivityIncreasedId() throws SQLException {
    planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description0", 0, true, null, 10, null);
    int i = takeIndex();
    planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description0", 0, true, null, 10, null);
    assertEquals(i+1,takeIndex());
    op.executeUpdate("delete from activity where id = (select max(id) from activity)");
    op.executeUpdate("delete from activity where id = (select max(id) from activity)");
    }
    
    @Test
    public void testCreateActivityPlanned() throws SQLException {
        boolean bool = planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description0", 0, true, null, 10, null);
        assertTrue(bool);
        op.executeUpdate("delete from activity where id = (select max(id) from activity)");
    }

    @Test
    public void testCreateActivityEWO() throws SQLException {
        boolean bool = planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", null, 0, true, materials, 10, "wsNotes");
        assertTrue(bool);
        op.executeUpdate("delete from activity where id = (select max(id) from activity)");
    }

    @Test
    public void testCreateActivityExtra() throws SQLException {
        boolean bool = planner.createActivity("Extra", "factTest", "areaTest", "typologyTest", null, 0, true, materials, 10, "wsNotes");
        assertTrue(bool);
        op.executeUpdate("delete from activity where id = (select max(id) from activity)");
    }

    /**
     * Tests of getActivity method, of class Planner.
     */
    @Test
    public void testgetActivityThreeTypes() throws SQLException {
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description", 0, true, materials, 10, "wsNotes");
        Activity A = planner.getActivity(takeIndex());
        assertEquals("Planned", A.getType());
        assertEquals("factTest", A.getFactorySite());
        assertEquals("areaTest", A.getArea());
        assertEquals("typologyTest", A.getTypology());
        assertEquals("description", A.getDescription());
        assertEquals(0, A.getEstimatedTime());
        assertEquals(10, A.getWeek());
        assertEquals("wsNotes", A.getWorkspaceNotes());
        assertEquals("materialTest", materials.getLast());
        planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", "description9", 0, true, materials, 10, "wsNotes");
        assertEquals("EWO", planner.getActivity(takeIndex()).getType());
        planner.createActivity("Extra", "factTest", "areaTest", "typologyTest", "description11", 0, true, materials, 10, "wsNotes");
        assertEquals("Extra", planner.getActivity(takeIndex()).getType());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");

    }

    /**
     * Tests of deleteActivity method, of class Planner.
     */
    @Test(expected = SQLException.class)
    public void testDeleteActivityExists() throws SQLException {
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description22", 0, true, materials, 10, "wsNotes");
        int id = takeIndex();
        planner.deleteActivity(id);
        ResultSet rst = op.executeQuery("select * from activity where activityId=" + id);
        rst.next();
        int x = rst.getInt("activityId");
    }

    /**
     * Tests of modifyActivity method, of class Planner.
     */

    @Test
    public void testModifyActivity() throws SQLException {
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description", 0, true, materials, 10, "wsNotes");
        Activity A = planner.getActivity(takeIndex());
        A = planner.modifyActivity(A, "ws2", "modified", 111, null);
        assertEquals("ws2", A.getWorkspaceNotes());
        assertEquals("description", A.getDescription());
        assertEquals(0, A.getEstimatedTime());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", "description44", 0, true, materials, 10, "wsNotes");
        EWOActivity Aewo = (EWOActivity) planner.getActivity(takeIndex());
        Aewo = (EWOActivity) planner.modifyActivity(Aewo, "wsEWO", "modifiedEWO", 111, competencies);
        assertEquals("wsNotes", Aewo.getWorkspaceNotes());
        assertEquals("modifiedEWO", Aewo.getDescription());
        assertEquals(111, Aewo.getEstimatedTime());
        assertEquals("competenceTest", Aewo.getCompetenciesList().getFirst());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        planner.createActivity("Extra", "factTest", "areaTest", "typologyTest", "description44", 0, true, materials, 10, "wsNotes");
        ExtraActivity Aextra = (ExtraActivity) planner.getActivity(takeIndex());
        Aextra = (ExtraActivity) planner.modifyActivity(Aextra, "wsExtra", "modifiedExtra", 111, competencies);
        assertEquals("wsNotes", Aextra.getWorkspaceNotes());
        assertEquals("modifiedExtra", Aextra.getDescription());
        assertEquals(111, Aextra.getEstimatedTime());
        assertEquals("competenceTest", Aewo.getCompetenciesList().getFirst());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");

    }

    /**
     * Tests of getActivities method, of class Planner.
     */
    @Test
    public void testGetActivities() throws SQLException {
        ResultSet rst = null;
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description55", 100, true, materials, 15, "wsNotes");
        int indiceUno = takeIndex();
        planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", "description66", 0, true, materials, 15, "wsNotes");
        int indiceDue = takeIndex();
        rst = planner.getActivities("15");
        assertNotNull(rst);
        rst.next();
        assertEquals(indiceDue, rst.getInt("id"));
        assertNull(rst.getString("estimated_time"));
        rst.next();
        assertEquals(indiceUno, rst.getInt("id"));
        assertEquals(100, rst.getInt("estimated_time"));
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");

    }

    @Test(expected = SQLException.class)
    public void testGetActivitiesWrongWeek() throws SQLException {
        ResultSet rst = planner.getActivities("100");
        rst.next();
        rst.getInt("id");

    }

    /**
     * Tests of getArray method, of class Planner.
     */
    @Test
    public void testGetArrayNoActivities() throws SQLException {
        op.executeUpdate("insert into users values('usTest','pwTest','maintainer')");
        int[] testarray = {60, 60, 60, 60, 60, 60, 60};
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description77", 100, true, materials, 15, "wsNotes");
        Activity a = planner.getActivity(takeIndex());
        int[] array = planner.getArray("usTest", a, 1);
        assertArrayEquals(testarray, array);
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");

    }

    @Test
    public void testGetArrayWithActivity() throws SQLException, UnsupportedOperationException {
        op.executeUpdate("insert into users values('usTest','pwTest','maintainer')");
        int[] testarray = {0, 60, 60, 60, 60, 60, 60};
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description88", 60, true, materials, 15, "wsNotes");
        Activity a = planner.getActivity(takeIndex());
        int[] array = planner.getArray("usTest", a, 1);
        planner.manageAvailability(array, "usTest", 1, 1, a);
        array = planner.getArray("usTest", a, 1);
        assertArrayEquals(testarray, array);
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
    }

    @Test
    public void testGetArrayEwo() throws SQLException, UnsupportedOperationException {
        int[] testarray = {40, 60, 60, 60, 60, 60, 60};
        op.executeUpdate("insert into users values('usTest','pwTest','maintainer')");
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description99", 20, false, materials, 15, "wsNotes");
        Activity a = planner.getActivity(takeIndex());
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description656", 20, true, materials, 15, "wsNotes");
        Activity b = planner.getActivity(takeIndex());
        planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", "description444", 10, false, materials, 15, "wsNotes");
        Activity ewo = planner.getActivity(takeIndex());
        int[] array = planner.getArray("usTest", a, 5);
        planner.manageAvailability(array, "usTest", 5, 1, a);
        array = planner.getArray("usTest", b, 5);
        planner.manageAvailability(array, "usTest", 5, 2, b);
        array = planner.getArray("usTest", ewo, 5);
        assertArrayEquals(testarray, array);
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");

    }

    /**
     * Tests of ManageAvailability method, of class Planner.
     */
    @Test
    public void testManageAvailabilityOneActivity() throws SQLException, UnsupportedOperationException {
        op.executeUpdate("insert into users values('usTest','pwTest','maintainer')");
        planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description111", 300, true, materials, 15, "wsNotes");
        Activity a = planner.getActivity(takeIndex());
        int[] array = planner.getArray("usTest", a, 2);
        planner.manageAvailability(array, "usTest", 2, 4, a);
        assertEquals(240, a.getEstimatedTime());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
    }

    @Test
    public void testManageAvailabilitySlotOccupied() throws SQLException {
        boolean bool = false;
        try {

            op.executeUpdate("insert into users values('usTest','pwTest','maintainer')");
            planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description121", 60, true, materials, 15, "wsNotes");
            Activity a = planner.getActivity(takeIndex());
            planner.createActivity("Planned", "factTest", "areaTest", "typologyTest", "description222", 5, true, materials, 15, "wsNotes");
            Activity b = planner.getActivity(takeIndex());
            int[] array = planner.getArray("usTest", a, 5);
            planner.manageAvailability(array, "usTest", 5, 1, a);
            array = planner.getArray("usTest", b, 5);
            planner.manageAvailability(array, "usTest", 5, 1, b);

        } catch (UnsupportedOperationException ex) {
            bool = true;
            assertTrue(bool);
            op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
            op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
        }
    }

}
