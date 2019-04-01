
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
public class Edit_Partner extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    /**
     * Creates new form Customer
     */
    public Edit_Partner() {
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
        fname_txt2 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        phone_txt2 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        email_txt2 = new javax.swing.JTextField();
        home_txt2 = new javax.swing.JTextField();
        locality_txt2 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        district_txt2 = new javax.swing.JTextField();
        post_txt2 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        province_txt2 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();

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
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลบริษัทคู่ค้า");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, -1, -1));

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
                "ลำดับที่", "ชื่อบริษัท"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, 360));

        jRadioButton1.setText("ลบข้อมูลบริษัทคู่ค้า");
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        jRadioButton2.setText("แก้ไขข้อมูลบริษัทคู่ค้า");
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        getContentPane().add(fname_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 310, -1));

        jLabel43.setText("ชื่อบริษัท:");
        getContentPane().add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, -1, -1));

        jLabel44.setText("เบอร์โทรศัพท์:");
        getContentPane().add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, -1, -1));
        getContentPane().add(phone_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 120, 120, -1));

        jLabel45.setText("อีเมลล์:");
        getContentPane().add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, -1, -1));
        getContentPane().add(email_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, 150, -1));

        home_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home_txt2ActionPerformed(evt);
            }
        });
        getContentPane().add(home_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 50, 20));

        locality_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locality_txt2ActionPerformed(evt);
            }
        });
        getContentPane().add(locality_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 160, 50, 20));

        jLabel47.setText("ตำบล:");
        getContentPane().add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, -1, -1));

        jLabel48.setText("อำเภอ:");
        getContentPane().add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 160, -1, -1));

        district_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                district_txt2ActionPerformed(evt);
            }
        });
        getContentPane().add(district_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 160, 50, 20));

        post_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post_txt2ActionPerformed(evt);
            }
        });
        getContentPane().add(post_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 200, 50, 20));

        jLabel49.setText("รหัสไปรษณีย์:");
        getContentPane().add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 200, -1, -1));

        jLabel50.setText("จังหวัด:");
        getContentPane().add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 200, -1, -1));

        province_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                province_txt2ActionPerformed(evt);
            }
        });
        getContentPane().add(province_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 200, 140, 20));

        jLabel51.setText("บ้านเลขที่:");
        getContentPane().add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 160, -1, -1));

        jLabel39.setText("ประเภทของคู่ค้า:");
        getContentPane().add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "คู่ค้าส่วนประกอบเครื่องดื่ม", "คู่ค้าของหวานเบเกอรี่", "คู่ค้าส่วนประกอบของคาว" }));
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, 180, 30));

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

    private void home_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_home_txt2ActionPerformed

    private void locality_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locality_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locality_txt2ActionPerformed

    private void district_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_district_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_district_txt2ActionPerformed

    private void post_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_post_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_post_txt2ActionPerformed

    private void province_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_province_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_province_txt2ActionPerformed

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
    private javax.swing.JTextField district_txt2;
    private javax.swing.JTextField email_txt2;
    private javax.swing.JTextField fname_txt2;
    private javax.swing.JTextField home_txt2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField locality_txt2;
    private javax.swing.JTextField phone_txt2;
    private javax.swing.JTextField post_txt2;
    private javax.swing.JTextField province_txt2;
    // End of variables declaration//GEN-END:variables
}