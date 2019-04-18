import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yonshisoru
 */
public class Order_confirm extends javax.swing.JFrame {
    Variable v = new Variable();
    boolean first_notification = false;
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    DBObject dbo;
    int sum_order_price = 0;
    double total_order_price = 0;
    int discount_price = 0;
    public void printInvoice(){
        String time = LocalTime.now().toString().substring(0,2)+"-"+LocalTime.now().toString().substring(3,5)+"-"+LocalTime.now().toString().substring(6,8);
        String filename = "$"+LocalDate.now()+"$"+time+"$"+/*t.getorderid()*/001+".pdf";
        try{
        Document doc = new Document();
        BaseFont baseFont = BaseFont.createFont("fonts/fontsgod.ttf", BaseFont.IDENTITY_H,true);
        Font font = new Font(baseFont,18);
        Font topicfont = new Font(baseFont,20);
        Font bigfont = new Font(baseFont,30);
        PdfWriter.getInstance(doc,new FileOutputStream("invoice/"+filename));
        doc.open();
        doc.add(new Paragraph(String.format("Inthanin Coffee"),bigfont));
        String date = LocalDate.now().toString().substring(LocalDate.now().toString().length()-2,LocalDate.now().toString().length());
        String month= LocalDate.now().toString().substring(5,7);
        String year = LocalDate.now().toString().substring(0,4);
        doc.add(new Paragraph(String.format("%s\n","วันที่: "+date+"/"+month+"/"+year),font));
        doc.add(new Phrase(String.format("%s\n","เวลา: "+LocalTime.now().toString().substring(0,8)+" น."),font));
        doc.add(new Paragraph(String.format("%s","-----------------------------------------------------------------------------------------------------"),font));
        doc.add(new Paragraph(String.format("%s\n","ใบเสร็จรับเงิน"),bigfont));
                /*for(Menu_variable mm:Pay_Menu){
                        double eiei = (double)mm.c.gettotal()*(double)mm.c.getunits();
                       doc.add(new Paragraph(String.format("%s",mm.c.getunits()+"          "+mm.getname()+"               "+String.format("%.2f",eiei)+" บาท"),font)); 
            }*/
     doc.add(new Paragraph(String.format("%s","-----------------------------------------------------------------------------------------------------"),font));  
     doc.add(new Paragraph(String.format("%s\n","จำนวนสุทธิ: "+" รายการ"),font));    
     doc.add(new Paragraph(String.format("%s\n","รับเงิน: "+" บาท"),font));       
     doc.add(new Paragraph(String.format("%s\n","ราคารวมทั้งหมด: "+" บาท"),font)); 
     doc.add(new Paragraph(String.format("%s\n","เงินทอน: "+" บาท"),font));     
     doc.add(new Paragraph(String.format("%s","\n\n\n"),font)); 
     doc.add(new Paragraph(String.format("%s","                                       Thank you and please come again                         "),font)); 
     doc.add(new Paragraph(String.format("%s","                                             Inthanin Coffee                              "),font)); 
     doc.add(new Paragraph(String.format("%s","\n\n\n"),font)); 
     doc.add(new Paragraph(String.format("%s"," -------------------------------------------@powered by SAM---------------------------------------"),font));  
        doc.close();
}catch (DocumentException ex){
    Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE,null,ex);
}      catch (FileNotFoundException ex) {
           Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE, null, ex);
       }catch(IOException ex){
          Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE, null, ex); 
       }
