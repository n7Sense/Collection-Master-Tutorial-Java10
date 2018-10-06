	LinkedHashMap
=====================

1. public class LinkedHashMap<K, V>   extends HashMap<K, V>  implements Map<K, V>
2. It is exactly same as HashMap (including methods and constructor), except the following difference.
3. The underlying data structure is combination of LinkedList and HashTable (hybrid Data structure)
4. Insertion order is preserved
5. Introduced in 1.4 version
6. LinkedHashSet and  LinkedHashMap are commonly used for developping cashed based application.

7. Constructor.
		1. public LinkedHashMap() { this.accessOrder = false;  }

		2.  public LinkedHashMap(int initialCapacity){
		    	super(initialCapacity);
		    	this.accessOrder = false;
		  	}

		3. public LinkedHashMap(int initialCapacity, float loadFactor){
		    	super(initialCapacity, loadFactor);
		    	this.accessOrder = false;
		  	}
		  
		4. public LinkedHashMap(Map<? extends K, ? extends V> m) {
		    	this.accessOrder = false;
		    	putMapEntries(m, false);
		  	}
		  
		 5. public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
		    	super(initialCapacity, loadFactor);
		    	this.accessOrder = accessOrder;
		  	}
  