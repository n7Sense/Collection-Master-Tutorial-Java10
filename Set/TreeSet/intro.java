	TreeSet
==============

1. The underlying data structure is Balanced Tree.
2. Duplicates Object are not allowed.
3. Insertion order not preserved.
4. Hetrogenious Objects are not allowed. otherwise we will get RunTime Exception saying ClassCastException.
5. Null insertion possible (but only once).
6. TreeSet<E>  extends AbstractSet<E>  implements NavigableSet<E>, Cloneable, Serializable.
7. All Objects will be inserted based on some Sorting Order. it may be Default natural sorting order or Customized Sorting order.

8. Constructor:
	 	public TreeSet()
	 	create empty TreeSet, where elements will be inserted in Default natural sorting order

	 	public TreeSet(Comparator<? super E> paramComparator)
	 	create new empty TreeSet Object, where the elements will be inserted according to Customized Sorting Order.

		TreeSet(NavigableMap<E, Object> paramNavigableMap)

	  	public TreeSet(SortedSet<E> paramSortedSet)

	  	public TreeSet(Collection<? extends E> paramCollection)

9. Until java 1.6 NULL is allowed to the Empty TreeSet but from 1.7 onwards NULL is not allowed even as the first element.
	i.e 'null' such type story not applicable for TreeSet from 1.7 onwards.

10. if we are depending on default sorting order then compalsory that object shuld be Homogenious and Comparable otherwise
	we will get RuntimeException saying ClassCastException.

11. an Object is said to be Compairable if and only if corresponding class implements Compairable interface.

12. String class and all Wrapper classes already implements Compairable interface. but StringBuffer class doesent implements
	Comparable interface. otherwise we will get 
	RE: Exception in thread "main" java.lang.ClassCastException: java.base/java.lang.StringBuffer 
	cannot be cast to java.base/java.lang.Comparable

13. Comparable contain only one method i.e 
	public int compareTo(Object t)

	obj1.compareTo(obj2);
		-> return -ve if obj1 has to come before obj2.
		-> return +ve if obj1 has to come after obj2.
		-> return 0 obj1 and obj2 are equal (duplicate).

14. If Default natural Sorting order not available or, if we are not cirtified with Default natural Sorting order then we can go
	for customized sorting order by using Comparator.

15. Comparable meant for Default Natural Sorting order where Comparator meant for Customized Sorting order.

