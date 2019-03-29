import com.mongodb.*;
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
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

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
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        disablepanel();
        sessionnow();
        System.out.println(v.getstatus());
    }
public void clear(){
    Calendar calendar = Calendar.getInstance();
    //Date date =  calendar.getTime();
    //System.out.println(date); //15/10/2013
    imageshow_txt.setIcon(null);
    fname_txt.setText("");
    lname_txt.setText("");
    agecombo.setSelectedIndex(0);
    man_radio.setSelected(false);
    woman_radio.setSelected(false);
    birthdate_txt.setSelectedDate(calendar);
    phone_txt.setText("");
    id_txt.setText("");
    home_txt.setText("");
    locality_txt.setText("");
    district_txt.setText("");
    province_txt.setText("");
    email_txt.setText("");
    position_combo.setSelectedIndex(0);
    user_txt.setText("");
    pwd_txt.setText("");
    showpwd_check.setSelected(false);
    showpwd = false;
    confirm.setSelected(false);
    prefix.setSelectedIndex(0);
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
        jLabel1 = new javax.swing.JLabel();
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
        jLabel23 = new javax.swing.JLabel();
        type_txt = new javax.swing.JComboBox<>();
        prefix1 = new javax.swing.JComboBox<>();
        lname_txt1 = new javax.swing.JTextField();
        fname_txt1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        phone_txt1 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        email_txt1 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        home_txt1 = new javax.swing.JTextField();
        locality_txt1 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        district_txt1 = new javax.swing.JTextField();
        post_txt1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        province_txt1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        birthdate_txt1 = new datechooser.beans.DateChooserCombo();
        jLabel22 = new javax.swing.JLabel();
        product_panel = new javax.swing.JPanel();
        fname_txt3 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        phone_txt3 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        stock_panel = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        phone_txt4 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        menu_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        partner_panel = new javax.swing.JPanel();
        fname_txt2 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        phone_txt2 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        email_txt2 = new javax.swing.JTextField();
        home_txt2 = new javax.swing.JTextField();
        locality_txt2 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        district_txt2 = new javax.swing.JTextField();
        post_txt2 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        province_txt2 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        order_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
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
        district_txt = new javax.swing.JTextField();
        id_txt = new javax.swing.JTextField();
        agecombo = new javax.swing.JComboBox<>();
        man_radio = new javax.swing.JRadioButton();
        woman_radio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        birthdate_txt = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        prefix = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        fname_txt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lname_txt = new javax.swing.JTextField();
        locality_txt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        home_txt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        email_txt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        post_txt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        province_txt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        user_txt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        position_combo = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        phone_txt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        pwd_txt = new javax.swing.JPasswordField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        imageshow_txt = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Untitled-1.png"))); // NOI18N
        title_panel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

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

        jLabel23.setText("นามสกุล:");
        customer_panel.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, -1, -1));

        type_txt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ลูกค้าใหม่", "ลูกค้าขาจร" }));
        customer_panel.add(type_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, -1, -1));

        prefix1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นาง", "นางสาว", "เด็กชาย", "เด็กหญิง" }));
        customer_panel.add(prefix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));
        customer_panel.add(lname_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, 150, -1));
        customer_panel.add(fname_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 120, -1));

        jLabel24.setText("ประเภทลูกค้า:");
        customer_panel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, -1));

        jLabel25.setText("ชื่อ:");
        customer_panel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jLabel26.setText("เบอร์โทรศัพท์:");
        customer_panel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, -1, -1));
        customer_panel.add(phone_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 120, -1));

        jLabel27.setText("อีเมลล์:");
        customer_panel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, -1, -1));
        customer_panel.add(email_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 150, -1));

        jLabel28.setText("วันเกิด:");
        customer_panel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, -1, -1));

        home_txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home_txt1ActionPerformed(evt);
            }
        });
        customer_panel.add(home_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 50, 20));

        locality_txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locality_txt1ActionPerformed(evt);
            }
        });
        customer_panel.add(locality_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 50, 20));

        jLabel29.setText("ตำบล:");
        customer_panel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 230, -1, -1));

        jLabel30.setText("อำเภอ:");
        customer_panel.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, -1, -1));

        district_txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                district_txt1ActionPerformed(evt);
            }
        });
        customer_panel.add(district_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 50, 20));

        post_txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post_txt1ActionPerformed(evt);
            }
        });
        customer_panel.add(post_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 50, 20));

        jLabel31.setText("รหัสไปรษณีย์:");
        customer_panel.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, -1, -1));

        jLabel32.setText("จังหวัด:");
        customer_panel.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, -1, -1));

        province_txt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                province_txt1ActionPerformed(evt);
            }
        });
        customer_panel.add(province_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 140, 20));

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
        customer_panel.add(birthdate_txt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, -1, 20));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel22.setText("เพิ่มข้อมูลลูกค้า");
        customer_panel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        main_panel.add(customer_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        product_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        product_panel.add(fname_txt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 310, -1));

        jLabel46.setText("ชื่อสินค้า:");
        product_panel.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        jLabel53.setText("ราคา:");
        product_panel.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));
        product_panel.add(phone_txt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 70, -1));

        jButton9.setText("ล้างข้อมูล");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        product_panel.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 110, 50));

        jButton10.setText("บันทึก");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        product_panel.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, 110, 50));

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel60.setText("เพิ่มข้อมูลสินค้า");
        product_panel.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, -1));

        jLabel42.setText("ประเภทของสินค้า:");
        product_panel.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ส่วนประกอบเครื่องดื่ม", "เบเกอรี่", "ส่วนประกอบของคาว" }));
        product_panel.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 180, 30));

        main_panel.add(product_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        stock_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setText("ชื่อสินค้า:");
        stock_panel.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, -1, -1));

        jLabel55.setText("จำนวนที่เพิ่มเข้าไปในสต๊อก:");
        stock_panel.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, -1, 20));
        stock_panel.add(phone_txt4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 70, -1));

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

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกสินค้า" }));
        stock_panel.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 110, 30));

        main_panel.add(stock_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        menu_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTable4);

        menu_panel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, 290, 280));

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setText("ตารางสินค้า");
        menu_panel.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, -1, -1));

        jLabel57.setText("ราคา:");
        menu_panel.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, -1, -1));

        jLabel58.setText("ชื่อเมนู:");
        menu_panel.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel59.setText("หน้าต่างการสร้างเมนู");
        menu_panel.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("รายละเอียดของเมนู");
        menu_panel.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, -1, -1));
        menu_panel.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 60, -1));
        menu_panel.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 90, -1));

        jButton13.setText("ล้างข้อมูล");
        menu_panel.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, -1, 30));

        jButton14.setText("ยืนยัน");
        menu_panel.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 80, 30));

        jButton15.setText("แก้ไขเมนู");
        menu_panel.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, -1, 30));

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(jTable5);

        menu_panel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 290, 200));

        main_panel.add(menu_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        partner_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        partner_panel.add(fname_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 310, -1));

        jLabel43.setText("ชื่อบริษัท:");
        partner_panel.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, -1, -1));

        jLabel44.setText("เบอร์โทรศัพท์:");
        partner_panel.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, -1, -1));
        partner_panel.add(phone_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 120, -1));

        jLabel45.setText("อีเมลล์:");
        partner_panel.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, -1, -1));
        partner_panel.add(email_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 150, -1));

        home_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home_txt2ActionPerformed(evt);
            }
        });
        partner_panel.add(home_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 50, 20));

        locality_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locality_txt2ActionPerformed(evt);
            }
        });
        partner_panel.add(locality_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 50, 20));

        jLabel47.setText("ตำบล:");
        partner_panel.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 190, -1, -1));

        jLabel48.setText("อำเภอ:");
        partner_panel.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 190, -1, -1));

        district_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                district_txt2ActionPerformed(evt);
            }
        });
        partner_panel.add(district_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, 50, 20));

        post_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post_txt2ActionPerformed(evt);
            }
        });
        partner_panel.add(post_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 50, 20));

        jLabel49.setText("รหัสไปรษณีย์:");
        partner_panel.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 230, -1, -1));

        jLabel50.setText("จังหวัด:");
        partner_panel.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, -1, -1));

        province_txt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                province_txt2ActionPerformed(evt);
            }
        });
        partner_panel.add(province_txt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 230, 140, 20));

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

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "คู่ค้าส่วนประกอบเครื่องดื่ม", "คู่ค้าของหวานเบเกอรี่", "คู่ค้าส่วนประกอบของคาว" }));
        partner_panel.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 180, 30));

        main_panel.add(partner_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        order_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
        }

        order_panel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 380, 270));

        jButton2.setText("เคลียร์");
        order_panel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, 100, 30));

        jButton6.setText("สั่ง");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        order_panel.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 420, 100, 30));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

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
        order_panel.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 60, -1));

        main_panel.add(order_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 900, 460));

        employee_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confirm.setText("ข้อมูลที่ท่านกรอกมาเป็นความจริงทั้งหมด");
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });
        employee_panel.add(confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, -1, -1));

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
        employee_panel.add(showpwd_check, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, -1, -1));

        district_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                district_txtActionPerformed(evt);
            }
        });
        employee_panel.add(district_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 260, 50, 20));
        employee_panel.add(id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 220, 210, 20));

        agecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกอายุ" }));
        employee_panel.add(agecombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 80, 20));

        man_radio.setText("ชาย");
        employee_panel.add(man_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, -1, -1));

        woman_radio.setText("หญิง");
        employee_panel.add(woman_radio, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 110, -1, -1));

        jLabel5.setText("วัน/เดือน/ปีเกิด:");
        employee_panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 150, -1, -1));

        jLabel6.setText("อายุ:");
        employee_panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, -1, -1));

        birthdate_txt.setWeekStyle(datechooser.view.WeekDaysStyle.FULL);
        employee_panel.add(birthdate_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, -1, -1));

        jLabel7.setText("เพศ:");
        employee_panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, -1, -1));

        prefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "นาย", "นางสาว" }));
        employee_panel.add(prefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));

        jLabel8.setText("เลขบัตรประจำตัวประชาชน:");
        employee_panel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 220, -1, -1));

        jLabel9.setText("ชื่อ:");
        employee_panel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, -1, -1));
        employee_panel.add(fname_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 110, 20));

        jLabel10.setText("นามสกุล:");
        employee_panel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, -1, -1));

        jLabel11.setText("อำเภอ:");
        employee_panel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, -1, -1));

        lname_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lname_txtActionPerformed(evt);
            }
        });
        employee_panel.add(lname_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 70, 110, 20));

        locality_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locality_txtActionPerformed(evt);
            }
        });
        employee_panel.add(locality_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, 50, 20));

        jLabel12.setText("ตำบล:");
        employee_panel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 260, -1, -1));

        home_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home_txtActionPerformed(evt);
            }
        });
        employee_panel.add(home_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 260, 50, 20));

        jLabel13.setText("บ้านเลขที่:");
        employee_panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 260, -1, -1));

        email_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                email_txtActionPerformed(evt);
            }
        });
        employee_panel.add(email_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 340, 140, 20));

        jLabel14.setText("จังหวัด:");
        employee_panel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 300, -1, -1));

        jLabel15.setText("ตำแหน่ง:");
        employee_panel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 340, -1, -1));

        post_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post_txtActionPerformed(evt);
            }
        });
        employee_panel.add(post_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 300, 50, 20));

        jLabel16.setText("อีเมลล์:");
        employee_panel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 340, -1, -1));

        province_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                province_txtActionPerformed(evt);
            }
        });
        employee_panel.add(province_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, 140, 20));

        jLabel17.setText("ชื่อผู้ใช้งาน:\n");
        employee_panel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, -1, -1));

        user_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_txtActionPerformed(evt);
            }
        });
        employee_panel.add(user_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 140, 20));

        jLabel18.setText("รหัสผ่าน:");
        employee_panel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        position_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "เลือกตำแหน่ง", "Employee", "Owner" }));
        employee_panel.add(position_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 340, -1, -1));

        jLabel19.setText("รหัสไปรษณีย์:");
        employee_panel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 300, -1, -1));

        phone_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phone_txtActionPerformed(evt);
            }
        });
        employee_panel.add(phone_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, 140, 20));

        jLabel20.setText("เบอร์โทรศัพท์:");
        employee_panel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, -1, -1));

        jButton3.setText("Upload");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        employee_panel.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, -1));

        jLabel21.setText("อัพโหลดรูปภาพ");
        employee_panel.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        pwd_txt.setToolTipText("");
        pwd_txt.setEchoChar('*');
        employee_panel.add(pwd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 140, -1));

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setMaximumSize(new java.awt.Dimension(310, 260));
        jDesktopPane1.setMinimumSize(new java.awt.Dimension(310, 260));
        jDesktopPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imageshow_txt.setForeground(new java.awt.Color(255, 255, 255));
        imageshow_txt.setMaximumSize(new java.awt.Dimension(310, 260));
        imageshow_txt.setMinimumSize(new java.awt.Dimension(310, 260));
        imageshow_txt.setPreferredSize(new java.awt.Dimension(310, 260));
        jDesktopPane1.add(imageshow_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 210));

        employee_panel.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 310, 210));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("หน้าต่างเพิ่มพนักงาน");
        employee_panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

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

    private void district_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_district_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_district_txtActionPerformed

    private void lname_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lname_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lname_txtActionPerformed

    private void locality_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locality_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locality_txtActionPerformed

    private void home_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_home_txtActionPerformed

    private void email_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_email_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_email_txtActionPerformed

    private void post_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_post_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_post_txtActionPerformed

    private void province_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_province_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_province_txtActionPerformed

    private void user_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_txtActionPerformed

    private void phone_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phone_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phone_txtActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        BufferedImage img = null;
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        try {
            File f=chooser.getSelectedFile();
            img = ImageIO.read(f);
            Image dimg = img.getScaledInstance(imageshow_txt.getWidth(), imageshow_txt.getHeight(),
                Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            imageshow_txt.setIcon(imageIcon);
            filename = f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        try{
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf=new byte[1024];
            for(int readNum; (readNum=fis.read(buf))!=-1;){
                bos.write(buf,0,readNum);
            }
            photo = bos.toByteArray();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnActionPerformed
        clear();
    }//GEN-LAST:event_clear_btnActionPerformed

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
                double n = (double)(cur.one().get("MS_EMPLOYEE_ID"));
                System.out.println(birthdate_txt.getText());
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                String formattedDate = formatter.format(LocalDate.now());
                month = v.month(Integer.parseInt(formattedDate.substring(4,6)));
                year = formattedDate.substring(0,4);
                date = formattedDate.substring(formattedDate.length()-2,formattedDate.length());
                //System.out.println(formattedDate);
                System.out.println(month+" "+date+", "+year);
                /*format.parse(LocalDate.now().toString());
                System.out.print(format.parse(LocalDate.now().toString()));*/
                BasicDBObject document = new BasicDBObject();
                document.put("MS_EMPLOYEE_ID",(int)n+1);
                document.put("MS_EMPLOYEE_USERNAME",user_txt.getText());
                document.put("MS_EMPLOYEE_PWD",pwd_txt.getText());
                document.put("MS_EMPLOYEE_NAME",prefix.getSelectedItem().toString()+" "+fname_txt.getText()+" "+lname_txt.getText());
                document.put("MS_EMPLOYEE_BIRTHDATE",birthdate_txt.getText());
                document.put("MS_EMPLOYEE_EMAIL",email_txt.getText());
                document.put("MS_EMPLOYEE_PHONE",phone_txt.getText());
                BasicDBObject address = new BasicDBObject();
                address.put("บ้านเลขที่", home_txt.getText());
                address.put("ตำบล", locality_txt.getText());
                address.put("อำเภอ", district_txt.getText());
                address.put("จังหวัด", province_txt.getText());
                address.put("รหัสไปรษณีย์", post_txt.getText());
                document.put("MS_EMPLOYEE_ADDRESS",address);
                document.put("MS_EMPLOYEE_HIRED_DATE",month+" "+date+", "+year);
                document.put("MS_EMPLOYEE_TYPE",position_combo.getSelectedItem().toString());
                document.put("MS_EMPLOYEE_PHOTO",filename);
                table.insert(document);
                JOptionPane.showMessageDialog(null,"ทำการลงทะเบียนสำเร็จ");
                this.setVisible(false);
            }catch(Exception e){

            }
        }
    }//GEN-LAST:event_confirm_btnActionPerformed

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmActionPerformed

    private void showpwd_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpwd_checkActionPerformed
        showpwd = !showpwd;
        if(showpwd ==true){
            pwd_txt.setEchoChar((char)0);
        }else{
            pwd_txt.setEchoChar('*');
        }
    }//GEN-LAST:event_showpwd_checkActionPerformed

    private void order_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_btnActionPerformed
        disablepanel();
        order_panel.setVisible(true);
        this.setTitle("หน้าต่างการออเดอร์");
    }//GEN-LAST:event_order_btnActionPerformed

    private void employee_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_btnActionPerformed
        disablepanel();
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
    }//GEN-LAST:event_stock_btnActionPerformed

    private void menu_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_btnActionPerformed
        disablepanel();
        menu_panel.setVisible(true);
        this.setTitle("หน้าต่างการจัดการเมนู");
    }//GEN-LAST:event_menu_btnActionPerformed

    private void partner_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partner_btnActionPerformed
        disablepanel();
        partner_panel.setVisible(true);
        this.setTitle("หน้าต่างการเพิ่มคู่ค้า");
    }//GEN-LAST:event_partner_btnActionPerformed

    private void home_txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_home_txt1ActionPerformed

    private void locality_txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locality_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locality_txt1ActionPerformed

    private void district_txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_district_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_district_txt1ActionPerformed

    private void post_txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_post_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_post_txt1ActionPerformed

    private void province_txt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_province_txt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_province_txt1ActionPerformed

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
                BasicDBObject document = new BasicDBObject();
                document.put("MS_CUSTOMER_ID",(int)id);
                document.put("MS_CUSTOMER_NAME",prefix.getSelectedItem().toString()+" "+fname_txt.getText()+" "+lname_txt.getText());
                document.put("MS_CUSTOMER_PHONE",phone_txt.getText());
                document.put("MS_CUSTOMER_EMAIL",email_txt.getText());
                BasicDBObject address = new BasicDBObject();
                address.put("บ้านเลขที่", home_txt.getText());
                address.put("ตำบล", locality_txt.getText());
                address.put("อำเภอ", district_txt.getText());
                address.put("จังหวัด", province_txt.getText());
                address.put("รหัสไปรษณีย์", post_txt.getText());
                document.put("MS_CUSTOMER_ADDRESS",address);
                document.put("MS_CUSTOMER_BIRTHDATE",birthdate_txt.getText());
                document.put("MS_CUSTOMER_TYPE",type_txt.getSelectedItem().toString());
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

    private void home_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_home_txt2ActionPerformed

    private void locality_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locality_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locality_txt2ActionPerformed

    private void district_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_district_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_district_txt2ActionPerformed

    private void post_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_post_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_post_txt2ActionPerformed

    private void province_txt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_province_txt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_province_txt2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(JOptionPane.showConfirmDialog(null,"คุณต้องการที่จะออกจากระบบหรือไม่","System",YES_NO_OPTION)==YES_OPTION){
        Login g = new Login();
            g.setVisible(true);
            this.setVisible(false);
            logout();
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
    private javax.swing.JComboBox<String> agecombo;
    private datechooser.beans.DateChooserCombo birthdate_txt;
    private datechooser.beans.DateChooserCombo birthdate_txt1;
    private javax.swing.JPanel btn_panel;
    private javax.swing.JButton clear_btn;
    private javax.swing.JCheckBox confirm;
    private javax.swing.JButton confirm_btn;
    private javax.swing.JButton customer_btn;
    private javax.swing.JPanel customer_panel;
    private javax.swing.JTextField district_txt;
    private javax.swing.JTextField district_txt1;
    private javax.swing.JTextField district_txt2;
    private javax.swing.JTextField email_txt;
    private javax.swing.JTextField email_txt1;
    private javax.swing.JTextField email_txt2;
    private javax.swing.JButton employee_btn;
    private javax.swing.JPanel employee_panel;
    private javax.swing.JPanel first_panel;
    private javax.swing.JTextField fname_txt;
    private javax.swing.JTextField fname_txt1;
    private javax.swing.JTextField fname_txt2;
    private javax.swing.JTextField fname_txt3;
    private javax.swing.JButton history_btn;
    private javax.swing.JPanel history_panel;
    private javax.swing.JTextField home_txt;
    private javax.swing.JTextField home_txt1;
    private javax.swing.JTextField home_txt2;
    private javax.swing.JTextField id_txt;
    private javax.swing.JLabel imageshow_txt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField lname_txt;
    private javax.swing.JTextField lname_txt1;
    private javax.swing.JTextField locality_txt;
    private javax.swing.JTextField locality_txt1;
    private javax.swing.JTextField locality_txt2;
    private javax.swing.JPanel main_panel;
    private javax.swing.JRadioButton man_radio;
    private javax.swing.JButton menu_btn;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JButton order_btn;
    private javax.swing.JPanel order_panel;
    private javax.swing.JButton partner_btn;
    private javax.swing.JPanel partner_panel;
    private javax.swing.JTextField phone_txt;
    private javax.swing.JTextField phone_txt1;
    private javax.swing.JTextField phone_txt2;
    private javax.swing.JTextField phone_txt3;
    private javax.swing.JTextField phone_txt4;
    private javax.swing.JComboBox<String> position_combo;
    private javax.swing.JTextField post_txt;
    private javax.swing.JTextField post_txt1;
    private javax.swing.JTextField post_txt2;
    private javax.swing.JComboBox<String> prefix;
    private javax.swing.JComboBox<String> prefix1;
    private javax.swing.JButton product_btn;
    private javax.swing.JPanel product_panel;
    private javax.swing.JTextField province_txt;
    private javax.swing.JTextField province_txt1;
    private javax.swing.JTextField province_txt2;
    private javax.swing.JPasswordField pwd_txt;
    private javax.swing.JCheckBox showpwd_check;
    private javax.swing.JButton stock_btn;
    private javax.swing.JPanel stock_panel;
    private javax.swing.JLabel title_name_txt;
    private javax.swing.JPanel title_panel;
    private javax.swing.JLabel title_position_txt;
    private javax.swing.JComboBox<String> type_txt;
    private javax.swing.JTextField user_txt;
    private javax.swing.JRadioButton woman_radio;
    // End of variables declaration//GEN-END:variables
byte[] photo=null;
String filename = null;
}
