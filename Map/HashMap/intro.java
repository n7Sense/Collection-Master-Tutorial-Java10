	
	HashMap
==============

1. The underlying Data Structure is hash table.
2. Insertion order is not preserved, and it is based on HashCode of Keys.
3. Duplicates keys are not allowed, but values can be duplicated.
4. Hetrogenious objects are allowed for both <key & value>.
5. Null is allowed for keys <only once>.
6. Null is allowed for values <any no. of times>
7. public class HashMap<K, V>   extends AbstractMap<K, V>   implements Map<K, V>, Cloneable, Serializable 
	but not implements RandomAccess interface.
8. HashMap is the best choice, if aur frequent operation is Search operation.

9. Constructor:
	1. public HashMap() {    this.loadFactor = 0.75F;   } 
		create an empty HashMap object with default initialCapacity is 16 and defaultFillRatio 0.75F.

	2. public HashMap(int initialCapacity) { this(initialCapacity, 0.75F); }
		create an empty HashMap object with specified initialCapacity and defaultFillRatio 0.75F. 
	
	3. public HashMap(int initialCapacity, float loadFactor)
		create an empty HashMap object with specified initialCapacity and specified defaultFillRatio. 

	4. public HashMap(Map<? extends K, ? extends V> m)
		intercommunication between Map objects.

10. Example in program
	HashMapKeyInsertionMechanism.java of Out/Put is
	{10 = Sunita}
	that means internally HashMap use equals() methods to identify the duplication of the keys.
	therefor equals() methods is for content comparison, so I1 and I2 contain same value i.e 10
	there for previous value is replaced by JVM.

11. Exampl: HashMap is stronger than Garbage Collector. HashMap dominate Garbage Collector.
	see example in HashMapStrongerThanGarbageCollector.java

12.  In the above Example the ProtectedByHashMap object not eligible for GC because it is assocoated with HashMap.
		int this case out put is 

	Before GC : {ProtectedByHashMap{id=2, name=Sunita}=Sunita, ProtectedByHashMap{id=1, name=Rahul}=Rahul}
	finalize method called :
	After GC : {ProtectedByHashMap{id=2, name=Sunita}=Sunita, ProtectedByHashMap{id=1, name=Rahul}=Rahul}
