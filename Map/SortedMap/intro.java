SortedMap
==========

1. It is a child interface of Map interface.
2. if we want to represent a group of key value pair object as according to some sorting order of KEYS then we 
	shuld go for SortedMp.
3. Sorting is based on the key but NOT based on the values.

4. Sorted map define the following specific methods.
	
	
	SortedSet { 101-A, 103-B, 104-C, 107-D, 125-E, 136-F }

	1. public  Object firstKey();  -> 101
  
  	2. public  Object lastKey();	-> 136

  	3. public  SortedMap<K, V> headMap(107); -> {101=A, 103=B, 104=C, }
  
  	4. public  SortedMap<K, V> tailMap(107); -> { 107=D, 125=E, 136=F }

  	5. public  SortedMap<K, V> subMap(103, 125); -> { 103=B, 104=C, 107-D}

  	7. public  Comparator<? super K> comparator(); -> null for Default sorting order.
