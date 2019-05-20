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
import com.mongodb.util.JSON;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Set;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.Document;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
 * @author Yonshisoru (The sadly man)!
 */
public class Main extends javax.swing.JFrame {
Variable v = new Variable();//สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
    MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
    DB db; //กำหนดตัวแปรประเภท DB
    DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
    boolean showpwd = false; //กำหนดตัวแปรเพื่อเช็คการแสดงรหัสผ่านในหน้าจอของพนักงาน
    static String username; //ตัวแปรของชื่อของผู้ใช้งาน   
    static String position; //ตัวแปรของตำแหน่ง
//-----------------------List / ArrayList -------------------------------------
List<DBObject>menu_component = new ArrayList<>(); //ลิสต์ของส่วนประกอบของเมนู
List<DBObject>order_list = new ArrayList<>(); //ลิสต์ของออเดอร์
//-------------------Menu Panel Variable-----------------------
String menu_table_doubleclick = ""; //ตัวแปรที่ใช้เช็คดับเบิ้ลคลิ๊กของตารางเมนู
String history_table_doubleclick = ""; //ตัวแปรใช้เช็คดับเบิ้ลคลิ๊กของตารางประวัติออเดอร์
//-------------------Order Variable--------------------------
boolean add_order = false; //
boolean check_order_list = false;
int total_price = -1; //ราคาทั้งหมดของออเดอร์
int order_list_price = -1; //ราคาของแต่ละรายการของออเดอร์
//---------------------Current date-------------------------
        Calendar calendar = Calendar.getInstance(); //ตัวแปรเก็บข้อมูลวันที่ปัจจุบัน
//----------------------------------------------------------
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        set_logo(); //ฟังก์ชั่นในการแสดงรูปไอค่อน
        this.setLocationRelativeTo(null); //ตั้งค่าการแสดงผลให้อยู่กลางหน้าจอ
        disablepanel(); //ซ่อนหน้าต่างทุกหน้าต่าง
        sessionnow(); //การใช้งานปัจจุบัน
        System.out.println(v.getstatus()); //แสดงสถานะของพนักงานปัจจุบัน
    }
    
//---------------------Initilization-----------------------------
        public void set_logo(){ //ฟังก์ชั่นในการแสดงรูปไอค่อน
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        ImageIcon imageIcon = new ImageIcon ("./image/main_icon.png"); //สร้างตัวแปรเพื่อเก็บรูปภาพจากปลายทาง 
        picture_label.setIcon(imageIcon);//ตั้งค่าไอค่อนของ label ให้เป็นรูปภาพ
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
    }
        
        public void disablepanel(){//ซ่อนหน้าต่างทั้งหมด
            first_panel.setVisible(false);//หน้าต่างแรก
            order_panel.setVisible(false);//หน้าต่างออเดอร์
            customer_panel.setVisible(false);//หน้าต่างลูกค้า
            product_panel.setVisible(false);//หน้าต่างสินค้า
            stock_panel.setVisible(false);//หน้าต่างการเพิ่มจำนวนสินค้า
            menu_panel.setVisible(false);//หน้าต่างเมนู
            partner_panel.setVisible(false);//หน้าต่างบริษัทคู่ค้า
            employee_panel.setVisible(false);//หน้าต่างพนักงาน
            history_panel.setVisible(false);//หน้าต่างประวัติออเดอร์
}
//--------customer--------------------------
    public void clear_customer(){ //ฟังก์ชั่นการเคลียร์ข้อมูลของลูกค้า
        customer_name_txt.setText(""); //ชื่อของลูกค้า
        customer_phone_txt.setText(""); //เบอร์โทรศัพท์ลูกค้า
        customer_email_txt.setText(""); //อีเมลลูกค้า
        customer_home_txt.setText(""); //บ้านเลขที่
        customer_locality_txt.setText(""); //ตำบล
        customer_district_txt.setText(""); //อำเภอ
        customer_province_txt.setText(""); //จังหวัด
        customer_post_txt.setText(""); //รหัสไปรษณีย์
        customer_type_combo.setSelectedIndex(0); //ปุ่มตัวเลือกประเภทลูกค้า
        customer_prefix.setSelectedIndex(0); //คำนำหน้าลูกค้า
        customer_birthdate_txt.setSelectedDate(calendar); //ปุ่มเลือกวันที่
    }
  public boolean check_null_customer(){
      if(customer_name_txt.getText().isEmpty()||customer_phone_txt.getText().isEmpty()||
              customer_email_txt.getText().isEmpty()||customer_home_txt.getText().isEmpty()||
              customer_locality_txt.getText().isEmpty()||customer_district_txt.getText().isEmpty()||
              customer_province_txt.getText().isEmpty()||customer_post_txt.getText().isEmpty()){
          return false;
      }else{
          return true;
      }
  }
  
//--------clear partner data--------------------------
    public void clear_partner(){ //ฟังก์ชั่นการเคลียร์ข้อมูลของบริษัทคู่ค้า
        partner_name_txt.setText(""); //ชื่อคู่ค้า
        partner_phone_txt.setText(""); //เบอร์โทรศัพท์คู่ค้า
        partner_email_txt.setText(""); //อีเมลคู่ค้า
        partner_home_txt.setText(""); //บ้านเลขที่
        partner_locality_txt.setText(""); //ตำบล
        partner_distict_txt.setText(""); //อำเภอ
        partner_province_txt.setText(""); //จังหวัด
        partner_post_txt.setText(""); //รหัสไปรษณีย์
        partner_type_combo.setSelectedIndex(0); //ปุ่มตัวเลือกประเภทคู่ค้า
    }

    public void clear_product(){ //ฟังก์ชั่นการเคลียร์ข้อมูลสินค้า
        pro_name_txt.setText(""); //ชื่อสินค้า
        pro_price_txt.setText(""); //ราคาสินค้า
        pro_type_combo.setSelectedIndex(0); //ปุ่มเลือกประเภทสินค้า
        partner_combo.setSelectedIndex(0); //ปุ่มตัวเลือกประเภทของคู่ค้า
    }
    
    public void clear_stock(){ //ฟังก์ชั่นการเคลียร์ข้อมูลการเพิ่มจำนวนสินค้า
        stock_combo.setSelectedIndex(0); //ปุ่มตัวเลือกสินค้า
        stock_amount.setText(""); //จำนวนของสินค้า
    }
    
    public void clear_emp(){ //ฟังก์ชั่นการเคลียร์ข้อมูลของพนักงาน
    employee_birthdate_txt.setSelectedDate(calendar);//วันเกิดของพนักงาน
    employee_name_txt.setText(""); //ชื่อของพนักงาน
    employee_age_combo.setSelectedIndex(0); //อายุของพนักงาน
    man_radio.setSelected(false); //ปุ่มเลือกเพศผู้ชาย
    woman_radio.setSelected(false); //ปุ่มเลือกเพศผู้หญิง
    employee_phone_txt.setText(""); //เบอร์โทรศัพท์พนักงาน
    employee_home_txt.setText("");//บ้านเลขที่
    employee_locality_txt.setText("");//ตำบล
    employee_district_txt.setText("");//อำเภอ
    employee_province_txt.setText("");//จังหวัด
    employee_post_txt.setText(""); //รหัสไปรษณีย์
    employee_email_txt.setText("");//อีเมลของพนักงาน
    employee_position_combo.setSelectedIndex(0);//ปุ่มตัวเลือกตำแหน่งของพนักงาน
    employee_user_txt.setText("");//รหัสผู้ใช้งานของพนักงาน
    employee_pwd_txt.setText("");//รหัสผ่านของพนักงาน
    showpwd_check.setSelected(false); //ปุ่มแสดงรหัสผ่าน
    showpwd = false;//ตัวแปรแสดงรหัสผ่านให้มีค่าเป็น false
    confirm.setSelected(false); //ยกเลิกการยืนยันข้อมูล
    employee_prefix.setSelectedIndex(0); //ปุ่มตัวเลือกคำนำหน้า
}
        public boolean check_null_employee(){ //ฟังก์ชั่นการเคลียร์ข้อมูลของพนักงาน
    if(employee_name_txt.getText().isEmpty()||employee_phone_txt.getText().isEmpty()||
       employee_home_txt.getText().isEmpty()||employee_locality_txt.getText().isEmpty()||
       employee_province_txt.getText().isEmpty()||employee_post_txt.getText().isEmpty()||
       employee_email_txt.getText().isEmpty()||employee_user_txt.getText().isEmpty()||
       employee_pwd_txt.getText().isEmpty()){ //ชื่อของพนักงาน
        return false;
    }else{
        return true;
    }
}
    
    public void clear_table(DefaultTableModel table){ //ฟังก์ชั่นการลบข้อมูลในตาราง
        while(table.getRowCount()>0){ //สร้างฟังก์ชั่น while โดยมีเงื่อนไขการหยุดทำงานคือจำนวนแถวของตารางเท่ากับ 0
            table.removeRow(0);//ลบข้อมูลแถวบนสุด
        }
    }
    
    public void clear_menu(){ //ฟังก์ชั่นการเคลียร์ข้อมูลเมนู
        menu_name_txt.setText(""); //ชื่อเมนู
        menu_price_txt.setText("");//ราคาของเมนู
    }
    //-------------------------Partner--------------------------------------//
    public void set_parnter_combo(int k){ //ฟังก์ชั่นการเพิ่มข้อมูลตามสินค้า
       partner_combo.removeAllItems(); //ลบข้อมูลเก่าทั้งหมดของปุ่มตัวเลือก
        partner_combo.addItem("เลือกบริษัทคู่ค้า"); //เพิ่มข้อมูลแถวบนสุด
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        DBCollection table = db.getCollection("MS_PARTNER");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
        DBCursor cur = table.find(); // ค้นหาข้อมูลในcollection(MS_PARTNER)
        while(cur.hasNext()){ //สร้างลูป while ที่จะหยุดทำงานต่อเมื่อไม่มีข้อมูลในCollection MS_PARTNER
            DBObject kk = cur.next();///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ kk
            switch (k) {
                case 1:
                    //ถ้าหากว่าข้อมูลของ parameter(Index ประเภทของสินค้า)
                    /*
                    1 - จะเพิ่มคู่ค้าที่เป็นประเภทของเครื่องดื่ม
                    2 - จะเพิ่มคู่ค้าที่เป็นประเภทของของหวาน
                    3 - จะเพิ่มคู่ค้าที่เป็นประเภทของของคาว
                    */
                    if(kk.get("MS_PARTNER_TYPE").toString().contains("Drink")){
                        partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                    }   break;
                case 2:
                    if(kk.get("MS_PARTNER_TYPE").toString().contains("Bakery")){
                        partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                    }   break;
                case 3:
                    if(kk.get("MS_PARTNER_TYPE").toString().contains("Meal")){
                        partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                    }   break;
                default:
                    break;
            }
        }
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
    }
//---------------------Product-------------------------------
      public boolean check_null_product(){
      if(pro_name_txt.getText().isEmpty()||pro_price_txt.getText().isEmpty()||
         pro_type_combo.getSelectedItem().toString().isEmpty()||partner_combo.getSelectedItem().toString().isEmpty()){
          return false;
      }else{
          return true;
      }
  }
//---------------------Partner-------------------------------
      public boolean check_null_partner(){
      if(partner_name_txt.getText().isEmpty()||partner_phone_txt.getText().isEmpty()||
              partner_email_txt.getText().isEmpty()||partner_home_txt.getText().isEmpty()||
              partner_locality_txt.getText().isEmpty()||partner_distict_txt.getText().isEmpty()||
              partner_province_txt.getText().isEmpty()||partner_post_txt.getText().isEmpty()){
          return false;
      }else{
          return true;
      }
  }
//----------------------Employee---------------------------
    public void emp_age_combo(){ //ฟังก์ชั่นเพิ่มอายุในปุ่มเลือกอายุ
        for(int i =15;i<60;i++){ //สร้างลูป for เพิ่มอายุตั้งแต่ 15-60ในปุ่มเลือกอายุ
            employee_age_combo.addItem(""+i);
        }
    }
//----------------------Stocking-------------------
    public void set_stocking_product_combo(){ //เพิ่มข้อมูลรายการสินค้าในแท็บเลือก
         stock_combo.removeAllItems(); //ลบข้อมูลทั้งหมดในปุ่มเลือก
         stock_combo.addItem("เลือกสินค้า"); //เพิ่มข้อมูลตัวแรก
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        DBCollection table = db.getCollection("MS_PRODUCT");//ดึงข้อมูลจากCollectionของสินค้ามาใส่ในตัวแปร
        DBCursor cur = table.find();// ค้นหาข้อมูลของสินค้าทั้งหมดในcollection(MS_PRODUCT)
        while(cur.hasNext()){//สร้างลูป while ที่จะหยุดทำงานต่อเมื่อไม่มีข้อมูลในCollection MS_PRODUCT
            DBObject kk = cur.next(); ///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ kk
            stock_combo.addItem(kk.get("MS_PRODUCT_NAME").toString());//เพิ่มชื่อของสินค้าลงในปุ่มตัวเลือก
            }
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
    }
