import com.mongodb.*;
import java.awt.Color;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.text.Document;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yonshisoru
 */
public class Login extends javax.swing.JFrame {
MongoClient mongo;
DB db;
DBCollection DBC;
boolean showpwd = false;
boolean connected = false;
Variable v = new Variable();
DBObject dbo;
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        this.setLocationRelativeTo(null);
        getConnect();
        if(connected==false){
            JOptionPane.showMessageDialog(null,"การเชื่อมต่อเซิร์ฟเวอร์ล้มเหลว\nกำลังปิดโปรแกรม");
            System.exit(0);
        }
    }
public void getConnect(){
    try{
        MongoClient mongo = new MongoClient("localhost",27017);
        db = mongo.getDB("InthaninDB");
        System.out.println(db);
        connected = true;
        if(db==null){
            connected = false;
        }
        System.out.println(mongo.getConnectPoint());
    }catch(Exception e){
        JOptionPane.showMessageDialog(null,"การเชื่อมต่อเซิร์ฟเวอร์ล้มเหลว\nกำลังปิดโปรแกรม");
        System.exit(0);
    }
}
public void clear(){
       user_txt.setText("Username or Email");
       pwd_txt.setText("Password");
}
/*public void getInfo(int id){
    try{
        BasicDBObject search = new BasicDBObject();
        search.put("MS_EMPLOYEE_ID",id);
    }catch(Exception e){
        
    }
}*/
public boolean checklogin(String user,String pwd){
    boolean output =false;
    boolean checkid = false;
    boolean checkemail = false;
    int employeeid = 0;
    try{
        DBC = db.getCollection("MS_EMPLOYEE");
        BasicDBObject search = new BasicDBObject();
        search.put("MS_EMPLOYEE_USERNAME",user);
        search.put("MS_EMPLOYEE_PWD",pwd);
        DBCursor c = DBC.find(search);
        dbo = DBC.findOne(search);
        if(c.hasNext()){
            checkid = true;
        }else{
            checkid = false;
        }
        try{
        employeeid = (int)dbo.get("MS_EMPLOYEE_ID");
        }catch(Exception e){
            
        }
        if(checkid==false){
        BasicDBObject searchemail = new BasicDBObject();
        DBCursor cl = DBC.find(searchemail);
        dbo = DBC.findOne(searchemail);
        searchemail.put("MS_EMPLOYEE_EMAIL",dbo.get("MS_EMPLOYEE_EMAIL"));
        searchemail.put("MS_EMPLOYEE_PWD",pwd);
        if(cl.hasNext()){
           checkemail = true;
        }else{
           checkemail =false;
        }
        double m = (double) dbo.get("MS_EMPLOYEE_ID");
        employeeid = (int)m;
        }
        System.out.println("PK=>"+employeeid);
        System.out.println(dbo.get("MS_EMPLOYEE_TYPE"));
        if(checkemail==true||checkid==true){
            output = true;
        }else{
            output = false;
        }
        if(dbo.get("MS_EMPLOYEE_TYPE").equals("Employee")){
            v.setstatus(1);
        }else if(dbo.get("MS_EMPLOYEE_TYPE").equals("Owner")){
            v.setstatus(0);
        }
        DBCollection table = db.getCollection("TRAN_LOG");
        BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
        DBCursor cur = table.find().sort(sortObject);
        int id = 0;
        DBCursor find = table.find();
        System.out.println(find.hasNext());
        if(find.hasNext()==true){
            System.out.println("eiei");
            int n = (int)cur.one().get("TRAN_LOG_ID");
            id = n+1;
        }else{
            id = 1; 
        }
             DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
             String formattedDate = formatter.format(LocalDate.now());
             String month = v.month(Integer.parseInt(formattedDate.substring(4,6)));
             String year = formattedDate.substring(0,4);
             String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());
            BasicDBObject document = new BasicDBObject();
            //-----------------------------------------------
            document.put("TRAN_LOG_ID",id);
            document.put("TRAN_LOG_DATE",month+" "+date+", "+year);
            document.put("TRAN_LOG_TIME",LocalTime.now().toString().substring(0,8));
            document.put("TRAN_LOG_TYPE","Login");
            document.put("MS_EMPLOYEE_ID",employeeid);
            //-----------------------------------------------
            table.insert(document);
            v.setid(employeeid);
            System.out.println("เพิ่มประวัติการเข้าใช้เรียบร้อยแล้ว");
        /*if(table)
        System.err.println(cur.one().get("TRAN_LOG_ID"));
        if(cur.one().get("TRAN_LOG_ID")){
            id = 1;
        }else{
            double n = (double)(cur.one().get("TRAN_LOG_ID"));
            id = (int)n+1;
        }*/
        System.err.println(id);
        /*while(c.hasNext()){
            System.out.println(c);
        }*/
    }catch(Exception e){
        e.printStackTrace();
    }
    return output;
}
public void Mainpanel(){
    Main m = new Main();
        m.setVisible(true);
    this.setVisible(false);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        user_txt = new javax.swing.JTextField();
        user_label = new javax.swing.JLabel();
        pwd_label = new javax.swing.JLabel();
        pwd_txt = new javax.swing.JPasswordField();
        jCheckBox2 = new javax.swing.JCheckBox();
        login_btn = new javax.swing.JButton();
        exit_btn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("หน้าต่างการล๊อคอิน");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user_txt.setForeground(new java.awt.Color(153, 153, 153));
        user_txt.setText("Username or Email");
        user_txt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        user_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                user_txtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                user_txtFocusLost(evt);
            }
        });
        user_txt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                user_txtMouseEntered(evt);
            }
        });
        getContentPane().add(user_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 210, 30));

        user_label.setText("ชื่อผู้ใช้");
        getContentPane().add(user_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 290, -1, -1));

        pwd_label.setText("รหัสผ่าน:");
        getContentPane().add(pwd_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, -1, -1));

        pwd_txt.setForeground(new java.awt.Color(153, 153, 153));
        pwd_txt.setText("Password");
        pwd_txt.setEchoChar((char)0);
        pwd_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pwd_txtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pwd_txtFocusLost(evt);
            }
        });
        getContentPane().add(pwd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 210, 30));

        jCheckBox2.setText("แสดงรหัสผ่าน");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        getContentPane().add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 340, -1, -1));

        login_btn.setText("ลงชื่อเข้าใช้");
        login_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login_btnMouseEntered(evt);
            }
        });
        login_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_btnActionPerformed(evt);
            }
        });
        getContentPane().add(login_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 120, 50));

        exit_btn.setText("ออกจากโปรแกรม");
        exit_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_btnActionPerformed(evt);
            }
        });
        getContentPane().add(exit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 430, 120, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Untitled-3.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 480, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_btnActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะออกจากโปรแกรมจริงหรือไม่","System",YES_NO_OPTION)==YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_exit_btnActionPerformed

    private void login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_btnActionPerformed
        String username = user_txt.getText();
        String password = pwd_txt.getText();
        if(checklogin(username,password)){
            JOptionPane.showMessageDialog(null,"เข้าสู่ระบบสำเร็จ");
            Mainpanel();
        }else{
            JOptionPane.showMessageDialog(null,"ไม่สามารถเข้าสู่ระบบได้ กรุณาลองใหม่อีกครั้ง",null,ERROR_MESSAGE);
        }
        clear();
    }//GEN-LAST:event_login_btnActionPerformed

    private void user_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_txtFocusLost
        if(user_txt.getText().equals("")){
            user_txt.setText("Username or Email");
            user_txt.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_user_txtFocusLost

    private void user_txtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_txtFocusGained
        if(user_txt.getText().equals("Username or Email")){
            user_txt.setText("");
            user_txt.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_user_txtFocusGained

    private void pwd_txtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwd_txtFocusGained
        if(pwd_txt.getText().equals("Password")){
            pwd_txt.setEchoChar('*');
            pwd_txt.setText("");
            pwd_txt.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_pwd_txtFocusGained

    private void pwd_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwd_txtFocusLost
        if(pwd_txt.getText().equals("")){
            pwd_txt.setEchoChar((char)0);
            pwd_txt.setText("Password");
            pwd_txt.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_pwd_txtFocusLost

    private void user_txtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_txtMouseEntered

    }//GEN-LAST:event_user_txtMouseEntered

    private void login_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_btnMouseEntered

    }//GEN-LAST:event_login_btnMouseEntered

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        showpwd = !showpwd;
        if(showpwd ==true){
            pwd_txt.setEchoChar((char)0);
        }else{
            pwd_txt.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exit_btn;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton login_btn;
    private javax.swing.JLabel pwd_label;
    private javax.swing.JPasswordField pwd_txt;
    private javax.swing.JLabel user_label;
    private javax.swing.JTextField user_txt;
    // End of variables declaration//GEN-END:variables
}
