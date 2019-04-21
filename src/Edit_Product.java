
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.JOptionPane;
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
public class Edit_Product extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
//---------------------Boolean------------------------------
        boolean edit = true;    //|--สร้างตัวแปร edit เพื่อเช็คการทำงาน
        boolean delete = false; //|--สร้างตัวแปร delete เพื่อเช็คการทำงาน
//------------------------------------------------------------
        int product_id = 0;
    /**
     * Creates new form Customer
     */
    public Edit_Product() {
        initComponents();
        getconnect();
        get_collection_in_to_table();
    }
    public void getconnect(){ //เชื่อมต่อ Database
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            MongoClient mongo = new MongoClient("localhost", 27017); //เชื่อมต่อ Database Mongodb IP:localhost Port:27017
            db = mongo.getDB(("InthaninDB")); //ดึงข้อมูลจากดาต้าเบสที่ชื่อ InthaninDB
            DBC = db.getCollection("MS_PRODUCT"); // ดึงข้อมูลจาก collection ที่ชื่อ MS_PRODUCT
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            System.exit(0);//ถ้าหากว่ามีการทำงานผิดพลาด ให้ออกจากโปรแกรม
        }
    }
    public void set_productcombo(int k){
         product_partner_combo.removeAllItems();
         product_partner_combo.addItem("เลือกบริษัทคู่ค้า");
        try{
        DBCollection table = db.getCollection("MS_PARTNER");
        DBCursor cur = table.find();
        while(cur.hasNext()){
            DBObject kk = cur.next();
            if(k==0){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Drink")){
                product_partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }else if(k==1){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Bakery")){
                product_partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }else if(k==2){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Meal")){
                product_partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        //partner_combo
    }
    public void check_function(){ //เช็คการทำงานในขณะนี้ (ลบ/แก้ไข)
        if(edit_radio.isSelected()){ //ถ้าหากว่าตัวเลือกแก้ไขได้ถูกเลือก
            product_panel.setVisible(true); //หน้าต่างแก้ไขข้อมูลจะปรากฏขึ้น
            edit=true; //แก้ไขค่าตัวแปรedit ให้มีค่า true
            delete=false; //แก้ไขค่าตัวแปรdelete ให้มีค่า false
            product_btn.setText("ยืนยันการแก้ไข"); //ตั้งการแสดงผลที่ปุ่ม
        }else if(delete_radio.isSelected()){//ถ้าหากว่าตัวเลือกลบได้ถูกเลือก
            product_panel.setVisible(false); //หน้าต่างแก้ไขข้อมูลจะถูกซ่อน
            product_table.clearSelection(); //ยกเลิกการเลือกในตารางลูกค้า
            product_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            clear_product(); //ลบข้อมูลทั้งหมดที่กรอกในหน้าต่างแก้ไขข้อมูล
        }
    }
    
     public int find_partner(String name){
        int id = 0;
        try{
        DBCollection table = db.getCollection("MS_PARTNER");
        BasicDBObject partner = new BasicDBObject("MS_PARTNER_NAME",name);
        DBCursor cur = table.find(partner);
        while(cur.hasNext()){
            DBObject kk = cur.next();
            id = (int)kk.get("MS_PARTNER_ID");
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return id;
        //partner_combo
    }
    
public void clear_table(DefaultTableModel table){ //ลบข้อมูลทั้งหมดในตาราง
        while(table.getRowCount()>0){ //สร้างลูป while โดยมีเงื่อนไขคือ จำนวนแถวในตารางจะต้องมากกว่า 0
            table.removeRow(0); //ลบข้อมูลในแถวที่ 1
        }
    }
    
    public void clear_product(){ //ลบข้อมูลในหน้าต่างแก้ไข
        product_type_combo.setSelectedIndex(0); //ประเภทลูกค้า
        product_partner_combo.setSelectedIndex(0); //คำนำหน้า
        product_name_txt.setText(""); //ชื่อลูกค้า
        product_price_txt.setText(""); //เบอร์โทรศัพท์
        product_amount_txt.setText(""); //อีเมล
    }
    
     public void get_collection_in_to_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางลูกค้า
        DefaultTableModel table = (DefaultTableModel)product_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ product_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 6 ช่อง
        DBCursor cursor = DBC.find(); // ค้นหาข้อมูลในcollection(MS_CUSTOMER)ทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject product_json = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
                //System.out.println(partner.get("MS_CUSTOMER_NAME"));
                try{ //สร้างลูป do-while
                row[0] = product_json.get("MS_PRODUCT_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของสินค้า
                row[1] = product_json.get("MS_PRODUCT_NAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของสินค้า
                row[2] = product_json.get("MS_PRODUCT_AMOUNT").toString(); //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของจำนวนของสินค้า
                table.addRow(row); //เพิ่มแถวข้อมูลของตาราง product_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
                }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                   e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                }
            //table.addRow(row);*/
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }while(cursor.hasNext()); //จะจบการทำงานเมื่อเงื่อนไขเป็น false (ไม่มีข้อมูลตัวถัดไป)
    }
     
        public void get_data_from_table(){
        DBCollection get_product = db.getCollection("MS_PRODUCT");
        BasicDBObject product_data = new BasicDBObject("MS_PRODUCT_ID",Integer.parseInt(product_table.getValueAt(product_table.getSelectedRow(),0).toString()));//สร้างObjectชื่อ product_data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor cursor = get_product.find(product_data);// ค้นหาข้อมูลในcollectionที่ตรงกับเงื่อนไขของ product_data
        do{//สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject product = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
                System.out.println(product);
//System.out.println(product);
                try{ //สร้างลูป do-while
                product_id = Integer.parseInt(product.get("MS_PRODUCT_ID").toString());
                product_name_txt.setText(product.get("MS_PRODUCT_NAME").toString()); //ใส่ข้อมูลของชื่อของสินค้าในช่องข้อมูลที่ชื่อว่า product_name_txt
                product_price_txt.setText(product.get("MS_PRODUCT_PRICE").toString()); //ใส่ข้อมูลของราคาของสินค้าในช่องข้อมูลที่ชื่อว่า product_price_txt
                product_amount_txt.setText(product.get("MS_PRODUCT_AMOUNT").toString()); //ใส่ข้อมูลของจำนวนของสินค้าในช่องข้อมูลที่ชื่อว่า product_amount_txt
                if(product.get("MS_PRODUCT_TYPE").equals("Drink")){
                    product_type_combo.setSelectedIndex(0);
                }else if(product.get("MS_PRODUCT_TYPE").equals("Bakery")){
                    product_type_combo.setSelectedIndex(1);
                }else if(product.get("MS_PRODUCT_TYPE").equals("Meal")){
                    product_type_combo.setSelectedIndex(2);
                }
                set_productcombo(product_type_combo.getSelectedIndex());
                DBCollection get_partner = db.getCollection("MS_PARTNER");
                BasicDBObject find_partner = new BasicDBObject("MS_PARTNER_ID",(int)product.get("MS_PARTNER_ID"));
                DBCursor finding_partner = get_partner.find(find_partner);
                String partner_name = null;
                while(finding_partner.hasNext()){
                    DBObject partner_json = finding_partner.next();
                    partner_name = partner_json.get("MS_PARTNER_NAME").toString();
                }
                product_partner_combo.setSelectedItem(partner_name);
                }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                   e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                }
            //table.addRow(row);
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }while(cursor.hasNext());//จะจบการทำงานเมื่อเงื่อนไขเป็น false (ไม่มีข้อมูลตัวถัดไป)
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        product_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        product_table = new javax.swing.JTable();
        delete_radio = new javax.swing.JRadioButton();
        edit_radio = new javax.swing.JRadioButton();
        product_panel = new javax.swing.JPanel();
        product_name_txt = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        product_price_txt = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        product_type_combo = new javax.swing.JComboBox<>();
        product_partner_combo = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        product_amount_txt = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างแก้ไขข้อมูลสินค้า");
        setMinimumSize(new java.awt.Dimension(713, 408));
        setPreferredSize(new java.awt.Dimension(915, 640));
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

        product_btn.setText("ยืนยันการแก้ไข");
        product_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_btnActionPerformed(evt);
            }
        });
        getContentPane().add(product_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, 110, 50));

        product_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รายการที่", "ชื่อสินค้า", "จำนวนที่มีอยู่"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        product_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                product_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(product_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, 360));

        buttonGroup1.add(delete_radio);
        delete_radio.setText("ลบข้อมูลสินค้า");
        delete_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_radioActionPerformed(evt);
            }
        });
        getContentPane().add(delete_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        buttonGroup1.add(edit_radio);
        edit_radio.setSelected(true);
        edit_radio.setText("แก้ไขข้อมูลสินค้า");
        edit_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_radioActionPerformed(evt);
            }
        });
        getContentPane().add(edit_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        product_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        product_panel.add(product_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 310, -1));

        jLabel46.setText("ชื่อสินค้า:");
        product_panel.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        jLabel53.setText("ราคา:");
        product_panel.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));
        product_panel.add(product_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 70, -1));

        jLabel42.setText("ประเภทของสินค้า:");
        product_panel.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        product_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ส่วนประกอบเครื่องดื่ม", "เบเกอรี่", "ส่วนประกอบของคาว" }));
        product_type_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_type_comboActionPerformed(evt);
            }
        });
        product_panel.add(product_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 180, 30));

        product_partner_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกบริษัทคู่ค้า" }));
        product_panel.add(product_partner_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 180, 30));

        jLabel63.setText("บริษัทคู่ค้า:");
        product_panel.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));
        product_panel.add(product_amount_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 70, -1));

        jLabel54.setText("จำนวนที่มีทั้งหมด:");
        product_panel.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        getContentPane().add(product_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 420, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void product_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_btnActionPerformed
        try{
           DBCollection get_product = db.getCollection("MS_PRODUCT");
        if(edit==true){  
            if(product_name_txt.getText().isEmpty()){
                throw new NullPointerException();
            }
           BasicDBObject searchFields = new BasicDBObject("MS_PRODUCT_ID",product_id);
           BasicDBObject updateFields = new BasicDBObject();
           BasicDBObject partner_address = new BasicDBObject();
           BasicDBObject setQuery = new BasicDBObject();
           String type = null;
        try{
           updateFields.append("MS_PRODUCT_NAME",product_name_txt.getText());
           updateFields.append("MS_PRODUCT_PRICE",Double.parseDouble(product_price_txt.getText()));
           updateFields.append("MS_PRODUCT_AMOUNT",Double.parseDouble(product_amount_txt.getText()));
           updateFields.append("MS_PRODUCT_TYPE",partner_address);
           updateFields.append("MS_PARTNER_ID",find_partner(product_partner_combo.getSelectedItem().toString()));
                /*

                  index 0 = เครื่องดื่ม
                  index 1 = เบเกอรี่
                  index 2 = ของคาว

                */

                if(product_type_combo.getSelectedIndex()==1){
                   type = "Drink";
                }else if(product_type_combo.getSelectedIndex()==2){
                    type = "Bakery";
                }else if(product_type_combo.getSelectedIndex()==3){
                    type = "Meal";
                }
           setQuery.append("$set", updateFields);
           updateFields.append("MS_PRODUCT_TYPE",type);
           get_product.update(searchFields,setQuery);
           System.out.println("Success");
           clear_table((DefaultTableModel)product_table.getModel());
           get_collection_in_to_table();
           JOptionPane.showMessageDialog(null,"แก้ไขข้อมูลของสินค้าเรียบร้อยแล้วค่ะ");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(delete==true){
            get_product.remove(new BasicDBObject("MS_PRODUCT_ID",product_id));
            clear_table((DefaultTableModel)product_table.getModel());
            get_collection_in_to_table();
            JOptionPane.showMessageDialog(null,"ลบข้อมูลของสินค้าเรียบร้อยแล้วค่ะ");
        }
        }catch(NullPointerException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่ครบ\nกรุณากรอกข้อมูลใหม่ด้วยค่ะ");
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_product_btnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
            this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_product_tableMouseClicked
        get_data_from_table();
    }//GEN-LAST:event_product_tableMouseClicked

    private void product_type_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_type_comboActionPerformed
            set_productcombo(product_type_combo.getSelectedIndex());
    }//GEN-LAST:event_product_type_comboActionPerformed

    private void delete_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_radioActionPerformed
        check_function();
    }//GEN-LAST:event_delete_radioActionPerformed

    private void edit_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_radioActionPerformed
        check_function();
    }//GEN-LAST:event_edit_radioActionPerformed

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
            java.util.logging.Logger.getLogger(Edit_Product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_Product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_Product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_Product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_Product().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton delete_radio;
    private javax.swing.JRadioButton edit_radio;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField product_amount_txt;
    private javax.swing.JButton product_btn;
    private javax.swing.JTextField product_name_txt;
    private javax.swing.JPanel product_panel;
    private javax.swing.JComboBox<String> product_partner_combo;
    private javax.swing.JTextField product_price_txt;
    private javax.swing.JTable product_table;
    private javax.swing.JComboBox<String> product_type_combo;
    // End of variables declaration//GEN-END:variables
}
