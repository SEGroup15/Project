/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.util.*;

/**
 *
 * @author saby_
 */
public class Activity {

    private final int activityId;
    private final String factorySite;
    private final String area;
    private final String typology;
    private final String description;
    private int estimatedTime;
    private final boolean interruptible;
    private final String materials;
    private final int week;
    private String workspaceNotes;
    private final Procedure procedure;

    public Activity(int activityId, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, String materials, int week, String workspaceNotes, Procedure procedure) {
        this.activityId = activityId;
        this.factorySite = factorySite;
        this.area = area;
        this.typology = typology;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.interruptible = interruptible;
        this.week = week;
        this.materials = materials;
        this.workspaceNotes = workspaceNotes;
        this.procedure = procedure;

    }

    public int getActivityId() {
        return activityId;
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

    public String getMaterials() {
        return materials;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public int getWeek() {
        return week;
    }

    public String getWorkspaceNotes() {
        return workspaceNotes;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    
}
