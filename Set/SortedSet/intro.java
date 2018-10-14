	SortedSet   extends Set<E>
=================

1. If we want to represent a group of an individual Objects according to some sorting order without duplicte
	then we shuld go for SortedSet.
2. SortedSet interface define total 8 Specific methods

e.g  {100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110}
  
  public abstract SortedSet<E> subSet(101, 105); 101....104 return SortedSet whose elements are >= obj1 & < obj2
  
  public abstract SortedSet<E> headSet(106); 100...105  return SortedSet object whose elements < than that specified parameter
  
  public abstract SortedSet<E> tailSet(106); 106...110 return SortedSet whose elements are >= specified param Object
  
  public abstract E first(); 100
  
  public abstract E last(); 110
  
  public Spliterator<E> spliterator()

  public abstract Comparator<? super E> comparator();
  return Comparator object that describe the underlying sorting technique. If we are using default natural
  SORTING order then we will get null.

  3.NOTE: The default natural sorting order for Number is Ascending order
  			and for String object is Alphanatical order.


