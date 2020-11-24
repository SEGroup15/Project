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

@Before
public void setUp() throws SQLException{
// apertura connessione usando driver di postgresql
conn = DriverManager.getConnection(url, user, pwd);
planner = new Planner("PLANNER","TEST","planner",conn);}

@After
public void end() throws SQLException {
conn.close();
}
    /**
     * Tests of addActivity method, of class Planner.
     */
    @Test
    public void testAddActivity() throws SQLException {
        boolean bool = planner.addActivity(1, "aa", "bb", "electrical", "dd", 60, true, "", 1, "qq");
        assertTrue(bool);
    }
    
    @Test
    public void testAddActivityWithoutMaterials() throws SQLException {
        boolean bool = planner.addActivity(2, "site", "area", "hydraulic", "description", 60, true, null, 2, "wsnotes");
        assertTrue(bool);
    }
    
        @Test
    public void testAddActivityWithoutNotes() throws SQLException {
        boolean bool = planner.addActivity(3, "site", "area", "hydraulic", "description", 60, true,"materials", 2, null);
        assertTrue(bool);
    }
    
     @Test(expected=SQLException.class)
    public void testAddActivitySameId() throws SQLException {
        planner.addActivity(3, "aa", "bb", "electrical", "dd", 60 ,true , null, 3, "qq");
    }
    
     @Test(expected=SQLException.class)
    public void testAddActivityZeroId() throws SQLException {
        planner.addActivity(0, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq");
    }
    
         @Test(expected=SQLException.class)
    public void testAddActivityNegativeId() throws SQLException {
        planner.addActivity(-1, "aa", "bb", "electrical", "dd", 60, true, "xd", 6, "qq");
    }
    
    
    @Test(expected=SQLException.class)
    public void testAddActivityWrongTypology() throws SQLException {
        planner.addActivity(99, "aa", "bb", "ELECTRICAL", "dd", 60, true,"xd", 8, "qq");
    }
   
    
        @Test(expected=SQLException.class)
    public void testAddActivityZeroWeek() throws SQLException {
        planner.addActivity(98, "aa", "bb", "electrical", "dd", 60, true,"xd", 0, "qq");
    }
    
        
        @Test(expected=SQLException.class)
    public void testAddActivityOutOfIntervalWeek() throws SQLException {
        planner.addActivity(97, "aa", "bb", "electrical", "dd", 60, true,"xd", 100, "qq");
    }
        
        @Test(expected=SQLException.class)
    public void testAddActivityNegativeWeek() throws SQLException {
        planner.addActivity(96, "aa", "bb", "electrical", "dd", 60, true,"xd", -1, "qq");
    }
    
    /**
     * Tests of deleteActivity method, of class Planner.
     */
    @Test
    public void testDeleteActivityExists() throws SQLException {
        boolean bool=planner.deleteActivity(1);
        assertTrue(bool);
    }
    /**
     * Tests of modifyActivity method, of class Planner.
     */
    @Test
    public void testModifyActivity() throws SQLException {
        boolean bool = planner.modifyActivity(99, "ciao");
        assertTrue(bool);
    }
    

    } 
