package hm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HashMapDemo{

	public static void main(String[] args) {
        
       HashMap hm = new HashMap();
       hm.put("myname", "Rahul");
       hm.put("lv", "Sunita");
       hm.put("friend", "Kunal");
       hm.put("east1", "Mahendra");
       hm.put("est2", "Keshav");
       hm.put("east3", "Madhusudan");
       
       System.out.println("HahMap Object: "+hm);
       hm.put("east1", "Krishna");
       System.out.println("HahMap Object replace: "+hm);
       Set set = hm.keySet();
       System.out.println("Key Set Object: "+set);
       Collection c  = hm.values();
       System.out.println("Collection Object values: "+c);
       Set entrySet = hm.entrySet();
       System.out.println("EntrySet Object: "+entrySet);
       
        Iterator itr = entrySet.iterator();
        while(itr.hasNext()){
            Map.Entry e = (Map.Entry)itr.next();
            System.out.println("Key & Value"+e.getKey()+" : "+e.getValue());
            if(e.getKey().equals(103))
                e.setValue("Manoj");
        }
        System.out.println("Change : "+hm);
       
       
    }
   
}