/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whm;

import java.util.Collection;
import java.util.Comparator;
import java.util.WeakHashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

/**
 *
 * @author nSense
 */

public class WeakHashMapWeakerThanGarbageCollector {

    /**
     * @param args the command line arguments
     */
       public static void main(String[] args) throws InterruptedException{
       WeakHashMap whm = new WeakHashMap();
       NotProtectedByHashMap pbh1 = new NotProtectedByHashMap(1, "Rahul");
       NotProtectedByHashMap  pbh2 = new NotProtectedByHashMap(2, "Sunita");
       whm.put(pbh1, "Rahul");
       whm.put(pbh2, "Sunita");
       
       System.out.println("Before GC : "+whm);
       pbh1 = null;
       System.gc();
       Thread.sleep(5000);
       
       System.out.println("After GC : "+whm);
       
    }
   
}

class NotProtectedByHashMap{

	int id;
	String name;

	NotProtectedByHashMap(int id, String name){
		this.id = id;
		this.name = name;
	}

    @Override
    public String toString() {
        return "NotProtectedByHashMap{" + "id=" + id + ", name=" + name + '}';
    }

    public void finalize(){
        System.out.println("finalize method called");
    }
	
}

/* OutPut

	Before GC : {ProtectedByHashMap{id=2, name=Sunita}=Sunita, ProtectedByHashMap{id=1, name=Rahul}=Rahul}
After GC : {ProtectedByHashMap{id=2, name=Sunita}=Sunita, ProtectedByHashMap{id=1, name=Rahul}=Rahul}
BUILD SUCCESSFUL (total time: 5 seconds)
*/