//-----------------------------Menu---------------------------------------------------------
    public void set_menu_table(DefaultTableModel table){ //ฟังก์ชั่นการเพิ่มข้อมูลลงในตารางเมนู
        DefaultTableModel model = table; //ดึงข้อมูลตารางของ parameter มาเก็บไว้ในตัวแปร
        clear_table(model);//ฟังก์ชั่นการลบข้อมูลในตาราง
        Object[] row = new Object[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        for(DBObject e:menu_component){ //สร้างลูป foreach ของ List ของส่วนประกอบของเมนู
            DBObject product = e; ///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
            row[0] = product.get("MS_PRODUCT_ID"); //Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของสินค้า
            row[1] = product.get("MS_PRODUCT_NAME");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของสินค้า
            row[2] = product.get("MS_PRODUCT_AMOUNT"); //Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของจำนวนของสินค้า
            model.addRow(row);//เพิ่มแถวข้อมูลของตารางtable โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
        }
    }
//-------------------------------Ordering--------------------------------------------
        public void set_order_table(DefaultTableModel table){//ฟังก์ชั่นการเพิ่มข้อมูลลงในตารางเมนู
        DefaultTableModel model = table; //ดึงข้อมูลตารางของ parameter มาเก็บไว้ในตัวแปร
        clear_table(model);//ฟังก์ชั่นการลบข้อมูลในตาราง
        Object[] row = new Object[3]; // สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        for(DBObject e:menu_component){//สร้างลูป foreach ของ List ของส่วนประกอบของเมนู
            DBObject product = e;///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
            row[0] = product.get("MS_MENU_ID");//Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของเมนู
            row[1] = product.get("MS_MENU_NAME");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของเมนู
            row[2] = product.get("MS_MENU_PRICE");//Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของราคาของเมนู
            model.addRow(row);//เพิ่มแถวข้อมูลของตาราง table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
        }
    }
        public void clearing_order(){ //ฟังก์ชั่นการเคลียร์รายการออเดอร์
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");// ดึงข้อมูลของรายการออเดอร์จาก collection ที่ชื่อ TRAN_ORDER_LIST
            DBCursor finding_order_list = get_order_list.find();// ค้นหาข้อมูลของรายการออเดอร์ในcollection(TRAN_ORDER_LIST)ทั้งหมด
            while (finding_order_list.hasNext()) {//สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในTRAN_ORDER_LIST
                get_order_list.remove(finding_order_list.next()); //ลบข้อมูลล่าสุดของรายการออเดอร์ใน TRAN_ORDER_LIST
            }
            clear_table((DefaultTableModel)order_table.getModel()); //ลบข้อมูลในตารางการออเดอร์
        }
//---------------------------Put Product Data into ModelTable--------------------------------------------
    public void get_menu(DefaultTableModel table){ //ฟังก์ชั่นการเพิ่มข้อมูลเมนูลงตาราง
        DefaultTableModel model = table;//ดึงข้อมูลตารางของ parameter มาเก็บไว้ในตัวแปร
        Object[] row = new Object[3];// สร้างอาเรย์ Object ชื่อว่า row ขนาด 3 ช่อง
        DBCollection menu_collection  = db.getCollection("MS_MENU");// ดึงข้อมูลของเมนูจาก collection ที่ชื่อ MS_MENU
        DBCursor menu_finding = menu_collection.find().sort(new BasicDBObject("MS_MENU_ID", 1));; // ค้นหาข้อมูลของคู่ค้าในcollection(MS_MENU)ทั้งหมดโดยเรียงตามรหัสของเมนู
        while(menu_finding.hasNext()){//สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในMS_MENU
            DBObject menu_json = menu_finding.next();///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product
            row[0] = (int)menu_json.get("MS_MENU_ID");//Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของเมนู
            row[1] = menu_json.get("MS_MENU_NAME");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของเมนู
            row[2] = menu_json.get("MS_MENU_PRICE");//Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของราคาของเมนู
            model.addRow(row);//เพิ่มแถวข้อมูลของตาราง table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
        }
    }
//----------------------------History---------------------------------------------
        public double get_history(String month){ //ฟังก์ชั่นการเพิ่มข้อมูลในตารางประวัติออเดอร์
        double total_price = 0; //ตัวแปรเก็บราคารวม
        DefaultTableModel model = (DefaultTableModel)history_table.getModel(); //ดึงข้อมูลของตารางประวัติออเดอร์มาใส่ไว้ในตัวแปร
        Object[] row = new Object[4];// สร้างอาเรย์ Object ชื่อว่า row ขนาด 4 ช่อง
        DBCollection get_history  = db.getCollection("TRAN_ORDER");// ดึงข้อมูลของออเดอร์จาก collection ที่ชื่อ TRAN_ORDER
        DBCursor order_finding = get_history.find();// ค้นหาข้อมูลของออเดอร์ในcollection(TRAN_ORDER)ทั้งหมด
        while(order_finding.hasNext()){//สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในTRAN_ORDER
           DBObject order_json = order_finding.next();///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ order_json
           if((order_json.get("TRAN_ORDER_DATE").toString().contains(month)&&order_json.get("TRAN_ORDER_DATE").toString()
                                                           .contains(LocalDate.now().toString().subSequence(0,4)))){
               /*
               ค้นหาประวัติออเดอร์โดยแยกประเภทตามเดือน โดยใช้การค้นหาจากวันที่ของออเดอร์นั้นๆ และมาเทียบกับเดือนโดยใช้ฟังก์ชั่นเปลี่ยนลำดับเดือนให้เป็นชื่อเดือนเต็ม
               */
               total_price += Double.parseDouble(order_json.get("TRAN_ORDER_TOTAL_PRICE").toString());//เพิ่มราคาของออเดอร์ไปในราคารวม
             //System.out.println(order_json.get("TRAN_ORDER_DATE").toString());
            row[0] = order_json.get("TRAN_ORDER_ID");//Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของออเดอร์
            row[1] = order_json.get("TRAN_ORDER_TOTAL_PRICE");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของราคารวมของออเดอร์
            row[2] = order_json.get("TRAN_ORDER_DATE");//Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของวันที่ของออเดอร์
            row[3] = order_json.get("TRAN_ORDER_TIME");//Object อาเรย์ ช่องที่ 4 เก็บข้อมูลของเวลาของออเดอร์
            model.addRow(row);//เพิ่มแถวข้อมูลของตาราง history_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
           }
        }
        return total_price; //คืนค่าราคารวมทั้งหมดของการขายเดือนนั้นๆ
    }
//---------------------------Put Product Data into --------------------------------------------
    public void get_product(DefaultTableModel table){ //ฟังก์ชั่นการเพิ่มข้อมูลสินค้าลงในตาราง
        DefaultTableModel model = table;//ดึงข้อมูลตารางของ parameter มาเก็บไว้ในตัวแปร
        Object[] row = new Object[4];// สร้างอาเรย์ Object ชื่อว่า row ขนาด 4 ช่อง
        DBCollection product_collection  = db.getCollection("MS_PRODUCT");// ดึงข้อมูลของสินค้าจาก collection ที่ชื่อ MS_PRODUCT
        DBCursor product_finding = product_collection.find();// ค้นหาข้อมูลของสินค้าในcollection(MS_PRODUCT)ทั้งหมด
        while(product_finding.hasNext()){//สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในMS_PRODUCT
            DBObject product_json = product_finding.next();///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product_json
            row[0] = (int)product_json.get("MS_PRODUCT_ID");//Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของสินค้า
            row[1] = product_json.get("MS_PRODUCT_NAME");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อของสินค้า
            row[2] = product_json.get("MS_PRODUCT_PRICE");//Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของราคาของสินค้า
            row[3] = product_json.get("MS_PRODUCT_AMOUNT");//Object อาเรย์ ช่องที่ 4 เก็บข้อมูลของจำนวนของสินค้า
            model.addRow(row);//เพิ่มแถวข้อมูลของตาราง product_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
        }
    }
//---------------------------Put Menu Data into --------------------------------------------
    public void get_order_list(DefaultTableModel table){ //ฟังก์ชั่นการเพิ่มข้อมูลออเดอร์ลงในตาราง
        int total_price = 0; //ตัวแปรเก็บราคารวม
        DefaultTableModel model = table;//ดึงข้อมูลตารางของ parameter มาเก็บไว้ในตัวแปร
        Object[] row = new Object[4];// สร้างอาเรย์ Object ชื่อว่า row ขนาด 4 ช่อง
        DBCollection product_collection  = db.getCollection("TRAN_ORDER_LIST");// ดึงข้อมูลของรายการออเดอร์จาก collection ที่ชื่อ TRAN_ORDER_LIST
        DBCursor product_finding = product_collection.find();// ค้นหาข้อมูลรายการออเดอร์ในcollection(TRAN_ORDER_LIST)ทั้งหมด
        if(product_finding.hasNext()==true){ //ถ้าหากว่ามีข้อมูลของรายการออเดอร์
        while(product_finding.hasNext()){//สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในTRAN_ORDER_LIST
            check_order_list = true; //มีข้อมูลการสั่ง
            DBObject menu_json = product_finding.next();///ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ menu_json
            int menu_id = (int)menu_json.get("MS_MENU_ID"); //เก็บข้อมูลรหัสเมนูในตัวแปร
            row[0] = (int)menu_json.get("TRAN_ORDER_LIST_ID");//Object อาเรย์ ช่องที่ 1 เก็บข้อมูลของรหัสของรายการออเดอร์
            row[1] = find_menu_name(menu_id).get("MS_MENU_NAME");//Object อาเรย์ ช่องที่ 2 เก็บข้อมูลของชื่อเมนู
            //System.out.println(">>>>"+menu_json.get("TRAN_ORDER_LIST_PRICE"));
            row[2] = menu_json.get("TRAN_ORDER_LIST_AMOUNT");//Object อาเรย์ ช่องที่ 3 เก็บข้อมูลของราคาของรายการออเดอร์
            row[3] = menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE");//Object อาเรย์ ช่องที่ 4 เก็บข้อมูลของราคารวมของรายการออเดอร์
            total_price += (int)menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE"); //เพิ่มค่าของราคารวมทั้งหมด
            model.addRow(row);//เพิ่มแถวข้อมูลของตาราง product_table โดยนำข้อมูลมาจาก อาเรย์ของObject ที่ชื่อว่า row
        }
        }else{//ถ้าหากว่าไม่มีข้อมูลของรายการออเดอร์
            check_order_list = false; //ไม่มีข้อมูลการสั่ง
        }
        order_total_txt.setText(""+total_price); //แสดงราคารวมทางหน้าจอ
    }    
        
