
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
    MongoClient mongo;
    DB db;
    DBCollection DBC;
    static public int id;
    static public int status;
    
    public DB getConnect(){
    try{
        MongoClient mongo = new MongoClient("localhost",27017);
        db = mongo.getDB("InthaninDB");
        System.out.println(db);
        System.out.println(mongo.getConnectPoint());
    }catch(Exception e){
    }
    return db;
}
    public int getid(){
        return this.id;
    }
    public int getstatus(){
        return this.status;
    }
    public void setid(int id){
        this.id = id;
    }
    public void setstatus(int status){
        this.status = status;
    }
    public String month(int month){
    String output="";
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
    return output;
}
}
