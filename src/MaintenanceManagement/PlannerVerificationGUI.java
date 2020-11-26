/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;
import java.io.*;

/**
 *
 * @author ivang
 */
public class PlannerVerificationGUI extends javax.swing.JFrame {

    /**
     * Creates new form PlannerVerificationGui
     */
    public PlannerVerificationGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TextFieldWeek = new javax.swing.JTextField();
        TextFieldActivity = new javax.swing.JTextField();
        WorkspaceNLabel = new javax.swing.JLabel();
        InterventionLabel = new javax.swing.JLabel();
        SkillsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        WorkspaceNotesArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        SkillsNeededTextArea = new javax.swing.JTextArea();
        ForwardButton = new javax.swing.JButton();
        InterventionTextField = new javax.swing.JTextField();
        SaveButton = new javax.swing.JButton();
        LabelSMP = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        LabelWeek = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 204, 51));
        setMinimumSize(new java.awt.Dimension(800, 400));
        getContentPane().setLayout(null);

        TextFieldWeek.setEditable(false);
        TextFieldWeek.setBackground(new java.awt.Color(102, 102, 102));
        TextFieldWeek.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(TextFieldWeek);
        TextFieldWeek.setBounds(205, 31, 58, 35);

        TextFieldActivity.setEditable(false);
        TextFieldActivity.setBackground(new java.awt.Color(102, 102, 102));
        TextFieldActivity.setForeground(new java.awt.Color(255, 255, 255));
        TextFieldActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldActivityActionPerformed(evt);
            }
        });
        getContentPane().add(TextFieldActivity);
        TextFieldActivity.setBounds(452, 30, 323, 37);

        WorkspaceNLabel.setBackground(new java.awt.Color(255, 204, 102));
        WorkspaceNLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        WorkspaceNLabel.setForeground(new java.awt.Color(51, 51, 51));
        WorkspaceNLabel.setText("  Workspace Notes");
        WorkspaceNLabel.setOpaque(true);
        getContentPane().add(WorkspaceNLabel);
        WorkspaceNLabel.setBounds(0, 97, 223, 44);

        InterventionLabel.setBackground(new java.awt.Color(255, 204, 102));
        InterventionLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        InterventionLabel.setText("  Intervention description");
        InterventionLabel.setOpaque(true);
        getContentPane().add(InterventionLabel);
        InterventionLabel.setBounds(233, 97, 212, 44);

        SkillsLabel.setBackground(new java.awt.Color(255, 204, 102));
        SkillsLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        SkillsLabel.setText("        Skills needed");
        SkillsLabel.setOpaque(true);
        getContentPane().add(SkillsLabel);
        SkillsLabel.setBounds(459, 97, 313, 44);

        WorkspaceNotesArea.setColumns(20);
        WorkspaceNotesArea.setRows(5);
        jScrollPane1.setViewportView(WorkspaceNotesArea);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 153, 213, 166);

        SkillsNeededTextArea.setEditable(false);
        SkillsNeededTextArea.setColumns(20);
        SkillsNeededTextArea.setRows(5);
        jScrollPane2.setViewportView(SkillsNeededTextArea);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(459, 147, 313, 113);

        ForwardButton.setBackground(new java.awt.Color(102, 102, 102));
        ForwardButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ForwardButton.setForeground(new java.awt.Color(255, 255, 255));
        ForwardButton.setText("FORWARD");
        ForwardButton.setBorderPainted(false);
        ForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ForwardButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ForwardButton);
        ForwardButton.setBounds(515, 270, 216, 45);

        InterventionTextField.setEditable(false);
        getContentPane().add(InterventionTextField);
        InterventionTextField.setBounds(229, 153, 216, 99);

        SaveButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\ivang\\Pictures\\icona.png")); // NOI18N
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });
        getContentPane().add(SaveButton);
        SaveButton.setBounds(235, 258, 113, 103);

        LabelSMP.setBackground(new java.awt.Color(255, 255, 255));
        LabelSMP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        LabelSMP.setText("<html>Standard \nMaintenance \nProcedure File (SMP)  ");
        LabelSMP.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        LabelSMP.setOpaque(true);
        getContentPane().add(LabelSMP);
        LabelSMP.setBounds(352, 258, 93, 100);
        LabelSMP.getAccessibleContext().setAccessibleName("Standard \nMaintenance \nProcedure File\n(SMP)");

        jPanel3.setBackground(new java.awt.Color(255, 153, 0));
        jPanel3.setLayout(null);

        LabelWeek.setBackground(new java.awt.Color(255, 255, 255));
        LabelWeek.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        LabelWeek.setText("     Week n°");
        LabelWeek.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        LabelWeek.setOpaque(true);
        jPanel3.add(LabelWeek);
        LabelWeek.setBounds(30, 30, 148, 39);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Activity to assign"); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setOpaque(true);
        jPanel3.add(jLabel1);
        jLabel1.setBounds(300, 30, 140, 35);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 780, 370);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ForwardButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ForwardButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
       /** try{
            
        }catch{
            
        }*/
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void TextFieldActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldActivityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldActivityActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(PlannerVerificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlannerVerificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlannerVerificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlannerVerificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlannerVerificationGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ForwardButton;
    private javax.swing.JLabel InterventionLabel;
    private javax.swing.JTextField InterventionTextField;
    private javax.swing.JLabel LabelSMP;
    private javax.swing.JLabel LabelWeek;
    private javax.swing.JButton SaveButton;
    private javax.swing.JLabel SkillsLabel;
    private javax.swing.JTextArea SkillsNeededTextArea;
    private javax.swing.JTextField TextFieldActivity;
    private javax.swing.JTextField TextFieldWeek;
    private javax.swing.JLabel WorkspaceNLabel;
    private javax.swing.JTextArea WorkspaceNotesArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}