//---------------------------Find & Check-------------------------------
    public int find_partner(String name){ //ฟังก์ชั่นค้นหารหัสบริษัทคู่ค้าจากชื่อ
        int id = 0; //ตัวแปรเก็บรหัสคู่ค้า
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        DBCollection table = db.getCollection("MS_PARTNER");//ดึงข้อมูลจากCollectionของบริษัทคู่ค้ามาใส่ในตัวแปร
        BasicDBObject partner = new BasicDBObject("MS_PARTNER_NAME",name); //สร้างObjectชื่อ partner เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor cur = table.find(partner); // ค้นหาข้อมูลในcollection(MS_PARTNER)จากชื่อของบริษัท
        while(cur.hasNext()){ //สร้างลูป while โดยมีเงื่อนไขคือจะต้องไม่มีข้อมูลตัวถัดไปในMS_PARTNER
            DBObject kk = cur.next();//ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ kk
            id = (int)kk.get("MS_PARTNER_ID"); //เก็บรหัสของบริษัทคู่ค้าใส่ในตัวแปร
        }
        }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
               e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        return id; //คืนค่ารหัสบริษัทคู่ค้า
    }
    
    public boolean checkpartnerid(int id){//ฟังก์ชั่นหาชื่อของบริษัทคู่ค้าจากรหัส
        DBCollection partnerdata = db.getCollection("MS_PARNTER");//ดึงข้อมูลจากCollectionของบริษัทคู่ค้ามาใส่ในตัวแปร
        BasicDBObject data = new BasicDBObject("MS_PARTNER_ID",id);//สร้างObjectชื่อ data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
        DBCursor find = partnerdata.find(data);// ค้นหาข้อมูลในcollection(MS_PARTNER)จากชื่อของบริษัท
        if(find.hasNext()){ //ถ้าหากว่ามีข้อมูล
            return true; //คืนค่าจริง
        }else{//ถ้าหากว่าไม่มีข้อมูล
            return false; //ค่าค่าเทจ
        }
    }
    
        public DBObject find_product_id(String name){ //ค้นหารหัสของสินค้าจากชื่อ
            DBCollection product = db.getCollection("MS_PRODUCT");//ดึงข้อมูลจากCollectionของสินค้ามาใส่ในตัวแปร
            BasicDBObject data = new BasicDBObject("MS_PRODUCT_NAME",name);//สร้างObjectชื่อ data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
            DBCursor find = product.find(data);// ค้นหาข้อมูลในcollection(MS_MENU)จากชื่อของเมนู
            DBObject product_json = null; //สร้างตัวแปรเปล่าเพื่อใช้ในการเตรียมเก็บข้อมูลของสินค้า
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                product_json = find.next(); //ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ product_json
            }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ");//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            }
        return product_json; //คืนค่าข้อมูลของสินค้า
    }
        public DBObject find_menu_name(int id){ //ค้นหาชื่อของเมนูจากรหัสเมนู
            DBCollection get_menu = db.getCollection("MS_MENU");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
            BasicDBObject data = new BasicDBObject("MS_MENU_ID",id);//สร้างObjectชื่อ data เพื่อเก็บข้อมูลที่จะนำไปค้นหา
            DBCursor find = get_menu.find(data);// ค้นหาข้อมูลในcollection(MS_MENU)จากชื่อของเมนู
            DBObject menu_json = null;//สร้างตัวแปรเปล่าเพื่อใช้ในการเตรียมเก็บข้อมูลของเมนู
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                menu_json = find.next();//ดึงข้อมูลObjectจากการค้นหามาใส่ตัวแปร DBObject ชื่อ menu_json
            }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ");//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            }
        return menu_json; //คืนค่าข้อมูลของเมนู
    }
        
        public int get_order_list_id(int menu_id){ //ฟังก์ชั่นการค้นหารหัสรายการออเดอร์
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");//ดึงข้อมูลจากCollectionของรายการออเดอร์มาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1); //สร้างObjectชื่อ data เพื่อเก็บข้อมูลที่จะนำไปค้นหาข้อมูลตัวสุดท้าย
            DBCursor finding_order_list_id = get_order_list.find().sort(sortObject); // ค้นหาข้อมูลในcollection(MS_MENU)ตัวสุดท้าย
              int order_list_id = -1; //ตัวแปรเก็บรหัสรายการออเดอร์
                if(finding_order_list_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_list_id.next();//สร้างตัวแปรเปล่าเพื่อใช้ในการเตรียมเก็บข้อมูลของรายการออเดอร์
                    order_list_id = 1+(int)data.get("TRAN_ORDER_LIST_ID"); //นำค่าของPKมาบวกด้วย 1
                }else{ 
                    order_list_id = 1;//สร้างPKของ MS_PRODUCT (ตัวแรก)
                }
                return order_list_id; //คืนค่าข้อมูลรหัสรายการออเดอร์
        }
//------------------------logout-----------------------------------
    public void logout(){//ฟังก์ชั่นออกจากระบบ
            //System.out.println("-----------logout----------------");
            DBCollection table = db.getCollection("TRAN_LOG");//ดึงข้อมูลจากCollectionของประวัติการใช้งานมาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObjectชื่อ data เพื่อเก็บข้อมูลที่จะนำไปค้นหาข้อมูลตัวสุดท้าย
            DBCursor cur = table.find().sort(sortObject);// ค้นหาข้อมูลในcollection(TRAN_LOG)ตัวสุดท้าย
            int id = 0; //สร้างตัวแปรเพื่อใช้เก็บรหัสของประวัติการใช้
            DBCursor find = table.find();// ค้นหาข้อมูลของประวัติการใช้ในcollection(TRAN_LOG)ตัวสุดท้าย
            //System.out.println(find.hasNext());
            if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
            //System.out.println("eiei");
            int n = (int)cur.one().get("TRAN_LOG_ID");//นำข้อมูลของรหัสประวัติการใช้มาใส่ในตัวแปร
            id = n+1;//นำค่าของPKมาบวกด้วย 1
            }else{
            id = 1;//ให้รหัสประวัติการใช้มีค่าเป็น 1
            }
            DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE; //สร้างตัวแปรในการกำหนดฟอร์แมทของวันที่
            String formattedDate = formatter.format(LocalDate.now()); //สร้างตัวแปรในการเก็บวันที่ปัจจุบันตามฟอร์แมทของ formatter
            String month = v.month(Integer.parseInt(formattedDate.substring(4,6))); //ใช้งานฟังก์ชั่นแปลงลำดับของเดือนเป็นชื่อเดือน
            String year = formattedDate.substring(0,4); //ปีปัจจุบัน
            String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length()); //วันที่ปัจจุบัน
            BasicDBObject document = new BasicDBObject(); //สร้าง Object เพื่อใช้เก็บข้อมูลที่จะเพิ่มเข้าไปใน collection
            //-----------------------------------------------
            document.put("TRAN_LOG_ID",id); //รหัสของประวัติการใช้งาน
            document.put("TRAN_LOG_DATE",month+" "+date+", "+year);//วันที่ที่ใช้งาน
            document.put("TRAN_LOG_TIME",LocalTime.now().toString().substring(0,8)); //เวลที่ใช้งาน
            document.put("TRAN_LOG_TYPE","Logout"); //สถานะการใช้งาน
            document.put("MS_EMPLOYEE_ID",v.getid()); //รหัสของพนักงาน
            //----------------------------------------------- 
            table.insert(document); //เพิ่มข้อมูลเข้าไปใน collection
            System.out.println("เพิ่มประวัติการเข้าใช้เรียบร้อยแล้ว"); //แสดงข้อความ
    }
    
