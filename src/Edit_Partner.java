import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;
import com.mongodb.MongoException;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author informatics
 */
public class Edit_Partner extends javax.swing.JFrame {
Variable v = new Variable(); //สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
DB db; //กำหนดตัวแปรประเภท DB
DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
//---------------------Integer-------------------------
        int partner_id = 0; //ตัวแปรที่ใช้เก็บรหัสของคู่ค้า
//---------------------Boolean------------------------------
        boolean edit = true;    //|--สร้างตัวแปร edit เพื่อเช็คการทำงาน
        boolean delete = false; //|--สร้างตัวแปร delete เพื่อเช็คการทำงาน
    /**
     * Creates new form Edit_Partner
     */
    public Edit_Partner() {
        initComponents(); //กำหนดตัวแปรของส่วนประกอบต่างๆ
        get_connect(); //เชื่อมต่อdatabase
        get_collection_in_to_table();//ดึงข้อมูลจากcollectionใส่ตาราง
    }
    
    
    public void clear_partner(){ //ลบข้อมูลในหน้าต่างแก้ไข
        partner_name_txt.setText(""); //ชื่อคู่ค้า
        partner_phone_txt.setText(""); //เบอร์โทรศัพท์
        partner_email_txt.setText(""); //อีเมล
        partner_home_txt.setText(""); //บ้านเลขที่
        partner_locality_txt.setText(""); //ตำบล
        partner_distict_txt.setText(""); //อำเภอ
        partner_province_txt.setText(""); //จังหวัด
        partner_post_txt.setText(""); //รหัสไปรษณีย์
        partner_type_combo.setSelectedIndex(0);//ประเภทของคู่ค้า
        
    }
    public void check_function(){ //เช็คการทำงานในขณะนี้ (ลบ/แก้ไข)
        if(edit_radio.isSelected()){ //ถ้าหากว่าตัวเลือกแก้ไขได้ถูกเลือก
            partner_panel.setVisible(true); //หน้าต่างแก้ไขข้อมูลจะปรากฏขึ้น
            edit=true; //แก้ไขค่าตัวแปรedit ให้มีค่า true
            delete=false; //แก้ไขค่าตัวแปรdelete ให้มีค่า false
            confirm_btn.setText("ยืนยันการแก้ไข"); //ตั้งการแสดงผลที่ปุ่ม
        }else if(delete_radio.isSelected()){//ถ้าหากว่าตัวเลือกลบได้ถูกเลือก
            partner_panel.setVisible(false); //หน้าต่างแก้ไขข้อมูลจะถูกซ่อน
            partner_table.clearSelection(); //ยกเลิกการเลือกในตารางลูกค้า
            confirm_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            clear_partner(); //ลบข้อมูลทั้งหมดที่กรอกในหน้าต่างแก้ไขข้อมูล
        }
    }
    public void get_connect(){
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
      	MongoClient mongo = new MongoClient("localhost", 27017); //เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        db = mongo.getDB(("InthaninDB")); //ดึงข้อมูลจากดาต้าเบสที่ชื่อ InthaninDB
        DBC = db.getCollection("MS_PARTNER"); // ดึงข้อมูลจาก collection ที่ชื่อ MS_PARTNER
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            System.exit(0);//ถ้าหากว่ามีการทำงานผิดพลาด ให้ออกจากโปรแกรม
        }
    }
    public void remove_data_in_table(){
        DefaultTableModel table = (DefaultTableModel)partner_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ partner_table มาเก็บไว้ในตัวแปร-
        while(table.getRowCount()>0){
            table.removeRow(0);
        }
        
    }
    public void get_collection_in_to_table(){
        DefaultTableModel table = (DefaultTableModel)partner_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ partner_table มาเก็บไว้ในตัวแปร-
        String[] row = new String[2]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 2 ช่อง
        DBCursor cursor = DBC.find(); // ค้นหาข้อมูลในcollectionทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject partner = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ partner
                System.out.println(partner.get("MS_PARTNER_NAME"));
                try{ //สร้างลูป do-while
                row[0] = partner.get("MS_PARTNER_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสบริษัทคู่ค้า
                row[1] = partner.get("MS_PARTNER_NAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อบริษัทคู่ค้า
                table.addRow(row); //เพิ่มแถวข้อมูลของตาราง partner_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
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
        int partnerid = 0; //
        BasicDBObject partner_data = new BasicDBObject("MS_PARTNER_ID",partner_table.getSelectedRow()+1);//สร้างObjectชื่อ partner_data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor cursor = DBC.find(partner_data);// ค้นหาข้อมูลในcollectionที่ตรงกับเงื่อนไขของ partner_data
        do{//สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject partner = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ partner
                System.out.println(partner);
                try{ //สร้างลูป do-while
                partner_id = (int)partner.get("MS_PARTNER_ID");
                partner_name_txt.setText(partner.get("MS_PARTNER_NAME").toString()); //ใส่ข้อมูลของชื่อบริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_name_txt
                partner_phone_txt.setText(partner.get("MS_PARTNER_PHONE").toString()); //ใส่ข้อมูลของเบอร์โทรศัพท์บริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_phone_txt
                partner_email_txt.setText(partner.get("MS_PARTNER_EMAIL").toString()); //ใส่ข้อมูลของอีเมลบริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_email_txt
                DBObject partner_address = (DBObject)partner.get("MS_PARTNER_ADDRESS"); //สร้างobjectที่ชื่อว่า partner_address โดยนำข้อมูลมาจาก MS_PARTNER_ADDRESS
                partner_home_txt.setText(partner_address.get("บ้านเลขที่").toString()); //ใส่ข้อมูลของบ้านเลขที่บริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_home_txt
                partner_locality_txt.setText(partner_address.get("ตำบล").toString()); //ใส่ข้อมูลของตำบลบริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_locality_txt
                partner_distict_txt.setText(partner_address.get("อำเภอ").toString()); //ใส่ข้อมูลของอำเภอบริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_distict_txt
                partner_province_txt.setText(partner_address.get("จังหวัด").toString()); //ใส่ข้อมูลของจังหวัดบริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_province_txt
                partner_post_txt.setText(partner_address.get("รหัสไปรษณีย์").toString()); //ใส่ข้อมูลของรหัสไปรษณีย์บริษัทคู่ค้าในช่องข้อมูลที่ชื่อว่า partner_post_txt
                if(partner.get("MS_PARTNER_TYPE").toString().contains("Drink")){ //ตรวจสอบประเภทของบริษัทคู่ค้าประเภทเครื่องดื่ม
                    partner_type_combo.setSelectedIndex(0);
                }else if(partner.get("MS_PARTNER_TYPE").toString().contains("Bakery")){//ตรวจสอบประเภทของบริษัทคู่ค้าประเภทเครื่องดื่ม
                    partner_type_combo.setSelectedIndex(1);
                }else if(partner.get("MS_PARTNER_TYPE").toString().contains("Meal")){//ตรวจสอบประเภทของบริษัทคู่ค้าประเภทเครื่องดื่ม
                   partner_type_combo.setSelectedIndex(2); 
                }
                }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                   e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                }
            //table.addRow(row);*/
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
        exit_btn = new javax.swing.JButton();
        confirm_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        partner_table = new javax.swing.JTable();
        delete_radio = new javax.swing.JRadioButton();
        edit_radio = new javax.swing.JRadioButton();
        partner_panel = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        partner_phone_txt = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        partner_email_txt = new javax.swing.JTextField();
        partner_home_txt = new javax.swing.JTextField();
        partner_locality_txt = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        partner_distict_txt = new javax.swing.JTextField();
        partner_post_txt = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        partner_province_txt = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        partner_type_combo = new javax.swing.JComboBox<>();
        partner_name_txt = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างแก้ไขข้อมูลบริษัทคู่ค้า");
        setPreferredSize(new java.awt.Dimension(930, 640));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(781, 334));
        jPanel1.setPreferredSize(new java.awt.Dimension(781, 334));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลบริษัทคู่ค้า");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 15, 900, 30));

        exit_btn.setText("ยกเลิก");
        exit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_btnActionPerformed(evt);
            }
        });
        getContentPane().add(exit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 535, 110, 50));

        confirm_btn.setText("ยืนยันการแก้ไข");
        confirm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_btnActionPerformed(evt);
            }
        });
        getContentPane().add(confirm_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 535, 110, 50));

        partner_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสบริษัท", "ชื่อบริษัท"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        partner_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                partner_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(partner_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 112, -1, 360));

        buttonGroup1.add(delete_radio);
        delete_radio.setText("ลบข้อมูลบริษัทคู่ค้า");
        delete_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_radioActionPerformed(evt);
            }
        });
        getContentPane().add(delete_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 75, -1, -1));

        buttonGroup1.add(edit_radio);
        edit_radio.setSelected(true);
        edit_radio.setText("แก้ไขข้อมูลบริษัทคู่ค้า");
        edit_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_radioActionPerformed(evt);
            }
        });
        getContentPane().add(edit_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, -1, -1));

        jLabel44.setText("เบอร์โทรศัพท์:");

        jLabel45.setText("อีเมลล์:");

        partner_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_home_txtActionPerformed(evt);
            }
        });

        partner_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_locality_txtActionPerformed(evt);
            }
        });

        jLabel47.setText("ตำบล:");

        jLabel48.setText("อำเภอ:");

        partner_distict_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_distict_txtActionPerformed(evt);
            }
        });

        partner_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_post_txtActionPerformed(evt);
            }
        });

        jLabel49.setText("รหัสไปรษณีย์:");

        jLabel50.setText("จังหวัด:");

        partner_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_province_txtActionPerformed(evt);
            }
        });

        jLabel51.setText("บ้านเลขที่:");

        jLabel39.setText("ประเภทของคู่ค้า:");

        partner_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "คู่ค้าส่วนประกอบเครื่องดื่ม", "คู่ค้าของหวานเบเกอรี่", "คู่ค้าส่วนประกอบของคาว" }));

        jLabel43.setText("ชื่อบริษัท:");

        javax.swing.GroupLayout partner_panelLayout = new javax.swing.GroupLayout(partner_panel);
        partner_panel.setLayout(partner_panelLayout);
        partner_panelLayout.setHorizontalGroup(
            partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partner_panelLayout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel43))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel44))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel51))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel50))
                    .addComponent(jLabel39))
                .addGap(1, 1, 1)
                .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partner_name_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addComponent(partner_phone_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel45)
                        .addGap(4, 4, 4)
                        .addComponent(partner_email_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addComponent(partner_home_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel47)
                        .addGap(0, 0, 0)
                        .addComponent(partner_locality_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel48)
                        .addGap(6, 6, 6)
                        .addComponent(partner_distict_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addComponent(partner_province_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel49)
                        .addGap(6, 6, 6)
                        .addComponent(partner_post_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(partner_type_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        partner_panelLayout.setVerticalGroup(
            partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partner_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel44)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel51)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel50)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel39))
                    .addGroup(partner_panelLayout.createSequentialGroup()
                        .addComponent(partner_name_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(partner_phone_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45)
                            .addComponent(partner_email_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(partner_home_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(partner_locality_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48)
                            .addComponent(partner_distict_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(partner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(partner_province_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49)
                            .addComponent(partner_post_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(partner_type_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(166, Short.MAX_VALUE))
        );

        getContentPane().add(partner_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 112, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_btnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_exit_btnActionPerformed

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        if(edit==true){ // ถ้าหากว่าใช้งานฟังก์ชั่นการแก้ไข
        if(partner_name_txt.getText().isEmpty()){//เช็คว่าตารางเมนูถูกเลือกหรือไม่
            throw new NullPointerException(); //ถ้าหากว่าไม่มีการเลือกให้คืนค่าการทำงานผิดพลาด
        }else{ //ถ้าหากว่ามีการเลือกตารางนี้
        BasicDBObject searchFields = new BasicDBObject("MS_PARTNER_ID",partner_id); //สร้างObjectชื่อ searchFields เพื่อเก็บข้อมูลที่จะนำไปค้นหา
           BasicDBObject updateFields = new BasicDBObject();//สร้างObjectชื่อ updateFields เพื่อเก็บข้อมูลที่จะนำไปค้นหา
           BasicDBObject partner_address = new BasicDBObject();//สร้างObjectชื่อ partner_address เพื่อเก็บข้อมูลที่อยู่ของคู่ค้า
           BasicDBObject setQuery = new BasicDBObject();//สร้างObjectชื่อ setQuery เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
           String type = null; //สร้างตัวแปรเพื่อใช้ในการเก็บประเภทของคู่ค้า
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
           //เพิ่มข้อมูลลงในpartner_address เพื่อเก็บข้อมูลที่อยู่ของคู่ค้า
           partner_address.append("บ้านเลขที่",partner_home_txt.getText());
           partner_address.append("ตำบล",partner_locality_txt.getText());
           partner_address.append("อำเภอ",partner_distict_txt.getText());
           partner_address.append("จังหวัด",partner_province_txt.getText());
           partner_address.append("รหัสไปรษณีย์",partner_post_txt.getText());
           
           
           updateFields.append("MS_PARTNER_NAME",partner_name_txt.getText()); //เพิ่มข้อมูลชื่อคู่ค้าที่จะแก้ไข
           updateFields.append("MS_PARTNER_PHONE",partner_phone_txt.getText()); //เพิ่มข้อมูลเบอร์โทรศัพท์ที่จะแก้ไข
           updateFields.append("MS_PARTNER_EMAIL",partner_email_txt.getText()); //เพิ่มข้อมูลอีเมลที่จะแก้ไข
           updateFields.append("MS_PARTNER_ADDRESS",partner_address); //เพิ่มข้อมูลที่อยู่ที่จะแก้ไข
           
           /*
           สร้างเงื่อนไขในการเช็คประเภทของคู่ค้า
                0 - คู่ค้าเกี่ยวกับเครื่องดื่ม
                1 - คู่ค้าเกี่ยวกับของหวาน
                2 - คู่ค้าเกี่ยวกับของคาว
           
           */
           if(partner_type_combo.getSelectedIndex()==0){
           type = "Drink Partner";
           }else if(partner_type_combo.getSelectedIndex()==1){
           type = "Bakery Partner";   
           }else if(partner_type_combo.getSelectedIndex()==2){
           type = "Meal Partner";    
           }
           updateFields.append("MS_PARTNER_TYPE",type);//เพิ่มข้อมูลประเภทของคู่ค้าจะแก้ไข
           setQuery.append("$set", updateFields);//ตั้งค่าฟังก์ชั่นที่จะใช้กับข้อมูล
           DBC.update(searchFields,setQuery);//ทำการอัพเดทข้อมูลที่ตรวจพบใน database
           System.out.println("Success");//แสดงผลทางหน้าจอหลังทำรายการสำเร็จ
           remove_data_in_table();//ลบข้อมูลของตารางคู่ค้า
           get_collection_in_to_table();//ดึงข้อมูลของคู่ค้าจากDatabaseมาใส่ในตาราง
           JOptionPane.showMessageDialog(null,"แก้ไขข้อมูลของบริษัทคู่ค้าเรียบร้อยแล้วค่ะ");//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
           }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
           }
           }
        }else if(delete==true){ //ถ้าหากว่าฟังก์ชั่นที่ใช้เป็นฟังก์ชั่นการลบ
           DBC.remove(new BasicDBObject("MS_PARTNER_ID",partner_id));//ลบข้อมูลคู่ค้าในDatabaseโดยใช้รหัสของคู่ค้า
           remove_data_in_table();//ลบข้อมูลของตารางคู่ค้า
           get_collection_in_to_table();//ดึงข้อมูลของคู่ค้าจากDatabaseมาใส่ในตาราง
           JOptionPane.showMessageDialog(null,"ลบข้อมูลของบริษัทคู่ค้าเรียบร้อยแล้วค่ะ");//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }
        }catch(NullPointerException e){ //ถ้าหากว่าข้อมูลที่กรอกไม่ครบหรือขาดบางส่วนที่สำคัญ
            e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่ครบถ้วน\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);
        }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void partner_home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_home_txtActionPerformed

    private void partner_locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_locality_txtActionPerformed

    private void partner_distict_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_distict_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_distict_txtActionPerformed

    private void partner_post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_post_txtActionPerformed

    private void partner_province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_province_txtActionPerformed

    private void partner_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_partner_tableMouseClicked
    try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        get_data_from_table(); //ใช้งาน Method เพื่อดึงข้อมูลจากตารางมากรอกใส่ในช่องกรอกข้อมูล
    }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
        System.out.println(e);//แสดงออกการผิดพลาดทางหน้าจอ
    }
    }//GEN-LAST:event_partner_tableMouseClicked

    private void edit_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_radioActionPerformed
        check_function();//เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
    }//GEN-LAST:event_edit_radioActionPerformed

    private void delete_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_radioActionPerformed
        check_function();//เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
    }//GEN-LAST:event_delete_radioActionPerformed

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
            java.util.logging.Logger.getLogger(Edit_Partner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_Partner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_Partner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_Partner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_Partner().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton confirm_btn;
    private javax.swing.JRadioButton delete_radio;
    private javax.swing.JRadioButton edit_radio;
    private javax.swing.JButton exit_btn;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField partner_distict_txt;
    private javax.swing.JTextField partner_email_txt;
    private javax.swing.JTextField partner_home_txt;
    private javax.swing.JTextField partner_locality_txt;
    private javax.swing.JTextField partner_name_txt;
    private javax.swing.JPanel partner_panel;
    private javax.swing.JTextField partner_phone_txt;
    private javax.swing.JTextField partner_post_txt;
    private javax.swing.JTextField partner_province_txt;
    private javax.swing.JTable partner_table;
    private javax.swing.JComboBox<String> partner_type_combo;
    // End of variables declaration//GEN-END:variables
}
