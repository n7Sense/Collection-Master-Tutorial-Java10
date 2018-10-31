	Hashtable
================

1. the underlying data structure for Hashtable is hash table.
2. insertion order is not preserved, and it is based on hashCode() of KEYS.
3. duplicate keys are not allowed, and values can be duplicated.
4. Hetrogenious objects are allowed for both keys and values.
5. Null is not allowed for both key and values, otherwise we will ge RuntimeException saying NullPointerException
6. 	public class Hashtable<K, V>  extends Dictionary<K, V>   implements Map<K, V>, Cloneable, Serializable
7. every methods present in Hashtable is synchronized and hence Hashtable object is Thread Safe.
8. Hashtable is the best choice if our frequent operation is for Search operation.

9. Constructor:

		create empty Hashtable object with default initialCapacity 11 and default fill ratio is (i.e loadFactor) 0.75F
		1. public Hashtable(){
		    	this(11, 0.75F);
		 	}

		 create empty Hashtable object with specified initialCapacity and default fill ratio is 0.75F
		2. 	public Hashtable(int initialCapacity){
		    	this(initialCapacity, 0.75F);
		  	}


		3. 	public Hashtable(int initialCapacity, float loadFactor){
			    if (initialCapacity < 0) {
			      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
			    }
			    if ((loadFactor <= 0.0F) || (Float.isNaN(loadFactor))) {
			      throw new IllegalArgumentException("Illegal Load: " + loadFactor);
			    }
			    if (initialCapacity == 0) {
			      initialCapacity = 1;
			    }
			    this.loadFactor = loadFactor;
			    this.table = new Entry[initialCapacity];
			    this.threshold = ((int)Math.min(initialCapacity * loadFactor, 2.147484E+009F));
		  	}

		4. 	public Hashtable(Map<? extends K, ? extends V> t){
		    	this(Math.max(2 * t.size(), 11), 0.75F);
		    	putAll(t);
		  	}
10. if we change hashCode() method of Temp class as. 
	public int hashCode(){
		return i+"";
	}
	 see the example program of HashtableBehindTheInsertionByHashCodeMethod.java

	according to the above program the insertion operation by hashCode, by default initial capacity 11.


	Bucket.
			-----------------
		10
			-----------------
		9
			-----------------
		8		
			-----------------
		7		16=F 
			-----------------
		6		6=C, 15=D
			-----------------
		5		5=A, 23=E
			-----------------
		4		
			-----------------
		3
			-----------------
		2		2=B
			-----------------
		1
			-----------------
		0
			-----------------

		Out/put
		{16=F, 15=D, 6=C, 23=E, 5=A, 2=B}

11. if we configure initialCapacity s 25 i.e Hashtable h =new Hashtable(25);
	then. behinnd the insertion by hashing data 

	Bucket.	
			-----------------
		24
			-----------------
		23		23=E
			-----------------
		22
			-----------------
		21
			-----------------
		20		
			-----------------
		19		
			-----------------
		18		
			-----------------
		17		
			-----------------
		16		16=F
			-----------------
		15		15=D
			-----------------
		14		2=B
			-----------------
		13
			-----------------
		12
			----------------- 
		11	
			-----------------
		10
			-----------------
		9
			-----------------
		8		
			-----------------
		7		
			-----------------
		6		6=C
			-----------------
		5		5=A
			-----------------
		4		
			-----------------
		3
			-----------------
		2		2=B
			-----------------
		1
			-----------------
		0
			-----------------

		Out/put
		{23=E, 16=F, 15=D, 6=C, 5=A, 2=B}