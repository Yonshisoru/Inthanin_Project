import com.mongodb.*;
import com.mongodb.util.JSON;
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
public class Main extends javax.swing.JFrame {
Variable v = new Variable();
MongoClient mongo;
DB db;
DBCollection DBC;
DBObject dbo;
boolean showpwd = false;
static String username;
static String position;
//-----------------------List / ArrayList -------------------------------------
List<DBObject>menu_component = new ArrayList<>();
List<DBObject>order_list = new ArrayList<>();
//-------------------Menu Panel Variable-----------------------
String menu_table_doubleclick = "";
//-------------------Order Variable--------------------------
boolean add_order = false;
int total_price = -1;
int order_list_price = -1;
//----------------------------------------------------------
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        set_logo();
        this.setLocationRelativeTo(null);
        disablepanel();
        sessionnow();
        System.out.println(v.getstatus());
    }
    
//---------------------Initilization-----------------------------
        public void set_logo(){
        try{
        ImageIcon imageIcon = new ImageIcon ("./image/main_icon.png");
        picture_label.setIcon(imageIcon);
                }catch(Exception e){
                    System.out.println(e);
                }
        //picture_label.setIcon(new ImageIcon("./image/Untitled-3.png"));
    }
        
        public void disablepanel(){
            first_panel.setVisible(false);
            order_panel.setVisible(false);
            customer_panel.setVisible(false);
            product_panel.setVisible(false);
            stock_panel.setVisible(false);
            menu_panel.setVisible(false);
            partner_panel.setVisible(false);
            employee_panel.setVisible(false);
            history_panel.setVisible(false);
}
//-------------------------------------------------------------------

