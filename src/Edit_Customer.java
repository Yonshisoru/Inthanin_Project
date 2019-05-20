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
import java.util.Calendar;
import java.util.Date;
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
public class Edit_Customer extends javax.swing.JFrame {
Variable v = new Variable(); //สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
DB db; //กำหนดตัวแปรประเภท DB
DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
//---------------------Current date-------------------------
        Calendar calendar = Calendar.getInstance(); //สร้างตัวแปร Calender ในวันที่ปัจจุบัน
//---------------------Boolean------------------------------
        boolean edit = true;    //|--สร้างตัวแปร edit เพื่อเช็คการทำงาน
        boolean delete = false; //|--สร้างตัวแปร delete เพื่อเช็คการทำงาน
//---------------------String--------------------------------
        String customer_id = null; //สร้างตัวแปร customer_id เพื่อเก็บรหัสลูกค้า
    /**
     *
     * Creates new form Customer
     */
    public Edit_Customer() {
        initComponents();
        getconnect(); //เชื่อมต่อ Database
        get_collection_in_to_table(); //ดึงข้อมูลจาก database ลงใน Table
    }
public void getconnect(){ //เชื่อมต่อ Database
    try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
      	MongoClient mongo = new MongoClient("localhost", 27017); //เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        db = mongo.getDB(("InthaninDB")); //เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        DBC = db.getCollection("MS_CUSTOMER"); // ดึงข้อมูลจาก collection ที่ชื่อ MS_CUSTOMER
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
        }
}

    public void check_function(){ //เช็คการทำงานในขณะนี้ (ลบ/แก้ไข)
        if(edit_radio.isSelected()){ //ถ้าหากว่าตัวเลือกแก้ไขได้ถูกเลือก
            customer_panel.setVisible(true); //หน้าต่างแก้ไขข้อมูลจะปรากฏขึ้น
            edit=true; //แก้ไขค่าตัวแปรedit ให้มีค่า true
            delete=false; //แก้ไขค่าตัวแปรdelete ให้มีค่า false
            confirm_btn.setText("ยืนยันการแก้ไข"); //ตั้งการแสดงผลที่ปุ่ม
        }else if(delete_radio.isSelected()){//ถ้าหากว่าตัวเลือกลบได้ถูกเลือก
            customer_panel.setVisible(false); //หน้าต่างแก้ไขข้อมูลจะถูกซ่อน
            customer_table.clearSelection(); //ยกเลิกการเลือกในตารางลูกค้า
            confirm_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            clear_customer(); //ลบข้อมูลทั้งหมดที่กรอกในหน้าต่างแก้ไขข้อมูล
        }
    }

    public void clear_table(DefaultTableModel table){ //ลบข้อมูลทั้งหมดในตาราง
        while(table.getRowCount()>0){ //สร้างลูป while โดยมีเงื่อนไขคือ จำนวนแถวในตารางจะต้องมากกว่า 0
            table.removeRow(0); //ลบข้อมูลในแถวที่ 1
        }
    }
    
    public void clear_customer(){ //ลบข้อมูลในหน้าต่างแก้ไข
        customer_type_combo.setSelectedIndex(0); //ประเภทลูกค้า
        customer_prefix.setSelectedIndex(0); //คำนำหน้า
        customer_name_txt.setText(""); //ชื่อลูกค้า
        customer_phone_txt.setText(""); //เบอร์โทรศัพท์
        customer_email_txt.setText(""); //อีเมล
        customer_birthdate_txt.setSelectedDate(calendar); //วันเกิด
        customer_home_txt.setText(""); //บ้านเลขที่
        customer_locality_txt.setText(""); //ตำบล
        customer_district_txt.setText(""); //อำเภอ
        customer_province_txt.setText(""); //จังหวัด
        customer_post_txt.setText(""); //รหัสไปรษณีย์
        
    }
    
     public void get_collection_in_to_table(){ //เพิ่มข้อมูลจาก Database มาเพิ่มในตารางลูกค้า
        DefaultTableModel table = (DefaultTableModel)customer_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ customer_table มาเก็บไว้ในตัวแปร
        String[] row = new String[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 6 ช่อง
        DBCursor cursor = DBC.find(); // ค้นหาข้อมูลในcollection(MS_CUSTOMER)ทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject customer_json = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ customer
                //System.out.println(partner.get("MS_CUSTOMER_NAME"));
                try{ //สร้างลูป do-while
                row[0] = customer_json.get("MS_CUSTOMER_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสลูกค้า
                row[1] = customer_json.get("MS_CUSTOMER_NAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อลูกค้า
                String customer_type = customer_json.get("MS_CUSTOMER_TYPE").toString(); //เก็บข้อมูลของประเภทลูกค้า
                //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของประเภทลูกค้า
                
                
                //*เช็คว่าเป็นลูกค้าใหม่หรือลูกค้าเก่าเพิ่อแสดงผลบนตาราง*//
                if(customer_type.contains("Old")){ //ถ้าดึงข้อมูลจาก MS_CUSTOMER_TYPE แล้วมีคำว่า Old อยู่ในนั้น ให้แสดงผลเป็นลูกค้าขาจร
                    row[2] = "ลูกค้าขาจร"; 
                }else if(customer_type.contains("New")){ //ถ้าดึงข้อมูลจาก MS_CUSTOMER_TYPE แล้วมีคำว่า New อยู่ในนั้น ให้แสดงผลเป็นลูกค้าใหม่
                    row[2] = "ลูกค้าใหม่"; 
                }
                
                table.addRow(row); //เพิ่มแถวข้อมูลของตาราง customer_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
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
        DBCollection get_customer = db.getCollection("MS_CUSTOMER"); //ดึงข้อมูลจากCollectionของลูกค้ามาใส่ในตัวแปร
        BasicDBObject customer_data = new BasicDBObject("MS_CUSTOMER_ID",customer_table.getValueAt(customer_table.getSelectedRow(),0));//สร้างObjectชื่อ customer_data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor cursor = get_customer.find(customer_data);// ค้นหาข้อมูลในcollectionที่ตรงกับเงื่อนไขของ customer_data
        do{//สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject customer = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ customer
                //System.out.println(customer);
                try{ //สร้างลูป do-while
                customer_id = customer.get("MS_CUSTOMER_ID").toString();
                customer_name_txt.setText(customer.get("MS_CUSTOMER_NAME").toString()); //ใส่ข้อมูลของชื่อของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_name_txt
                customer_phone_txt.setText(customer.get("MS_CUSTOMER_PHONE").toString()); //ใส่ข้อมูลของเบอร์โทรศัพท์ของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_phone_txt
                customer_email_txt.setText(customer.get("MS_CUSTOMER_EMAIL").toString()); //ใส่ข้อมูลของอีเมลของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_email_txt
                //******************ตั้งค่าวันที่ใน customer_birthdate_txt*********************
                SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd,yyyy"); //สร้างdateformatขึ้นมา
                Date date = sdf.parse(customer.get("MS_CUSTOMER_BIRTHDATE").toString()); //สร้างตัวแปร date เพื่อเก็บข้อมูลวันเกิดมาแปลงให้เป็น format ที่สร้างก่อนหน้านี้
                calendar.setTime(date); //ตั้งวันที่จากวันที่ ที่แปลงเรียบร้อยแล้ว
                customer_birthdate_txt.setSelectedDate(calendar); //ตั้งช่องข้อมูลให้เป็นวันที่ ที่เรากำหนด
                DBObject customer_address = (DBObject)customer.get("MS_CUSTOMER_ADDRESS"); //สร้างobjectที่ชื่อว่า customer_address โดยนำข้อมูลมาจาก MS_CUSTOMER_ADDRESS
                customer_home_txt.setText(customer_address.get("บ้านเลขที่").toString()); //ใส่ข้อมูลของบ้านเลขที่ของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_home_txt
                customer_locality_txt.setText(customer_address.get("ตำบล").toString()); //ใส่ข้อมูลของตำบลของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_locality_txt
                customer_district_txt.setText(customer_address.get("อำเภอ").toString()); //ใส่ข้อมูลของอำเภอของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_distict_txt
                customer_province_txt.setText(customer_address.get("จังหวัด").toString()); //ใส่ข้อมูลของจังหวัดของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_province_txt
                customer_post_txt.setText(customer_address.get("รหัสไปรษณีย์").toString()); //ใส่ข้อมูลของรหัสไปรษณีย์ของลูกค้าในช่องข้อมูลที่ชื่อว่า customer_post_txt
                /*
                   0 = New Customer
                   1 = Old Customer
                */
                
                if(customer.get("MS_CUSTOMER_TYPE").toString().contains("New")){ //ตรวจสอบประเภทของลูกค้า
                    customer_type_combo.setSelectedIndex(0);
                }else if(customer.get("MS_CUSTOMER_TYPE").toString().contains("Old")){ //ตรวจสอบประเภทของลูกค้า
                    customer_type_combo.setSelectedIndex(1);
                }
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
        confirm_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        customer_table = new javax.swing.JTable();
        delete_radio = new javax.swing.JRadioButton();
        edit_radio = new javax.swing.JRadioButton();
        customer_panel = new javax.swing.JPanel();
        customer_home_txt = new javax.swing.JTextField();
        customer_phone_txt = new javax.swing.JTextField();
        customer_birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        customer_prefix = new javax.swing.JComboBox<>();
        customer_locality_txt = new javax.swing.JTextField();
        customer_name_txt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        customer_province_txt = new javax.swing.JTextField();
        customer_email_txt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        customer_district_txt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        customer_type_combo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        customer_post_txt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างแก้ไขข้อมูลลูกค้า");
        setMinimumSize(new java.awt.Dimension(713, 408));
        setPreferredSize(new java.awt.Dimension(915, 640));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(781, 334));
        jPanel1.setPreferredSize(new java.awt.Dimension(781, 334));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("หน้าต่างแก้ไขข้อมูลลูกค้า");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 5, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 30));

        jButton3.setText("ยกเลิก");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 110, 50));

        confirm_btn.setText("ยืนยันการแก้ไข");
        confirm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_btnActionPerformed(evt);
            }
        });
        getContentPane().add(confirm_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, 110, 50));

        customer_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสมาชิก", "ชื่อ-สกุล", "ประเภทลูกค้า"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customer_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customer_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(customer_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, 360));

        buttonGroup1.add(delete_radio);
        delete_radio.setText("ลบข้อมูลลูกค้า");
        delete_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_radioActionPerformed(evt);
            }
        });
        getContentPane().add(delete_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        buttonGroup1.add(edit_radio);
        edit_radio.setSelected(true);
        edit_radio.setText("แก้ไขข้อมูลลูกค้า");
        edit_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_radioActionPerformed(evt);
            }
        });
        getContentPane().add(edit_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        customer_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customer_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_home_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, 50, -1));
        customer_panel.add(customer_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 120, -1));

        customer_birthdate_txt.setFormat(1);
        customer_panel.add(customer_birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        jLabel11.setText("วันเกิด:");
        customer_panel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel3.setText("ประเภทลูกค้า:");
        customer_panel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        customer_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นาง", "นางสาว", "เด็กชาย", "เด็กหญิง" }));
        customer_panel.add(customer_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        customer_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_locality_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 50, -1));
        customer_panel.add(customer_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 250, -1));

        jLabel13.setText("บ้านเลขที่:");
        customer_panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jLabel5.setText("เบอร์โทรศัพท์:");
        customer_panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        customer_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_province_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, 140, -1));
        customer_panel.add(customer_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 150, -1));

        jLabel4.setText("ชื่อ-สกุล:");
        customer_panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel18.setText("รหัสไปรษณีย์:");
        customer_panel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, -1, -1));

        customer_district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_district_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 50, -1));

        jLabel10.setText("ตำบล:");
        customer_panel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, -1, -1));

        customer_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ลูกค้าใหม่", "ลูกค้าขาจร" }));
        customer_panel.add(customer_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, -1, -1));

        jLabel12.setText("จังหวัด:");
        customer_panel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, -1, -1));

        jLabel6.setText("อีเมลล์:");
        customer_panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        customer_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_post_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 50, -1));

        jLabel9.setText("อำเภอ:");
        customer_panel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, -1, -1));

        getContentPane().add(customer_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 430, 410));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
       try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        DBCollection get_customer = db.getCollection("MS_CUSTOMER"); //ดึงข้อมูลจากCollectionของลูกค้ามาใส่ในตัวแปร
        if(edit==true){ //ถ้าหากว่าอยู่ในโหมดแก้ไข
           if(customer_table.getSelectionModel().isSelectionEmpty()){ //เช็คว่าตารางลูกค้าถูกเลือกหรือไม่
               throw new NullPointerException(); //แสดงข้อผิดพลาดเมื่อตารางลูกค้าไม่ถูกเลือก
           }else{
           BasicDBObject searchFields = new BasicDBObject("MS_CUSTOMER_ID",customer_id); //ค้นหาข้อมูลจากรหัสลูกค้า
           BasicDBObject updateFields = new BasicDBObject(); //ข้อมูลที่จะแก้ไขของลูกค้า
           BasicDBObject customer_address = new BasicDBObject(); //ที่อยู่ลูกค้า
           BasicDBObject setQuery = new BasicDBObject();  //ตั้งค่าregexเมื่อส่งข้อมูลเข้าสู่ Database
        try{
           customer_address.append("บ้านเลขที่",customer_home_txt.getText()); //ที่อยู่ลูกค้า
           customer_address.append("ตำบล",customer_locality_txt.getText()); //ตำบลลูกค้า
           customer_address.append("อำเภอ",customer_district_txt.getText()); //อำเภอลูกค้า
           customer_address.append("จังหวัด",customer_province_txt.getText()); //จังหวัดของลูกค้า
           customer_address.append("รหัสไปรษณีย์",customer_post_txt.getText()); //รหัสไปรษณีย์ของลูกค้า
           updateFields.append("MS_CUSTOMER_ADDRESS",customer_address); //นำข้อมูลทั้งหมดมาบีบอัดให้เป็นObjectก่อนจะยัดเข้าฐานข้อมูล
           updateFields.append("MS_CUSTOMER_NAME",customer_prefix.getSelectedItem().toString()+customer_name_txt.getText().substring(3)); //ชื่อลูกค้า
           updateFields.append("MS_CUSTOMER_PHONE",customer_phone_txt.getText()); //เบอร์โทรศัพท์ลูกค้า
           updateFields.append("MS_CUSTOMER_EMAIL",customer_email_txt.getText()); //อีเมลลูกค้า
           updateFields.append("MS_CUSTOMER_BIRTHDATE",customer_birthdate_txt.getText()); //วันเกิดลูกค้า
           String customer_type = null; //ประเภทของลูกค้า
           /* เช็คประเภทของลูกค้า
                0 = New Customer
                1 = Old Customer
           */
                if(customer_type_combo.getSelectedIndex()==0){
                    customer_type = "New Customer";
                }else if(customer_type_combo.getSelectedIndex()==1){
                    customer_type = "Old Customer";
                }
           updateFields.append("MS_CUSTOMER_TYPE",customer_type);  //ประเภทลูกค้า
           setQuery.append("$set", updateFields); //ตั้งค่าให้queryให้เป็นการแก้ไขจากข้อมูลเดิม
           get_customer.update(searchFields,setQuery); //อัพเดทข้อมูลใหม่ในฐานข้อมูล
           //System.out.println("Success");
           clear_table((DefaultTableModel)customer_table.getModel()); //ลบข้อมูลในตารางลูกค้า
           get_collection_in_to_table(); //เรียกข้อมูลในตารางลูกค้าจากฐานข้อมูล
                       JOptionPane.showMessageDialog(null,"แก้ไขข้อมูลของลูกค้าเรียบร้อยแล้วค่ะ");
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
           }
        }else if(delete==true){ //ถ้าหากว่าอยู่ในโหมดลบ
            get_customer.remove(new BasicDBObject("MS_CUSTOMER_ID",customer_id)); //ลบข้อมูลจากฐานข้อมูลโดยค้นหาจากรหัสลูกค้า
            clear_table((DefaultTableModel)customer_table.getModel()); //ลบข้อมูลในตารางลูกค้า
            get_collection_in_to_table();//เรียกข้อมูลในตารางลูกค้าจากฐานข้อมูล
            JOptionPane.showMessageDialog(null,"ลบข้อมูลของลูกค้าเรียบร้อยแล้วค่ะ");
        }
       }catch(NullPointerException e){
           JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่ครบถ้วน\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);;//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
       }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
       }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
            this.setVisible(false); //ปิดการทำงานของหน้าจอนี้
    }//GEN-LAST:event_jButton3ActionPerformed

    private void customer_province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_province_txtActionPerformed

    private void customer_post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_post_txtActionPerformed

    private void customer_district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_district_txtActionPerformed

    private void customer_locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_locality_txtActionPerformed

    private void customer_home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_home_txtActionPerformed

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseClicked
        get_data_from_table(); //ดึงข้อมูลจากตารางที่มาจาก(MS_CUSTOMER)ลงในหน้าต่างแก้ไข
    }//GEN-LAST:event_customer_tableMouseClicked

    private void edit_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_radioActionPerformed
        check_function(); //เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
    }//GEN-LAST:event_edit_radioActionPerformed

    private void delete_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_radioActionPerformed
        check_function(); //เช็คการทำงานในปัจจุบัน(ลบ/แก้ไขข้อมูล)
        clear_customer(); //เคลียร์ข้อมูลทั้งหมดที่อยู่ในหน้าต่างแก้ไข
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton confirm_btn;
    private datechooser.beans.DateChooserCombo customer_birthdate_txt;
    private javax.swing.JTextField customer_district_txt;
    private javax.swing.JTextField customer_email_txt;
    private javax.swing.JTextField customer_home_txt;
    private javax.swing.JTextField customer_locality_txt;
    private javax.swing.JTextField customer_name_txt;
    private javax.swing.JPanel customer_panel;
    private javax.swing.JTextField customer_phone_txt;
    private javax.swing.JTextField customer_post_txt;
    private javax.swing.JComboBox<String> customer_prefix;
    private javax.swing.JTextField customer_province_txt;
    private javax.swing.JTable customer_table;
    private javax.swing.JComboBox<String> customer_type_combo;
    private javax.swing.JRadioButton delete_radio;
    private javax.swing.JRadioButton edit_radio;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
