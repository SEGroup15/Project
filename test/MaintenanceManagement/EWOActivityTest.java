/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import Activities.EWOActivity;
import Users.Planner;
import java.sql.*;
import java.util.LinkedList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author saby_
 */
public class EWOActivityTest {
    EWOActivity a;
    Connection conn;
    Statement op;
    String url = "jdbc:postgresql://localhost:5432/locale2";
    String pwd = "kek2";
    String user = "kek2";
    LinkedList<String> materials = new LinkedList<>();
    LinkedList<String> competencies = new LinkedList<>();
    Planner planner;

    @Before
    public void setUp() throws SQLException {
        planner = new Planner("kek", "kekkeroni");
        conn = DriverManager.getConnection(url, user, pwd);
        op = conn.createStatement();
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
     * Tests of upload method, of class EWOActivity.
     */
    @Test
    public void testUploadNoWsNoMaterials() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaTest", "typologyTest", true, null, 10, null);
        boolean bool = a.upload(conn);
        assertTrue(bool);
        op.executeUpdate("delete from activity where id = (select max(id) from activity)");

    }

    @Test
    public void testUploadWrongArea() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaWrong", "typologyTest", true, materials, 10, "wsnotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);

    }

            @Test
    public void testUploadWrongMaterials() throws SQLException {
        LinkedList<String> materiale = new LinkedList<>();
        materiale.add("HEHE");
        a = new EWOActivity(1, "factTest", "areaWrong", "typologyTest",  true,materiale , 10, "wsNotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);

    }

    
    @Test
    public void testUploadWrongTypology() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaTest", "typologyWrong", true, materials, 10, "wsnotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);
    }

    @Test
    public void testUploadZeroWeek() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaTest", "typologyTest", true, materials, 0, "wsnotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);

    }

    @Test
    public void testUploadOutOfIntervalWeek() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaTest", "typologyTest", true, materials, 55, "wsnotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);

    }

    @Test
    public void testUploadNegativeWeek() throws SQLException {
        a = new EWOActivity(1, "factTest", "areaTest", "typologyTest", true, materials, -10, "wsnotes");
        boolean bool = a.upload(conn);
        assertFalse(bool);

    }


        /**
         * Tests of modify method, of class EWOActivity.
         */
@Test
    public void TestModify() throws SQLException {
        planner.createActivity("EWO", "factTest", "areaTest", "typologyTest", "description", 0, true, materials, 10, "wsNotes");
        a = (EWOActivity) planner.getActivity(takeIndex());
        a.modify(conn, "newWSN", "blabla", 4, competencies);
        ResultSet rst = op.executeQuery("select description,estimated_time from activity where id =" + takeIndex());
        ResultSet r = op.executeQuery("select name_competence from ewo_competence where id = "+ takeIndex());
        r.next();
        rst.next();
        assertEquals("competenceTest",r.getString("name_competence"));
        assertEquals(4, rst.getInt("estimated_time"));
        assertEquals("blabla", rst.getString("description"));
        assertEquals("wsNotes", a.getWorkspaceNotes());
        assertEquals("blabla", a.getDescription());
        assertEquals(4, a.getEstimatedTime());
        op.executeUpdate("delete from activity where (id= ( select max(id) from activity))");
    }    
}

    
   
