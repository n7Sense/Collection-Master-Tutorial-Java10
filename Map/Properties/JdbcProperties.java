
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package p;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author nSense
 */
public class JdbcProperties {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException{
      
        Properties p = new Properties();
        FileInputStream fis = new FileInputStream("E:\\Java\\Collection-Master-Tutorial-Java10\\Map\\Properties\\p\\db.properties");   
        p.load(fis);
        Set entrySet = p.entrySet();
        System.out.println("Set Object : "+entrySet);
        Iterator itr = entrySet.iterator();
        while(itr.hasNext()){
            Map.Entry entry = (Map.Entry)itr.next();
            System.out.println("key & value \t"+entry.getKey()+" : "+entry.getValue());
        }
        
        p.setProperty("hibernate.hbm2ddl", "update");
        FileOutputStream fos = new FileOutputStream("E:\\Java\\Collection-Master-Tutorial-Java10\\Map\\Properties\\p\\db.properties");
        p.store(fos, "Updated by nSense");
        
       
    }
   
}

