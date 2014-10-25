/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package POS;

import Helper.Connector;
import Helper.NFCMethods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Benjamin
 */
public class SelectStoreUI extends javax.swing.JFrame {

    /**
     * Creates new form SelectStoreUI
     */
    private LoginUI loginUI = null;
    private String staffJSON = null;
    private String cardId = null;
    private Double totalRegisterCash;
    private CardTerminal acr122uCardTerminal = null;
    private Boolean isChecking = false;
    private String currencyCode;
    private String storeType;
    /**
     * Creates new form SelectStore
     */
    public SelectStoreUI() {
        initComponents();
    }
    
    public SelectStoreUI(String staffJSON, Double totalRegisterCash, String storeType) throws IOException, ParseException
    {
        this();
        
        this.staffJSON = staffJSON;
        this.totalRegisterCash = totalRegisterCash;
        this.storeType = storeType;
        if (storeType.equals("furniture")){
            retailStoreButton.setVisible(Boolean.FALSE);
            restaurantButton.setVisible(Boolean.FALSE);
        }else if (storeType.equals("retail")){
            furnitureStoreButton.setVisible(Boolean.FALSE);
            restaurantButton.setVisible(Boolean.FALSE);
        }else if (storeType.equals("restaurant")){
            furnitureStoreButton.setVisible(Boolean.FALSE);
            retailStoreButton.setVisible(Boolean.FALSE);
        }
        confirmButton.setEnabled(Boolean.FALSE);
        newBalanceField.setEnabled(Boolean.FALSE);
        logoutButton.setEnabled(Boolean.FALSE);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(staffJSON);
        String name = (String) jsonObject.get("name");
        String plant = (String) jsonObject.get("plant");
        currencyCode = (String) jsonObject.get("symbol");
        cashBalanceLabel.setText("Cash Balance : " +currencyCode +" "+ totalRegisterCash);
        cardId = (String) jsonObject.get("cardId");
        welcomeLabel.setText("Welcome " + name + " of " + plant + " store!");
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
        furnitureStoreButton = new javax.swing.JButton();
        retailStoreButton = new javax.swing.JButton();
        restaurantButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        welcomeLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        supervisorField = new javax.swing.JLabel();
        cashBalanceLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        newBalanceField = new javax.swing.JTextField();
        confirmButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        verifyButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 720));

        furnitureStoreButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        furnitureStoreButton.setText("Furniture Store");
        furnitureStoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                furnitureStoreButtonActionPerformed(evt);
            }
        });

        retailStoreButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        retailStoreButton.setText("Retail Store");
        retailStoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retailStoreButtonActionPerformed(evt);
            }
        });

        restaurantButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        restaurantButton.setText("Restaurant");
        restaurantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restaurantButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(44, 62, 50));
        jLabel1.setText("Select Type of Store:");

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setText("Welcome");

        logoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("Reconcile Total");

        supervisorField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        supervisorField.setText("Supervisor : ");

        cashBalanceLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cashBalanceLabel.setText("Cash Balance :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel5.setText("New Balance : ");

        newBalanceField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        newBalanceField.setMinimumSize(new java.awt.Dimension(200, 50));
        newBalanceField.setPreferredSize(new java.awt.Dimension(200, 50));

        confirmButton.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        verifyButton.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        verifyButton.setText("Verify");
        verifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(welcomeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(logoutButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(supervisorField)
                                        .addGap(18, 18, 18)
                                        .addComponent(verifyButton))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(newBalanceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(confirmButton))
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(furnitureStoreButton)
                                .addGap(18, 18, 18)
                                .addComponent(retailStoreButton)
                                .addGap(18, 18, 18)
                                .addComponent(restaurantButton))
                            .addComponent(jLabel1)
                            .addComponent(cashBalanceLabel))
                        .addGap(0, 15, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoutButton)
                    .addComponent(welcomeLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(furnitureStoreButton)
                    .addComponent(retailStoreButton)
                    .addComponent(restaurantButton))
                .addGap(31, 31, 31)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supervisorField)
                    .addComponent(verifyButton))
                .addGap(18, 18, 18)
                .addComponent(cashBalanceLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(newBalanceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmButton))
                .addContainerGap(246, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void furnitureStoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_furnitureStoreButtonActionPerformed
        storeType = "furniture";
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("storetype");
        values.add("furniture");
        params.add("cardId");
        values.add(cardId);
        try {
            String furniturelist = Connector.postForm(params, values, "stock/furniturelist");
            System.err.println(furniturelist);
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, furniturelist, totalRegisterCash, storeType);
            scanItem.setVisible(true);
            this.setVisible(false);
        }catch (Exception ex) {
            Logger.getLogger(SelectStoreUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_furnitureStoreButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        LoginUI loginUI = new LoginUI(totalRegisterCash, storeType);
        loginUI.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void retailStoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retailStoreButtonActionPerformed
        storeType = "retail";
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("storetype");
        values.add("retail");
        params.add("cardId");
        values.add(cardId);
        try {
            String retaillist = Connector.postForm(params, values, "stock/retaillist");
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, retaillist, totalRegisterCash, storeType);
            scanItem.setVisible(true);
            this.setVisible(false);
        }catch (Exception ex) {
            Logger.getLogger(SelectStoreUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_retailStoreButtonActionPerformed

    private void restaurantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restaurantButtonActionPerformed
        storeType = "restaurant";
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("storetype");
        values.add("restaurant");
        params.add("cardId");
        values.add(cardId);
        try {
            String restaurantlist = Connector.postForm(params, values, "stock/restaurantlist");
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, restaurantlist, totalRegisterCash, storeType);
            scanItem.setVisible(true);
            this.setVisible(false);
        }catch (Exception ex) {
            Logger.getLogger(SelectStoreUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_restaurantButtonActionPerformed

    private void verifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyButtonActionPerformed
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
                            NFCMethods nfc = new NFCMethods();
                            String id = (nfc.getID(acr122uCardTerminal)).substring(0, 8);                           
                            List params = new ArrayList();
                            List values = new ArrayList();
                            params.add("cardId");
                            values.add(id);
                            String result = "";
                            try {
                                result = Connector.postForm(params, values, "auth/supervisornfc");
                            }catch (Exception ex) {
                                Logger.getLogger(SelectStoreUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(result.equals("OK")){
                                verifyButton.setVisible(Boolean.FALSE);
                                supervisorField.setText("Supervisor : OK");
                                confirmButton.setEnabled(Boolean.TRUE);
                                newBalanceField.setEnabled(Boolean.TRUE);
                                logoutButton.setEnabled(Boolean.TRUE);
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
    }//GEN-LAST:event_verifyButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        totalRegisterCash = Double.parseDouble(newBalanceField.getText());
        cashBalanceLabel.setText("Cash Balance : " +currencyCode + " "+ totalRegisterCash);
    }//GEN-LAST:event_confirmButtonActionPerformed

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
            java.util.logging.Logger.getLogger(SelectStoreUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelectStoreUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelectStoreUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelectStoreUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SelectStoreUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cashBalanceLabel;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton furnitureStoreButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField newBalanceField;
    private javax.swing.JButton restaurantButton;
    private javax.swing.JButton retailStoreButton;
    private javax.swing.JLabel supervisorField;
    private javax.swing.JButton verifyButton;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
