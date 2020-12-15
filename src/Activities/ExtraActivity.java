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
public class ExtraActivity extends Activity {

    private LinkedList<String> competenciesList;

    public ExtraActivity(int id, String factorySite, String area, String typology, boolean interruptible, LinkedList<String> materials, int week, String workspaceNotes) {
        this.id = id;
        this.factorySite = factorySite;
        this.area = area;
        this.typology = typology;
        this.interruptible = interruptible;
        this.materials = materials;
        this.week = week;
        this.workspaceNotes = workspaceNotes;
    }

    public void setCompetenciesList(LinkedList<String> competenciesList) {
        this.competenciesList = competenciesList;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public LinkedList<String> getCompetenciesList() {
        return competenciesList;
    }

    @Override
    public boolean upload(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("insert into extra_activity values('" + this.factorySite + "','" + this.area + "','" + this.typology + "',"
                    + this.interruptible + "," + this.week + ",'" + this.workspaceNotes + "')");
            if (this.materials != null) {
                for (String mat : this.materials) {
                    st.executeUpdate("insert into pm values('" + mat + "'," + this.id + ")");
                }
            }
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public void modify(Connection conn, String workspaceNotes, String description, int estimatedTime, LinkedList<String> competencies) {
        try {
            Statement st = conn.createStatement();
            this.setDescription(description);
            this.setEstimatedTime(estimatedTime);
            this.setCompetenciesList(competencies);
            st.executeUpdate("update activity set estimated_time = " + estimatedTime + ", description = '" + description + "' where id=" + id);
            for (String competence : competencies) {
                if (competence == null) {
                    break;
                }
                st.executeUpdate("insert into extra_competence values ('" + competence + "'," + id + ")");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExtraActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getType() {
        return "Extra";
    }
}
