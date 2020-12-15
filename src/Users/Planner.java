/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Users;

import Activities.Activity;
import Activities.EWOActivity;
import Activities.ExtraActivity;
import Activities.PlannedActivity;
import Activities.Procedure;
import java.util.*;
import java.sql.*;

/**
 *
 * @author saby_
 */
public class Planner implements User {

    private final String username;
    private final String password;
    private final String url = "jdbc:postgresql://localhost:5432/locale2";
    private final Connection conn;
    private final Statement op;

    /**
     *
     * @param username
     * @param password
     * @throws SQLException
     */
    public Planner(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        this.conn = DriverManager.getConnection(url, username, password);
        this.op = conn.createStatement();
    }

    /**
     * By type, it creates and uploads the specified activity into the database.
     * @param type
     * @param factorySite
     * @param area
     * @param typology
     * @param description
     * @param estimatedTime
     * @param interruptible
     * @param materials
     * @param week
     * @param workspaceNotes
     * @return
     * @throws SQLException
     */
    public boolean createActivity(String type, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, LinkedList<String> materials, int week, String workspaceNotes) throws SQLException {
        int id = 1;
        ResultSet rst = op.executeQuery("select max(id) as max from activity");
        if (rst.next()) {
            id = rst.getInt("max") + 1;
        }

        switch (type) {
            case "Planned":
                return new PlannedActivity(id, factorySite, area, typology, description, estimatedTime, interruptible, materials, week, workspaceNotes).upload(conn);
            case "EWO":
                return new EWOActivity(id, factorySite, area, typology, interruptible, materials, week, workspaceNotes).upload(conn);
            case "Extra":
                return new ExtraActivity(id, factorySite, area, typology, interruptible, materials, week, workspaceNotes).upload(conn);
            default:
                throw new UnsupportedOperationException("Not supported.");
        }
    }

