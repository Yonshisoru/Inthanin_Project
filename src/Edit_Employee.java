
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
public class Edit_Employee extends javax.swing.JFrame {
Variable v = new Variable();
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    //---------------------Boolean------------------------------
        boolean edit = true;    //|--สร้างตัวแปร edit เพื่อเช็คการทำงาน
        boolean delete = false; //|--สร้างตัวแปร delete เพื่อเช็คการทำงาน
        boolean showpwd = false; //เปิด/ปิดการแสดงรหัสผ่าน
    //---------------------Current date-------------------------
        Calendar calendar = Calendar.getInstance(); //สร้างตัวแปร Calender ในวันที่ปัจจุบัน
    //---------------------String--------------------------------
        String employee_id = null; //สร้างตัวแปร employee_id เพื่อเก็บรหัสพนักงาน
    /**
     * Creates new form Customer
     */
    public Edit_Employee() {
        initComponents();
        get_connect();
        get_collection_in_to_table();
    }
    public void get_connect(){
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
      	MongoClient mongo = new MongoClient("localhost", 27017); //เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        db = mongo.getDB(("InthaninDB")); //ดึงข้อมูลจากดาต้าเบสที่ชื่อ InthaninDB
        DBC = db.getCollection("MS_EMPLOYEE"); // ดึงข้อมูลจาก collection ที่ชื่อ MS_EMPLOYEE
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            System.exit(0);//ถ้าหากว่ามีการทำงานผิดพลาด ให้ออกจากโปรแกรม
        }
    }

        public void clear_table(DefaultTableModel table){ //ลบข้อมูลทั้งหมดในตาราง
            while(table.getRowCount()>0){ //สร้างลูป while โดยมีเงื่อนไขคือ จำนวนแถวในตารางจะต้องมากกว่า 0
                table.removeRow(0); //ลบข้อมูลในแถวที่ 1
            }   
        }
    
        public void check_function(){ //เช็คการทำงานในขณะนี้ (ลบ/แก้ไข)
        if(edit_radio.isSelected()){ //ถ้าหากว่าตัวเลือกแก้ไขได้ถูกเลือก
            employee_panel.setVisible(true); //หน้าต่างแก้ไขข้อมูลจะปรากฏขึ้น
            edit=true; //แก้ไขค่าตัวแปรedit ให้มีค่า true
            delete=false; //แก้ไขค่าตัวแปรdelete ให้มีค่า false
            confirm_btn.setText("ยืนยันการแก้ไข"); //ตั้งการแสดงผลที่ปุ่ม
        }else if(delete_radio.isSelected()){//ถ้าหากว่าตัวเลือกลบได้ถูกเลือก
            employee_panel.setVisible(false); //หน้าต่างแก้ไขข้อมูลจะถูกซ่อน
            employee_table.clearSelection(); //ยกเลิกการเลือกในตารางลูกค้า
            confirm_btn.setText("ยืนยันการลบ"); //ตั้งการแสดงผลที่ปุ่ม
            delete=true; //แก้ไขค่าตัวแปรdelete ให้มีค่า true
            edit=false; //แก้ไขค่าตัวแปรedit ให้มีค่า false
            clear_employee(); //ลบข้อมูลทั้งหมดที่กรอกในหน้าต่างแก้ไขข้อมูล
        }
    }
        
        public void clear_employee(){ //ลบข้อมูลในหน้าต่างแก้ไข
        employee_position_combo.setSelectedIndex(0); //ประเภทลูกค้า
        employee_prefix.setSelectedIndex(0); //คำนำหน้า
        employee_name_txt.setText(""); //ชื่อลูกค้า
        employee_phone_txt.setText(""); //เบอร์โทรศัพท์
        employee_email_txt.setText(""); //อีเมล
        employee_birthdate_txt.setSelectedDate(calendar); //วันเกิด
        employee_home_txt.setText(""); //บ้านเลขที่
        employee_locality_txt.setText(""); //ตำบล
        employee_district_txt.setText(""); //อำเภอ
        employee_province_txt.setText(""); //จังหวัด
        employee_post_txt.setText(""); //รหัสไปรษณีย์
        employee_user_txt.setText(""); //รหัสผู้ใช้ของพนักงาน
        employee_pwd_txt.setText(""); //รหัสผ่าน
        
    }
    
    public void get_collection_in_to_table(){
        DefaultTableModel table = (DefaultTableModel)employee_table.getModel(); //ดึงข้อมูลตารางจากตารางชื่อ partner_table มาเก็บไว้ในตัวแปร-
        String[] row = new String[5]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 2 ช่อง
        DBCursor cursor = DBC.find(); // ค้นหาข้อมูลในcollectionทั้งหมด
        do{ //สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject employee = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ partner
                //System.out.println(employee.get("MS_EMPLOYEE_NAME"));
                try{ //สร้างลูป do-while
                row[0] = employee.get("MS_EMPLOYEE_ID").toString(); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสพนักงาน
                row[1] = employee.get("MS_EMPLOYEE_USERNAME").toString(); //Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อผู้เข้าใช้งานของพนักงาน
                row[2] = employee.get("MS_EMPLOYEE_NAME").toString(); //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของชื่อพนักงาน
                row[3] = employee.get("MS_EMPLOYEE_EMAIL").toString(); //Object อาเรย์ ช่องที่ 4 เก็บข้อมูลอีเมลของพนักงาน
                row[4] = employee.get("MS_EMPLOYEE_TYPE").toString(); //Object อาเรย์ ช่องที่ 5 เก็บข้อมูลตำแหน่งของพนักงาน
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
        BasicDBObject employee_data = new BasicDBObject("MS_EMPLOYEE_ID",employee_table.getSelectedRow()+1);//สร้างObjectชื่อ employee_data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor cursor = DBC.find(employee_data);// ค้นหาข้อมูลในcollectionที่ตรงกับเงื่อนไขของ employee_data
        do{//สร้างลูป do-while
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBObject employee = cursor.next(); //ดึงข้อมูลjsonจากการค้นหามาใส่ตัวแปร DBObject ชื่อ employee
                System.out.println(employee);
                try{ //สร้างลูป do-while
                employee_id = employee.get("MS_EMPLOYEE_ID").toString();
                if(employee.get("MS_EMPLOYEE_NAME").toString().contains("นาย")){
                    employee_prefix.setSelectedIndex(0);
                }else if(employee.get("MS_EMPLOYEE_NAME").toString().contains("นาง")){
                    employee_prefix.setSelectedIndex(1);
                }
                employee_name_txt.setText(employee.get("MS_EMPLOYEE_NAME").toString()); //ใส่ข้อมูลของชื่อของพนักงานในช่องข้อมูลที่ชื่อว่า employee_name_txt
                employee_phone_txt.setText(employee.get("MS_EMPLOYEE_PHONE").toString()); //ใส่ข้อมูลของเบอร์โทรศัพท์ของพนักงานในช่องข้อมูลที่ชื่อว่า employee_phone_txt
                employee_email_txt.setText(employee.get("MS_EMPLOYEE_EMAIL").toString()); //ใส่ข้อมูลของอีเมลของพนักงานในช่องข้อมูลที่ชื่อว่า employee_email_txt
                employee_user_txt.setText(employee.get("MS_EMPLOYEE_USERNAME").toString()); //ใส่ข้อมูลของอีเมลของพนักงานในช่องข้อมูลที่ชื่อว่า employee_email_txt
                employee_pwd_txt.setText(employee.get("MS_EMPLOYEE_PWD").toString()); //ใส่ข้อมูลของอีเมลของพนักงานในช่องข้อมูลที่ชื่อว่า employee_email_txt
                DBObject employee_address = (DBObject)employee.get("MS_EMPLOYEE_ADDRESS"); //สร้างobjectที่ชื่อว่า employee_address โดยนำข้อมูลมาจาก MS_EMPLOYEE_ADDRESS
                employee_home_txt.setText(employee_address.get("บ้านเลขที่").toString()); //ใส่ข้อมูลของบ้านเลขที่ของพนักงานในช่องข้อมูลที่ชื่อว่า employee_home_txt
                employee_locality_txt.setText(employee_address.get("ตำบล").toString()); //ใส่ข้อมูลของตำบลของพนักงานในช่องข้อมูลที่ชื่อว่า employee_locality_txt
                employee_district_txt.setText(employee_address.get("อำเภอ").toString()); //ใส่ข้อมูลของอำเภอของพนักงานในช่องข้อมูลที่ชื่อว่า employee_distict_txt
                employee_province_txt.setText(employee_address.get("จังหวัด").toString()); //ใส่ข้อมูลของจังหวัดของพนักงานในช่องข้อมูลที่ชื่อว่า employee_province_txt
                employee_post_txt.setText(employee_address.get("รหัสไปรษณีย์").toString()); //ใส่ข้อมูลของรหัสไปรษณีย์ของพนักงานในช่องข้อมูลที่ชื่อว่า employee_post_txt
                /*
                    1 = Employee
                    2 = Owner
                */
                if(employee.get("MS_EMPLOYEE_TYPE").toString().contains("Employee")){ //ตรวจสอบประเภทของของพนักงาน
                    employee_position_combo.setSelectedIndex(1);
                }else if(employee.get("MS_EMPLOYEE_TYPE").toString().contains("Owner")){//ตรวจสอบประเภทของพนักงาน
                    employee_position_combo.setSelectedIndex(2);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        employee_table = new javax.swing.JTable();
        delete_radio = new javax.swing.JRadioButton();
        edit_radio = new javax.swing.JRadioButton();
        employee_panel = new javax.swing.JPanel();
        showpwd_check = new javax.swing.JCheckBox();
        employee_district_txt = new javax.swing.JTextField();
        employee_age_combo = new javax.swing.JComboBox<>();
        man_radio = new javax.swing.JRadioButton();
        woman_radio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        employee_birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        employee_prefix = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        employee_name_txt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        employee_locality_txt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        employee_home_txt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        employee_email_txt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        employee_post_txt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        employee_province_txt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        employee_user_txt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        employee_position_combo = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        employee_phone_txt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        employee_pwd_txt = new javax.swing.JPasswordField();
        cancel_btn = new javax.swing.JButton();
        confirm_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างแก้ไขข้อมูลพนักงาน");
        setMinimumSize(new java.awt.Dimension(713, 408));
        setPreferredSize(new java.awt.Dimension(915, 640));
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

        employee_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสพนักงาน", "ชื่อผู้เข้าใช้งาน", "ชื่อ-สกุล", "อีเมล", "ตำแหน่ง"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        employee_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employee_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(employee_table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 860, 150));

        buttonGroup1.add(delete_radio);
        delete_radio.setText("ลบข้อมูลพนักงาน");
        delete_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_radioActionPerformed(evt);
            }
        });
        getContentPane().add(delete_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        buttonGroup1.add(edit_radio);
        edit_radio.setSelected(true);
        edit_radio.setText("แก้ไขข้อมูลพนักงาน");
        edit_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_radioActionPerformed(evt);
            }
        });
        getContentPane().add(edit_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        employee_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        showpwd_check.setText("แสดงรหัสผ่าน");
        showpwd_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpwd_checkActionPerformed(evt);
            }
        });
        employee_panel.add(showpwd_check, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 200, -1, -1));

        employee_district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_district_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, 50, -1));

        employee_age_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกอายุ" }));
        employee_panel.add(employee_age_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 80, -1));

        man_radio.setText("ชาย");
        employee_panel.add(man_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, -1, -1));

        woman_radio.setText("หญิง");
        employee_panel.add(woman_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        jLabel5.setText("วัน/เดือน/ปีเกิด:");
        employee_panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, -1));

        jLabel6.setText("อายุ:");
        employee_panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));

        employee_birthdate_txt.setFormat(1);
        employee_birthdate_txt.setWeekStyle(datechooser.view.WeekDaysStyle.FULL);
        employee_panel.add(employee_birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        jLabel7.setText("เพศ:");
        employee_panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        employee_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นางสาว" }));
        employee_panel.add(employee_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel9.setText("ชื่อ-สกุล:");
        employee_panel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));
        employee_panel.add(employee_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 280, -1));

        jLabel11.setText("อำเภอ:");
        employee_panel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, -1, -1));

        employee_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_locality_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 50, -1));

        jLabel12.setText("ตำบล:");
        employee_panel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, -1));

        employee_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_home_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 50, -1));

        jLabel13.setText("บ้านเลขที่:");
        employee_panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        employee_email_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_email_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, 140, -1));

        jLabel14.setText("จังหวัด:");
        employee_panel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, -1, -1));

        jLabel15.setText("ตำแหน่ง:");
        employee_panel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));

        employee_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_post_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 70, 50, -1));

        jLabel16.setText("อีเมลล์:");
        employee_panel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, -1, -1));

        employee_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_province_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 140, -1));

        jLabel17.setText("ชื่อผู้ใช้งาน:\n");
        employee_panel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, -1, -1));

        employee_user_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_user_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_user_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 140, -1));

        jLabel18.setText("รหัสผ่าน:");
        employee_panel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, -1, -1));

        employee_position_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกตำแหน่ง", "Employee", "Owner" }));
        employee_panel.add(employee_position_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, -1, -1));

        jLabel19.setText("รหัสไปรษณีย์:");
        employee_panel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, -1, -1));

        employee_phone_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_phone_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 140, -1));

        jLabel20.setText("เบอร์โทรศัพท์:");
        employee_panel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, -1));

        employee_pwd_txt.setToolTipText("");
        employee_pwd_txt.setEchoChar('*');
        employee_panel.add(employee_pwd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, 140, -1));

        getContentPane().add(employee_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 840, 280));

        cancel_btn.setText("ยกเลิก");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });
        getContentPane().add(cancel_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 530, 130, 50));

        confirm_btn.setText("ยืนยันแก้ไข");
        confirm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_btnActionPerformed(evt);
            }
        });
        getContentPane().add(confirm_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 530, 130, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showpwd_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpwd_checkActionPerformed
        showpwd = !showpwd;
        if(showpwd ==true){ //ถ้าหากว่าติ๊กแสดงรหัสผ่าน
            employee_pwd_txt.setEchoChar((char)0); //โชว์รหัสผ่าน
        }else{ //ถ้าหากว่าไม่ได้ติ๊กแสดงรหัสผ่าน
            employee_pwd_txt.setEchoChar('*'); //ปิดการโชว์รหัสผ่าน
        }
    }//GEN-LAST:event_showpwd_checkActionPerformed

    private void employee_district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_district_txtActionPerformed

    private void employee_locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_locality_txtActionPerformed

    private void employee_home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_home_txtActionPerformed

    private void employee_email_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_email_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_email_txtActionPerformed

    private void employee_post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_post_txtActionPerformed

    private void employee_province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_province_txtActionPerformed

    private void employee_user_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_user_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_user_txtActionPerformed

    private void employee_phone_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_phone_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_phone_txtActionPerformed

    private void employee_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employee_tableMouseClicked
        get_data_from_table();//ดึงข้อมูลจากตารางมาลงในหน้าต่างการแก้ไข
    }//GEN-LAST:event_employee_tableMouseClicked

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
        try{
            DBCollection get_employee = db.getCollection("MS_EMPLOYEE");
           if(edit==true){
               if(employee_name_txt.getText().isEmpty()){
                    throw new NullPointerException();
               }else{
           BasicDBObject searchFields = new BasicDBObject("MS_EMPLOYEE_ID",Integer.parseInt(employee_id));
           BasicDBObject updateFields = new BasicDBObject();
           BasicDBObject employee_address = new BasicDBObject();
           BasicDBObject setQuery = new BasicDBObject();
           String type = null;
           employee_address.append("บ้านเลขที่",employee_home_txt.getText());
           employee_address.append("ตำบล",employee_locality_txt.getText());
           employee_address.append("อำเภอ",employee_district_txt.getText());
           employee_address.append("จังหวัด",employee_province_txt.getText());
           employee_address.append("รหัสไปรษณีย์",employee_post_txt.getText());
           updateFields.append("MS_EMPLOYEE_NAME",employee_prefix.getSelectedItem().toString()+employee_name_txt.getText().substring(3));
           updateFields.append("MS_EMPLOYEE_PHONE",employee_phone_txt.getText());
           updateFields.append("MS_EMPLOYEE_EMAIL",employee_email_txt.getText());
           updateFields.append("MS_EMPLOYEE_ADDRESS",employee_address);
           updateFields.append("MS_EMPLOYEE_BIRTHDATE",employee_birthdate_txt.getText());
           updateFields.append("MS_EMPLOYEE_USERNAME",employee_user_txt.getText());
           updateFields.append("MS_EMPLOYEE_PWD",employee_pwd_txt.getText());
           String employee_type = null;
                if(employee_position_combo.getSelectedIndex()==1){
                    employee_type = "Employee";
                }else if(employee_position_combo.getSelectedIndex()==2){
                    employee_type = "Owner";
                }
           setQuery.append("$set", updateFields);
           updateFields.append("MS_EMPLOYEE_TYPE",employee_type);
           get_employee.update(searchFields,setQuery);
           System.out.println("Success");
           clear_table((DefaultTableModel)employee_table.getModel());
           get_collection_in_to_table();
            JOptionPane.showMessageDialog(null,"แก้ไขข้อมูลของพนักงานเรียบร้อยแล้วค่ะ");
           clear_employee();
               }
        }else if(delete==true){
            get_employee.remove(new BasicDBObject("MS_EMPLOYEE_ID",Integer.parseInt(employee_id)));
            clear_table((DefaultTableModel)employee_table.getModel());
            get_collection_in_to_table();
            JOptionPane.showMessageDialog(null,"ลบข้อมูลของพนักงานเรียบร้อยแล้วค่ะ");
        }               
        }catch(NullPointerException e){
           JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่ครบถ้วน\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
        }

    }//GEN-LAST:event_confirm_btnActionPerformed

    private void edit_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_radioActionPerformed
       check_function();
    }//GEN-LAST:event_edit_radioActionPerformed

    private void delete_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_radioActionPerformed
       check_function();
    }//GEN-LAST:event_delete_radioActionPerformed

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancel_btnActionPerformed

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
            java.util.logging.Logger.getLogger(Edit_Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new Edit_Employee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancel_btn;
    private javax.swing.JButton confirm_btn;
    private javax.swing.JRadioButton delete_radio;
    private javax.swing.JRadioButton edit_radio;
    private javax.swing.JComboBox<String> employee_age_combo;
    private datechooser.beans.DateChooserCombo employee_birthdate_txt;
    private javax.swing.JTextField employee_district_txt;
    private javax.swing.JTextField employee_email_txt;
    private javax.swing.JTextField employee_home_txt;
    private javax.swing.JTextField employee_locality_txt;
    private javax.swing.JTextField employee_name_txt;
    private javax.swing.JPanel employee_panel;
    private javax.swing.JTextField employee_phone_txt;
    private javax.swing.JComboBox<String> employee_position_combo;
    private javax.swing.JTextField employee_post_txt;
    private javax.swing.JComboBox<String> employee_prefix;
    private javax.swing.JTextField employee_province_txt;
    private javax.swing.JPasswordField employee_pwd_txt;
    private javax.swing.JTable employee_table;
    private javax.swing.JTextField employee_user_txt;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton man_radio;
    private javax.swing.JCheckBox showpwd_check;
    private javax.swing.JRadioButton woman_radio;
    // End of variables declaration//GEN-END:variables
}
