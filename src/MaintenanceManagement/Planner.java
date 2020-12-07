/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.util.*;
import java.sql.*;
import java.lang.Error;

/**
 *
 * @author saby_
 */
public class Planner extends User {

    private Connection conn;
    private Statement op;

    public Planner(String username, String password, String role, Connection conn) throws SQLException {
        super(username, password, role);
        this.conn = conn;
        this.op= conn.createStatement();
    }

    public Activity createActivity(int activityId, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, String materials, int week, String workspaceNotes, Procedure procedura, String type) {
        Activity act = new Activity(activityId, factorySite, area, typology, description, estimatedTime, interruptible, materials, week, workspaceNotes, procedura, type);
        return act;
    }

    public boolean addActivity(Activity a) throws SQLException {
        String material = (a.getMaterials() == null || a.getMaterials() == "") ? "''" : "'" + a.getMaterials() + "'";
        String workspaceNote = (a.getWorkspaceNotes() == null || a.getMaterials() == "") ? "''" : "'" + a.getWorkspaceNotes() + "'";
        String insert = "insert into activity values (" + a.getActivityId() + ",'" + a.getFactorySite() + "','" + a.getArea() + "','" + a.getTypology() + "','" + a.getDescription() + "'," + a.getEstimatedTime() + ",'"
                + a.isInterruptible() + "'," + material + "," + a.getWeek() + "," + workspaceNote + ",'" + a.getProcedure().getNome() + "','"+a.getType()+"')";
        op.executeUpdate(insert);
        return true;
    }
    
    

    public boolean deleteActivity(int activityId) throws SQLException {
        String delete = "delete from activity where activityId=" + activityId;
        op.executeUpdate(delete);
        return true;
    }

    public boolean modifyActivity(int idActivity, String workspaceNotes) throws SQLException {
        String modify = "update activity set workspaceNotes='" + workspaceNotes + "' where activityId=" + idActivity;
        op.executeUpdate(modify);
        return true;
    }
    
    public boolean modifyEwo(Activity a, int estimatedTime, String description, String[] lista) throws SQLException, InsertException {
    if ("EWO".equals(a.getType())){
    String modify = "update activity set estimatedTime = " + estimatedTime + ", description = '" + description +"' where activityId= " + a.getActivityId();
    op.executeUpdate(modify);
    a.setEstimatedTime(estimatedTime);
    a.setDescription(description);
    for(String stringa : lista) {
        String query ="insert into ewoComp values (" + a.getActivityId() + ",'" + stringa + "')";
        op.executeUpdate(query); }
    }
    else
        throw new InsertException();
    return true;}
            
            
    public ResultSet getActivities(String week) throws SQLException {
        ResultSet rst = op.executeQuery("select * from activity A where (week = " + week + " and A.estimatedTime!=0)");
        return rst;
    }

    public Activity getActivity(int id) throws SQLException {
        ResultSet rest = op.executeQuery("select * from activity where activityId = " + id);
        Activity a;
        rest.next();
        a = createActivity(id, rest.getString("factorySite"), rest.getString("area"), rest.getString("typology"), rest.getString("description"), rest.getInt("estimatedTime"), rest.getBoolean("interruptible"), rest.getString("materials"), rest.getInt("week"), rest.getString("workspacenotes"), new Procedure(rest.getString("procedura")), rest.getString("typ"));
        return a;
    }

    public int[] getArray(String maintainer, Activity act, int day) throws SQLException {
        int[] vector = new int[7];
        for (int i = 0; i < 7; i++) {
            vector[i] = 60;
        }
        ResultSet rst;
        if ("EWO".equals(act.getType())) {
            rst = op.executeQuery("select C.fascia,C.minuti from calendar C, activity A where(A.activityId=C.idattivita and A.interruptible= 'false' and C.maintainer='" + maintainer + "' and C.week= " + act.getWeek() + " and C.day= " + day + ")"); }
        else {
            rst = op.executeQuery("select fascia,minuti from calendar where(maintainer='" + maintainer + "' and week= " + act.getWeek() + " and day= " + day + ")"); }
        while (rst.next()) {
            vector[rst.getInt("fascia") - 1] -= rst.getInt("minuti");
        }
        return vector;
    }

    public void manageAvailability(int[] array, String maintainer, int day, int fascia, Activity attività) throws SQLException, InsertException {
        if (array[fascia-1] == 0)
            throw new InsertException();
        int estimated = attività.getEstimatedTime();
        int available = array[fascia-1];
        int time = (estimated<=available) ? estimated : available;
        op.executeUpdate("insert into calendar values ('" + maintainer + "'," + attività.getActivityId() + "," + attività.getWeek() + "," + fascia + "," + day + "," + time + ")");
        op.executeUpdate("update activity set estimatedTime=estimatedTime-" + time + " where activityId= " + attività.getActivityId());
        attività.setEstimatedTime(estimated-time);
        
        }
}