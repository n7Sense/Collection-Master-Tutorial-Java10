
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
import java.util.Properties;

/**
 *
 * @author nSense
 */
public class PropertiesDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException{
      
        Properties p = new Properties();
        FileInputStream fis = new FileInputStream("E:\\Java\\Collection-Master-Tutorial-Java10\\Map\\Properties\\p\\sense.properties");   
        p.load(fis);
        System.out.println(p);
        String user = p.getProperty("user");
        System.out.println("User Name : "+user);
        p.setProperty("city", "Thane");
        FileOutputStream fos = new FileOutputStream("E:\\Java\\Collection-Master-Tutorial-Java10\\Map\\Properties\\p\\sense.properties");
        p.store(fos, "Updated by nSense");
        
       
    }
   
}

