import com.mongodb.*;
import java.awt.Color;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
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
Variable v = new Variable();//สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
DB db; //กำหนดตัวแปรประเภท DB
DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
//--------Boolean----------------------------
boolean showpwd = false; //สร้างตัวแปรเพื่อใช้ในการเช็คฟังก์ชั่นแสดงรหัสผ่าน
boolean connected = false; //สร้างตัวแปรเพื่อเช็คการเชื่อมต่อ

//DBObject dbo;
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setpicture(); //ฟังก์ชั่นเพิ่มรูปไอค่อน
        this.setLocationRelativeTo(null); //ตั้งค่าการแสดงผลให้อยู่กลางหน้าจอ
        getConnect(); //ฟังก์ชั่นการเชื่อมต่อ
        if(connected==false){ //ถ้าหากการเชื่อมต่อล้มเหลว
            JOptionPane.showMessageDialog(null,"การเชื่อมต่อเซิร์ฟเวอร์ล้มเหลว\nกำลังปิดโปรแกรม"); //แสดงหน้าต่างพร้อมตักอักษรขึ้นหน้าจอ
            System.exit(0); //ปิดการทำงานของโปรแกรม
        }
    }
    public void setpicture(){ //ฟังก์ชั่นเพิ่มรูปไอค่อน
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        ImageIcon imageIcon = new ImageIcon ("./image/login_icon.png"); //สร้างตัวแปรเพื่อเก็บรูปภาพจากปลายทาง 
        picture_label.setIcon(imageIcon); //ตั้งค่าไอค่อนของ label ให้เป็นรูปภาพ
                }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                    e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                }
        }
