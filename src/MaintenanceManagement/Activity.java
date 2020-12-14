/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author saby_
 */
public abstract class Activity {
    protected int id;
    protected String factorySite;
    protected String area;
    protected String typology;
    protected String description;
    protected int estimatedTime;
    protected boolean interruptible;
    protected LinkedList<String> materials;
    protected int week;
    protected String workspaceNotes;

    public int getId() {
        return id;
    }
    public String getFactorySite() {
        return factorySite;
    }

    public String getArea() {
        return area;
    }

    public String getTypology() {
        return typology;
    }

    public String getDescription() {
        return description;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public boolean isInterruptible() {
        return interruptible;
    }

    public LinkedList<String> getMaterials() {
        return materials;
    }

    public int getWeek() {
        return week;
    }

    public String getWorkspaceNotes() {
        return workspaceNotes;
    }

    public abstract void setDescription(String description);

    public abstract void setEstimatedTime(int estimatedTime);
    
    public abstract boolean upload(Connection conn);
    
    public abstract void modify(Connection conn,String workspaceNotes,String description, int estimatedTime,LinkedList<String> competencies);
        
    public abstract String getType();


    
    
}