try{
        Desktop.getDesktop().open(new File("./invoice/"+filename));
                }catch(Exception e){
                    System.out.println(e);
                }
    }
    /**
     * Creates new form Order_confirm
     */
    public Order_confirm() {
        initComponents();
        get_order_list((DefaultTableModel)order_table.getModel());
        set_total_text();
    }
    public void set_total_text(){
        order_sum_txt.setText(""+sum_order_price);
        order_total_txt.setText(""+total_order_price);
    }
    public void get_order_list(DefaultTableModel table){
        db = v.getConnect();
        DefaultTableModel model = table;
        Object[] row = new Object[4];
        DBCollection product_collection  = db.getCollection("TRAN_ORDER_LIST");
        DBCursor product_finding = product_collection.find();
        while(product_finding.hasNext()){
            DBObject menu_json = product_finding.next();
            int menu_id = (int)menu_json.get("MS_MENU_ID");
            row[0] = (int)menu_json.get("TRAN_ORDER_LIST_ID");
            row[1] = find_product_name(menu_id).get("MS_MENU_NAME");
            //System.out.println(">>>>"+menu_json.get("TRAN_ORDER_LIST_PRICE"));
            row[2] = menu_json.get("TRAN_ORDER_LIST_AMOUNT");
            row[3] = menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE");
            sum_order_price += (int)menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE");
            model.addRow(row);
        }
        total_order_price = sum_order_price;
    } 
        public DBObject find_product_id(String name){ //ค้นหารหัสของสินค้าจากชื่อ
            DBCollection product = db.getCollection("MS_PRODUCT");
            BasicDBObject data = new BasicDBObject("MS_PRODUCT_NAME",name);
            DBCursor find = product.find(data);
            DBObject product_json = null;
            //int productid = -1; //-1 = null
            try{
                product_json = find.next();
                //System.out.println(product_json);
                //productid = (int)product_json.get("MS_PRODUCT_ID");
                //System.out.println(productid);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ");
            }
        return product_json;
    }
        public DBObject find_product_name(int id){ //ค้นหารหัสของสินค้าจากชื่อ
            DBCollection product = db.getCollection("MS_MENU");
            BasicDBObject data = new BasicDBObject("MS_MENU_ID",id);
            DBCursor find = product.find(data);
            DBObject product_json = null;
            //int productid = -1; //-1 = null
            try{
                product_json = find.next();
                //System.out.println(product_json);
                //productid = (int)product_json.get("MS_PRODUCT_ID");
                //System.out.println(productid);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ");
            }
        return product_json;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        order_sum_txt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        order_discount_txt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        order_total_txt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        order_table = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างยืนยันการสั่ง");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(915, 640));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("รหัสสมาชิก");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 113, -1));

        order_sum_txt.setEnabled(false);
        getContentPane().add(order_sum_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 380, 113, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("ราคารวม:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 380, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("รหัสสมาชิก");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField4.setEnabled(false);
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 230, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("เบอร์โทร:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField5.setEnabled(false);
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 240, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("ชื่อ-สกุล:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField6.setEnabled(false);
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 250, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("อีเมลล์:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        jTextField7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField7.setEnabled(false);
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 80, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("แต้มสะสม:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("ส่วนลด:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 380, -1, -1));

        order_discount_txt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                order_discount_txtMouseClicked(evt);
            }
        });
        getContentPane().add(order_discount_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 380, 113, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("ราคาสุทธิ:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 420, -1, -1));

        order_total_txt.setEnabled(false);
        getContentPane().add(order_total_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 420, 113, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("ยืนยัน");
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("ยกเลิก");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 460, 120, 40));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("ยืนยันการออเดอร์");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 450, 120, 40));

        order_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "จำนวน", "ราคา"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(order_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, 500, 290));

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel36.setText("ตารางออเดอร์");
        getContentPane().add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, -1, -1));

        jButton4.setText("ยืนยัน");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 380, 60, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         printInvoice();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{
        int discount = Integer.parseInt(order_discount_txt.getText());
        total_order_price = sum_order_price-((sum_order_price*discount)/100);
        set_total_text();
        //order_discount_txt.setEnabled(false);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"คุณใส่จำนวนส่วนลดไม่ถูกต้อง\nกรุณากรอกเป็นจำนวนเต็มด้วยค่ะ","",ERROR_MESSAGE);
            order_discount_txt.setText("");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void order_discount_txtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_order_discount_txtMouseClicked

    }//GEN-LAST:event_order_discount_txtMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       if(first_notification==false){
        JOptionPane.showMessageDialog(null,"กรุณากรอกส่วนลดเป็นจำนวนเต็มด้วยค่ะ\nถ้าหากไม่มีส่วนลด กรุณากรอกเลข 0 หรือ ปล่อยช่องให้เว้นว่างไว้ค่ะ\nRange(1-100) หน่วย:เปอร์เซนต์");
        first_notification = true;
       }else{
       }
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(Order_confirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Order_confirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Order_confirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Order_confirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Order_confirm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField order_discount_txt;
    private javax.swing.JTextField order_sum_txt;
    private javax.swing.JTable order_table;
    private javax.swing.JTextField order_total_txt;
    // End of variables declaration//GEN-END:variables
}
