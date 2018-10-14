	TreeMap
==============

1. The underlying data structure is RED-BLACK TREE.
2. Insertion order is not preserved and it is based on som SORTING ORDER of KEYS.
3. Duplicate keys are not allowed, but values can be duplicated.
4. If we are depending on default natural sorting order then KEYS shuld be HOMOGENIOUS and Comparable otherwise we will get
	RE: ClassCastException.
5. If we are defining our own sorting by Comparator then keys need not be HOMOGENIOUS and Comparable, we can take Hetrogenious
	Non-Comparable object also.
6. whether we are depending on Default natural sorting order or Customized sorting order there are no restriction for values
	we can take Hetrogenious Non-Comparable object also.

7. NULL Expectence : 
	 1.for Non-Empty TreeMap if we are trying to insert an entry with NULL key then we will get RE: NullPointerException.
	 
	 2.for empty TreeMap as the first entry with null key is allowed, but after inserting that entry if we are trying to insert
		any other entry then we will get RE: NullPointerException

	NOTE:
		The NULL expectence RULE applicable Until 1.6 version only from 1.7 version NULL is not allowed for key
		but for values we can use null any number of times. there is no restriction. whether it is 1.6 versionor 1.7.

8. Constructor: 
		
		for default natural sorting order
	1.	public TreeMap() {
	        this.comparator = null;
	    }

	    for customized sorting order
	2.  public TreeMap(Comparator<? super K> comparator) {
	        this.comparator = comparator;
	    }

	3.  public TreeMap(SortedMap<K, ? extends V> m) {
	        this.comparator = m.comparator();
	        try {
	            buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
	        } catch (IOException | ClassNotFoundException localIOException) {
	        }
	    }
	    
	4. 	public TreeMap(Map<? extends K, ? extends V> m) {
	        this.comparator = null;
	        putAll(m);
	    }