//--------clear partner data--------------------------
    public void clear_partner(){
        partner_name_txt.setText("");
        partner_phone_txt.setText("");
        partner_email_txt.setText("");
        partner_home_txt.setText("");
        partner_locality_txt.setText("");
        partner_distict_txt.setText("");
        partner_province_txt.setText("");
        partner_post_txt.setText("");
        partner_type_combo.setSelectedIndex(0);
    }

    public void clear_product(){
        pro_name_txt.setText("");
        pro_price_txt.setText("");
        pro_type_combo.setSelectedIndex(0);
        partner_combo.setSelectedIndex(0);
    }
    
    public void clear_stock(){
        stock_combo.setSelectedIndex(0);
        stock_amount.setText("");
    }
    
    public void clear_emp(){
    Calendar calendar = Calendar.getInstance();
    //Date date =  calendar.getTime();
    //System.out.println(date); //15/10/2013
    employee_name_txt.setText("");
    employee_age_combo.setSelectedIndex(0);
    man_radio.setSelected(false);
    woman_radio.setSelected(false);
    employee_birthdate_txt.setSelectedDate(calendar);
    employee_phone_txt.setText("");
    employee_id_txt.setText("");
    employee_home_txt.setText("");
    employee_locality_txt.setText("");
    employee_district_txt.setText("");
    employee_province_txt.setText("");
    employee_email_txt.setText("");
    employee_position_combo.setSelectedIndex(0);
    employee_user_txt.setText("");
    employee_pwd_txt.setText("");
    showpwd_check.setSelected(false);
    showpwd = false;
    confirm.setSelected(false);
    employee_prefix.setSelectedIndex(0);
}
    
    public void clear_table(DefaultTableModel table){
        while(table.getRowCount()>0){
            table.removeRow(0);
        }
    }
    
    public void clear_menu(){
        menu_name_txt.setText("");
        menu_price_txt.setText("");
    }
    //-------------------------Partner--------------------------------------//
    public void set_productcombo(int k){
         partner_combo.removeAllItems();
         partner_combo.addItem("เลือกบริษัทคู่ค้า");
        try{
        DBCollection table = db.getCollection("MS_PARTNER");
        DBCursor cur = table.find();
        while(cur.hasNext()){
            DBObject kk = cur.next();
            if(k==1){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Drink")){
                partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }else if(k==2){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Bakery")){
                partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }else if(k==3){
                if(kk.get("MS_PARTNER_TYPE").toString().contains("Meal")){
                partner_combo.addItem(kk.get("MS_PARTNER_NAME").toString());
                }
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        //partner_combo
    }


//----------------------Employee---------------------------
    public void emp_age_combo(){
        for(int i =15;i<60;i++){
            employee_age_combo.addItem(""+i);
        }
    }
//----------------------Stocking-------------------
    public void set_stocking_product_combo(){ //เพิ่มข้อมูลรายการสินค้าในแท็บเลือก
         stock_combo.removeAllItems();
         stock_combo.addItem("เลือกสินค้า");
        try{
        DBCollection table = db.getCollection("MS_PRODUCT");
        DBCursor cur = table.find();
        while(cur.hasNext()){
            DBObject kk = cur.next();
            stock_combo.addItem(kk.get("MS_PRODUCT_NAME").toString());
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        //partner_combo
    }
//-----------------------------Menu---------------------------------------------------------
    public void set_menu_table(DefaultTableModel table){
        DefaultTableModel model = table;
        while(model.getRowCount()>0){
            model.removeRow(0);
        }
        Object[] row = new Object[3];
        for(DBObject e:menu_component){
            DBObject product = e;
            row[0] = product.get("MS_PRODUCT_ID");
            row[1] = product.get("MS_PRODUCT_NAME");
            row[2] = product.get("MS_PRODUCT_AMOUNT");
            model.addRow(row);
        }
    }
//-------------------------------Ordering--------------------------------------------
        public void set_order_table(DefaultTableModel table){
        DefaultTableModel model = table;
        while(model.getRowCount()>0){
            model.removeRow(0);
        }
        Object[] row = new Object[3];
        for(DBObject e:menu_component){
            DBObject product = e;
            row[0] = product.get("MS_MENU_ID");
            row[1] = product.get("MS_MENU_NAME");
            row[2] = product.get("MS_MENU_PRICE");
            model.addRow(row);
        }
    }
        public void clearing_order(){
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");
            DBCursor finding_order_list = get_order_list.find();
            while (finding_order_list.hasNext()) {
                get_order_list.remove(finding_order_list.next());
            }
            clear_table((DefaultTableModel)order_table.getModel());
        }
//---------------------------Put Product Data into ModelTable--------------------------------------------
    public void get_menu(DefaultTableModel table){
        DefaultTableModel model = table;
        Object[] row = new Object[3];
        DBCollection product_collection  = db.getCollection("MS_MENU");
        DBCursor product_finding = product_collection.find().sort(new BasicDBObject("MS_MENU_ID", 1));;
        while(product_finding.hasNext()){
            DBObject product_json = product_finding.next();
            row[0] = (int)product_json.get("MS_MENU_ID");
            row[1] = product_json.get("MS_MENU_NAME");
            row[2] = product_json.get("MS_MENU_PRICE");
            model.addRow(row);
        }
    }
    
//---------------------------Put Menu Data into --------------------------------------------
    public void get_product(DefaultTableModel table){
        DefaultTableModel model = table;
        Object[] row = new Object[4];
        DBCollection product_collection  = db.getCollection("MS_PRODUCT");
        DBCursor product_finding = product_collection.find();
        while(product_finding.hasNext()){
            DBObject product_json = product_finding.next();
            row[0] = (int)product_json.get("MS_PRODUCT_ID");
            row[1] = product_json.get("MS_PRODUCT_NAME");
            row[2] = product_json.get("MS_PRODUCT_PRICE");
            row[3] = product_json.get("MS_PRODUCT_AMOUNT");
            model.addRow(row);
        }
    }
//---------------------------Put Menu Data into --------------------------------------------
    public void get_order_list(DefaultTableModel table){
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
            
            model.addRow(row);
        }
    }    
        
//---------------------------Find & Check-------------------------------
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
    
    public boolean checkpartnerid(int id){
        DBCollection partnerdata = db.getCollection("MS_PARNTER");
        BasicDBObject data = new BasicDBObject("MS_PARTNER_ID",id);
        DBCursor find = partnerdata.find(data);
        if(find.hasNext()){
            return true;
        }else{
            return false;
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
        
        public int get_order_list_id(int menu_id){
            DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor finding_order_list_id = get_order_list.find().sort(sortObject);
              int order_list_id = -1;
                if(finding_order_list_id.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = finding_order_list_id.next();
                    try{//ดักจับข้อผิดพลาดของตัวเลขโดยใช้ try-catch
                    order_list_id = 1+(int)data.get("TRAN_ORDER_LIST_ID"); //นำค่าของPKมาบวกด้วย 1
                    }catch(Exception e){
                    double k = 1+(double)data.get("TRAN_ORDER_LIST_ID"); //นำค่าของPKมาบวกด้วย 1
                    order_list_id = (int)k ;
                    }
                }else{
                    order_list_id = 1;//สร้างPKของ MS_PRODUCT
                }
                return order_list_id;
        }
//------------------------logout-----------------------------------
    public void logout(){
            System.out.println("-----------logout----------------");
            DBCollection table = db.getCollection("TRAN_LOG");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor cur = table.find().sort(sortObject);
            int id = 0;
            DBCursor find = table.find();
            System.out.println(find.hasNext());
            if(find.hasNext()==true){
            System.out.println("eiei");
            int n = (int)cur.one().get("TRAN_LOG_ID");
            id = n+1;
            }else{
            id = 1;
            }
            DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
            String formattedDate = formatter.format(LocalDate.now());
            String month = v.month(Integer.parseInt(formattedDate.substring(4,6)));
            String year = formattedDate.substring(0,4);
            String date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());
            BasicDBObject document = new BasicDBObject();
            //-----------------------------------------------
            document.put("TRAN_LOG_ID",id);
            document.put("TRAN_LOG_DATE",month+" "+date+", "+year);
            document.put("TRAN_LOG_TIME",LocalTime.now().toString().substring(0,8));
            document.put("TRAN_LOG_TYPE","Logout");
            document.put("MS_EMPLOYEE_ID",v.getid());
            //-----------------------------------------------
            table.insert(document);
            System.out.println("เพิ่มประวัติการเข้าใช้เรียบร้อยแล้ว");
    }
    
//-----------------------------Check last session in system-----------------------------------------
    public void sessionnow(){
            db = v.getConnect();
            DBCollection log = db.getCollection("TRAN_LOG");
            DBCollection employee = db.getCollection("MS_EMPLOYEE");
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor cur = log.find().sort(sortObject);
            int emp_id = (int)cur.one().get("MS_EMPLOYEE_ID");
            BasicDBObject search = new BasicDBObject();
            search.put("MS_EMPLOYEE_ID",emp_id);
            DBObject findemp = employee.findOne(search);
            title_name_txt.setText(" "+findemp.get("MS_EMPLOYEE_NAME").toString());
            if(findemp.get("MS_EMPLOYEE_TYPE").toString().equals("Owner")){
                title_position_txt.setText(" เจ้าของร้าน");
            }else{
                title_position_txt.setText(" พนักงาน");
                product_btn.setVisible(false);
                stock_btn.setVisible(false);
                menu_btn.setVisible(false);
                partner_btn.setVisible(false);
                employee_btn.setVisible(false);
                history_btn.setVisible(false);
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
        customer_type_txt = new javax.swing.JComboBox<>();
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
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        customer_birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel22 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        product_panel = new javax.swing.JPanel();
        pro_name_txt = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        pro_price_txt = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        pro_type_combo = new javax.swing.JComboBox<>();
        partner_combo = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        stock_panel = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        stock_amount = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
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
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
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
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        partner_type_combo = new javax.swing.JComboBox<>();
        jButton18 = new javax.swing.JButton();
        order_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        order_table = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        menu_order_table = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        employee_panel = new javax.swing.JPanel();
        confirm = new javax.swing.JCheckBox();
        clear_btn = new javax.swing.JButton();
        confirm_btn = new javax.swing.JButton();
        showpwd_check = new javax.swing.JCheckBox();
        employee_district_txt = new javax.swing.JTextField();
        employee_id_txt = new javax.swing.JTextField();
        employee_age_combo = new javax.swing.JComboBox<>();
        man_radio = new javax.swing.JRadioButton();
        woman_radio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        employee_birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        employee_prefix = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
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
        jButton3 = new javax.swing.JButton();
        history_panel = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();

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
        title_name_txt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        title_panel.add(title_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 18, 250, 20));

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
        title_position_txt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        title_panel.add(title_position_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 48, 170, 20));
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

        customer_type_txt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ลูกค้าใหม่", "ลูกค้าขาจร" }));
        customer_panel.add(customer_type_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, -1, -1));

        customer_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นาง", "นางสาว", "เด็กชาย", "เด็กหญิง" }));
        customer_panel.add(customer_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));
        customer_panel.add(customer_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 340, -1));

        jLabel24.setText("ประเภทลูกค้า:");
        customer_panel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, -1));

        jLabel25.setText("ชื่อ:");
        customer_panel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jLabel26.setText("เบอร์โทรศัพท์:");
        customer_panel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, -1, -1));
        customer_panel.add(customer_phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 120, -1));

        jLabel27.setText("อีเมลล์:");
        customer_panel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, -1, -1));
        customer_panel.add(customer_email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 150, -1));

        jLabel28.setText("วันเกิด:");
        customer_panel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, -1, -1));

        customer_home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_home_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 50, 20));

        customer_locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_locality_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 50, 20));

        jLabel29.setText("ตำบล:");
        customer_panel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 230, -1, -1));

        jLabel30.setText("อำเภอ:");
        customer_panel.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, -1, -1));

        customer_district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_district_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 50, 20));

        customer_post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_post_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 50, 20));

        jLabel31.setText("รหัสไปรษณีย์:");
        customer_panel.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, -1, -1));

        jLabel32.setText("จังหวัด:");
        customer_panel.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, -1, -1));

        customer_province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_province_txtActionPerformed(evt);
            }
        });
        customer_panel.add(customer_province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 140, 20));

        jButton4.setText("ล้างข้อมูล");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        customer_panel.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 320, 110, 50));

        jButton5.setText("บันทึก");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        customer_panel.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 110, 50));

        jLabel33.setText("บ้านเลขที่:");
        customer_panel.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, -1, -1));
        customer_panel.add(customer_birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, -1, 20));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel22.setText("เพิ่มข้อมูลลูกค้า");
        customer_panel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        jButton17.setText("แก้ไขข้อมูลลูกค้า");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        customer_panel.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, -1));

        main_panel.add(customer_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        product_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        product_panel.add(pro_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 310, -1));

        jLabel46.setText("ชื่อสินค้า:");
        product_panel.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        jLabel53.setText("ราคา:");
        product_panel.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));
        product_panel.add(pro_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 70, -1));

        jButton9.setText("ล้างข้อมูล");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        product_panel.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 110, 50));

        jButton10.setText("บันทึก");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        product_panel.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 110, 50));

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

        jButton19.setText("แก้ไขข้อมูลสินค้า");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        product_panel.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, -1, -1));

        main_panel.add(product_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        stock_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setText("ชื่อสินค้า:");
        stock_panel.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, -1, -1));

        jLabel55.setText("จำนวนที่เพิ่มเข้าไปในสต๊อก:");
        stock_panel.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, -1, 20));
        stock_panel.add(stock_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 70, -1));

        jButton11.setText("ล้างข้อมูล");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        stock_panel.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 240, 110, 50));

        jButton12.setText("บันทึก");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        stock_panel.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, 110, 50));

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

        jButton13.setText("ล้างข้อมูล");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        menu_panel.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, -1, 30));

        jButton14.setText("ยืนยัน");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        menu_panel.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 80, 30));

        jButton15.setText("แก้ไขเมนู");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        menu_panel.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, -1, 30));

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

        jButton7.setText("ล้างข้อมูล");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        partner_panel.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 320, 110, 50));

        jButton8.setText("บันทึก");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        partner_panel.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 110, 50));

        jLabel51.setText("บ้านเลขที่:");
        partner_panel.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, -1, -1));

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel52.setText("เพิ่มข้อมูลคู่ค้า");
        partner_panel.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        jLabel39.setText("ประเภทของคู่ค้า:");
        partner_panel.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, -1, -1));

        partner_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "คู่ค้าส่วนประกอบเครื่องดื่ม", "คู่ค้าของหวานเบเกอรี่", "คู่ค้าส่วนประกอบของคาว" }));
        partner_panel.add(partner_type_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 180, 30));

        jButton18.setText("แก้ไขข้อมูลคู่ค้า");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        partner_panel.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

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

        jButton2.setText("เคลียร์");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        order_panel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, 100, 30));

        jButton6.setText("สั่ง");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        order_panel.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 420, 100, 30));

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

        jTextField2.setEditable(false);
        jTextField2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        order_panel.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 380, 93, 30));

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("ราคารวม");
        order_panel.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 380, -1, -1));

        jButton16.setText(">>");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        order_panel.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 60, -1));

        main_panel.add(order_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        employee_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confirm.setText("ข้อมูลที่ท่านกรอกมาเป็นความจริงทั้งหมด");
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });
        employee_panel.add(confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, -1, -1));

        clear_btn.setText("ล้างข้อมูล");
        clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_btnActionPerformed(evt);
            }
        });
        employee_panel.add(clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 410, 110, 40));

        confirm_btn.setText("ยืนยัน");
        confirm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_btnActionPerformed(evt);
            }
        });
        employee_panel.add(confirm_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 410, 110, 40));

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
        employee_panel.add(employee_id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 210, 20));

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
        employee_panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        employee_prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นางสาว" }));
        employee_panel.add(employee_prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel8.setText("เลขบัตรประจำตัวประชาชน:");
        employee_panel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, -1, -1));

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

        jLabel16.setText("อีเมลล์:");
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

        jButton3.setText("แก้ไขพนักงาน");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        employee_panel.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        main_panel.add(employee_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        history_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกเดือน", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มีนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" }));
        history_panel.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 150, 30));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "วันที่", "รหัสลูกค้า", "สินค้า", "ราคา", "ราคารวม"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        history_panel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 570, 330));

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("ยอดรวมทั้งหมด                                                บาท");
        history_panel.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, -1, -1));

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel41.setText("เดือน:");
        history_panel.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        main_panel.add(history_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        getContentPane().add(main_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 900, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
            Login g = new Login();
            g.setVisible(true);
            this.setVisible(false);
            logout();
    }//GEN-LAST:event_formWindowClosing

    private void order_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_btnActionPerformed
        disablepanel();
        order_panel.setVisible(true);
        clear_table((DefaultTableModel)menu_order_table.getModel());
        clear_table((DefaultTableModel)order_table.getModel());
        get_menu((DefaultTableModel)menu_order_table.getModel());
        set_order_table((DefaultTableModel)menu_product_table.getModel());
        get_order_list((DefaultTableModel)order_table.getModel());
        this.setTitle("หน้าต่างการออเดอร์");
    }//GEN-LAST:event_order_btnActionPerformed

    private void employee_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_btnActionPerformed
        disablepanel();
        emp_age_combo();
        employee_panel.setVisible(true);
        this.setTitle("หน้าต่างการเพิ่มพนักงาน");
    }//GEN-LAST:event_employee_btnActionPerformed

    private void customer_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btnActionPerformed
        disablepanel();
        customer_panel.setVisible(true);
        this.setTitle("หน้าต่างเพิ่มลูกค้า");
    }//GEN-LAST:event_customer_btnActionPerformed

    private void product_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_btnActionPerformed
        disablepanel();
        product_panel.setVisible(true);
        this.setTitle("หน้าต่างเพิ่มสินค้า");
    }//GEN-LAST:event_product_btnActionPerformed

    private void stock_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stock_btnActionPerformed
        disablepanel();
        stock_panel.setVisible(true);
        this.setTitle("หน้าต่างเพิ่มสต๊อก");
        set_stocking_product_combo();
    }//GEN-LAST:event_stock_btnActionPerformed

    private void menu_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_btnActionPerformed
        disablepanel();
        clear_table((DefaultTableModel)menu_product_table.getModel());
        get_product((DefaultTableModel)menu_product_table.getModel());
        menu_panel.setVisible(true);
        this.setTitle("หน้าต่างการจัดการเมนู");
    }//GEN-LAST:event_menu_btnActionPerformed

    private void partner_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_btnActionPerformed
        disablepanel();
        partner_panel.setVisible(true);
        this.setTitle("หน้าต่างการเพิ่มคู่ค้า");
    }//GEN-LAST:event_partner_btnActionPerformed

    private void customer_home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_home_txtActionPerformed

    private void customer_locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_locality_txtActionPerformed

    private void customer_district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_district_txtActionPerformed

    private void customer_post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_post_txtActionPerformed

    private void customer_province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_province_txtActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลลูกค้าใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){
        try{
            try{
                DBCollection table = db.getCollection("MS_CUSTOMER");
                BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
                DBCursor cur = table.find().sort(sortObject);
                //double n = (double)(cur.one().get("MS_CUSTOMER_ID"));
                int id = 0;
                DBCursor find = table.find();
                System.out.println(find.hasNext());
                if(find.hasNext()==true){
                    System.out.println("eiei");
                    int k = (int)cur.one().get("MS_CUSTOMER_ID");
                    id = k+1;
                }else{
                    id = 1;
                }
                System.out.println(id);
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                //DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                //String month = v.month(Integer.parseInt(customer_birthdate_txt.getText().substring(4,6)));
                //String year = customer_birthdate_txt.getText().substring(0,4);
                //String date = customer_birthdate_txt.getText().substring(customer_birthdate_txt.getText().length()-2,customer_birthdate_txt.getText().length());
                BasicDBObject document = new BasicDBObject();
                String birthdate = format.format(customer_birthdate_txt.getSelectedDate().getTime());
                document.put("MS_CUSTOMER_ID",(int)id);
                document.put("MS_CUSTOMER_NAME",customer_prefix.getSelectedItem().toString()+" "+customer_name_txt.getText());
                document.put("MS_CUSTOMER_PHONE",customer_phone_txt.getText());
                document.put("MS_CUSTOMER_EMAIL",customer_email_txt.getText());
                BasicDBObject address = new BasicDBObject();
                address.put("บ้านเลขที่", customer_home_txt.getText());
                address.put("ตำบล", customer_locality_txt.getText());
                address.put("อำเภอ", customer_district_txt.getText());
                address.put("จังหวัด", customer_province_txt.getText());
                address.put("รหัสไปรษณีย์", customer_post_txt.getText());
                document.put("MS_CUSTOMER_ADDRESS",address);
                document.put("MS_CUSTOMER_BIRTHDATE",birthdate);
                if(customer_type_txt.getSelectedIndex()==0){
                    document.put("MS_CUSTOMER_TYPE","New Customer");
                }else if(customer_type_txt.getSelectedIndex()==1){
                    document.put("MS_CUSTOMER_TYPE","Patron");
                }
                document.put("MS_CUSTOMER_POINTS",0);
                table.insert(document);
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ");
                //this.setVisible(false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){

        }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Order_confirm o = new Order_confirm();
        o.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void history_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_history_btnActionPerformed
        disablepanel();
        history_panel.setVisible(true);
        this.setTitle("หน้าต่างประวัติการขาย");
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

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    int partner_id = 0;
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลบริษัทคู่ค้าใหม่ใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงผลหน้าจอประเภท2คำตอบ
        try{//ดักจับข้อผิดพลาดโดยใช้ try-catch
            try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_PARTNER"); //ดึงข้อมูลของcollection MS_PRODUCT มาใส่ในตัวแปรที่ชื่อว่า table
                BasicDBObject partnerobject = new BasicDBObject().append("_id", -1); //ดึงข้อมูลตัวสุดท้าย
                System.out.println(partnerobject);
                DBCursor find = table.find().sort(partnerobject); //ค้นหาข้อมูลทั้งหมดในcollection MS_PRODUCT
                //System.out.println(find.hasNext());
                if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject getdoc = find.next();
                    System.out.println((int)getdoc.get("MS_PARTNER_ID"));
                    try{//ดักจับข้อผิดพลาดของตัวเลขโดยใช้ try-catch
                    partner_id = 1+(int)getdoc.get("MS_PARTNER_ID"); //นำค่าของPKมาบวกด้วย 1
                    }catch(Exception e){
                    double k = 1+(double)getdoc.get("MS_PARTNER_ID"); //นำค่าของPKมาบวกด้วย 1
                    partner_id = (int)k ;
                    }
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
                clear_partner();
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ"); //แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ
                //this.setVisible(false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
    int productid = 0;
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะเพิ่มข้อมูลสินค้าใหม่ใช่หรือไม่","System",YES_NO_OPTION)==YES_OPTION){ //แสดงผลหน้าจอประเภท2คำตอบ
        try{//ดักจับข้อผิดพลาดโดยใช้ try-catch
            try{ //ดักจับข้อผิดพลาดโดยใช้ try-catch
                DBCollection table = db.getCollection("MS_PRODUCT"); //ดึงข้อมูลของcollection MS_PRODUCT มาใส่ในตัวแปรที่ชื่อว่า table
                BasicDBObject productobject = new BasicDBObject().append("_id", -1); //ดึงข้อมูลตัวสุดท้าย
                DBCursor find = table.find().sort(productobject); //ค้นหาข้อมูลทั้งหมดในcollection MS_PRODUCT
                //System.out.println(find.hasNext());
                if(find.hasNext()==true){ //ถ้าหากว่ามีข้อมูลอยู่แล้ว
                    DBObject data = find.next();
                    try{//ดักจับข้อผิดพลาดของตัวเลขโดยใช้ try-catch
                    productid = 1+(int)data.get("MS_PRODUCT_ID"); //นำค่าของPKมาบวกด้วย 1
                    }catch(Exception e){
                    double k = 1+(double)data.get("MS_PRODUCT_ID"); //นำค่าของPKมาบวกด้วย 1
                    productid = (int)k ;
                    }
                }else{
                    productid = 1;//สร้างPKของ MS_PRODUCT
                }
                //System.out.println(productid);
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
                clear_product();
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ"); //แสดงผลทางหน้าจอว่าเพิ่มข้อมูลสำเร็จ
                //this.setVisible(false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if(stock_combo.getSelectedIndex()!=0){
        try{
            double amount = Double.parseDouble(stock_amount.getText());
            String product_name = stock_combo.getSelectedItem().toString();
            find_product_id(product_name);
            DBCollection partner = db.getCollection("MS_PRODUCT");
            DBObject partner_json = find_product_id(stock_combo.getSelectedItem().toString());
            if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า : "+product_name+
                                               "\nจำนวนที่มีอยู่  : "+(double)partner_json.get("MS_PRODUCT_AMOUNT")+
                                               "\nจำนวนที่จะเพิ่มเข้าไป : "+amount+
                                               "\n\nคุณยืนยันที่จะเพิ่มสินค้าตามจำนวนข้างต้นใช่หรือไม่","",YES_NO_OPTION)==YES_OPTION){
            try{
                BasicDBObject partner_id = new BasicDBObject("MS_PRODUCT_ID",partner_json.get("MS_PRODUCT_ID"));
                BasicDBObject setQuery = new BasicDBObject();
                BasicDBObject updateFields = new BasicDBObject();
                updateFields.append("MS_PRODUCT_AMOUNT",(double)partner_json.get("MS_PRODUCT_AMOUNT")+amount);
                setQuery.append("$set", updateFields);
                partner.update(partner_id,setQuery);
                partner_json = find_product_id(stock_combo.getSelectedItem().toString());
                JOptionPane.showMessageDialog(null, "ทำรายการเสร็จสิ้น!!\n"+
                                                    "\nชื่อสินค้า : "+product_name+""+
                                                    "\nจำนวนที่มีอยู่ขณะนี้ : "+(double)partner_json.get("MS_PRODUCT_AMOUNT"));
                clear_stock();
            }catch(Exception e){
                e.printStackTrace();
            }
            }else{
                clear_stock();
            }
        }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"จำนวนไม่ถูกต้อง\nกรุณาทำรายการใหม่ด้วยครับ","",ERROR_MESSAGE);
                clear_stock();
        }catch(Exception e){
                e.printStackTrace();
                clear_stock();
        }
        }else{
            JOptionPane.showMessageDialog(null,"กรุณาใส่ชื่อให้ถูกต้อง","",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะออกจากระบบหรือไม่","System",YES_NO_OPTION)==YES_OPTION){
        Login g = new Login();
            g.setVisible(true);
            this.setVisible(false);
            logout();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Edit_Partner g = new Edit_Partner();
            g.setLocationRelativeTo(null);
            g.setVisible(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
                Edit_Menu g = new Edit_Menu();
                g.setLocationRelativeTo(null);
                g.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
                Edit_Product g = new Edit_Product();
                g.setLocationRelativeTo(null);
                g.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        Edit_Customer g = new Edit_Customer();
        g.setLocationRelativeTo(null);
                g.setVisible(true);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Edit_Employee g = new Edit_Employee();
        g.setLocationRelativeTo(null);
        g.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

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
        showpwd = !showpwd;
        if(showpwd ==true){
            employee_pwd_txt.setEchoChar((char)0);
        }else{
            employee_pwd_txt.setEchoChar('*');
        }
    }//GEN-LAST:event_showpwd_checkActionPerformed

    private void confirm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_btnActionPerformed
        if(confirm.isSelected()==false){
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้ยืนยันข้อมูล\nกรุณายืนยันด้วยครับ",null,ERROR_MESSAGE);
        }else{
            String year;
            String month;
            String date;
            try{
                DBCollection table = db.getCollection("MS_EMPLOYEE");
                DBObject dbo = table.findOne(new BasicDBObject("_id",-1));
                BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
                DBCursor cur = table.find().sort(sortObject);
                int n;
                try{
                    n = (int)(cur.one().get("MS_EMPLOYEE_ID"));
                }catch(Exception e){
                    double k = (double)(cur.one().get("MS_EMPLOYEE_ID"));
                    n = (int)k;

                }
                //System.out.println(employee_birthdate_txt.getText());
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                String formattedDate = formatter.format(LocalDate.now());
                month = v.month(Integer.parseInt(formattedDate.substring(4,6)));
                year = formattedDate.substring(0,4);
                date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());
                String birthdate = format.format(employee_birthdate_txt.getSelectedDate().getTime());
                //System.out.println(formattedDate);
                System.out.println(month+" "+date+", "+year);
                System.out.println(birthdate);
                /*format.parse(LocalDate.now().toString());
                System.out.print(format.parse(LocalDate.now().toString()));*/
                BasicDBObject document = new BasicDBObject();
                document.put("MS_EMPLOYEE_ID",(int)n+1);
                document.put("MS_EMPLOYEE_USERNAME",employee_user_txt.getText());
                document.put("MS_EMPLOYEE_PWD",employee_pwd_txt.getText());
                document.put("MS_EMPLOYEE_NAME",employee_prefix.getSelectedItem().toString()+" "+employee_name_txt.getText());
                document.put("MS_EMPLOYEE_BIRTHDATE",birthdate);
                document.put("MS_EMPLOYEE_EMAIL",employee_email_txt.getText());
                document.put("MS_EMPLOYEE_PHONE",employee_phone_txt.getText());
                BasicDBObject address = new BasicDBObject();
                address.put("บ้านเลขที่", employee_home_txt.getText());
                address.put("ตำบล", employee_locality_txt.getText());
                address.put("อำเภอ", employee_district_txt.getText());
                address.put("จังหวัด", employee_province_txt.getText());
                address.put("รหัสไปรษณีย์", employee_post_txt.getText());
                document.put("MS_EMPLOYEE_ADDRESS",address);
                document.put("MS_EMPLOYEE_HIRED_DATE",month+" "+date+", "+year);
                document.put("MS_EMPLOYEE_TYPE",employee_position_combo.getSelectedItem().toString());
                table.insert(document);
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ");
                clear_emp();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnActionPerformed
        clear_emp();
    }//GEN-LAST:event_clear_btnActionPerformed

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmActionPerformed

    private void pro_type_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_type_comboActionPerformed
        set_productcombo(pro_type_combo.getSelectedIndex());
    }//GEN-LAST:event_pro_type_comboActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        try{
            String menu_name = menu_name_txt.getText();
            double menu_price = Double.parseDouble(menu_price_txt.getText());
            DBCollection get_menu = db.getCollection("MS_MENU");
            BasicDBObject menu_json = new BasicDBObject();
            BasicDBObject find_menu_condition = new BasicDBObject().append("_id", -1);
            DBCursor finding_last_menu = get_menu.find().sort(find_menu_condition);
                int menu_last_id;
                try{
                    menu_last_id = (int)(finding_last_menu.one().get("MS_MENU_ID"))+1;
                }catch(Exception e){
                    double k = (double)(finding_last_menu.one().get("MS_MENU_ID"));
                    menu_last_id = (int)k+1;
                }
            menu_json.put("MS_MENU_ID",menu_last_id);    
            menu_json.put("MS_MENU_NAME",menu_name);
            menu_json.put("MS_MENU_PRICE",(int)menu_price);
            menu_json.put("MS_PRODUCT",menu_component);
            get_menu.insert(menu_json);
            clear_table((DefaultTableModel)menu_product_table.getModel());
            clear_table((DefaultTableModel)menu_table.getModel());
            get_product((DefaultTableModel)menu_product_table.getModel());
            clear_menu();
            JOptionPane.showMessageDialog(null,"เพิ่มเมนูสำเร็จ");
            menu_component.clear();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"เพิ่มเมนูไม่สำเร็จ","",ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        clear_menu();
        clear_table((DefaultTableModel)menu_product_table.getModel());
        clear_table((DefaultTableModel)menu_table.getModel());
        get_product((DefaultTableModel)menu_product_table.getModel());
        menu_component.clear();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void menu_product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_product_tableMouseClicked
        String productid = menu_product_table.getModel().getValueAt(menu_product_table.getSelectedRow(),0).toString();
        String productname = menu_product_table.getModel().getValueAt(menu_product_table.getSelectedRow(),1).toString();
        try{
        DBCollection get_product = db.getCollection("MS_PRODUCT");
        if(menu_table_doubleclick.equals(productid)){
        try{
        double menu_per_amount = Double.parseDouble(JOptionPane.showInputDialog(null,"จำนวนที่คุณต้องการจะใช้ต่อเมนู 1 รายการ\nRange ของจำนวนจะอยู่ตั้งแต่ 0.01-9.99 หน่วย"));
        if(menu_per_amount<=0||menu_per_amount>=10){
            JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);
            menu_table_doubleclick = "";
        }else{
        if(JOptionPane.showConfirmDialog(null,"ชื่อสินค้า: "+productname+" \n"+
                                              "จำนวนที่จะใช้ต่อเมนู: "+ menu_per_amount+" \n"+
                                              "\n ยืนยืนการเพิ่มสินค้าของเมนูนี้","",YES_NO_OPTION)==YES_OPTION){
        BasicDBObject product_condition = new BasicDBObject("MS_PRODUCT_ID",Integer.parseInt(productid));
        DBCursor find_product = get_product.find(product_condition);
        if(find_product.hasNext()==true){
            DBObject product_json = find_product.next();
            product_json.put("MS_PRODUCT_AMOUNT", menu_per_amount);
            product_json.removeField("_id");
            product_json.removeField("MS_PRODUCT_PRICE");
            //System.out.println(product_json);
            /*new_product_json.append("$set", new BasicDBObject().append("MS_PRODUCT_AMOUNT", menu_per_amount));
            BasicDBObject searchQuery = (BasicDBObject)product_json;
            searchQuery.*/
            //product_json.update(new BasicDBObject("MS_PRODUCT_ID",Integer.parseInt(productid)));
            //System.out.println(product_json);
            menu_component.add(product_json);
            System.out.println("***************************************");
            for(DBObject d:menu_component){
                System.out.println(d);
            }
            System.out.println("***************************************");
            DefaultTableModel product_model = (DefaultTableModel) menu_product_table.getModel();
            DefaultTableModel menu_model = (DefaultTableModel) menu_table.getModel();
            product_model.removeRow(menu_product_table.getSelectedRow());
            set_menu_table(menu_model);
            //menu_product_table.remove(menu_product_table.getSelectedRow());
        }
        //System.out.println(menu_product_table.getModel().getValueAt(menu_product_table.getSelectedRow(),0));
        menu_table_doubleclick = "";
        }
        }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"คุณใส่ตัวเลขไม่ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);
            menu_table_doubleclick = "";
        }catch(NullPointerException e){
            menu_table_doubleclick = ""; 
        //JOptionPane.showMessageDialog(null,"คุณยังไม่ได้กรอกตัวเลขให้ถูกต้อง\nกรุณาทำรายการใหม่",null,ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
        }
        }else{
            menu_table_doubleclick = productid;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_menu_product_tableMouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if(add_order){
         int menu_id = (int)menu_order_table.getModel().getValueAt(menu_order_table.getSelectedRow(),0);
         //System.out.println(menu_id);
            try{
              DBCollection get_order = db.getCollection("TRAN_ORDER");
              DBCollection get_order_list = db.getCollection("TRAN_ORDER_LIST");
              DBCollection get_menu = db.getCollection("MS_MENU");
              BasicDBObject find_menu = new BasicDBObject("MS_MENU_ID",menu_id);
              DBCursor finding_menu = get_menu.find(find_menu);
              DBObject menu_json = null;
              System.out.println(">>"+get_order_list_id(menu_id));
              if(finding_menu.hasNext()){
                  menu_json = finding_menu.next();
                  menu_json.removeField("_id");
            try{
                int menu_amount = Integer.parseInt(JOptionPane.showInputDialog(null,""
                     + "รายการเมนู\n"
                     + "ชื่อเมนู: "+menu_json.get("MS_MENU_NAME")+"\n"
                     + "ราคาต่อเมนู: "+menu_json.get("MS_MENU_PRICE")+"\n"
                     + "\nกรุณากรอกจำนวนเมนูที่ต้องการค่ะ"
                     + "\n(กรุณากรอกเป็นตัวเลขจำนวนเต็ม)"));
                if(menu_amount<=0){
                    throw new NumberFormatException();
                }else{
                  order_list_price = ((int)menu_json.get("MS_MENU_PRICE")*menu_amount);
                  BasicDBObject insert_order_list = new BasicDBObject();
                  insert_order_list.put("TRAN_ORDER_LIST_ID",get_order_list_id(menu_id));
                  insert_order_list.put("TRAN_ORDER_LIST_AMOUNT",menu_amount);
                  insert_order_list.put("TRAN_ORDER_LIST_TOTAL_PRICE",order_list_price);
                  insert_order_list.put("MS_MENU_ID",(int)menu_json.get("MS_MENU_ID"));
                  get_order_list.insert(insert_order_list);
                  clear_table((DefaultTableModel)order_table.getModel());
                  get_order_list((DefaultTableModel)order_table.getModel());
                  System.out.println(menu_json.get("MS_MENU_ID"));
                  System.err.println(order_list_price);
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"คุณกรอกจำนวนไม่ถูกต้อง\nกรุณาทำรายการใหม่","",ERROR_MESSAGE);
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
              }else{
                  throw new NullPointerException();
              }
            }catch(NullPointerException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"ไม่พบข้อมูล\nกรุณาทำรายการใหม่","",ERROR_MESSAGE);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"คุณยังไม่ได้เลือกเมนูในรายการ\nกรุณาทำรายการใหม่ครับ","",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void menu_order_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_order_tableMouseClicked
      add_order = true;
    }//GEN-LAST:event_menu_order_tableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        menu_order_table.clearSelection();
        add_order = false;
        clearing_order();
        clear_table((DefaultTableModel)order_table.getModel());
        get_order_list((DefaultTableModel)order_table.getModel());
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
    private javax.swing.JPanel btn_panel;
    private javax.swing.JButton clear_btn;
    private javax.swing.JCheckBox confirm;
    private javax.swing.JButton confirm_btn;
    private datechooser.beans.DateChooserCombo customer_birthdate_txt;
    private javax.swing.JButton customer_btn;
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
    private javax.swing.JComboBox<String> customer_type_txt;
    private javax.swing.JComboBox<String> employee_age_combo;
    private datechooser.beans.DateChooserCombo employee_birthdate_txt;
    private javax.swing.JButton employee_btn;
    private javax.swing.JTextField employee_district_txt;
    private javax.swing.JTextField employee_email_txt;
    private javax.swing.JTextField employee_home_txt;
    private javax.swing.JTextField employee_id_txt;
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
    private javax.swing.JPanel history_panel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel main_panel;
    private javax.swing.JRadioButton man_radio;
    private javax.swing.JButton menu_btn;
    private javax.swing.JTextField menu_name_txt;
    private javax.swing.JTable menu_order_table;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JTextField menu_price_txt;
    private javax.swing.JTable menu_product_table;
    private javax.swing.JTable menu_table;
    private javax.swing.JButton order_btn;
    private javax.swing.JPanel order_panel;
    private javax.swing.JTable order_table;
    private javax.swing.JButton partner_btn;
    private javax.swing.JComboBox<String> partner_combo;
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
    private javax.swing.JPanel product_panel;
    private javax.swing.JCheckBox showpwd_check;
    private javax.swing.JTextField stock_amount;
    private javax.swing.JButton stock_btn;
    private javax.swing.JComboBox<String> stock_combo;
    private javax.swing.JPanel stock_panel;
    private javax.swing.JLabel title_name_txt;
    private javax.swing.JPanel title_panel;
    private javax.swing.JLabel title_position_txt;
    private javax.swing.JRadioButton woman_radio;
    // End of variables declaration//GEN-END:variables
}
