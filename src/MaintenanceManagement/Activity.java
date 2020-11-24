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
    private final int estimatedTime;
    private final boolean interruptible;
    private final String materials;
    private final int week;
    private String workspaceNotes;

    public Activity(int activityId, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, String materials, int week, String workspaceNotes) {
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
    }
    
}
