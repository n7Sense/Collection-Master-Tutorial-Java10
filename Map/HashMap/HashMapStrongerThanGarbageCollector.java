/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hm;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
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

public class HashMapStrongerThanGarbageCollector {

    /**
     * @param args the command line arguments
     */
       public static void main(String[] args) throws InterruptedException{
       HashMap hm = new HashMap();
       ProtectedByHashMap pbh1 = new ProtectedByHashMap(1, "Rahul");
       ProtectedByHashMap  pbh2 = new ProtectedByHashMap(2, "Sunita");
       hm.put(pbh1, "Rahul");
       hm.put(pbh2, "Sunita");
       
       System.out.println("Before GC : "+hm);
       pbh1 = null;
       System.gc();
       Thread.sleep(5000);
       
       System.out.println("After GC : "+hm);
       
    }
   
}

class ProtectedByHashMap{

	int id;
	String name;
	ProtectedByHashMap(int id, String name){
		this.id = id;
		this.name = name;
	}

    @Override
    public String toString() {
        return "ProtectedByHashMap{" + "id=" + id + ", name=" + name + '}';
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