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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Stream.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author nSense
 */
public class ArrayListJdk8 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<String> planets = Stream.of("Earth", "Mars", "Venus").collect(Collectors.toList());
        planets.add("Hello");
        System.out.println("Planets 1 "+planets.toString());
        
        /**This is possible by static import
        *import static java.util.stream.Stream.of;
        *import static java.util.stream.Collectors.toList;
        */
        List<String> planets2 = of("Earth", "Mars", "Venus").collect(toList());
        planets2.add("Hello");
        System.out.println("Planets 2 "+planets2.toString());
        
        //by -> import static java.util.stream.Collectors.toList;
        ArrayList<String> planets3 = of("Earth", "Mars", "Venus").collect(toCollection(ArrayList::new));
        planets3.add("Hello");
        System.out.println("Planets 2 "+planets2.toString());
        
    }
    
    
}
