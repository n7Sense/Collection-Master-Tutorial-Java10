
LinkedList
================

1. underline data-structure is Double LinkedList or doubly LinkedList
2. Insertion order preserved.
3. allowed duplicate.
4. Hetrogenious object allowed.
5. Null insertion is allowed.
6. extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable
7. if our frequent operation is Insertion and deletion then LinkedList is the best choice.
8. LinkedList is worst choice if our frequent operation is Retrival


Q.1. Write a program Array based implementation with Stack and Queue.
	it is easy but performence wise bad.

Q.2. Write a program LinkedList based implementation with Stack (LIFO) and Queue (FIFO).
	 usually we can LinkedList to develop Stack and Queue to provide support for this requirement
	 LinkedList class the define the following specific methods.
	 LinkedList commoenly use with Stack and Queue, because it specific methods.
	 1. void addFirst();
	 2. void addLast();
	 3. Object getFirst();
	 4. Object getLast();
	 5. Object removeFirst();
	 3. Object removeLast();