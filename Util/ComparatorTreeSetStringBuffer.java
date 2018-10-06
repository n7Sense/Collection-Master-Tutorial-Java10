package ts;

import java.util.Comparator;
import java.util.TreeSet;
public class ComparatorTreeSetStringBuffer{

  public static void main(String[] args) {
     
                
        TreeSet t = new TreeSet(new MyComparator());
        t.add(new StringBuffer("Raja"));
        t.add(new StringBuffer("Rajkumar"));
        t.add(new StringBuffer("Sunita"));
        t.add(new StringBuffer("Prashad"));
        t.add(new StringBuffer("Supriya"));
        t.add(new StringBuffer("Akchhata"));
        t.add(new StringBuffer("Archana"));
        
        System.out.println(t); 
    }

   
}

class MyComparator implements Comparator{
    
    @Override
    public int compare(Object o1, Object o2) {
        String s1  = o1.toString();
        String s2  = o2.toString();
    
        return s2.compareTo(s1);
     
    }
    
    
}
 