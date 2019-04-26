
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
Variable v = new Variable();//สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
    MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
    DB db; //กำหนดตัวแปรประเภท DB
    DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
//---------------------Boolean------------------------------
        boolean edit = true;    //|--สร้างตัวแปร edit เพื่อเช็คการทำงาน
        boolean delete = false; //|--สร้างตัวแปร delete เพื่อเช็คการทำงาน
    List<DBObject>menu_component = new ArrayList<>(); //สร้างListเพื่อเก็บข้อมูลของส่วนประกอบของเมนู
    //----------------------------เช็คดับเบิ้ลคลิ๊กของตาราง--------------------------------
    String menu_component_doubleclick = null;
    String adding_menu_component_doubleclick = null;
    /**
     * Creates new form Customer
     */
    public Edit_Menu() {
        initComponents();
        getconnect(); //เชื่อมต่อ Database
        get_collection_in_to_menu_table();
        get_collection_in_to_product_table();
    }
    public void getconnect(){ //เชื่อมต่อ Database
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            mongo = new MongoClient("localhost",27017);//เชื่อมต่อ Database Mongodb IP:localhost Port:27017
            db = mongo.getDB("InthaninDB");//ดึงข้อมูลฐานข้อมูล
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
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
            menu_table.clearSelection(); //ยกเลิกการเลือกในตารางเมนู
            confirm_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            menu_clear();
        }
    }
    
        public void menu_clear(){ //เคลียร์ข้อมูลในฟิลด์หน้าต่างเมนู
            menu_name_txt.setText("");
            menu_price_txt.setText("");
            menu_component.clear();
            clear_table((DefaultTableModel)menu_component_table.getModel()); //เคลียร์ตารางส่วนประกอบของเมนู
            clear_table((DefaultTableModel)menu_table.getModel()); //เคลียร์ตารางสินค้า
            get_collection_in_to_menu_table(); //ดึงข้อมูลมาใส่ในตารางสินค้า
        }
        
    public DBObject finding_product(int id,double product_amount){ //ค้นหาข้อมูลของสินค้า
        DBCollection product = db.getCollection("MS_PRODUCT"); // ดึงข้อมูลจาก collection ที่ชื่อ MS_PRODUCT
        BasicDBObject find_product = new BasicDBObject("MS_PRODUCT_ID",id); //สร้างObjectชื่อ find_product เพื่อเก็บข้อมูลที่จะนำไปค้นหาจาก Database
        DBCursor finding_product = product.find(find_product); // ค้นหาข้อมูลของสินค้าในcollection(MS_PRODUCT)ทั้งหมด
        DBObject product_json = null; //สร้างตัวแปรในการเก็บข้อมูลของสินค้า
        while(finding_product.hasNext()){//สร้างลูป while โดยจะสิ้นสุดการทำงานเมื่อไม่มีข้อมูลหลงเหลือแล้วใน Collection
            product_json = finding_product.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปรชื่อ product_json
            product_json.removeField("_id"); //ลบ Id Object ของ json
            product_json.put("MS_PRODUCT_AMOUNT", product_amount); //เปลี่ยนจำนวนของสินค้าที่จะใช้ต่อเมนู
        }
        System.err.println(product_json);
        return product_json; //คืนค่าเป็นข้อมูลของสินค้า
    }

    public void clear_table(DefaultTableModel table){ //ลบข้อมูลทั้งหมดในตาราง
        while(table.getRowCount()>0){ //สร้างลูป while โดยมีเงื่อนไขคือ จำนวนแถวในตารางจะต้องมากกว่า 0
            table.removeRow(0); //ลบข้อมูลในแถวที่ 1
        }
    }
    public void get_collection_in_to_menu_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางเมนู
        DefaultTableModel table = (DefaultTableModel)menu_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ menu_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        DBCollection get_menu = db.getCollection("MS_MENU");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
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
    
    public void get_menu_component_table(){ //เพิ่มข้อมูลลงตารางจาก List ของวัตถุดิบ
        DefaultTableModel table = (DefaultTableModel)menu_component_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ table มาเก็บไว้ในตัวแปร
        Object[] row = new Object[3]; //สร้างArrayของObjectขึ้นมาขนาด 3
        for(Object eiei:menu_component){ //สร้างลูป for-each ของ List ของวัตถุดิบ
            DBObject e = (DBObject)eiei; //ดึงข้อมูลนั้นๆของวัตถุดิบมาเก็บใส่ตัวแปรใหม่เพื่อป้องกันข้อผิดพลาด
            row[0] = e.get("MS_PRODUCT_ID"); //เพิ่มข้อมูลของรหัสสินค้าลงในช่องที่ 1 ของ Array
            row[1] = e.get("MS_PRODUCT_NAME"); //เพิ่มข้อมูลชื่อของสินค้าลงในช่องที่ 2 ของ Array
            row[2] = e.get("MS_PRODUCT_AMOUNT"); //เพิ่มข้อมูลจำนวนของสินค้าลงในช่องที่ 3 ของ Array
            table.addRow(row); //เพิ่มแถวลงในตาราง
        }
    }

  public void get_collection_in_to_product_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางเมนู
        DefaultTableModel table = (DefaultTableModel)product_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ product_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        DBCollection get_menu = db.getCollection("MS_PRODUCT");//ดึงข้อมูลจากCollectionของลูกค้ามาใส่ในตัวแปร (สินค้า)
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
  
  
  
  public DBObject finding_menu(int id){ //ค้นหาข้อมูลของเมนูจากรหัสเมนู
       DBObject menu_json = null; //สร้างตัวแปรเปล่าของข้อมูลเพื่อใช้ในการเก็บข้อมูลเมนู
      try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
          DBCollection get_menu = db.getCollection("MS_MENU");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
          BasicDBObject find_menu = new BasicDBObject("MS_MENU_ID",id);
          DBCursor finding_menu = get_menu.find(find_menu); // ค้นหาข้อมูลในcollection(MS_MENU)จากรหัสเมนู
          while(finding_menu.hasNext()){
              menu_json = finding_menu.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ menu_json
              menu_name_txt.setText(menu_json.get("MS_MENU_NAME").toString()); //เซ็ตข้อมูลของราคาใส่ช่องกรอกข้อมูล
              menu_price_txt.setText(menu_json.get("MS_MENU_PRICE").toString());//เซ็ตข้อมูลของราคาใส่ช่องกรอกข้อมูล
              BasicDBList list = (BasicDBList)(menu_json.get("MS_MENU_PRODUCT")); 
              for(Object el: list) { //for each loop
                DBObject product = (DBObject)el;
                menu_component.add(product); //เพิ่มข้อมูลลงในList
                //เพิ่มข้อมูลลงในตาราง
                DefaultTableModel model = (DefaultTableModel)menu_component_table.getModel();
                Object[] row = new Object[3];
                DBObject parse_product = (DBObject)el;
                row[0] = parse_product.get("MS_PRODUCT_ID"); //รหัสสินค้า
                row[1] = parse_product.get("MS_PRODUCT_NAME"); //ชื่อสินค้า
                row[2] = parse_product.get("MS_PRODUCT_AMOUNT");//จำนวนสินค้า
                model.addRow(row);
            }
              //System.out.println(menu_json);
          }
          //System.err.println("1"+menu_component.get(0));
          //System.err.println("2"+menu_component.get(1));
      }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
      }
       return menu_json; //คืนค่าเป็นข้อมูลของข้อมูลของเมนู
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
        menu_component.clear(); //เคลียร์List ของวัตถุดิบ
        clear_table((DefaultTableModel)menu_component_table.getModel());//เคลียร์ข้อมูลในตารางวัตถุดิบ
        finding_menu(Integer.parseInt(menu_table.getValueAt(menu_table.getSelectedRow(),0).toString())); //ค้นหาข้อมูลของเมนูจากรหัสเมนู
    }//GEN-LAST:event_menu_tableMouseClicked

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
        if(!menu_table.getSelectionModel().isSelectionEmpty()){ //เช็คว่าตารางเมนูถูกเลือกหรือไม่
           DBCollection get_menu = db.getCollection("MS_MENU"); //ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
           int menu_id = Integer.parseInt(menu_table.getValueAt(menu_table.getSelectedRow(), 0).toString()); //สร้างตัวแปรเพื่อเก็บรหัสเมนูจากตาราง
        if(edit==true){ // ถ้าหากว่าใช้งานฟังก์ชั่นการแก้ไข
        if(JOptionPane.showConfirmDialog(null,"กรุณายืนยันการแก้ไขข้อมูล/การลบข้อมูลด้วยค่ะ","",YES_NO_OPTION)==YES_OPTION){ //สร้างหน้าต่างขึ้นมาเพื่อยืนยันการทำรายการ
            // ถ้าหากว่ายืนยัน จะทำการแก้ไขข้อมูลโดยการน้ำข้อมูลทั้งหมดแก้ไขในข้อมูลเดิมโดยใช้การค้นหาจากรหัสเมนู
            BasicDBObject searchFields = new BasicDBObject("MS_MENU_ID",menu_id); //สร้างObjectชื่อ searchFields เพื่อเก็บข้อมูลที่จะนำไปค้นหา
            BasicDBObject updateFields = new BasicDBObject(); //สร้างObjectชื่อ updateFields เพื่อเก็บข้อมูลที่จะนำไปแก้ไขจากข้อมูลเดิม
            BasicDBObject setQuery = new BasicDBObject();//สร้างObjectชื่อ setQuery เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
        try{//เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
           updateFields.append("MS_MENU_NAME",menu_name_txt.getText()); //อัพเดทชื่อเมนู
           updateFields.append("MS_MENU_PRICE",Integer.parseInt(menu_price_txt.getText())); //อัพเดทราคา
           updateFields.append("MS_MENU_PRODUCT",menu_component); //อัพเดทวัตถุดิบที่ใช้
           setQuery.append("$set", updateFields); //ตั้งค่าฟังก์ชั่นที่จะใช้กับข้อมูล
           get_menu.update(searchFields,setQuery); //ทำการอัพเดทข้อมูลที่ตรวจพบใน database
           System.out.println("Success"); //แสดงผลทางหน้าจอหลังทำรายการสำเร็จ
           menu_component.clear(); //ลบข้อมูลในListของวัตถุดิบที่ใช้
           product_table.clearSelection(); //ยกเลิกการเลือกของตาราง
           clear_table((DefaultTableModel)menu_table.getModel()); //ลบข้อมูลของตารางเมนู
           clear_table((DefaultTableModel)menu_component_table.getModel()); //ลบข้อมูลของตารางวัตถุดิบ
           menu_clear(); //ลบข้อมูลที่กรอกอยู่ทั้งหมดในหน้าจอ
           get_collection_in_to_menu_table(); //ดึงข้อมูลของเมนูจากDatabaseมาใส่ในตาราง
           JOptionPane.showMessageDialog(null,"ทำการแก้ไขเมนูสำเร็จค่ะ"); //แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        }else{ //ถ้าหากว่าไม่ยืนยันการแก้ไขข้อมูล
            JOptionPane.showMessageDialog(null,"ทำการยกเลิกรายการเรียบร้อยแล้วค่ะ"); //แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }
        }else if(delete==true){ //ถ้าหากว่าฟังก์ชั่นที่ใช้เป็นฟังก์ชั่นการลบ
            if(JOptionPane.showConfirmDialog(null,"กรุณายืนยันการแก้ไขข้อมูล/การลบข้อมูลของเมนูด้วยค่ะ","",YES_NO_OPTION)==YES_OPTION){ //สร้างหน้าต่างขึ้นมาเพื่อยืนยันการทำรายการ
            get_menu.remove(new BasicDBObject("MS_MENU_ID",menu_id)); //ลบข้อมูลเมนูในDatabaseโดยใช้รหัสเมนู
            menu_clear();//ลบข้อมูลที่กรอกอยู่ทั้งหมดในหน้าจอ
            JOptionPane.showMessageDialog(null,"ลบข้อมูลของเมนูเรียบร้อยแล้วค่ะ"); //แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            }
        }
        }else{ //ถ้าหากว่ายังไม่มีการเลือกเมนูที่จะทำรายการและมีการกดปุ่มนี้
            if(edit==true||delete==true)
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้เลือกเมนูที่จะแก้ไขเลยค่ะ\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE); //แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void menu_component_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_component_tableMouseClicked
      //เมื่อมีการคลิ๊กเกิดขึ้นที่ตารางวัตถุดิบ
      
        if(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString().equals(menu_component_doubleclick)){  //ฟังก์ชั่นดับเบิ้ลคลิ๊ก
          /*
                    ใช้วิธีเทียบความเหมือนของสองตัวแปร ถ้าหากมีการคลิ๊กที่ตาราง 1 ครั้ง จะทำการตั้งค่าให้ตัวแปรมีค่าเดียวกันกับค่าที่คลิ๊กเพื่อใช้ในการคลิ๊กอีกครั้ง
                    เมื่อคลิ๊กอีกครั้งแล้วเปรียบเทียบกันว่าตัวแปรทั้งสองมีค่าเหมือนกันหรือไม่ จะทำให้ตัวแปรทั้งสองมีค่าเหมือนกัน จึงreturnกลับมาเป็น true
          */
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
          int choice=0; //ตัวแปรตัวเลือก
          /*
                1 - แก้ไขจำนวนวัตถุดิบ
                2 - ลบวัตถุดิบของเมนูนั้นๆ
                **ถ้าหากว่าเมนูนั้นๆเหลือวัตถุดิบเพียงชนิดเดียวจะทำให้ไม่สามารถลบวัตถุดิบได้
          */
          try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
          choice = Integer.parseInt(JOptionPane.showInputDialog(null,"1.แก้ไขจำนวนวัตถุดิบ\n"
                                                                    +"2.ลบวัตถุดิบที่ใช้\n"
                                                                    +"\nกรุณากรอกหัวข้อที่คุณต้องการจะดำเนินการค่ะ"));
          }catch(NumberFormatException e){ 
              /*
                   เมื่อมีการทำงานผิดพลาดของตัวเลขยกตัวอย่างเช่น การใส่จำนวนไม่ถูกต้อง เพราะตัวแปรที่รองรับในการใช้งานจะเป็นตัวแปรจำนวนเต็มเท่านั้น
                   ถ้าหากมีการกรอกจำนวนจริงหรือตัวอักษรจะทำให้เกิดข้อผิดพลาดขึ้น
              */
              e.printStackTrace(); //แสดงข้อผิดพลาดทางหน้าจอ
              JOptionPane.showMessageDialog(null,"คุณเลือกตัวเลือกไม่ถูกต้อง\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
              menu_component_doubleclick =""; //เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก
              menu_component_table.clearSelection(); //เคลียร์การเลือกของตารางนี้
          }
         /******************************************ฟังก์ชั่นการแก้ไขวัตถุดิบ***********************************************************************************************************/
         /**/ if(choice==1){ 
         /**/ int product_id = Integer.parseInt(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString()); //ดึงข้อมูลรหัสวัตถุดิบจากตารางมาเก็บใส่ในตัวแปร
         /**/ String product_name = menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 1).toString(); //ดึงชื่อของวัตถุดิบจากตารางมาเก็บใส่ในตัวแปร
         /**/ double product_amount = Double.parseDouble(menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 2).toString()); //ดึงจำนวนของวัตถุดิบจากตารางมาเก็บใส่ในตัวแปร
         /**/     try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
         /**/         double amount = Double.parseDouble(JOptionPane.showInputDialog(null,"ชื่อสินค้า: "+product_name
         /**/                                                                           + "\nจำนวนสินค้าที่ใช้ในขณะนี้: "+product_amount
         /**/                                                                           + "\n\nกรุณากรอกจำนวนใหม่ที่คุณต้องการจะแก้ไข"));
         /**/          //สร้างตัวแปรจำนวนจริงเพื่อเก็บข้อมูลจำนวน 
         /**/         for(Object product:menu_component){ //สร้างลูป for-each ของวัตถุดิบในเมนูนั้นๆ จาก List ของวัตถุดิบ
         /**/             DBObject product_json = (DBObject)product; //ดึงข้อมูลนั้นๆของวัตถุดิบมาเก็บใส่ตัวแปรใหม่เพื่อป้องกันข้อผิดพลาด
         /**/             if((int)product_json.get("MS_PRODUCT_ID")==product_id){ //ถ้าหากว่ารหัสของวัตถุดิบตรงกัน
         /**/             product_json.put("MS_PRODUCT_AMOUNT",amount); //ให้ทำการแก้ไขจำนวนของวัตถุดิบนั้นๆ
         /**/             product = product_json; //คืนค่ากลับไปยังListของวัตถุดิบ
         /**/             break;
         /**/             }
         /**/         }
         /**/         
         /**/          clear_table((DefaultTableModel)menu_component_table.getModel()); //เคลียร์ตารางวัตถุดิบทั้งหมด
         /**/          get_menu_component_table(); //ดึงข้อมูลจากDatabaseมาใส่ในตาราง
         /**/          menu_component_doubleclick =""; //เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก
         /**/          menu_component_table.clearSelection(); //เคลียร์การเลือกของตารางนี้
         /**/     }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
         /**/        e.printStackTrace();e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
         /**/        JOptionPane.showMessageDialog(null,"คุณกรอกจำนวนที่จะแก้ไขไม่ถูกต้อง\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
         /**/          menu_component_doubleclick =""; //เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก
         /**/          menu_component_table.clearSelection(); //เคลียร์การเลือกของตารางนี้
         /**/     }                                 
         /***************************ฟังก์ชั่นการลบวัตถุดิบ********************************************************************************************************************/
          }else if(choice==2){ 
              if(menu_component.size()<=1){ //ถ้าหากว่าวัตถุดิบมีน้อยกว่าหรือเท่ากับ 1 จำนวน
                  throw new ArithmeticException(); //คืนค่ากลับให้เป็นข้อผิดพลาดทางการคำนวนทางคณิตศาสตร์
              }
              menu_component.remove(menu_component_table.getSelectedRow()); //ลบข้อมูลในตารางจากแถวที่ถูกเลือก
              clear_table((DefaultTableModel)menu_component_table.getModel()); //เคลียร์ตารางวัตถุดิบทั้งหมด
              get_menu_component_table();//ดึงข้อมูลจากDatabaseมาใส่ในตาราง
          }
          }catch(ArithmeticException e){ //ถ้าหากเกิดข้อผิดพลาดจากการคำนวนทางคณิตศาสตร์เกิดขึ้น
              e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
              JOptionPane.showMessageDialog(null,"คุณไม่สามารถลบวัตถุดิบทั้งหมดได้\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE); //แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
          }catch(Exception e){ //ถ้าหากเกิดข้อผิดพลาดนอกเหนือจากนั้นทั้งหมด 
              e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ 
              menu_component_doubleclick ="";//เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก
          }
      }else{ //ถ้าหากว่าตัวแปรที่กำหนดไว้มีค่าไม่ตรงกับตัวแปรในตาราง
          menu_component_doubleclick = menu_component_table.getValueAt(menu_component_table.getSelectedRow(), 0).toString(); //ตั้งค่าตัวแปรที่กำหนดไว้ให้มีค่าตรงกับตัวแปรในตารางแถวที่เลือกช่องที่1
      }
    }//GEN-LAST:event_menu_component_tableMouseClicked

    private void product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_product_tableMouseClicked
        if((product_table.getValueAt(product_table.getSelectedRow(),0).toString().equals(adding_menu_component_doubleclick))){ //ฟังก์ชั่นดับเบิ้ลคลิ๊ก
            /*
                    ใช้วิธีเทียบความเหมือนของสองตัวแปร ถ้าหากมีการคลิ๊กที่ตาราง 1 ครั้ง จะทำการตั้งค่าให้ตัวแปรมีค่าเดียวกันกับค่าที่คลิ๊กเพื่อใช้ในการคลิ๊กอีกครั้ง
                    เมื่อคลิ๊กอีกครั้งแล้วเปรียบเทียบกันว่าตัวแปรทั้งสองมีค่าเหมือนกันหรือไม่ จะทำให้ตัวแปรทั้งสองมีค่าเหมือนกัน จึงreturnกลับมาเป็น true
            */
            adding_menu_component_doubleclick = ""; //เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก 
            int productid = Integer.parseInt(product_table.getValueAt(product_table.getSelectedRow(),0).toString()); //กำหนดตัวแปรเพื่อใช้ในการเก็บรหัสสินค้า
            String productname = product_table.getValueAt(product_table.getSelectedRow(),1).toString(); //กำหนดัวแปรเพื่อใช้ในการเก็บชื่อของสินค้า
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            for(Object product:menu_component){ //สร้างลูป for-each ของวัตถุดิบในเมนูนั้นๆ จาก List ของวัตถุดิบ
                DBObject product_json = (DBObject)product; //ดึงข้อมูลนั้นๆของวัตถุดิบมาเก็บใส่ตัวแปรใหม่เพื่อป้องกันข้อผิดพลาด
                if(Integer.parseInt(product_table.getValueAt(product_table.getSelectedRow(),0).toString())==(int)product_json.get("MS_PRODUCT_ID")){ //เช็คว่าข้อมูลรหัสสินค้าที่ดึงมาตรงกับตารางที่เลือก
                    //ฟังก์ชั่นนี้ใช้ในการตรวจจับว่าวัตถุดิบที่จะเพิ่มเข้าไปนี้ มีอยู่แล้วหรือไม่ในเมนูนั้นๆ ถ้าหากว่ามีอยู่แล้วจะคืนค่าการทำงานผิดพลาด แต่ถ้าไม่มีอยู่แล้วจะอนุญาติให้เพื่มวัตถุดิบ   

                    //ถ้าหากตรงให้ทำการคืนค่าเป็นการทำงานผิดพลาด
                    throw new IllegalAccessException();
                }else{ //ถ้าหากตรวจพบว่าไม่มีวัตถุดับใดๆ
                    try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                    double menu_per_amount = Double.parseDouble(JOptionPane.showInputDialog(null,"จำนวนที่คุณต้องการจะใช้ต่อเมนู 1 รายการ\nRange ของจำนวนจะอยู่ตั้งแต่ 0.01-9.99 หน่วย"));
                    //สร้างหน้าต่างขึ้นมาเพื่อใช้ในการเก็บข้อมูลจำนวนวัตถุดิบที่ใช้ต่อเมนู
                    if(menu_per_amount<=0||menu_per_amount>=10){ //ถ้าหากว่ากรอกจำนวนที่น้อยกว่า 0 หรือว่า จำนวนที่มากกว่า10
                    JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE); //แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
                    }else{ //แต่ถ้าอยู่นอกเงื่อนไขนั้น 0.01-9.99
                    if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า: "+productname+" \n"+
                                              "จำนวนที่จะใช้ต่อเมนู: "+ menu_per_amount+" \n"+
                                              "\n ยืนยืนการเพิ่มสินค้าของเมนูนี้","",YES_NO_OPTION)==YES_OPTION){ 
                        //สร้างหน้าต่างขึ้นมาเพื่อยืนยันการทำรายการเพิ่มวัตถุดิบ
                     menu_component.add(finding_product(productid,menu_per_amount)); //เพิ่มข้อมูลลงในListของวัตถุดิบ
                     clear_table((DefaultTableModel)menu_component_table.getModel()); //เคลียร์ตารางวัตถุดิบทั้งหมด
                     get_menu_component_table(); //ดึงข้อมูลจากDatabaseของข้อมูลวัตถุดิบ
                     product_table.clearSelection();//เคลียร์การเลือกของเมนูสินค้า
                     }
                     }
                 }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                     e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                 }
            }
            }
            }catch(IllegalAccessException e){ //ข้อผิดพลาดที่ใช้ในการตรวจสอบวัตถุดิบที่ซ้ำกัน
                JOptionPane.showMessageDialog(null,"คุณไม่สามารถเพิ่มสินค้าที่ซ้ำกันได้\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
              menu_component_doubleclick ="";//เคลียร์ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊ก
                     product_table.clearSelection();//เคลียร์การเลือกของเมนูสินค้า
            }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                     e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }else{//ถ้าหากว่าตัวแปรที่กำหนดไว้มีค่าไม่ตรงกับตัวแปรในตาราง
            adding_menu_component_doubleclick = product_table.getValueAt(product_table.getSelectedRow(),0).toString();//ตั้งค่าตัวแปรที่กำหนดไว้ให้มีค่าตรงกับตัวแปรในตารางแถวที่เลือกช่องที่1
        }
    }//GEN-LAST:event_product_tableMouseClicked

    private void delete_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_radioActionPerformed
        check_function();//เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
    }//GEN-LAST:event_delete_radioActionPerformed

    private void edit_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_radioActionPerformed
        check_function();//เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
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
