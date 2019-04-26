
import com.mongodb.*;
import java.time.LocalTime;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yonshisoru
 */
public class Variable {
//--------------------------MongoDB variable-------------------------------
    MongoClient mongo; //กำหนดตัวแปรประเภท MongoClient
    DB db; //กำหนดตัวแปรประเภท DB
    DBCollection DBC; //กำหนดตัวแปรประเภท DBCollection
    static public int id; //ัตัวแปรรหัสของพนักงานแบบ static
    static public int status; //ตัวแปรสถานะของพนักงานแบบ static
    
    public void getConnect(){ //ฟังก์ชั่นการเชื่อมต่อ
    try{ //ดักจับการทำงานผิดพลาดโดยใช้ try-catch
        MongoClient mongo = new MongoClient("localhost",27017);//เชื่อมต่อ Database Mongodb IP:localhost Port:27017
        db = mongo.getDB("InthaninDB");//ดึงข้อมูลฐานข้อมูล
    }catch(Exception e){ //ดักจับการทำงานผิดพลาดทุกอย่างโดยให้ชื่อว่า e
            e.printStackTrace(); //แสดงออกการผิดพลาดทางหน้าจอ
    }
}
    public String month(int month){ //ฟังก์ชั่นเปลี่ยนลำดับเดือนเป็นชื่อเดือน
    String output=""; //ตัวแปร Stringเพื่อใช้เก็บผลลัพธ์
    switch(month){
        case 1:output="January";break;
        case 2:output="Febuary";break;
        case 3:output="March";break;
        case 4:output="April";break;
        case 5:output="May";break;
        case 6:output="June";break;
        case 7:output="July";break;
        case 8:output="August";break;
        case 9:output="September";break;
        case 10:output="Octuber";break;
        case 11:output="November";break;
        case 12:output="December";break;
    }
    return output; //แสดงผลลัพธ์
}
}