    /**
     * Returns the specified type activity with all its features, selecting it by id.
     * @param id
     * @return
     * @throws SQLException
     */
    public Activity getActivity(int id) throws SQLException {
        try {
            ResultSet rst = op.executeQuery("select * from activity where id=" + id);
            ResultSet rstMaterials = op.executeQuery("select name_material from pm where id=" + id);
            LinkedList<String> materials = new LinkedList<>();
            rst.next();
            String type = rst.getString("type");
            while (rstMaterials.next()) {
                materials.add(rstMaterials.getString("name_material"));
            }
            switch (type) {
                case "planned":
                    return getPlanned(rst, id, materials);
                case "ewo":
                    return getEwo(rst, id, materials);
                case "extra":
                    return getExtra(rst, id, materials);
                default:
                    throw new UnsupportedOperationException("Not supported.");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    /* Returns the planned activity with the specified id. */ 
    private PlannedActivity getPlanned(ResultSet rst, int id, LinkedList<String> materials) throws SQLException {
        LinkedList<String> competenciesProcedure = new LinkedList<>();
        ResultSet rstProcedure = op.executeQuery("select P.name_procedure,url_pdf from procedure P,possession_act PA where P.name_procedure=PA.name_procedure and id=" + id);
        ResultSet rstCompetenciesProcedure = op.executeQuery("select name_competence from possession_act PA,possession_procedure PP where PA.name_procedure=PP.name_procedure and id=" + id);
        while (rstCompetenciesProcedure.next()) {
            competenciesProcedure.add(rstCompetenciesProcedure.getString("name_competence"));
        }
        Procedure p = null;
        if (rstProcedure.next()) {
            p = new Procedure(rstProcedure.getString("name_procedure"), rstProcedure.getString("url_pdf"), competenciesProcedure);
        }
        PlannedActivity pa = new PlannedActivity(id, rst.getString("factory_site"), rst.getString("area"), rst.getString("name_typology"), rst.getString("description"), rst.getInt("estimated_time"), rst.getBoolean("interruptibility"), materials, rst.getInt("week"), rst.getString("workspace_notes"));
        pa.setProcedure(p);
        return pa;
    }
    
    /* Returns the EWO activity with the specified id. */ 
    private EWOActivity getEwo(ResultSet rst, int id, LinkedList<String> materials) throws SQLException {
        LinkedList<String> competencies = new LinkedList<>();
        ResultSet rstCompetencies = op.executeQuery("select name_competence from ewo_competence where id=" + id);
        while (rstCompetencies.next()) {
            competencies.add(rstCompetencies.getString("name_competence"));
        }
        EWOActivity ewo = new EWOActivity(id, rst.getString("factory_site"), rst.getString("area"), rst.getString("name_typology"), rst.getBoolean("interruptibility"), materials, rst.getInt("week"), rst.getString("workspace_notes"));
        ewo.setDescription(rst.getString("description"));
        ewo.setEstimatedTime(rst.getInt("estimated_time"));
        ewo.setCompetenciesList(competencies);
        return ewo;

    }
    
    /* Returns the extra activity with the specified id. */ 
    private ExtraActivity getExtra(ResultSet rst, int id, LinkedList<String> materials) throws SQLException {
        LinkedList<String> competencies = new LinkedList<>();
        ResultSet rstCompetencies = op.executeQuery("select name_competence from extra_competence where id=" + id);
        while (rstCompetencies.next()) {
            competencies.add(rstCompetencies.getString("name_competence"));
        }
        ExtraActivity extra = new ExtraActivity(id, rst.getString("factory_site"), rst.getString("area"), rst.getString("name_typology"), rst.getBoolean("interruptibility"), materials, rst.getInt("week"), rst.getString("workspace_notes"));
        extra.setDescription(rst.getString("description"));
        extra.setEstimatedTime(rst.getInt("estimated_time"));
        extra.setCompetenciesList(competencies);
        return extra;
    }

    /**
     * Deletes the activity with the specified ID from the database.
     * @param activityId
     * @throws SQLException
     */
    public void deleteActivity(int activityId) throws SQLException {
        String delete = "delete from activity where id=" + activityId;
        op.executeUpdate(delete);
    }

    /**
     * Modifies the activity passed as a parameter and sets the activity values to the passed other parameters.
     * @param a
     * @param workspaceNotes
     * @param description
     * @param estimatedTime
     * @param competencies
     * @return
     * @throws SQLException
     */
    public Activity modifyActivity(Activity a, String workspaceNotes, String description, int estimatedTime, LinkedList<String> competencies) throws SQLException {
        a.modify(conn, workspaceNotes, description, estimatedTime, competencies);
        return a;
    }

    /**
     *
     * @param week
     * @return
     * @throws SQLException
     */
    public ResultSet getActivities(String week) throws SQLException {
        ResultSet rst = op.executeQuery("select * from activity where (week = " + week + " and (estimated_time!=0 or estimated_time is null)) order by id desc");
        return rst;
    }

    /**
     * Returns an array of integers (each cell of the array represents a time slot) relative to the availability in minutes of a specified maintainer in a specified date.
     * @param maintainer
     * @param act
     * @param day
     * @return
     */
    public int[] getArray(String maintainer, Activity act, int day) {
        try {
            String[] days = {"lunedi", "martedi", "mercoledi", "giovedi", "venerdi", "sabato", "domenica"};
            int[] vector = {60, 60, 60, 60, 60, 60, 60};
            ResultSet rst = op.executeQuery("select * from activity where id=" + act.getId());
            rst.next();
            String type = rst.getString("type");
            if (type.equals("ewo")) {
                rst = op.executeQuery("select fascia,minuti from assigned ASS, activity A where(A.id=ASS.id and A.interruptibility= 'false' and ASS.username='" + maintainer + "' and A.week= " + act.getWeek() + " and ASS.giorno= '" + days[day - 1] + "')");
            } else {
                rst = op.executeQuery("select fascia,minuti from assigned ASS,activity A where(ASS.username='" + maintainer + "' and A.week= " + act.getWeek() + " and ASS.giorno= '" + days[day - 1] + "' and A.id=" + act.getId() + ")");
            }
            while (rst.next()) {
                vector[rst.getInt("fascia") - 1] -= rst.getInt("minuti");
            }
            return vector;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    /**
     * Inserts the activity passed as parameter in a specified day and time slot and update the estimated time of the activity.
     * @param array
     * @param maintainer
     * @param day
     * @param fascia
     * @param a
     * @throws SQLException
     * @throws UnsupportedOperationException
     */
    public void manageAvailability(int[] array, String maintainer, int day, int fascia, Activity a) throws SQLException, UnsupportedOperationException {
        try {
            String[] days = {"lunedi", "martedi", "mercoledi", "giovedi", "venerdi", "sabato", "domenica"};
            if (array[fascia - 1] == 0) {
                throw new UnsupportedOperationException("Can't insert here");
            }
            int estimated = a.getEstimatedTime();
            int available = array[fascia - 1];
            int time = (estimated <= available) ? estimated : available;
            op.executeUpdate("insert into assigned values ('" + days[day - 1] + "'," + fascia + "," + a.getId() + ",'" + maintainer + "'," + time + ")");
            op.executeUpdate("update activity set estimated_time=estimated_time-" + time + " where id= " + a.getId());
            a.setEstimatedTime(estimated - time);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     *
     * @return
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRole() {
        return "Planner";
    }
}
