Difference Between HashMap and HashTable
==========================================


				HashMap																HashTable
		----------------------												----------------------

1. Every methods in HashMap is non-synchronized					+	Every methods present in HashTable is synchronized
																+	
2.  at a time multiple threads are allowed to operate 			+	At a time only One thread is allowed to operate on
	in HashMap Object. and hence it is not thread safe.			+	HashTable, and hence a it thread safe.
																+	
3. Relatively performence is high because threads are not       +	Relatively performence is low because threads are
	required to wait to operate on HashMap object.				+	required to wait to operate on HashTable object.
																+
4. Null is allowed for both key ad value.						+	Null is not allowed for keys and values otherwise
																+	we will ge NullPointerException.
																+
5. introduced in java 1.2 version, and it not Legacy			+	introduced in java 1.0 version and it is Legacy
																+	
6. synchronized the HashMap										+	Hashtable is internally synchronized and
HashMap map = Collections.synchronizedMap(HashMapObject)		+	cant be unsynchronized.
																+
7. Iterator in HashMap is fail-fast.							+	Enumerator in Hashtable is not fail-fast
																+
8.	HashMap is traversed by Iterator.							+	Hashtable is traversed by Enumerator and Iterator.
																+
9.	HashMap inherits AbstractMap class.							+	Hashtable inherits Dictionary class.