public void getConnect(){ //ฟังก์ชั่นการเชื่อมต่อ
    try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        MongoClient mongo = new MongoClient("localhost",27017);//เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        db = mongo.getDB("InthaninDB");//ดึงข้อมูลฐานข้อมูล
        connected = true; //การเชื่อมต่อสำเร็จ
        if(db==null){ //ถ้าหากไม่มีการเชื่อมต่อ
            connected = false; //การเชื่อมต่อล้มเหลว
        }
    }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
        e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
        JOptionPane.showMessageDialog(null,"การเชื่อมต่อเซิร์ฟเวอร์ล้มเหลว\nกำลังปิดโปรแกรม");
        System.exit(0); //ปิดการทำงานของโปรแกรม
    }
}
public void clear(){ //ฟังก์ชั่นเคลียร์ข้อมูลทั้งหมด
       user_txt.setText("Username or Email");
       pwd_txt.setText("Password");
}
public boolean checklogin(String user,String pwd){ //ฟังก์ชั่นเช็คการ login
    boolean output =false; //สร้างตัวแปรเพื่อนำไปแสดงผลว่าการ login สำเร็จหรือไม่
    boolean checkid = false; //สร้างตัวแปรเช็คการ login จากid+password
    boolean checkemail = false; //สร้างตัวแปรเช็คการlogin จาก email+password
    int employeeid = 0; //กำหนดตัวแปรเพื่อใช้เก็บรหัสของพนักงาน
    try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        DBObject userinfo = null; //สร้างตัวแปรเปล่าของ DBObject เพื่อใช้เก็บข้อมูลของพนักงาน
        DBC = db.getCollection("MS_EMPLOYEE"); //ดึงข้อมูลจาก Collection ของพนักงานมาใส่ในตัวแปร
        BasicDBObject search = new BasicDBObject(); //สร้างObjectชื่อ search เพื่อใช้เก็บข้อมูลที่ใช้ค้นหา
        search.put("MS_EMPLOYEE_USERNAME",user); //ชื่อผู้ใช้งาน
        search.put("MS_EMPLOYEE_PWD",pwd); //รหัสผ่าน
        DBCursor c = DBC.find(search); //ค้นหาข้อมูลทั้งหมดของ (MS_EMPLOYEE)
        if(c.hasNext()){ //ถ้าหากว่าพบข้อมูล
            checkid = true; //ให้ตัวแปรเช็คการ login จาก id+password เป็น true
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                userinfo = c.next(); //นำข้อมูลของพนักงานไปใส่ในตัวแปร userinfo
            employeeid = (int)userinfo.get("MS_EMPLOYEE_ID"); //ดึงข้อมูลรหัสพนักงานมาเก็บไว้ในตัวแปร
            String employeeid2 = userinfo.get("MS_EMPLOYEE_NAME").toString();
            }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            }
        }else{ //ถ้าหากว่าไม่พบจ้อมูล
            checkid = false; //ให้ตัวแปรเช็คการ login จาก id+password เป็น false
        }
        if(checkid==false){ //ถ้าหากไม่พบการlogin จาก id+password
        BasicDBObject searchemail = new BasicDBObject(); //สร้างObject  searchemail เพื่อใช้ค้นหาข้อมูล
        searchemail.put("MS_EMPLOYEE_EMAIL",user); //อีเมลของพนักงาน
        searchemail.put("MS_EMPLOYEE_PWD",pwd);//รหัสผ่านของพนักงาน
        DBCursor cl = DBC.find(searchemail); //ค้นหาข้อมูลทั้งหมดของ (MS_EMPLOYEE)
        if(cl.hasNext()){ //ถ้าหากพบข้อมูล
           checkemail = true;//ให้ตัวแปรเช็คการ login จาก email+password เป็น true
           userinfo = cl.next(); //นำข้อมูลของพนักงานไปใส่ในตัวแปร userinfo
           employeeid = (int)userinfo.get("MS_EMPLOYEE_ID"); //ดึงข้อมูลรหัสพนักงานมาเก็บไว้ในตัวแปร
        }else{
           checkemail =false;//ให้ตัวแปรเช็คการ login จาก email+password เป็น false

        }
        }
        if(checkemail==true||checkid==true){//ถ้าหากว่าการ login สำเร็จให้แสดงผล
            output = true;
        }else{
            output = false;
        }
        if(userinfo.get("MS_EMPLOYEE_TYPE").equals("Employee")){ //ถ้าหากว่าพนักงานเป็นพนักงานปกติให้สถานะเป็น 1
            v.setstatus(1);
        }else if(userinfo.get("MS_EMPLOYEE_TYPE").equals("Owner")){ //ถ้าหากว่าพนักงานเป็นเจ้าของร้านให้สถานะเป็น 0
            v.setstatus(0);
        }
        DBCollection table = db.getCollection("TRAN_LOG"); //ดึงข้อมูลของประวัติการใช้งาน (TRAN_LOG) มาใส่ในตัวแปร table
        BasicDBObject sortObject = new BasicDBObject().append("_id", -1); //สร้างObject เพื่อค้นหาข้อมูลตัวสุดท้าย
        DBCursor cur = table.find().sort(sortObject); //ค้นหาข้อมูลโดยจัดเรียงตาม sortObject
        int id = 0; //สร้างตัวแปรเพื่อใช้เก็บรหัสลำดับของประวัติการใช้งาน
        DBCursor find = table.find(); //ค้นหาข้อมูลทั้งหมดของประวัติการใช้งาน
       // System.out.println(find.hasNext());
        if(find.hasNext()==true){//ถ้าหากเจอให้ดึงข้อมูลรหัสลำดับตัวสุดท้ายแล้วบวกด้วย 1
            int n = (int)cur.one().get("TRAN_LOG_ID");
            id = n+1;
        }else{ //ถ้าหากว่าไม่เจอให้ค่าเท่ากับ 1 (ตัวแรก)
            id = 1; 
        }
             DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE; //สร้างตัวแปรในการกำหนดฟอร์แมทของวันที่
             String formattedDate = formatter.format(LocalDate.now()); //สร้างตัวแปรในการเก็บวันที่ปัจจุบันตามฟอร์แมทของ formatter
             String month = v.month(Integer.parseInt(formattedDate.substring(4,6))); //ใช้งานฟังก์ชั่นแปลงลำดับของเดือนเป็นชื่อเดือน
             String year = formattedDate.substring(0,4); //ปีปัจจุบัน
             String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length()); //วันที่ปัจจุบัน
            //-----------------------------------------------
            BasicDBObject document = new BasicDBObject(); //สร้างObject เพื่อค้นหาข้อมูลของประวัติการใช้งาน
            document.put("TRAN_LOG_ID",id); //รหัสลำดับประวัติการใช้งาน
            document.put("TRAN_LOG_DATE",month+" "+date+", "+year); //วันที่ใช้งาน
            document.put("TRAN_LOG_TIME",LocalTime.now().toString().substring(0,8)); //เวลาที่ใช้งาน
            document.put("TRAN_LOG_TYPE","Login"); //ประเภทการใช้งาน
            document.put("MS_EMPLOYEE_ID",employeeid); //รหัสพนักงาน
            //-----------------------------------------------
            table.insert(document); //เพิ่มข้อมูลของประวัติการใช้งานลง Database
            v.setid(employeeid); //ตั้งค่าประเภทของผู้ใช้เป็น static
            System.out.println("เพิ่มประวัติการเข้าใช้เรียบร้อยแล้ว");//แสดงข้อความเมื่อเพิ่มประวัติการใช้งานสำเร็จ
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
    return output; //คืนค่าผลการ login
}
public void Mainpanel(){ //ฟังก์ชั่นหน้าจอหลัก
    Main m = new Main(); //สร้างObjectของหน้าจอหลัก
        m.setVisible(true);//แสดงหน้าจอหลัก
    this.setVisible(false);//ซ่อนหน้าจอนี้
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
        picture_label = new javax.swing.JLabel();

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
        getContentPane().add(picture_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 480, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_btnActionPerformed
        //ยืนยันว่าจะออกจากโปรแกรมหรือไม่ ถ้ายืนยันจะทำงานออกจากโปรแกรม
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะออกจากโปรแกรมจริงหรือไม่","System",YES_NO_OPTION)==YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_exit_btnActionPerformed

    private void login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_btnActionPerformed
        String username = user_txt.getText(); //ดึงรหัสผู้ใช้มาใส่ไว้ในตัวแปร
        String password = pwd_txt.getText(); //ดึงรหัสผ่านมาใส่ไว้ในตัวแปร
        if(checklogin(username,password)){ //ถ้าหากว่าการ login สำเร็จ
            JOptionPane.showMessageDialog(null,"เข้าสู่ระบบสำเร็จ"); //แสดงกล่องข้อความทางหน้าจอพร้อมตัวหนังสือ
            Mainpanel();//ใช้งานฟังก์ชั่นแสดงหน้าจอหลัก
        }else{//ถ้าหากการ login ไม่สำเร็จ
            JOptionPane.showMessageDialog(null,"ไม่สามารถเข้าสู่ระบบได้ กรุณาลองใหม่อีกครั้ง",null,ERROR_MESSAGE);//แสดงกล่องข้อความทางหน้าจอพร้อมตัวหนังสือ
        }
        clear();//ลบข้อมูลทั้งหมด
    }//GEN-LAST:event_login_btnActionPerformed

    private void user_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_txtFocusLost
        if(user_txt.getText().equals("")){//ถ้าหากว่ากล่องกรอกชื่อผู้ใช้งานผ่านว่าง
            user_txt.setText("Username or Email"); //ให้แสดงข้อความพื้นหลัง
            user_txt.setForeground(Color.GRAY); //ให้ข้อความเป็นสีเทา
        }
    }//GEN-LAST:event_user_txtFocusLost

    private void user_txtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_txtFocusGained
        if(user_txt.getText().equals("Username or Email")){//ถ้าหากว่ามีการกรอกข้อความให้ซ่อนข้อความ
            user_txt.setText("");
            user_txt.setForeground(Color.BLACK);//ข้อความเป็นสีดำ
        }
    }//GEN-LAST:event_user_txtFocusGained

    private void pwd_txtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwd_txtFocusGained
        if(pwd_txt.getText().equals("Password")){ //ถ้าหากว่ากล่องรหัสผ่านมีการกรอกข้อความ
            pwd_txt.setEchoChar('*');//ให้ซ่อนรหัสผ่านโดยให้ * แทนรหัสผ่าน และซ่อนข้อความ
            pwd_txt.setText("");
            pwd_txt.setForeground(Color.BLACK); //ให้ข้อความเป็นสีดำ
        }
    }//GEN-LAST:event_pwd_txtFocusGained

    private void pwd_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwd_txtFocusLost
        if(pwd_txt.getText().equals("")){//ถ้าหากว่ากล่องรหัสผ่านว่าง
            pwd_txt.setEchoChar((char)0); //ยกเลิกการซ่อนรหัสผ่าน
            pwd_txt.setText("Password"); //ให้แสดงข้อความ
            pwd_txt.setForeground(Color.GRAY); //ให้ข้อความเป็นสีเทา
        }
    }//GEN-LAST:event_pwd_txtFocusLost

    private void user_txtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_txtMouseEntered

    }//GEN-LAST:event_user_txtMouseEntered

    private void login_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_btnMouseEntered

    }//GEN-LAST:event_login_btnMouseEntered

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        showpwd = !showpwd; //สลับตัวแปรแสดงรหัสผ่านเมื่อมีการกด
        if(showpwd ==true){//ถ้าหากมีการกดใช้งานการแสดงรหัสผ่าน
            pwd_txt.setEchoChar((char)0); //แสดงรหัสผ่าน
        }else{
            pwd_txt.setEchoChar('*');//ซ่อนรหัสผ่าน
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
    private javax.swing.JButton login_btn;
    private javax.swing.JLabel picture_label;
    private javax.swing.JLabel pwd_label;
    private javax.swing.JPasswordField pwd_txt;
    private javax.swing.JLabel user_label;
    private javax.swing.JTextField user_txt;
    // End of variables declaration//GEN-END:variables
}
