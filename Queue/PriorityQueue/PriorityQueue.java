package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;
import jdk.internal.misc.JavaObjectInputStreamAccess;
import jdk.internal.misc.SharedSecrets;

public class PriorityQueue<E>   extends AbstractQueue<E>   implements Serializable {
  
  private static final long serialVersionUID = -7720805057305804111L;
  private static final int DEFAULT_INITIAL_CAPACITY = 11;
  transient Object[] queue;
  int size;
  private final Comparator<? super E> comparator;
  transient int modCount;
  private static final int MAX_ARRAY_SIZE = 2147483639;
  
  public PriorityQueue()
  {
    this(11, null);
  }
  
  public PriorityQueue(int initialCapacity)
  {
    this(initialCapacity, null);
  }
  
  public PriorityQueue(Comparator<? super E> comparator)
  {
    this(11, comparator);
  }
  
  public PriorityQueue(int initialCapacity, Comparator<? super E> comparator)
  {
    if (initialCapacity < 1) {
      throw new IllegalArgumentException();
    }
    this.queue = new Object[initialCapacity];
    this.comparator = comparator;
  }
  
  public PriorityQueue(Collection<? extends E> c)
  {
    if ((c instanceof SortedSet))
    {
      SortedSet<? extends E> ss = (SortedSet)c;
      this.comparator = ss.comparator();
      initElementsFromCollection(ss);
    }
    else if ((c instanceof PriorityQueue))
    {
      PriorityQueue<? extends E> pq = (PriorityQueue)c;
      this.comparator = pq.comparator();
      initFromPriorityQueue(pq);
    }
    else
    {
      this.comparator = null;
      initFromCollection(c);
    }
  }
  
  public PriorityQueue(PriorityQueue<? extends E> c)
  {
    this.comparator = c.comparator();
    initFromPriorityQueue(c);
  }
  
  public PriorityQueue(SortedSet<? extends E> c)
  {
    this.comparator = c.comparator();
    initElementsFromCollection(c);
  }
  
  private void initFromPriorityQueue(PriorityQueue<? extends E> c)
  {
    if (c.getClass() == PriorityQueue.class)
    {
      this.queue = c.toArray();
      this.size = c.size();
    }
    else
    {
      initFromCollection(c);
    }
  }
  
