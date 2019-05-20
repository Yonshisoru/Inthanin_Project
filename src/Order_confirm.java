import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.BasicDBList;
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
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
Variable v = new Variable();//สร้าง Object ใหม่จาก Variable Class เพื่อดึง Method มาใช้
//--------------------------MongoDB variable-------------------------------
    MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
    DB db; //กำหนดตัวแปรประเภท DB
    DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
//---------------------Boolean------------------------------
    boolean first_notification = false; //กำหนดให้มีการแจ้งเตือนเมื่อเข้าหน้าจอ
//---------------------Double------------------------------
    double sum_order_price = 0; //ราคารวมทั้งหมด(ยังไม่ได้หักส่วนลด)
    double total_order_price = 0; //ราคารวมทั้งหมด (รวมส่วนลด)
//---------------------Integer------------------------------
    int discount_price = 0; //จำนวนส่วนลด(เปอร์เซตน์ 1-100)
//---------------------String------------------------------
    String time = LocalTime.now().toString().substring(0,2)+"-"+LocalTime.now().toString().substring(3,5)+"-"+LocalTime.now().toString().substring(6,8); //กำหนดตัวแปรเพื่อใช้เก็บเวลา
//---------------------List------------------------------    
    List<DBObject> order_list = new ArrayList<>(); //สร้างListเพื่อเก็บข้อมูลของ
    
    public void printInvoice(String id,double income){ //ฟังก์ชั่นการสร้างใบเสร็
        int count = 0; //ตัวแปรเก็บจำนวนรายการทั้งหมด
        String filename = "$"+LocalDate.now()+"$"+time+"$"+/*t.getorderid()*/id+".pdf"; //เก็บชื่อไฟล์ของใบเสร็จ (วันที่+เวลา+รหัสออเดอร์)
        try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        Document doc = new Document(new Rectangle(218,400),20f, 0f, 0f, 0f); //สร้างเอกสารเปล่าขึ้นมาขนาด 218x400
        BaseFont baseFont = BaseFont.createFont("./fonts/fontsgod.ttf", BaseFont.IDENTITY_H,true); //เรียกใช้งานฟอนต์พื้นฐาน
        Font font = new Font(baseFont,7); //สร้างฟอนต๊ใหม่ขนาด 7
        Font topicfont = new Font(baseFont,6); //สร้างฟอนต์ใหม่ขนาด 6
        Font bigfont = new Font(baseFont,10); //สร้างฟอนต์หัวเรื่องขนาด 10
        PdfWriter.getInstance(doc,new FileOutputStream("./invoice/"+filename));//สร้างไฟล์ของบิลใบเสร็จตามชื่อที่ตั้งไว้
        doc.open(); //เปิดเอกสารเปล่าขึ้นมา
        doc.add(new Paragraph(String.format("Inthanin Coffee"),bigfont)); //เพิ่มบรรทัดใหม่พร้อมทั้งตัวหนังสือ
        String date = LocalDate.now().toString().substring(LocalDate.now().toString().length()-2,LocalDate.now().toString().length());
        String month= LocalDate.now().toString().substring(5,7);
        String year = LocalDate.now().toString().substring(0,4);
        doc.add(new Paragraph(String.format("%s\n","วันที่: "+date+"/"+month+"/"+year),font));
        doc.add(new Phrase(String.format("%s\n","เวลา: "+LocalTime.now().toString().substring(0,8)+" น."),font)); //เพิ่มวรรคของตัวหนังสือ
        doc.add(new Paragraph(String.format("%s","-----------------------------------------------------------------------------------------------------"),font));
        doc.add(new Paragraph(String.format("%s\n","ใบเสร็จรับเงิน"),bigfont));
                for(DBObject ol:order_list){
                       doc.add(new Paragraph(String.format("%s",ol.get("TRAN_ORDER_LIST_AMOUNT")+"          "+get_menu((int)ol.get("MS_MENU_ID")).get("MS_MENU_NAME")+"               "+String.format("%s",ol.get("TRAN_ORDER_LIST_TOTAL_PRICE"))+" บาท"),font)); 
                       count++;
                }
        doc.add(new Paragraph(String.format("%s","-----------------------------------------------------------------------------------------------------"),font));  
        doc.add(new Paragraph(String.format("%s\n","จำนวนสุทธิ: "+count+" รายการ"),font));    
        doc.add(new Paragraph(String.format("%s\n","รับเงิน: "+income+" บาท"),font));       
        doc.add(new Paragraph(String.format("%s\n","ราคารวม: "+order_sum_txt.getText()+" บาท"),font)); 
        if(order_discount_txt.getText().equals("0")||order_discount_txt.getText().isEmpty()){
        }else{
        doc.add(new Paragraph(String.format("%s\n","ส่วนลดทั้งหมด: "+order_discount_txt.getText()+" %"),font)); 
        }
        doc.add(new Paragraph(String.format("%s\n","ราคารวมสุทธิ: "+order_total_txt.getText()+" บาท"),font)); 
        doc.add(new Paragraph(String.format("%s\n","เงินทอน: "+(income-(Double.parseDouble(order_total_txt.getText())))+" บาท"),font));     
        doc.add(new Paragraph(String.format("%s","\n\n\n"),font)); 
        doc.add(new Paragraph(String.format("%s","                                 Thank you and please come again                         "),font)); 
        doc.add(new Paragraph(String.format("%s","                                             Inthanin Coffee                              "),font)); 
        doc.add(new Paragraph(String.format("%s","\n\n\n"),font)); 
        doc.add(new Paragraph(String.format("%s"," -------------------------------------------@powered by SAM---------------------------------------"),font));  
        doc.close();
}catch (DocumentException ex){ //ดักจับการทำงานผิดพลาดเกี่ยวกับเอกสาร
    Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE,null,ex);
}      catch (FileNotFoundException ex) {
           Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE, null, ex);
       }catch(IOException ex){
          Logger.getLogger(Order_confirm.class.getName()).log(Level.SEVERE, null, ex); 
       }
