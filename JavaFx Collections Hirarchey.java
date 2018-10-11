
                                        Collection(I)
                                        	|       
                    +--------------------------------------------------+
                    |                                                  |
                    List(I)       javafx.beans.Observable(I)          Set(I)
                    |                          |                        |
                    |						   |						|	
                    |>javafx.collections.ObservableList<E>              |>javafx.collections.ObservableSet<E>
                    |(also extends javafx.beans.Observable)             | (also extends javafx.beans.Observable)


                        


  javafx.beans.Observable(I)          Map(I)
                        |              |
                        |			   |
                     javafx.collections.ObservableMap<K,V> 
                    (also extends javafx.beans.Observable)




                                javafx.beans.Observable(I)
                                           |
                        +-------------------------------+                                
                        |                               |    
    javafx.collections.ObservableArray(I)               |>javafx.collections.ObservableList<E> ----------->
        |                                               |   (also extends java.util.List<E>)(I)
        |                                               |
        |>javafx.collections.ObservableFloatArray(I)    |>javafx.collections.ObservableMap<K,V>(I)
        |                                               |   (also extends java.util.Map<K,V>)
        |                                               |
        |>javafx.collections.ObservableIntegerArray(I)  |>javafx.collections.ObservableSet<E>(I)
                                                            (also extends java.util.Set<E>)
 


 