  private void initElementsFromCollection(Collection<? extends E> c)
  {
    Object[] a = c.toArray();
    if (a.getClass() != [Ljava.lang.Object.class) {
      a = Arrays.copyOf(a, a.length, [Ljava.lang.Object.class);
    }
    int len = a.length;
    if ((len == 1) || (this.comparator != null)) {
      for (Object e : a) {
        if (e == null) {
          throw new NullPointerException();
        }
      }
    }
    this.queue = a;
    this.size = a.length;
  }
  
  private void initFromCollection(Collection<? extends E> c)
  {
    initElementsFromCollection(c);
    heapify();
  }
  
  private void grow(int minCapacity)
  {
    int oldCapacity = this.queue.length;
    


    int newCapacity = oldCapacity + (oldCapacity < 64 ? oldCapacity + 2 : oldCapacity >> 1);
    if (newCapacity - 2147483639 > 0) {
      newCapacity = hugeCapacity(minCapacity);
    }
    this.queue = Arrays.copyOf(this.queue, newCapacity);
  }
  
  private static int hugeCapacity(int minCapacity)
  {
    if (minCapacity < 0) {
      throw new OutOfMemoryError();
    }
    return minCapacity > 2147483639 ? 
      2147483647 : 
      2147483639;
  }
  
  public boolean add(E e)
  {
    return offer(e);
  }
  
  public boolean offer(E e)
  {
    if (e == null) {
      throw new NullPointerException();
    }
    this.modCount += 1;
    int i = this.size;
    if (i >= this.queue.length) {
      grow(i + 1);
    }
    siftUp(i, e);
    this.size = (i + 1);
    return true;
  }
  
  public E peek()
  {
    return this.size == 0 ? null : this.queue[0];
  }
  
  private int indexOf(Object o)
  {
    if (o != null) {
      for (int i = 0; i < this.size; i++) {
        if (o.equals(this.queue[i])) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public boolean remove(Object o)
  {
    int i = indexOf(o);
    if (i == -1) {
      return false;
    }
    removeAt(i);
    return true;
  }
  
  boolean removeEq(Object o)
  {
    for (int i = 0; i < this.size; i++) {
      if (o == this.queue[i])
      {
        removeAt(i);
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(Object o)
  {
    return indexOf(o) >= 0;
  }
  
  public Object[] toArray()
  {
    return Arrays.copyOf(this.queue, this.size);
  }
  
  public <T> T[] toArray(T[] a)
  {
    int size = this.size;
    if (a.length < size) {
      return Arrays.copyOf(this.queue, size, a.getClass());
    }
    System.arraycopy(this.queue, 0, a, 0, size);
    if (a.length > size) {
      a[size] = null;
    }
    return a;
  }
  
  public Iterator<E> iterator()
  {
    return new Itr();
  }
  
  private final class Itr
    implements Iterator<E>
  {
    private int cursor;
    private int lastRet = -1;
    private ArrayDeque<E> forgetMeNot;
    private E lastRetElt;
    private int expectedModCount = PriorityQueue.this.modCount;
    
    Itr() {}
    
    public boolean hasNext()
    {
      return (this.cursor < PriorityQueue.this.size) || ((this.forgetMeNot != null) && 
        (!this.forgetMeNot.isEmpty()));
    }
    
    public E next()
    {
      if (this.expectedModCount != PriorityQueue.this.modCount) {
        throw new ConcurrentModificationException();
      }
      if (this.cursor < PriorityQueue.this.size) {
        return PriorityQueue.this.queue[(this.lastRet = this.cursor++)];
      }
      if (this.forgetMeNot != null)
      {
        this.lastRet = -1;
        this.lastRetElt = this.forgetMeNot.poll();
        if (this.lastRetElt != null) {
          return this.lastRetElt;
        }
      }
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      if (this.expectedModCount != PriorityQueue.this.modCount) {
        throw new ConcurrentModificationException();
      }
      if (this.lastRet != -1)
      {
        E moved = PriorityQueue.this.removeAt(this.lastRet);
        this.lastRet = -1;
        if (moved == null)
        {
          this.cursor -= 1;
        }
        else
        {
          if (this.forgetMeNot == null) {
            this.forgetMeNot = new ArrayDeque();
          }
          this.forgetMeNot.add(moved);
        }
      }
      else if (this.lastRetElt != null)
      {
        PriorityQueue.this.removeEq(this.lastRetElt);
        this.lastRetElt = null;
      }
      else
      {
        throw new IllegalStateException();
      }
      this.expectedModCount = PriorityQueue.this.modCount;
    }
  }
  
  public int size()
  {
    return this.size;
  }
  
  public void clear()
  {
    this.modCount += 1;
    for (int i = 0; i < this.size; i++) {
      this.queue[i] = null;
    }
    this.size = 0;
  }
  
  public E poll()
  {
    if (this.size == 0) {
      return null;
    }
    int s = --this.size;
    this.modCount += 1;
    E result = this.queue[0];
    E x = this.queue[s];
    this.queue[s] = null;
    if (s != 0) {
      siftDown(0, x);
    }
    return result;
  }
  
  E removeAt(int i)
  {
    this.modCount += 1;
    int s = --this.size;
    if (s == i)
    {
      this.queue[i] = null;
    }
    else
    {
      E moved = this.queue[s];
      this.queue[s] = null;
      siftDown(i, moved);
      if (this.queue[i] == moved)
      {
        siftUp(i, moved);
        if (this.queue[i] != moved) {
          return moved;
        }
      }
    }
    return null;
  }
  
  private void siftUp(int k, E x)
  {
    if (this.comparator != null) {
      siftUpUsingComparator(k, x);
    } else {
      siftUpComparable(k, x);
    }
  }
  
  private void siftUpComparable(int k, E x)
  {
    Comparable<? super E> key = (Comparable)x;
    while (k > 0)
    {
      int parent = k - 1 >>> 1;
      Object e = this.queue[parent];
      if (key.compareTo(e) >= 0) {
        break;
      }
      this.queue[k] = e;
      k = parent;
    }
    this.queue[k] = key;
  }
  
  private void siftUpUsingComparator(int k, E x)
  {
    while (k > 0)
    {
      int parent = k - 1 >>> 1;
      Object e = this.queue[parent];
      if (this.comparator.compare(x, e) >= 0) {
        break;
      }
      this.queue[k] = e;
      k = parent;
    }
    this.queue[k] = x;
  }
  
  private void siftDown(int k, E x)
  {
    if (this.comparator != null) {
      siftDownUsingComparator(k, x);
    } else {
      siftDownComparable(k, x);
    }
  }
  
  private void siftDownComparable(int k, E x)
  {
    Comparable<? super E> key = (Comparable)x;
    int half = this.size >>> 1;
    while (k < half)
    {
      int child = (k << 1) + 1;
      Object c = this.queue[child];
      int right = child + 1;
      if ((right < this.size) && 
        (((Comparable)c).compareTo(this.queue[right]) > 0)) {
        c = this.queue[(child = right)];
      }
      if (key.compareTo(c) <= 0) {
        break;
      }
      this.queue[k] = c;
      k = child;
    }
    this.queue[k] = key;
  }
  
  private void siftDownUsingComparator(int k, E x)
  {
    int half = this.size >>> 1;
    while (k < half)
    {
      int child = (k << 1) + 1;
      Object c = this.queue[child];
      int right = child + 1;
      if ((right < this.size) && 
        (this.comparator.compare(c, this.queue[right]) > 0)) {
        c = this.queue[(child = right)];
      }
      if (this.comparator.compare(x, c) <= 0) {
        break;
      }
      this.queue[k] = c;
      k = child;
    }
    this.queue[k] = x;
  }
  
  private void heapify()
  {
    Object[] es = this.queue;
    int i = (this.size >>> 1) - 1;
    if (this.comparator == null) {
      for (; i >= 0; i--) {
        siftDownComparable(i, es[i]);
      }
    }
    for (; i >= 0; i--) {
      siftDownUsingComparator(i, es[i]);
    }
  }
  
  public Comparator<? super E> comparator()
  {
    return this.comparator;
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    s.defaultWriteObject();
    

    s.writeInt(Math.max(2, this.size + 1));
    for (int i = 0; i < this.size; i++) {
      s.writeObject(this.queue[i]);
    }
  }
  
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    

    s.readInt();
    
    SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, [Ljava.lang.Object.class, this.size);
    this.queue = new Object[this.size];
    for (int i = 0; i < this.size; i++) {
      this.queue[i] = s.readObject();
    }
    heapify();
  }
  
  public final Spliterator<E> spliterator()
  {
    return new PriorityQueueSpliterator(0, -1, 0);
  }
  
  final class PriorityQueueSpliterator
    implements Spliterator<E>
  {
    private int index;
    private int fence;
    private int expectedModCount;
    
    PriorityQueueSpliterator(int origin, int fence, int expectedModCount)
    {
      this.index = origin;
      this.fence = fence;
      this.expectedModCount = expectedModCount;
    }
    
    private int getFence()
    {
      int hi;
      if ((hi = this.fence) < 0)
      {
        this.expectedModCount = PriorityQueue.this.modCount;
        hi = this.fence = PriorityQueue.this.size;
      }
      return hi;
    }
    
    public PriorityQueue<E>.PriorityQueueSpliterator trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return lo >= mid ? null : 
        new PriorityQueueSpliterator(PriorityQueue.this, lo, this.index = mid, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super E> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.fence < 0)
      {
        this.fence = PriorityQueue.this.size;this.expectedModCount = PriorityQueue.this.modCount;
      }
      Object[] a = PriorityQueue.this.queue;
      
      int i = this.index;
      int hi;
      for (this.index = (hi = this.fence); i < hi; i++)
      {
        E e;
        if ((e = a[i]) == null) {
          break;
        }
        action.accept(e);
      }
      if (PriorityQueue.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
    
    public boolean tryAdvance(Consumer<? super E> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.fence < 0)
      {
        this.fence = PriorityQueue.this.size;this.expectedModCount = PriorityQueue.this.modCount;
      }
      int i;
      if ((i = this.index) < this.fence)
      {
        this.index = (i + 1);
        E e;
        if (((e = PriorityQueue.this.queue[i]) == null) || (PriorityQueue.this.modCount != this.expectedModCount)) {
          throw new ConcurrentModificationException();
        }
        action.accept(e);
        return true;
      }
      return false;
    }
    
    public long estimateSize()
    {
      return getFence() - this.index;
    }
    
    public int characteristics()
    {
      return 16704;
    }
  }
}
