
A. HashSet	 extends AbstractSet<E>  implements Set<E>, Cloneable, java.io.Serializable
============

1. The UnderLine Data Structure is HashTable.
2. Duplicates objects are not allowed.
3. Insertion order is not preserved and it is based on Hashcode of Objects.
4. Null insertion is possible (but only once)
5. Hetrogenious Objects are allowed.
6. implements Serializable and Cloneable but not RandomAccess interface.
7. HashSet is the best choice if aor frequent operation is SEARCH OPERATION.
9. NOTE: In HashSet duplicate are not allowed, If we are trying to insert duplicate then we wont get any 
		Compile Time or Runtime Errors, and add() methods simply returns FALSE.
		EXAMPLE: HashSet sh = new HashSet();
				 S.o.pl( sh.add("A") ); return true
				 S.o.pl( sh.add("A") ); return false
10.Constructor
	a. HashSet hs =  new HashSet();
		create an empty HashSet object with default initial capacity 16 and DEFAULT FILL RATIO 0.75.
	b. HashSet h = new HashSet(int initialCapacity);
		creates an empty HashSet object with specified initial capacity and default fill RATIO 0.75.
	c. HashSet h = new HashSet(int initialCapacity, float fillRatio)
		creates an empty HashSet object with specified initial capacity and customized  fill RATIO  e.g 0.90, 0.25, etc..
	d. HashSet h = new HashSet(Collection<? extends E> paramCollection)
		creates an equivalent HashSet for the given Collection
		theas Constructor is mean for interconversion between Collection objects.

11. Fill Ratio /or Load Factor
	After filling how much Ratio a new HashSet object will be created, this Ratio is called 'Fill Ratio' or 'Load Factor'
	example Fille : 0.75 after filling 75% ratio a new HashSet object will be created

