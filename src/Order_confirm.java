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
    Variable v = new Variable();
    boolean first_notification = false;
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    DBObject dbo;
    double sum_order_price = 0;
    double total_order_price = 0;
    int discount_price = 0;
    String time = LocalTime.now().toString().substring(0,2)+"-"+LocalTime.now().toString().substring(3,5)+"-"+LocalTime.now().toString().substring(6,8);
    List<DBObject> order_list = new ArrayList<>();
    
    public void printInvoice(String id,double income){
        int count = 0;
        //String time = LocalTime.now().toString().substring(0,2)+"-"+LocalTime.now().toString().substring(3,5)+"-"+LocalTime.now().toString().substring(6,8);
        String filename = "$"+LocalDate.now()+"$"+time+"$"+/*t.getorderid()*/id+".pdf";
        try{
        Document doc = new Document(new Rectangle(218,400),20f, 0f, 0f, 0f);
        BaseFont baseFont = BaseFont.createFont("fonts/fontsgod.ttf", BaseFont.IDENTITY_H,true);
        Font font = new Font(baseFont,7);
        Font topicfont = new Font(baseFont,6);
        Font bigfont = new Font(baseFont,10);
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
    public void clear_customer(){
                customer_id_txt.setText("");
                customer_name_txt.setText("");
                customer_email_txt.setText("");
                customer_phone_txt.setText("");
    }
    public void get_order_list(DefaultTableModel table){
        db = v.getConnect();
        DefaultTableModel model = table;
        Object[] row = new Object[4];
        DBCollection product_collection  = db.getCollection("TRAN_ORDER_LIST");
        DBCursor product_finding = product_collection.find();
        if(product_finding.hasNext()==true){
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
        }else{
            throw new NullPointerException();
        }
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
        
         public DBObject find_customer(String id){ //ค้นหารหัสของสินค้าจากชื่อ
            DBCollection customer = db.getCollection("MS_CUSTOMER");
            BasicDBObject data = new BasicDBObject("MS_CUSTOMER_ID",id);
            DBCursor find = customer.find(data);
            DBObject customer_json = null;
            //int productid = -1; //-1 = null
            try{
                customer_json = find.next();
                //System.out.println(product_json);
                //productid = (int)product_json.get("MS_PRODUCT_ID");
                //System.out.println(productid);
            }catch(Exception e){
                throw new NullPointerException();
            }
        return customer_json;
    }
        public int get_invoice_id(){
            DBCollection get_order_list = db.getCollection("TRAN_INVOICE");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor finding_order_list_id = get_order_list.find().sort(sortObject);
              int order_list_id = -1;
                if(finding_order_list_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_list_id.next();
                    order_list_id = 1+(int)data.get("TRAN_INVOICE_ID"); //นำค่าของPKมาบวกด้วย 1
                }else{
                    order_list_id = 1;//สร้างPKของ MS_PRODUCT
                }
                return order_list_id;
        }
        public String get_order_id(){
            DBCollection get_order = db.getCollection("TRAN_ORDER");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor finding_order_id = get_order.find().sort(sortObject);
              String order_list_id = null;
              int id = 0;
                if(finding_order_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_id.next();
                    id = 1+Integer.parseInt(data.get("TRAN_ORDER_ID").toString().substring(1,data.get("TRAN_ORDER_ID").toString().length())); //นำค่าของPKมาบวกด้วย 1
                    if(id>=10){
                        order_list_id = "O0"+id;
                    }else if(id>100){
                        order_list_id = "O"+id;
                    }else if(id<10){
                        order_list_id = "O00"+id;
                    }
                }else{
                    order_list_id = "O00"+1;
                }
                return order_list_id;
        }
        public DBObject get_menu(int id){
           try{
           DBCollection get_order = db.getCollection("MS_MENU"); 
           BasicDBObject sortObject = new BasicDBObject("MS_MENU_ID",id);
           DBCursor finding_menu = get_order.find(sortObject);
           DBObject menu_json = null;
           if(finding_menu.hasNext()==true){
               menu_json = finding_menu.next();
           }
           return menu_json;
           }catch(Exception e){
               e.printStackTrace();
               throw new NullPointerException();
           }
        }
        
        public void edit_product(int id,double amount_using){
           try{
           DBCollection get_product = db.getCollection("MS_PRODUCT"); 
           BasicDBObject sortObject = new BasicDBObject("MS_PRODUCT_ID",id);
           BasicDBObject updateFields = new BasicDBObject();
           BasicDBObject setQuery = new BasicDBObject();
           DBCursor finding_product = get_product.find(sortObject);
           DBObject product_json = null;
               while(finding_product.hasNext()){
               product_json = finding_product.next();
               //System.out.println(product_json);
               double product_amount = (double)product_json.get("MS_PRODUCT_AMOUNT");
               System.out.println(product_amount-amount_using);
               if(product_amount-amount_using>=0){
                   DecimalFormat f = new DecimalFormat("##.00");
                   double output = product_amount-amount_using;
                   updateFields.put("MS_PRODUCT_AMOUNT",Double.parseDouble(f.format(output)));
               }else{
                   throw new Exception();
               }
           setQuery.append("$set", updateFields);
           //updateFields.append("MS_PARTNER_TYPE",type);
           get_product.update(product_json,setQuery);
           }
           }catch(Exception e){
               e.printStackTrace();
               throw new ArithmeticException();
           }
           
        }
          public int sessionnow(){
            db = v.getConnect();
            DBCollection log = db.getCollection("TRAN_LOG");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor cur = log.find().sort(sortObject);
            int emp_id = (int)cur.one().get("MS_EMPLOYEE_ID");
            BasicDBObject search = new BasicDBObject();
            search.put("MS_EMPLOYEE_ID",emp_id);
            return emp_id;
    }
          public String get_date(){
                 DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                String formattedDate = formatter.format(LocalDate.now());
                String month = v.month(Integer.parseInt(formattedDate.substring(4,6)));
                String year = formattedDate.substring(0,4);
                String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());
                return month+" "+date+", "+year;
          }
         public void clearing_order(){
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");
            DBCursor finding_order_list = get_order_list.find();
            while (finding_order_list.hasNext()) {
                get_order_list.remove(finding_order_list.next());
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
        try{
        double income = 0;
        do{
        income = Double.parseDouble(JOptionPane.showInputDialog(null,"ได้รับเงินจากลูกค้าจำนวน(บาท)"));
        if(income>=Double.parseDouble(order_total_txt.getText())){
            break;
        }else{
            JOptionPane.showMessageDialog(null,"คุณกรอกจำนวนเงินไม่ถูกต้อง\nกรุณาทำรายการใหม่ค่ะ","",ERROR_MESSAGE);
        }
        }while(income<Double.parseDouble(order_total_txt.getText()));
        DBCollection get_order = db.getCollection("TRAN_ORDER");
        DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");
        DBCollection get_invoice = db.getCollection("TRAN_INVOICE");
        int menu_amount = 0;
        DBCursor find_order_list = get_order_list.find();
        DBObject order_list_json = null;
        while(find_order_list.hasNext()){
            order_list_json = find_order_list.next();
            order_list.add(order_list_json);
            int menu_id = (int)order_list_json.get("MS_MENU_ID");
            menu_amount = (int)order_list_json.get("TRAN_ORDER_LIST_AMOUNT");
            for(int i =0;i<menu_amount;i++){
            BasicDBList list = (BasicDBList)(get_menu(menu_id).get("MS_MENU_PRODUCT"));
            for(Object el: list) {
                DBObject product = (DBObject)el;
                edit_product((int)product.get("MS_PRODUCT_ID"),(double)product.get("MS_PRODUCT_AMOUNT"));
            }
            }
        }
        BasicDBObject order_doc = new BasicDBObject();
        order_doc.put("TRAN_ORDER_ID",get_order_id());
        if(!customer_id_txt.getText().isEmpty()){
            order_doc.put("MS_CUSTOMER_ID",customer_id_txt.getText());
        }
        order_doc.put("MS_EMPLOYEE", sessionnow());
        order_doc.put("TRAN_ORDER_TOTAL_PRICE",order_total_txt.getText());
        order_doc.put("TRAN_ORDER_DATE",get_date());
        order_doc.put("TRAN_ORDER_TIME",LocalTime.now().toString().substring(0, 8));
        order_doc.put("TRAN_ORDER_LIST",order_list);
        BasicDBObject invoice_doc = new BasicDBObject();
        invoice_doc.put("TRAN_INVOICE_ID",get_invoice_id());
        invoice_doc.put("TRAN_ORDER_ID",get_order_id());
        invoice_doc.put("TRAN_INVOICE_DATE",get_date());
        invoice_doc.put("TRAN_INVOICE_TIME",LocalTime.now().toString().substring(0, 8));;
        printInvoice(get_order_id(),income);
        get_invoice.insert(invoice_doc);
        get_order.insert(order_doc);
        clearing_order();
        order_list.clear();
        this.setVisible(false);
        Main m = new Main();
        m.setVisible(false);
        m.setVisible(true);
        }catch(ArithmeticException e){
            JOptionPane.showMessageDialog(null,"ไม่สามารถดำเนินการได้\nเนื่องจากวัตถุดิบไม่เพียงพอ","",ERROR_MESSAGE);
        }catch(NullPointerException e){
            order_list.clear();
        }catch(Exception e){
            e.printStackTrace();
        }
        //printInvoice();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{
        int discount = Integer.parseInt(order_discount_txt.getText());
        total_order_price = (double)sum_order_price-(((double)sum_order_price*(double)discount)/(double)100);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(customer_id_txt.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้กรอกรหัสลูกค้า\nกรุณาลองใหม่ค่ะ","",ERROR_MESSAGE);
            clear_customer();
        }else{
            try{
                DBObject customer_json = find_customer(customer_id_txt.getText());
                String customer_id = customer_json.get("MS_CUSTOMER_ID").toString();
                String customer_name = customer_json.get("MS_CUSTOMER_NAME").toString();
                String customer_phone = customer_json.get("MS_CUSTOMER_PHONE").toString();
                String customer_email = customer_json.get("MS_CUSTOMER_EMAIL").toString();
                JOptionPane.showMessageDialog(null,"ยืนยันรหัสลูกค้า "+customer_json.get("MS_CUSTOMER_ID")+"\n"
                                                 + "ชื่อลูกค้า "+customer_json.get("MS_CUSTOMER_NAME"));
                customer_name_txt.setText(customer_name);
                customer_email_txt.setText(customer_email);
                customer_phone_txt.setText(customer_phone);
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูล\nกรุณาลองใหม่อีกครั้งค่ะ","",ERROR_MESSAGE);
                clear_customer();
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
