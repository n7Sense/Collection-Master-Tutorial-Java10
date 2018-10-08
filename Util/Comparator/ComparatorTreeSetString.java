
package ts;

import java.util.Comparator;

import java.util.TreeSet;

public class ComparatorTreeSetString{

    public static void main(String[] args) {
     
                
        TreeSet t = new TreeSet(new StringComparator());
        t.add("Raja");
        t.add("Rajkumar");
        t.add("Sunita");
        t.add("Prashad");
        t.add("Supriya");
        t.add("Akchhata");
        t.add("Archana");
        
        System.out.println(t); 
    }
}

class StringComparator implements Comparator{
    
    @Override
    public int compare(Object o1, Object o2) {
        
        String s1  = (String)o1;
        String s2  = o2.toString();
    
        return s2.compareTo(s1); // for descending alphabatical order
    }
    
}