/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package POS;

import Helper.Connector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Benjamin
 */
public class PaymentUI extends javax.swing.JFrame {

    private String staffJSON;
    private String listJSON;
    private List<List<String>> transaction;
    private String customerName;
    private Double oriTotal = 0.0;
    private Double grandTotalAmt = 0.0;
    private Double voucherAmt = 0.0;
    private Double receiptAmt = 0.0;
    private Double cashAmt = 0.0;
    private Double changeAmt = 0.0;
    private Double payableAmt =0.0;
    private String cardId;
    
    /**
     * Creates new form PaymentUI
     */
    public PaymentUI() {
        initComponents();
    }

    PaymentUI(String staffJSON, String listJSON, List<List<String>> transaction, String customerName, Double grandTotal) throws ParseException {
        this();
        this.staffJSON = staffJSON;
        this.listJSON = listJSON;
        this.transaction = transaction;
        this.customerName = customerName;
        this.grandTotalAmt = grandTotal;
        this.oriTotal = grandTotal;
        grandTotalLabel.setText("Grand Total : "+ grandTotal);
        checkoutButton.setVisible(Boolean.FALSE);
        cashCreditField.setEditable(Boolean.FALSE);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(staffJSON);
        String name = (String) jsonObject.get("name");
        String plant = (String) jsonObject.get("plant");
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
        welcomeLabel = new javax.swing.JLabel();
        voucherLabel = new javax.swing.JLabel();
        returnReceiptLabel = new javax.swing.JLabel();
        voucherField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        verifyVoucherButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        cashButton = new javax.swing.JToggleButton();
        cashCreditField = new javax.swing.JTextField();
        creditCardButton = new javax.swing.JToggleButton();
        returnReceiptField = new javax.swing.JTextField();
        grandTotalLabel = new javax.swing.JLabel();
        changeDueLabel = new javax.swing.JLabel();
        payButton = new javax.swing.JButton();
        payableLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        voucherCredit = new javax.swing.JLabel();
        receiptCredit = new javax.swing.JLabel();
        doneButton = new javax.swing.JButton();
        checkoutButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1366, 720));
        setPreferredSize(new java.awt.Dimension(1366, 720));

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setText("welcome xxxxxxxxxxxxxxxxxxxx of xxxxxxxxxxx store");

        voucherLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherLabel.setText("Voucher:");

        returnReceiptLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        returnReceiptLabel.setText("Return Receipt:");

        voucherField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherField.setMinimumSize(new java.awt.Dimension(200, 50));
        voucherField.setPreferredSize(new java.awt.Dimension(400, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        verifyVoucherButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        verifyVoucherButton.setText("Verify");
        verifyVoucherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyVoucherButtonActionPerformed(evt);
            }
        });

        addButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        addButton.setText("Add");

        cashButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        cashButton.setText("Cash");
        cashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashButtonActionPerformed(evt);
            }
        });

        cashCreditField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cashCreditField.setMinimumSize(new java.awt.Dimension(400, 50));
        cashCreditField.setPreferredSize(new java.awt.Dimension(400, 50));

        creditCardButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        creditCardButton.setText("Credit Card");
        creditCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditCardButtonActionPerformed(evt);
            }
        });

        returnReceiptField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        returnReceiptField.setPreferredSize(new java.awt.Dimension(400, 50));

        grandTotalLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        grandTotalLabel.setText("Grand Total : 0");

        changeDueLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        changeDueLabel.setText("Change due : 0");

        payButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        payButton.setText("Pay");
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        payableLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        payableLabel.setText("Total Payable : 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeLabel)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(returnReceiptLabel)
                            .addComponent(voucherLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(returnReceiptField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(verifyVoucherButton)
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cashButton)
                        .addGap(18, 18, 18)
                        .addComponent(creditCardButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cashCreditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(payButton))
                    .addComponent(grandTotalLabel)
                    .addComponent(changeDueLabel)
                    .addComponent(payableLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(48, 48, 48)
                                    .addComponent(voucherLabel))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(verifyVoucherButton)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addButton)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(returnReceiptField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(returnReceiptLabel))))))
                .addGap(104, 104, 104)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashButton)
                    .addComponent(creditCardButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashCreditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payButton))
                .addGap(18, 18, 18)
                .addComponent(grandTotalLabel)
                .addGap(18, 18, 18)
                .addComponent(payableLabel)
                .addGap(18, 18, 18)
                .addComponent(changeDueLabel)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        logoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        voucherCredit.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherCredit.setText("Credit:0");

        receiptCredit.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        receiptCredit.setText("Credit:0");

        doneButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        doneButton.setText("Done");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        checkoutButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        checkoutButton.setText("Checkout");

        backButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 878, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkoutButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(18, 18, 18)
                        .addComponent(logoutButton))
                    .addComponent(doneButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(receiptCredit, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(voucherCredit, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutButton)
                    .addComponent(backButton))
                .addGap(51, 51, 51)
                .addComponent(voucherCredit)
                .addGap(18, 18, 18)
                .addComponent(receiptCredit)
                .addGap(18, 18, 18)
                .addComponent(doneButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(checkoutButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        try {
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, listJSON);
            scanItem.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        verifyVoucherButton.setVisible(Boolean.FALSE);
        addButton.setVisible(Boolean.FALSE);
        voucherField.setEditable(Boolean.FALSE);
        returnReceiptField.setEditable(Boolean.FALSE);
        cashCreditField.setEditable(Boolean.TRUE);
        checkoutButton.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_doneButtonActionPerformed

    private void creditCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditCardButtonActionPerformed
        cashButton.setSelected(Boolean.FALSE);
    }//GEN-LAST:event_creditCardButtonActionPerformed

    private void cashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashButtonActionPerformed
        creditCardButton.setSelected(Boolean.FALSE);
    }//GEN-LAST:event_cashButtonActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        cashAmt = Double.parseDouble(cashCreditField.getText());
        calculateTotal();
    }//GEN-LAST:event_payButtonActionPerformed

    private void verifyVoucherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyVoucherButtonActionPerformed
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("cardId");
        values.add(cardId.substring(0, 8));
        params.add("voucher");
        values.add(voucherField.getText());
        try {
            String value = Connector.postForm(params, values, "stock/checkvoucher");
            System.err.println(value);
            } catch (Exception ex) {
                Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_verifyVoucherButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PaymentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaymentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaymentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaymentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PaymentUI().setVisible(true);
            }
        });
    }
    
    public void calculateTotal(){
        changeAmt = 0.0;
        payableAmt = 0.0;
        grandTotalAmt = oriTotal - voucherAmt - receiptAmt;
        if (cashButton.isSelected() == true){           
            payableAmt = grandTotalAmt - cashAmt;
            if (payableAmt < 0){
                changeAmt = payableAmt * -1;
                payableAmt = 0.0;
            } 
        }else{
            changeAmt = 0.0;
            payableAmt = 0.0;
        }
        grandTotalLabel.setText("Grand Total: "+grandTotalAmt);
        changeDueLabel.setText("Change Due: "+changeAmt);
        payableLabel.setText("Total Payable: "+payableAmt);
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JToggleButton cashButton;
    private javax.swing.JTextField cashCreditField;
    private javax.swing.JLabel changeDueLabel;
    private javax.swing.JButton checkoutButton;
    private javax.swing.JToggleButton creditCardButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JLabel grandTotalLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton payButton;
    private javax.swing.JLabel payableLabel;
    private javax.swing.JLabel receiptCredit;
    private javax.swing.JTextField returnReceiptField;
    private javax.swing.JLabel returnReceiptLabel;
    private javax.swing.JButton verifyVoucherButton;
    private javax.swing.JLabel voucherCredit;
    private javax.swing.JTextField voucherField;
    private javax.swing.JLabel voucherLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
