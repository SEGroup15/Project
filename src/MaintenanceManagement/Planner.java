/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.util.*;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


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

    public Planner(String username, String password) throws SQLException {
        this.username= username;
        this.password=password;
        this.conn = DriverManager.getConnection(url, username, password);
        this.op= conn.createStatement();
    }

    public boolean createActivity(String type,String factorySite,String area,String typology,String description,int estimatedTime,boolean interruptible,LinkedList<String> materials,int week,String workspaceNotes) throws SQLException{      
        int id=1;
        ResultSet rst = op.executeQuery("select max(id) as max from activity");
        if(rst.next())
            id=rst.getInt("max")+1;
        
        switch (type) {
            case "Planned":
                return new PlannedActivity(id,factorySite,area,typology,description,estimatedTime,interruptible,materials,week,workspaceNotes).upload(conn);
            case "EWO":
                return new EWOActivity(id,factorySite,area,typology,interruptible,materials,week,workspaceNotes).upload(conn);
            case "Extra":
                return new ExtraActivity(id,factorySite,area,typology,interruptible,materials,week,workspaceNotes).upload(conn);
            default:
                throw new UnsupportedOperationException("Not supported.");
        }
    }
    
    
    public Activity getActivity(int id) throws SQLException{
        try{
        ResultSet rst = op.executeQuery("select * from activity where id="+id);
        ResultSet rstMaterials= op.executeQuery("select name_material from pm where id="+id);
        ResultSet rstProcedure = null;
        ResultSet rstCompetenciesProcedure =null;
        ResultSet rstCompetencies = null;
        LinkedList<String> materials = new LinkedList<>();
        LinkedList<String> competencies = new LinkedList<>();
        LinkedList<String> competenciesProcedure = new LinkedList<>();
        
        rst.next();
        String type=rst.getString("type");
        while (rstMaterials.next())
            materials.add(rstMaterials.getString("name_material"));
        
        switch (type) {
            case "planned":
                rstProcedure = op.executeQuery("select P.name_procedure,url_pdf from procedure P,possession_act PA where P.name_procedure=PA.name_procedure and id="+id);
                rstCompetenciesProcedure = op.executeQuery("select name_competence from possession_act PA,possession_procedure PP where PA.name_procedure=PP.name_procedure and id="+id);
                while (rstCompetenciesProcedure.next())
                    competenciesProcedure.add(rstCompetenciesProcedure.getString("name_competence"));
                Procedure p=null;
                if(rstProcedure.next())
                    p = new Procedure(rstProcedure.getString("name_procedure"),rstProcedure.getString("url_pdf"),competenciesProcedure);
                PlannedActivity pa= new PlannedActivity(id,rst.getString("factory_site"),rst.getString("area"),rst.getString("name_typology"),rst.getString("description"),rst.getInt("estimated_time"),rst.getBoolean("interruptibility"),materials,rst.getInt("week"),rst.getString("workspace_notes"));
                pa.setProcedure(p);
                return pa;
            case "ewo":
                rstCompetencies = op.executeQuery("select name_competence from ewo_competence where id="+id);
                while (rstCompetencies.next())
                    competencies.add(rstCompetencies.getString("name_competence"));
                EWOActivity ewo= new EWOActivity(id,rst.getString("factory_site"),rst.getString("area"),rst.getString("name_typology"),rst.getBoolean("interruptibility"),materials,rst.getInt("week"),rst.getString("workspace_notes"));
                ewo.setDescription(rst.getString("description"));
                ewo.setEstimatedTime(rst.getInt("estimated_time"));
                ewo.setCompetenciesList(competencies);
                return ewo;
            case "extra":
                rstCompetencies = op.executeQuery("select name_competence from extra_competence where id="+id);
                while (rstCompetencies.next())
                    competencies.add(rstCompetencies.getString("name_competence"));
                ExtraActivity extra= new ExtraActivity(id,rst.getString("factory_site"),rst.getString("area"),rst.getString("name_typology"),rst.getBoolean("interruptibility"),materials,rst.getInt("week"),rst.getString("workspace_notes"));
                extra.setDescription(rst.getString("description"));
                extra.setEstimatedTime(rst.getInt("estimated_time"));
                extra.setCompetenciesList(competencies);
                return extra;
            default:
                throw new UnsupportedOperationException("Not supported.");
        }}catch(SQLException ex){
            System.out.println(ex);
        }
        return null;
    }
    
    

    public boolean deleteActivity(int activityId) throws SQLException {
        String delete = "delete from activity where id=" + activityId;
        op.executeUpdate(delete);
        return true;
    }

    public Activity modifyActivity(Activity a,String workspaceNotes,String description,int estimatedTime,LinkedList<String> competencies) throws SQLException {
        a.modify(conn,workspaceNotes, description, estimatedTime, competencies);
        return a;
    }
    
                      
    public ResultSet getActivities(String week) throws SQLException {
        ResultSet rst = op.executeQuery("select * from activity where (week = " + week + " and (estimated_time!=0 or estimated_time is null))");
        return rst;
    }


    public int[] getArray(String maintainer, Activity act, int day){
        try{
        String[] days = {"lunedi","martedi","mercoledi","giovedi","venerdi","sabato","domenica"};
        int[] vector = {60,60,60,60,60,60,60};
        ResultSet rst = op.executeQuery("select * from activity where id="+act.getId());
        rst.next();
        String type=rst.getString("type");
        if (type.equals("ewo")) {
            rst = op.executeQuery("select fascia,minuti from assigned ASS, activity A where(A.id=ASS.id and A.interruptibility= 'false' and ASS.username='" + maintainer + "' and A.week= " + act.getWeek() + " and ASS.giorno= '" + days[day-1] + "')"); }
        else {
            rst = op.executeQuery("select fascia,minuti from assigned ASS,activity A where(ASS.username='" + maintainer + "' and A.week= " + act.getWeek() + " and ASS.giorno= '" + days[day-1] + "' and A.id="+act.getId()+")"); }
        while (rst.next()) {
            vector[rst.getInt("fascia") - 1] -= rst.getInt("minuti");
        }
        return vector;
        }catch(SQLException ex){
            System.out.println(ex);
        }
        return null;
    }

    public void manageAvailability(int[] array, String maintainer, int day, int fascia, Activity a) throws SQLException, InsertException {
        try{
        String[] days = {"lunedi","martedi","mercoledi","giovedi","venerdi","sabato","domenica"};
        if (array[fascia-1] == 0)
            throw new InsertException();
        int estimated = a.getEstimatedTime();
        int available = array[fascia-1];
        int time = (estimated<=available) ? estimated : available;
        op.executeUpdate("insert into assigned values ('"+days[day-1] + "'," + fascia + "," + a.getId() + ",'" + maintainer + "'," + time + ")");
        op.executeUpdate("update activity set estimated_time=estimated_time-" + time + " where id= " + a.getId());
        a.setEstimatedTime(estimated-time);
        } catch(SQLException ex ){
            System.out.println(ex);
        }
        }
    
    
