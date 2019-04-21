
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
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
public class Edit_Menu extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    boolean edit = true;
    boolean delete = false;
    List<DBObject>menu_component = new ArrayList<>();
    String menu_component_doubleclick = null;
    String adding_menu_component_doubleclick = null;
    /**
     * Creates new form Customer
     */
    public Edit_Menu() {
        initComponents();
        getconnect();
        get_collection_in_to_menu_table();
        get_collection_in_to_product_table();
    }
    public void getconnect(){
        try{
            mongo = new MongoClient("localhost",27017);
            db = mongo.getDB("InthaninDB");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
        public void check_function(){ //เช็คการทำงานในขณะนี้ (ลบ/แก้ไข)
        if(edit_radio.isSelected()){ //ถ้าหากว่าตัวเลือกแก้ไขได้ถูกเลือก
            menu_panel.setVisible(true); //หน้าต่างแก้ไขข้อมูลจะปรากฏขึ้น
            edit=true; //แก้ไขค่าตัวแปรedit ให้มีค่า true
            delete=false; //แก้ไขค่าตัวแปรdelete ให้มีค่า false
            confirm_btn.setText("ยืนยันการแก้ไข"); //ตั้งการแสดงผลที่ปุ่ม
            menu_clear();
        }else if(delete_radio.isSelected()){//ถ้าหากว่าตัวเลือกลบได้ถูกเลือก
            menu_panel.setVisible(false); //หน้าต่างแก้ไขข้อมูลจะถูกซ่อน
            menu_table.clearSelection(); //ยกเลิกการเลือกในตารางลูกค้า
            confirm_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            menu_clear();
        }
    }
    
        public void menu_clear(){
            menu_name_txt.setText("");
            menu_price_txt.setText("");
            menu_component.clear();
            clear_table((DefaultTableModel)menu_component_table.getModel());
            clear_table((DefaultTableModel)menu_table.getModel());
            get_collection_in_to_menu_table();
        }
        
    public DBObject finding_product(int id,double product_amount){
        DBCollection product = db.getCollection("MS_PRODUCT");
        BasicDBObject find_product = new BasicDBObject("MS_PRODUCT_ID",id);
        DBCursor finding_product = product.find(find_product);
        DBObject product_json = null;
        while(finding_product.hasNext()){
            product_json = finding_product.next();
            product_json.removeField("_id");
            product_json.put("MS_PRODUCT_AMOUNT", product_amount);
        }
        System.err.println(product_json);
        return product_json;
    }

    public void clear_table(DefaultTableModel table){ //ลบข้อมูลทั้งหมดในตาราง
        while(table.getRowCount()>0){ //สร้างลูป while โดยมีเงื่อนไขคือ จำนวนแถวในตารางจะต้องมากกว่า 0
            table.removeRow(0); //ลบข้อมูลในแถวที่ 1
        }
    }
    public void get_collection_in_to_menu_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางลูกค้า
        DefaultTableModel table = (DefaultTableModel)menu_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ menu_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        DBCollection get_menu = db.getCollection("MS_MENU");
        DBCursor cursor = get_menu.find().sort(new BasicDBObject("MS_MENU_ID", 1)); // ค้นหาข้อมูลในcollection(MS_MENU)ทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject menu_json = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
                //System.out.println(partner.get("MS_CUSTOMER_NAME"));
                try{ //สร้างลูป do-while
                row[0] = menu_json.get("MS_MENU_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสเมนู
                row[1] = menu_json.get("MS_MENU_NAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของเมนู
                row[2] = menu_json.get("MS_MENU_PRICE").toString(); //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของราคาของเมนู
                table.addRow(row); //เพิ่มแถวข้อมูลของตาราง menu_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
                }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                   e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                }
            //table.addRow(row);*/
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }while(cursor.hasNext()); //จะจบการทำงานเมื่อเงื่อนไขเป็น false (ไม่มีข้อมูลตัวถัดไป)
    }
    
    public void set_menu_component_table(){
        DefaultTableModel table = (DefaultTableModel)menu_component_table.getModel();
        Object[] row = new Object[3];
        for(Object eiei:menu_component){
            DBObject e = (DBObject)eiei;
            row[0] = e.get("MS_PRODUCT_ID");
            row[1] = e.get("MS_PRODUCT_NAME");
            row[2] = e.get("MS_PRODUCT_AMOUNT");
            table.addRow(row);
        }
    }

  public void get_collection_in_to_product_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางลูกค้า
        DefaultTableModel table = (DefaultTableModel)product_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ product_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 6 ช่อง
        DBCollection get_menu = db.getCollection("MS_PRODUCT");
        DBCursor cursor = get_menu.find(); // ค้นหาข้อมูลในcollection(MS_CUSTOMER)ทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject menu_json = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
                //System.out.println(partner.get("MS_CUSTOMER_NAME"));
                try{ //สร้างลูป do-while
                row[0] = menu_json.get("MS_PRODUCT_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของสินค้า
                row[1] = menu_json.get("MS_PRODUCT_NAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของสินค้า
                row[2] = menu_json.get("MS_PRODUCT_AMOUNT").toString(); //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของจำนวนของสินค้า
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
  
  
  
  public DBObject finding_menu(int id){
       DBObject menu_json = null;
      try{
          DBCollection get_menu = db.getCollection("MS_MENU");
          BasicDBObject find_menu = new BasicDBObject("MS_MENU_ID",id);
          DBCursor finding_menu = get_menu.find(find_menu);
          menu_json = null;
          while(finding_menu.hasNext()){
              menu_json = finding_menu.next();
              menu_name_txt.setText(menu_json.get("MS_MENU_NAME").toString());
              menu_price_txt.setText(menu_json.get("MS_MENU_PRICE").toString());
              BasicDBList list = (BasicDBList)(menu_json.get("MS_MENU_PRODUCT"));
              for(Object el: list) {
                DBObject product = (DBObject)el;
                menu_component.add(product);
                DefaultTableModel model = (DefaultTableModel)menu_component_table.getModel();
                Object[] row = new Object[3];
                DBObject parse_product = (DBObject)el;
                row[0] = parse_product.get("MS_PRODUCT_ID");
                row[1] = parse_product.get("MS_PRODUCT_NAME");
                row[2] = parse_product.get("MS_PRODUCT_AMOUNT");
                model.addRow(row);
            }
              System.out.println(menu_json);
          }
          //System.err.println("1"+menu_component.get(0));
          //System.err.println("2"+menu_component.get(1));
      }catch(Exception e){
          e.printStackTrace();
      }
       return menu_json;
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
        cancel_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        menu_table = new javax.swing.JTable();
        delete_radio = new javax.swing.JRadioButton();
        edit_radio = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        confirm_btn = new javax.swing.JButton();
        menu_panel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        menu_component_table = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        product_table = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        menu_name_txt = new javax.swing.JTextField();
        menu_price_txt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างแก้ไขข้อมูลลูกค้า");
        setMinimumSize(new java.awt.Dimension(713, 408));
        setPreferredSize(new java.awt.Dimension(915, 640));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cancel_btn.setText("ยกเลิก");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });
        getContentPane().add(cancel_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 110, 50));

        menu_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสเมนู", "ชื่อเมนู", "ราคา"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        menu_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(menu_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, 250));

        buttonGroup1.add(delete_radio);
        delete_radio.setText("ลบข้อมูลเมนู");
        delete_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_radioActionPerformed(evt);
            }
        });
        getContentPane().add(delete_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        buttonGroup1.add(edit_radio);
        edit_radio.setSelected(true);
        edit_radio.setText("แก้ไขข้อมูลเมนู");
        edit_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_radioActionPerformed(evt);
            }
        });
        getContentPane().add(edit_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(781, 334));
        jPanel1.setPreferredSize(new java.awt.Dimension(781, 334));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลเมนู");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 5, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 30));

        confirm_btn.setText("ยืนยันแก้ไข");
        confirm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_btnActionPerformed(evt);
            }
        });
        getContentPane().add(confirm_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 520, 130, 50));

        menu_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu_component_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "จำนวนที่ใช้"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        menu_component_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_component_tableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(menu_component_table);

        menu_panel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 250, 370, 180));

        product_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "จำนวนที่มีอยู่"
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
        jScrollPane4.setViewportView(product_table);

        menu_panel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 370, 180));

        jLabel2.setText("ตารางสินค้าของเมนู");
        menu_panel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 230, -1, -1));

        jLabel3.setText("ตารางสินค้าทั้งหมด");
        menu_panel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("*ดับเบิ้ลคลิ๊กเพื่อลบ/แก้ไขสินค้า");
        menu_panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 430, 150, -1));

        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("*ดับเบิ้ลคลิ๊กเพื่อเพิ่มสินค้า");
        menu_panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 210, 140, -1));

        jLabel8.setText("ชื่อเมนู:");
        menu_panel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, -1, -1));
        menu_panel.add(menu_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 190, -1));
        menu_panel.add(menu_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 380, 60, -1));

        jLabel7.setText("ราคา:");
        menu_panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, -1, -1));

        getContentPane().add(menu_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 880, 460));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void menu_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_tableMouseClicked
        menu_component.clear();
        clear_table((DefaultTableModel)menu_component_table.getModel());
        finding_menu(Integer.parseInt(menu_table.getValueAt(menu_table.getSelectedRow(),0).toString()));
    }//GEN-LAST:event_menu_tableMouseClicked

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
        if(!menu_table.getSelectionModel().isSelectionEmpty()){
           DBCollection get_menu = db.getCollection("MS_MENU");
           int menu_id = Integer.parseInt(menu_table.getValueAt(menu_table.getSelectedRow(), 0).toString());
        if(edit==true){
        if(JOptionPane.showConfirmDialog(null,"กรุณายืนยันการแก้ไขข้อมูล/การลบข้อมูลด้วยค่ะ","",YES_NO_OPTION)==YES_OPTION){
            BasicDBObject searchFields = new BasicDBObject("MS_MENU_ID",menu_id);
            BasicDBObject updateFields = new BasicDBObject();
            BasicDBObject setQuery = new BasicDBObject();
        try{
           updateFields.append("MS_MENU_ID",menu_id);
           updateFields.append("MS_MENU_NAME",menu_name_txt.getText());
           updateFields.append("MS_MENU_PRICE",Integer.parseInt(menu_price_txt.getText()));
           updateFields.append("MS_MENU_PRODUCT",menu_component);
           setQuery.append("$set", updateFields);
           get_menu.update(searchFields,setQuery);
           System.out.println("Success");
           menu_component.clear();
           product_table.clearSelection();
           clear_table((DefaultTableModel)menu_table.getModel());
           clear_table((DefaultTableModel)menu_component_table.getModel());
           menu_clear();
           get_collection_in_to_menu_table();
           JOptionPane.showMessageDialog(null,"ทำการแก้ไขเมนูสำเร็จค่ะ");
        }catch(Exception e){
            e.printStackTrace();
        }
        }else{
            JOptionPane.showMessageDialog(null,"ทำการยกเลิกรายการเรียบร้อยแล้วค่ะ");
        }
        }else if(delete==true){
            if(JOptionPane.showConfirmDialog(null,"กรุณายืนยันการแก้ไขข้อมูล/การลบข้อมูลของเมนูด้วยค่ะ","",YES_NO_OPTION)==YES_OPTION){
            get_menu.remove(new BasicDBObject("MS_MENU_ID",menu_id));
            menu_clear();
            JOptionPane.showMessageDialog(null,"ลบข้อมูลของเมนูเรียบร้อยแล้วค่ะ");
            }
        }
        }else{
            if(edit==true)
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้เลือกเมนูที่จะแก้ไขเลยค่ะ\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE);
            if(delete==true)
                JOptionPane.showMessageDialog(null,"คุณยังไม่ได้เลือกเมนูที่จะลบเลยค่ะ\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void menu_component_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_component_tableMouseClicked
      //System.out.println(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0));
      //System.out.println((menu_component_doubleclick));
        if(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString().equals(menu_component_doubleclick)){
          try{
          int choice=0;
          try{
          choice = Integer.parseInt(JOptionPane.showInputDialog(null,"1.แก้ไขจำนวนวัตถุดิบ\n"
                                                                    +"2.ลบวัตถุดิบที่ใช้\n"
                                                                    +"\nกรุณากรอกหัวข้อที่คุณต้องการจะดำเนินการค่ะ"));
          }catch(NumberFormatException e){
              e.printStackTrace();
              JOptionPane.showMessageDialog(null,"คุณเลือกตัวเลือกไม่ถูกต้อง\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);
              menu_component_doubleclick ="";
              menu_component_table.clearSelection();
          }
          if(choice==1){
          int product_id = Integer.parseInt(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString());
          String product_name = menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 1).toString();
          double product_amount = Double.parseDouble(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 2).toString());
              try{
                  double amount = Double.parseDouble(JOptionPane.showInputDialog(null,"ชื่อสินค้า: "+product_name
                                                                                    + "\nจำนวนสินค้าที่ใช้ในขณะนี้: "+product_amount
                                                                                    + "\n\nกรุณากรอกจำนวนใหม่ที่คุณต้องการจะแก้ไข"));
                  for(Object product:menu_component){
                      DBObject product_json = (DBObject)product;
                      if((int)product_json.get("MS_PRODUCT_ID")==product_id){
                      product_json.put("MS_PRODUCT_AMOUNT",amount);
                      product = product_json;
                      break;
                      }
                  }
                   clear_table((DefaultTableModel)menu_component_table.getModel());
                   set_menu_component_table();
                  System.out.println(menu_component);
                  menu_component_doubleclick ="";
                  menu_component_table.clearSelection();
              }catch(Exception e){
                  e.printStackTrace();
                  menu_component_doubleclick ="";
                  menu_component_table.clearSelection();
              }
          }else if(choice==2){
              if(menu_component.size()<=1){
                  throw new ArithmeticException();
              }
              menu_component.remove(menu_component_table.getSelectedRow());
              System.out.println(menu_component);
              clear_table((DefaultTableModel)menu_component_table.getModel());
              set_menu_component_table();
          }
          }catch(ArithmeticException e){
              JOptionPane.showMessageDialog(null,"คุณไม่สามารถลบวัตถุดิบทั้งหมดได้\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE);
          }catch(Exception e){
              e.printStackTrace();
               menu_component_doubleclick ="";
          }
      }else{
          menu_component_doubleclick = menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString();
      }
    }//GEN-LAST:event_menu_component_tableMouseClicked

    private void product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_product_tableMouseClicked
        if((product_table.getValueAt(product_table.getSelectedRow(),0).toString().equals(adding_menu_component_doubleclick))){
            adding_menu_component_doubleclick = "";
            int productid = Integer.parseInt(product_table.getValueAt(product_table.getSelectedRow(),0).toString());
            String productname = product_table.getValueAt(product_table.getSelectedRow(),1).toString();
            try{
            for(Object product:menu_component){
                DBObject product_json = (DBObject)product;
                if(Integer.parseInt(product_table.getValueAt(product_table.getSelectedRow(),0).toString())==(int)product_json.get("MS_PRODUCT_ID")){
                    throw new IllegalAccessException();
                }else{
                         try{
                    double menu_per_amount = Double.parseDouble(JOptionPane.showInputDialog(null,"จำนวนที่คุณต้องการจะใช้ต่อเมนู 1 รายการ\nRange ของจำนวนจะอยู่ตั้งแต่ 0.01-9.99 หน่วย"));
                    if(menu_per_amount<=0||menu_per_amount>=10){
                    JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);
                    }else{
                    if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า: "+productname+" \n"+
                                              "จำนวนที่จะใช้ต่อเมนู: "+ menu_per_amount+" \n"+
                                              "\n ยืนยืนการเพิ่มสินค้าของเมนูนี้","",YES_NO_OPTION)==YES_OPTION){
                        menu_component.add(finding_product(productid,menu_per_amount));
                        clear_table((DefaultTableModel)menu_component_table.getModel());
                        set_menu_component_table();
                        product_table.clearSelection();
                    }
                    }
                         }catch(Exception e){
                             
                         }
            }
            }
            }catch(IllegalAccessException e){
                JOptionPane.showMessageDialog(null,"คุณไม่สามารถเพิ่มสินค้าที่ซ้ำกันได้\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);
                adding_menu_component_doubleclick = "";
                product_table.clearSelection();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            adding_menu_component_doubleclick = product_table.getValueAt(product_table.getSelectedRow(),0).toString();
        }
    }//GEN-LAST:event_product_tableMouseClicked

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
            java.util.logging.Logger.getLogger(Edit_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancel_btn;
    private javax.swing.JButton confirm_btn;
    private javax.swing.JRadioButton delete_radio;
    private javax.swing.JRadioButton edit_radio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable menu_component_table;
    private javax.swing.JTextField menu_name_txt;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JTextField menu_price_txt;
    private javax.swing.JTable menu_table;
    private javax.swing.JTable product_table;
    // End of variables declaration//GEN-END:variables
}
