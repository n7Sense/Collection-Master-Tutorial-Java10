
ArrayList  public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable

1.Duplicates are allowed
2. insertion order must be preserved.
3. hetrogenios object are allowed except TreeSet and TreeMap 
4. Null are allowed.
5.Resizable or growable.
5. New Capacity = (current capacity * 3/2)+1
6. implements RandomAccess(i.e marker interface) to provide randomly access element with same perfomence(speed)
	 where required ability will be provided autometically by the JVM.

7. if our frequent operation is Retrival is best but if for Insertion and deletion it is very worst
	because in ArrayList is there is 10 element and if i add value at 1index 

8. Every methods present in all are Non-Synchronized
9. Not thread safe.
10. Relatively performence vise good.
11. by default ArrayList is Non-Synchronized
11. But i want Thread safety with ArrayList object
	ArrayList al = new ArrayList();
	List l = Collections.synchronizedList(al);

12. default initial capicity methods 10 and incremented by (initial_capacity*3/2)+1


