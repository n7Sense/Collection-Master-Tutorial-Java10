	Map
============

1. Map is NOT child interface of Collection.
2. If we want to represent a group of Object as <key, value> pair the we shuld go for map.
3. both keys and Values are object only.
4. Duplicate keys are not allowed but values can be duplicated.
5. each <Key, Value> pair is called entry. hence Map is consider as a Collection of Entry Object.
6. Methods
	1. public  Object put(K paramK, Object paramV);
		To add one key value pair to tha map, if the key is already present then old value will be replaced with the new value,
		and returns old value.
		e.g -> 	map.put(101, "Sunita"); return null 
			->	map.put(102, "Rahul"); return null
			->  map.put(101, "Love"); return Sunita

	2. public  Object remove(Object paramObject);
  
  	3. public  void putAll(Map<? extends K, ? extends V> paramMap);
  
  	4. public  void clear();
  			
  	5. public  boolean equals(Object paramObject);
  	
  	6. public  int size();
  
 	7. public boolean isEmpty();
  
  	8. public boolean containsKey(Object paramObject);
  
  	9. public boolean containsValue(Object paramObject);

  	10. public  Set<K> keySet();
  
  	11. public  Collection<V> values();
  
  	12. public  Set<Map.Entry<K, V>> entrySet();

7. 	A map is a group of key value pair, and each <key, value> pair is called an enty hence a Map is consider as a Collection of entry Object.
	without existing Map Object there is no chance of existing entry Object Hence Entry interface is define inside Map interface.
	and the Entry specific methods is
	1. public  Set<K> keySet(); 2. public  Collection<V> values(); 3. public  Set<Map.Entry<K, V>> entrySet();
	and we can only apply on Entry Object.

8. To get synchronized version of all Map implementation Class
	Map smap = Collections.synchronizedMap(mapImplementationClassObject)