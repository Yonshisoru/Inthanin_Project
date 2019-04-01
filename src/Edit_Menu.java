
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yonshisoru
 */
public class Edit_Menu extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    boolean showpwd = false;
    /**
     * Creates new form Customer
     */
    public Edit_Menu() {
        initComponents();
        getconnect();
    }
public void getconnect(){
    try{
        mongo = new MongoClient("localhost",27017);
        db = mongo.getDB("InthaninDB");
    }catch(Exception e){
        System.out.println(e);
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
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        showpwd_check = new javax.swing.JCheckBox();
        district_txt = new javax.swing.JTextField();
        id_txt = new javax.swing.JTextField();
        agecombo = new javax.swing.JComboBox<>();
        man_radio = new javax.swing.JRadioButton();
        woman_radio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        prefix = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        fname_txt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lname_txt = new javax.swing.JTextField();
        locality_txt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        home_txt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        email_txt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        post_txt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        province_txt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        user_txt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        position_combo = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        phone_txt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        pwd_txt = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inthanin.exe");
        setMinimumSize(new java.awt.Dimension(713, 408));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(781, 334));
        jPanel1.setPreferredSize(new java.awt.Dimension(781, 334));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลพนักงาน");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 30));

        jButton3.setText("ยกเลิก");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 110, 50));

        jButton4.setText("ยืนยันการแก้ไข");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, 110, 50));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ลำดับที่", "ชื่อ-สกุล", "ชื่อผู้เข้าใช้งาน", "อีเมลล์", "ตำแหน่ง"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 860, 100));

        jRadioButton1.setText("ลบข้อมูลพนักงาน");
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        jRadioButton2.setText("แก้ไขข้อมูลพนักงาน");
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        showpwd_check.setText("แสดงรหัสผ่าน");
        showpwd_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpwd_checkActionPerformed(evt);
            }
        });
        getContentPane().add(showpwd_check, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, -1, -1));

        district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                district_txtActionPerformed(evt);
            }
        });
        getContentPane().add(district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 220, 50, 20));
        getContentPane().add(id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 340, 210, 20));

        agecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกอายุ" }));
        getContentPane().add(agecombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 80, 20));

        man_radio.setText("ชาย");
        getContentPane().add(man_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, -1, -1));

        woman_radio.setText("หญิง");
        getContentPane().add(woman_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 250, -1, -1));

        jLabel5.setText("วัน/เดือน/ปีเกิด:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, -1, -1));

        jLabel6.setText("อายุ:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, -1, -1));

        birthdate_txt.setWeekStyle(datechooser.view.WeekDaysStyle.FULL);
        getContentPane().add(birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, -1, -1));

        jLabel7.setText("เพศ:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, -1, -1));

        prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นางสาว" }));
        getContentPane().add(prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));

        jLabel8.setText("เลขบัตรประจำตัวประชาชน:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, -1, -1));

        jLabel9.setText("ชื่อ:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, -1, -1));
        getContentPane().add(fname_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 110, 20));

        jLabel10.setText("นามสกุล:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, -1, -1));

        jLabel11.setText("อำเภอ:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 220, -1, -1));

        lname_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lname_txtActionPerformed(evt);
            }
        });
        getContentPane().add(lname_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 110, 20));

        locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locality_txtActionPerformed(evt);
            }
        });
        getContentPane().add(locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 220, 50, 20));

        jLabel12.setText("ตำบล:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 220, -1, -1));

        home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home_txtActionPerformed(evt);
            }
        });
        getContentPane().add(home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, 50, 20));

        jLabel13.setText("บ้านเลขที่:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 220, -1, -1));

        email_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                email_txtActionPerformed(evt);
            }
        });
        getContentPane().add(email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 300, 140, 20));

        jLabel14.setText("จังหวัด:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 260, -1, -1));

        jLabel15.setText("ตำแหน่ง:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, -1, -1));

        post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post_txtActionPerformed(evt);
            }
        });
        getContentPane().add(post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 260, 50, 20));

        jLabel16.setText("อีเมลล์:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 300, -1, -1));

        province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                province_txtActionPerformed(evt);
            }
        });
        getContentPane().add(province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 260, 140, 20));

        jLabel17.setText("ชื่อผู้ใช้งาน:\n");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, -1, -1));

        user_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_txtActionPerformed(evt);
            }
        });
        getContentPane().add(user_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 410, 140, 20));

        jLabel18.setText("รหัสผ่าน:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 410, -1, -1));

        position_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกตำแหน่ง", "Employee", "Owner" }));
        getContentPane().add(position_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, -1, -1));

        jLabel19.setText("รหัสไปรษณีย์:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 260, -1, -1));

        phone_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phone_txtActionPerformed(evt);
            }
        });
        getContentPane().add(phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, 140, 20));

        jLabel20.setText("เบอร์โทรศัพท์:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, -1, -1));

        pwd_txt.setToolTipText("");
        pwd_txt.setEchoChar('*');
        getContentPane().add(pwd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 410, 140, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{
           try{
             DBCollection table = db.getCollection("MS_CUSTOMER");
             BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
             DBCursor cur = table.find().sort(sortObject);
             //double n = (double)(cur.one().get("MS_CUSTOMER_ID"));
            int id = 0;
            DBCursor find = table.find();
            System.out.println(find.hasNext());
            if(find.hasNext()==true){
            System.out.println("eiei");
            int k = (int)cur.one().get("MS_CUSTOMER_ID");
            id = k+1;
            }else{
            id = 1; 
            }
            System.out.println(id);
             /*BasicDBObject document = new BasicDBObject();
             document.put("MS_CUSTOMER_ID",(int)id);
             document.put("MS_CUSTOMER_NAME",prefix.getSelectedItem().toString()+" "+fname_txt.getText()+" "+lname_txt.getText());
             document.put("MS_CUSTOMER_PHONE",phone_txt.getText());
             document.put("MS_CUSTOMER_EMAIL",email_txt.getText());
             BasicDBObject address = new BasicDBObject();
             address.put("บ้านเลขที่", home_txt.getText());
             address.put("ตำบล", locality_txt.getText());
             address.put("อำเภอ", district_txt.getText());
             address.put("จังหวัด", province_txt.getText());
             address.put("รหัสไปรษณีย์", post_txt.getText());
             document.put("MS_CUSTOMER_ADDRESS",address);
             document.put("MS_CUSTOMER_BIRTHDATE",birthdate_txt.getText());
             document.put("MS_CUSTOMER_TYPE",type_txt.getSelectedItem().toString());
             document.put("MS_CUSTOMER_POINTS",0);
             table.insert(document);*/
             JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ");
             //this.setVisible(false);
           }catch(Exception e){
               e.printStackTrace();
           }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
            this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void showpwd_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpwd_checkActionPerformed
        showpwd = !showpwd;
        if(showpwd ==true){
            pwd_txt.setEchoChar((char)0);
        }else{
            pwd_txt.setEchoChar('*');
        }
    }//GEN-LAST:event_showpwd_checkActionPerformed

    private void district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_district_txtActionPerformed

    private void lname_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lname_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lname_txtActionPerformed

    private void locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locality_txtActionPerformed

    private void home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_home_txtActionPerformed

    private void email_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_email_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_email_txtActionPerformed

    private void post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_post_txtActionPerformed

    private void province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_province_txtActionPerformed

    private void user_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_txtActionPerformed

    private void phone_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phone_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phone_txtActionPerformed

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
            java.util.logging.Logger.getLogger(Edit_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_Customer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> agecombo;
    private datechooser.beans.DateChooserCombo birthdate_txt;
    private javax.swing.JTextField district_txt;
    private javax.swing.JTextField email_txt;
    private javax.swing.JTextField fname_txt;
    private javax.swing.JTextField home_txt;
    private javax.swing.JTextField id_txt;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lname_txt;
    private javax.swing.JTextField locality_txt;
    private javax.swing.JRadioButton man_radio;
    private javax.swing.JTextField phone_txt;
    private javax.swing.JComboBox<String> position_combo;
    private javax.swing.JTextField post_txt;
    private javax.swing.JComboBox<String> prefix;
    private javax.swing.JTextField province_txt;
    private javax.swing.JPasswordField pwd_txt;
    private javax.swing.JCheckBox showpwd_check;
    private javax.swing.JTextField user_txt;
    private javax.swing.JRadioButton woman_radio;
    // End of variables declaration//GEN-END:variables
}