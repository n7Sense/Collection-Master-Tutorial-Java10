
Navigable Interfaces:
As a part of 1.6 version te following 2 concept introduced in Collection Framework
	A.> NavigableSet
	B.> NavigableMap

A. NavigableSet

	1. it is the child interface of SortedSet and it defines several methods for Navigation purposes
			Collection (I) 1.2V
			   \|/
			   Set (I) 1.2V
			   \|/
			SortedSet (I) 1.2V
			   \|/
			NavigableSet (I) 1.6V
			   \|/
			 TreeSet  (I) 1.2V

	2. Methods:
			
			it return highest element which is <  E
		1.	public  E lower(E paramE);
			
			it return highest element which is >= E
		2.	public  E floor(E paramE);
			
			it return lowest element which is >= E
		3.	public  E ceiling(E paramE);
			
			it return lowest element which is > E
		4.	public  E higher(E paramE);
		
			remove and return first element
		5.	public  E pollFirst();
			
			remove and return last element
		6.	public  E pollLast();
		
		7. 	public  Iterator<E> iterator();
			
			it returns NavigableSet in reverse order
		8.	public  NavigableSet<E> descendingSet();
		
		9.	public  Iterator<E> descendingIterator();
		
		10.	public  NavigableSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
		
		11.	public  NavigableSet<E> headSet(E paramE, boolean paramBoolean);
		
		12.	public  NavigableSet<E> tailSet(E paramE, boolean paramBoolean);
		
		13.	public  SortedSet<E> subSet(E paramE1, E paramE2);
		
		14.	public  SortedSet<E> headSet(E paramE);
		
		15.	public  SortedSet<E> tailSet(E paramE);


B.	NavigableMap
		1. NavigableMap is the child interface of SortedMap, it defines several methods for Navigation purposes.	
			   Map (I) 1.2V
			   \|/
			SortedMap (I) 1.2V
				   \|/
			NavigableMap (I) 1.6V
			   \|/
			 TreeMap  (I) 1.2V

		2. NavigableMap defines the following methods.

			1.	public  Map.Entry<K, V> lowerEntry(K paramK);
				
			2.	public  K lowerKey(K paramK);
				
			3.	public  Map.Entry<K, V> floorEntry(K paramK);
				
			4.	public  K floorKey(K paramK);
				
			5.	public  Map.Entry<K, V> ceilingEntry(K paramK);
				
			7.	public  K ceilingKey(K paramK);
				
			8.	public  Map.Entry<K, V> higherEntry(K paramK);
				
			9.	public  K higherKey(K paramK);
				
			10.	public  Map.Entry<K, V> firstEntry();
				
			11.	public  Map.Entry<K, V> lastEntry();
				
			12.	public  Map.Entry<K, V> pollFirstEntry();
				
			13.	public  Map.Entry<K, V> pollLastEntry();
				
			14.	public  NavigableMap<K, V> descendingMap();
				
			15.	public  NavigableSet<K> navigableKeySet();
				
			16.	public  NavigableSet<K> descendingKeySet();
				
			17.	public  NavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2);
				
			18.	public  NavigableMap<K, V> headMap(K paramK, boolean paramBoolean);
				
			19.	public  NavigableMap<K, V> tailMap(K paramK, boolean paramBoolean);
				
			20.	public  SortedMap<K, V> subMap(K paramK1, K paramK2);
				
			21.	public  SortedMap<K, V> headMap(K paramK);
				
			22	public  SortedMap<K, V> tailMap(K paramK);