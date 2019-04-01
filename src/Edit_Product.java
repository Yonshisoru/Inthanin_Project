
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
public class Edit_Product extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    /**
     * Creates new form Customer
     */
    public Edit_Product() {
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
        pro_name_txt = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        pro_price_txt = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        pro_type_combo = new javax.swing.JComboBox<>();
        partner_combo = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();

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
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลสินค้า");
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
                "รายการที่", "ชื่อสินค้า"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, 360));

        jRadioButton1.setText("ลบข้อมูลสินค้า");
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        jRadioButton2.setText("แก้ไขข้อมูลสินค้า");
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        getContentPane().add(pro_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 310, -1));

        jLabel46.setText("ชื่อสินค้า:");
        getContentPane().add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, -1, -1));

        jLabel53.setText("ราคา:");
        getContentPane().add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, -1, -1));
        getContentPane().add(pro_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 150, 70, -1));

        jLabel42.setText("ประเภทของสินค้า:");
        getContentPane().add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, -1, -1));

        pro_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ส่วนประกอบเครื่องดื่ม", "เบเกอรี่", "ส่วนประกอบของคาว" }));
        getContentPane().add(pro_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 190, 180, 30));

        partner_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกบริษัทคู่ค้า" }));
        getContentPane().add(partner_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 240, 180, 30));

        jLabel63.setText("บริษัทคู่ค้า:");
        getContentPane().add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, -1, -1));

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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> partner_combo;
    private javax.swing.JTextField pro_name_txt;
    private javax.swing.JTextField pro_price_txt;
    private javax.swing.JComboBox<String> pro_type_combo;
    // End of variables declaration//GEN-END:variables
}
