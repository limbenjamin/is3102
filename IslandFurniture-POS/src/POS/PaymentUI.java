/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package POS;

import Helper.Connector;
import Helper.LCD;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.json.simple.JSONArray;
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
    private List<String> voucherList = new ArrayList();
    private String staffname;
    private String plantname;
    private String plantAddress;
    private String customerCardId;
    private Double totalPayable;
    private Double totalRegisterCash;
    private String currencyCode;
    private String storeType;
    private String transactionId;
    private DecimalFormat df;
    private String result;
    
    private OutputStream partnerPoleDisplayOutputStream;
    SerialPort serialPort;
    private String partnerPoleDisplayCOMPort;
    byte[] clear = {0x0C};
    byte[] newLine = {0x0A};
    byte[] carriageReturn = {0x0D};
    
    /**
     * Creates new form PaymentUI
     */
    public PaymentUI() {
        initComponents();
    }

    PaymentUI(String staffJSON, String listJSON, List<List<String>> transaction, String customerName, String customerCardId, Double grandTotal, Double totalRegisterCash, String storeType) throws ParseException {
        this();
        this.staffJSON = staffJSON;
        this.listJSON = listJSON;
        this.transaction = transaction;
        this.customerName = customerName;
        this.grandTotalAmt = grandTotal;
        this.oriTotal = grandTotal;
        this.customerCardId = customerCardId;
        this.totalRegisterCash = totalRegisterCash;
        this.storeType = storeType;
        if (storeType.equals("restaurant") || storeType.equals("retail")){
            returnReceiptField.setEnabled(Boolean.FALSE);
            addButton.setEnabled(Boolean.FALSE);
        }
        df = new DecimalFormat("#0.00"); 
        finishButton.setEnabled(Boolean.FALSE);
        cashCreditField.setEnabled(Boolean.FALSE);
        cashButton.setSelected(Boolean.TRUE);
        cashButton.setEnabled(Boolean.FALSE);
        creditCardButton.setEnabled(Boolean.FALSE);
        payButton.setEnabled(Boolean.FALSE);
        reconcileButton.setEnabled(false);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(staffJSON);
        staffname = (String) jsonObject.get("name");
        plantname = (String) jsonObject.get("plant");
        plantAddress = (String) jsonObject.get("plantAddress");
        currencyCode = (String) jsonObject.get("symbol");
        grandTotalLabel.setText("Grand Total : "+currencyCode+" "+ grandTotal);
        voucherCredit.setText("Credit : "+currencyCode+" 0");
        receiptCredit.setText("Credit : "+currencyCode+" 0");
        changeDueLabel.setText("Change Due: "+currencyCode+" 0");
        payableLabel.setText("Total Payable: "+currencyCode+" 0");
        cardId = (String) jsonObject.get("cardId");
        try{
            partnerPoleDisplayCOMPort = LCD.getPort();
            initPartnerPoleDisplay();
        }catch(Exception e){
            System.err.println("Unable to init Partner Pole Display");
        } 
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
        cashButton = new javax.swing.JToggleButton();
        cashCreditField = new javax.swing.JTextField();
        creditCardButton = new javax.swing.JToggleButton();
        returnReceiptField = new javax.swing.JTextField();
        grandTotalLabel = new javax.swing.JLabel();
        changeDueLabel = new javax.swing.JLabel();
        payButton = new javax.swing.JButton();
        payableLabel = new javax.swing.JLabel();
        verifyVoucherButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        voucherCredit = new javax.swing.JLabel();
        receiptCredit = new javax.swing.JLabel();
        doneButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        reconcileButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        welcomeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/islandfurniture.png"))); // NOI18N
        welcomeLabel.setText("Island Furniture");

        voucherLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherLabel.setText("Voucher:");

        returnReceiptLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        returnReceiptLabel.setText("Return Receipt:");

        voucherField.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherField.setMinimumSize(new java.awt.Dimension(200, 50));
        voucherField.setPreferredSize(new java.awt.Dimension(400, 50));

        cashButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        cashButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/money.png"))); // NOI18N
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
        creditCardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/credit-card.png"))); // NOI18N
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
        payButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/dollar.png"))); // NOI18N
        payButton.setText("Pay");
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        payableLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        payableLabel.setText("Total Payable : 0");

        verifyVoucherButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        verifyVoucherButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/check.png"))); // NOI18N
        verifyVoucherButton.setText("Verify");
        verifyVoucherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyVoucherButtonActionPerformed(evt);
            }
        });

        addButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/plus.png"))); // NOI18N
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        voucherCredit.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        voucherCredit.setText("Credit : 0");

        receiptCredit.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        receiptCredit.setText("Credit : 0");

        doneButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        doneButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mail-forward.png"))); // NOI18N
        doneButton.setText("Done");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        backButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mail-reply.png"))); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        reconcileButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        reconcileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/money.png"))); // NOI18N
        reconcileButton.setText("Reconcile");
        reconcileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reconcileButtonActionPerformed(evt);
            }
        });

        finishButton.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        finishButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mail-forward.png"))); // NOI18N
        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backButton)
                        .addGap(18, 18, 18)
                        .addComponent(reconcileButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(payableLabel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(returnReceiptLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(returnReceiptField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(voucherLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(verifyVoucherButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(voucherCredit))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(receiptCredit))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(173, 173, 173)
                                        .addComponent(doneButton))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cashButton)
                                .addGap(18, 18, 18)
                                .addComponent(creditCardButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cashCreditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(payButton))
                            .addComponent(grandTotalLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(changeDueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(finishButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeLabel)
                    .addComponent(reconcileButton)
                    .addComponent(backButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(voucherLabel)
                    .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verifyVoucherButton)
                    .addComponent(voucherCredit))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnReceiptLabel)
                    .addComponent(returnReceiptField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton)
                    .addComponent(receiptCredit))
                .addGap(18, 18, 18)
                .addComponent(doneButton)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
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
                        .addContainerGap(42, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(finishButton)
                        .addContainerGap())))
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

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        try{
            if(serialPort != null){
                closePartnerPoleDisplay();
            }
        }catch(Exception e){
            System.err.println("Unable to close Partner Pole Display");
        }
        try {
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, listJSON, totalRegisterCash, storeType);
            scanItem.setExtendedState(JFrame.MAXIMIZED_BOTH);
            scanItem.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void reconcileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reconcileButtonActionPerformed
        try{
            if(serialPort != null){
                closePartnerPoleDisplay();
            }
        }catch(Exception e){
            System.err.println("Unable to close Partner Pole Display");
        }
        try {
            SelectStoreUI store = new SelectStoreUI(staffJSON, totalRegisterCash, storeType);
            store.setExtendedState(JFrame.MAXIMIZED_BOTH);
            store.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_reconcileButtonActionPerformed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        verifyVoucherButton.setEnabled(Boolean.FALSE);
        addButton.setEnabled(Boolean.FALSE);
        voucherField.setEnabled(Boolean.FALSE);
        returnReceiptField.setEnabled(Boolean.FALSE);
        doneButton.setEnabled(Boolean.FALSE);
        cashButton.setEnabled(Boolean.TRUE);
        creditCardButton.setEnabled(Boolean.TRUE);
        payButton.setEnabled(Boolean.TRUE);
        cashCreditField.setEnabled(Boolean.TRUE);
        calculateTotal();
    }//GEN-LAST:event_doneButtonActionPerformed

    private void creditCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditCardButtonActionPerformed
        cashButton.setSelected(Boolean.FALSE);
    }//GEN-LAST:event_creditCardButtonActionPerformed

    private void cashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashButtonActionPerformed
        creditCardButton.setSelected(Boolean.FALSE);
    }//GEN-LAST:event_cashButtonActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        if (cashButton.isSelected() == true){
            cashAmt = Double.parseDouble(cashCreditField.getText());
        }
        calculateTotal();
        if (payableAmt == 0.0){
            System.err.println("");
            System.err.println("");
            System.err.println(transaction.toString());
            System.err.println(staffname);
            System.err.println(plantname);
            System.err.println(voucherList.toString());
            System.err.println(returnReceiptField.getText());
            System.err.println(customerName);
            System.err.println("");
            System.err.println("");
            JSONArray transactionList = new JSONArray();
            for (int i=0;i<transaction.size();i++){
                Map obj=new LinkedHashMap();
                obj.put("id",transaction.get(i).get(0));
                obj.put("name", transaction.get(i).get(1));
                obj.put("price", transaction.get(i).get(2));
                obj.put("qty", transaction.get(i).get(3));
                System.err.println(transaction.get(i).size());
                if (transaction.get(i).size() == 6){
                    transaction.get(i).add(6, "null");
                }
                obj.put("disc", transaction.get(i).get(6));
                transactionList.add(obj);
            }
            List params = new ArrayList();
            List values = new ArrayList();
            params.add("cardId");
            values.add(cardId.substring(0, 8));
            params.add("transaction");
            values.add(transactionList);            
            params.add("voucher");
            values.add(voucherList.toString());     
            params.add("receipt");
            values.add(returnReceiptField.getText());             
            params.add("customerCardId");
            values.add(customerCardId);
            params.add("storeType");
            values.add(storeType);
            params.add("grandTotal");
            values.add(grandTotalAmt);
            params.add("voucherAmt");
            values.add(voucherAmt);
            params.add("receiptAmt");
            values.add(receiptAmt);
            try {
                result = Connector.postForm(params, values, "stock/maketransaction");   
            } catch (Exception ex) {
                Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (cashButton.isSelected() == true){
                totalRegisterCash += totalPayable;
            }
            finishButton.setEnabled(Boolean.TRUE);
            cashCreditField.setEnabled(Boolean.FALSE);
            payButton.setEnabled(Boolean.FALSE);
            backButton.setEnabled(false);           
            cashButton.setEnabled(Boolean.FALSE);
            creditCardButton.setEnabled(Boolean.FALSE);
            try{
                partnerPoleDisplayOutputStream.write(clear);
                partnerPoleDisplayOutputStream.write(new String("Total: " + totalPayable).getBytes());
                partnerPoleDisplayOutputStream.write(newLine);
                partnerPoleDisplayOutputStream.write(carriageReturn);
                partnerPoleDisplayOutputStream.write(new String("Change: "+currencyCode+" "+df.format(Math.round(changeAmt * 100.0) / 100.0)).getBytes());
            }catch(Exception ex){
                System.err.println("Unable to write to Partner Pole Display");
            }
            String receipt = "Island Furniture\n";
            receipt += plantname + " Store\n";
            receipt += plantAddress + "\n";
            receipt += new Date() + " \n";
            receipt += "\n\n";
            receipt += "ID   Product Name        Price\n";
            receipt += "------------------------------\n";//30 chars
            for (int i=0;i<transaction.size();i++){
                System.err.println(transaction.get(i).get(0));
                System.err.println(transaction.get(i).get(1));
                System.err.println(transaction.get(i).get(2));
                System.err.println(transaction.get(i).get(3));
                System.err.println(transaction.get(i).get(4));
                System.err.println(transaction.get(i).get(5));
                receipt += transaction.get(i).get(0)+"  ";
                receipt += transaction.get(i).get(1)+" ("+transaction.get(i).get(3)+"x)\n";
                Double totalUnroundedAmt = Double.parseDouble(transaction.get(i).get(2)) * Double.parseDouble(transaction.get(i).get(3));
                Double roundedamt = Math.round(totalUnroundedAmt* 100.0)/100.0;
                String transactionPrice = currencyCode + " " + df.format(roundedamt);
                String emptyString = "                                                                    ";
                if (transaction.get(i).get(5).equals("")){

                }else{
                    int k;
                    if(transaction.get(i).get(5).length()>30){
                        k = 30;
                    }else{
                        k = transaction.get(i).get(5).length();
                    }
                    int length = 30 - (k + transactionPrice.length());
                    receipt += transaction.get(i).get(5).substring(0, k)+ "\n";
                }
                receipt += emptyString.substring(0,30-transactionPrice.length()) + transactionPrice + "\n\n"; 
            }
            receipt += "------------------------------\n";
            receipt+= "Grand Total: " +currencyCode+" "+df.format(Math.round(grandTotalAmt * 100.0) / 100.0) + "\n";
            Double d = voucherAmt + receiptAmt;
            receipt+= "Discounts: " +currencyCode+" "+ df.format(Math.round(d * 100.0) / 100.0) + "\n";
            receipt+= "Amount Payable: " +currencyCode+" "+ df.format(Math.round(totalPayable * 100.0) / 100.0) + "\n\n";
            
            if (cashButton.isSelected() == true){
                receipt+= "Payment Mode: Cash\n";
                receipt+= "Cash Amount: " +currencyCode+" "+ df.format(Math.round(cashAmt * 100.0) / 100.0) + "\n";
                receipt+= "Change: " +currencyCode+" " + df.format(Math.round(changeAmt * 100.0) / 100.0) + "\n";
            }else{
                receipt+= "Payment Mode : Credit Card\n";
            }
            receipt+= "Cashier : "+ staffname +"\n\n";
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonParser.parse(result);
            } catch (ParseException ex) {
                Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            transactionId = String.valueOf( jsonObject.get("transactionId"));
            String points = String.valueOf( jsonObject.get("points"));
            if (customerName == null){
                receipt+= "Thank you for shopping with us!\n";
            }else{
                receipt+= customerName+", thank you for shopping with us!\n";
                receipt+= "Current Points: " + points + "\n";
            }
            receipt += "Transaction Id: "+transactionId + "\n";
            
            
            try
            {
                JTextPane printing = new JTextPane();
                printing.setSize(180, 300);
                printing.setText(receipt);
                Font font = new Font("Courier New",Font.PLAIN, 7);
                printing.setFont(font);
                Double margin = 20.0;
                Integer lines = 8;
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PageFormat pageFormat = printerJob.defaultPage();
                Paper paper = new Paper();
                paper.setSize(180.0, (double) (paper.getHeight() + lines * 10.0));
                paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
                pageFormat.setPaper(paper);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                ByteArrayOutputStream out = QRCode.from(String.valueOf(transactionId)).to(ImageType.PNG).stream();
                ImageIcon icon = new ImageIcon( out.toByteArray() );
                printing.insertIcon(icon);
                printing.insertIcon(icon);
                printerJob.setPrintable(printing.getPrintable(null, null), pageFormat);
                printerJob.print();
            }
            catch(PrinterException ex)
            {
                JOptionPane.showMessageDialog(null, "Unable to print to Partner Thermal Printer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_payButtonActionPerformed

    private void verifyVoucherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyVoucherButtonActionPerformed
        if (voucherField.getText().equals("")){
            return;
        }
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("cardId");
        values.add(cardId.substring(0, 8));
        params.add("voucher");
        values.add(voucherField.getText());
        try {
            String value = Connector.postForm(params, values, "stock/checkvoucher");
            System.err.println(value);
            if (value.equals("-1")){
                voucherField.setText("");
                JOptionPane.showMessageDialog(new JFrame(), "Unable to apply voucher", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                voucherAmt += Double.parseDouble(value);
                voucherCredit.setText("Credit : "+currencyCode+" "+voucherAmt);
                voucherList.add(voucherField.getText());
                voucherField.setText("");
                try
                {
                    partnerPoleDisplayOutputStream.write(clear);
                    partnerPoleDisplayOutputStream.write(new String("Voucher: " + voucherField.getText()).getBytes());
                    partnerPoleDisplayOutputStream.write(newLine);
                    partnerPoleDisplayOutputStream.write(carriageReturn);
                    partnerPoleDisplayOutputStream.write(new String("- "+currencyCode+" "+voucherAmt).getBytes());
                }catch(Exception ex){
                    System.err.println("Unable to write to Partner Pole Display");
                }
            }
            } catch (Exception ex) {
                Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_verifyVoucherButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (returnReceiptField.getText().equals("")){
            return;
        }
        List params = new ArrayList();
        List values = new ArrayList();
        params.add("cardId");
        values.add(cardId.substring(0, 8));
        params.add("receipt");
        values.add(returnReceiptField.getText());
        try {
            String value = Connector.postForm(params, values, "stock/checkreceipt");
            System.err.println(value);
            if (value.equals("-1")){
                returnReceiptField.setText("");
                JOptionPane.showMessageDialog(new JFrame(), "Unable to apply receipt", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                receiptAmt += Double.parseDouble(value);
                receiptCredit.setText("Credit : "+currencyCode+" "+receiptAmt);
                returnReceiptField.setEnabled(Boolean.FALSE);
                addButton.setEnabled(Boolean.FALSE);
                try
                {
                    partnerPoleDisplayOutputStream.write(clear);
                    partnerPoleDisplayOutputStream.write(new String("Receipt: " + returnReceiptField.getText()).getBytes());
                    partnerPoleDisplayOutputStream.write(newLine);
                    partnerPoleDisplayOutputStream.write(carriageReturn);
                    partnerPoleDisplayOutputStream.write(new String("- "+currencyCode+" "+receiptAmt).getBytes());
                }catch(Exception ex){
                    System.err.println("Unable to write to Partner Pole Display");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CheckoutUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        try{
            if(serialPort != null){
                closePartnerPoleDisplay();
            }
        }catch(Exception e){
            System.err.println("Unable to close Partner Pole Display");
        }
        try {
            ScanItemsUI scanItem = new ScanItemsUI(staffJSON, listJSON, totalRegisterCash, storeType);
            scanItem.setExtendedState(JFrame.MAXIMIZED_BOTH);
            scanItem.setVisible(true);
            this.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PaymentUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_finishButtonActionPerformed

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
        finishButton.setEnabled(Boolean.FALSE);
        changeAmt = 0.0;
        payableAmt = 0.0;
        grandTotalAmt = oriTotal; 
        payableAmt = grandTotalAmt - voucherAmt - receiptAmt;
        totalPayable = payableAmt;
        //voucher and reciept overshot total
        if (grandTotalAmt < voucherAmt+receiptAmt){
            changeAmt = 0.0;
            payableAmt = 0.0;
            totalPayable = 0.0;
        }
        else{
            if (cashButton.isSelected() == true){
                if (payableAmt - cashAmt < 0){
                    changeAmt = (payableAmt - cashAmt) * -1;
                    payableAmt = 0.0;
                }else{
                    payableAmt = grandTotalAmt - voucherAmt - receiptAmt - cashAmt;
                } 
            //credit card just charge all balance    
            }else{
                changeAmt = 0.0;
                payableAmt = 0.0;
            }
        }
        grandTotalLabel.setText("Grand Total: "+currencyCode+" "+grandTotalAmt);
        changeDueLabel.setText("Change Due: "+currencyCode+" "+Math.round(changeAmt * 100.0) / 100.0);
        payableLabel.setText("Total Payable: "+currencyCode+" "+Math.round(payableAmt * 100.0) / 100.0);
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
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JToggleButton cashButton;
    private javax.swing.JTextField cashCreditField;
    private javax.swing.JLabel changeDueLabel;
    private javax.swing.JToggleButton creditCardButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JButton finishButton;
    private javax.swing.JLabel grandTotalLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton payButton;
    private javax.swing.JLabel payableLabel;
    private javax.swing.JLabel receiptCredit;
    private javax.swing.JButton reconcileButton;
    private javax.swing.JTextField returnReceiptField;
    private javax.swing.JLabel returnReceiptLabel;
    private javax.swing.JButton verifyVoucherButton;
    private javax.swing.JLabel voucherCredit;
    private javax.swing.JTextField voucherField;
    private javax.swing.JLabel voucherLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
