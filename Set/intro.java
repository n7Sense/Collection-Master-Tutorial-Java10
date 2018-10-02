	Set 	1.2
==========
1.If we want to represent a group of a single objects as a single entity where Duplicates are not allowed and 
	Insertion Order not preserved. then we shuld go for Set.
2. Set interface doesn't contain any new methods we have to use only Collection. interface methods.

A. HashSet
============

1. The UnderLine Data Structure is HashTable.
2. Duplicates objects are not allowed.
3. Insertion order is not preserved and it is based on Hashcode of Objects.
4. Null insertion is possible (but only once)
5. Hetrogenious Objects are allowed.
6. implements Serializable and Cloneable but RandomAccess interface.
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


To get synchronized version of all Set implementation Class

Set s = Collections.synchronizedSet(setImplementationClassObject)