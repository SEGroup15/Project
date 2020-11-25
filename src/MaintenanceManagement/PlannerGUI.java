/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author giuse
 */
public class PlannerGUI extends javax.swing.JFrame {

    /**
     * Creates new form PlannerGUI
     */
    private final Connection conn;
    private final Planner Planner;
    private static final String url = "jdbc:postgresql://suleiman.db.elephantsql.com:5432/litqgeus";
    private static final String pwd = "tlZzxfA1WKpHPYzim2E_PENlR6oDlZ52";
    private static final String user = "litqgeus";
    
    public PlannerGUI(){
        initComponents();
        conn = PlannerGUI.startConnection();
        Planner= new Planner("admin","admin","Planner",conn);    
        setList(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameManageMaintenance = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduledMaintenanceList = new javax.swing.JTable();
        LabelWeekNumber = new javax.swing.JTextField();
        buttonManageMaintenance = new javax.swing.JButton();
        comboBoxWeek = new javax.swing.JComboBox<>();

        frameManageMaintenance.setMinimumSize(new java.awt.Dimension(800, 500));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Angelo e Sabatino");

        javax.swing.GroupLayout frameManageMaintenanceLayout = new javax.swing.GroupLayout(frameManageMaintenance.getContentPane());
        frameManageMaintenance.getContentPane().setLayout(frameManageMaintenanceLayout);
        frameManageMaintenanceLayout.setHorizontalGroup(
            frameManageMaintenanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameManageMaintenanceLayout.createSequentialGroup()
                .addContainerGap(283, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(208, 208, 208))
        );
        frameManageMaintenanceLayout.setVerticalGroup(
            frameManageMaintenanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameManageMaintenanceLayout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(jLabel1)
                .addContainerGap(338, Short.MAX_VALUE))
        );

        this.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PlannerGUI");
        setMinimumSize(new java.awt.Dimension(800, 500));

        scheduledMaintenanceList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "AREA", "TYPE", "Estimated intervention time [min]"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scheduledMaintenanceList.setMaximumSize(new java.awt.Dimension(800, 400));
        scheduledMaintenanceList.setMinimumSize(new java.awt.Dimension(800, 400));
        scheduledMaintenanceList.setPreferredSize(new java.awt.Dimension(800, 400));
        scheduledMaintenanceList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduledMaintenanceListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(scheduledMaintenanceList);
        if (scheduledMaintenanceList.getColumnModel().getColumnCount() > 0) {
            scheduledMaintenanceList.getColumnModel().getColumn(0).setHeaderValue("ID");
            scheduledMaintenanceList.getColumnModel().getColumn(1).setHeaderValue("AREA");
            scheduledMaintenanceList.getColumnModel().getColumn(2).setHeaderValue("TYPE");
            scheduledMaintenanceList.getColumnModel().getColumn(3).setHeaderValue("Estimated intervention time [min]");
        }

        LabelWeekNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LabelWeekNumber.setText("               WEEK N°");
        LabelWeekNumber.setFocusable(false);
        LabelWeekNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabelWeekNumberActionPerformed(evt);
            }
        });

        buttonManageMaintenance.setText("Manage Maintenance ");
        buttonManageMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonManageMaintenanceActionPerformed(evt);
            }
        });

        comboBoxWeek.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52" }));
        comboBoxWeek.setSelectedItem(2);
        comboBoxWeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxWeekActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelWeekNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboBoxWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonManageMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelWeekNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonManageMaintenance)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LabelWeekNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabelWeekNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LabelWeekNumberActionPerformed

    private void buttonManageMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonManageMaintenanceActionPerformed
        new PlannerRecordGUI().setVisible(true);
    }//GEN-LAST:event_buttonManageMaintenanceActionPerformed

    private void scheduledMaintenanceListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduledMaintenanceListMouseClicked
        new PlannerVerificationGUI().setVisible(true);
        
    }//GEN-LAST:event_scheduledMaintenanceListMouseClicked

    private void comboBoxWeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxWeekActionPerformed
        setList(false);
    }//GEN-LAST:event_comboBoxWeekActionPerformed
    
    
    private void setList(boolean initialize){
        if (initialize==true) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(LocalDate.now().getYear(),LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
        this.comboBoxWeek.getModel().setSelectedItem(calendar.get(Calendar.WEEK_OF_YEAR));
        };
        String[] nomi = {"ID","AREA","TYPE","Estimated intervention time[min]"};
        ResultSet rst = null;
        try {
            rst = Planner.getActivities(this.comboBoxWeek.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(PlannerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DefaultTableModel tab = (DefaultTableModel) this.scheduledMaintenanceList.getModel();
        tab.setRowCount(0);
        tab.setColumnIdentifiers(nomi);
        Object[] row = new Object[4];
        try{
            while(rst.next()){
                row[0]=rst.getString("activityid");
                row[1]=rst.getString("factorysite") + " - " + rst.getString("area");
                row[2]=rst.getString("typology");
                row[3]=rst.getString("estimatedtime");
                tab.addRow(row);
            }
        }catch(SQLException ex){
            
        }
             
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
               
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlannerGUI().setVisible(true);
            }
        });
    }
    
    public static Connection startConnection(){
        Connection conn = null;
        try {
            conn=DriverManager.getConnection(url, user, pwd);
        }
        catch(SQLException ex){
            
        }
        return conn;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField LabelWeekNumber;
    private javax.swing.JButton buttonManageMaintenance;
    private javax.swing.JComboBox<String> comboBoxWeek;
    private javax.swing.JFrame frameManageMaintenance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable scheduledMaintenanceList;
    // End of variables declaration//GEN-END:variables
}
