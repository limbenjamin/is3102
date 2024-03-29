package POS;

import Helper.Connector;
import Helper.LCD;
import Helper.NFCMethods;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.json.simple.parser.ParseException;

public class LoginUI extends javax.swing.JFrame {

    private CardTerminal acr122uCardTerminal = null;
    private Boolean isChecking = false;
    private List<Timer> timerList = new ArrayList();
    private Double totalRegisterCash;
    private String storeType;
    
    private OutputStream partnerPoleDisplayOutputStream;
    private String partnerPoleDisplayCOMPort;
    SerialPort serialPort;
    byte[] clear = {0x0C};
    byte[] newLine = {0x0A};
    byte[] carriageReturn = {0x0D};
            
    public LoginUI(Double totalRegisterCash, String storeType) {
        initComponents();
        try{
            partnerPoleDisplayCOMPort = LCD.getPort();
            initPartnerPoleDisplay();
        }catch(Exception e){
            System.err.println("Unable to init Partner Pole Display");
        }
        try{
            partnerPoleDisplayOutputStream.write(clear);
            partnerPoleDisplayOutputStream.write("Welcome to".getBytes());
            partnerPoleDisplayOutputStream.write(newLine);
            partnerPoleDisplayOutputStream.write(carriageReturn);
            partnerPoleDisplayOutputStream.write("Island Furniture".getBytes());
        }catch(Exception ex){
            System.err.println("Unable to write to Partner Pole Display");
            //ex.printStackTrace();
        }
        try{
            if(serialPort != null){
                closePartnerPoleDisplay();
            }
        }catch(Exception e){
            System.err.println("Unable to close Partner Pole Display");
        }
        this.totalRegisterCash = totalRegisterCash;
        this.storeType = storeType;
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
                    ActionListener actionListenerCheckCardPresent = new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            try {
                                if (acr122uCardTerminal.isCardPresent()){
                                    if (isChecking == false){
                                        timerList.get(0).stop();
                                        isChecking = true;
                                        NFCMethods nfc = new NFCMethods();
                                        String cardId = nfc.getID(acr122uCardTerminal);
                                        List params = new ArrayList();
                                        List values = new ArrayList();
                                        params.add("cardId");
                                        values.add(cardId.substring(0, 8));
                                        String result = Connector.postForm(params, values, "auth/nfc");
                                        if (result.equals("Error")) {
                                            JOptionPane.showMessageDialog(new JFrame(), "Error. Unable to authenticate", "Error", JOptionPane.ERROR_MESSAGE);
                                            isChecking = false;
                                            timerList.get(0).start();
                                        } else {
                                            System.err.println(result);
                                            SelectScreen(result);
                                        }
                                        
                                    }
                                }
                            } catch (CardException ex) {
                                Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    Timer timerCheckCardPresent = new Timer(1000, actionListenerCheckCardPresent);
                    timerCheckCardPresent.setRepeats(true);
                    timerCheckCardPresent.start();
                    timerList.add(timerCheckCardPresent);
                } else {
                }
            } else {
            }
        } catch (Exception ex) {
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
        jButtonExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jUsernameFieldUsername = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPasswordFieldPassword = new javax.swing.JPasswordField();
        jButtonLogin = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Unified Point of Sale Login");
        setPreferredSize(new java.awt.Dimension(1366, 720));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 720));

        jButtonExit.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButtonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/power-off.png"))); // NOI18N
        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabel1.setDisplayedMnemonic('2');
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(44, 62, 50));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/islandfurniture.png"))); // NOI18N
        jLabel1.setText("Island Furniture");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("Username:");

        jUsernameFieldUsername.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jUsernameFieldUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUsernameFieldUsernameActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setText("Password:");

        jPasswordFieldPassword.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jPasswordFieldPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldPasswordActionPerformed(evt);
            }
        });

        jButtonLogin.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButtonLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sign-in.png"))); // NOI18N
        jButtonLogin.setText("Login");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jButtonClear.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButtonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/trash.png"))); // NOI18N
        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExit))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 753, Short.MAX_VALUE)
                                .addComponent(jButtonClear)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonLogin))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPasswordFieldPassword)
                                    .addComponent(jUsernameFieldUsername))))
                        .addGap(211, 211, 211)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExit)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jUsernameFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogin)
                    .addComponent(jButtonClear))
                .addContainerGap(404, Short.MAX_VALUE))
        );

        jUsernameFieldUsername.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1342, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Point of Sale Login");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed

        List params = new ArrayList();
        List values = new ArrayList();
        params.add("username");
        values.add(jUsernameFieldUsername.getText());
        params.add("password");
        values.add(jPasswordFieldPassword.getText());
        try {
            String result = Connector.postForm(params, values, "auth/username");
            System.err.println(result);
            if (result.equals("Error")) {
                JOptionPane.showMessageDialog(new JFrame(), "Error. Unable to authenticate", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (!timerList.isEmpty()){
                    timerList.get(0).stop();
                }
                SelectScreen(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        jUsernameFieldUsername.setText("");
        jPasswordFieldPassword.setText("");
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jPasswordFieldPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldPasswordActionPerformed

    private void jUsernameFieldUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsernameFieldUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jUsernameFieldUsernameActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        URL url = this.getClass().getResource("/resources/config.xml");
        try {
            File f = new File(url.toURI());
            FileOutputStream output = new FileOutputStream(f, false);
            String s = "<root>\n<balance>" + totalRegisterCash + "</balance>\n<plant>" + storeType + "</plant>\n<root>";
            output.write(s.getBytes());
            output.close();
        } catch (URISyntaxException ex) {
            Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.exit(0);
    }//GEN-LAST:event_jButtonExitActionPerformed

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
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/resources/config.xml")));
                String line;
                Double cash = 0.0;
                String storeType = "None";
                try{
                    while ((line = br.readLine()) != null) 
                    {
                      System.err.println(line);
                      if (line.contains("<balance>"))
                          cash = Double.parseDouble(line.substring(9, line.length()-10));
                      if (line.contains("<plant>"))
                          storeType = line.substring(7, line.length()-8);
                    }
                    br.close();
                }catch(Exception e){
                    System.err.println(e);
                }
                LoginUI loginUI = new LoginUI(cash, storeType);
                loginUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                loginUI.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordFieldPassword;
    private javax.swing.JTextField jUsernameFieldUsername;
    // End of variables declaration//GEN-END:variables

    private void SelectScreen(String result) throws IOException, ParseException{
        SelectStoreUI store = new SelectStoreUI(result, totalRegisterCash, storeType);
        store.setExtendedState(JFrame.MAXIMIZED_BOTH);
        store.setVisible(true);
        this.setVisible(false);
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
    
}
