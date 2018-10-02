/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static java.util.Arrays.asList;
/**
 *
 * @author nSense
 */
public class TraditionalArrayListJdk7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<String> planets = new ArrayList<String>();
        planets.add("Earth");
        planets.add("Mars");
        planets.add("Venus"); 
        planets.remove(2);
        
        System.out.println("Planets1 : "+planets.toString()+" Size : "+planets.size());
        
        //Creates an immutable list containing only the specified object. The returned list is serializable.
        List<String> planets2 = Collections.singletonList("Immutable");
       // planets2.set(0, "Mutable");
        System.out.println("Planet2 : "+planets2.toString());
        /*
        ****Not allowed throws java.lang.UnsupportedOperationException
        *   planets2.add(planets);
        */
        
        /*Creates a fixed-size list backed by the specified array. This method acts as bridge between array-based 
        *and collection-based APIs, in combination with Collection.toArray(). 
        *The returned list is serializable and implements RandomAccess. 
        *This method also provides a convenient way to create a fixed-size
        *list initialized to contain several elements:
        **/
        List<String> planets3 = Arrays.asList("Earth", "Mars", "Venus");
        /*planets3.add("Uteras"); 
        *throws RE: java.lang.UnsupportedOperationException
        *Because in a aslist(param) the param is an Array which is fixed in size
        */
        System.out.println("Planet 3"+planets3.toString());
        
        
        //because we already imported java.util.Arrays
        List<String> planets4 = asList("Earth", "Mars", "Venus", "Uteras");
        System.out.println("Planet 4 : "+planets4.toString());  
        
        String[] arr = new String[3];
        arr[0] = "Earth";
        arr[1] = "Mars";
        arr[2] = "Venus";
        
        List<String> planets5 = Arrays.asList(arr);
        System.out.println("Planet 5 : "+planets5);  
        
        List<String> planets6 = new ArrayList<String>(Arrays.asList("Earth", "Mars", "Venus"));
        planets6.set(0, "Sunita");
        planets6.add("Neptune");
        System.out.println("Planet 6 : "+planets6.toString());     
    }
    
}
