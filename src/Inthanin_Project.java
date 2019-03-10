import com.mongodb.*;

public class Inthanin_Project {
    public static void main(String[] args) {
        try{
            MongoClient mongo = new MongoClient("localhost",27017);
            System.out.println("Connected");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
}
