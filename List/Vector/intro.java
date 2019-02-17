	Vector
=============

1. The underline Data-Structure is resizable Array or Growable Array
2. Insertion order is preserved.
3. Duplicates are allowed.
4. Hetrogenious objects are allowed.
5. Null allowed.
6. every methods in Vector are synchronized and hennce Vector is Thread safe
7. public class Vector<E>   extends AbstractList<E>  implements List<E>, RandomAccess, Cloneable, Serializable
8. Vector v=new Vector() create empty vector Object with default initial capacity 10 once vector reaches it is MAX capacity
	then a new Vector objects will be created with the new_capacity = current_capacity * 2;
9.	:Constructoe
	 	public Vector()
  		public Vector(int capacity)
  		public Vector(int initialCapacity, incrementalCapicity)
  		public Vector(Collection<? extends E> paramCollection){
  			this Constructoe is for intercommunication between collection objects.
  		}
10. Specific methods

	add(Object obj)  Collection
	add(int index, Object obj) List
	void addElement(Object obj); Vector

	remove(Object o) Collection
	void removeElement(Object obj); Vector
	void remove(int index, Object obj); List
	void addElementAt(int index); Vector
	clear() collection
	removeAllElement() Vector

	Object get(int index) List
	Object elementAt(int index) Vector
	Object firstElement(); Vector
	Object lastEmement() Vector

	int size()
	int capacity()
	Enumeration elements();

7. Most of the methods are synchronized.
8. Thread Safe
9. Relatively performence is low.
10. it is Legacy class, because it introduces in java 1.0