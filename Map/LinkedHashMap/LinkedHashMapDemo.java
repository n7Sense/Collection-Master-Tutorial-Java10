/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lhm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nSense
 */
public class LinkedHashMapDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       LinkedHashMap lhm = new LinkedHashMap();
       lhm.put("myname", "Rahul");
       lhm.put("lv", "Sunita");
       lhm.put("friend", "Kunal");
       lhm.put("east1", "Mahendra");
       lhm.put("est2", "Keshav");
       lhm.put("east3", "Madhusudan");
       
       System.out.println("HahMap Object: "+lhm);
       lhm.put("east1", "Krishna");
       System.out.println("HahMap Object replace: "+lhm);
       Set set = lhm.keySet();
       System.out.println("Key Set Object: "+set);
       Collection c  = lhm.values();
       System.out.println("Collection Object values: "+c);
       Set entrySet = lhm.entrySet();
       System.out.println("EntrySet Object: "+entrySet);
       
        Iterator itr = entrySet.iterator();
        while(itr.hasNext()){
            Map.Entry e = (Map.Entry)itr.next();
            System.out.println("Key & Value"+e.getKey()+" : "+e.getValue());
            if(e.getKey().equals(103))
                e.setValue("Manoj");
        }
        System.out.println("Change : "+lhm);
       
    }
   
}
Out/Put:
HahMap Object: {myname=Rahul, lv=Sunita, friend=Kunal, east1=Mahendra, est2=Keshav, east3=Madhusudan}
HahMap Object replace: {myname=Rahul, lv=Sunita, friend=Kunal, east1=Krishna, est2=Keshav, east3=Madhusudan}
Key Set Object: [myname, lv, friend, east1, est2, east3]
Collection Object values: [Rahul, Sunita, Kunal, Krishna, Keshav, Madhusudan]
EntrySet Object: [myname=Rahul, lv=Sunita, friend=Kunal, east1=Krishna, est2=Keshav, east3=Madhusudan]
Key & Valuemyname : Rahul
Key & Valuelv : Sunita
Key & Valuefriend : Kunal
Key & Valueeast1 : Krishna
Key & Valueest2 : Keshav
Key & Valueeast3 : Madhusudan
Change : {myname=Rahul, lv=Sunita, friend=Kunal, east1=Krishna, est2=Keshav, east3=Madhusudan}
