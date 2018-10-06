package java.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class WeakHashMap<K, V>   extends AbstractMap<K, V>   implements Map<K, V> {
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  private static final int MAXIMUM_CAPACITY = 1073741824;
  private static final float DEFAULT_LOAD_FACTOR = 0.75F;
  Entry<K, V>[] table;
  private int size;
  private int threshold;
  private final float loadFactor;
  private final ReferenceQueue<Object> queue = new ReferenceQueue();
  int modCount;
  
  private Entry<K, V>[] newTable(int n)
  {
    return new Entry[n];
  }
  
  public WeakHashMap(int initialCapacity, float loadFactor)
  {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal Initial Capacity: " + initialCapacity);
    }
    if (initialCapacity > 1073741824) {
      initialCapacity = 1073741824;
    }
    if ((loadFactor <= 0.0F) || (Float.isNaN(loadFactor))) {
      throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
    }
    int capacity = 1;
    while (capacity < initialCapacity) {
      capacity <<= 1;
    }
    this.table = newTable(capacity);
    this.loadFactor = loadFactor;
    this.threshold = ((int)(capacity * loadFactor));
  }
  
  public WeakHashMap(int initialCapacity)
  {
    this(initialCapacity, 0.75F);
  }
  
  public WeakHashMap()
  {
    this(16, 0.75F);
  }
  
  public WeakHashMap(Map<? extends K, ? extends V> m)
  {
    this(Math.max((int)(m.size() / 0.75F) + 1, 16), 0.75F);
    

    putAll(m);
  }
  
  private static final Object NULL_KEY = new Object();
  private transient Set<Map.Entry<K, V>> entrySet;
  
  private static Object maskNull(Object key)
  {
    return key == null ? NULL_KEY : key;
  }
  
  static Object unmaskNull(Object key)
  {
    return key == NULL_KEY ? null : key;
  }
  
  private static boolean eq(Object x, Object y)
  {
    return (x == y) || (x.equals(y));
  }
  
  final int hash(Object k)
  {
    int h = k.hashCode();
    



    h ^= h >>> 20 ^ h >>> 12;
    return h ^ h >>> 7 ^ h >>> 4;
  }
  
  private static int indexFor(int h, int length)
  {
    return h & length - 1;
  }
  
  private void expungeStaleEntries()
  {
    Object x;
    while ((x = this.queue.poll()) != null) {
      synchronized (this.queue)
      {
        Entry<K, V> e = (Entry)x;
        int i = indexFor(e.hash, this.table.length);
        
        Entry<K, V> prev = this.table[i];
        Entry<K, V> p = prev;
        while (p != null)
        {
          Entry<K, V> next = p.next;
          if (p == e)
          {
            if (prev == e) {
              this.table[i] = next;
            } else {
              prev.next = next;
            }
            e.value = null;
            this.size -= 1;
            break;
          }
          prev = p;
          p = next;
        }
      }
    }
  }
  
  private Entry<K, V>[] getTable()
  {
    expungeStaleEntries();
    return this.table;
  }
  
  public int size()
  {
    if (this.size == 0) {
      return 0;
    }
    expungeStaleEntries();
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public V get(Object key)
  {
    Object k = maskNull(key);
    int h = hash(k);
    Entry<K, V>[] tab = getTable();
    int index = indexFor(h, tab.length);
    Entry<K, V> e = tab[index];
    while (e != null)
    {
      if ((e.hash == h) && (eq(k, e.get()))) {
        return e.value;
      }
      e = e.next;
    }
    return null;
  }
  
  public boolean containsKey(Object key)
  {
    return getEntry(key) != null;
  }
  
  Entry<K, V> getEntry(Object key)
  {
    Object k = maskNull(key);
    int h = hash(k);
    Entry<K, V>[] tab = getTable();
    int index = indexFor(h, tab.length);
    Entry<K, V> e = tab[index];
    while ((e != null) && ((e.hash != h) || (!eq(k, e.get())))) {
      e = e.next;
    }
    return e;
  }
  
  public V put(K key, V value)
  {
    Object k = maskNull(key);
    int h = hash(k);
    Entry<K, V>[] tab = getTable();
    int i = indexFor(h, tab.length);
    for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
      if ((h == e.hash) && (eq(k, e.get())))
      {
        V oldValue = e.value;
        if (value != oldValue) {
          e.value = value;
        }
        return oldValue;
      }
    }
    this.modCount += 1;
    Entry<K, V> e = tab[i];
    tab[i] = new Entry(k, value, this.queue, h, e);
    if (++this.size >= this.threshold) {
      resize(tab.length * 2);
    }
    return null;
  }
  
  void resize(int newCapacity)
  {
    Entry<K, V>[] oldTable = getTable();
    int oldCapacity = oldTable.length;
    if (oldCapacity == 1073741824)
    {
      this.threshold = 2147483647;
      return;
    }
    Entry<K, V>[] newTable = newTable(newCapacity);
    transfer(oldTable, newTable);
    this.table = newTable;
    if (this.size >= this.threshold / 2)
    {
      this.threshold = ((int)(newCapacity * this.loadFactor));
    }
    else
    {
      expungeStaleEntries();
      transfer(newTable, oldTable);
      this.table = oldTable;
    }
  }
  
  private void transfer(Entry<K, V>[] src, Entry<K, V>[] dest)
  {
    for (int j = 0; j < src.length; j++)
    {
      Entry<K, V> e = src[j];
      src[j] = null;
      while (e != null)
      {
        Entry<K, V> next = e.next;
        Object key = e.get();
        if (key == null)
        {
          e.next = null;
          e.value = null;
          this.size -= 1;
        }
        else
        {
          int i = indexFor(e.hash, dest.length);
          e.next = dest[i];
          dest[i] = e;
        }
        e = next;
      }
    }
  }
  
  public void putAll(Map<? extends K, ? extends V> m)
  {
    int numKeysToBeAdded = m.size();
    if (numKeysToBeAdded == 0) {
      return;
    }
    int targetCapacity;
    if (numKeysToBeAdded > this.threshold)
    {
      targetCapacity = (int)(numKeysToBeAdded / this.loadFactor + 1.0F);
      if (targetCapacity > 1073741824) {
        targetCapacity = 1073741824;
      }
      int newCapacity = this.table.length;
      while (newCapacity < targetCapacity) {
        newCapacity <<= 1;
      }
      if (newCapacity > this.table.length) {
        resize(newCapacity);
      }
    }
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  public V remove(Object key)
  {
    Object k = maskNull(key);
    int h = hash(k);
    Entry<K, V>[] tab = getTable();
    int i = indexFor(h, tab.length);
    Entry<K, V> prev = tab[i];
    Entry<K, V> e = prev;
    while (e != null)
    {
      Entry<K, V> next = e.next;
      if ((h == e.hash) && (eq(k, e.get())))
      {
        this.modCount += 1;
        this.size -= 1;
        if (prev == e) {
          tab[i] = next;
        } else {
          prev.next = next;
        }
        return e.value;
      }
      prev = e;
      e = next;
    }
    return null;
  }
  
  boolean removeMapping(Object o)
  {
    if (!(o instanceof Map.Entry)) {
      return false;
    }
    Entry<K, V>[] tab = getTable();
    Map.Entry<?, ?> entry = (Map.Entry)o;
    Object k = maskNull(entry.getKey());
    int h = hash(k);
    int i = indexFor(h, tab.length);
    Entry<K, V> prev = tab[i];
    Entry<K, V> e = prev;
    while (e != null)
    {
      Entry<K, V> next = e.next;
      if ((h == e.hash) && (e.equals(entry)))
      {
        this.modCount += 1;
        this.size -= 1;
        if (prev == e) {
          tab[i] = next;
        } else {
          prev.next = next;
        }
        return true;
      }
      prev = e;
      e = next;
    }
    return false;
  }
  
  public void clear()
  {
    while (this.queue.poll() != null) {}
    this.modCount += 1;
    Arrays.fill(this.table, null);
    this.size = 0;
    while (this.queue.poll() != null) {}
  }
  
  public boolean containsValue(Object value)
  {
    if (value == null) {
      return containsNullValue();
    }
    Entry<K, V>[] tab = getTable();
    for (int i = tab.length; i-- > 0;) {
      for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
        if (value.equals(e.value)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean containsNullValue()
  {
    Entry<K, V>[] tab = getTable();
    for (int i = tab.length; i-- > 0;) {
      for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
        if (e.value == null) {
          return true;
        }
      }
    }
    return false;
  }
  
  private static class Entry<K, V>
    extends WeakReference<Object>
    implements Map.Entry<K, V>
  {
    V value;
    final int hash;
    Entry<K, V> next;
    
    Entry(Object key, V value, ReferenceQueue<Object> queue, int hash, Entry<K, V> next)
    {
      super(queue);
      this.value = value;
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey()
    {
      return WeakHashMap.unmaskNull(get());
    }
    
    public V getValue()
    {
      return this.value;
    }
    
    public V setValue(V newValue)
    {
      V oldValue = this.value;
      this.value = newValue;
      return oldValue;
    }
    
    public boolean equals(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      K k1 = getKey();
      Object k2 = e.getKey();
      if ((k1 == k2) || ((k1 != null) && (k1.equals(k2))))
      {
        V v1 = getValue();
        Object v2 = e.getValue();
        if ((v1 == v2) || ((v1 != null) && (v1.equals(v2)))) {
          return true;
        }
      }
      return false;
    }
    
    public int hashCode()
    {
      K k = getKey();
      V v = getValue();
      return Objects.hashCode(k) ^ Objects.hashCode(v);
    }
    
    public String toString()
    {
      return getKey() + "=" + getValue();
    }
  }
  
  private abstract class HashIterator<T>
    implements Iterator<T>
  {
    private int index;
    private WeakHashMap.Entry<K, V> entry;
    private WeakHashMap.Entry<K, V> lastReturned;
    private int expectedModCount = WeakHashMap.this.modCount;
    private Object nextKey;
    private Object currentKey;
    
    HashIterator()
    {
      this.index = (WeakHashMap.this.isEmpty() ? 0 : WeakHashMap.this.table.length);
    }
    
    public boolean hasNext()
    {
      WeakHashMap.Entry<K, V>[] t = WeakHashMap.this.table;
      while (this.nextKey == null)
      {
        WeakHashMap.Entry<K, V> e = this.entry;
        int i = this.index;
        while ((e == null) && (i > 0)) {
          e = t[(--i)];
        }
        this.entry = e;
        this.index = i;
        if (e == null)
        {
          this.currentKey = null;
          return false;
        }
        this.nextKey = e.get();
        if (this.nextKey == null) {
          this.entry = this.entry.next;
        }
      }
      return true;
    }
    
    protected WeakHashMap.Entry<K, V> nextEntry()
    {
      if (WeakHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if ((this.nextKey == null) && (!hasNext())) {
        throw new NoSuchElementException();
      }
      this.lastReturned = this.entry;
      this.entry = this.entry.next;
      this.currentKey = this.nextKey;
      this.nextKey = null;
      return this.lastReturned;
    }
    
    public void remove()
    {
      if (this.lastReturned == null) {
        throw new IllegalStateException();
      }
      if (WeakHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      WeakHashMap.this.remove(this.currentKey);
      this.expectedModCount = WeakHashMap.this.modCount;
      this.lastReturned = null;
      this.currentKey = null;
    }
  }
  
  private class ValueIterator
    extends WeakHashMap<K, V>.HashIterator<V>
  {
    private ValueIterator()
    {
      super();
    }
    
    public V next()
    {
      return nextEntry().value;
    }
  }
  
  private class KeyIterator
    extends WeakHashMap<K, V>.HashIterator<K>
  {
    private KeyIterator()
    {
      super();
    }
    
    public K next()
    {
      return nextEntry().getKey();
    }
  }
  
  private class EntryIterator
    extends WeakHashMap<K, V>.HashIterator<Map.Entry<K, V>>
  {
    private EntryIterator()
    {
      super();
    }
    
    public Map.Entry<K, V> next()
    {
      return nextEntry();
    }
  }
  
  public Set<K> keySet()
  {
    Set<K> ks = this.keySet;
    if (ks == null)
    {
      ks = new KeySet(null);
      this.keySet = ks;
    }
    return ks;
  }
  
  private class KeySet
    extends AbstractSet<K>
  {
    private KeySet() {}
    
    public Iterator<K> iterator()
    {
      return new WeakHashMap.KeyIterator(WeakHashMap.this, null);
    }
    
    public int size()
    {
      return WeakHashMap.this.size();
    }
    
    public boolean contains(Object o)
    {
      return WeakHashMap.this.containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      if (WeakHashMap.this.containsKey(o))
      {
        WeakHashMap.this.remove(o);
        return true;
      }
      return false;
    }
    
    public void clear()
    {
      WeakHashMap.this.clear();
    }
    
    public Spliterator<K> spliterator()
    {
      return new WeakHashMap.KeySpliterator(WeakHashMap.this, 0, -1, 0, 0);
    }
  }
  
  public Collection<V> values()
  {
    Collection<V> vs = this.values;
    if (vs == null)
    {
      vs = new Values(null);
      this.values = vs;
    }
    return vs;
  }
  
  private class Values
    extends AbstractCollection<V>
  {
    private Values() {}
    
    public Iterator<V> iterator()
    {
      return new WeakHashMap.ValueIterator(WeakHashMap.this, null);
    }
    
    public int size()
    {
      return WeakHashMap.this.size();
    }
    
    public boolean contains(Object o)
    {
      return WeakHashMap.this.containsValue(o);
    }
    
    public void clear()
    {
      WeakHashMap.this.clear();
    }
    
    public Spliterator<V> spliterator()
    {
      return new WeakHashMap.ValueSpliterator(WeakHashMap.this, 0, -1, 0, 0);
    }
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es = this.entrySet;
    return this.entrySet = new EntrySet(null);
  }
  
  private class EntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    private EntrySet() {}
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new WeakHashMap.EntryIterator(WeakHashMap.this, null);
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      WeakHashMap.Entry<K, V> candidate = WeakHashMap.this.getEntry(e.getKey());
      return (candidate != null) && (candidate.equals(e));
    }
    
    public boolean remove(Object o)
    {
      return WeakHashMap.this.removeMapping(o);
    }
    
    public int size()
    {
      return WeakHashMap.this.size();
    }
    
    public void clear()
    {
      WeakHashMap.this.clear();
    }
    
    private List<Map.Entry<K, V>> deepCopy()
    {
      List<Map.Entry<K, V>> list = new ArrayList(size());
      for (Map.Entry<K, V> e : this) {
        list.add(new AbstractMap.SimpleEntry(e));
      }
      return list;
    }
    
    public Object[] toArray()
    {
      return deepCopy().toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return deepCopy().toArray(a);
    }
    
    public Spliterator<Map.Entry<K, V>> spliterator()
    {
      return new WeakHashMap.EntrySpliterator(WeakHashMap.this, 0, -1, 0, 0);
    }
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    Objects.requireNonNull(action);
    int expectedModCount = this.modCount;
    
    Entry<K, V>[] tab = getTable();
    for (Entry<K, V> entry : tab) {
      while (entry != null)
      {
        Object key = entry.get();
        if (key != null) {
          action.accept(unmaskNull(key), entry.value);
        }
        entry = entry.next;
        if (expectedModCount != this.modCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    Objects.requireNonNull(function);
    int expectedModCount = this.modCount;
    
    Entry<K, V>[] tab = getTable();
    for (Entry<K, V> entry : tab) {
      while (entry != null)
      {
        Object key = entry.get();
        if (key != null) {
          entry.value = function.apply(unmaskNull(key), entry.value);
        }
        entry = entry.next;
        if (expectedModCount != this.modCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  static class WeakHashMapSpliterator<K, V>
  {
    final WeakHashMap<K, V> map;
    WeakHashMap.Entry<K, V> current;
    int index;
    int fence;
    int est;
    int expectedModCount;
    
    WeakHashMapSpliterator(WeakHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      this.map = m;
      this.index = origin;
      this.fence = fence;
      this.est = est;
      this.expectedModCount = expectedModCount;
    }
    
    final int getFence()
    {
      int hi;
      if ((hi = this.fence) < 0)
      {
        WeakHashMap<K, V> m = this.map;
        this.est = m.size();
        this.expectedModCount = m.modCount;
        hi = this.fence = m.table.length;
      }
      return hi;
    }
    
    public final long estimateSize()
    {
      getFence();
      return this.est;
    }
  }
  
  static final class KeySpliterator<K, V>
    extends WeakHashMap.WeakHashMapSpliterator<K, V>
    implements Spliterator<K>
  {
    KeySpliterator(WeakHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public KeySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return lo >= mid ? null : 
        new KeySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap<K, V> m = this.map;
      WeakHashMap.Entry<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        WeakHashMap.Entry<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            Object x = p.get();
            p = p.next;
            if (x != null)
            {
              K k = WeakHashMap.unmaskNull(x);
              action.accept(k);
            }
          }
        } while ((p != null) || (i < hi));
      }
      if (m.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
    
    public boolean tryAdvance(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap.Entry<K, V>[] tab = this.map.table;
      int hi;
      if ((tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            Object x = this.current.get();
            this.current = this.current.next;
            if (x != null)
            {
              K k = WeakHashMap.unmaskNull(x);
              action.accept(k);
              if (this.map.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
              }
              return true;
            }
          }
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return 1;
    }
  }
  
  static final class ValueSpliterator<K, V>
    extends WeakHashMap.WeakHashMapSpliterator<K, V>
    implements Spliterator<V>
  {
    ValueSpliterator(WeakHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public ValueSpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return lo >= mid ? null : 
        new ValueSpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap<K, V> m = this.map;
      WeakHashMap.Entry<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        WeakHashMap.Entry<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            Object x = p.get();
            V v = p.value;
            p = p.next;
            if (x != null) {
              action.accept(v);
            }
          }
        } while ((p != null) || (i < hi));
      }
      if (m.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
    
    public boolean tryAdvance(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap.Entry<K, V>[] tab = this.map.table;
      int hi;
      if ((tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            Object x = this.current.get();
            V v = this.current.value;
            this.current = this.current.next;
            if (x != null)
            {
              action.accept(v);
              if (this.map.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
              }
              return true;
            }
          }
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return 0;
    }
  }
  
  static final class EntrySpliterator<K, V>
    extends WeakHashMap.WeakHashMapSpliterator<K, V>
    implements Spliterator<Map.Entry<K, V>>
  {
    EntrySpliterator(WeakHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public EntrySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return lo >= mid ? null : 
        new EntrySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap<K, V> m = this.map;
      WeakHashMap.Entry<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        WeakHashMap.Entry<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            Object x = p.get();
            V v = p.value;
            p = p.next;
            if (x != null)
            {
              K k = WeakHashMap.unmaskNull(x);
              action
                .accept(new AbstractMap.SimpleImmutableEntry(k, v));
            }
          }
        } while ((p != null) || (i < hi));
      }
      if (m.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
    
    public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      WeakHashMap.Entry<K, V>[] tab = this.map.table;
      int hi;
      if ((tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            Object x = this.current.get();
            V v = this.current.value;
            this.current = this.current.next;
            if (x != null)
            {
              K k = WeakHashMap.unmaskNull(x);
              action
                .accept(new AbstractMap.SimpleImmutableEntry(k, v));
              if (this.map.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
              }
              return true;
            }
          }
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return 1;
    }
  }
}
