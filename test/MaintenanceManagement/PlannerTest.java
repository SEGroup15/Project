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
    


//LinkedList<String> materials= new LinkedList<>();
Planner planner;
Connection conn;
String url="jdbc:postgresql://suleiman.db.elephantsql.com:5432/litqgeus";
String pwd = "tlZzxfA1WKpHPYzim2E_PENlR6oDlZ52";
String user = "litqgeus";
Procedure procedure;

@Before
public void setUp() throws SQLException{
// apertura connessione usando driver di postgresql
conn = DriverManager.getConnection(url, user, pwd);
planner = new Planner("PLANNER","TEST","planner",conn);
procedure= new Procedure("nome");}

@After
public void end() throws SQLException {
conn.close();
}
    /**
     * Tests of addActivity method, of class Planner.
     */
    @Test
    public void testAddActivity() throws SQLException {
        boolean bool = planner.addActivity(new Activity(7, "aa", "bb", "electrical", "dd", 60, true, "", 1, "qq",procedure));
        assertTrue(bool);
    }
    
    @Test
    public void testAddActivityWithoutMaterials() throws SQLException {
        boolean bool = planner.addActivity(new Activity(14, "site", "area", "hydraulic", "description", 60, true, null, 2, "wsnotes",procedure));
        assertTrue(bool);
    }
    
        @Test
    public void testAddActivityWithoutNotes() throws SQLException {
        boolean bool = planner.addActivity(new Activity(15, "site", "area", "hydraulic", "description", 60, true,"materials", 2, null,procedure));
        assertTrue(bool);
    }
    
     @Test(expected=SQLException.class)
    public void testAddActivitySameId() throws SQLException {
        planner.addActivity(new Activity(3, "aa", "bb", "electrical", "dd", 60 ,true , null, 3, "qq",procedure));
    }
    
     @Test(expected=SQLException.class)
    public void testAddActivityZeroId() throws SQLException {
        planner.addActivity(new Activity(0, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq",procedure));
    }
    
         @Test(expected=SQLException.class)
    public void testAddActivityNegativeId() throws SQLException {
        planner.addActivity(new Activity(-1, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq",procedure));
    }
    
    
    @Test(expected=SQLException.class)
    public void testAddActivityWrongTypology() throws SQLException {
        planner.addActivity(new Activity(99, "aa", "bb", "ELECTRICAL", "dd", 60, true,"xd", 8, "qq",procedure));
    }
   
    
        @Test(expected=SQLException.class)
    public void testAddActivityZeroWeek() throws SQLException {
        planner.addActivity(new Activity(98, "aa", "bb", "electrical", "dd", 60, true,"xd", 0, "qq",procedure));
    }
    
        
        @Test(expected=SQLException.class)
    public void testAddActivityOutOfIntervalWeek() throws SQLException {
        planner.addActivity(new Activity(97, "aa", "bb", "electrical", "dd", 60, true,"xd", 100, "qq",procedure));
    }
        
        @Test(expected=SQLException.class)
    public void testAddActivityNegativeWeek() throws SQLException {
        planner.addActivity(new Activity(96, "aa", "bb", "electrical", "dd", 60, true,"xd", -1, "qq",procedure));
    }
    
    @Test(expected=SQLException.class)
    public void testAddActivityNegativeEstimatedTime() throws SQLException {
        planner.addActivity(new Activity(96, "aa", "bb", "electrical", "dd", -3, true,"xd", 50, "qq",procedure));
    }
    /**
     * Tests of deleteActivity method, of class Planner.
     */
    @Test
    public void testDeleteActivityExists() throws SQLException {
        boolean bool1=planner.deleteActivity(7);
        boolean bool2=planner.deleteActivity(14);
        boolean bool3=planner.deleteActivity(15);
        assertTrue(bool1);
        assertTrue(bool2);
        assertTrue(bool3);
    }
    /**
     * Tests of modifyActivity method, of class Planner.
     */
    @Test
    public void testModifyActivity() throws SQLException {
        boolean bool = planner.modifyActivity(99, "ciao");
        assertTrue(bool);
    }
    
    @Test
    public void testGetActivities() throws SQLException {
        ResultSet rst= null;
        rst = planner.getActivities("1");
        assertNotNull(rst);
    }
    
    @Test (expected=SQLException.class)
    public void testGetActivitiesWrongTypeWeek() throws SQLException {
        ResultSet rst= null;
        rst = planner.getActivities("a");
    }
    

    } 
