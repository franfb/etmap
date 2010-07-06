/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CreateCompDataInternalFrame.java
 *
 * Created on 01-may-2010, 21:47:26
 */
package etmap.gui;

import etmap.modis.AllLstData;
import javax.swing.JFileChooser;

/**
 *
 * @author Fran
 */
public class CreateCompDataInternalFrame extends javax.swing.JInternalFrame {

    /** Creates new form CreateCompDataInternalFrame */
    public CreateCompDataInternalFrame() {
        initComponents();
        hdfDirTextField.setText("D:\\etsii\\pfc\\datos\\Aqua11A1");
        terraDirTextField.setText("D:\\etsii\\pfc\\datos\\Terra11A1");
        stationsFileTextField.setText("D:\\etsii\\pfc\\datos\\estacionesagrocabildo.txt");
        yearSpinner.setValue((Integer) 2009);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        hdfDirTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        stationsFileTextField = new javax.swing.JTextField();
        openHdfDirButton = new javax.swing.JButton();
        openStationDirButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        yearSpinner = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        outDirTextField = new javax.swing.JTextField();
        chooseOutDirButton = new javax.swing.JButton();
        beginButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        terraDirTextField = new javax.swing.JTextField();
        openTerraDirButton = new javax.swing.JButton();
        writeMissingValues = new javax.swing.JCheckBox();
        writeMissingEt = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(etmap.gui.EtmapApp.class).getContext().getResourceMap(CreateCompDataInternalFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        hdfDirTextField.setText(resourceMap.getString("hdfDirTextField.text")); // NOI18N
        hdfDirTextField.setName("hdfDirTextField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        stationsFileTextField.setName("stationsFileTextField"); // NOI18N

        openHdfDirButton.setText(resourceMap.getString("openHdfDirButton.text")); // NOI18N
        openHdfDirButton.setName("openHdfDirButton"); // NOI18N
        openHdfDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openHdfDirButtonActionPerformed(evt);
            }
        });

        openStationDirButton.setText(resourceMap.getString("openStationDirButton.text")); // NOI18N
        openStationDirButton.setName("openStationDirButton"); // NOI18N
        openStationDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStationDirButtonActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        yearSpinner.setName("yearSpinner"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setEnabled(false);
        jLabel4.setName("jLabel4"); // NOI18N

        outDirTextField.setEnabled(false);
        outDirTextField.setName("outDirTextField"); // NOI18N

        chooseOutDirButton.setText(resourceMap.getString("chooseOutDirButton.text")); // NOI18N
        chooseOutDirButton.setEnabled(false);
        chooseOutDirButton.setName("chooseOutDirButton"); // NOI18N

        beginButton.setText(resourceMap.getString("beginButton.text")); // NOI18N
        beginButton.setName("beginButton"); // NOI18N
        beginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginButtonActionPerformed(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        terraDirTextField.setName("terraDirTextField"); // NOI18N

        openTerraDirButton.setText(resourceMap.getString("openTerraDirButton.text")); // NOI18N
        openTerraDirButton.setName("openTerraDirButton"); // NOI18N
        openTerraDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTerraDirButtonActionPerformed(evt);
            }
        });

        writeMissingValues.setText(resourceMap.getString("writeMissingValues.text")); // NOI18N
        writeMissingValues.setName("writeMissingValues"); // NOI18N

        writeMissingEt.setText(resourceMap.getString("writeMissingEt.text")); // NOI18N
        writeMissingEt.setName("writeMissingEt"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addComponent(hdfDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openHdfDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(33, 33, 33)
                        .addComponent(terraDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openTerraDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(writeMissingEt))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(stationsFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(openStationDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(outDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chooseOutDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(beginButton))
                    .addComponent(writeMissingValues))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(hdfDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openHdfDirButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(openTerraDirButton)
                    .addComponent(terraDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(stationsFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openStationDirButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chooseOutDirButton)
                    .addComponent(outDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(writeMissingValues)
                    .addComponent(writeMissingEt))
                .addGap(16, 16, 16)
                .addComponent(beginButton)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openHdfDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openHdfDirButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            hdfDirTextField.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
        }
    }//GEN-LAST:event_openHdfDirButtonActionPerformed

    private void beginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginButtonActionPerformed
        if ((!hdfDirTextField.getText().equals("")) && (!stationsFileTextField.getText().equals("")) && ((Integer) yearSpinner.getValue() != 0)) {
            AllLstData lstData = new AllLstData();
//            lstData.writeComparisonFile(hdfDirTextField.getText(), terraDirTextField.getText(), title, title, resizable);
            lstData.writeComparisonFile(hdfDirTextField.getText(), terraDirTextField.getText(), stationsFileTextField.getText(), ((Integer) yearSpinner.getValue()).toString(), writeMissingValues.isSelected(), writeMissingEt.isSelected());
        } else {
            System.out.println("Debe especificar todos los parámetros requeridos.");
        }
    }//GEN-LAST:event_beginButtonActionPerformed

    private void openStationDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStationDirButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showOpenDialog(this.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            stationsFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
        }
    }//GEN-LAST:event_openStationDirButtonActionPerformed

    private void openTerraDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTerraDirButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            terraDirTextField.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
        }
    }//GEN-LAST:event_openTerraDirButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton beginButton;
    private javax.swing.JButton chooseOutDirButton;
    private javax.swing.JTextField hdfDirTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton openHdfDirButton;
    private javax.swing.JButton openStationDirButton;
    private javax.swing.JButton openTerraDirButton;
    private javax.swing.JTextField outDirTextField;
    private javax.swing.JTextField stationsFileTextField;
    private javax.swing.JTextField terraDirTextField;
    private javax.swing.JCheckBox writeMissingEt;
    private javax.swing.JCheckBox writeMissingValues;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
}