public ResultSet getAssignedEWO (String week) throws SQLException{
        Statement op = conn.createStatement();
        ResultSet rst = op.executeQuery("select * from activity  where (type='ewo' and week = " + week + " and id in (select id from assigned))");
        return rst;
    }
    
    public String[] getEWOestime(int id) throws SQLException{
        Statement op = conn.createStatement();
        ResultSet rst = op.executeQuery("select sum(minuti),giorno from assigned where id="+ id+" group by giorno");
        String[] esti=new String[2];
        while(rst.next()){
            esti[0]=rst.getString("sum");
            esti[1]=rst.getString("giorno");
    }
        return esti;
}
    
    
    public String getEWOTotalEstime(int id) throws SQLException{
        Statement op = conn.createStatement();
        ResultSet rst = op.executeQuery("select sum(minuti) from assigned where id="+ id);
        String esti="";
        while(rst.next()){
          esti = rst.getString("sum");
    }
        return esti; 
    }
    
    public boolean EWOexists(int id) throws SQLException{
        boolean exist=false;
        Statement op = conn.createStatement();
        ResultSet rst = op.executeQuery("select id from assigned where id="+ id);
        while(rst.next()){
            if (rst.getString("id") != null){
                return true;
            }
        }
        return false;
    }
    
    public ResultSet getAssignedSkills(int id) throws SQLException{
        Statement op = conn.createStatement();
        ResultSet rst = op.executeQuery("select name_competence from ewo_competence where id="+ id);
        return rst;        
    }
    
    public ResultSet getAllMaintainer() throws SQLException{
       Statement op = conn.createStatement();
       ResultSet maintainerList = op.executeQuery("select username from users where role= 'maintainer'");
       return maintainerList;
    }
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getRole() {
        return "Planner";
    }
}