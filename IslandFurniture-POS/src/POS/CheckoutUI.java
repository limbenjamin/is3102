/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import Helper.Connector;
import Helper.LCD;
import Helper.NFCMethods;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Benjamin
 */
public class CheckoutUI extends javax.swing.JFrame {

    private String staffJSON;
    private String listJSON;
    private List<List<String>> transaction;
    private String cardId;
    private String customerCardId = "";
    private CardTerminal acr122uCardTerminal = null;
    private Boolean isChecking = false;
    private String customerName;
    private Double grandTotal;
    private Double totalRegisterCash;
    private String currencyCode;
    private String storeType;
    
    private OutputStream partnerPoleDisplayOutputStream;
    SerialPort serialPort;
    private String partnerPoleDisplayCOMPort;
    byte[] clear = {0x0C};
    byte[] newLine = {0x0A};
    byte[] carriageReturn = {0x0D};
    
    /**
     * Creates new form CheckoutUI
     */
    public CheckoutUI() {
        initComponents();
    }

    public CheckoutUI(String staffJSON, String listJSON, List<List<String>> transaction, Double totalRegisterCash, String storeType) throws ParseException {
        this();
        this.staffJSON = staffJSON;
        this.listJSON = listJSON;
        this.transaction = transaction;
        this.totalRegisterCash = totalRegisterCash;
        this.storeType = storeType;
        if (storeType.equals("restaurant") || storeType.equals("retail")){
            couponField.setEnabled(Boolean.FALSE);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(staffJSON);
        String name = (String) jsonObject.get("name");
        String plant = (String) jsonObject.get("plant");
        currencyCode = (String) jsonObject.get("symbol");
        grandTotalLabel.setText("Grand Total: " +currencyCode+" 0");
        cardId = (String) jsonObject.get("cardId");
        System.err.println(listJSON);
        welcomeLabel.setText("Welcome " + name + " of " + plant + " store!");
        jTable.setRowHeight(50);
        payButton.setVisible(Boolean.FALSE);
        reconcileButton.setEnabled(Boolean.FALSE);
        for (int i=0;i<transaction.size();i++){
            ((DefaultTableModel) jTable.getModel()).addRow(new Vector());
            jTable.getModel().setValueAt(transaction.get(i).get(0), i, 0);
            jTable.getModel().setValueAt(transaction.get(i).get(1), i, 1);
            jTable.getModel().setValueAt(transaction.get(i).get(2), i, 2);
            jTable.getModel().setValueAt(transaction.get(i).get(3), i, 4);
            jTable.getModel().setValueAt(transaction.get(i).get(4), i, 5);
        }
        partnerPoleDisplayCOMPort = LCD.getPort();
        initPartnerPoleDisplay();
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
        reconcileButton = new javax.swing.JButton();
        welcomeLabel = new javax.swing.JLabel();
        memberLabel = new javax.swing.JLabel();
        readCardButton = new javax.swing.JButton();
        couponField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        backButton = new javax.swing.JButton();
        payButton = new javax.swing.JButton();
        grandTotalLabel = new javax.swing.JLabel();
        calculateButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 720));

        reconcileButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        reconcileButton.setText("Reconcile");
        reconcileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reconcileButtonActionPerformed(evt);
            }
        });

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setText("welcome xxxxxxxxxxxxxxxxxxxx of xxxxxxxxxxx store");

        memberLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        memberLabel.setText("Member:");

        readCardButton.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        readCardButton.setText("Read Card");
        readCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readCardButtonActionPerformed(evt);
            }
        });

        couponField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        couponField.setMinimumSize(new java.awt.Dimension(200, 50));
        couponField.setPreferredSize(new java.awt.Dimension(200, 50));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Coupon :");

        jTable.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "ItemPrice", "Coupon Code", "Item Qty", "Total Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setResizable(false);
            jTable.getColumnModel().getColumn(1).setResizable(false);
            jTable.getColumnModel().getColumn(2).setResizable(false);
            jTable.getColumnModel().getColumn(3).setResizable(false);
            jTable.getColumnModel().getColumn(4).setResizable(false);
            jTable.getColumnModel().getColumn(5).setResizable(false);
        }

        backButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        payButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        payButton.setText("Pay");
        payButton.setPreferredSize(new java.awt.Dimension(200, 53));
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        grandTotalLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        grandTotalLabel.setText("Grand Total : 0");

        calculateButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        calculateButton.setText("Calculate");
        calculateButton.setPreferredSize(new java.awt.Dimension(200, 53));
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(welcomeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(backButton)
                                .addGap(18, 18, 18)
                                .addComponent(reconcileButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(memberLabel)
                                .addGap(18, 18, 18)
                                .addComponent(readCardButton)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(couponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(grandTotalLabel)
                .addGap(18, 18, 18)
                .addComponent(calculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reconcileButton)
                    .addComponent(welcomeLabel)
                    .addComponent(backButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memberLabel)
                    .addComponent(readCardButton)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(couponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grandTotalLabel)
                    .addComponent(calculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void reconcileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reconcileButtonActionPerformed
        if(serialPort != null){
            closePartnerPoleDisplay();
        }
        try {
            SelectStoreUI store = new SelectStoreUI(staffJSON, totalRegisterCash, storeType);
            store.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_reconcileButtonActionPerformed

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
                                customerCardId = (nfc.getID(acr122uCardTerminal)).substring(0, 8);
                                readCardButton.setVisible(Boolean.FALSE);
                                memberLabel.setText("Member: " + customerCardId);
                                try{
                                    partnerPoleDisplayOutputStream.write(clear);
                                    partnerPoleDisplayOutputStream.write(new String("Member Id:").getBytes());
                                    partnerPoleDisplayOutputStream.write(newLine);
                                    partnerPoleDisplayOutputStream.write(carriageReturn);
                                    String s = customerCardId;
                                    partnerPoleDisplayOutputStream.write(s.getBytes());
                                } catch (Exception ex) {
                                    System.err.println("Unable to write to Partner Pole Display");
                                }
                                
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

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if(serialPort != null){
            closePartnerPoleDisplay();
        }
        try {
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, listJSON, totalRegisterCash, storeType);
            scanItem.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        calculateButton.setVisible(Boolean.FALSE);
        couponField.setEnabled(Boolean.FALSE);
        readCardButton.setEnabled(Boolean.FALSE);
        int rows = jTable.getModel().getRowCount();
        List params = new ArrayList();
        List values = new ArrayList();
        if (!customerCardId.equals("")){
            params.add("cardId");
            values.add(cardId.substring(0, 8));
            params.add("customerCardId");
            values.add(customerCardId);
            try {
                    customerName = Connector.postForm(params, values, "stock/getcustomername");
                    System.err.println(customerName);
                } catch (Exception ex) {
                    Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if (storeType != "restaurant"){
            for (int i=0;i<rows;i++){
                params = new ArrayList();
                values = new ArrayList();
                params.add("cardId");
                values.add(cardId.substring(0, 8));
                params.add("customerCardId");
                values.add(customerCardId);
                params.add("coupon");
                values.add(couponField.getText());
                params.add("stock");
                values.add(transaction.get(i).get(0));
                params.add("stockCoupon");
                values.add(jTable.getModel().getValueAt(i, 3));

                try {
                    String result = Connector.postForm(params, values, "stock/checkpromo");
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                    String price = (String) jsonObject.get("price");
                    String promo = (String) jsonObject.get("promo");
                    transaction.get(i).add(5, promo);
                    transaction.get(i).add(6, String.valueOf(jTable.getModel().getValueAt(i, 3)));
                    transaction.get(i).set(2, price);
                    System.err.println(result);
                } catch (Exception ex) {
                    Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int i=0;i<transaction.size();i++){
            ((DefaultTableModel) jTable.getModel()).addRow(new Vector());
            jTable.getModel().setValueAt(transaction.get(i).get(0), i, 0);
            jTable.getModel().setValueAt(transaction.get(i).get(1), i, 1);
            jTable.getModel().setValueAt(transaction.get(i).get(2), i, 2);
            jTable.getModel().setValueAt(transaction.get(i).get(5), i, 3);
            jTable.getModel().setValueAt(transaction.get(i).get(3), i, 4);
            jTable.getModel().setValueAt(transaction.get(i).get(4), i, 5);
            }
        }else{
            for (int i=0;i<transaction.size();i++){
                transaction.get(i).add(5, "");
            }
        }
        updateTotal();
        payButton.setVisible(Boolean.TRUE);
        
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        PaymentUI payment = null;
        if(serialPort != null){
            closePartnerPoleDisplay();
        }
        try {
            payment = new PaymentUI(staffJSON, listJSON, transaction, customerName, customerCardId, grandTotal, totalRegisterCash, storeType);
        } catch (ParseException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        payment.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_payButtonActionPerformed

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
            java.util.logging.Logger.getLogger(CheckoutUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CheckoutUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CheckoutUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CheckoutUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CheckoutUI().setVisible(true);
            }
        });
    }
    
    public void updateTotal() {
        Double total = 0.0;
        Double current = 0.0;
        int rows = transaction.size();
        for (int i = 0; i < rows; i++) {
            Double price = Double.parseDouble(transaction.get(i).get(2));
            String qty = String.valueOf(transaction.get(i).get(3));
            total = (Double.parseDouble(qty)) * price;
            jTable.getModel().setValueAt(Math.round(total * 100.0) / 100.0, i, 5);
        }
        total = 0.0;
        for (int i = 0; i <= rows; i++) {
            try{
                String val = String.valueOf(jTable.getModel().getValueAt(i, 5));
                current = Double.parseDouble(val);
                total += current;
                current = 0.0;
            }catch(Exception e){
                
            }
        }
        grandTotalLabel.setText("Grand Total: " +currencyCode+" "+ Math.round(total * 100.0) / 100.0);
        grandTotal = Math.round(total * 100.0) / 100.0;
        try
        {
            partnerPoleDisplayOutputStream.write(clear);
            partnerPoleDisplayOutputStream.write(new String("Grand Total").getBytes());
            partnerPoleDisplayOutputStream.write(newLine);
            partnerPoleDisplayOutputStream.write(carriageReturn);
            String s = currencyCode+" "+ Math.round(total * 100.0) / 100.0;
            partnerPoleDisplayOutputStream.write(s.getBytes());
        }catch(Exception ex){
            System.err.println("Unable to write to Partner Pole Display");
        }
    }
    
    private void initPartnerPoleDisplay()
    {
        Enumeration commPortList = CommPortIdentifier.getPortIdentifiers();
        
        while (commPortList.hasMoreElements()) 
        {
            CommPortIdentifier commPort = (CommPortIdentifier) commPortList.nextElement();
            
            if (commPort.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    commPort.getName().equals(partnerPoleDisplayCOMPort))
            {
                try
                {
                    serialPort = (SerialPort) commPort.open("UnifiedPointOfSale", 5000);
                    partnerPoleDisplayOutputStream = serialPort.getOutputStream();
                }
                catch(PortInUseException ex)
                {
                    JOptionPane.showMessageDialog(null, "Unable to initialize Partner Pole Display: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Unable to initialize Partner Pole Display: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void closePartnerPoleDisplay(){
        if(serialPort != null)
        {
            try
            {
                byte[] clear = {0x0C};
                partnerPoleDisplayOutputStream.write(clear);
                partnerPoleDisplayOutputStream.close();
                serialPort.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton calculateButton;
    private javax.swing.JTextField couponField;
    private javax.swing.JLabel grandTotalLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel memberLabel;
    private javax.swing.JButton payButton;
    private javax.swing.JButton readCardButton;
    private javax.swing.JButton reconcileButton;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
