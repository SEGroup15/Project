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
    public Planner(String username, String password, String role, Connection conn) {
        super(username,password,role);
        this.conn=conn;
    }
    
    public Activity createActivity(int activityId, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, String materials, int week, String workspaceNotes, Procedure procedura) {
        Activity act = new Activity(activityId,factorySite,area,typology,description,estimatedTime,interruptible,materials,week,workspaceNotes,procedura);
    return act;
    }
    
    public boolean addActivity(Activity a) throws SQLException {
        Statement op= conn.createStatement();
        
        String material = (a.getMaterials()==null || a.getMaterials() == "") ? null : "'"+a.getMaterials()+"'";
        String workspaceNote = (a.getWorkspaceNotes()==null || a.getWorkspaceNotes() == "") ? null : "'"+a.getWorkspaceNotes()+"'";
        
        String insert="insert into activity values ("+a.getActivityId()+",'"+a.getFactorySite()+"','"+a.getArea()+"','"+a.getTypology()+"','"+a.getDescription()+"',"+a.getEstimatedTime()+",'"
            +a.isInterruptible()+"',"+ material+","+ a.getWeek()+","+ workspaceNote +",'"+a.getProcedure().getNome()+"')";
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
        op.executeUpdate(modify);
        return true; 
    }
    
    public ResultSet getActivities(String week) throws SQLException{
        Statement op= conn.createStatement();
        ResultSet rst = op.executeQuery("select * from activity where week = " + week);
        return rst;
    }
    
    public Activity getActivity(int id) throws SQLException {
    Statement op = conn.createStatement();
    ResultSet rst= op.executeQuery ("select * from activity where activityId = "+id);
    rst.next();
    return createActivity(id,rst.getString("factorySite"),rst.getString("area"),rst.getString("typology"),rst.getString("description"),rst.getInt("estimatedTime"),rst.getBoolean("interruptible"),rst.getString("materials"),rst.getInt("week"),rst.getString("workspaceNotes"),new Procedure(rst.getString("procedura")));
    }
    
    
    public int[] getArray(String maintainer,int week, int day) throws SQLException {
    int[] vector= new int[7];
    
    for(int i=0;i<7;i++) {
    vector[i]=60;
    }
    Statement op = conn.createStatement();
    ResultSet rst = op.executeQuery("select fascia,minuti from calendar where(maintainer='"+maintainer+"' and week= "+week+" and day= "+day+")");
    while(rst.next()) {
        vector[rst.getInt("fascia")-1]-=rst.getInt("minuti");
    }
    return vector;
    }

    public void manageArray(int[] array, String maintainer, int day, int position, Activity attività) throws SQLException, InsertException {
        Statement op = conn.createStatement();
        int time = attività.getEstimatedTime();
        
        int x=1;
        boolean y=true;
        int xtime=time-array[position-1];
        while (xtime>0){
            xtime-=60;
            x+=1; }
        if (x==1) {
        op.executeUpdate("insert into calendar values ('"+maintainer+"',"+attività.getActivityId()+","+attività.getWeek()+","+ position +","+ day+","+time+")");
        deleteActivity(attività.getActivityId());
        return;}
        System.out.println(x);
        
        for(int i=1;i<x;i++) {
            if (position<7)
            y = (array[position+i-1]==60) ? true : false;
            if (y==false || (position-1+x)>7) 
                    throw new InsertException();
            if (position==7)
                y = (array[position+i-1]==60) ? true : false;
            
            
             }
        
        for(int i=0;i<x;i++) {
            
            if(i+1==x) {
                System.out.println("HEHE");
            op.executeUpdate("insert into calendar values ('"+maintainer+"',"+attività.getActivityId()+","+attività.getWeek()+","+ position +","+ day+","+time+")");
            deleteActivity(attività.getActivityId());
            return;}
 
            op.executeUpdate("insert into calendar values ('"+maintainer+"',"+attività.getActivityId()+","+attività.getWeek()+","+ position +","+ day+","+array[position-1]+")");
            array[position-1]-=time;
            if (array[position-1]<0) {
                time=-array[position-1];
                array[position-1]=0;}
            position=position+1;
        
        }

        
    }
}
    
    
