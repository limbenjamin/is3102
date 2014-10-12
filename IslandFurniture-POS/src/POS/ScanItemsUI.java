/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package POS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Benjamin
 */
public class ScanItemsUI extends javax.swing.JFrame {

    /**
     * Creates new form ScanItems
     */
    
    private String staffJSON = null;
    private String cardId = null;
    
    public ScanItemsUI() {
        initComponents();
    }
    
    public ScanItemsUI(SelectStoreUI selectStoreUI, String staffJSON, String listJSON) throws IOException, ParseException{
        this();
        this.staffJSON = staffJSON;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(staffJSON);
        String name = (String) jsonObject.get("name");
        String plant = (String) jsonObject.get("plant");
        cardId = (String) jsonObject.get("cardId");
        System.err.println(listJSON);
        welcomeLabel.setText("Welcome " + name + " of " + plant + " store!");
        jTable.setRowHeight(50);
        jTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                String id = String.valueOf(jTable.getModel().getValueAt(row, column));
                System.err.println(row+"   "+column);
                if (column == 0){
                   List<String> itemList = Arrays.asList(listJSON.substring(2, listJSON.length()-2).split("\\},\\{"));
                   for (int i=0;i<itemList.size();i++){
                       try {
                           String JSONobj = "{" + itemList.get(i) + "}";
                           JSONObject jsonObject = (JSONObject) jsonParser.parse(JSONobj);
                           String currentId = (String) jsonObject.get("id");
                           System.err.println(currentId);
                           if (currentId.equals(id)){
                               jTable.getModel().setValueAt(jsonObject.get("name"), row, 1);
                               jTable.getModel().setValueAt(1, row, 2);
                               jTable.getModel().setValueAt("10", row, 3);
                           }
                       } catch (ParseException ex) {
                           Logger.getLogger(ScanItemsUI.class.getName()).log(Level.SEVERE, null, ex);
                       }
                       
                   }
                   updateTotal();
                }
                else if (column == 2){
                    int price = 10;
                    String qty = String.valueOf(jTable.getModel().getValueAt(row, column));
                    int total = (Integer.valueOf(qty))*price;
                    System.err.println(total);
                    jTable.getModel().setValueAt(total, row, 3);
                    updateTotal();
                }
            }
          });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        logoutButton = new javax.swing.JButton();
        totalLabel = new javax.swing.JLabel();
        checkoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 720));

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setText("welcome");

        jTable.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setMaximumSize(new java.awt.Dimension(2147483647, 240));
        jScrollPane1.setViewportView(jTable);

        logoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        totalLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        totalLabel.setText("Grand total:");

        checkoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        checkoutButton.setText("Checkout");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logoutButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 506, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(totalLabel)
                                .addGap(18, 18, 18)
                                .addComponent(checkoutButton)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeLabel)
                    .addComponent(logoutButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(checkoutButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_logoutButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ScanItemsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScanItemsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScanItemsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScanItemsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScanItemsUI().setVisible(true);
            }
        });
    }
    
    public void updateTotal(){
        int rows = jTable.getModel().getRowCount();
        int total = 0;
        int current = 0;
        for (int i=0;i<rows;i++){
            try{
                String val = String.valueOf(jTable.getModel().getValueAt(i, 3));
                current = Integer.parseInt(val);
            }catch(Exception e){
                
            }
            total += current;
            current = 0;
        }
        totalLabel.setText("Grand Total: "+total);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkoutButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
