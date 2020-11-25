/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.util.*;
import java.sql.*;



/**
 *
 * @author saby_
 */
public class Planner extends User {
    private Connection conn;
    public Planner(String username, String password, String role, Connection conn) {
        super(username,password,role);
        this.conn=conn;
    }
    
    
    public boolean addActivity(int activityId, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, String materials, int week, String workspaceNotes) throws SQLException {
        Statement op= conn.createStatement();
        
        String material = (materials==null) ? null : "'"+materials+"'";
        String workspaceNote = (workspaceNotes==null) ? null : "'"+workspaceNotes+"'";
        
        String insert="insert into activity values ("+activityId+",'"+factorySite+"','"+area+"','"+typology+"','"+description+"',"+estimatedTime+",'"
            +interruptible+"',"+material+","+ week+","+ workspaceNote +")";
        op.executeUpdate(insert);
        
        return true;
    }
    
    public boolean deleteActivity(int activityId) throws SQLException {
        Statement op= conn.createStatement();
        String delete = "delete from activity where activityId="+ activityId;
        op.executeUpdate(delete);
    
        return true;}
    
    public boolean modifyActivity(int idActivity, String workspaceNotes) throws SQLException {
        Statement op= conn.createStatement();
        String modify = "update activity set workspaceNotes='"+workspaceNotes+"' where activityId="+ idActivity;
        return true; 
    }
    
    public ResultSet getActivities(String week) throws SQLException{
        Statement op= conn.createStatement();
        ResultSet rst = op.executeQuery("select * from activity where week = " + week);
        return rst;
    }
}
    
    
