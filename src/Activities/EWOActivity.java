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
public class EWOActivity extends Activity {

    private LinkedList<String> competenciesList;

    /**
     *
     * @param id
     * @param factorySite
     * @param area
     * @param typology
     * @param interruptible
     * @param materials
     * @param week
     * @param workspaceNotes
     */
    public EWOActivity(int id, String factorySite, String area, String typology, boolean interruptible, LinkedList<String> materials, int week, String workspaceNotes) {
        this.id = id;
        this.factorySite = factorySite;
        this.area = area;
        this.typology = typology;
        this.interruptible = interruptible;
        this.materials = materials;
        this.week = week;
        this.workspaceNotes = workspaceNotes;
    }

    /**
     *
     * @param competenciesList
     */
    public void setCompetenciesList(LinkedList<String> competenciesList) {
        this.competenciesList = competenciesList;
    }

    /**
     *
     * @param description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
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
     * Returns competencies list;
     * @return
     */
    public LinkedList<String> getCompetenciesList() {
        return competenciesList;
    }

    
    /**
     * Insert an EWO activity into the database (through the view ewo_activity) and the materials linked with it;
     * It returns true if the upload was successful, false if not.
     * @param conn
     * @return
     */

    @Override
    public boolean upload(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("insert into ewo_activity values('" + this.factorySite + "','" + this.area + "','" + this.typology + "',"
                    + this.interruptible + "," + this.week + ",'" + this.workspaceNotes + "')");
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
     * Modify the EWO activity, setting all the editable fields (description, estimated time, competencies).
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
            this.setDescription(description);
            this.setEstimatedTime(estimatedTime);
            this.setCompetenciesList(competencies);
            st.executeUpdate("update activity set estimated_time = " + estimatedTime + ", description = '" + description + "' where id=" + id);
            st.executeQuery("select * from updatewo(" + this.id + ")");
            if (competencies != null) {
                for (String competence : competencies) {
                    st.executeUpdate("insert into ewo_competence values ('" + competence + "'," + id + ")");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExtraActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return "EWO";
    }
}
