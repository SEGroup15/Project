/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Activities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giuse
 */
public class PlannedActivity extends Activity {

    private Procedure procedure;

    /**
     *
     * @param id
     * @param factorySite
     * @param area
     * @param typology
     * @param description
     * @param estimatedTime
     * @param interruptible
     * @param materials
     * @param week
     * @param workspaceNotes
     */
    public PlannedActivity(int id, String factorySite, String area, String typology, String description, int estimatedTime, boolean interruptible, LinkedList<String> materials, int week, String workspaceNotes) {
        this.id = id;
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

    /**
     *
     * @param workspaceNotes
     */
    public void setWorkspaceNotes(String workspaceNotes) {
        this.workspaceNotes = workspaceNotes;
    }

    /**
     *
     * @return
     */
    public Procedure getProcedure() {
        return procedure;
    }

    /**
     *
     * @param procedure
     */
    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    /**
     * Insert a planned activity into the database (through the view planned_activity) and the materials linked with it;
     * It returns true if the upload was successful, false if not.
     * @param conn
     * @return
     */
    @Override
    public boolean upload(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("insert into planned_activity values('" + this.factorySite + "','" + this.area + "','" + this.typology + "','"
                    + this.description + "'," + this.estimatedTime + "," + this.interruptible + "," + this.week + ",'" + this.workspaceNotes + "')");
            if (this.materials != null) {
                for (String mat : this.materials) {
                    st.executeUpdate("insert into pm values('" + mat + "'," + this.id + ")");
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    /**
     *
     * @param estimatedTime
     */
    @Override
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    /**
     *
     * @param description
     */
    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Modify the planned activity, setting all the editable fields (workspace notes).
     * @param conn
     * @param workspaceNotes
     * @param description
     * @param estimatedTime
     * @param competencies
     */
    @Override
    public void modify(Connection conn, String workspaceNotes, String description, int estimatedTime, LinkedList<String> competencies) {
        try {
            Statement st = conn.createStatement();
            this.setWorkspaceNotes(workspaceNotes);
            st.executeUpdate("update activity set workspace_notes='" + workspaceNotes + "' where id=" + id);
        } catch (SQLException ex) {
            Logger.getLogger(PlannedActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return "Planned";
    }

}