try{ //ดักจับการทำงานผิดพลาดโดยใช้
        Desktop.getDesktop().open(new File("./invoice/"+filename)); //เปิดไฟล์ใบเสร็จขึ้นมา
                }catch(Exception e){//ดักจับการทำงานผิดพลาดทั้งหมด
                    System.out.println(e); //แสดงการทำงานผิดพลาด
                }
    }
    /**
     * Creates new form Order_confirm
     */
   
    public Order_confirm() {
        initComponents();
        this.setLocationRelativeTo(null); //ตั้งค่าการแสดงผลให้อยู่กลางหน้าจอ
        get_order_list((DefaultTableModel)order_table.getModel()); //ดึงข้อมูลของรายการออเดอร์จาก Database
        set_total_text(); //ตั้งค่าราคารวมทั้งหมด
    }
    public void set_total_text(){ //ฟังก์ชั่นการตั้งราคารวมทั้งหมดขึ้นหน้าจอ
        order_sum_txt.setText(""+sum_order_price); //ราคารวม(ยังไม่หักส่วนลด)
        order_total_txt.setText(""+total_order_price); //ราคารวม(หักส่วนลดแล้ว)
    }
    public void clear_customer(){ //ฟังก์ชั่นการเคลียร์ข้อมูลลูกค้า
                customer_id_txt.setText("");
                customer_name_txt.setText("");
                customer_email_txt.setText("");
                customer_phone_txt.setText("");
    }
    public void get_order_list(DefaultTableModel table){ //ฟังก์ชั่นดึงข้อมูลของรายการออเดอร์ไปใส่ในตาราง
        db = v.getConnect(); //เชื่อมต่อDatabase 
        DefaultTableModel model = table; //ดึงตารางของparameterมาใช้
        Object[] row = new Object[4]; //สร้างอาเรย์ของ Object ขนาด 4
        DBCollection product_collection  = db.getCollection("TRAN_ORDER_LIST");//ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
        DBCursor product_finding = product_collection.find(); //ค้นหาข้อมูลของรายการออเดอร์ทั้งหมด
        if(product_finding.hasNext()==true){ //ถ้าหากมีข้อมูล
        while(product_finding.hasNext()){ //สร้างลูป while โดยจะหยุดทำงานต่อเมื่อไม่มีข้อมูลตัวถัดไปแล้ว
            DBObject menu_json = product_finding.next(); //ดึงข้อมูลของเมนูมาเก็บไว้
            int menu_id = (int)menu_json.get("MS_MENU_ID"); //กำหนดตัวแปรเก็บรหัสเมนู
            row[0] = (int)menu_json.get("TRAN_ORDER_LIST_ID"); //รหัสรายการออเดอร์
            row[1] = find_product_name(menu_id).get("MS_MENU_NAME"); //ชื่อเมนู
            //System.out.println(">>>>"+menu_json.get("TRAN_ORDER_LIST_PRICE"));
            row[2] = menu_json.get("TRAN_ORDER_LIST_AMOUNT"); //จำนวน
            row[3] = menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE"); //ราคารวม
            sum_order_price += (int)menu_json.get("TRAN_ORDER_LIST_TOTAL_PRICE"); //ราคาสุทธิ
            model.addRow(row); //เพิ่มแถว
        }
        total_order_price = sum_order_price; //ราคารวมเท่ากับราคารวมสุทธิ
        }else{ //ถ้าหากไม่พบข้อมูล
            throw new NullPointerException(); //คืนค่าความผิดพลาดเกี่ยวกับค่าว่างของข้อมูล
        }
    } 
        public DBObject find_product_id(String name){ //ค้นหารหัสของสินค้าจากชื่อ
            DBCollection product = db.getCollection("MS_PRODUCT"); ////ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
            BasicDBObject data = new BasicDBObject("MS_PRODUCT_NAME",name); //สร้างObject ในการค้นหาโดยใช้ชื่อของสินค้า
            DBCursor find = product.find(data); //ค้นหาข้อมูลที่ตรงกับเงื่อนไข
            DBObject product_json = null; //สร้างตัวแปรเพี่อใช้เก็บข้อมูลของสินค้า
            try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
                product_json = find.next(); //เก็บข้อมูลของสินค้าลงตัวแปร
            }catch(Exception e){ //ถ้าหากมีการทำงานผิดพลาดทุกชนิด
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ"); //แสดงกล่องข้อความพร้อมตัวหนังสือ
            }
        return product_json; //คืนค่าข้อมูลสินค้า
    }
        public DBObject find_product_name(int id){ //ค้นหาข้อมูลสินค้าจากรหัสเมนู
            DBCollection product = db.getCollection("MS_MENU"); //ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
            BasicDBObject data = new BasicDBObject("MS_MENU_ID",id); //สร้างObjectเพื่อใช้หาข้อมูลสินค้าโดยใช้รหัสเมนู
            DBCursor find = product.find(data); //ค้นหาข้อมูลทั้งหมดที่ตรงกับเงื่อนไข
            DBObject product_json = null; //สร้างตัวแปรเพี่อใช้เก็บข้อมูลของสินค้า
            try{
                product_json = find.next();//เก็บข้อมูลของสินค้าลงตัวแปร
            }catch(Exception e){//ถ้าหากมีการทำงานผิดพลาดทุกชนิด
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูลในฐานข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ");//แสดงกล่องข้อความพร้อมตัวหนังสือ
            }
        return product_json;//คืนค่าข้อมูลสินค้า
    }
        
         public DBObject find_customer(String id){ //ค้นหาข้อมูลจากรหัสลูกค้า
            DBCollection customer = db.getCollection("MS_CUSTOMER");//ดึงข้อมูลจากCollectionของลูกค้ามาใส่ในตัวแปร
            BasicDBObject data = new BasicDBObject("MS_CUSTOMER_ID",id);//สร้างObjectเพื่อใช้หาข้อมูลลูกค้าโดยใช้รหัสลูกค้า
            DBCursor find = customer.find(data); //ค้นหาข้อมูลทั้งหมดที่ตรงกับเงื่อนไข
            DBObject customer_json = null;//สร้างตัวแปรเพี่อใช้เก็บข้อมูลของลูกค้า
            try{
                customer_json = find.next();//เก็บข้อมูลของลูกค้าลงตัวแปร
            }catch(Exception e){//ถ้าหากมีการทำงานผิดพลาดทุกชนิด
                throw new NullPointerException();//แสดงกล่องข้อความพร้อมตัวหนังสือ
            }
        return customer_json;//คืนค่าข้อมูลลูกค้า
    }
        public int get_invoice_id(){//ฟังก์ชั่นการค้นหารหัสใบเสร็จ
            DBCollection get_order_list = db.getCollection("TRAN_INVOICE");//ดึงข้อมูลจากCollectionของใบเสร็จมาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1); //สร้างObjectเพื่อใช้หาข้อมูลลำดับสุดท้าย
            DBCursor finding_order_list_id = get_order_list.find().sort(sortObject); //ค้นหาข้อมูลโดยจัดเรียงจากล่างขึ้นบน
              int invoice_id = -1; //ตัวแปรเก็บรหัสใบเสร็จ
                if(finding_order_list_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_list_id.next(); //เก็บข้อมูลของใบเสร็จลงตัวแปร
                    invoice_id = 1+(int)data.get("TRAN_INVOICE_ID"); //นำค่าของPKมาบวกด้วย 1
                }else{
                    invoice_id = 1;//สร้างPKของ TRAN_INVOICE
                }
                return invoice_id; //คืนค่าข้อมูลรหัสใบเสร็จ
        }
        public String get_order_id(){ //ฟังก์ชั่นการค้นหารหัสออเดอร์
            DBCollection get_order = db.getCollection("TRAN_ORDER");//ดึงข้อมูลจากCollectionของออเดอร์มาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObjectเพื่อใช้หาข้อมูลลำดับสุดท้าย
            DBCursor finding_order_id = get_order.find().sort(sortObject);//ค้นหาข้อมูลโดยจัดเรียงจากล่างขึ้นบน
              String order_id = null;//ตัวแปรเก็บรหัสออเดอร์
              int id = 0;
                if(finding_order_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_id.next();//เก็บข้อมูลของออเดอร์ตัวแปร
                    id = 1+Integer.parseInt(data.get("TRAN_ORDER_ID").toString().substring(1,data.get("TRAN_ORDER_ID").toString().length())); //นำค่าของPKมาบวกด้วย 1
                    if(id>=10){
                        order_id = "O0"+id;
                    }else if(id>100){
                        order_id = "O"+id;
                    }else if(id<10){
                        order_id = "O00"+id;
                    }
                }else{
                    order_id = "O00"+1;
                }
                return order_id;//คืนค่าข้อมูลรหัสออเดอร์
        }
        public DBObject get_menu(int id){ //ฟังก์ชั่นการค้นหาข้อมูลเมนู
           try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
           DBCollection get_order = db.getCollection("MS_MENU"); //ดึงข้อมูลจากCollectionของเมนูมาใส่ในตัวแปร
           BasicDBObject sortObject = new BasicDBObject("MS_MENU_ID",id);//สร้างObjectเพื่อใช้หาข้อมูลเมนูโดยใช้รหัสเมนู
           DBCursor finding_menu = get_order.find(sortObject); //ค้นหาข้อมูลทั้งหมดที่ตรงกับเงื่อนไข
           DBObject menu_json = null; //กำหนดตัวแปรเพื่อใช้ในการเก็บข้อมูล
           if(finding_menu.hasNext()==true){ //ถ้าหากว่ามีข้อมูล
               menu_json = finding_menu.next(); //นำข้อมูลไปเก็บไว้ในตัวแปร
           } 
           return menu_json; //คืนค่าข้อมูลเมนู
           }catch(Exception e){ //ถ้าหากการทำงานผิดพลาดทุกชนิด
               e.printStackTrace(); //แสดงผลทางหน้าจอ
               throw new NullPointerException(); //คืนค่าการทำงานผิดพลาดเกี่ยวกับค่าว่างของตัวแปร
           }
        }
        
        public void edit_product(int id,double amount_using){ //ฟังก์ชั่นการลบสินค้าที่ใช้
           try{//ดักจับการทำงานผิดพลาดโดยใช้ try-catch
           DBCollection get_product = db.getCollection("MS_PRODUCT"); //ดึงข้อมูลจากCollectionของสินค้ามาใส่ในตัวแปร
           BasicDBObject sortObject = new BasicDBObject("MS_PRODUCT_ID",id); //สร้างObjectในการค้นหาข้อมูลของสินค้าจากรหัสสินค้า
           BasicDBObject updateFields = new BasicDBObject(); //สร้างObjectในการเก็บข้อมูลที่จะแก้ไข
           BasicDBObject setQuery = new BasicDBObject();//สร้างObjectในการตั้งค่าการแทนข้อมูลใหม่
           DBCursor finding_product = get_product.find(sortObject); //ค้นหาข้อมูลทั้งหมดที่ตรงกับเงื่อนไข
           DBObject product_json = null; //สร้างตัวแปรเพื่อใช้เก็บจ้อมูลสินค้า
               while(finding_product.hasNext()){ //สร้างloop while จะหยุดทำงานต่อเมื่อไม่มีข้อมูลตัวถัดไป
               product_json = finding_product.next(); //นำข้อมูลของสินค้าไปเก็บไว้ในตัวแปร
               //System.out.println(product_json);
               double product_amount = (double)product_json.get("MS_PRODUCT_AMOUNT"); //ดึงจำนวนของสินค้าที่มี
               //System.out.println(product_amount-amount_using);
               if(product_amount-amount_using>=0){ //ถ้าหากว่าจำนวนสามารถหักลบแล้วมากกว่า 0
                   DecimalFormat f = new DecimalFormat("##.00"); //ทศนิยมของจำนวนจะต้องไม่เกิน 2 ตำแหน่ง
                   double output = product_amount-amount_using; //ลบจำนวนที่ใช้ในจำนวนของสินค้า
                   updateFields.put("MS_PRODUCT_AMOUNT",Double.parseDouble(f.format(output))); //เพิ่มข้อมูลจำนวนที่จะอัพเดท
               }else{ //ถ้าหากว่าไม่สามารถลบได้
                   throw new Exception(); //คืนค่าการทำงานผิดพลาด
               }
           setQuery.append("$set", updateFields); //ตั้งค่าการอัพเดทข้อมูล
           get_product.update(product_json,setQuery);//อัพเดทข้อมูลในdatabase
           }
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
               e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
               throw new ArithmeticException(); //คืนค่าความผิดพลาดทางคณิตศาสตร์
           }
           
        }
          public int sessionnow(){ 
            db = v.getConnect(); //ใช้งาน Method การเชื่อมต่อ
            DBCollection log = db.getCollection("TRAN_LOG"); //ดึงข้อมูลจาก Collection ของประวัติการใช้งานมาใส่ในตัวแปร
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);//สร้างObjectชื่อ search เพื่อใช้เก็บข้อมูลที่ใช้ค้นหาตัวสุดท้าย
            DBCursor cur = log.find().sort(sortObject);//ค้นหาข้อมูลทั้งหมดของประวัติการใช้งาน (TRAN_LOG)ตัวสุดท้าย
            int emp_id = (int)cur.one().get("MS_EMPLOYEE_ID"); //กำหนดตัวแปรเพื่อเก็บข้อมูลรหัสของพนักงาน
            return emp_id;
    }
          public String get_date(){ //ฟังก์ชั่นการเรียกวันปัจจุบัน
                DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                String formattedDate = formatter.format(LocalDate.now()); //ดึงค่าวันที่ปัจจุบัน
                 String month = v.month(Integer.parseInt(formattedDate.substring(4,6))); //ใช้งานฟังก์ชั่นแปลงลำดับของเดือนเป็นชื่อเดือน
                String year = formattedDate.substring(0,4); //ปีปัจจุบัน
                String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length()); //วันที่ปัจจุบัน
                return month+" "+date+", "+year; //คืนค่าวันที่
          }
         public void clearing_order(){ //ฟังก์ชั่นเคลียร์ข้อมูลรายการออเดอร์
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST"); //ดึงข้อมูลจากCollectionของรายการออเดอร์ามาใส่ในตัวแปร
            DBCursor finding_order_list = get_order_list.find(); //ค้นหาข้อมูลทั้งหมดของรายการออเดอร์
            while (finding_order_list.hasNext()) { //ถ้าหากว่ามีข้อมูลตัวถัดไป
                get_order_list.remove(finding_order_list.next()); //ลบข้อมูลรายการออเดอร์
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

        jLabel1 = new javax.swing.JLabel();
        customer_id_txt = new javax.swing.JTextField();
        order_sum_txt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        customer_phone_txt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        customer_name_txt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        customer_email_txt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
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

        customer_id_txt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(customer_id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 113, -1));

        order_sum_txt.setEnabled(false);
        getContentPane().add(order_sum_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 380, 113, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("ราคารวม:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 380, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("รหัสสมาชิก");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        customer_phone_txt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customer_phone_txt.setEnabled(false);
        getContentPane().add(customer_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 230, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("เบอร์โทร:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));

        customer_name_txt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customer_name_txt.setEnabled(false);
        getContentPane().add(customer_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 240, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("ชื่อ-สกุล:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        customer_email_txt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customer_email_txt.setEnabled(false);
        getContentPane().add(customer_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 250, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("อีเมลล์:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

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
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("ยกเลิก");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
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
        try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
        double income = 0; //เงินที่รับจากลูกค้า
        do{ //ทำซ้ำจนกว่าเงินที่ได้รับจากลูกค้าจะมากกว่าราคารวม
        income = Double.parseDouble(JOptionPane.showInputDialog(null,"ได้รับเงินจากลูกค้าจำนวน(บาท)"));
        if(income>=Double.parseDouble(order_total_txt.getText())){
            break;
        }else{
            JOptionPane.showMessageDialog(null,"คุณกรอกจำนวนเงินไม่ถูกต้อง\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);
        }
        }while(income<Double.parseDouble(order_total_txt.getText()));
        DBCollection get_order = db.getCollection("TRAN_ORDER");//ดึงข้อมูลจากCollectionของออเดอร์มาใส่ในตัวแปร
        DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");//ดึงข้อมูลจากCollectionของรายการออเดอร์ามาใส่ในตัวแปร
        DBCollection get_invoice = db.getCollection("TRAN_INVOICE");//ดึงข้อมูลจากCollectionของใบเสร็จมาใส่ในตัวแปร
        int menu_amount = 0;//จำนวนเมนู
        DBCursor find_order_list = get_order_list.find(); //ค้นหาข้อมูลของรายการออเดอร์ทั้งหมด
        DBObject order_list_json = null; //ตัวแปรเก็บข้อมูลของรายการออเดอร์
        while(find_order_list.hasNext()){ //ถ้าหากมีรายการออเดอรตัวถัดไปอยู่
            order_list_json = find_order_list.next(); //ดึงข้อมูลรายการออเดอร์เก็บใส่ตัวแปร
            order_list.add(order_list_json); //เพิ่มข้อมูลรายการออเดอร์ใส่ใน List
            int menu_id = (int)order_list_json.get("MS_MENU_ID"); //รหัสเมนู
            menu_amount = (int)order_list_json.get("TRAN_ORDER_LIST_AMOUNT"); //จำนวนเมนูที่สั่ง
            for(int i =0;i<menu_amount;i++){ //ทำซ้ำตามจำนวนเมนูที่สั่ง
            BasicDBList list = (BasicDBList)(get_menu(menu_id).get("MS_MENU_PRODUCT")); //ดึงวัตถุดิบที่ใช้ออกมาเป็นList
            for(Object el: list) { //ดึงข้อมูลในListเพิ่อวนซ้ำ
                DBObject product = (DBObject)el; //ดึงข้อมูลของสินค้าที่ใช้
                edit_product((int)product.get("MS_PRODUCT_ID"),(double)product.get("MS_PRODUCT_AMOUNT")); //ฟังก์ชั่นการลบจำนวนสินค้าที่ใช้
            }
            }
        }
        BasicDBObject order_doc = new BasicDBObject(); //สร้างตัวแปรเพื่อเก็บข้อมูลของออเดอร์
        order_doc.put("TRAN_ORDER_ID",get_order_id()); //รหัสออเดอร์
        if(!customer_id_txt.getText().isEmpty()){ //ถ้าใส่รหัสลูกค้าจะดึงข้อมูลลูกค้า
            order_doc.put("MS_CUSTOMER_ID",customer_id_txt.getText());
        }
        order_doc.put("MS_EMPLOYEE", sessionnow()); //รหัสพนักงาน
        order_doc.put("TRAN_ORDER_TOTAL_PRICE",order_total_txt.getText()); //ราคารวม
        order_doc.put("TRAN_ORDER_DATE",get_date()); //วันที่
        order_doc.put("TRAN_ORDER_TIME",LocalTime.now().toString().substring(0, 8)); //เวลา
        order_doc.put("TRAN_ORDER_LIST",order_list); //รายการออเดอร์
        BasicDBObject invoice_doc = new BasicDBObject(); //สร้างตัวแปรเพื่อเก็บข้อมูลของใบเสร็จ
        invoice_doc.put("TRAN_INVOICE_ID",get_invoice_id()); //รหัสใบเสร็จ
        invoice_doc.put("TRAN_ORDER_ID",get_order_id()); //รหัสออเดอร์
        invoice_doc.put("TRAN_INVOICE_DATE",get_date()); //วันที่
        invoice_doc.put("TRAN_INVOICE_TIME",LocalTime.now().toString().substring(0, 8));; //เวลา
        printInvoice(get_order_id(),income); //ฟังก์ชั่นพิมพ์ใบเสร็จ
        get_invoice.insert(invoice_doc); //เพิ่มใบเสร็จในDatabase
        get_order.insert(order_doc); //เพิ่มออเดอร์ในDatabase
        clearing_order();//เคลียร์ข้อมูลทั้งหมด
        order_list.clear(); //ลบข้อมูลในListของรายการออเดอร์
        this.setVisible(false); //ซ่อนหน้านี้
        Main m = new Main();
        m.setVisible(false);
        m.setVisible(true);//แสดงหน้าจอหลัก
        }catch(ArithmeticException e){ //ถ้าหากมีจำนวนไม่เพียงพอ
            JOptionPane.showMessageDialog(null,"ไม่สามารถดำเนินการได้\nเนื่องจากวัตถุดิบไม่เพียงพอ","",ERROR_MESSAGE); //แสดงข้อความทางหน้าจอ
        }catch(NullPointerException e){ //ถ้าหากไม่มีข้อมูล
            order_list.clear(); //ลบข้อมูลในListของรายการออเดอร์
        }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{//ดักจับข้อผิดพลาดโดยใช้ try-catch
        int discount = Integer.parseInt(order_discount_txt.getText()); //ดึงส่วนลดมาใสในตัวแปร
        total_order_price = (double)sum_order_price-(((double)sum_order_price*(double)discount)/(double)100); //คำนวณส่วนลด
        set_total_text();//ใช้งานฟังก์ชั่นตั้งค่าราคาสุทธิ
        //order_discount_txt.setEnabled(false);
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
            JOptionPane.showMessageDialog(null,"คุณใส่จำนวนส่วนลดไม่ถูกต้อง\nกรุณากรอกเป็นจำนวนเต็มด้วยค่ะ","",ERROR_MESSAGE); //แสดงข้อความทางหน้าจอ
            order_discount_txt.setText(""); //เคลียร์ส่วนลด
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void order_discount_txtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_order_discount_txtMouseClicked

    }//GEN-LAST:event_order_discount_txtMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       //ให้แสดงข้อความเพียงครั้งเดียวเมื่อเข้าสู่หน้าจอ
        if(first_notification==false){
        JOptionPane.showMessageDialog(null,"กรุณากรอกส่วนลดเป็นจำนวนเต็มด้วยค่ะ\nถ้าหากไม่มีส่วนลด กรุณากรอกเลข 0 หรือ ปล่อยช่องให้เว้นว่างไว้ค่ะ\nRange(1-100) หน่วย:เปอร์เซนต์");
        first_notification = true;
       }else{
       }
    }//GEN-LAST:event_formWindowActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(customer_id_txt.getText().isEmpty()){ //ตรวจสอบการกรอกรหัสลูกค้า
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้กรอกรหัสลูกค้า\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);
                total_order_price = sum_order_price; //คำนวณส่วนลด
                set_total_text();//ใช้งานฟังก์ชั่นตั้งค่าราคาสุทธิ
                order_discount_txt.setText("");
                clear_customer();//เคลียร์ข้อมูลลูกค้า
        }else{
            try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
                DBObject customer_json = find_customer(customer_id_txt.getText()); //ค้นหาข้อมูลของลูกค้า
                String customer_name = customer_json.get("MS_CUSTOMER_NAME").toString(); //ชื่อลูกค้า
                String customer_phone = customer_json.get("MS_CUSTOMER_PHONE").toString(); //เบอร์โทรศัพท์
                String customer_email = customer_json.get("MS_CUSTOMER_EMAIL").toString(); //อีเมล
                JOptionPane.showMessageDialog(null,"ยืนยันรหัสลูกค้า "+customer_json.get("MS_CUSTOMER_ID")+"\n"
                                                 + "ชื่อลูกค้า "+customer_json.get("MS_CUSTOMER_NAME")+"\n\n"
                                                         + "ได้รับส่วนลด 5% จากราคาทั้งหมด"); //แสดงหน้าต่างข้อความรายละเอียดลูกค้า
                //ตั้งหน้าในกล่องข้อความ
                customer_name_txt.setText(customer_name);//ชื่อลูกค้า
                customer_email_txt.setText(customer_email);//เบอร์โทรศัพท์
                customer_phone_txt.setText(customer_phone);//อีเมล
                order_discount_txt.setText("5");
                int discount = Integer.parseInt(order_discount_txt.getText()); //ดึงส่วนลดมาใสในตัวแปร
                total_order_price = (double)sum_order_price-(((double)sum_order_price*(double)discount)/(double)100); //คำนวณส่วนลด
                set_total_text();//ใช้งานฟังก์ชั่นตั้งค่าราคาสุทธิ
                
            }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
                e.printStackTrace();//แสดงออกการผิดพลาดทางหน้าจอ
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ","",ERROR_MESSAGE); //แสดงหน้าต่างข้อความ
                clear_customer();//เคลียร์ข้อมูลลูกค้า
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setVisible(false);
        Main m = new Main();
        m.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JTextField customer_email_txt;
    private javax.swing.JTextField customer_id_txt;
    private javax.swing.JTextField customer_name_txt;
    private javax.swing.JTextField customer_phone_txt;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField order_discount_txt;
    private javax.swing.JTextField order_sum_txt;
    private javax.swing.JTable order_table;
    private javax.swing.JTextField order_total_txt;
    // End of variables declaration//GEN-END:variables
}
