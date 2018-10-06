
package ts;

import java.util.Comparator;
import java.util.TreeSet;
public class ComparatorTreeSetHetrogenious{

  public static void main(String[] args) {
     
                
        TreeSet t = new TreeSet(new ComparatorWithHetroGenious());
        TreeSet t = new TreeSet(new MyComparator());
        t.add(new StringBuffer("Raja"));
        t.add(new StringBuffer("Rajkumar"));
        t.add(new StringBuffer("Sunita"));
        t.add(new StringBuffer("Prashad"));
        t.add(new StringBuffer("Supriya"));
        t.add(new StringBuffer("Akchhata"));
        t.add(new StringBuffer("Archana"));
        t.add("Sunita");
        t.add("rahul");
        t.add("archana");
        t.add("kumar");
        t.add("A");
        t.add("a");
        t.add("Abc");
        t.add("aBc");
        System.out.println(t); 
        
        System.out.println(t); 
    }

   
}

class ComparatorWithHetroGenious implements Comparator{
    
    @Override
    public int compare(Object o1, Object o2) {
        String s1  = o1.toString();
        String s2  = o2.toString();
        int l1 = s1.length();
        int l2 = s2.length();
        if(l1<l2)
            return -1;
        else if(l1>l2)
            return +1;
        else
           return s1.compareTo(s2);
     
    }
    
    
}
 