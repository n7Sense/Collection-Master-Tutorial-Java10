public interface List<E> extends Collection<E> {

	void add(int index,Object element)	It is used to insert element into the invoking list at the index passed in the index.

	boolean addAll(int index,Collection c)	It is used to insert all elements of c into the invoking list at the index passed in the index.

	object get(int index)	It is used to return the object stored at the specified index within the invoking collection.

	object set(int index,Object element)	It is used to assign element to the location specified by index within the invoking list.

	object remove(int index)	It is used to remove the element at position index from the invoking list and return the deleted element.

	ListIterator listIterator()	It is used to return an iterator to the start of the invoking list.

	ListIterator listIterator(int index)	It is used to return an iterator to the invoking list that begins at the specified index.

	public abstract List<E> subList(int paramInt1, int paramInt2);
  
  	public Spliterator<E> spliterator()

  	public abstract void clear();
  
  	public abstract boolean equals(Object paramObject);
  
  	public abstract int hashCode();
}