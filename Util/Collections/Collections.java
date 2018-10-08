package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Collections
{
  private static final int BINARYSEARCH_THRESHOLD = 5000;
  private static final int REVERSE_THRESHOLD = 18;
  private static final int SHUFFLE_THRESHOLD = 5;
  private static final int FILL_THRESHOLD = 25;
  private static final int ROTATE_THRESHOLD = 100;
  private static final int COPY_THRESHOLD = 10;
  private static final int REPLACEALL_THRESHOLD = 11;
  private static final int INDEXOFSUBLIST_THRESHOLD = 35;
  private static Random r;
  
  public static <T extends Comparable<? super T>> void sort(List<T> list)
  {
    list.sort(null);
  }
  
  public static <T> void sort(List<T> list, Comparator<? super T> c)
  {
    list.sort(c);
  }
  
  public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key)
  {
    if (((list instanceof RandomAccess)) || (list.size() < 5000)) {
      return indexedBinarySearch(list, key);
    }
    return iteratorBinarySearch(list, key);
  }
  
  private static <T> int indexedBinarySearch(List<? extends Comparable<? super T>> list, T key)
  {
    int low = 0;
    int high = list.size() - 1;
    while (low <= high)
    {
      int mid = low + high >>> 1;
      Comparable<? super T> midVal = (Comparable)list.get(mid);
      int cmp = midVal.compareTo(key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return -(low + 1);
  }
  
  private static <T> int iteratorBinarySearch(List<? extends Comparable<? super T>> list, T key)
  {
    int low = 0;
    int high = list.size() - 1;
    ListIterator<? extends Comparable<? super T>> i = list.listIterator();
    while (low <= high)
    {
      int mid = low + high >>> 1;
      Comparable<? super T> midVal = (Comparable)get(i, mid);
      int cmp = midVal.compareTo(key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return -(low + 1);
  }
  
  private static <T> T get(ListIterator<? extends T> i, int index)
  {
    T obj = null;
    int pos = i.nextIndex();
    if (pos <= index) {
      do
      {
        obj = i.next();
      } while (pos++ < index);
    } else {
      do
      {
        obj = i.previous();
        pos--;
      } while (pos > index);
    }
    return obj;
  }
  
  public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c)
  {
    if (c == null) {
      return binarySearch(list, key);
    }
    if (((list instanceof RandomAccess)) || (list.size() < 5000)) {
      return indexedBinarySearch(list, key, c);
    }
    return iteratorBinarySearch(list, key, c);
  }
  
  private static <T> int indexedBinarySearch(List<? extends T> l, T key, Comparator<? super T> c)
  {
    int low = 0;
    int high = l.size() - 1;
    while (low <= high)
    {
      int mid = low + high >>> 1;
      T midVal = l.get(mid);
      int cmp = c.compare(midVal, key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return -(low + 1);
  }
  
  private static <T> int iteratorBinarySearch(List<? extends T> l, T key, Comparator<? super T> c)
  {
    int low = 0;
    int high = l.size() - 1;
    ListIterator<? extends T> i = l.listIterator();
    while (low <= high)
    {
      int mid = low + high >>> 1;
      T midVal = get(i, mid);
      int cmp = c.compare(midVal, key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return -(low + 1);
  }
  
  public static void reverse(List<?> list)
  {
    int size = list.size();
    if ((size < 18) || ((list instanceof RandomAccess)))
    {
      int i = 0;int mid = size >> 1;
      for (int j = size - 1; i < mid; j--)
      {
        swap(list, i, j);i++;
      }
    }
    else
    {
      ListIterator fwd = list.listIterator();
      ListIterator rev = list.listIterator(size);
      int i = 0;
      for (int mid = list.size() >> 1; i < mid; i++)
      {
        Object tmp = fwd.next();
        fwd.set(rev.previous());
        rev.set(tmp);
      }
    }
  }
  
  public static void shuffle(List<?> list)
  {
    Random rnd = r;
    if (rnd == null) {
      r = rnd = new Random();
    }
    shuffle(list, rnd);
  }
  
  public static void shuffle(List<?> list, Random rnd)
  {
    int size = list.size();
    if ((size < 5) || ((list instanceof RandomAccess)))
    {
      for (int i = size; i > 1; i--) {
        swap(list, i - 1, rnd.nextInt(i));
      }
    }
    else
    {
      Object[] arr = list.toArray();
      for (int i = size; i > 1; i--) {
        swap(arr, i - 1, rnd.nextInt(i));
      }
      ListIterator it = list.listIterator();
      for (Object e : arr)
      {
        it.next();
        it.set(e);
      }
    }
  }
  
  public static void swap(List<?> list, int i, int j)
  {
    List l = list;
    l.set(i, l.set(j, l.get(i)));
  }
  
  private static void swap(Object[] arr, int i, int j)
  {
    Object tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }
  
  public static <T> void fill(List<? super T> list, T obj)
  {
    int size = list.size();
    if ((size < 25) || ((list instanceof RandomAccess)))
    {
      for (int i = 0; i < size; i++) {
        list.set(i, obj);
      }
    }
    else
    {
      ListIterator<? super T> itr = list.listIterator();
      for (int i = 0; i < size; i++)
      {
        itr.next();
        itr.set(obj);
      }
    }
  }
  
  public static <T> void copy(List<? super T> dest, List<? extends T> src)
  {
    int srcSize = src.size();
    if (srcSize > dest.size()) {
      throw new IndexOutOfBoundsException("Source does not fit in dest");
    }
    if ((srcSize < 10) || (((src instanceof RandomAccess)) && ((dest instanceof RandomAccess))))
    {
      for (int i = 0; i < srcSize; i++) {
        dest.set(i, src.get(i));
      }
    }
    else
    {
      ListIterator<? super T> di = dest.listIterator();
      ListIterator<? extends T> si = src.listIterator();
      for (int i = 0; i < srcSize; i++)
      {
        di.next();
        di.set(si.next());
      }
    }
  }
  
  public static <T,  extends Comparable<? super T>> T min(Collection<? extends T> coll)
  {
    Iterator<? extends T> i = coll.iterator();
    T candidate = i.next();
    while (i.hasNext())
    {
      T next = i.next();
      if (((Comparable)next).compareTo(candidate) < 0) {
        candidate = next;
      }
    }
    return candidate;
  }
  
  public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp)
  {
    if (comp == null) {
      return min(coll);
    }
    Iterator<? extends T> i = coll.iterator();
    T candidate = i.next();
    while (i.hasNext())
    {
      T next = i.next();
      if (comp.compare(next, candidate) < 0) {
        candidate = next;
      }
    }
    return candidate;
  }
  
  public static <T,  extends Comparable<? super T>> T max(Collection<? extends T> coll)
  {
    Iterator<? extends T> i = coll.iterator();
    T candidate = i.next();
    while (i.hasNext())
    {
      T next = i.next();
      if (((Comparable)next).compareTo(candidate) > 0) {
        candidate = next;
      }
    }
    return candidate;
  }
  
  public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp)
  {
    if (comp == null) {
      return max(coll);
    }
    Iterator<? extends T> i = coll.iterator();
    T candidate = i.next();
    while (i.hasNext())
    {
      T next = i.next();
      if (comp.compare(next, candidate) > 0) {
        candidate = next;
      }
    }
    return candidate;
  }
  
  public static void rotate(List<?> list, int distance)
  {
    if (((list instanceof RandomAccess)) || (list.size() < 100)) {
      rotate1(list, distance);
    } else {
      rotate2(list, distance);
    }
  }
  
  private static <T> void rotate1(List<T> list, int distance)
  {
    int size = list.size();
    if (size == 0) {
      return;
    }
    distance %= size;
    if (distance < 0) {
      distance += size;
    }
    if (distance == 0) {
      return;
    }
    int cycleStart = 0;
    for (int nMoved = 0; nMoved != size; cycleStart++)
    {
      T displaced = list.get(cycleStart);
      int i = cycleStart;
      do
      {
        i += distance;
        if (i >= size) {
          i -= size;
        }
        displaced = list.set(i, displaced);
        nMoved++;
      } while (i != cycleStart);
    }
  }
  
  private static void rotate2(List<?> list, int distance)
  {
    int size = list.size();
    if (size == 0) {
      return;
    }
    int mid = -distance % size;
    if (mid < 0) {
      mid += size;
    }
    if (mid == 0) {
      return;
    }
    reverse(list.subList(0, mid));
    reverse(list.subList(mid, size));
    reverse(list);
  }
  
  public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal)
  {
    boolean result = false;
    int size = list.size();
    if ((size < 11) || ((list instanceof RandomAccess)))
    {
      if (oldVal == null) {
        for (int i = 0; i < size; i++) {
          if (list.get(i) == null)
          {
            list.set(i, newVal);
            result = true;
          }
        }
      } else {
        for (int i = 0; i < size; i++) {
          if (oldVal.equals(list.get(i)))
          {
            list.set(i, newVal);
            result = true;
          }
        }
      }
    }
    else
    {
      ListIterator<T> itr = list.listIterator();
      if (oldVal == null) {
        for (int i = 0; i < size; i++) {
          if (itr.next() == null)
          {
            itr.set(newVal);
            result = true;
          }
        }
      } else {
        for (int i = 0; i < size; i++) {
          if (oldVal.equals(itr.next()))
          {
            itr.set(newVal);
            result = true;
          }
        }
      }
    }
    return result;
  }
  
  public static int indexOfSubList(List<?> source, List<?> target)
  {
    int sourceSize = source.size();
    int targetSize = target.size();
    int maxCandidate = sourceSize - targetSize;
    if ((sourceSize < 35) || (((source instanceof RandomAccess)) && ((target instanceof RandomAccess))))
    {
      label99:
      for (int candidate = 0; candidate <= maxCandidate; candidate++)
      {
        int i = 0;
        for (int j = candidate; i < targetSize; j++)
        {
          if (!eq(target.get(i), source.get(j))) {
            break label99;
          }
          i++;
        }
        return candidate;
      }
    }
    else
    {
      ListIterator<?> si = source.listIterator();
      label199:
      for (int candidate = 0; candidate <= maxCandidate; candidate++)
      {
        ListIterator<?> ti = target.listIterator();
        for (int i = 0; i < targetSize; i++) {
          if (!eq(ti.next(), si.next()))
          {
            for (int j = 0; j < i; j++) {
              si.previous();
            }
            break label199;
          }
        }
        return candidate;
      }
    }
    return -1;
  }
  
  public static int lastIndexOfSubList(List<?> source, List<?> target)
  {
    int sourceSize = source.size();
    int targetSize = target.size();
    int maxCandidate = sourceSize - targetSize;
    if ((sourceSize < 35) || ((source instanceof RandomAccess)))
    {
      label91:
      for (int candidate = maxCandidate; candidate >= 0; candidate--)
      {
        int i = 0;
        for (int j = candidate; i < targetSize; j++)
        {
          if (!eq(target.get(i), source.get(j))) {
            break label91;
          }
          i++;
        }
        return candidate;
      }
    }
    else
    {
      if (maxCandidate < 0) {
        return -1;
      }
      ListIterator<?> si = source.listIterator(maxCandidate);
      label206:
      for (int candidate = maxCandidate; candidate >= 0; candidate--)
      {
        ListIterator<?> ti = target.listIterator();
        for (int i = 0; i < targetSize; i++) {
          if (!eq(ti.next(), si.next()))
          {
            if (candidate == 0) {
              break label206;
            }
            for (int j = 0; j <= i + 1; j++) {
              si.previous();
            }
            break label206;
          }
        }
        return candidate;
      }
    }
    return -1;
  }
  
  public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c)
  {
    return new UnmodifiableCollection(c);
  }
  
  static class UnmodifiableCollection<E>
    implements Collection<E>, Serializable
  {
    private static final long serialVersionUID = 1820017752578914078L;
    final Collection<? extends E> c;
    
    UnmodifiableCollection(Collection<? extends E> c)
    {
      if (c == null) {
        throw new NullPointerException();
      }
      this.c = c;
    }
    
    public int size()
    {
      return this.c.size();
    }
    
    public boolean isEmpty()
    {
      return this.c.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return this.c.contains(o);
    }
    
    public Object[] toArray()
    {
      return this.c.toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return this.c.toArray(a);
    }
    
    public String toString()
    {
      return this.c.toString();
    }
    
    public Iterator<E> iterator()
    {
      new Iterator()
      {
        private final Iterator<? extends E> i = Collections.UnmodifiableCollection.this.c.iterator();
        
        public boolean hasNext()
        {
          return this.i.hasNext();
        }
        
        public E next()
        {
          return this.i.next();
        }
        
        public void remove()
        {
          throw new UnsupportedOperationException();
        }
        
        public void forEachRemaining(Consumer<? super E> action)
        {
          this.i.forEachRemaining(action);
        }
      };
    }
    
    public boolean add(E e)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object o)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean containsAll(Collection<?> coll)
    {
      return this.c.containsAll(coll);
    }
    
    public boolean addAll(Collection<? extends E> coll)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> coll)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> coll)
    {
      throw new UnsupportedOperationException();
    }
    
    public void clear()
    {
      throw new UnsupportedOperationException();
    }
    
    public void forEach(Consumer<? super E> action)
    {
      this.c.forEach(action);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      throw new UnsupportedOperationException();
    }
    
    public Spliterator<E> spliterator()
    {
      return this.c.spliterator();
    }
    
    public Stream<E> stream()
    {
      return this.c.stream();
    }
    
    public Stream<E> parallelStream()
    {
      return this.c.parallelStream();
    }
  }
  
  public static <T> Set<T> unmodifiableSet(Set<? extends T> s)
  {
    return new UnmodifiableSet(s);
  }
  
  static class UnmodifiableSet<E>
    extends Collections.UnmodifiableCollection<E>
    implements Set<E>, Serializable
  {
    private static final long serialVersionUID = -9215047833775013803L;
    
    UnmodifiableSet(Set<? extends E> s)
    {
      super();
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.c.equals(o));
    }
    
    public int hashCode()
    {
      return this.c.hashCode();
    }
  }
  
  public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> s)
  {
    return new UnmodifiableSortedSet(s);
  }
  
  static class UnmodifiableSortedSet<E>
    extends Collections.UnmodifiableSet<E>
    implements SortedSet<E>, Serializable
  {
    private static final long serialVersionUID = -4929149591599911165L;
    private final SortedSet<E> ss;
    
    UnmodifiableSortedSet(SortedSet<E> s)
    {
      super();this.ss = s;
    }
    
    public Comparator<? super E> comparator()
    {
      return this.ss.comparator();
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement)
    {
      return new UnmodifiableSortedSet(this.ss.subSet(fromElement, toElement));
    }
    
    public SortedSet<E> headSet(E toElement)
    {
      return new UnmodifiableSortedSet(this.ss.headSet(toElement));
    }
    
    public SortedSet<E> tailSet(E fromElement)
    {
      return new UnmodifiableSortedSet(this.ss.tailSet(fromElement));
    }
    
    public E first()
    {
      return this.ss.first();
    }
    
    public E last()
    {
      return this.ss.last();
    }
  }
  
  public static <T> NavigableSet<T> unmodifiableNavigableSet(NavigableSet<T> s)
  {
    return new UnmodifiableNavigableSet(s);
  }
  
  static class UnmodifiableNavigableSet<E>
    extends Collections.UnmodifiableSortedSet<E>
    implements NavigableSet<E>, Serializable
  {
    private static final long serialVersionUID = -6027448201786391929L;
    
    private static class EmptyNavigableSet<E>
      extends Collections.UnmodifiableNavigableSet<E>
      implements Serializable
    {
      private static final long serialVersionUID = -6291252904449939134L;
      
      public EmptyNavigableSet()
      {
        super();
      }
      
      private Object readResolve()
      {
        return Collections.UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
      }
    }
    
    private static final NavigableSet<?> EMPTY_NAVIGABLE_SET = new EmptyNavigableSet();
    private final NavigableSet<E> ns;
    
    UnmodifiableNavigableSet(NavigableSet<E> s)
    {
      super();this.ns = s;
    }
    
    public E lower(E e)
    {
      return this.ns.lower(e);
    }
    
    public E floor(E e)
    {
      return this.ns.floor(e);
    }
    
    public E ceiling(E e)
    {
      return this.ns.ceiling(e);
    }
    
    public E higher(E e)
    {
      return this.ns.higher(e);
    }
    
    public E pollFirst()
    {
      throw new UnsupportedOperationException();
    }
    
    public E pollLast()
    {
      throw new UnsupportedOperationException();
    }
    
    public NavigableSet<E> descendingSet()
    {
      return new UnmodifiableNavigableSet(this.ns.descendingSet());
    }
    
    public Iterator<E> descendingIterator()
    {
      return descendingSet().iterator();
    }
    
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
    {
      return new UnmodifiableNavigableSet(this.ns
        .subSet(fromElement, fromInclusive, toElement, toInclusive));
    }
    
    public NavigableSet<E> headSet(E toElement, boolean inclusive)
    {
      return new UnmodifiableNavigableSet(this.ns
        .headSet(toElement, inclusive));
    }
    
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
    {
      return new UnmodifiableNavigableSet(this.ns
        .tailSet(fromElement, inclusive));
    }
  }
  
  public static <T> List<T> unmodifiableList(List<? extends T> list)
  {
    return (list instanceof RandomAccess) ? 
      new UnmodifiableRandomAccessList(list) : 
      new UnmodifiableList(list);
  }
  
  static class UnmodifiableList<E>
    extends Collections.UnmodifiableCollection<E>
    implements List<E>
  {
    private static final long serialVersionUID = -283967356065247728L;
    final List<? extends E> list;
    
    UnmodifiableList(List<? extends E> list)
    {
      super();
      this.list = list;
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.list.equals(o));
    }
    
    public int hashCode()
    {
      return this.list.hashCode();
    }
    
    public E get(int index)
    {
      return this.list.get(index);
    }
    
    public E set(int index, E element)
    {
      throw new UnsupportedOperationException();
    }
    
    public void add(int index, E element)
    {
      throw new UnsupportedOperationException();
    }
    
    public E remove(int index)
    {
      throw new UnsupportedOperationException();
    }
    
    public int indexOf(Object o)
    {
      return this.list.indexOf(o);
    }
    
    public int lastIndexOf(Object o)
    {
      return this.list.lastIndexOf(o);
    }
    
    public boolean addAll(int index, Collection<? extends E> c)
    {
      throw new UnsupportedOperationException();
    }
    
    public void replaceAll(UnaryOperator<E> operator)
    {
      throw new UnsupportedOperationException();
    }
    
    public void sort(Comparator<? super E> c)
    {
      throw new UnsupportedOperationException();
    }
    
    public ListIterator<E> listIterator()
    {
      return listIterator(0);
    }
    
    public ListIterator<E> listIterator(final int index)
    {
      new ListIterator()
      {
        private final ListIterator<? extends E> i = Collections.UnmodifiableList.this.list
          .listIterator(index);
        
        public boolean hasNext()
        {
          return this.i.hasNext();
        }
        
        public E next()
        {
          return this.i.next();
        }
        
        public boolean hasPrevious()
        {
          return this.i.hasPrevious();
        }
        
        public E previous()
        {
          return this.i.previous();
        }
        
        public int nextIndex()
        {
          return this.i.nextIndex();
        }
        
        public int previousIndex()
        {
          return this.i.previousIndex();
        }
        
        public void remove()
        {
          throw new UnsupportedOperationException();
        }
        
        public void set(E e)
        {
          throw new UnsupportedOperationException();
        }
        
        public void add(E e)
        {
          throw new UnsupportedOperationException();
        }
        
        public void forEachRemaining(Consumer<? super E> action)
        {
          this.i.forEachRemaining(action);
        }
      };
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      return new UnmodifiableList(this.list.subList(fromIndex, toIndex));
    }
    
    private Object readResolve()
    {
      return (this.list instanceof RandomAccess) ? 
        new Collections.UnmodifiableRandomAccessList(this.list) : 
        this;
    }
  }
  
  static class UnmodifiableRandomAccessList<E>
    extends Collections.UnmodifiableList<E>
    implements RandomAccess
  {
    private static final long serialVersionUID = -2542308836966382001L;
    
    UnmodifiableRandomAccessList(List<? extends E> list)
    {
      super();
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      return new UnmodifiableRandomAccessList(this.list
        .subList(fromIndex, toIndex));
    }
    
    private Object writeReplace()
    {
      return new Collections.UnmodifiableList(this.list);
    }
  }
  
  public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m)
  {
    return new UnmodifiableMap(m);
  }
  
  private static class UnmodifiableMap<K, V>
    implements Map<K, V>, Serializable
  {
    private static final long serialVersionUID = -1034234728574286014L;
    private final Map<? extends K, ? extends V> m;
    private transient Set<K> keySet;
    private transient Set<Map.Entry<K, V>> entrySet;
    private transient Collection<V> values;
    
    UnmodifiableMap(Map<? extends K, ? extends V> m)
    {
      if (m == null) {
        throw new NullPointerException();
      }
      this.m = m;
    }
    
    public int size()
    {
      return this.m.size();
    }
    
    public boolean isEmpty()
    {
      return this.m.isEmpty();
    }
    
    public boolean containsKey(Object key)
    {
      return this.m.containsKey(key);
    }
    
    public boolean containsValue(Object val)
    {
      return this.m.containsValue(val);
    }
    
    public V get(Object key)
    {
      return this.m.get(key);
    }
    
    public V put(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public V remove(Object key)
    {
      throw new UnsupportedOperationException();
    }
    
    public void putAll(Map<? extends K, ? extends V> m)
    {
      throw new UnsupportedOperationException();
    }
    
    public void clear()
    {
      throw new UnsupportedOperationException();
    }
    
    public Set<K> keySet()
    {
      if (this.keySet == null) {
        this.keySet = Collections.unmodifiableSet(this.m.keySet());
      }
      return this.keySet;
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      if (this.entrySet == null) {
        this.entrySet = new Collections.UnmodifiableMap.UnmodifiableEntrySet(this.m.entrySet());
      }
      return this.entrySet;
    }
    
    public Collection<V> values()
    {
      if (this.values == null) {
        this.values = Collections.unmodifiableCollection(this.m.values());
      }
      return this.values;
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.m.equals(o));
    }
    
    public int hashCode()
    {
      return this.m.hashCode();
    }
    
    public String toString()
    {
      return this.m.toString();
    }
    
    public V getOrDefault(Object k, V defaultValue)
    {
      return this.m.getOrDefault(k, defaultValue);
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action)
    {
      this.m.forEach(action);
    }
    
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
    {
      throw new UnsupportedOperationException();
    }
    
    public V putIfAbsent(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object key, Object value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean replace(K key, V oldValue, V newValue)
    {
      throw new UnsupportedOperationException();
    }
    
    public V replace(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m)
  {
    return new UnmodifiableSortedMap(m);
  }
  
  static class UnmodifiableSortedMap<K, V>
    extends Collections.UnmodifiableMap<K, V>
    implements SortedMap<K, V>, Serializable
  {
    private static final long serialVersionUID = -8806743815996713206L;
    private final SortedMap<K, ? extends V> sm;
    
    UnmodifiableSortedMap(SortedMap<K, ? extends V> m)
    {
      super();this.sm = m;
    }
    
    public Comparator<? super K> comparator()
    {
      return this.sm.comparator();
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      return new UnmodifiableSortedMap(this.sm.subMap(fromKey, toKey));
    }
    
    public SortedMap<K, V> headMap(K toKey)
    {
      return new UnmodifiableSortedMap(this.sm.headMap(toKey));
    }
    
    public SortedMap<K, V> tailMap(K fromKey)
    {
      return new UnmodifiableSortedMap(this.sm.tailMap(fromKey));
    }
    
    public K firstKey()
    {
      return this.sm.firstKey();
    }
    
    public K lastKey()
    {
      return this.sm.lastKey();
    }
  }
  
  public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> m)
  {
    return new UnmodifiableNavigableMap(m);
  }
  
  static class UnmodifiableNavigableMap<K, V>
    extends Collections.UnmodifiableSortedMap<K, V>
    implements NavigableMap<K, V>, Serializable
  {
    private static final long serialVersionUID = -4858195264774772197L;
    
    private static class EmptyNavigableMap<K, V>
      extends Collections.UnmodifiableNavigableMap<K, V>
      implements Serializable
    {
      private static final long serialVersionUID = -2239321462712562324L;
      
      EmptyNavigableMap()
      {
        super();
      }
      
      public NavigableSet<K> navigableKeySet()
      {
        return Collections.emptyNavigableSet();
      }
      
      private Object readResolve()
      {
        return Collections.UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
      }
    }
    
    private static final EmptyNavigableMap<?, ?> EMPTY_NAVIGABLE_MAP = new EmptyNavigableMap();
    private final NavigableMap<K, ? extends V> nm;
    
    UnmodifiableNavigableMap(NavigableMap<K, ? extends V> m)
    {
      super();this.nm = m;
    }
    
    public K lowerKey(K key)
    {
      return this.nm.lowerKey(key);
    }
    
    public K floorKey(K key)
    {
      return this.nm.floorKey(key);
    }
    
    public K ceilingKey(K key)
    {
      return this.nm.ceilingKey(key);
    }
    
    public K higherKey(K key)
    {
      return this.nm.higherKey(key);
    }
    
    public Map.Entry<K, V> lowerEntry(K key)
    {
      Map.Entry<K, V> lower = this.nm.lowerEntry(key);
      return null != lower ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(lower) : 
        null;
    }
    
    public Map.Entry<K, V> floorEntry(K key)
    {
      Map.Entry<K, V> floor = this.nm.floorEntry(key);
      return null != floor ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(floor) : 
        null;
    }
    
    public Map.Entry<K, V> ceilingEntry(K key)
    {
      Map.Entry<K, V> ceiling = this.nm.ceilingEntry(key);
      return null != ceiling ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(ceiling) : 
        null;
    }
    
    public Map.Entry<K, V> higherEntry(K key)
    {
      Map.Entry<K, V> higher = this.nm.higherEntry(key);
      return null != higher ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(higher) : 
        null;
    }
    
    public Map.Entry<K, V> firstEntry()
    {
      Map.Entry<K, V> first = this.nm.firstEntry();
      return null != first ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(first) : 
        null;
    }
    
    public Map.Entry<K, V> lastEntry()
    {
      Map.Entry<K, V> last = this.nm.lastEntry();
      return null != last ? 
        new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(last) : 
        null;
    }
    
    public Map.Entry<K, V> pollFirstEntry()
    {
      throw new UnsupportedOperationException();
    }
    
    public Map.Entry<K, V> pollLastEntry()
    {
      throw new UnsupportedOperationException();
    }
    
    public NavigableMap<K, V> descendingMap()
    {
      return Collections.unmodifiableNavigableMap(this.nm.descendingMap());
    }
    
    public NavigableSet<K> navigableKeySet()
    {
      return Collections.unmodifiableNavigableSet(this.nm.navigableKeySet());
    }
    
    public NavigableSet<K> descendingKeySet()
    {
      return Collections.unmodifiableNavigableSet(this.nm.descendingKeySet());
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
      return Collections.unmodifiableNavigableMap(this.nm
        .subMap(fromKey, fromInclusive, toKey, toInclusive));
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
    {
      return Collections.unmodifiableNavigableMap(this.nm.headMap(toKey, inclusive));
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
    {
      return Collections.unmodifiableNavigableMap(this.nm.tailMap(fromKey, inclusive));
    }
  }
  
  public static <T> Collection<T> synchronizedCollection(Collection<T> c)
  {
    return new SynchronizedCollection(c);
  }
  
  static <T> Collection<T> synchronizedCollection(Collection<T> c, Object mutex)
  {
    return new SynchronizedCollection(c, mutex);
  }
  
  static class SynchronizedCollection<E>
    implements Collection<E>, Serializable
  {
    private static final long serialVersionUID = 3053995032091335093L;
    final Collection<E> c;
    final Object mutex;
    
    SynchronizedCollection(Collection<E> c)
    {
      this.c = ((Collection)Objects.requireNonNull(c));
      this.mutex = this;
    }
    
    SynchronizedCollection(Collection<E> c, Object mutex)
    {
      this.c = ((Collection)Objects.requireNonNull(c));
      this.mutex = Objects.requireNonNull(mutex);
    }
    
    public int size()
    {
      synchronized (this.mutex)
      {
        return this.c.size();
      }
    }
    
    public boolean isEmpty()
    {
      synchronized (this.mutex)
      {
        return this.c.isEmpty();
      }
    }
    
    public boolean contains(Object o)
    {
      synchronized (this.mutex)
      {
        return this.c.contains(o);
      }
    }
    
    public Object[] toArray()
    {
      synchronized (this.mutex)
      {
        return this.c.toArray();
      }
    }
    
    public <T> T[] toArray(T[] a)
    {
      synchronized (this.mutex)
      {
        return this.c.toArray(a);
      }
    }
    
    public Iterator<E> iterator()
    {
      return this.c.iterator();
    }
    
    public boolean add(E e)
    {
      synchronized (this.mutex)
      {
        return this.c.add(e);
      }
    }
    
    public boolean remove(Object o)
    {
      synchronized (this.mutex)
      {
        return this.c.remove(o);
      }
    }
    
    public boolean containsAll(Collection<?> coll)
    {
      synchronized (this.mutex)
      {
        return this.c.containsAll(coll);
      }
    }
    
    public boolean addAll(Collection<? extends E> coll)
    {
      synchronized (this.mutex)
      {
        return this.c.addAll(coll);
      }
    }
    
    public boolean removeAll(Collection<?> coll)
    {
      synchronized (this.mutex)
      {
        return this.c.removeAll(coll);
      }
    }
    
    public boolean retainAll(Collection<?> coll)
    {
      synchronized (this.mutex)
      {
        return this.c.retainAll(coll);
      }
    }
    
    public void clear()
    {
      synchronized (this.mutex)
      {
        this.c.clear();
      }
    }
    
    public String toString()
    {
      synchronized (this.mutex)
      {
        return this.c.toString();
      }
    }
    
    public void forEach(Consumer<? super E> consumer)
    {
      synchronized (this.mutex)
      {
        this.c.forEach(consumer);
      }
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      synchronized (this.mutex)
      {
        return this.c.removeIf(filter);
      }
    }
    
    public Spliterator<E> spliterator()
    {
      return this.c.spliterator();
    }
    
    public Stream<E> stream()
    {
      return this.c.stream();
    }
    
    public Stream<E> parallelStream()
    {
      return this.c.parallelStream();
    }
    
    private void writeObject(ObjectOutputStream s)
      throws IOException
    {
      synchronized (this.mutex)
      {
        s.defaultWriteObject();
      }
    }
  }
  
  public static <T> Set<T> synchronizedSet(Set<T> s)
  {
    return new SynchronizedSet(s);
  }
  
  static <T> Set<T> synchronizedSet(Set<T> s, Object mutex)
  {
    return new SynchronizedSet(s, mutex);
  }
  
  static class SynchronizedSet<E>
    extends Collections.SynchronizedCollection<E>
    implements Set<E>
  {
    private static final long serialVersionUID = 487447009682186044L;
    
    SynchronizedSet(Set<E> s)
    {
      super();
    }
    
    SynchronizedSet(Set<E> s, Object mutex)
    {
      super(mutex);
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      synchronized (this.mutex)
      {
        return this.c.equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (this.mutex)
      {
        return this.c.hashCode();
      }
    }
  }
  
  public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s)
  {
    return new SynchronizedSortedSet(s);
  }
  
  static class SynchronizedSortedSet<E>
    extends Collections.SynchronizedSet<E>
    implements SortedSet<E>
  {
    private static final long serialVersionUID = 8695801310862127406L;
    private final SortedSet<E> ss;
    
    SynchronizedSortedSet(SortedSet<E> s)
    {
      super();
      this.ss = s;
    }
    
    SynchronizedSortedSet(SortedSet<E> s, Object mutex)
    {
      super(mutex);
      this.ss = s;
    }
    
    public Comparator<? super E> comparator()
    {
      synchronized (this.mutex)
      {
        return this.ss.comparator();
      }
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedSet(this.ss
          .subSet(fromElement, toElement), this.mutex);
      }
    }
    
    public SortedSet<E> headSet(E toElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedSet(this.ss.headSet(toElement), this.mutex);
      }
    }
    
    public SortedSet<E> tailSet(E fromElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedSet(this.ss.tailSet(fromElement), this.mutex);
      }
    }
    
    public E first()
    {
      synchronized (this.mutex)
      {
        return this.ss.first();
      }
    }
    
    public E last()
    {
      synchronized (this.mutex)
      {
        return this.ss.last();
      }
    }
  }
  
  public static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> s)
  {
    return new SynchronizedNavigableSet(s);
  }
  
  static class SynchronizedNavigableSet<E>
    extends Collections.SynchronizedSortedSet<E>
    implements NavigableSet<E>
  {
    private static final long serialVersionUID = -5505529816273629798L;
    private final NavigableSet<E> ns;
    
    SynchronizedNavigableSet(NavigableSet<E> s)
    {
      super();
      this.ns = s;
    }
    
    SynchronizedNavigableSet(NavigableSet<E> s, Object mutex)
    {
      super(mutex);
      this.ns = s;
    }
    
    public E lower(E e)
    {
      synchronized (this.mutex)
      {
        return this.ns.lower(e);
      }
    }
    
    public E floor(E e)
    {
      synchronized (this.mutex)
      {
        return this.ns.floor(e);
      }
    }
    
    public E ceiling(E e)
    {
      synchronized (this.mutex)
      {
        return this.ns.ceiling(e);
      }
    }
    
    public E higher(E e)
    {
      synchronized (this.mutex)
      {
        return this.ns.higher(e);
      }
    }
    
    public E pollFirst()
    {
      synchronized (this.mutex)
      {
        return this.ns.pollFirst();
      }
    }
    
    public E pollLast()
    {
      synchronized (this.mutex)
      {
        return this.ns.pollLast();
      }
    }
    
    public NavigableSet<E> descendingSet()
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.descendingSet(), this.mutex);
      }
    }
    
    public Iterator<E> descendingIterator()
    {
      synchronized (this.mutex)
      {
        return descendingSet().iterator();
      }
    }
    
    public NavigableSet<E> subSet(E fromElement, E toElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.subSet(fromElement, true, toElement, false), this.mutex);
      }
    }
    
    public NavigableSet<E> headSet(E toElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.headSet(toElement, false), this.mutex);
      }
    }
    
    public NavigableSet<E> tailSet(E fromElement)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.tailSet(fromElement, true), this.mutex);
      }
    }
    
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
      }
    }
    
    public NavigableSet<E> headSet(E toElement, boolean inclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.headSet(toElement, inclusive), this.mutex);
      }
    }
    
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableSet(this.ns.tailSet(fromElement, inclusive), this.mutex);
      }
    }
  }
  
  public static <T> List<T> synchronizedList(List<T> list)
  {
    return (list instanceof RandomAccess) ? 
      new SynchronizedRandomAccessList(list) : 
      new SynchronizedList(list);
  }
  
  static <T> List<T> synchronizedList(List<T> list, Object mutex)
  {
    return (list instanceof RandomAccess) ? 
      new SynchronizedRandomAccessList(list, mutex) : 
      new SynchronizedList(list, mutex);
  }
  
  static class SynchronizedList<E>
    extends Collections.SynchronizedCollection<E>
    implements List<E>
  {
    private static final long serialVersionUID = -7754090372962971524L;
    final List<E> list;
    
    SynchronizedList(List<E> list)
    {
      super();
      this.list = list;
    }
    
    SynchronizedList(List<E> list, Object mutex)
    {
      super(mutex);
      this.list = list;
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      synchronized (this.mutex)
      {
        return this.list.equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (this.mutex)
      {
        return this.list.hashCode();
      }
    }
    
    public E get(int index)
    {
      synchronized (this.mutex)
      {
        return this.list.get(index);
      }
    }
    
    public E set(int index, E element)
    {
      synchronized (this.mutex)
      {
        return this.list.set(index, element);
      }
    }
    
    public void add(int index, E element)
    {
      synchronized (this.mutex)
      {
        this.list.add(index, element);
      }
    }
    
    public E remove(int index)
    {
      synchronized (this.mutex)
      {
        return this.list.remove(index);
      }
    }
    
    public int indexOf(Object o)
    {
      synchronized (this.mutex)
      {
        return this.list.indexOf(o);
      }
    }
    
    public int lastIndexOf(Object o)
    {
      synchronized (this.mutex)
      {
        return this.list.lastIndexOf(o);
      }
    }
    
    public boolean addAll(int index, Collection<? extends E> c)
    {
      synchronized (this.mutex)
      {
        return this.list.addAll(index, c);
      }
    }
    
    public ListIterator<E> listIterator()
    {
      return this.list.listIterator();
    }
    
    public ListIterator<E> listIterator(int index)
    {
      return this.list.listIterator(index);
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedList(this.list.subList(fromIndex, toIndex), this.mutex);
      }
    }
    
    public void replaceAll(UnaryOperator<E> operator)
    {
      synchronized (this.mutex)
      {
        this.list.replaceAll(operator);
      }
    }
    
    public void sort(Comparator<? super E> c)
    {
      synchronized (this.mutex)
      {
        this.list.sort(c);
      }
    }
    
    private Object readResolve()
    {
      return (this.list instanceof RandomAccess) ? 
        new Collections.SynchronizedRandomAccessList(this.list) : 
        this;
    }
  }
  
  static class SynchronizedRandomAccessList<E>
    extends Collections.SynchronizedList<E>
    implements RandomAccess
  {
    private static final long serialVersionUID = 1530674583602358482L;
    
    SynchronizedRandomAccessList(List<E> list)
    {
      super();
    }
    
    SynchronizedRandomAccessList(List<E> list, Object mutex)
    {
      super(mutex);
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedRandomAccessList(this.list
          .subList(fromIndex, toIndex), this.mutex);
      }
    }
    
    private Object writeReplace()
    {
      return new Collections.SynchronizedList(this.list);
    }
  }
  
  public static <K, V> Map<K, V> synchronizedMap(Map<K, V> m)
  {
    return new SynchronizedMap(m);
  }
  
  private static class SynchronizedMap<K, V>
    implements Map<K, V>, Serializable
  {
    private static final long serialVersionUID = 1978198479659022715L;
    private final Map<K, V> m;
    final Object mutex;
    private transient Set<K> keySet;
    private transient Set<Map.Entry<K, V>> entrySet;
    private transient Collection<V> values;
    
    SynchronizedMap(Map<K, V> m)
    {
      this.m = ((Map)Objects.requireNonNull(m));
      this.mutex = this;
    }
    
    SynchronizedMap(Map<K, V> m, Object mutex)
    {
      this.m = m;
      this.mutex = mutex;
    }
    
    public int size()
    {
      synchronized (this.mutex)
      {
        return this.m.size();
      }
    }
    
    public boolean isEmpty()
    {
      synchronized (this.mutex)
      {
        return this.m.isEmpty();
      }
    }
    
    public boolean containsKey(Object key)
    {
      synchronized (this.mutex)
      {
        return this.m.containsKey(key);
      }
    }
    
    public boolean containsValue(Object value)
    {
      synchronized (this.mutex)
      {
        return this.m.containsValue(value);
      }
    }
    
    public V get(Object key)
    {
      synchronized (this.mutex)
      {
        return this.m.get(key);
      }
    }
    
    public V put(K key, V value)
    {
      synchronized (this.mutex)
      {
        return this.m.put(key, value);
      }
    }
    
    public V remove(Object key)
    {
      synchronized (this.mutex)
      {
        return this.m.remove(key);
      }
    }
    
    public void putAll(Map<? extends K, ? extends V> map)
    {
      synchronized (this.mutex)
      {
        this.m.putAll(map);
      }
    }
    
    public void clear()
    {
      synchronized (this.mutex)
      {
        this.m.clear();
      }
    }
    
    public Set<K> keySet()
    {
      synchronized (this.mutex)
      {
        if (this.keySet == null) {
          this.keySet = new Collections.SynchronizedSet(this.m.keySet(), this.mutex);
        }
        return this.keySet;
      }
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      synchronized (this.mutex)
      {
        if (this.entrySet == null) {
          this.entrySet = new Collections.SynchronizedSet(this.m.entrySet(), this.mutex);
        }
        return this.entrySet;
      }
    }
    
    public Collection<V> values()
    {
      synchronized (this.mutex)
      {
        if (this.values == null) {
          this.values = new Collections.SynchronizedCollection(this.m.values(), this.mutex);
        }
        return this.values;
      }
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      synchronized (this.mutex)
      {
        return this.m.equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (this.mutex)
      {
        return this.m.hashCode();
      }
    }
    
    public String toString()
    {
      synchronized (this.mutex)
      {
        return this.m.toString();
      }
    }
    
    public V getOrDefault(Object k, V defaultValue)
    {
      synchronized (this.mutex)
      {
        return this.m.getOrDefault(k, defaultValue);
      }
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action)
    {
      synchronized (this.mutex)
      {
        this.m.forEach(action);
      }
    }
    
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
    {
      synchronized (this.mutex)
      {
        this.m.replaceAll(function);
      }
    }
    
    public V putIfAbsent(K key, V value)
    {
      synchronized (this.mutex)
      {
        return this.m.putIfAbsent(key, value);
      }
    }
    
    public boolean remove(Object key, Object value)
    {
      synchronized (this.mutex)
      {
        return this.m.remove(key, value);
      }
    }
    
    public boolean replace(K key, V oldValue, V newValue)
    {
      synchronized (this.mutex)
      {
        return this.m.replace(key, oldValue, newValue);
      }
    }
    
    public V replace(K key, V value)
    {
      synchronized (this.mutex)
      {
        return this.m.replace(key, value);
      }
    }
    
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
    {
      synchronized (this.mutex)
      {
        return this.m.computeIfAbsent(key, mappingFunction);
      }
    }
    
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      synchronized (this.mutex)
      {
        return this.m.computeIfPresent(key, remappingFunction);
      }
    }
    
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      synchronized (this.mutex)
      {
        return this.m.compute(key, remappingFunction);
      }
    }
    
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
    {
      synchronized (this.mutex)
      {
        return this.m.merge(key, value, remappingFunction);
      }
    }
    
    private void writeObject(ObjectOutputStream s)
      throws IOException
    {
      synchronized (this.mutex)
      {
        s.defaultWriteObject();
      }
    }
  }
  
  public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> m)
  {
    return new SynchronizedSortedMap(m);
  }
  
  static class SynchronizedSortedMap<K, V>
    extends Collections.SynchronizedMap<K, V>
    implements SortedMap<K, V>
  {
    private static final long serialVersionUID = -8798146769416483793L;
    private final SortedMap<K, V> sm;
    
    SynchronizedSortedMap(SortedMap<K, V> m)
    {
      super();
      this.sm = m;
    }
    
    SynchronizedSortedMap(SortedMap<K, V> m, Object mutex)
    {
      super(mutex);
      this.sm = m;
    }
    
    public Comparator<? super K> comparator()
    {
      synchronized (this.mutex)
      {
        return this.sm.comparator();
      }
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedMap(this.sm
          .subMap(fromKey, toKey), this.mutex);
      }
    }
    
    public SortedMap<K, V> headMap(K toKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedMap(this.sm.headMap(toKey), this.mutex);
      }
    }
    
    public SortedMap<K, V> tailMap(K fromKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedSortedMap(this.sm.tailMap(fromKey), this.mutex);
      }
    }
    
    public K firstKey()
    {
      synchronized (this.mutex)
      {
        return this.sm.firstKey();
      }
    }
    
    public K lastKey()
    {
      synchronized (this.mutex)
      {
        return this.sm.lastKey();
      }
    }
  }
  
  public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> m)
  {
    return new SynchronizedNavigableMap(m);
  }
  
  static class SynchronizedNavigableMap<K, V>
    extends Collections.SynchronizedSortedMap<K, V>
    implements NavigableMap<K, V>
  {
    private static final long serialVersionUID = 699392247599746807L;
    private final NavigableMap<K, V> nm;
    
    SynchronizedNavigableMap(NavigableMap<K, V> m)
    {
      super();
      this.nm = m;
    }
    
    SynchronizedNavigableMap(NavigableMap<K, V> m, Object mutex)
    {
      super(mutex);
      this.nm = m;
    }
    
    public Map.Entry<K, V> lowerEntry(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.lowerEntry(key);
      }
    }
    
    public K lowerKey(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.lowerKey(key);
      }
    }
    
    public Map.Entry<K, V> floorEntry(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.floorEntry(key);
      }
    }
    
    public K floorKey(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.floorKey(key);
      }
    }
    
    public Map.Entry<K, V> ceilingEntry(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.ceilingEntry(key);
      }
    }
    
    public K ceilingKey(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.ceilingKey(key);
      }
    }
    
    public Map.Entry<K, V> higherEntry(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.higherEntry(key);
      }
    }
    
    public K higherKey(K key)
    {
      synchronized (this.mutex)
      {
        return this.nm.higherKey(key);
      }
    }
    
    public Map.Entry<K, V> firstEntry()
    {
      synchronized (this.mutex)
      {
        return this.nm.firstEntry();
      }
    }
    
    public Map.Entry<K, V> lastEntry()
    {
      synchronized (this.mutex)
      {
        return this.nm.lastEntry();
      }
    }
    
    public Map.Entry<K, V> pollFirstEntry()
    {
      synchronized (this.mutex)
      {
        return this.nm.pollFirstEntry();
      }
    }
    
    public Map.Entry<K, V> pollLastEntry()
    {
      synchronized (this.mutex)
      {
        return this.nm.pollLastEntry();
      }
    }
    
    public NavigableMap<K, V> descendingMap()
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm
          .descendingMap(), this.mutex);
      }
    }
    
    public NavigableSet<K> keySet()
    {
      return navigableKeySet();
    }
    
    public NavigableSet<K> navigableKeySet()
    {
      synchronized (this.mutex)
      {
        return new Collections.SynchronizedNavigableSet(this.nm.navigableKeySet(), this.mutex);
      }
    }
    
    public NavigableSet<K> descendingKeySet()
    {
      synchronized (this.mutex)
      {
        return new Collections.SynchronizedNavigableSet(this.nm.descendingKeySet(), this.mutex);
      }
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm
          .subMap(fromKey, true, toKey, false), this.mutex);
      }
    }
    
    public SortedMap<K, V> headMap(K toKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm.headMap(toKey, false), this.mutex);
      }
    }
    
    public SortedMap<K, V> tailMap(K fromKey)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm.tailMap(fromKey, true), this.mutex);
      }
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm
          .subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
      }
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm
          .headMap(toKey, inclusive), this.mutex);
      }
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
    {
      synchronized (this.mutex)
      {
        return new SynchronizedNavigableMap(this.nm
          .tailMap(fromKey, inclusive), this.mutex);
      }
    }
  }
  
  public static <E> Collection<E> checkedCollection(Collection<E> c, Class<E> type)
  {
    return new CheckedCollection(c, type);
  }
  
  static <T> T[] zeroLengthArray(Class<T> type)
  {
    return (Object[])Array.newInstance(type, 0);
  }
  
  static class CheckedCollection<E>
    implements Collection<E>, Serializable
  {
    private static final long serialVersionUID = 1578914078182001775L;
    final Collection<E> c;
    final Class<E> type;
    private E[] zeroLengthElementArray;
    
    E typeCheck(Object o)
    {
      if ((o != null) && (!this.type.isInstance(o))) {
        throw new ClassCastException(badElementMsg(o));
      }
      return o;
    }
    
    private String badElementMsg(Object o)
    {
      return "Attempt to insert " + o.getClass() + " element into collection with element type " + this.type;
    }
    
    CheckedCollection(Collection<E> c, Class<E> type)
    {
      this.c = ((Collection)Objects.requireNonNull(c, "c"));
      this.type = ((Class)Objects.requireNonNull(type, "type"));
    }
    
    public int size()
    {
      return this.c.size();
    }
    
    public boolean isEmpty()
    {
      return this.c.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return this.c.contains(o);
    }
    
    public Object[] toArray()
    {
      return this.c.toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return this.c.toArray(a);
    }
    
    public String toString()
    {
      return this.c.toString();
    }
    
    public boolean remove(Object o)
    {
      return this.c.remove(o);
    }
    
    public void clear()
    {
      this.c.clear();
    }
    
    public boolean containsAll(Collection<?> coll)
    {
      return this.c.containsAll(coll);
    }
    
    public boolean removeAll(Collection<?> coll)
    {
      return this.c.removeAll(coll);
    }
    
    public boolean retainAll(Collection<?> coll)
    {
      return this.c.retainAll(coll);
    }
    
    public Iterator<E> iterator()
    {
      final Iterator<E> it = this.c.iterator();
      new Iterator()
      {
        public boolean hasNext()
        {
          return it.hasNext();
        }
        
        public E next()
        {
          return it.next();
        }
        
        public void remove()
        {
          it.remove();
        }
      };
    }
    
    public boolean add(E e)
    {
      return this.c.add(typeCheck(e));
    }
    
    private E[] zeroLengthElementArray()
    {
      return 
        this.zeroLengthElementArray = Collections.zeroLengthArray(this.type);
    }
    
    Collection<E> checkedCopyOf(Collection<? extends E> coll)
    {
      Object[] a;
      Object[] arrayOfObject1;
      int i;
      int j;
      try
      {
        E[] z = zeroLengthElementArray();
        Object[] a = coll.toArray(z);
        if (a.getClass() != z.getClass()) {
          a = Arrays.copyOf(a, a.length, z.getClass());
        }
      }
      catch (ArrayStoreException ignore)
      {
        a = (Object[])coll.toArray().clone();
        arrayOfObject1 = a;i = arrayOfObject1.length;j = 0;
      }
      for (; j < i; j++)
      {
        Object o = arrayOfObject1[j];
        typeCheck(o);
      }
      return Arrays.asList(a);
    }
    
    public boolean addAll(Collection<? extends E> coll)
    {
      return this.c.addAll(checkedCopyOf(coll));
    }
    
    public void forEach(Consumer<? super E> action)
    {
      this.c.forEach(action);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      return this.c.removeIf(filter);
    }
    
    public Spliterator<E> spliterator()
    {
      return this.c.spliterator();
    }
    
    public Stream<E> stream()
    {
      return this.c.stream();
    }
    
    public Stream<E> parallelStream()
    {
      return this.c.parallelStream();
    }
  }
  
  public static <E> Queue<E> checkedQueue(Queue<E> queue, Class<E> type)
  {
    return new CheckedQueue(queue, type);
  }
  
  static class CheckedQueue<E>
    extends Collections.CheckedCollection<E>
    implements Queue<E>, Serializable
  {
    private static final long serialVersionUID = 1433151992604707767L;
    final Queue<E> queue;
    
    CheckedQueue(Queue<E> queue, Class<E> elementType)
    {
      super(elementType);
      this.queue = queue;
    }
    
    public E element()
    {
      return this.queue.element();
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.c.equals(o));
    }
    
    public int hashCode()
    {
      return this.c.hashCode();
    }
    
    public E peek()
    {
      return this.queue.peek();
    }
    
    public E poll()
    {
      return this.queue.poll();
    }
    
    public E remove()
    {
      return this.queue.remove();
    }
    
    public boolean offer(E e)
    {
      return this.queue.offer(typeCheck(e));
    }
  }
  
  public static <E> Set<E> checkedSet(Set<E> s, Class<E> type)
  {
    return new CheckedSet(s, type);
  }
  
  static class CheckedSet<E>
    extends Collections.CheckedCollection<E>
    implements Set<E>, Serializable
  {
    private static final long serialVersionUID = 4694047833775013803L;
    
    CheckedSet(Set<E> s, Class<E> elementType)
    {
      super(elementType);
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.c.equals(o));
    }
    
    public int hashCode()
    {
      return this.c.hashCode();
    }
  }
  
  public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> s, Class<E> type)
  {
    return new CheckedSortedSet(s, type);
  }
  
  static class CheckedSortedSet<E>
    extends Collections.CheckedSet<E>
    implements SortedSet<E>, Serializable
  {
    private static final long serialVersionUID = 1599911165492914959L;
    private final SortedSet<E> ss;
    
    CheckedSortedSet(SortedSet<E> s, Class<E> type)
    {
      super(type);
      this.ss = s;
    }
    
    public Comparator<? super E> comparator()
    {
      return this.ss.comparator();
    }
    
    public E first()
    {
      return this.ss.first();
    }
    
    public E last()
    {
      return this.ss.last();
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement)
    {
      return Collections.checkedSortedSet(this.ss.subSet(fromElement, toElement), this.type);
    }
    
    public SortedSet<E> headSet(E toElement)
    {
      return Collections.checkedSortedSet(this.ss.headSet(toElement), this.type);
    }
    
    public SortedSet<E> tailSet(E fromElement)
    {
      return Collections.checkedSortedSet(this.ss.tailSet(fromElement), this.type);
    }
  }
  
  public static <E> NavigableSet<E> checkedNavigableSet(NavigableSet<E> s, Class<E> type)
  {
    return new CheckedNavigableSet(s, type);
  }
  
  static class CheckedNavigableSet<E>
    extends Collections.CheckedSortedSet<E>
    implements NavigableSet<E>, Serializable
  {
    private static final long serialVersionUID = -5429120189805438922L;
    private final NavigableSet<E> ns;
    
    CheckedNavigableSet(NavigableSet<E> s, Class<E> type)
    {
      super(type);
      this.ns = s;
    }
    
    public E lower(E e)
    {
      return this.ns.lower(e);
    }
    
    public E floor(E e)
    {
      return this.ns.floor(e);
    }
    
    public E ceiling(E e)
    {
      return this.ns.ceiling(e);
    }
    
    public E higher(E e)
    {
      return this.ns.higher(e);
    }
    
    public E pollFirst()
    {
      return this.ns.pollFirst();
    }
    
    public E pollLast()
    {
      return this.ns.pollLast();
    }
    
    public NavigableSet<E> descendingSet()
    {
      return Collections.checkedNavigableSet(this.ns.descendingSet(), this.type);
    }
    
    public Iterator<E> descendingIterator()
    {
      return Collections.checkedNavigableSet(this.ns.descendingSet(), this.type).iterator();
    }
    
    public NavigableSet<E> subSet(E fromElement, E toElement)
    {
      return Collections.checkedNavigableSet(this.ns.subSet(fromElement, true, toElement, false), this.type);
    }
    
    public NavigableSet<E> headSet(E toElement)
    {
      return Collections.checkedNavigableSet(this.ns.headSet(toElement, false), this.type);
    }
    
    public NavigableSet<E> tailSet(E fromElement)
    {
      return Collections.checkedNavigableSet(this.ns.tailSet(fromElement, true), this.type);
    }
    
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
    {
      return Collections.checkedNavigableSet(this.ns.subSet(fromElement, fromInclusive, toElement, toInclusive), this.type);
    }
    
    public NavigableSet<E> headSet(E toElement, boolean inclusive)
    {
      return Collections.checkedNavigableSet(this.ns.headSet(toElement, inclusive), this.type);
    }
    
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
    {
      return Collections.checkedNavigableSet(this.ns.tailSet(fromElement, inclusive), this.type);
    }
  }
  
  public static <E> List<E> checkedList(List<E> list, Class<E> type)
  {
    return (list instanceof RandomAccess) ? 
      new CheckedRandomAccessList(list, type) : 
      new Collections.CheckedList(list, type);
  }
  
  static class CheckedRandomAccessList<E>
    extends Collections.CheckedList<E>
    implements RandomAccess
  {
    private static final long serialVersionUID = 1638200125423088369L;
    
    CheckedRandomAccessList(List<E> list, Class<E> type)
    {
      super(type);
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      return new CheckedRandomAccessList(this.list
        .subList(fromIndex, toIndex), this.type);
    }
  }
  
  public static <K, V> Map<K, V> checkedMap(Map<K, V> m, Class<K> keyType, Class<V> valueType)
  {
    return new Collections.CheckedMap(m, keyType, valueType);
  }
  
  public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> m, Class<K> keyType, Class<V> valueType)
  {
    return new CheckedSortedMap(m, keyType, valueType);
  }
  
  static class CheckedSortedMap<K, V>
    extends Collections.CheckedMap<K, V>
    implements SortedMap<K, V>, Serializable
  {
    private static final long serialVersionUID = 1599671320688067438L;
    private final SortedMap<K, V> sm;
    
    CheckedSortedMap(SortedMap<K, V> m, Class<K> keyType, Class<V> valueType)
    {
      super(keyType, valueType);
      this.sm = m;
    }
    
    public Comparator<? super K> comparator()
    {
      return this.sm.comparator();
    }
    
    public K firstKey()
    {
      return this.sm.firstKey();
    }
    
    public K lastKey()
    {
      return this.sm.lastKey();
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      return Collections.checkedSortedMap(this.sm.subMap(fromKey, toKey), this.keyType, this.valueType);
    }
    
    public SortedMap<K, V> headMap(K toKey)
    {
      return Collections.checkedSortedMap(this.sm.headMap(toKey), this.keyType, this.valueType);
    }
    
    public SortedMap<K, V> tailMap(K fromKey)
    {
      return Collections.checkedSortedMap(this.sm.tailMap(fromKey), this.keyType, this.valueType);
    }
  }
  
  public static <K, V> NavigableMap<K, V> checkedNavigableMap(NavigableMap<K, V> m, Class<K> keyType, Class<V> valueType)
  {
    return new CheckedNavigableMap(m, keyType, valueType);
  }
  
  static class CheckedNavigableMap<K, V>
    extends Collections.CheckedSortedMap<K, V>
    implements NavigableMap<K, V>, Serializable
  {
    private static final long serialVersionUID = -4852462692372534096L;
    private final NavigableMap<K, V> nm;
    
    CheckedNavigableMap(NavigableMap<K, V> m, Class<K> keyType, Class<V> valueType)
    {
      super(keyType, valueType);
      this.nm = m;
    }
    
    public Comparator<? super K> comparator()
    {
      return this.nm.comparator();
    }
    
    public K firstKey()
    {
      return this.nm.firstKey();
    }
    
    public K lastKey()
    {
      return this.nm.lastKey();
    }
    
    public Map.Entry<K, V> lowerEntry(K key)
    {
      Map.Entry<K, V> lower = this.nm.lowerEntry(key);
      return null != lower ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(lower, this.valueType) : 
        null;
    }
    
    public K lowerKey(K key)
    {
      return this.nm.lowerKey(key);
    }
    
    public Map.Entry<K, V> floorEntry(K key)
    {
      Map.Entry<K, V> floor = this.nm.floorEntry(key);
      return null != floor ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(floor, this.valueType) : 
        null;
    }
    
    public K floorKey(K key)
    {
      return this.nm.floorKey(key);
    }
    
    public Map.Entry<K, V> ceilingEntry(K key)
    {
      Map.Entry<K, V> ceiling = this.nm.ceilingEntry(key);
      return null != ceiling ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(ceiling, this.valueType) : 
        null;
    }
    
    public K ceilingKey(K key)
    {
      return this.nm.ceilingKey(key);
    }
    
    public Map.Entry<K, V> higherEntry(K key)
    {
      Map.Entry<K, V> higher = this.nm.higherEntry(key);
      return null != higher ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(higher, this.valueType) : 
        null;
    }
    
    public K higherKey(K key)
    {
      return this.nm.higherKey(key);
    }
    
    public Map.Entry<K, V> firstEntry()
    {
      Map.Entry<K, V> first = this.nm.firstEntry();
      return null != first ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(first, this.valueType) : 
        null;
    }
    
    public Map.Entry<K, V> lastEntry()
    {
      Map.Entry<K, V> last = this.nm.lastEntry();
      return null != last ? 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(last, this.valueType) : 
        null;
    }
    
    public Map.Entry<K, V> pollFirstEntry()
    {
      Map.Entry<K, V> entry = this.nm.pollFirstEntry();
      return null == entry ? 
        null : 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(entry, this.valueType);
    }
    
    public Map.Entry<K, V> pollLastEntry()
    {
      Map.Entry<K, V> entry = this.nm.pollLastEntry();
      return null == entry ? 
        null : 
        new Collections.CheckedMap.CheckedEntrySet.CheckedEntry(entry, this.valueType);
    }
    
    public NavigableMap<K, V> descendingMap()
    {
      return Collections.checkedNavigableMap(this.nm.descendingMap(), this.keyType, this.valueType);
    }
    
    public NavigableSet<K> keySet()
    {
      return navigableKeySet();
    }
    
    public NavigableSet<K> navigableKeySet()
    {
      return Collections.checkedNavigableSet(this.nm.navigableKeySet(), this.keyType);
    }
    
    public NavigableSet<K> descendingKeySet()
    {
      return Collections.checkedNavigableSet(this.nm.descendingKeySet(), this.keyType);
    }
    
    public NavigableMap<K, V> subMap(K fromKey, K toKey)
    {
      return Collections.checkedNavigableMap(this.nm.subMap(fromKey, true, toKey, false), this.keyType, this.valueType);
    }
    
    public NavigableMap<K, V> headMap(K toKey)
    {
      return Collections.checkedNavigableMap(this.nm.headMap(toKey, false), this.keyType, this.valueType);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey)
    {
      return Collections.checkedNavigableMap(this.nm.tailMap(fromKey, true), this.keyType, this.valueType);
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
      return Collections.checkedNavigableMap(this.nm.subMap(fromKey, fromInclusive, toKey, toInclusive), this.keyType, this.valueType);
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
    {
      return Collections.checkedNavigableMap(this.nm.headMap(toKey, inclusive), this.keyType, this.valueType);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
    {
      return Collections.checkedNavigableMap(this.nm.tailMap(fromKey, inclusive), this.keyType, this.valueType);
    }
  }
  
  public static <T> Iterator<T> emptyIterator()
  {
    return EmptyIterator.EMPTY_ITERATOR;
  }
  
  private static class EmptyIterator<E>
    implements Iterator<E>
  {
    static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator();
    
    public boolean hasNext()
    {
      return false;
    }
    
    public E next()
    {
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new IllegalStateException();
    }
    
    public void forEachRemaining(Consumer<? super E> action)
    {
      Objects.requireNonNull(action);
    }
  }
  
  public static <T> ListIterator<T> emptyListIterator()
  {
    return EmptyListIterator.EMPTY_ITERATOR;
  }
  
  private static class EmptyListIterator<E>
    extends Collections.EmptyIterator<E>
    implements ListIterator<E>
  {
    private EmptyListIterator()
    {
      super();
    }
    
    static final EmptyListIterator<Object> EMPTY_ITERATOR = new EmptyListIterator();
    
    public boolean hasPrevious()
    {
      return false;
    }
    
    public E previous()
    {
      throw new NoSuchElementException();
    }
    
    public int nextIndex()
    {
      return 0;
    }
    
    public int previousIndex()
    {
      return -1;
    }
    
    public void set(E e)
    {
      throw new IllegalStateException();
    }
    
    public void add(E e)
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public static <T> Enumeration<T> emptyEnumeration()
  {
    return EmptyEnumeration.EMPTY_ENUMERATION;
  }
  
  private static class EmptyEnumeration<E>
    implements Enumeration<E>
  {
    static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration();
    
    public boolean hasMoreElements()
    {
      return false;
    }
    
    public E nextElement()
    {
      throw new NoSuchElementException();
    }
    
    public Iterator<E> asIterator()
    {
      return Collections.emptyIterator();
    }
  }
  
  public static final Set EMPTY_SET = new EmptySet(null);
  
  public static final <T> Set<T> emptySet()
  {
    return EMPTY_SET;
  }
  
  private static class EmptySet<E>
    extends AbstractSet<E>
    implements Serializable
  {
    private static final long serialVersionUID = 1582296315990362920L;
    
    public Iterator<E> iterator()
    {
      return Collections.emptyIterator();
    }
    
    public int size()
    {
      return 0;
    }
    
    public boolean isEmpty()
    {
      return true;
    }
    
    public void clear() {}
    
    public boolean contains(Object obj)
    {
      return false;
    }
    
    public boolean containsAll(Collection<?> c)
    {
      return c.isEmpty();
    }
    
    public Object[] toArray()
    {
      return new Object[0];
    }
    
    public <T> T[] toArray(T[] a)
    {
      if (a.length > 0) {
        a[0] = null;
      }
      return a;
    }
    
    public void forEach(Consumer<? super E> action)
    {
      Objects.requireNonNull(action);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      Objects.requireNonNull(filter);
      return false;
    }
    
    public Spliterator<E> spliterator()
    {
      return Spliterators.emptySpliterator();
    }
    
    private Object readResolve()
    {
      return Collections.EMPTY_SET;
    }
    
    public int hashCode()
    {
      return 0;
    }
  }
  
  public static <E> SortedSet<E> emptySortedSet()
  {
    return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
  }
  
  public static <E> NavigableSet<E> emptyNavigableSet()
  {
    return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
  }
  
  public static final List EMPTY_LIST = new EmptyList(null);
  
  public static final <T> List<T> emptyList()
  {
    return EMPTY_LIST;
  }
  
  private static class EmptyList<E>
    extends AbstractList<E>
    implements RandomAccess, Serializable
  {
    private static final long serialVersionUID = 8842843931221139166L;
    
    public Iterator<E> iterator()
    {
      return Collections.emptyIterator();
    }
    
    public ListIterator<E> listIterator()
    {
      return Collections.emptyListIterator();
    }
    
    public int size()
    {
      return 0;
    }
    
    public boolean isEmpty()
    {
      return true;
    }
    
    public void clear() {}
    
    public boolean contains(Object obj)
    {
      return false;
    }
    
    public boolean containsAll(Collection<?> c)
    {
      return c.isEmpty();
    }
    
    public Object[] toArray()
    {
      return new Object[0];
    }
    
    public <T> T[] toArray(T[] a)
    {
      if (a.length > 0) {
        a[0] = null;
      }
      return a;
    }
    
    public E get(int index)
    {
      throw new IndexOutOfBoundsException("Index: " + index);
    }
    
    public boolean equals(Object o)
    {
      return ((o instanceof List)) && (((List)o).isEmpty());
    }
    
    public int hashCode()
    {
      return 1;
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      Objects.requireNonNull(filter);
      return false;
    }
    
    public void replaceAll(UnaryOperator<E> operator)
    {
      Objects.requireNonNull(operator);
    }
    
    public void sort(Comparator<? super E> c) {}
    
    public void forEach(Consumer<? super E> action)
    {
      Objects.requireNonNull(action);
    }
    
    public Spliterator<E> spliterator()
    {
      return Spliterators.emptySpliterator();
    }
    
    private Object readResolve()
    {
      return Collections.EMPTY_LIST;
    }
  }
  
  public static final Map EMPTY_MAP = new EmptyMap(null);
  
  public static final <K, V> Map<K, V> emptyMap()
  {
    return EMPTY_MAP;
  }
  
  public static final <K, V> SortedMap<K, V> emptySortedMap()
  {
    return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
  }
  
  public static final <K, V> NavigableMap<K, V> emptyNavigableMap()
  {
    return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
  }
  
  private static class EmptyMap<K, V>
    extends AbstractMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID = 6428348081105594320L;
    
    public int size()
    {
      return 0;
    }
    
    public boolean isEmpty()
    {
      return true;
    }
    
    public void clear() {}
    
    public boolean containsKey(Object key)
    {
      return false;
    }
    
    public boolean containsValue(Object value)
    {
      return false;
    }
    
    public V get(Object key)
    {
      return null;
    }
    
    public Set<K> keySet()
    {
      return Collections.emptySet();
    }
    
    public Collection<V> values()
    {
      return Collections.emptySet();
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      return Collections.emptySet();
    }
    
    public boolean equals(Object o)
    {
      return ((o instanceof Map)) && (((Map)o).isEmpty());
    }
    
    public int hashCode()
    {
      return 0;
    }
    
    public V getOrDefault(Object k, V defaultValue)
    {
      return defaultValue;
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action)
    {
      Objects.requireNonNull(action);
    }
    
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
    {
      Objects.requireNonNull(function);
    }
    
    public V putIfAbsent(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object key, Object value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean replace(K key, V oldValue, V newValue)
    {
      throw new UnsupportedOperationException();
    }
    
    public V replace(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    private Object readResolve()
    {
      return Collections.EMPTY_MAP;
    }
  }
  
  public static <T> Set<T> singleton(T o)
  {
    return new SingletonSet(o);
  }
  
  static <E> Iterator<E> singletonIterator(E e)
  {
    new Iterator()
    {
      private boolean hasNext = true;
      
      public boolean hasNext()
      {
        return this.hasNext;
      }
      
      public E next()
      {
        if (this.hasNext)
        {
          this.hasNext = false;
          return Collections.this;
        }
        throw new NoSuchElementException();
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
      
      public void forEachRemaining(Consumer<? super E> action)
      {
        Objects.requireNonNull(action);
        if (this.hasNext)
        {
          this.hasNext = false;
          action.accept(Collections.this);
        }
      }
    };
  }
  
  static <T> Spliterator<T> singletonSpliterator(T element)
  {
    new Spliterator()
    {
      long est = 1L;
      
      public Spliterator<T> trySplit()
      {
        return null;
      }
      
      public boolean tryAdvance(Consumer<? super T> consumer)
      {
        Objects.requireNonNull(consumer);
        if (this.est > 0L)
        {
          this.est -= 1L;
          consumer.accept(Collections.this);
          return true;
        }
        return false;
      }
      
      public void forEachRemaining(Consumer<? super T> consumer)
      {
        tryAdvance(consumer);
      }
      
      public long estimateSize()
      {
        return this.est;
      }
      
      public int characteristics()
      {
        int value = Collections.this != null ? 256 : 0;
        
        return value | 0x40 | 0x4000 | 0x400 | 0x1 | 0x10;
      }
    };
  }
  
  private static class SingletonSet<E>
    extends AbstractSet<E>
    implements Serializable
  {
    private static final long serialVersionUID = 3193687207550431679L;
    private final E element;
    
    SingletonSet(E e)
    {
      this.element = e;
    }
    
    public Iterator<E> iterator()
    {
      return Collections.singletonIterator(this.element);
    }
    
    public int size()
    {
      return 1;
    }
    
    public boolean contains(Object o)
    {
      return Collections.eq(o, this.element);
    }
    
    public void forEach(Consumer<? super E> action)
    {
      action.accept(this.element);
    }
    
    public Spliterator<E> spliterator()
    {
      return Collections.singletonSpliterator(this.element);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      throw new UnsupportedOperationException();
    }
    
    public int hashCode()
    {
      return Objects.hashCode(this.element);
    }
  }
  
  public static <T> List<T> singletonList(T o)
  {
    return new SingletonList(o);
  }
  
  private static class SingletonList<E>
    extends AbstractList<E>
    implements RandomAccess, Serializable
  {
    private static final long serialVersionUID = 3093736618740652951L;
    private final E element;
    
    SingletonList(E obj)
    {
      this.element = obj;
    }
    
    public Iterator<E> iterator()
    {
      return Collections.singletonIterator(this.element);
    }
    
    public int size()
    {
      return 1;
    }
    
    public boolean contains(Object obj)
    {
      return Collections.eq(obj, this.element);
    }
    
    public E get(int index)
    {
      if (index != 0) {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: 1");
      }
      return this.element;
    }
    
    public void forEach(Consumer<? super E> action)
    {
      action.accept(this.element);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      throw new UnsupportedOperationException();
    }
    
    public void replaceAll(UnaryOperator<E> operator)
    {
      throw new UnsupportedOperationException();
    }
    
    public void sort(Comparator<? super E> c) {}
    
    public Spliterator<E> spliterator()
    {
      return Collections.singletonSpliterator(this.element);
    }
    
    public int hashCode()
    {
      return 31 + Objects.hashCode(this.element);
    }
  }
  
  public static <K, V> Map<K, V> singletonMap(K key, V value)
  {
    return new SingletonMap(key, value);
  }
  
  private static class SingletonMap<K, V>
    extends AbstractMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID = -6979724477215052911L;
    private final K k;
    private final V v;
    private transient Set<K> keySet;
    private transient Set<Map.Entry<K, V>> entrySet;
    private transient Collection<V> values;
    
    SingletonMap(K key, V value)
    {
      this.k = key;
      this.v = value;
    }
    
    public int size()
    {
      return 1;
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    public boolean containsKey(Object key)
    {
      return Collections.eq(key, this.k);
    }
    
    public boolean containsValue(Object value)
    {
      return Collections.eq(value, this.v);
    }
    
    public V get(Object key)
    {
      return Collections.eq(key, this.k) ? this.v : null;
    }
    
    public Set<K> keySet()
    {
      if (this.keySet == null) {
        this.keySet = Collections.singleton(this.k);
      }
      return this.keySet;
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      if (this.entrySet == null) {
        this.entrySet = Collections.singleton(new AbstractMap.SimpleImmutableEntry(this.k, this.v));
      }
      return this.entrySet;
    }
    
    public Collection<V> values()
    {
      if (this.values == null) {
        this.values = Collections.singleton(this.v);
      }
      return this.values;
    }
    
    public V getOrDefault(Object key, V defaultValue)
    {
      return Collections.eq(key, this.k) ? this.v : defaultValue;
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action)
    {
      action.accept(this.k, this.v);
    }
    
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
    {
      throw new UnsupportedOperationException();
    }
    
    public V putIfAbsent(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object key, Object value)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean replace(K key, V oldValue, V newValue)
    {
      throw new UnsupportedOperationException();
    }
    
    public V replace(K key, V value)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
    {
      throw new UnsupportedOperationException();
    }
    
    public int hashCode()
    {
      return Objects.hashCode(this.k) ^ Objects.hashCode(this.v);
    }
  }
  
  public static <T> List<T> nCopies(int n, T o)
  {
    if (n < 0) {
      throw new IllegalArgumentException("List length = " + n);
    }
    return new Collections.CopiesList(n, o);
  }
  
  public static <T> Comparator<T> reverseOrder()
  {
    return ReverseComparator.REVERSE_ORDER;
  }
  
  private static class ReverseComparator
    implements Comparator<Comparable<Object>>, Serializable
  {
    private static final long serialVersionUID = 7207038068494060240L;
    static final ReverseComparator REVERSE_ORDER = new ReverseComparator();
    
    public int compare(Comparable<Object> c1, Comparable<Object> c2)
    {
      return c2.compareTo(c1);
    }
    
    private Object readResolve()
    {
      return Collections.reverseOrder();
    }
    
    public Comparator<Comparable<Object>> reversed()
    {
      return Comparator.naturalOrder();
    }
  }
  
  public static <T> Comparator<T> reverseOrder(Comparator<T> cmp)
  {
    if (cmp == null) {
      return ReverseComparator.REVERSE_ORDER;
    }
    if (cmp == ReverseComparator.REVERSE_ORDER) {
      return Comparators.NaturalOrderComparator.INSTANCE;
    }
    if (cmp == Comparators.NaturalOrderComparator.INSTANCE) {
      return ReverseComparator.REVERSE_ORDER;
    }
    if ((cmp instanceof ReverseComparator2)) {
      return ((ReverseComparator2)cmp).cmp;
    }
    return new ReverseComparator2(cmp);
  }
  
  private static class ReverseComparator2<T>
    implements Comparator<T>, Serializable
  {
    private static final long serialVersionUID = 4374092139857L;
    final Comparator<T> cmp;
    
    ReverseComparator2(Comparator<T> cmp)
    {
      assert (cmp != null);
      this.cmp = cmp;
    }
    
    public int compare(T t1, T t2)
    {
      return this.cmp.compare(t2, t1);
    }
    
    public boolean equals(Object o)
    {
      if (o != this) {
        if (!(o instanceof ReverseComparator2)) {
          break label35;
        }
      }
      label35:
      return this.cmp
      
        .equals(((ReverseComparator2)o).cmp);
    }
    
    public int hashCode()
    {
      return this.cmp.hashCode() ^ 0x80000000;
    }
    
    public Comparator<T> reversed()
    {
      return this.cmp;
    }
  }
  
  public static <T> Enumeration<T> enumeration(Collection<T> c)
  {
    new Enumeration()
    {
      private final Iterator<T> i = Collections.this.iterator();
      
      public boolean hasMoreElements()
      {
        return this.i.hasNext();
      }
      
      public T nextElement()
      {
        return this.i.next();
      }
    };
  }
  
  public static <T> ArrayList<T> list(Enumeration<T> e)
  {
    ArrayList<T> l = new ArrayList();
    while (e.hasMoreElements()) {
      l.add(e.nextElement());
    }
    return l;
  }
  
  static boolean eq(Object o1, Object o2)
  {
    return o1 == null ? false : o2 == null ? true : o1.equals(o2);
  }
  
  public static int frequency(Collection<?> c, Object o)
  {
    int result = 0;
    if (o == null) {
      for (Object e : c) {
        if (e == null) {
          result++;
        }
      }
    } else {
      for (Object e : c) {
        if (o.equals(e)) {
          result++;
        }
      }
    }
    return result;
  }
  
  public static boolean disjoint(Collection<?> c1, Collection<?> c2)
  {
    Collection<?> contains = c2;
    




    Collection<?> iterate = c1;
    int c1size;
    if ((c1 instanceof Set))
    {
      iterate = c2;
      contains = c1;
    }
    else if (!(c2 instanceof Set))
    {
      c1size = c1.size();
      int c2size = c2.size();
      if ((c1size == 0) || (c2size == 0)) {
        return true;
      }
      if (c1size > c2size)
      {
        iterate = c2;
        contains = c1;
      }
    }
    for (Object e : iterate) {
      if (contains.contains(e)) {
        return false;
      }
    }
    return true;
  }
  
  @SafeVarargs
  public static <T> boolean addAll(Collection<? super T> c, T... elements)
  {
    boolean result = false;
    for (T element : elements) {
      result |= c.add(element);
    }
    return result;
  }
  
  public static <E> Set<E> newSetFromMap(Map<E, Boolean> map)
  {
    return new SetFromMap(map);
  }
  
  private static class SetFromMap<E>
    extends AbstractSet<E>
    implements Set<E>, Serializable
  {
    private final Map<E, Boolean> m;
    private transient Set<E> s;
    private static final long serialVersionUID = 2454657854757543876L;
    
    SetFromMap(Map<E, Boolean> map)
    {
      if (!map.isEmpty()) {
        throw new IllegalArgumentException("Map is non-empty");
      }
      this.m = map;
      this.s = map.keySet();
    }
    
    public void clear()
    {
      this.m.clear();
    }
    
    public int size()
    {
      return this.m.size();
    }
    
    public boolean isEmpty()
    {
      return this.m.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return this.m.containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      return this.m.remove(o) != null;
    }
    
    public boolean add(E e)
    {
      return this.m.put(e, Boolean.TRUE) == null;
    }
    
    public Iterator<E> iterator()
    {
      return this.s.iterator();
    }
    
    public Object[] toArray()
    {
      return this.s.toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return this.s.toArray(a);
    }
    
    public String toString()
    {
      return this.s.toString();
    }
    
    public int hashCode()
    {
      return this.s.hashCode();
    }
    
    public boolean equals(Object o)
    {
      return (o == this) || (this.s.equals(o));
    }
    
    public boolean containsAll(Collection<?> c)
    {
      return this.s.containsAll(c);
    }
    
    public boolean removeAll(Collection<?> c)
    {
      return this.s.removeAll(c);
    }
    
    public boolean retainAll(Collection<?> c)
    {
      return this.s.retainAll(c);
    }
    
    public void forEach(Consumer<? super E> action)
    {
      this.s.forEach(action);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      return this.s.removeIf(filter);
    }
    
    public Spliterator<E> spliterator()
    {
      return this.s.spliterator();
    }
    
    public Stream<E> stream()
    {
      return this.s.stream();
    }
    
    public Stream<E> parallelStream()
    {
      return this.s.parallelStream();
    }
    
    private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException
    {
      stream.defaultReadObject();
      this.s = this.m.keySet();
    }
  }
  
  public static <T> Queue<T> asLifoQueue(Deque<T> deque)
  {
    return new AsLIFOQueue((Deque)Objects.requireNonNull(deque));
  }
  
  static class AsLIFOQueue<E>
    extends AbstractQueue<E>
    implements Queue<E>, Serializable
  {
    private static final long serialVersionUID = 1802017725587941708L;
    private final Deque<E> q;
    
    AsLIFOQueue(Deque<E> q)
    {
      this.q = q;
    }
    
    public boolean add(E e)
    {
      this.q.addFirst(e);return true;
    }
    
    public boolean offer(E e)
    {
      return this.q.offerFirst(e);
    }
    
    public E poll()
    {
      return this.q.pollFirst();
    }
    
    public E remove()
    {
      return this.q.removeFirst();
    }
    
    public E peek()
    {
      return this.q.peekFirst();
    }
    
    public E element()
    {
      return this.q.getFirst();
    }
    
    public void clear()
    {
      this.q.clear();
    }
    
    public int size()
    {
      return this.q.size();
    }
    
    public boolean isEmpty()
    {
      return this.q.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return this.q.contains(o);
    }
    
    public boolean remove(Object o)
    {
      return this.q.remove(o);
    }
    
    public Iterator<E> iterator()
    {
      return this.q.iterator();
    }
    
    public Object[] toArray()
    {
      return this.q.toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return this.q.toArray(a);
    }
    
    public String toString()
    {
      return this.q.toString();
    }
    
    public boolean containsAll(Collection<?> c)
    {
      return this.q.containsAll(c);
    }
    
    public boolean removeAll(Collection<?> c)
    {
      return this.q.removeAll(c);
    }
    
    public boolean retainAll(Collection<?> c)
    {
      return this.q.retainAll(c);
    }
    
    public void forEach(Consumer<? super E> action)
    {
      this.q.forEach(action);
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      return this.q.removeIf(filter);
    }
    
    public Spliterator<E> spliterator()
    {
      return this.q.spliterator();
    }
    
    public Stream<E> stream()
    {
      return this.q.stream();
    }
    
    public Stream<E> parallelStream()
    {
      return this.q.parallelStream();
    }
  }
}
