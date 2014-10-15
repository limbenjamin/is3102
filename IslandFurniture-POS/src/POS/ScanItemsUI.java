/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import Helper.NFCMethods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
    private Boolean changing = false;
    private CardTerminal acr122uCardTerminal = null;
    private Boolean isChecking = false;

    public ScanItemsUI() {
        initComponents();
    }

    public ScanItemsUI(SelectStoreUI selectStoreUI, String staffJSON, String listJSON) throws IOException, ParseException {
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
        jTable.changeSelection(0, 0, false, false);
        jTable.editCellAt(0, 0);
        jTable.getEditorComponent().requestFocusInWindow();
        jTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (changing.equals(false)) {
                    changing = true;
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (row == jTable.getModel().getRowCount()-1){
                        ((DefaultTableModel) jTable.getModel()).addRow(new Vector());
                    }
                    String id = String.valueOf(jTable.getModel().getValueAt(row, column));
                    //edited item id
                    if (column == 0) {
                        JSONArray array = (JSONArray) JSONValue.parse(listJSON);
                        Iterator i = array.iterator();
                        while (i.hasNext()) {
                                JSONObject jsonObject = (JSONObject) i.next();
                                String currentId = (String) jsonObject.get("id");
                                //remove item
                                if (id.equals("")){
                                    jTable.getModel().setValueAt("", row, 1);
                                    jTable.getModel().setValueAt("", row, 2);
                                    jTable.getModel().setValueAt("", row, 3);
                                    jTable.getModel().setValueAt("", row, 4);
                                    break;
                                }
                                if (currentId.equals(id)) {
                                    jTable.getModel().setValueAt(jsonObject.get("name"), row, 1);
                                    jTable.getModel().setValueAt(jsonObject.get("price"), row, 2);
                                    jTable.getModel().setValueAt("1", row, 3);
                                    jTable.getModel().setValueAt(jsonObject.get("price"), row, 4);
                                    //TODO : print to 20x2 LCD
                                    System.out.println(jsonObject.get("name") + " x 1");
                                    System.out.println(jsonObject.get("price"));
                                }
                        }
                        Boolean res = consolidate(row);
                        updateTotal(row);
                        //no need sort if 1 row
                        if (res.equals(Boolean.FALSE) || row == 0){
                            jTable.changeSelection(row + 1, column, false, false);
                            jTable.editCellAt(row + 1, column);
                            jTable.getEditorComponent().requestFocusInWindow();
                        }else{
                        }
                    //edited qty    
                    } else if (column == 3) {
                        Double price = Double.parseDouble((String) jTable.getModel().getValueAt(row, 2));
                        String qty = String.valueOf(jTable.getModel().getValueAt(row, column));
                        Double total = (Integer.parseInt(qty)) * price;
                        jTable.getModel().setValueAt(Math.round(total), row, 4);
                        //TODO : print to 20x2 LCD
                        System.out.println(jTable.getModel().getValueAt(row, 1) + " x " + qty);
                        System.out.println(Math.round(total));
                        updateTotal(row);
                    }
                    changing = false;
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
        resetButton = new javax.swing.JButton();
        memberLabel = new javax.swing.JLabel();
        readCardButton = new javax.swing.JButton();
        idLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 720));

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setText("welcome xxxxxxxxxxxxxxxxxxxx of xxxxxxxxxxx store");

        jTable.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Unit Price", "Quantity", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, true, false
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
        totalLabel.setText("Grand total: 0");

        checkoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        checkoutButton.setText("Checkout");
        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkoutButtonActionPerformed(evt);
            }
        });

        resetButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        memberLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        memberLabel.setText("Member:");

        readCardButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        readCardButton.setText("Read Card");
        readCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readCardButtonActionPerformed(evt);
            }
        });

        idLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        idLabel.setText("                                    ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetButton)
                        .addGap(18, 18, 18)
                        .addComponent(logoutButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(memberLabel)
                        .addGap(18, 18, 18)
                        .addComponent(readCardButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalLabel)
                        .addGap(18, 18, 18)
                        .addComponent(checkoutButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(idLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 268, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(logoutButton)
                        .addComponent(resetButton))
                    .addComponent(welcomeLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkoutButton)
                            .addComponent(totalLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(memberLabel)
                            .addComponent(readCardButton))
                        .addGap(18, 18, 18)
                        .addComponent(idLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
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

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        int row = jTable.getRowCount();
        int col = jTable.getColumnCount();
        changing = true;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                jTable.setValueAt("", i, j);
            }
        }
        changing = false;
    }//GEN-LAST:event_resetButtonActionPerformed

    private void readCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readCardButtonActionPerformed
        try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            if (!terminalFactory.terminals().list().isEmpty()) {
                for (CardTerminal cardTerminal : terminalFactory.terminals().list()) {
                    if (cardTerminal.getName().contains("ACS ACR122")) {
                        acr122uCardTerminal = cardTerminal;
                        break;
                    }
                }
                if (acr122uCardTerminal != null) {
                    try {
                        if (acr122uCardTerminal.isCardPresent()) {
                            if (isChecking == false) {
                                isChecking = true;
                                NFCMethods nfc = new NFCMethods();
                                String cardId = nfc.getID(acr122uCardTerminal);
                                idLabel.setText(cardId);
                            }
                        }
                    } catch (CardException ex) {
                        Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            } else {
            }
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_readCardButtonActionPerformed

    private void checkoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutButtonActionPerformed

    }//GEN-LAST:event_checkoutButtonActionPerformed

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

    public void updateTotal(int rows) {
        Double total = 0.0;
        Double current = 0.0;
        for (int i = 0; i < rows; i++) {
            try{
                String val = String.valueOf(jTable.getModel().getValueAt(i, 4));
                current = Double.parseDouble(val);
                total += current;
                current = 0.0;
            }catch(Exception e){
                
            }
        }
        totalLabel.setText("Grand Total: " + Math.round(total * 100.0) / 100.0);
    }
    
    public Boolean consolidate(int numrows){
        //check if item already added, then add qty only
        Boolean duplicate = false;
        List<String> list = new ArrayList();
        for (int i = 0; i <= numrows; i++) {
            String val = String.valueOf(jTable.getModel().getValueAt(i, 0));
            if (list.contains(val)){
                try{
                    duplicate = true;
                    int pos = list.indexOf(val);
                    String qty = String.valueOf(jTable.getModel().getValueAt(i, 3));
                    String curr_qty = String.valueOf(jTable.getModel().getValueAt(pos, 3));
                    String total_qty = String.valueOf(Integer.parseInt(qty)+Integer.parseInt(curr_qty));
                    jTable.getModel().setValueAt(total_qty, pos, 3);
                    Double price = Double.parseDouble((String) jTable.getModel().getValueAt(pos, 2));
                    Double total = (Integer.parseInt(total_qty)) * price;
                    jTable.getModel().setValueAt(Math.round(total * 100.0) / 100.0, pos, 4);
                    jTable.getModel().setValueAt("", i, 0);
                    jTable.getModel().setValueAt("", i, 1);
                    jTable.getModel().setValueAt("", i, 2);
                    jTable.getModel().setValueAt("", i, 3);
                    jTable.getModel().setValueAt("", i, 4);
                    //TODO : print to 20x2 LCD
                    System.out.println(jTable.getModel().getValueAt(pos, 1) + " x " + total_qty);
                    System.out.println(Math.round(total));
                }catch(Exception e){
                
                }
            }else{
                list.add(val);
            }
        }
        return duplicate;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkoutButton;
    private javax.swing.JLabel idLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel memberLabel;
    private javax.swing.JButton readCardButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