//-----------------------------Check last session in system-----------------------------------------
    public void sessionnow(){ //ฟังก์ชั่นการใช้งานปัจจุบัน
            db = v.getConnect(); //ใช้งาน Method การเชื่อมต่อ
            DBCollection log = db.getCollection("TRAN_LOG"); //ดึงข้อมูลจาก Collection ของประวัติการใช้งานมาใส่ในตัวแปร
            DBCollection employee = db.getCollection("MS_EMPLOYEE"); //ดึงข้อมูลจาก Collection ของพนักงานมาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObjectชื่อ search เพื่อใช้เก็บข้อมูลที่ใช้ค้นหาตัวสุดท้าย
            DBCursor cur = log.find().sort(sortObject);//ค้นหาข้อมูลทั้งหมดของประวัติการใช้งาน (TRAN_LOG)ตัวสุดท้าย
            int emp_id = (int)cur.one().get("MS_EMPLOYEE_ID"); //กำหนดตัวแปรเพื่อเก็บข้อมูลรหัสของพนักงาน
            BasicDBObject search = new BasicDBObject(); ////สร้างObjectชื่อ search เพื่อใช้เก็บข้อมูลที่ใช้ค้นหาจากรหัสพนักงาน
            search.put("MS_EMPLOYEE_ID",emp_id);
            DBObject findemp = employee.findOne(search); //สร้างตัวแปรเพื่อเก็บการค้นหาข้อมูลพนักงาน
            title_name_txt.setText(" "+findemp.get("MS_EMPLOYEE_NAME").toString()); //แสดงชื่อของพนักงานที่ใช้งานอยู่ขณะนี้
            if(findemp.get("MS_EMPLOYEE_TYPE").toString().equals("Owner")){//ถ้าหากว่าเป็นเจ้าของร้าน
                //แสดงสถานะเจ้าของร้าน
                title_position_txt.setText(" เจ้าของร้าน");
            }else{//ถ้าหากไม่ใช่
                //แสดงสถานะพนักงานและซ่อนฟังก์ชั่นที่ไม่เกี่ยวข้อง
                title_position_txt.setText(" พนักงาน");
                product_btn.setVisible(false);//ซ่อนฟังก์ชั่นสินค้า
                stock_btn.setVisible(false);//ซ่อนฟังก์ชั่นการเพิ่มจำนวนสินค้า
                menu_btn.setVisible(false);//ซ่อนฟังก์ชั่นเมนู
                partner_btn.setVisible(false);//ซ่อนฟังก์ชั่นบริษัทคู่ค้า
                employee_btn.setVisible(false);//ซ่อนฟังก์ชั่นพนักงาน
                history_btn.setVisible(false);//ซ่อนฟังก์ชั่นประวัติการขาย
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

        title_panel = new javax.swing.JPanel();
        title_name_txt = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        title_position_txt = new javax.swing.JLabel();
        picture_label = new javax.swing.JLabel();
        btn_panel = new javax.swing.JPanel();
        customer_btn = new javax.swing.JButton();
        stock_btn = new javax.swing.JButton();
        order_btn = new javax.swing.JButton();
        menu_btn = new javax.swing.JButton();
        partner_btn = new javax.swing.JButton();
        product_btn = new javax.swing.JButton();
        employee_btn = new javax.swing.JButton();
        history_btn = new javax.swing.JButton();
        main_panel = new javax.swing.JPanel();
        first_panel = new javax.swing.JPanel();
        customer_panel = new javax.swing.JPanel();
        customer_type_combo = new javax.swing.JComboBox<>();
        customer_prefix = new javax.swing.JComboBox<>();
        customer_name_txt = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        customer_phone_txt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        customer_email_txt = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        customer_home_txt = new javax.swing.JTextField();
        customer_locality_txt = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        customer_district_txt = new javax.swing.JTextField();
        customer_post_txt = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        customer_province_txt = new javax.swing.JTextField();
        customer_clear_btn = new javax.swing.JButton();
        customer_commit_btn = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        customer_birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel22 = new javax.swing.JLabel();
        edit_customer_btn = new javax.swing.JButton();
        product_panel = new javax.swing.JPanel();
        pro_name_txt = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        pro_price_txt = new javax.swing.JTextField();
        product_clear_btn = new javax.swing.JButton();
        product_commit_btn = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        pro_type_combo = new javax.swing.JComboBox<>();
        partner_combo = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        edit_product_btn = new javax.swing.JButton();
        stock_panel = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        stock_amount = new javax.swing.JTextField();
        stock_clear_btn = new javax.swing.JButton();
        stock_commit_btn = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        stock_combo = new javax.swing.JComboBox<>();
        menu_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        menu_table = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        menu_price_txt = new javax.swing.JTextField();
        menu_name_txt = new javax.swing.JTextField();
        menu_clear_btn = new javax.swing.JButton();
        menu_commit_btn = new javax.swing.JButton();
        edit_menu_btn = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        menu_product_table = new javax.swing.JTable();
        partner_panel = new javax.swing.JPanel();
        partner_name_txt = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
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
        partner_clear_btn = new javax.swing.JButton();
        partner_commit_btn = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        partner_type_combo = new javax.swing.JComboBox<>();
        edit_partner_btn = new javax.swing.JButton();
        order_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        order_table = new javax.swing.JTable();
        order_clear_btn = new javax.swing.JButton();
        ordering_btn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        menu_order_table = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        order_total_txt = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        adding_order_btn = new javax.swing.JButton();
        employee_panel = new javax.swing.JPanel();
        confirm = new javax.swing.JCheckBox();
        employee_clear_btn = new javax.swing.JButton();
        employee_commit_btn = new javax.swing.JButton();
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
        jLabel4 = new javax.swing.JLabel();
        edit_employee_btn = new javax.swing.JButton();
        history_panel = new javax.swing.JPanel();
        history_combo = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        history_table = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        total_price_txt = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("หน้าต่างหลัก");
        setMinimumSize(new java.awt.Dimension(815, 640));
        setPreferredSize(new java.awt.Dimension(915, 640));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_name_txt.setText(" ");
        title_panel.add(title_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 17, 250, 20));

        jLabel2.setText("ยินดีต้อนรับคุณ:");
        title_panel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        jButton1.setText("ออกจากระบบ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        title_panel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, -1, -1));

        jLabel3.setText("ตำแหน่ง:");
        title_panel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, -1));

        title_position_txt.setText(" ");
        title_panel.add(title_position_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 47, 170, 20));
        title_panel.add(picture_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        getContentPane().add(title_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 100));

        btn_panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customer_btn.setText("เพิ่มข้อมูลลูกค้า");
        customer_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_btnActionPerformed(evt);
            }
        });
        btn_panel.add(customer_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 120, 40));

        stock_btn.setText("เพิ่มสินค้าในสต๊อก");
        stock_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stock_btnActionPerformed(evt);
            }
        });
        btn_panel.add(stock_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 120, 40));

        order_btn.setText("หน้าต่างการขาย");
        order_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                order_btnActionPerformed(evt);
            }
        });
        btn_panel.add(order_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 40));

        menu_btn.setText("จัดการเมนู");
        menu_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_btnActionPerformed(evt);
            }
        });
        btn_panel.add(menu_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 110, 40));

        partner_btn.setText("เพิ่มคู่ค้า");
        partner_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_btnActionPerformed(evt);
            }
        });
        btn_panel.add(partner_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, 100, 40));

        product_btn.setText("เพิ่มสินค้า");
        product_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_btnActionPerformed(evt);
            }
        });
        btn_panel.add(product_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 120, 40));

        employee_btn.setText("เพิ่มพนักงาน");
        employee_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_btnActionPerformed(evt);
            }
        });
        btn_panel.add(employee_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 100, 40));

        history_btn.setText("<html><body><center>ประวัติ<br>การขาย</center></body></html>");
        history_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                history_btnActionPerformed(evt);
            }
        });
        btn_panel.add(history_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 0, 110, 40));

        getContentPane().add(btn_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 900, 40));

        main_panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        main_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        first_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        main_panel.add(first_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 910, 460));

        customer_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customer_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ลูกค้าใหม่", "ลูกค้าขาจร" }));
        customer_panel.add(customer_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, -1, -1));

        customer_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นาง", "นางสาว", "เด็กชาย", "เด็กหญิง" }));
        customer_panel.add(customer_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));
        customer_panel.add(customer_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 320, -1));

        jLabel24.setText("ประเภทลูกค้า:");
        customer_panel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, -1));

        jLabel25.setText("ชื่อ-สกุล:");
        customer_panel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jLabel26.setText("เบอร์โทรศัพท์:");
        customer_panel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, -1, -1));
        customer_panel.add(customer_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 120, -1));

        jLabel27.setText("อีเมล:");
        customer_panel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 150, -1, -1));
        customer_panel.add(customer_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 150, -1));

        jLabel28.setText("วันเกิด:");
        customer_panel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, -1, -1));
        customer_panel.add(customer_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 50, 20));
        customer_panel.add(customer_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 50, 20));

        jLabel29.setText("ตำบล:");
        customer_panel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 230, -1, -1));

        jLabel30.setText("อำเภอ:");
        customer_panel.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, -1, -1));
        customer_panel.add(customer_district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 50, 20));
        customer_panel.add(customer_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 50, 20));

        jLabel31.setText("รหัสไปรษณีย์:");
        customer_panel.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, -1, -1));

        jLabel32.setText("จังหวัด:");
        customer_panel.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, -1, -1));
        customer_panel.add(customer_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 140, 20));

        customer_clear_btn.setText("ล้างข้อมูล");
        customer_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_clear_btnActionPerformed(evt);
            }
        });
        customer_panel.add(customer_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 320, 110, 50));

        customer_commit_btn.setText("บันทึก");
        customer_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_commit_btnActionPerformed(evt);
            }
        });
        customer_panel.add(customer_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 110, 50));

        jLabel33.setText("บ้านเลขที่:");
        customer_panel.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, -1, -1));

        customer_birthdate_txt.setFormat(1);
        customer_panel.add(customer_birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, -1, 20));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel22.setText("เพิ่มข้อมูลลูกค้า");
        customer_panel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        edit_customer_btn.setText("แก้ไขข้อมูลลูกค้า");
        edit_customer_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_customer_btnActionPerformed(evt);
            }
        });
        customer_panel.add(edit_customer_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, -1));

        main_panel.add(customer_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        product_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        product_panel.add(pro_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 310, -1));

        jLabel46.setText("ชื่อสินค้า:");
        product_panel.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        jLabel53.setText("ราคา:");
        product_panel.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));
        product_panel.add(pro_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 70, -1));

        product_clear_btn.setText("ล้างข้อมูล");
        product_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_clear_btnActionPerformed(evt);
            }
        });
        product_panel.add(product_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 110, 50));

        product_commit_btn.setText("บันทึก");
        product_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_commit_btnActionPerformed(evt);
            }
        });
        product_panel.add(product_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 110, 50));

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel60.setText("เพิ่มข้อมูลสินค้า");
        product_panel.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, -1));

        jLabel42.setText("ประเภทของสินค้า:");
        product_panel.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, -1, -1));

        pro_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกประเภทของสินค้า", "ส่วนประกอบเครื่องดื่ม", "เบเกอรี่", "ส่วนประกอบของคาว" }));
        pro_type_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_type_comboActionPerformed(evt);
            }
        });
        product_panel.add(pro_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 180, 30));

        partner_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกบริษัทคู่ค้า" }));
        product_panel.add(partner_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, 180, 30));

        jLabel63.setText("บริษัทคู่ค้า:");
        product_panel.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, -1, -1));

        edit_product_btn.setText("แก้ไขข้อมูลสินค้า");
        edit_product_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_product_btnActionPerformed(evt);
            }
        });
        product_panel.add(edit_product_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, -1, -1));

        main_panel.add(product_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        stock_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setText("ชื่อสินค้า:");
        stock_panel.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, -1, -1));

        jLabel55.setText("จำนวนที่เพิ่มเข้าไปในสต๊อก:");
        stock_panel.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, -1, 20));
        stock_panel.add(stock_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 70, -1));

        stock_clear_btn.setText("ล้างข้อมูล");
        stock_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stock_clear_btnActionPerformed(evt);
            }
        });
        stock_panel.add(stock_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 240, 110, 50));

        stock_commit_btn.setText("บันทึก");
        stock_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stock_commit_btnActionPerformed(evt);
            }
        });
        stock_panel.add(stock_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, 110, 50));

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel61.setText("เพิ่มข้อมูลสินค้าในสต๊อก");
        stock_panel.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, -1, -1));

        stock_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกสินค้า" }));
        stock_panel.add(stock_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 110, 30));

        main_panel.add(stock_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        menu_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "จำนวน"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(menu_table);

        menu_panel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 390, 280));

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setText("ตารางสินค้า");
        menu_panel.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, -1, -1));

        jLabel57.setText("ราคา:");
        menu_panel.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, -1, -1));

        jLabel58.setText("ชื่อเมนู:");
        menu_panel.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel59.setText("หน้าต่างการสร้างเมนู");
        menu_panel.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("รายละเอียดของเมนู");
        menu_panel.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, -1, -1));
        menu_panel.add(menu_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 60, -1));
        menu_panel.add(menu_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 90, -1));

        menu_clear_btn.setText("ล้างข้อมูล");
        menu_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_clear_btnActionPerformed(evt);
            }
        });
        menu_panel.add(menu_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, -1, 30));

        menu_commit_btn.setText("ยืนยัน");
        menu_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_commit_btnActionPerformed(evt);
            }
        });
        menu_panel.add(menu_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 80, 30));

        edit_menu_btn.setText("แก้ไขเมนู");
        edit_menu_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_menu_btnActionPerformed(evt);
            }
        });
        menu_panel.add(edit_menu_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, -1, 30));

        menu_product_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "ราคา", "จำนวนที่มีอยู่"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        menu_product_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_product_tableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(menu_product_table);

        menu_panel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 390, 200));

        main_panel.add(menu_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        partner_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        partner_panel.add(partner_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 310, -1));

        jLabel43.setText("ชื่อบริษัท:");
        partner_panel.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, -1, -1));

        jLabel44.setText("เบอร์โทรศัพท์:");
        partner_panel.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, -1, -1));
        partner_panel.add(partner_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 120, -1));

        jLabel45.setText("อีเมล:");
        partner_panel.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, -1, -1));
        partner_panel.add(partner_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 150, -1));

        partner_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_home_txtActionPerformed(evt);
            }
        });
        partner_panel.add(partner_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 50, 20));

        partner_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_locality_txtActionPerformed(evt);
            }
        });
        partner_panel.add(partner_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 50, 20));

        jLabel47.setText("ตำบล:");
        partner_panel.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 190, -1, -1));

        jLabel48.setText("อำเภอ:");
        partner_panel.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 190, -1, -1));

        partner_distict_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_distict_txtActionPerformed(evt);
            }
        });
        partner_panel.add(partner_distict_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, 50, 20));

        partner_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_post_txtActionPerformed(evt);
            }
        });
        partner_panel.add(partner_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 50, 20));

        jLabel49.setText("รหัสไปรษณีย์:");
        partner_panel.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 230, -1, -1));

        jLabel50.setText("จังหวัด:");
        partner_panel.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, -1, -1));

        partner_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_province_txtActionPerformed(evt);
            }
        });
        partner_panel.add(partner_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 230, 140, 20));

        partner_clear_btn.setText("ล้างข้อมูล");
        partner_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_clear_btnActionPerformed(evt);
            }
        });
        partner_panel.add(partner_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 320, 110, 50));

        partner_commit_btn.setText("บันทึก");
        partner_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partner_commit_btnActionPerformed(evt);
            }
        });
        partner_panel.add(partner_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 110, 50));

        jLabel51.setText("บ้านเลขที่:");
        partner_panel.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, -1, -1));

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel52.setText("เพิ่มข้อมูลคู่ค้า");
        partner_panel.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        jLabel39.setText("ประเภทของคู่ค้า:");
        partner_panel.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, -1, -1));

        partner_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "คู่ค้าส่วนประกอบเครื่องดื่ม", "คู่ค้าของหวานเบเกอรี่", "คู่ค้าส่วนประกอบของคาว" }));
        partner_panel.add(partner_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 180, 30));

        edit_partner_btn.setText("แก้ไขข้อมูลคู่ค้า");
        edit_partner_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_partner_btnActionPerformed(evt);
            }
        });
        partner_panel.add(edit_partner_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        main_panel.add(partner_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        order_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        if (order_table.getColumnModel().getColumnCount() > 0) {
            order_table.getColumnModel().getColumn(0).setPreferredWidth(40);
            order_table.getColumnModel().getColumn(1).setPreferredWidth(200);
            order_table.getColumnModel().getColumn(2).setPreferredWidth(40);
            order_table.getColumnModel().getColumn(3).setPreferredWidth(40);
        }

        order_panel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 380, 270));

        order_clear_btn.setText("เคลียร์");
        order_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                order_clear_btnActionPerformed(evt);
            }
        });
        order_panel.add(order_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, 100, 30));

        ordering_btn.setText("สั่ง");
        ordering_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordering_btnActionPerformed(evt);
            }
        });
        order_panel.add(ordering_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 420, 100, 30));

        menu_order_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสสินค้า", "ชื่อ", "ราคา"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        menu_order_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_order_tableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(menu_order_table);

        order_panel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 390, 270));

        jLabel35.setForeground(new java.awt.Color(255, 0, 0));
        jLabel35.setText("เพิ่มสินค้า");
        order_panel.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, 60, 20));

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel36.setText("ตารางออเดอร์");
        order_panel.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, -1, -1));

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel37.setText("ตารางสินค้า");
        order_panel.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, -1, -1));

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel38.setText("หน้าต่างการสั่งออเดอร์");
        order_panel.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        order_total_txt.setEditable(false);
        order_total_txt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        order_total_txt.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        order_panel.add(order_total_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 380, 93, 30));

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("ราคารวม");
        order_panel.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 380, -1, -1));

        adding_order_btn.setText(">>");
        adding_order_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adding_order_btnActionPerformed(evt);
            }
        });
        order_panel.add(adding_order_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 60, -1));

        main_panel.add(order_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        employee_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confirm.setText("ข้อมูลที่ท่านกรอกมาเป็นความจริงทั้งหมด");
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });
        employee_panel.add(confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, -1, -1));

        employee_clear_btn.setText("ล้างข้อมูล");
        employee_clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_clear_btnActionPerformed(evt);
            }
        });
        employee_panel.add(employee_clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 410, 110, 40));

        employee_commit_btn.setText("ยืนยัน");
        employee_commit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_commit_btnActionPerformed(evt);
            }
        });
        employee_panel.add(employee_commit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 410, 110, 40));

        showpwd_check.setText("แสดงรหัสผ่าน");
        showpwd_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpwd_checkActionPerformed(evt);
            }
        });
        employee_panel.add(showpwd_check, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 290, -1, -1));

        employee_district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_district_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 100, 50, 20));

        employee_age_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกอายุ" }));
        employee_panel.add(employee_age_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 80, 20));

        man_radio.setText("ชาย");
        employee_panel.add(man_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));

        woman_radio.setText("หญิง");
        employee_panel.add(woman_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));

        jLabel5.setText("วัน/เดือน/ปีเกิด:");
        employee_panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, -1, -1));

        jLabel6.setText("อายุ:");
        employee_panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        employee_birthdate_txt.setWeekStyle(datechooser.view.WeekDaysStyle.FULL);
        employee_panel.add(employee_birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, -1, -1));

        jLabel7.setText("เพศ:");
        employee_panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 135, -1, -1));

        employee_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นางสาว" }));
        employee_panel.add(employee_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel9.setText("ชื่อ-สกุล:");
        employee_panel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, -1, -1));
        employee_panel.add(employee_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 280, 20));

        jLabel11.setText("อำเภอ:");
        employee_panel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, -1, -1));

        employee_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_locality_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 50, 20));

        jLabel12.setText("ตำบล:");
        employee_panel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, -1, -1));

        employee_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_home_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 50, 20));

        jLabel13.setText("บ้านเลขที่:");
        employee_panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, -1, -1));

        employee_email_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_email_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, 140, 20));

        jLabel14.setText("จังหวัด:");
        employee_panel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, -1, -1));

        jLabel15.setText("ตำแหน่ง:");
        employee_panel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, -1, -1));

        employee_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_post_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 140, 50, 20));

        jLabel16.setText("อีเมล:");
        employee_panel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        employee_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_province_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 140, 20));

        jLabel17.setText("ชื่อผู้ใช้งาน:\n");
        employee_panel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, -1));

        employee_user_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_user_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_user_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 140, 20));

        jLabel18.setText("รหัสผ่าน:");
        employee_panel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 290, -1, -1));

        employee_position_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกตำแหน่ง", "Employee", "Owner" }));
        employee_panel.add(employee_position_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, -1, -1));

        jLabel19.setText("รหัสไปรษณีย์:");
        employee_panel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 140, -1, -1));

        employee_phone_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_phone_txtActionPerformed(evt);
            }
        });
        employee_panel.add(employee_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 140, 20));

        jLabel20.setText("เบอร์โทรศัพท์:");
        employee_panel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        employee_pwd_txt.setToolTipText("");
        employee_pwd_txt.setEchoChar('*');
        employee_panel.add(employee_pwd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, 140, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("หน้าต่างเพิ่มพนักงาน");
        employee_panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        edit_employee_btn.setText("แก้ไขพนักงาน");
        edit_employee_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_employee_btnActionPerformed(evt);
            }
        });
        employee_panel.add(edit_employee_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        main_panel.add(employee_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        history_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        history_combo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        history_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกเดือน", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มีนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" }));
        history_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                history_comboActionPerformed(evt);
            }
        });
        history_panel.add(history_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 150, 30));

        history_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัสออเดอร์", "ราคารวม", "วันที่", "เวลา"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        history_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                history_tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(history_table);

        history_panel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 570, 330));

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("ยอดรวมทั้งหมด                                                บาท");
        history_panel.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, -1, -1));

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel41.setText("เดือน:");
        history_panel.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        total_price_txt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        total_price_txt.setText("0.0");
        history_panel.add(total_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, 90, 20));

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("*ดับเบิ้ลคลิ๊กเพื่อดูรายละเอียดของออเดอร์");
        history_panel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 400, 230, 20));

        main_panel.add(history_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        getContentPane().add(main_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 900, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
            //เมื่อหน้าจอนี้ปิดลง
            Login g = new Login(); //สร้าง Object ของหน้าต่าง login
            g.setVisible(true); //แสดงหน้าต่าง login
            this.setVisible(false); //ซ่อนหน้าจอนี้
            logout(); //ใช้งานฟังก์ชั่นlogout
    }//GEN-LAST:event_formWindowClosing

    private void order_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_btnActionPerformed
        //หน้าต่างออเดอร์
        disablepanel(); //ซ่อนทุกหน้าต่าง
        order_panel.setVisible(true); //แสดงหน้าต่างออเดรอ์
        clear_table((DefaultTableModel)menu_order_table.getModel()); //ลบข้อมูลทั้งหมดของตารางเมนู
        clear_table((DefaultTableModel)order_table.getModel());//ลบข้อมูลทั้งหมดของตารางออเดอร์
        get_menu((DefaultTableModel)menu_order_table.getModel()); //ใช้งานฟังก์ชั่นเรียกข้อมูลของเมนูใส่ในตาราง
        get_order_list((DefaultTableModel)order_table.getModel());//ใช้งานฟังก์ชั่นเรียกข้อมูลของออเดอร์ใส่ในตาราง
        set_order_table((DefaultTableModel)menu_product_table.getModel()); //ใช้งานฟังก์ชั่นเรียกข้อมูลของสินค้าใส่ในตาราง
        if(check_order_list==true){//ถ้าหากว่ามีออเดอร์
            ordering_btn.setEnabled(true); //เปิดการใช้งานปุ่มการสั่ง
        }else{//ถ้าหากไม่มีออเดอร์
            ordering_btn.setEnabled(false);//ปิดการใช้งานปุ่มการสั่ง
        }
        this.setTitle("หน้าต่างการออเดอร์");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_order_btnActionPerformed

    private void employee_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_btnActionPerformed
        //หน้าต่างพนักงาน
        disablepanel(); //ซ่อนทุกหน้าต่าง
        emp_age_combo(); //ใช้งานฟังก์ชั่นเพิ่มอายุ
        employee_panel.setVisible(true); //แสดงหน้าต่างพนักงาน
        this.setTitle("หน้าต่างการเพิ่มพนักงาน");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_employee_btnActionPerformed

    private void customer_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btnActionPerformed
        disablepanel(); //ซ่อนทุกหน้าต่าง
        customer_panel.setVisible(true); //แสดงหน้าต่างลูกค้า
        this.setTitle("หน้าต่างเพิ่มลูกค้า");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_customer_btnActionPerformed

    private void product_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_btnActionPerformed
        disablepanel();//ซ่อนทุกหน้าต่าง
        product_panel.setVisible(true);//แสดงหน้าต่างสินค้า
        this.setTitle("หน้าต่างเพิ่มสินค้า");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_product_btnActionPerformed

    private void stock_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stock_btnActionPerformed
        disablepanel();//ซ่อนทุกหน้าต่าง
        stock_panel.setVisible(true);//แสดงหน้าต่างเพิ่มจำนวนสินค้า
        this.setTitle("หน้าต่างเพิ่มสต๊อก");//เปลี่ยนชื่อหน้าต่าง
        set_stocking_product_combo();//ใช้งานฟังก์ชั่นเพิ่มจำนวน
    }//GEN-LAST:event_stock_btnActionPerformed

    private void menu_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_btnActionPerformed
        disablepanel();//ซ่อนทุกหน้าต่าง
        clear_table((DefaultTableModel)menu_product_table.getModel()); //ลบข้อมูลในตารางของเมนู
        get_product((DefaultTableModel)menu_product_table.getModel()); //ดึงข้อมูลจาก Database ลงในตาราง
        menu_panel.setVisible(true);//แสดงหน้าต่างเมนู
        this.setTitle("หน้าต่างการจัดการเมนู");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_menu_btnActionPerformed

    private void partner_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_btnActionPerformed
        disablepanel();//ซ่อนทุกหน้าต่าง
        partner_panel.setVisible(true);//แสดงหน้าต่างบริษัทคู่ค้า
        this.setTitle("หน้าต่างการเพิ่มคู่ค้า");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_partner_btnActionPerformed

    private void customer_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_clear_btnActionPerformed
        clear_customer(); //เคลียร์ข้อมูลของลูกค้าทั้งหมด
    }//GEN-LAST:event_customer_clear_btnActionPerformed

    private void customer_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_commit_btnActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลลูกค้าใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงหน้าต่างยืนยันการเพิ่มข้อมูลลูกค้า
        if(check_null_customer()){
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_CUSTOMER");//ดึงข้อมูลจาก Collection ของลูกค้ามาใส่ในตัวแปร
                BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObject เพื่อค้นหาข้อมูลตัวสุดท้าย
                DBCursor cur = table.find().sort(sortObject);//ค้นหาข้อมูลโดยจัดเรียงตาม sortObject
                String cus_id = null; //สร้างตัวแปรเพื่อใช้เก็บรหัสของลูกค้า
                DBCursor find = table.find(); //ค้นหาข้อมูลทั้งหมดของลูกค้า
                //System.out.println(find.hasNext());
                if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลของลูกค้า
                    //System.out.println("OWIOA"+Integer.valueOf(cur.one().get("MS_CUSTOMER_ID").toString().substring(1,cur.one().get("MS_CUSTOMER_ID").toString().length())));
                    int k = Integer.valueOf(cur.one().get("MS_CUSTOMER_ID").toString().substring(1,cur.one().get("MS_CUSTOMER_ID").toString().length()));
                    //สร้างตัวแปรมาเก็บข้อมูลรหัสลูกค้าเป็นตัวเลข
                    k += 1; //เพิ่มจำนวนอีก 1
                    /*
                        เช็คจำนวนและปรับให้รหัสลูกค้ามีความถูกต้อง
                    */
                    if(k>=10){
                        cus_id = "C0"+k;
                    }else if(k>100){
                        cus_id = "C"+k;
                    }else if(k<10){
                        cus_id = "C00"+k;
                    }
                }else{
                    cus_id = "C00"+1;
                }
                //System.out.println(cus_id);
                BasicDBObject document = new BasicDBObject();//สร้างObject เพื่อค้นหาข้อมูลที่จะเพิ่มเข้าไปใน database
                String birthdate =customer_birthdate_txt.getText().toString(); //กำหนดตัวแปรเพื่อเก็บค่าวันเกิดของลูกค้า
                document.put("MS_CUSTOMER_ID",cus_id);//รหัสของลูกค้า
                document.put("MS_CUSTOMER_NAME",customer_prefix.getSelectedItem().toString()+customer_name_txt.getText()); //ชื่อของลูกค้า
                document.put("MS_CUSTOMER_PHONE",customer_phone_txt.getText());//เบอร์โทรศัพท์ของลูกค้า
                document.put("MS_CUSTOMER_EMAIL",customer_email_txt.getText());//อีเมลของลูกค้า
                BasicDBObject address = new BasicDBObject();//สร้างObject เพื่อเก็บข้อมูลที่อยู่ของลูกค้า
                /***************************ที่อยู่ของลูกค้า*****************************************/
                address.put("บ้านเลขที่", customer_home_txt.getText());
                address.put("ตำบล", customer_locality_txt.getText());
                address.put("อำเภอ", customer_district_txt.getText());
                address.put("จังหวัด", customer_province_txt.getText());
                address.put("รหัสไปรษณีย์", customer_post_txt.getText());
                /******************************************************************************/
                document.put("MS_CUSTOMER_ADDRESS",address); //ที่อยู่ของลูกค้่า
                document.put("MS_CUSTOMER_BIRTHDATE",birthdate);//วันเกิดของลูกค้า
                /*
                    เช็คประเภทของลูกค้า
                    0 - ลูกค้าใหม่
                    1 - ลูกค้าเก่า
                
                */
                if(customer_type_combo.getSelectedIndex()==0){
                    document.put("MS_CUSTOMER_TYPE","New Customer");
                }else if(customer_type_combo.getSelectedIndex()==1){
                    document.put("MS_CUSTOMER_TYPE","Patron");
                }
                
                table.insert(document);//เพิ่มข้อมูลลงใน database
                clear_customer();//ล้างข้อมูลทั้งหมดของลูกค้า
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ");//แสดงข้อความเมื่อเพิ่มประวัติการใช้งานสำเร็จ
            }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            }
        }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
            }
        }else{
            JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่สมบูรณ์\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);//แสดงข้อความเมื่อเพิ่มประวัติการใช้งานสำเร็จ
            clear_customer();//ล้างข้อมูลทั้งหมดของลูกค้า
        }
        }
    }//GEN-LAST:event_customer_commit_btnActionPerformed

    private void ordering_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordering_btnActionPerformed
        Order_confirm o = new Order_confirm(); //สร้างObject ของหน้าต่างยืนยันออเดอร์
        this.setVisible(false);//ซ่อนหน้านี้
        o.setVisible(true);//แสดงหน้าต่างยืนยันออเดอร์
    }//GEN-LAST:event_ordering_btnActionPerformed

    private void history_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_history_btnActionPerformed
        disablepanel();//ซ่อนหน้าต่างทั้งหมด
        history_panel.setVisible(true);//แสดงหน้าต่างประวัติการขาย
        this.setTitle("หน้าต่างประวัติออเดอร์");//เปลี่ยนชื่อหน้าต่าง
    }//GEN-LAST:event_history_btnActionPerformed

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

    private void partner_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_clear_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partner_clear_btnActionPerformed

    private void partner_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_commit_btnActionPerformed
    int partner_id = 0;
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลบริษัทคู่ค้าใหม่ใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงผลหน้าจอประเภท2คำตอบ
        if(check_null_partner()){
        try{//ดักจับข้อผิดพลาดโดยใช้ try-catch
            try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_PARTNER"); //ดึงข้อมูลของcollection MS_PRODUCT มาใส่ในตัวแปรที่ชื่อว่า table
                BasicDBObject partnerobject = new BasicDBObject().append("_id", -1); //ดึงข้อมูลตัวสุดท้าย
                System.out.println(partnerobject);
                DBCursor find = table.find().sort(partnerobject); //ค้นหาข้อมูลทั้งหมดในcollection MS_PRODUCT
                //System.out.println(find.hasNext());
                if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject getdoc = find.next();//นำข้อมูลของบริษัทคู่ค้าไปใส่ในตัวแปร getdoc
                    System.out.println((int)getdoc.get("MS_PARTNER_ID"));
                    partner_id = 1+(int)getdoc.get("MS_PARTNER_ID"); //นำค่าของPKมาบวกด้วย 1
                }else{
                    partner_id = 1;//สร้างPKของ MS_PRODUCT
                }
                //System.out.println(productid);
                //สร้างการเก็บข้อมูลที่อยู่เป็นชุดข้อมูลแยก
                BasicDBObject document = new BasicDBObject(); //สร้างการเก็บข้อมูลใหม่
                document.put("MS_PARTNER_ID",(int)partner_id); //เพิ่มข้อมูลรหัสของสินค้า
                document.put("MS_PARTNER_NAME",partner_name_txt.getText()); //เพิ่มข้อมูลชื่อ
                document.put("MS_PARTNER_PHONE",partner_phone_txt.getText()); //ตั้งจำนวนให้มีค่าเท่ากับ 0
                document.put("MS_PARTNER_EMAIL",Integer.parseInt(partner_email_txt.getText())); //เพิ่มข้อมูลอีเมล
                BasicDBObject partner_address = new BasicDBObject();//สร้างการเก็บข้อมูลใหม่
                partner_address.put("บ้านเลขที่", partner_home_txt.getText());
                partner_address.put("ตำบล", partner_locality_txt.getText());
                partner_address.put("อำเภอ", partner_distict_txt.getText());
                partner_address.put("จังหวัด", partner_province_txt.getText());
                partner_address.put("รหัสไปรษณีย์", partner_post_txt.getText());
                document.put("MS_PARTNER_ADDRESS",partner_address); //เพิ่มข้อมูลอีเมล
                String type = null; //สร้างตัวแปรเปล่าเพื่อเก็บประเภทของสินค้า

                /*

                  index 0 = เครื่องดื่ม
                  index 1 = เบเกอรี่
                  index 2 = ของคาว

                */

                if(partner_type_combo.getSelectedIndex()==0){
                   type = "Drink Partner";
                }else if(partner_type_combo.getSelectedIndex()==1){
                    type = "Bakery Partner";
                }else if(partner_type_combo.getSelectedIndex()==2){
                    type = "Meal Partner";
                }
                document.put("MS_PARTNER_TYPE",type);//เพิ่มข้อมูลประเภท
                table.insert(document); //เพิ่มชุดข้อมูลในcollection
                clear_partner();//เคลียร์ข้อมูลคู่ค้า
                JOptionPane.showMessageDialog(null,"เพิ่มข้อมูลสินค้าเรียบร้อยแล้วค่ะ"); //แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ
                //this.setVisible(false);
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        }else{
             clear_partner();//เคลียร์ข้อมูลคู่ค้า
             JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่สมบูรณ์\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);//แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ 
        }
        }
    }//GEN-LAST:event_partner_commit_btnActionPerformed

    private void product_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_clear_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_product_clear_btnActionPerformed

    private void product_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_commit_btnActionPerformed
    int productid = 0;//ตัวแปรที่ใช้เก็บรหัสของสินค้า
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลสินค้าใหม่ใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงผลหน้าจอประเภท2คำตอบ
        if(check_null_product()){
        try{//ดักจับข้อผิดพลาดโดยใช้ try-catch
            try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_PRODUCT"); //ดึงข้อมูลของcollection MS_PRODUCT มาใส่ในตัวแปรที่ชื่อว่า table
                BasicDBObject productobject = new BasicDBObject().append("_id", -1); //ดึงข้อมูลตัวสุดท้าย
                DBCursor find = table.find().sort(productobject); //ค้นหาข้อมูลทั้งหมดในcollection MS_PRODUCT
                //System.out.println(find.hasNext());
                if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = find.next();//นำข้อมูลของสินค้าไปใส่ในตัวแปร data
                    productid = 1+(int)data.get("MS_PRODUCT_ID"); //นำค่าของPKมาบวกด้วย 1
                }else{
                    productid = 1;//สร้างPKของ MS_PRODUCT
                }
                BasicDBObject document = new BasicDBObject(); //สร้างการเก็บข้อมูลใหม่
                document.put("MS_PRODUCT_ID",(int)productid); //เพิ่มข้อมูลรหัสของสินค้า
                document.put("MS_PRODUCT_NAME",pro_name_txt.getText()); //เพิ่มข้อมูลชื่อ
                document.put("MS_PRODUCT_AMOUNT",(double)0); //ตั้งจำนวนให้มีค่าเท่ากับ 0
                document.put("MS_PRODUCT_PRICE",Double.parseDouble(pro_price_txt.getText())); //เพิ่มข้อมูลราคา
                String type = null; //สร้างตัวแปรเปล่าเพื่อเก็บประเภทของสินค้า

                /*

                  index 0 = เครื่องดื่ม
                  index 1 = เบเกอรี่
                  index 2 = ของคาว

                */

                if(pro_type_combo.getSelectedIndex()==1){
                   type = "Drink";
                }else if(pro_type_combo.getSelectedIndex()==2){
                    type = "Bakery";
                }else if(pro_type_combo.getSelectedIndex()==3){
                    type = "Meal";
                }
                document.put("MS_PRODUCT_TYPE",type);//เพิ่มข้อมูลประเภท

                document.put("MS_PARTNER_ID", find_partner(partner_combo.getSelectedItem().toString()));//เพิ่มข้อมูลประเภท
                table.insert(document); //เพิ่มชุดข้อมูลในcollection
                clear_product();//ล้างข้อมูลสินค้า
                JOptionPane.showMessageDialog(null,"เพิ่มข้อมูลสินค้าเรียบร้อยแล้วค่ะ"); //แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ
                //this.setVisible(false);
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        }else{
            clear_product();//ล้างข้อมูลสินค้า
            JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่สมบูรณ์\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);//แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ
        }
        }
    }//GEN-LAST:event_product_commit_btnActionPerformed

    private void stock_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stock_clear_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stock_clear_btnActionPerformed

    private void stock_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stock_commit_btnActionPerformed
        if(stock_combo.getSelectedIndex()!=0){ //ถ้าหากว่ามีการเลือกเมนู
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            double amount = Double.parseDouble(stock_amount.getText()); //ตัวแปรเก็บข้อมูลของจำนวนที่จะแก้ไข
            String product_name = stock_combo.getSelectedItem().toString(); //ตัวแปรเก็บข้อมูลชื่อของสินค้า
            DBCollection partner = db.getCollection("MS_PRODUCT");//ดึงข้อมูลจากCollectionของสินค้ามาใส่ในตัวแปร
            DBObject product_json = find_product_id(product_name); //ใช้งานฟังก์ชั่นค้นหารหัสสินค้าจากชื่อสินค้า
            if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า : "+product_name+
                                               "\nจำนวนที่มีอยู่  : "+(double)product_json.get("MS_PRODUCT_AMOUNT")+
                                               "\nจำนวนที่จะเพิ่มเข้าไป : "+amount+
                                               "\n\nคุณยืนยันที่จะเพิ่มสินค้าตามจำนวนข้างต้นใช่หรือไม่","",YES_NO_OPTION)==YES_OPTION){
                
                /********************************************/
                /*หน้าต่างยืนยันการเพิ่มสินค้า                       */
                /*ชื่อสินค้า :                                  */
                /*จำนวนที่มีอยู่ :                               */
                /*จำนวนที่จะเพิ่มเข้าไป :                         */
                /*                                          */
                /*คุณยืนยันที่จะเพิ่มสินค้าตามจำนวนข้างต้นใช่หรือไม่      */
                /*                                          */
                /********************************************/
                
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                BasicDBObject product_id = new BasicDBObject("MS_PRODUCT_ID",product_json.get("MS_PRODUCT_ID")); //สร้างObjectชื่อ partner_id โดยใช้รหัสของสินค้าในการค้นหา
                BasicDBObject updateFields = new BasicDBObject(); //สร้างObjectชื่อ updateFields เพื่อเก็บข้อมูลที่จะนำไปแก้ไขจากข้อมูลเดิม
                BasicDBObject setQuery = new BasicDBObject();//สร้างObjectชื่อ setQuery เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
                updateFields.append("MS_PRODUCT_AMOUNT",(double)product_json.get("MS_PRODUCT_AMOUNT")+amount);//แก้ไขข้อมูลจำนวนของ
                setQuery.append("$set", updateFields); //ตั้งค่าฟังก์ชั่นที่จะใช้กับข้อมูล
                partner.update(product_id,setQuery);//ทำการอัพเดทข้อมูลที่ตรวจพบใน database
                product_json = find_product_id(stock_combo.getSelectedItem().toString());
                JOptionPane.showMessageDialog(null, "ทำรายการเสร็จสิ้น!!\n"+
                                                    "\nชื่อสินค้า : "+product_name+""+
                                                    "\nจำนวนที่มีอยู่ขณะนี้ : "+(double)product_json.get("MS_PRODUCT_AMOUNT"));
                
                /********************************************/
                /*ทำรายการเสร็จสิ้น                             */
                /*ชื่อสินค้า :                                  */
                /*จำนวนที่มีอยู่ขณะนี้ :                          */
                /********************************************/
                
                
                clear_stock(); //เคลียร์ข้อมูลของหน้าต่างเพิ่มจำนวนของสินค้า
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
            }else{
                clear_stock();//เคลียร์ข้อมูลของหน้าต่างเพิ่มจำนวนของสินค้า
            }
        }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"จำนวนไม่ถูกต้อง\nกรุณาทำรายการใหม่ด้วยค่ะ","",ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
                clear_stock();
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        }else{
            JOptionPane.showMessageDialog(null,"กรุณาเลือกสินค้าที่จะเพิ่มจำนวนด้วยค่ะ","",ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }
    }//GEN-LAST:event_stock_commit_btnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะออกจากระบบหรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงหน้าต่างตัวเลือกยืนยัน
        Login g = new Login();  //สร้างObject ของหน้าต่าง Login
            g.setVisible(true); //แสดงหน้า Login
            this.setVisible(false); //ซ่อนหน้านี้
            logout();//ใช้งานMethod Logout
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void edit_partner_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_partner_btnActionPerformed
           Edit_Partner Partner_Panel = new Edit_Partner();//สร้างObject ของหน้าต่าง Partner
           Partner_Panel.setLocationRelativeTo(null); //ตั้งค่าให้แสดงกลางหน้าจอ
           Partner_Panel.setVisible(true); //แสดงหน้าต่างแก้ไข
    }//GEN-LAST:event_edit_partner_btnActionPerformed

    private void edit_menu_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_menu_btnActionPerformed
                Edit_Menu Menu_Panel = new Edit_Menu();//สร้างObject ของหน้าต่าง Menu
                Menu_Panel.setLocationRelativeTo(null);//ตั้งค่าให้แสดงกลางหน้าจอ
                Menu_Panel.setVisible(true);//แสดงหน้าต่างแก้ไข
    }//GEN-LAST:event_edit_menu_btnActionPerformed

    private void edit_product_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_product_btnActionPerformed
                Edit_Product Product_Panel = new Edit_Product();//สร้างObject ของหน้าต่าง Product
                Product_Panel.setLocationRelativeTo(null);//ตั้งค่าให้แสดงกลางหน้าจอ
                Product_Panel.setVisible(true);//แสดงหน้าต่างแก้ไข
    }//GEN-LAST:event_edit_product_btnActionPerformed

    private void edit_customer_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_customer_btnActionPerformed
        Edit_Customer Customer_Panel = new Edit_Customer();//สร้างObject ของหน้าต่าง Customer
        Customer_Panel.setLocationRelativeTo(null);//ตั้งค่าให้แสดงกลางหน้าจอ
        Customer_Panel.setVisible(true);//แสดงหน้าต่างแก้ไข
    }//GEN-LAST:event_edit_customer_btnActionPerformed

    private void edit_employee_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_employee_btnActionPerformed
        Edit_Employee g = new Edit_Employee();//สร้างObject ของหน้าต่าง Employee
        g.setLocationRelativeTo(null);//ตั้งค่าให้แสดงกลางหน้าจอ
        g.setVisible(true);//แสดงหน้าต่างแก้ไข
    }//GEN-LAST:event_edit_employee_btnActionPerformed

    private void employee_phone_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_phone_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_phone_txtActionPerformed

    private void employee_user_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_user_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_user_txtActionPerformed

    private void employee_province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_province_txtActionPerformed

    private void employee_post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_post_txtActionPerformed

    private void employee_email_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_email_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_email_txtActionPerformed

    private void employee_home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_home_txtActionPerformed

    private void employee_locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_locality_txtActionPerformed

    private void employee_district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_district_txtActionPerformed

    private void showpwd_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpwd_checkActionPerformed
        showpwd = !showpwd; //สลับตัวแปรแสดงรหัสผ่านเมื่อมีการกด
        if(showpwd ==true){//ถ้าหากมีการกดใช้งานการแสดงรหัสผ่าน
            employee_pwd_txt.setEchoChar((char)0); //แสดงรหัสผ่าน
        }else{
            employee_pwd_txt.setEchoChar('*');//ซ่อนรหัสผ่าน
        }
    }//GEN-LAST:event_showpwd_checkActionPerformed

    private void employee_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_commit_btnActionPerformed
        if(confirm.isSelected()==false){ //ถ้าหากว่ายังไม่ยืนยันการเพิ่มข้อมูล
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้ยืนยันข้อมูล\nกรุณายืนยันด้วยค่ะ",null,ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }else{ //ถ้าหากว่ายืนยันการเพิ่มข้อมูลแล้ว
            if(check_null_employee()){
            String year; //ตัวแปรเก็บข้อมูลปี
            String month; //ตัวแปรเก็บข้อมูลเดือน
            String date; //ตัวแปรเก็บข้อมูลวัน
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_EMPLOYEE");//ดึงข้อมูลจากCollectionของพนักงานมาใส่ในตัวแปร
                BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObject เพื่อค้นหาข้อมูลตัวสุดท้าย
                DBCursor cur = table.find().sort(sortObject);//ค้นหาข้อมูลโดยจัดเรียงตาม sortObject
                int emp_id = (int)(cur.one().get("MS_EMPLOYEE_ID")); //กำหนดตัวแปรเพื่อเก็บรหัสพนักงาน
                DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE; //สร้างฟอร์แมตวันที่ปัจจุบัน
                String formattedDate = formatter.format(LocalDate.now()); //ตัวแปรเก็บข้อมูลวันที่ปัจจุบันตามฟอร์แมต
                month = v.month(Integer.parseInt(formattedDate.substring(4,6))); //เดือน
                year = formattedDate.substring(0,4);//ปีปัจจุบัน
                date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());//วันที่
                BasicDBObject document = new BasicDBObject(); //สร้างObject เพื่อเก็บข้อมูลที่จะเพิ่มไปยัง database
                document.put("MS_EMPLOYEE_ID",(int)emp_id+1); //รหัสพนักงาน
                document.put("MS_EMPLOYEE_USERNAME",employee_user_txt.getText()); //รหัสผู้ใช้งานของพนักงาน
                document.put("MS_EMPLOYEE_PWD",employee_pwd_txt.getText()); //รหัสของพนักงาน
                document.put("MS_EMPLOYEE_NAME",employee_prefix.getSelectedItem().toString()+" "+employee_name_txt.getText()); //ชื่อของพนักงาน
                document.put("MS_EMPLOYEE_BIRTHDATE",employee_birthdate_txt.getText()); //วันเกิดของพนักงาน
                document.put("MS_EMPLOYEE_EMAIL",employee_email_txt.getText()); //อีเมลของพนักงาน
                document.put("MS_EMPLOYEE_PHONE",employee_phone_txt.getText()); //เบอร์โทรศัพท์ของหนักงาน
                BasicDBObject address = new BasicDBObject();//สร้างObject เพื่อเก็บข้อมูลของที่อยู่ของพนักงาน
                /**********************ที่อยู่พนักงาน**********************************************/
                address.put("บ้านเลขที่", employee_home_txt.getText());
                address.put("ตำบล", employee_locality_txt.getText());
                address.put("อำเภอ", employee_district_txt.getText());    
                address.put("จังหวัด", employee_province_txt.getText()); 
                address.put("รหัสไปรษณีย์", employee_post_txt.getText());
                /*************************************************************/
                document.put("MS_EMPLOYEE_ADDRESS",address); //ที่อยู่พนักงาน
                document.put("MS_EMPLOYEE_HIRED_DATE",month+" "+date+", "+year); //วันที่จ้างพนักงาน
                document.put("MS_EMPLOYEE_TYPE",employee_position_combo.getSelectedItem().toString()); //ประเภทของพนักงาน
                table.insert(document); //เพิ่มข้อมูลใน database
                JOptionPane.showMessageDialog(null,"เพิ่มข้อมูลพนักงานเรียบร้อยแล้วค่ะ");//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
                clear_emp();//เคลียร์ข้อมูลทั้งหมดในหน้าต่างพนักงาน
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
            }else{
                JOptionPane.showMessageDialog(null,"คุณกรอกข้อมูลไม่สมบูรณ์\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            }
        }
    }//GEN-LAST:event_employee_commit_btnActionPerformed

    private void employee_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_clear_btnActionPerformed
        clear_emp(); //เคลียร์ข้อมูลทั้งหมดในหน้าต่างพนักงาน
    }//GEN-LAST:event_employee_clear_btnActionPerformed

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmActionPerformed

    private void pro_type_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_type_comboActionPerformed
        set_parnter_combo(pro_type_combo.getSelectedIndex()); //เมื่อเลือกประเภทของสินค้าแล้ว จะเพิ่มบริษัทคู่ค้าตามประเภทของสินค้า
    }//GEN-LAST:event_pro_type_comboActionPerformed

    private void menu_commit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_commit_btnActionPerformed
        try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            String menu_name = menu_name_txt.getText(); //กำหนดตัวแปรที่เก็บชื่อเมนู
            double menu_price = Double.parseDouble(menu_price_txt.getText()); //กำหนดตัวแปรที่เก็บราคาของเมนู
            DBCollection get_menu = db.getCollection("MS_MENU"); //ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
            BasicDBObject menu_json = new BasicDBObject(); //สร้างObject เพื่อใช้เก็บข้อมูลเมนูที่จะเพิ่มลงไปใน database
            BasicDBObject find_menu_condition = new BasicDBObject().append("_id", -1);//สร้างObject เพื่อค้นหาข้อมูลตัวสุดท้าย
            DBCursor finding_last_menu = get_menu.find().sort(find_menu_condition);//ค้นหาข้อมูลโดยจัดเรียงตาม sortObject
            int menu_last_id = (int)(finding_last_menu.one().get("MS_MENU_ID"))+1; //กำหนดตัวแปรของรหัสเมนูแล้วบวกด้วย 1
            menu_json.put("MS_MENU_ID",menu_last_id);     //รหัสเมนู
            menu_json.put("MS_MENU_NAME",menu_name);      //ชื่อเมนู
            menu_json.put("MS_MENU_PRICE",(int)menu_price);//ราคาเมนู
            menu_json.put("MS_PRODUCT",menu_component); //ส่วนประกอบเมนู
            get_menu.insert(menu_json); //เพิ่มข้อมูลลงในdatabase
            clear_table((DefaultTableModel)menu_product_table.getModel()); //ล้างข้อมูลในตารางสินค้า
            clear_table((DefaultTableModel)menu_table.getModel());      //ล้างข้อมูลในตารางเมนู
            get_product((DefaultTableModel)menu_product_table.getModel()); //ดึงข้อมูลจาก database ลงตารางสินค้า
            clear_menu(); //เคลียร์ข้อมูลในหน้าต่างเมนูทั้งหมด
            JOptionPane.showMessageDialog(null,"เพิ่มเมนูสำเร็จ");//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            menu_component.clear();//เคลียร์Listของส่วนประกอบของเมนู
        }catch(Exception e){//ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            JOptionPane.showMessageDialog(null,"เพิ่มเมนูไม่สำเร็จ","",ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
    }//GEN-LAST:event_menu_commit_btnActionPerformed

    private void menu_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_clear_btnActionPerformed
            clear_menu();//เคลียร์ข้อมูลในหน้าต่างเมนูทั้งหมด
            clear_table((DefaultTableModel)menu_product_table.getModel()); //ล้างข้อมูลในตารางสินค้า
            clear_table((DefaultTableModel)menu_table.getModel());      //ล้างข้อมูลในตารางเมนู
            get_product((DefaultTableModel)menu_product_table.getModel()); //ดึงข้อมูลจาก database ลงตารางสินค้า
            menu_component.clear();//เคลียร์Listของส่วนประกอบของเมนู
    }//GEN-LAST:event_menu_clear_btnActionPerformed

    private void menu_product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_product_tableMouseClicked
        String productid = menu_product_table.getModel().getValueAt(menu_product_table.getSelectedRow(),0).toString(); //กำหนดตัวแปรเก็บรหัสสินค้า
        String productname = menu_product_table.getModel().getValueAt(menu_product_table.getSelectedRow(),1).toString(); //กำหนดตัวแปรเก็บชื่อสินค้า
        try{//เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
        DBCollection get_product = db.getCollection("MS_PRODUCT");//ดึงข้อมูลจากCollectionของสินค้ามาใส่ในตัวแปร
        if(menu_table_doubleclick.equals(productid)){ 
            /*
                    ใช้วิธีเทียบความเหมือนของสองตัวแปร ถ้าหากมีการคลิ๊กที่ตาราง 1 ครั้ง จะทำการตั้งค่าให้ตัวแปรมีค่าเดียวกันกับค่าที่คลิ๊กเพื่อใช้ในการคลิ๊กอีกครั้ง
                    เมื่อคลิ๊กอีกครั้งแล้วเปรียบเทียบกันว่าตัวแปรทั้งสองมีค่าเหมือนกันหรือไม่ จะทำให้ตัวแปรทั้งสองมีค่าเหมือนกัน จึงreturnกลับมาเป็น true
          */
        try{//เพื่อใช้ในการตั้งค่าเงื่อนไขในการแก้ไขข้อมูล
        double menu_per_amount = Double.parseDouble(JOptionPane.showInputDialog(null,"จำนวนที่คุณต้องการจะใช้ต่อเมนู 1 รายการ\nRange ของจำนวนจะอยู่ตั้งแต่ 0.01-9.99 หน่วย"));
        //สร้างหน้าต่างป้อนข้อมูลขึ้นมา
        if(menu_per_amount<=0||menu_per_amount>=10){ //ถ้าหากว่าจำนวนน้อยกว่า 0 หรือมากกว่า10
            JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);//แสดงหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            menu_table_doubleclick = "";
        }else{//ถ้าอยู่นอกเงื่อนไข
        if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า: "+productname+" \n"+
                                              "จำนวนที่จะใช้ต่อเมนู: "+ menu_per_amount+" \n"+
                                              "\n ยืนยืนการเพิ่มสินค้าของเมนูนี้","",YES_NO_OPTION)==YES_OPTION){
                /********************************************/
                /*ชื่อสินค้า :                                  */
                /*จำนวนที่จะใช้ต่อเมนู :                          */
                /*ยืนยันการเพิ่มสินค้าของเมนูนี้                      */
                /*                                          */
                /********************************************/

        BasicDBObject product_condition = new BasicDBObject("MS_PRODUCT_ID",Integer.parseInt(productid)); //สร้างObject เพื่อใช้ค้นหาจากรหัสของสินค้า
        DBCursor find_product = get_product.find(product_condition); //ค้นหาข้อมูลทั้งหมดจาก MS_PRODUCT โดยใช้รหัสสินค้า
        if(find_product.hasNext()==true){ //ถ้าหามีข้อมูล
            DBObject product_json = find_product.next(); //ดึงข้อมูลนั้นๆของวัตถุดิบมาเก็บใส่ตัวแปรใหม่เพื่อป้องกันข้อผิดพลาด
            product_json.put("MS_PRODUCT_AMOUNT", menu_per_amount); //แก้ไขข้อมูลจำนวนที่ใช้ต่อเมนู
            product_json.removeField("_id"); //ลบObject id
            product_json.removeField("MS_PRODUCT_PRICE"); //ลบราคาของสินค้า
            menu_component.add(product_json); //เพิ่มข้อมูลไปยัง List ของส่วนประกอบ
            /*System.out.println("***************************************");
            for(DBObject d:menu_component){ //foreach ส่วนประกอบของเมนู
                System.out.println(d);
            }
            System.out.println("***************************************");*/
            DefaultTableModel product_model = (DefaultTableModel) menu_product_table.getModel(); //กำหนดตัวแปรจากตารางสินค้า
            DefaultTableModel menu_model = (DefaultTableModel) menu_table.getModel(); //กำหนดตัวแปรจากตารางเมนู
            product_model.removeRow(menu_product_table.getSelectedRow()); //ลบแถวที่เลือกในตารางสินค้า
            set_menu_table(menu_model); //ใช้ฟังก์ชั่นดึงข้อมูลเมนู
        }
        menu_table_doubleclick = ""; //เคลียร์ฟังก์ชั่นดับเบิ้ลคลิ๊ก
        }
        }
        }catch(NumberFormatException e){ //เกิดข้อผิดพลาดเกี่ยวกับประเภทของตัวเลข
            JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);
            menu_table_doubleclick = "";//เคลียร์ฟังก์ชั่นดับเบิ้ลคลิ๊ก
        }catch(NullPointerException e){//เกิดข้อผิดพลาดเกี่ยวกับการไม่พบข้อมูล
            menu_table_doubleclick = ""; //เคลียร์ฟังก์ชั่นดับเบิ้ลคลิ๊ก
        }catch(Exception e){ //ถ้าหากเกิดข้อผิดพลาดนอกเหนือจากนั้นทั้งหมด 
              e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ 
        }
        }else{//ถ้าหากว่าตัวแปรที่กำหนดไว้มีค่าไม่ตรงกับตัวแปรในตาราง
            menu_table_doubleclick = productid; //ตั้งค่าตัวแปรที่กำหนดไว้ให้มีค่าตรงกับตัวแปรในตารางแถวที่เลือกช่องที่1
        }
        }catch(Exception e){ //ถ้าหากเกิดข้อผิดพลาดนอกเหนือจากนั้นทั้งหมด 
              e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ 
        }
    }//GEN-LAST:event_menu_product_tableMouseClicked

    private void adding_order_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adding_order_btnActionPerformed
        if(add_order){ //ถ้าหากว่ามีการเพิ่มเมนูในออเดอร์
         int menu_id = (int)menu_order_table.getModel().getValueAt(menu_order_table.getSelectedRow(),0); //ตั้งตัวแปรเพื่อเก็บข้อมูลรหัสเมนู
         //System.out.println(menu_id);
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
              DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");//ดึงข้อมูลจากCollectionของรายการออเดอร์
              DBCollection get_menu = db.getCollection("MS_MENU");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
              BasicDBObject find_menu = new BasicDBObject("MS_MENU_ID",menu_id);//สร้างObjectชื่อ find_menu เพื่อเก็บข้อมูลที่จะนำไปค้นหาจากรหัสเมนู
              DBCursor finding_menu = get_menu.find(find_menu); //ค้นหาข้อมูลจากรหัสเมนู
              DBObject menu_json = null; //สร้างตัวแปรเปล่าเพื่อใช้เก็บข้อมูลของเมนู
              //System.out.println(">>"+get_order_list_id(menu_id));
              if(finding_menu.hasNext()){ //ถ้าหากว่ามีข้อมูล
                  menu_json = finding_menu.next(); //นำข้อมูลไปเก็บไว้ในตัวแปร menu_json
                  menu_json.removeField("_id");//ลบ Objectid ของเมนู
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                int menu_amount = Integer.parseInt(JOptionPane.showInputDialog(null,""
                     + "รายการเมนู\n"
                     + "ชื่อเมนู: "+menu_json.get("MS_MENU_NAME")+"\n"
                     + "ราคาต่อเมนู: "+menu_json.get("MS_MENU_PRICE")+"\n"
                     + "\nกรุณากรอกจำนวนเมนูที่ต้องการค่ะ"
                     + "\n(กรุณากรอกเป็นตัวเลขจำนวนเต็ม)"));
                /*************************************************/
                /*รายการเมนู :                                     */
                /*ชื่อเมนู :                                        */
                /*ราคาต่อเมนู :                                     */
                /*กรุณากรอกจำนวนเมนูที่ต้องการค่ะ                       */
                /*(กรุณากรอกเป็นตัวเลขจำนวนเต็ม)                       */
                /*************************************************/
                if(menu_amount<=0){ //ถ้าหากว่าจำนวนของเมนูน้อยกว่า 0
                    throw new NumberFormatException(); //ส่งการทำงานผิดพลาดของประเภทของตัวเลข
                }else{//ถ้าหากว่านอกเหนือจากนั้น
                  order_list_price = ((int)menu_json.get("MS_MENU_PRICE")*menu_amount); //ราคาของรายการเมนู
                  BasicDBObject insert_order_list = new BasicDBObject(); //สร้างObject เพื่อใช้เก็บข้อมูลรายการออเดอร์ที่จะเพิ่มใน database
                  insert_order_list.put("TRAN_ORDER_LIST_ID",get_order_list_id(menu_id)); //รหัสรายการออเดอร์
                  insert_order_list.put("TRAN_ORDER_LIST_AMOUNT",menu_amount); //จำนวนรายการออเดอร์
                  insert_order_list.put("TRAN_ORDER_LIST_TOTAL_PRICE",order_list_price); //ราคารายการออเดอร์
                  insert_order_list.put("MS_MENU_ID",(int)menu_json.get("MS_MENU_ID")); //รหัสเมนู
                  get_order_list.insert(insert_order_list); //เพิ่มข้อมูลรายการออเดอร์ใส่ database
                  clear_table((DefaultTableModel)order_table.getModel()); //ลบข้อมูลในตารางออเดอร์
                  get_order_list((DefaultTableModel)order_table.getModel()); //ดึงข้อมูลของตารางออเดอร์
                  //System.out.println(menu_json.get("MS_MENU_ID"));
                  //System.err.println(order_list_price);
                  if(check_order_list==true){ //ถ้าหากว่ามีข้อมูลรายการออเดอร์อยู่แล้ว
                    ordering_btn.setEnabled(true); //เปิดการใช้งานปุ่มการสั่งออเดอรื
                  }else{//ถ้าไม่ใช่
                    ordering_btn.setEnabled(false);//ปิดการทำงานปุ่มการสั่งออเดอร์
                  }
                }
            }catch(NumberFormatException e){ //ถ้าหากมีการทำงานผิดพลาดประเภทของจำนวน
                JOptionPane.showMessageDialog(null,"คุณกรอกจำนวนไม่ถูกต้อง\nกรุณาทำรายการใหม่","",ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
              }else{//ถ้าหากว่าไม่มีข้อมูล
                  throw new NullPointerException(); //ส่งการทำงานผิดพลาดเกี่ยวกับประเภทของจำนวน
              }
            }catch(NullPointerException e){//ถ้าหากมีการทำงานผิดพลาดประเภทของจำนวน
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูล\nกรุณาทำรายการใหม่","",ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
        }
        }else{//ถ้าหากไม่มีการเลือกออเดอร์
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้เลือกเมนูในรายการ\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);//แสดงผลหน้าต่างขึ้นมาทางหน้าจอพร้อมตัวหนังสือ
        }
    }//GEN-LAST:event_adding_order_btnActionPerformed

    private void menu_order_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_order_tableMouseClicked
      add_order = true;
    }//GEN-LAST:event_menu_order_tableMouseClicked

    private void order_clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_clear_btnActionPerformed
        menu_order_table.clearSelection(); //เคลียร์การเลือกของตาราง
        add_order = false; //ส่งค่าตัวแปรการเพิ่มข้อมูลเป็น false
        clearing_order(); //เคลียร์ข้อมูลในหน้าต่างออเดอร์ทั้งหมด
        clear_table((DefaultTableModel)order_table.getModel()); //เคลียร์ข้อมูลในตารางออเดอร์
        get_order_list((DefaultTableModel)order_table.getModel()); //ดึงข้อมูลของออเดอร์จาก database
        ordering_btn.setEnabled(false); //ตั้งค่าปุ่มการสั่งออเดอรืให้ใช้การไม่ได้
    }//GEN-LAST:event_order_clear_btnActionPerformed

    private void history_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_history_comboActionPerformed
        clear_table((DefaultTableModel)history_table.getModel()); //เคลียร์ข้อมูลในตารางประวัติการขาย
        switch(history_combo.getSelectedIndex()){ //switch case ของประวัติการขาย ตั้งค่าเกี่ยวกับจำนวนเงิน และ เดือน
            case 1:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("January"));break;
            case 2:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("February"));break;
            case 3:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("March"));break;
            case 4:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("April"));break;
            case 5:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("May"));break;
            case 6:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("June"));break;
            case 7:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("July"));break;
            case 8:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("Auguest"));break;
            case 9:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("September"));break;
            case 10:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("October"));break;
            case 11:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("November"));break;
            case 12:total_price_txt.setText("0.0");total_price_txt.setText(""+get_history("December"));break;
            default:total_price_txt.setText("0.0");clear_table((DefaultTableModel)history_table.getModel());
        }
    }//GEN-LAST:event_history_comboActionPerformed

    private void history_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_history_tableMouseClicked
        String order_id = history_table.getModel().getValueAt(history_table.getSelectedRow(),0).toString();
        
        if(history_table_doubleclick.equals(order_id)){
        /*
               ใช้วิธีเทียบความเหมือนของสองตัวแปร ถ้าหากมีการคลิ๊กที่ตาราง 1 ครั้ง จะทำการตั้งค่าให้ตัวแปรมีค่าเดียวกันกับค่าที่คลิ๊กเพื่อใช้ในการคลิ๊กอีกครั้ง
               เมื่อคลิ๊กอีกครั้งแล้วเปรียบเทียบกันว่าตัวแปรทั้งสองมีค่าเหมือนกันหรือไม่ จะทำให้ตัวแปรทั้งสองมีค่าเหมือนกัน จึงreturnกลับมาเป็น true
        */
            try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
            String findpart = "./invoice/"; //ตั้งค่า path ของใบเสร็จ
            File file=new File(findpart); //ดึงข้อมูลไฟล์ตาม path
            File files[] = file.listFiles(); //ดึงข้อมูลทั้งหมดในpath (folder) มาเก็บใส list
            for(File f:files){ //สร้าง loop foreach ของไฟล์ 
                if(f.getName().contains(order_id)){ //ถ้าหากชื่อของไฟล์มีส่วนประกอบของรหัสออเดอร์
                findpart = (f.getName()); //ตั้งค่า path ใหม่
            }
            }
            Desktop.getDesktop().open(new File("./invoice/"+findpart)); //เปิดไฟล์ตาม path ที่ตั้งไว้
            }catch(Exception e){ //ถ้าหากเกิดข้อผิดพลาดนอกเหนือจากนั้นทั้งหมด 
              e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ 
          }
            history_table_doubleclick = ""; //เคลียร์ฟังก์ชั่นดับเบิ้ลคลิ๊ก
        }else{//ถ้าหากว่าตัวแปรที่กำหนดไว้มีค่าไม่ตรงกับตัวแปรในตาราง
            history_table_doubleclick = order_id;//ตั้งค่าตัวแปรที่กำหนดไว้ให้มีค่าตรงกับตัวแปรในตารางแถวที่เลือกช่องที่1
        }
    }//GEN-LAST:event_history_tableMouseClicked

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adding_order_btn;
    private javax.swing.JPanel btn_panel;
    private javax.swing.JCheckBox confirm;
    private datechooser.beans.DateChooserCombo customer_birthdate_txt;
    private javax.swing.JButton customer_btn;
    private javax.swing.JButton customer_clear_btn;
    private javax.swing.JButton customer_commit_btn;
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
    private javax.swing.JComboBox<String> customer_type_combo;
    private javax.swing.JButton edit_customer_btn;
    private javax.swing.JButton edit_employee_btn;
    private javax.swing.JButton edit_menu_btn;
    private javax.swing.JButton edit_partner_btn;
    private javax.swing.JButton edit_product_btn;
    private javax.swing.JComboBox<String> employee_age_combo;
    private datechooser.beans.DateChooserCombo employee_birthdate_txt;
    private javax.swing.JButton employee_btn;
    private javax.swing.JButton employee_clear_btn;
    private javax.swing.JButton employee_commit_btn;
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
    private javax.swing.JTextField employee_user_txt;
    private javax.swing.JPanel first_panel;
    private javax.swing.JButton history_btn;
    private javax.swing.JComboBox<String> history_combo;
    private javax.swing.JPanel history_panel;
    private javax.swing.JTable history_table;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel main_panel;
    private javax.swing.JRadioButton man_radio;
    private javax.swing.JButton menu_btn;
    private javax.swing.JButton menu_clear_btn;
    private javax.swing.JButton menu_commit_btn;
    private javax.swing.JTextField menu_name_txt;
    private javax.swing.JTable menu_order_table;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JTextField menu_price_txt;
    private javax.swing.JTable menu_product_table;
    private javax.swing.JTable menu_table;
    private javax.swing.JButton order_btn;
    private javax.swing.JButton order_clear_btn;
    private javax.swing.JPanel order_panel;
    private javax.swing.JTable order_table;
    private javax.swing.JTextField order_total_txt;
    private javax.swing.JButton ordering_btn;
    private javax.swing.JButton partner_btn;
    private javax.swing.JButton partner_clear_btn;
    private javax.swing.JComboBox<String> partner_combo;
    private javax.swing.JButton partner_commit_btn;
    private javax.swing.JTextField partner_distict_txt;
    private javax.swing.JTextField partner_email_txt;
    private javax.swing.JTextField partner_home_txt;
    private javax.swing.JTextField partner_locality_txt;
    private javax.swing.JTextField partner_name_txt;
    private javax.swing.JPanel partner_panel;
    private javax.swing.JTextField partner_phone_txt;
    private javax.swing.JTextField partner_post_txt;
    private javax.swing.JTextField partner_province_txt;
    private javax.swing.JComboBox<String> partner_type_combo;
    private javax.swing.JLabel picture_label;
    private javax.swing.JTextField pro_name_txt;
    private javax.swing.JTextField pro_price_txt;
    private javax.swing.JComboBox<String> pro_type_combo;
    private javax.swing.JButton product_btn;
    private javax.swing.JButton product_clear_btn;
    private javax.swing.JButton product_commit_btn;
    private javax.swing.JPanel product_panel;
    private javax.swing.JCheckBox showpwd_check;
    private javax.swing.JTextField stock_amount;
    private javax.swing.JButton stock_btn;
    private javax.swing.JButton stock_clear_btn;
    private javax.swing.JComboBox<String> stock_combo;
    private javax.swing.JButton stock_commit_btn;
    private javax.swing.JPanel stock_panel;
    private javax.swing.JLabel title_name_txt;
    private javax.swing.JPanel title_panel;
    private javax.swing.JLabel title_position_txt;
    private javax.swing.JLabel total_price_txt;
    private javax.swing.JRadioButton woman_radio;
    // End of variables declaration//GEN-END:variables
}
