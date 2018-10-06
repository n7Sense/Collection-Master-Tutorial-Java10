package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TreeMap<K, V>   extends AbstractMap<K, V>   implements NavigableMap<K, V>, Cloneable, Serializable
{
  private final Comparator<? super K> comparator;
  private transient Entry<K, V> root;
  private transient int size = 0;
  private transient int modCount = 0;
  private transient TreeMap<K, V>.EntrySet entrySet;
  private transient KeySet<K> navigableKeySet;
  private transient NavigableMap<K, V> descendingMap;
  
  public TreeMap()
  {
    this.comparator = null;
  }
  
  public TreeMap(Comparator<? super K> comparator)
  {
    this.comparator = comparator;
  }
  
  public TreeMap(Map<? extends K, ? extends V> m)
  {
    this.comparator = null;
    putAll(m);
  }
  
  public TreeMap(SortedMap<K, ? extends V> m)
  {
    this.comparator = m.comparator();
    try
    {
      buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
    }
    catch (IOException|ClassNotFoundException localIOException) {}
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean containsKey(Object key)
  {
    return getEntry(key) != null;
  }
  
  public boolean containsValue(Object value)
  {
    for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e)) {
      if (valEquals(value, e.value)) {
        return true;
      }
    }
    return false;
  }
  
  public V get(Object key)
  {
    Entry<K, V> p = getEntry(key);
    return p == null ? null : p.value;
  }
  
  public Comparator<? super K> comparator()
  {
    return this.comparator;
  }
  
  public K firstKey()
  {
    return key(getFirstEntry());
  }
  
  public K lastKey()
  {
    return key(getLastEntry());
  }
  
  public void putAll(Map<? extends K, ? extends V> map)
  {
    int mapSize = map.size();
    if ((this.size == 0) && (mapSize != 0) && ((map instanceof SortedMap)))
    {
      Comparator<?> c = ((SortedMap)map).comparator();
      if ((c == this.comparator) || ((c != null) && (c.equals(this.comparator))))
      {
        this.modCount += 1;
        try
        {
          buildFromSorted(mapSize, map.entrySet().iterator(), null, null);
        }
        catch (IOException|ClassNotFoundException localIOException) {}
        return;
      }
    }
    super.putAll(map);
  }
  
  final Entry<K, V> getEntry(Object key)
  {
    if (this.comparator != null) {
      return getEntryUsingComparator(key);
    }
    if (key == null) {
      throw new NullPointerException();
    }
    Comparable<? super K> k = (Comparable)key;
    Entry<K, V> p = this.root;
    while (p != null)
    {
      int cmp = k.compareTo(p.key);
      if (cmp < 0) {
        p = p.left;
      } else if (cmp > 0) {
        p = p.right;
      } else {
        return p;
      }
    }
    return null;
  }
  
  final Entry<K, V> getEntryUsingComparator(Object key)
  {
    K k = key;
    Comparator<? super K> cpr = this.comparator;
    if (cpr != null)
    {
      Entry<K, V> p = this.root;
      while (p != null)
      {
        int cmp = cpr.compare(k, p.key);
        if (cmp < 0) {
          p = p.left;
        } else if (cmp > 0) {
          p = p.right;
        } else {
          return p;
        }
      }
    }
    return null;
  }
  
  final Entry<K, V> getCeilingEntry(K key)
  {
    Entry<K, V> p = this.root;
    while (p != null)
    {
      int cmp = compare(key, p.key);
      if (cmp < 0)
      {
        if (p.left != null) {
          p = p.left;
        } else {
          return p;
        }
      }
      else if (cmp > 0)
      {
        if (p.right != null)
        {
          p = p.right;
        }
        else
        {
          Entry<K, V> parent = p.parent;
          Entry<K, V> ch = p;
          while ((parent != null) && (ch == parent.right))
          {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      }
      else {
        return p;
      }
    }
    return null;
  }
  
  final Entry<K, V> getFloorEntry(K key)
  {
    Entry<K, V> p = this.root;
    while (p != null)
    {
      int cmp = compare(key, p.key);
      if (cmp > 0)
      {
        if (p.right != null) {
          p = p.right;
        } else {
          return p;
        }
      }
      else if (cmp < 0)
      {
        if (p.left != null)
        {
          p = p.left;
        }
        else
        {
          Entry<K, V> parent = p.parent;
          Entry<K, V> ch = p;
          while ((parent != null) && (ch == parent.left))
          {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      }
      else {
        return p;
      }
    }
    return null;
  }
  
  final Entry<K, V> getHigherEntry(K key)
  {
    Entry<K, V> p = this.root;
    while (p != null)
    {
      int cmp = compare(key, p.key);
      if (cmp < 0)
      {
        if (p.left != null) {
          p = p.left;
        } else {
          return p;
        }
      }
      else if (p.right != null)
      {
        p = p.right;
      }
      else
      {
        Entry<K, V> parent = p.parent;
        Entry<K, V> ch = p;
        while ((parent != null) && (ch == parent.right))
        {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
    }
    return null;
  }
  
  final Entry<K, V> getLowerEntry(K key)
  {
    Entry<K, V> p = this.root;
    while (p != null)
    {
      int cmp = compare(key, p.key);
      if (cmp > 0)
      {
        if (p.right != null) {
          p = p.right;
        } else {
          return p;
        }
      }
      else if (p.left != null)
      {
        p = p.left;
      }
      else
      {
        Entry<K, V> parent = p.parent;
        Entry<K, V> ch = p;
        while ((parent != null) && (ch == parent.left))
        {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
    }
    return null;
  }
  
  public V put(K key, V value)
  {
    Entry<K, V> t = this.root;
    if (t == null)
    {
      compare(key, key);
      
      this.root = new Entry(key, value, null);
      this.size = 1;
      this.modCount += 1;
      return null;
    }
    Comparator<? super K> cpr = this.comparator;
    Entry<K, V> parent;
    int cmp;
    if (cpr != null)
    {
      do
      {
        Entry<K, V> parent = t;
        int cmp = cpr.compare(key, t.key);
        if (cmp < 0) {
          t = t.left;
        } else if (cmp > 0) {
          t = t.right;
        } else {
          return t.setValue(value);
        }
      } while (t != null);
    }
    else
    {
      if (key == null) {
        throw new NullPointerException();
      }
      Comparable<? super K> k = (Comparable)key;
      do
      {
        parent = t;
        cmp = k.compareTo(t.key);
        if (cmp < 0) {
          t = t.left;
        } else if (cmp > 0) {
          t = t.right;
        } else {
          return t.setValue(value);
        }
      } while (t != null);
    }
    Entry<K, V> e = new Entry(key, value, parent);
    if (cmp < 0) {
      parent.left = e;
    } else {
      parent.right = e;
    }
    fixAfterInsertion(e);
    this.size += 1;
    this.modCount += 1;
    return null;
  }
  
  public V remove(Object key)
  {
    Entry<K, V> p = getEntry(key);
    if (p == null) {
      return null;
    }
    V oldValue = p.value;
    deleteEntry(p);
    return oldValue;
  }
  
  public void clear()
  {
    this.modCount += 1;
    this.size = 0;
    this.root = null;
  }
  
  public Object clone()
  {
    try
    {
      clone = (TreeMap)super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      TreeMap<?, ?> clone;
      throw new InternalError(e);
    }
    TreeMap<?, ?> clone;
    clone.root = null;
    clone.size = 0;
    clone.modCount = 0;
    clone.entrySet = null;
    clone.navigableKeySet = null;
    clone.descendingMap = null;
    try
    {
      clone.buildFromSorted(this.size, entrySet().iterator(), null, null);
    }
    catch (IOException|ClassNotFoundException localIOException) {}
    return clone;
  }
  
  public Map.Entry<K, V> firstEntry()
  {
    return exportEntry(getFirstEntry());
  }
  
  public Map.Entry<K, V> lastEntry()
  {
    return exportEntry(getLastEntry());
  }
  
  public Map.Entry<K, V> pollFirstEntry()
  {
    Entry<K, V> p = getFirstEntry();
    Map.Entry<K, V> result = exportEntry(p);
    if (p != null) {
      deleteEntry(p);
    }
    return result;
  }
  
  public Map.Entry<K, V> pollLastEntry()
  {
    Entry<K, V> p = getLastEntry();
    Map.Entry<K, V> result = exportEntry(p);
    if (p != null) {
      deleteEntry(p);
    }
    return result;
  }
  
  public Map.Entry<K, V> lowerEntry(K key)
  {
    return exportEntry(getLowerEntry(key));
  }
  
  public K lowerKey(K key)
  {
    return keyOrNull(getLowerEntry(key));
  }
  
  public Map.Entry<K, V> floorEntry(K key)
  {
    return exportEntry(getFloorEntry(key));
  }
  
  public K floorKey(K key)
  {
    return keyOrNull(getFloorEntry(key));
  }
  
  public Map.Entry<K, V> ceilingEntry(K key)
  {
    return exportEntry(getCeilingEntry(key));
  }
  
  public K ceilingKey(K key)
  {
    return keyOrNull(getCeilingEntry(key));
  }
  
  public Map.Entry<K, V> higherEntry(K key)
  {
    return exportEntry(getHigherEntry(key));
  }
  
  public K higherKey(K key)
  {
    return keyOrNull(getHigherEntry(key));
  }
  
  public Set<K> keySet()
  {
    return navigableKeySet();
  }
  
  public NavigableSet<K> navigableKeySet()
  {
    KeySet<K> nks = this.navigableKeySet;
    return this.navigableKeySet = new KeySet(this);
  }
  
  public NavigableSet<K> descendingKeySet()
  {
    return descendingMap().navigableKeySet();
  }
  
  public Collection<V> values()
  {
    Collection<V> vs = this.values;
    if (vs == null)
    {
      vs = new Values();
      this.values = vs;
    }
    return vs;
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    TreeMap<K, V>.EntrySet es = this.entrySet;
    return this.entrySet = new EntrySet();
  }
  
  public NavigableMap<K, V> descendingMap()
  {
    NavigableMap<K, V> km = this.descendingMap;
    return 
      this.descendingMap = new DescendingSubMap(this, true, null, true, true, null, true);
  }
  
  public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
  {
    return new AscendingSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
  }
  
  public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
  {
    return new AscendingSubMap(this, true, null, true, false, toKey, inclusive);
  }
  
  public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
  {
    return new AscendingSubMap(this, false, fromKey, inclusive, true, null, true);
  }
  
  public SortedMap<K, V> subMap(K fromKey, K toKey)
  {
    return subMap(fromKey, true, toKey, false);
  }
  
  public SortedMap<K, V> headMap(K toKey)
  {
    return headMap(toKey, false);
  }
  
  public SortedMap<K, V> tailMap(K fromKey)
  {
    return tailMap(fromKey, true);
  }
  
  public boolean replace(K key, V oldValue, V newValue)
  {
    Entry<K, V> p = getEntry(key);
    if ((p != null) && (Objects.equals(oldValue, p.value)))
    {
      p.value = newValue;
      return true;
    }
    return false;
  }
  
  public V replace(K key, V value)
  {
    Entry<K, V> p = getEntry(key);
    if (p != null)
    {
      V oldValue = p.value;
      p.value = value;
      return oldValue;
    }
    return null;
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    Objects.requireNonNull(action);
    int expectedModCount = this.modCount;
    for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e))
    {
      action.accept(e.key, e.value);
      if (expectedModCount != this.modCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    Objects.requireNonNull(function);
    int expectedModCount = this.modCount;
    for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e))
    {
      e.value = function.apply(e.key, e.value);
      if (expectedModCount != this.modCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  class Values
    extends AbstractCollection<V>
  {
    Values() {}
    
    public Iterator<V> iterator()
    {
      return new TreeMap.ValueIterator(TreeMap.this, TreeMap.this.getFirstEntry());
    }
    
    public int size()
    {
      return TreeMap.this.size();
    }
    
    public boolean contains(Object o)
    {
      return TreeMap.this.containsValue(o);
    }
    
    public boolean remove(Object o)
    {
      for (TreeMap.Entry<K, V> e = TreeMap.this.getFirstEntry(); e != null; e = TreeMap.successor(e)) {
        if (TreeMap.valEquals(e.getValue(), o))
        {
          TreeMap.this.deleteEntry(e);
          return true;
        }
      }
      return false;
    }
    
    public void clear()
    {
      TreeMap.this.clear();
    }
    
    public Spliterator<V> spliterator()
    {
      return new TreeMap.ValueSpliterator(TreeMap.this, null, null, 0, -1, 0);
    }
  }
  
  class EntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    EntrySet() {}
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new TreeMap.EntryIterator(TreeMap.this, TreeMap.this.getFirstEntry());
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      Object value = entry.getValue();
      TreeMap.Entry<K, V> p = TreeMap.this.getEntry(entry.getKey());
      return (p != null) && (TreeMap.valEquals(p.getValue(), value));
    }
    
    public boolean remove(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      Object value = entry.getValue();
      TreeMap.Entry<K, V> p = TreeMap.this.getEntry(entry.getKey());
      if ((p != null) && (TreeMap.valEquals(p.getValue(), value)))
      {
        TreeMap.this.deleteEntry(p);
        return true;
      }
      return false;
    }
    
    public int size()
    {
      return TreeMap.this.size();
    }
    
    public void clear()
    {
      TreeMap.this.clear();
    }
    
    public Spliterator<Map.Entry<K, V>> spliterator()
    {
      return new TreeMap.EntrySpliterator(TreeMap.this, null, null, 0, -1, 0);
    }
  }
  
  Iterator<K> keyIterator()
  {
    return new KeyIterator(getFirstEntry());
  }
  
  Iterator<K> descendingKeyIterator()
  {
    return new DescendingKeyIterator(getLastEntry());
  }
  
  static final class KeySet<E>
    extends AbstractSet<E>
    implements NavigableSet<E>
  {
    private final NavigableMap<E, ?> m;
    
    KeySet(NavigableMap<E, ?> map)
    {
      this.m = map;
    }
    
    public Iterator<E> iterator()
    {
      if ((this.m instanceof TreeMap)) {
        return ((TreeMap)this.m).keyIterator();
      }
      return ((TreeMap.NavigableSubMap)this.m).keyIterator();
    }
    
    public Iterator<E> descendingIterator()
    {
      if ((this.m instanceof TreeMap)) {
        return ((TreeMap)this.m).descendingKeyIterator();
      }
      return ((TreeMap.NavigableSubMap)this.m).descendingKeyIterator();
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
    
    public void clear()
    {
      this.m.clear();
    }
    
    public E lower(E e)
    {
      return this.m.lowerKey(e);
    }
    
    public E floor(E e)
    {
      return this.m.floorKey(e);
    }
    
    public E ceiling(E e)
    {
      return this.m.ceilingKey(e);
    }
    
    public E higher(E e)
    {
      return this.m.higherKey(e);
    }
    
    public E first()
    {
      return this.m.firstKey();
    }
    
    public E last()
    {
      return this.m.lastKey();
    }
    
    public Comparator<? super E> comparator()
    {
      return this.m.comparator();
    }
    
    public E pollFirst()
    {
      Map.Entry<E, ?> e = this.m.pollFirstEntry();
      return e == null ? null : e.getKey();
    }
    
    public E pollLast()
    {
      Map.Entry<E, ?> e = this.m.pollLastEntry();
      return e == null ? null : e.getKey();
    }
    
    public boolean remove(Object o)
    {
      int oldSize = size();
      this.m.remove(o);
      return size() != oldSize;
    }
    
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
    {
      return new KeySet(this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
    }
    
    public NavigableSet<E> headSet(E toElement, boolean inclusive)
    {
      return new KeySet(this.m.headMap(toElement, inclusive));
    }
    
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
    {
      return new KeySet(this.m.tailMap(fromElement, inclusive));
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement)
    {
      return subSet(fromElement, true, toElement, false);
    }
    
    public SortedSet<E> headSet(E toElement)
    {
      return headSet(toElement, false);
    }
    
    public SortedSet<E> tailSet(E fromElement)
    {
      return tailSet(fromElement, true);
    }
    
    public NavigableSet<E> descendingSet()
    {
      return new KeySet(this.m.descendingMap());
    }
    
    public Spliterator<E> spliterator()
    {
      return TreeMap.keySpliteratorFor(this.m);
    }
  }
  
  abstract class PrivateEntryIterator<T>
    implements Iterator<T>
  {
    TreeMap.Entry<K, V> next;
    TreeMap.Entry<K, V> lastReturned;
    int expectedModCount;
    
    PrivateEntryIterator()
    {
      this.expectedModCount = TreeMap.this.modCount;
      this.lastReturned = null;
      this.next = first;
    }
    
    public final boolean hasNext()
    {
      return this.next != null;
    }
    
    final TreeMap.Entry<K, V> nextEntry()
    {
      TreeMap.Entry<K, V> e = this.next;
      if (e == null) {
        throw new NoSuchElementException();
      }
      if (TreeMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.next = TreeMap.successor(e);
      this.lastReturned = e;
      return e;
    }
    
    final TreeMap.Entry<K, V> prevEntry()
    {
      TreeMap.Entry<K, V> e = this.next;
      if (e == null) {
        throw new NoSuchElementException();
      }
      if (TreeMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.next = TreeMap.predecessor(e);
      this.lastReturned = e;
      return e;
    }
    
    public void remove()
    {
      if (this.lastReturned == null) {
        throw new IllegalStateException();
      }
      if (TreeMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if ((this.lastReturned.left != null) && (this.lastReturned.right != null)) {
        this.next = this.lastReturned;
      }
      TreeMap.this.deleteEntry(this.lastReturned);
      this.expectedModCount = TreeMap.this.modCount;
      this.lastReturned = null;
    }
  }
  
  final class EntryIterator
    extends TreeMap<K, V>.PrivateEntryIterator<Map.Entry<K, V>>
  {
    EntryIterator()
    {
      super(first);
    }
    
    public Map.Entry<K, V> next()
    {
      return nextEntry();
    }
  }
  
  final class ValueIterator
    extends TreeMap<K, V>.PrivateEntryIterator<V>
  {
    ValueIterator()
    {
      super(first);
    }
    
    public V next()
    {
      return nextEntry().value;
    }
  }
  
  final class KeyIterator
    extends TreeMap<K, V>.PrivateEntryIterator<K>
  {
    KeyIterator()
    {
      super(first);
    }
    
    public K next()
    {
      return nextEntry().key;
    }
  }
  
  final class DescendingKeyIterator
    extends TreeMap<K, V>.PrivateEntryIterator<K>
  {
    DescendingKeyIterator()
    {
      super(first);
    }
    
    public K next()
    {
      return prevEntry().key;
    }
    
    public void remove()
    {
      if (this.lastReturned == null) {
        throw new IllegalStateException();
      }
      if (TreeMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      TreeMap.this.deleteEntry(this.lastReturned);
      this.lastReturned = null;
      this.expectedModCount = TreeMap.this.modCount;
    }
  }
  
  final int compare(Object k1, Object k2)
  {
    return this.comparator == null ? ((Comparable)k1).compareTo(k2) : 
      this.comparator.compare(k1, k2);
  }
  
  static final boolean valEquals(Object o1, Object o2)
  {
    return o1 == null ? false : o2 == null ? true : o1.equals(o2);
  }
  
  static <K, V> Map.Entry<K, V> exportEntry(Entry<K, V> e)
  {
    return e == null ? null : 
      new AbstractMap.SimpleImmutableEntry(e);
  }
  
  static <K, V> K keyOrNull(Entry<K, V> e)
  {
    return e == null ? null : e.key;
  }
  
  static <K> K key(Entry<K, ?> e)
  {
    if (e == null) {
      throw new NoSuchElementException();
    }
    return e.key;
  }
  
  private static final Object UNBOUNDED = new Object();
  private static final boolean RED = false;
  private static final boolean BLACK = true;
  private static final long serialVersionUID = 919286545866124006L;
  
  static abstract class NavigableSubMap<K, V>
    extends AbstractMap<K, V>
    implements NavigableMap<K, V>, Serializable
  {
    private static final long serialVersionUID = -2102997345730753016L;
    final TreeMap<K, V> m;
    final K lo;
    final K hi;
    final boolean fromStart;
    final boolean toEnd;
    final boolean loInclusive;
    final boolean hiInclusive;
    transient NavigableMap<K, V> descendingMapView;
    transient NavigableSubMap<K, V>.EntrySetView entrySetView;
    transient TreeMap.KeySet<K> navigableKeySetView;
    
    NavigableSubMap(TreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive)
    {
      if ((!fromStart) && (!toEnd))
      {
        if (m.compare(lo, hi) > 0) {
          throw new IllegalArgumentException("fromKey > toKey");
        }
      }
      else
      {
        if (!fromStart) {
          m.compare(lo, lo);
        }
        if (!toEnd) {
          m.compare(hi, hi);
        }
      }
      this.m = m;
      this.fromStart = fromStart;
      this.lo = lo;
      this.loInclusive = loInclusive;
      this.toEnd = toEnd;
      this.hi = hi;
      this.hiInclusive = hiInclusive;
    }
    
    final boolean tooLow(Object key)
    {
      if (!this.fromStart)
      {
        int c = this.m.compare(key, this.lo);
        if ((c < 0) || ((c == 0) && (!this.loInclusive))) {
          return true;
        }
      }
      return false;
    }
    
    final boolean tooHigh(Object key)
    {
      if (!this.toEnd)
      {
        int c = this.m.compare(key, this.hi);
        if ((c > 0) || ((c == 0) && (!this.hiInclusive))) {
          return true;
        }
      }
      return false;
    }
    
    final boolean inRange(Object key)
    {
      return (!tooLow(key)) && (!tooHigh(key));
    }
    
    final boolean inClosedRange(Object key)
    {
      return ((this.fromStart) || (this.m.compare(key, this.lo) >= 0)) && ((this.toEnd) || 
        (this.m.compare(this.hi, key) >= 0));
    }
    
    final boolean inRange(Object key, boolean inclusive)
    {
      return inclusive ? inRange(key) : inClosedRange(key);
    }
    
    final TreeMap.Entry<K, V> absLowest()
    {
      TreeMap.Entry<K, V> e = this.loInclusive ? this.m.getCeilingEntry(this.lo) : this.fromStart ? this.m.getFirstEntry() : this.m.getHigherEntry(this.lo);
      return (e == null) || (tooHigh(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absHighest()
    {
      TreeMap.Entry<K, V> e = this.hiInclusive ? this.m.getFloorEntry(this.hi) : this.toEnd ? this.m.getLastEntry() : this.m.getLowerEntry(this.hi);
      return (e == null) || (tooLow(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absCeiling(K key)
    {
      if (tooLow(key)) {
        return absLowest();
      }
      TreeMap.Entry<K, V> e = this.m.getCeilingEntry(key);
      return (e == null) || (tooHigh(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absHigher(K key)
    {
      if (tooLow(key)) {
        return absLowest();
      }
      TreeMap.Entry<K, V> e = this.m.getHigherEntry(key);
      return (e == null) || (tooHigh(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absFloor(K key)
    {
      if (tooHigh(key)) {
        return absHighest();
      }
      TreeMap.Entry<K, V> e = this.m.getFloorEntry(key);
      return (e == null) || (tooLow(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absLower(K key)
    {
      if (tooHigh(key)) {
        return absHighest();
      }
      TreeMap.Entry<K, V> e = this.m.getLowerEntry(key);
      return (e == null) || (tooLow(e.key)) ? null : e;
    }
    
    final TreeMap.Entry<K, V> absHighFence()
    {
      return this.hiInclusive ? 
        this.m.getHigherEntry(this.hi) : this.toEnd ? null : 
        this.m.getCeilingEntry(this.hi);
    }
    
    final TreeMap.Entry<K, V> absLowFence()
    {
      return this.loInclusive ? 
        this.m.getLowerEntry(this.lo) : this.fromStart ? null : 
        this.m.getFloorEntry(this.lo);
    }
    
    abstract TreeMap.Entry<K, V> subLowest();
    
    abstract TreeMap.Entry<K, V> subHighest();
    
    abstract TreeMap.Entry<K, V> subCeiling(K paramK);
    
    abstract TreeMap.Entry<K, V> subHigher(K paramK);
    
    abstract TreeMap.Entry<K, V> subFloor(K paramK);
    
    abstract TreeMap.Entry<K, V> subLower(K paramK);
    
    abstract Iterator<K> keyIterator();
    
    abstract Spliterator<K> keySpliterator();
    
    abstract Iterator<K> descendingKeyIterator();
    
    public boolean isEmpty()
    {
      return (this.fromStart) && (this.toEnd) ? this.m.isEmpty() : entrySet().isEmpty();
    }
    
    public int size()
    {
      return (this.fromStart) && (this.toEnd) ? this.m.size() : entrySet().size();
    }
    
    public final boolean containsKey(Object key)
    {
      return (inRange(key)) && (this.m.containsKey(key));
    }
    
    public final V put(K key, V value)
    {
      if (!inRange(key)) {
        throw new IllegalArgumentException("key out of range");
      }
      return this.m.put(key, value);
    }
    
    public final V get(Object key)
    {
      return !inRange(key) ? null : this.m.get(key);
    }
    
    public final V remove(Object key)
    {
      return !inRange(key) ? null : this.m.remove(key);
    }
    
    public final Map.Entry<K, V> ceilingEntry(K key)
    {
      return TreeMap.exportEntry(subCeiling(key));
    }
    
    public final K ceilingKey(K key)
    {
      return TreeMap.keyOrNull(subCeiling(key));
    }
    
    public final Map.Entry<K, V> higherEntry(K key)
    {
      return TreeMap.exportEntry(subHigher(key));
    }
    
    public final K higherKey(K key)
    {
      return TreeMap.keyOrNull(subHigher(key));
    }
    
    public final Map.Entry<K, V> floorEntry(K key)
    {
      return TreeMap.exportEntry(subFloor(key));
    }
    
    public final K floorKey(K key)
    {
      return TreeMap.keyOrNull(subFloor(key));
    }
    
    public final Map.Entry<K, V> lowerEntry(K key)
    {
      return TreeMap.exportEntry(subLower(key));
    }
    
    public final K lowerKey(K key)
    {
      return TreeMap.keyOrNull(subLower(key));
    }
    
    public final K firstKey()
    {
      return TreeMap.key(subLowest());
    }
    
    public final K lastKey()
    {
      return TreeMap.key(subHighest());
    }
    
    public final Map.Entry<K, V> firstEntry()
    {
      return TreeMap.exportEntry(subLowest());
    }
    
    public final Map.Entry<K, V> lastEntry()
    {
      return TreeMap.exportEntry(subHighest());
    }
    
    public final Map.Entry<K, V> pollFirstEntry()
    {
      TreeMap.Entry<K, V> e = subLowest();
      Map.Entry<K, V> result = TreeMap.exportEntry(e);
      if (e != null) {
        this.m.deleteEntry(e);
      }
      return result;
    }
    
    public final Map.Entry<K, V> pollLastEntry()
    {
      TreeMap.Entry<K, V> e = subHighest();
      Map.Entry<K, V> result = TreeMap.exportEntry(e);
      if (e != null) {
        this.m.deleteEntry(e);
      }
      return result;
    }
    
    public final NavigableSet<K> navigableKeySet()
    {
      TreeMap.KeySet<K> nksv = this.navigableKeySetView;
      return 
        this.navigableKeySetView = new TreeMap.KeySet(this);
    }
    
    public final Set<K> keySet()
    {
      return navigableKeySet();
    }
    
    public NavigableSet<K> descendingKeySet()
    {
      return descendingMap().navigableKeySet();
    }
    
    public final SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      return subMap(fromKey, true, toKey, false);
    }
    
    public final SortedMap<K, V> headMap(K toKey)
    {
      return headMap(toKey, false);
    }
    
    public final SortedMap<K, V> tailMap(K fromKey)
    {
      return tailMap(fromKey, true);
    }
    
    abstract class EntrySetView
      extends AbstractSet<Map.Entry<K, V>>
    {
      private transient int size = -1;
      private transient int sizeModCount;
      
      EntrySetView() {}
      
      public int size()
      {
        if ((TreeMap.NavigableSubMap.this.fromStart) && (TreeMap.NavigableSubMap.this.toEnd)) {
          return TreeMap.NavigableSubMap.this.m.size();
        }
        if ((this.size == -1) || (this.sizeModCount != TreeMap.NavigableSubMap.this.m.modCount))
        {
          this.sizeModCount = TreeMap.NavigableSubMap.this.m.modCount;
          this.size = 0;
          Iterator<?> i = iterator();
          while (i.hasNext())
          {
            this.size += 1;
            i.next();
          }
        }
        return this.size;
      }
      
      public boolean isEmpty()
      {
        TreeMap.Entry<K, V> n = TreeMap.NavigableSubMap.this.absLowest();
        return (n == null) || (TreeMap.NavigableSubMap.this.tooHigh(n.key));
      }
      
      public boolean contains(Object o)
      {
        if (!(o instanceof Map.Entry)) {
          return false;
        }
        Map.Entry<?, ?> entry = (Map.Entry)o;
        Object key = entry.getKey();
        if (!TreeMap.NavigableSubMap.this.inRange(key)) {
          return false;
        }
        TreeMap.Entry<?, ?> node = TreeMap.NavigableSubMap.this.m.getEntry(key);
        return (node != null) && 
          (TreeMap.valEquals(node.getValue(), entry.getValue()));
      }
      
      public boolean remove(Object o)
      {
        if (!(o instanceof Map.Entry)) {
          return false;
        }
        Map.Entry<?, ?> entry = (Map.Entry)o;
        Object key = entry.getKey();
        if (!TreeMap.NavigableSubMap.this.inRange(key)) {
          return false;
        }
        TreeMap.Entry<K, V> node = TreeMap.NavigableSubMap.this.m.getEntry(key);
        if ((node != null) && (TreeMap.valEquals(node.getValue(), entry
          .getValue())))
        {
          TreeMap.NavigableSubMap.this.m.deleteEntry(node);
          return true;
        }
        return false;
      }
    }
    
    abstract class SubMapIterator<T>
      implements Iterator<T>
    {
      TreeMap.Entry<K, V> lastReturned;
      TreeMap.Entry<K, V> next;
      final Object fenceKey;
      int expectedModCount;
      
      SubMapIterator(TreeMap.Entry<K, V> first)
      {
        this.expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
        this.lastReturned = null;
        this.next = first;
        this.fenceKey = (fence == null ? TreeMap.UNBOUNDED : fence.key);
      }
      
      public final boolean hasNext()
      {
        return (this.next != null) && (this.next.key != this.fenceKey);
      }
      
      final TreeMap.Entry<K, V> nextEntry()
      {
        TreeMap.Entry<K, V> e = this.next;
        if ((e == null) || (e.key == this.fenceKey)) {
          throw new NoSuchElementException();
        }
        if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
        this.next = TreeMap.successor(e);
        this.lastReturned = e;
        return e;
      }
      
      final TreeMap.Entry<K, V> prevEntry()
      {
        TreeMap.Entry<K, V> e = this.next;
        if ((e == null) || (e.key == this.fenceKey)) {
          throw new NoSuchElementException();
        }
        if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
        this.next = TreeMap.predecessor(e);
        this.lastReturned = e;
        return e;
      }
      
      final void removeAscending()
      {
        if (this.lastReturned == null) {
          throw new IllegalStateException();
        }
        if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
        if ((this.lastReturned.left != null) && (this.lastReturned.right != null)) {
          this.next = this.lastReturned;
        }
        TreeMap.NavigableSubMap.this.m.deleteEntry(this.lastReturned);
        this.lastReturned = null;
        this.expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
      }
      
      final void removeDescending()
      {
        if (this.lastReturned == null) {
          throw new IllegalStateException();
        }
        if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
        TreeMap.NavigableSubMap.this.m.deleteEntry(this.lastReturned);
        this.lastReturned = null;
        this.expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
      }
    }
    
    final class SubMapEntryIterator
      extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>>
    {
      SubMapEntryIterator(TreeMap.Entry<K, V> first)
      {
        super(first, fence);
      }
      
      public Map.Entry<K, V> next()
      {
        return nextEntry();
      }
      
      public void remove()
      {
        removeAscending();
      }
    }
    
    final class DescendingSubMapEntryIterator
      extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>>
    {
      DescendingSubMapEntryIterator(TreeMap.Entry<K, V> last)
      {
        super(last, fence);
      }
      
      public Map.Entry<K, V> next()
      {
        return prevEntry();
      }
      
      public void remove()
      {
        removeDescending();
      }
    }
    
    final class SubMapKeyIterator
      extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<K>
      implements Spliterator<K>
    {
      SubMapKeyIterator(TreeMap.Entry<K, V> first)
      {
        super(first, fence);
      }
      
      public K next()
      {
        return nextEntry().key;
      }
      
      public void remove()
      {
        removeAscending();
      }
      
      public Spliterator<K> trySplit()
      {
        return null;
      }
      
      public void forEachRemaining(Consumer<? super K> action)
      {
        while (hasNext()) {
          action.accept(next());
        }
      }
      
      public boolean tryAdvance(Consumer<? super K> action)
      {
        if (hasNext())
        {
          action.accept(next());
          return true;
        }
        return false;
      }
      
      public long estimateSize()
      {
        return 9223372036854775807L;
      }
      
      public int characteristics()
      {
        return 21;
      }
      
      public final Comparator<? super K> getComparator()
      {
        return TreeMap.NavigableSubMap.this.comparator();
      }
    }
    
    final class DescendingSubMapKeyIterator
      extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<K>
      implements Spliterator<K>
    {
      DescendingSubMapKeyIterator(TreeMap.Entry<K, V> last)
      {
        super(last, fence);
      }
      
      public K next()
      {
        return prevEntry().key;
      }
      
      public void remove()
      {
        removeDescending();
      }
      
      public Spliterator<K> trySplit()
      {
        return null;
      }
      
      public void forEachRemaining(Consumer<? super K> action)
      {
        while (hasNext()) {
          action.accept(next());
        }
      }
      
      public boolean tryAdvance(Consumer<? super K> action)
      {
        if (hasNext())
        {
          action.accept(next());
          return true;
        }
        return false;
      }
      
      public long estimateSize()
      {
        return 9223372036854775807L;
      }
      
      public int characteristics()
      {
        return 17;
      }
    }
  }
  
  static final class AscendingSubMap<K, V>
    extends TreeMap.NavigableSubMap<K, V>
  {
    private static final long serialVersionUID = 912986545866124060L;
    
    AscendingSubMap(TreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive)
    {
      super(fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
    }
    
    public Comparator<? super K> comparator()
    {
      return this.m.comparator();
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
      if (!inRange(fromKey, fromInclusive)) {
        throw new IllegalArgumentException("fromKey out of range");
      }
      if (!inRange(toKey, toInclusive)) {
        throw new IllegalArgumentException("toKey out of range");
      }
      return new AscendingSubMap(this.m, false, fromKey, fromInclusive, false, toKey, toInclusive);
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
    {
      if (!inRange(toKey, inclusive)) {
        throw new IllegalArgumentException("toKey out of range");
      }
      return new AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, toKey, inclusive);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
    {
      if (!inRange(fromKey, inclusive)) {
        throw new IllegalArgumentException("fromKey out of range");
      }
      return new AscendingSubMap(this.m, false, fromKey, inclusive, this.toEnd, this.hi, this.hiInclusive);
    }
    
    public NavigableMap<K, V> descendingMap()
    {
      NavigableMap<K, V> mv = this.descendingMapView;
      return 
        this.descendingMapView = new TreeMap.DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
    }
    
    Iterator<K> keyIterator()
    {
      return new TreeMap.NavigableSubMap.SubMapKeyIterator(this, absLowest(), absHighFence());
    }
    
    Spliterator<K> keySpliterator()
    {
      return new TreeMap.NavigableSubMap.SubMapKeyIterator(this, absLowest(), absHighFence());
    }
    
    Iterator<K> descendingKeyIterator()
    {
      return new TreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this, absHighest(), absLowFence());
    }
    
    final class AscendingEntrySetView
      extends TreeMap.NavigableSubMap<K, V>.EntrySetView
    {
      AscendingEntrySetView()
      {
        super();
      }
      
      public Iterator<Map.Entry<K, V>> iterator()
      {
        return new TreeMap.NavigableSubMap.SubMapEntryIterator(TreeMap.AscendingSubMap.this, TreeMap.AscendingSubMap.this.absLowest(), TreeMap.AscendingSubMap.this.absHighFence());
      }
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      TreeMap.NavigableSubMap<K, V>.EntrySetView es = this.entrySetView;
      return this.entrySetView = new AscendingEntrySetView();
    }
    
    TreeMap.Entry<K, V> subLowest()
    {
      return absLowest();
    }
    
    TreeMap.Entry<K, V> subHighest()
    {
      return absHighest();
    }
    
    TreeMap.Entry<K, V> subCeiling(K key)
    {
      return absCeiling(key);
    }
    
    TreeMap.Entry<K, V> subHigher(K key)
    {
      return absHigher(key);
    }
    
    TreeMap.Entry<K, V> subFloor(K key)
    {
      return absFloor(key);
    }
    
    TreeMap.Entry<K, V> subLower(K key)
    {
      return absLower(key);
    }
  }
  
  static final class DescendingSubMap<K, V>
    extends TreeMap.NavigableSubMap<K, V>
  {
    private static final long serialVersionUID = 912986545866120460L;
    
    DescendingSubMap(TreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive)
    {
      super(fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
    }
    
    private final Comparator<? super K> reverseComparator = Collections.reverseOrder(this.m.comparator);
    
    public Comparator<? super K> comparator()
    {
      return this.reverseComparator;
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
      if (!inRange(fromKey, fromInclusive)) {
        throw new IllegalArgumentException("fromKey out of range");
      }
      if (!inRange(toKey, toInclusive)) {
        throw new IllegalArgumentException("toKey out of range");
      }
      return new DescendingSubMap(this.m, false, toKey, toInclusive, false, fromKey, fromInclusive);
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
    {
      if (!inRange(toKey, inclusive)) {
        throw new IllegalArgumentException("toKey out of range");
      }
      return new DescendingSubMap(this.m, false, toKey, inclusive, this.toEnd, this.hi, this.hiInclusive);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
    {
      if (!inRange(fromKey, inclusive)) {
        throw new IllegalArgumentException("fromKey out of range");
      }
      return new DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, fromKey, inclusive);
    }
    
    public NavigableMap<K, V> descendingMap()
    {
      NavigableMap<K, V> mv = this.descendingMapView;
      return 
        this.descendingMapView = new TreeMap.AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
    }
    
    Iterator<K> keyIterator()
    {
      return new TreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this, absHighest(), absLowFence());
    }
    
    Spliterator<K> keySpliterator()
    {
      return new TreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this, absHighest(), absLowFence());
    }
    
    Iterator<K> descendingKeyIterator()
    {
      return new TreeMap.NavigableSubMap.SubMapKeyIterator(this, absLowest(), absHighFence());
    }
    
    final class DescendingEntrySetView
      extends TreeMap.NavigableSubMap<K, V>.EntrySetView
    {
      DescendingEntrySetView()
      {
        super();
      }
      
      public Iterator<Map.Entry<K, V>> iterator()
      {
        return new TreeMap.NavigableSubMap.DescendingSubMapEntryIterator(TreeMap.DescendingSubMap.this, TreeMap.DescendingSubMap.this.absHighest(), TreeMap.DescendingSubMap.this.absLowFence());
      }
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      TreeMap.NavigableSubMap<K, V>.EntrySetView es = this.entrySetView;
      return this.entrySetView = new DescendingEntrySetView();
    }
    
    TreeMap.Entry<K, V> subLowest()
    {
      return absHighest();
    }
    
    TreeMap.Entry<K, V> subHighest()
    {
      return absLowest();
    }
    
    TreeMap.Entry<K, V> subCeiling(K key)
    {
      return absFloor(key);
    }
    
    TreeMap.Entry<K, V> subHigher(K key)
    {
      return absLower(key);
    }
    
    TreeMap.Entry<K, V> subFloor(K key)
    {
      return absCeiling(key);
    }
    
    TreeMap.Entry<K, V> subLower(K key)
    {
      return absHigher(key);
    }
  }
  
  private class SubMap
    extends AbstractMap<K, V>
    implements SortedMap<K, V>, Serializable
  {
    private K toKey;
    private K fromKey;
    private boolean toEnd = false;
    private boolean fromStart = false;
    private static final long serialVersionUID = -6520786458950516097L;
    
    private SubMap() {}
    
    private Object readResolve()
    {
      return new TreeMap.AscendingSubMap(TreeMap.this, this.fromStart, this.fromKey, true, this.toEnd, this.toKey, false);
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      throw new InternalError();
    }
    
    public K lastKey()
    {
      throw new InternalError();
    }
    
    public K firstKey()
    {
      throw new InternalError();
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
      throw new InternalError();
    }
    
    public SortedMap<K, V> headMap(K toKey)
    {
      throw new InternalError();
    }
    
    public SortedMap<K, V> tailMap(K fromKey)
    {
      throw new InternalError();
    }
    
    public Comparator<? super K> comparator()
    {
      throw new InternalError();
    }
  }
  
  static final class Entry<K, V>
    implements Map.Entry<K, V>
  {
    K key;
    V value;
    Entry<K, V> left;
    Entry<K, V> right;
    Entry<K, V> parent;
    boolean color = true;
    
    Entry(K key, V value, Entry<K, V> parent)
    {
      this.key = key;
      this.value = value;
      this.parent = parent;
    }
    
    public K getKey()
    {
      return this.key;
    }
    
    public V getValue()
    {
      return this.value;
    }
    
    public V setValue(V value)
    {
      V oldValue = this.value;
      this.value = value;
      return oldValue;
    }
    
    public boolean equals(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      
      return (TreeMap.valEquals(this.key, e.getKey())) && (TreeMap.valEquals(this.value, e.getValue()));
    }
    
    public int hashCode()
    {
      int keyHash = this.key == null ? 0 : this.key.hashCode();
      int valueHash = this.value == null ? 0 : this.value.hashCode();
      return keyHash ^ valueHash;
    }
    
    public String toString()
    {
      return this.key + "=" + this.value;
    }
  }
  
  final Entry<K, V> getFirstEntry()
  {
    Entry<K, V> p = this.root;
    if (p != null) {
      while (p.left != null) {
        p = p.left;
      }
    }
    return p;
  }
  
  final Entry<K, V> getLastEntry()
  {
    Entry<K, V> p = this.root;
    if (p != null) {
      while (p.right != null) {
        p = p.right;
      }
    }
    return p;
  }
  
  static <K, V> Entry<K, V> successor(Entry<K, V> t)
  {
    if (t == null) {
      return null;
    }
    if (t.right != null)
    {
      Entry<K, V> p = t.right;
      while (p.left != null) {
        p = p.left;
      }
      return p;
    }
    Entry<K, V> p = t.parent;
    Entry<K, V> ch = t;
    while ((p != null) && (ch == p.right))
    {
      ch = p;
      p = p.parent;
    }
    return p;
  }
  
  static <K, V> Entry<K, V> predecessor(Entry<K, V> t)
  {
    if (t == null) {
      return null;
    }
    if (t.left != null)
    {
      Entry<K, V> p = t.left;
      while (p.right != null) {
        p = p.right;
      }
      return p;
    }
    Entry<K, V> p = t.parent;
    Entry<K, V> ch = t;
    while ((p != null) && (ch == p.left))
    {
      ch = p;
      p = p.parent;
    }
    return p;
  }
  
  private static <K, V> boolean colorOf(Entry<K, V> p)
  {
    return p == null ? true : p.color;
  }
  
  private static <K, V> Entry<K, V> parentOf(Entry<K, V> p)
  {
    return p == null ? null : p.parent;
  }
  
  private static <K, V> void setColor(Entry<K, V> p, boolean c)
  {
    if (p != null) {
      p.color = c;
    }
  }
  
  private static <K, V> Entry<K, V> leftOf(Entry<K, V> p)
  {
    return p == null ? null : p.left;
  }
  
  private static <K, V> Entry<K, V> rightOf(Entry<K, V> p)
  {
    return p == null ? null : p.right;
  }
  
  private void rotateLeft(Entry<K, V> p)
  {
    if (p != null)
    {
      Entry<K, V> r = p.right;
      p.right = r.left;
      if (r.left != null) {
        r.left.parent = p;
      }
      r.parent = p.parent;
      if (p.parent == null) {
        this.root = r;
      } else if (p.parent.left == p) {
        p.parent.left = r;
      } else {
        p.parent.right = r;
      }
      r.left = p;
      p.parent = r;
    }
  }
  
  private void rotateRight(Entry<K, V> p)
  {
    if (p != null)
    {
      Entry<K, V> l = p.left;
      p.left = l.right;
      if (l.right != null) {
        l.right.parent = p;
      }
      l.parent = p.parent;
      if (p.parent == null) {
        this.root = l;
      } else if (p.parent.right == p) {
        p.parent.right = l;
      } else {
        p.parent.left = l;
      }
      l.right = p;
      p.parent = l;
    }
  }
  
  private void fixAfterInsertion(Entry<K, V> x)
  {
    x.color = false;
    while ((x != null) && (x != this.root) && (!x.parent.color)) {
      if (parentOf(x) == leftOf(parentOf(parentOf(x))))
      {
        Entry<K, V> y = rightOf(parentOf(parentOf(x)));
        if (!colorOf(y))
        {
          setColor(parentOf(x), true);
          setColor(y, true);
          setColor(parentOf(parentOf(x)), false);
          x = parentOf(parentOf(x));
        }
        else
        {
          if (x == rightOf(parentOf(x)))
          {
            x = parentOf(x);
            rotateLeft(x);
          }
          setColor(parentOf(x), true);
          setColor(parentOf(parentOf(x)), false);
          rotateRight(parentOf(parentOf(x)));
        }
      }
      else
      {
        Entry<K, V> y = leftOf(parentOf(parentOf(x)));
        if (!colorOf(y))
        {
          setColor(parentOf(x), true);
          setColor(y, true);
          setColor(parentOf(parentOf(x)), false);
          x = parentOf(parentOf(x));
        }
        else
        {
          if (x == leftOf(parentOf(x)))
          {
            x = parentOf(x);
            rotateRight(x);
          }
          setColor(parentOf(x), true);
          setColor(parentOf(parentOf(x)), false);
          rotateLeft(parentOf(parentOf(x)));
        }
      }
    }
    this.root.color = true;
  }
  
  private void deleteEntry(Entry<K, V> p)
  {
    this.modCount += 1;
    this.size -= 1;
    if ((p.left != null) && (p.right != null))
    {
      Entry<K, V> s = successor(p);
      p.key = s.key;
      p.value = s.value;
      p = s;
    }
    Entry<K, V> replacement = p.left != null ? p.left : p.right;
    if (replacement != null)
    {
      replacement.parent = p.parent;
      if (p.parent == null) {
        this.root = replacement;
      } else if (p == p.parent.left) {
        p.parent.left = replacement;
      } else {
        p.parent.right = replacement;
      }
      p.left = (p.right = p.parent = null);
      if (p.color == true) {
        fixAfterDeletion(replacement);
      }
    }
    else if (p.parent == null)
    {
      this.root = null;
    }
    else
    {
      if (p.color == true) {
        fixAfterDeletion(p);
      }
      if (p.parent != null)
      {
        if (p == p.parent.left) {
          p.parent.left = null;
        } else if (p == p.parent.right) {
          p.parent.right = null;
        }
        p.parent = null;
      }
    }
  }
  
  private void fixAfterDeletion(Entry<K, V> x)
  {
    while ((x != this.root) && (colorOf(x) == true)) {
      if (x == leftOf(parentOf(x)))
      {
        Entry<K, V> sib = rightOf(parentOf(x));
        if (!colorOf(sib))
        {
          setColor(sib, true);
          setColor(parentOf(x), false);
          rotateLeft(parentOf(x));
          sib = rightOf(parentOf(x));
        }
        if ((colorOf(leftOf(sib)) == true) && 
          (colorOf(rightOf(sib)) == true))
        {
          setColor(sib, false);
          x = parentOf(x);
        }
        else
        {
          if (colorOf(rightOf(sib)) == true)
          {
            setColor(leftOf(sib), true);
            setColor(sib, false);
            rotateRight(sib);
            sib = rightOf(parentOf(x));
          }
          setColor(sib, colorOf(parentOf(x)));
          setColor(parentOf(x), true);
          setColor(rightOf(sib), true);
          rotateLeft(parentOf(x));
          x = this.root;
        }
      }
      else
      {
        Entry<K, V> sib = leftOf(parentOf(x));
        if (!colorOf(sib))
        {
          setColor(sib, true);
          setColor(parentOf(x), false);
          rotateRight(parentOf(x));
          sib = leftOf(parentOf(x));
        }
        if ((colorOf(rightOf(sib)) == true) && 
          (colorOf(leftOf(sib)) == true))
        {
          setColor(sib, false);
          x = parentOf(x);
        }
        else
        {
          if (colorOf(leftOf(sib)) == true)
          {
            setColor(rightOf(sib), true);
            setColor(sib, false);
            rotateLeft(sib);
            sib = leftOf(parentOf(x));
          }
          setColor(sib, colorOf(parentOf(x)));
          setColor(parentOf(x), true);
          setColor(leftOf(sib), true);
          rotateRight(parentOf(x));
          x = this.root;
        }
      }
    }
    setColor(x, true);
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    s.defaultWriteObject();
    

    s.writeInt(this.size);
    for (Map.Entry<K, V> e : entrySet())
    {
      s.writeObject(e.getKey());
      s.writeObject(e.getValue());
    }
  }
  
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    

    int size = s.readInt();
    
    buildFromSorted(size, null, s, null);
  }
  
  void readTreeSet(int size, ObjectInputStream s, V defaultVal)
    throws IOException, ClassNotFoundException
  {
    buildFromSorted(size, null, s, defaultVal);
  }
  
  void addAllForTreeSet(SortedSet<? extends K> set, V defaultVal)
  {
    try
    {
      buildFromSorted(set.size(), set.iterator(), null, defaultVal);
    }
    catch (IOException|ClassNotFoundException localIOException) {}
  }
  
  private void buildFromSorted(int size, Iterator<?> it, ObjectInputStream str, V defaultVal)
    throws IOException, ClassNotFoundException
  {
    this.size = size;
    this.root = buildFromSorted(0, 0, size - 1, computeRedLevel(size), it, str, defaultVal);
  }
  
  private final Entry<K, V> buildFromSorted(int level, int lo, int hi, int redLevel, Iterator<?> it, ObjectInputStream str, V defaultVal)
    throws IOException, ClassNotFoundException
  {
    if (hi < lo) {
      return null;
    }
    int mid = lo + hi >>> 1;
    
    Entry<K, V> left = null;
    if (lo < mid) {
      left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
    }
    V value;
    K key;
    V value;
    if (it != null)
    {
      V value;
      if (defaultVal == null)
      {
        Map.Entry<?, ?> entry = (Map.Entry)it.next();
        K key = entry.getKey();
        value = entry.getValue();
      }
      else
      {
        K key = it.next();
        value = defaultVal;
      }
    }
    else
    {
      key = str.readObject();
      value = defaultVal != null ? defaultVal : str.readObject();
    }
    Entry<K, V> middle = new Entry(key, value, null);
    if (level == redLevel) {
      middle.color = false;
    }
    if (left != null)
    {
      middle.left = left;
      left.parent = middle;
    }
    if (mid < hi)
    {
      Entry<K, V> right = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
      
      middle.right = right;
      right.parent = middle;
    }
    return middle;
  }
  
  private static int computeRedLevel(int size)
  {
    return 31 - Integer.numberOfLeadingZeros(size + 1);
  }
  
  static <K> Spliterator<K> keySpliteratorFor(NavigableMap<K, ?> m)
  {
    if ((m instanceof TreeMap))
    {
      TreeMap<K, Object> t = (TreeMap)m;
      
      return t.keySpliterator();
    }
    if ((m instanceof DescendingSubMap))
    {
      DescendingSubMap<K, ?> dm = (DescendingSubMap)m;
      
      TreeMap<K, ?> tm = dm.m;
      if (dm == tm.descendingMap)
      {
        TreeMap<K, Object> t = tm;
        
        return t.descendingKeySpliterator();
      }
    }
    NavigableSubMap<K, ?> sm = (NavigableSubMap)m;
    
    return sm.keySpliterator();
  }
  
  final Spliterator<K> keySpliterator()
  {
    return new KeySpliterator(this, null, null, 0, -1, 0);
  }
  
  final Spliterator<K> descendingKeySpliterator()
  {
    return new DescendingKeySpliterator(this, null, null, 0, -2, 0);
  }
  
  static class TreeMapSpliterator<K, V>
  {
    final TreeMap<K, V> tree;
    TreeMap.Entry<K, V> current;
    TreeMap.Entry<K, V> fence;
    int side;
    int est;
    int expectedModCount;
    
    TreeMapSpliterator(TreeMap<K, V> tree, TreeMap.Entry<K, V> origin, TreeMap.Entry<K, V> fence, int side, int est, int expectedModCount)
    {
      this.tree = tree;
      this.current = origin;
      this.fence = fence;
      this.side = side;
      this.est = est;
      this.expectedModCount = expectedModCount;
    }
    
    final int getEstimate()
    {
      int s;
      if ((s = this.est) < 0)
      {
        TreeMap<K, V> t;
        if ((t = this.tree) != null)
        {
          this.current = (s == -1 ? t.getFirstEntry() : t.getLastEntry());
          s = this.est = t.size;
          this.expectedModCount = t.modCount;
        }
        else
        {
          s = this.est = 0;
        }
      }
      return s;
    }
    
    public final long estimateSize()
    {
      return getEstimate();
    }
  }
  
  static final class KeySpliterator<K, V>
    extends TreeMap.TreeMapSpliterator<K, V>
    implements Spliterator<K>
  {
    KeySpliterator(TreeMap<K, V> tree, TreeMap.Entry<K, V> origin, TreeMap.Entry<K, V> fence, int side, int est, int expectedModCount)
    {
      super(origin, fence, side, est, expectedModCount);
    }
    
    public KeySpliterator<K, V> trySplit()
    {
      if (this.est < 0) {
        getEstimate();
      }
      int d = this.side;
      TreeMap.Entry<K, V> e = this.current;TreeMap.Entry<K, V> f = this.fence;
      



      TreeMap.Entry<K, V> s = (d < 0) && (f != null) ? f.left : d > 0 ? e.right : d == 0 ? this.tree.root : (e == null) || (e == f) ? null : null;
      if ((s != null) && (s != e) && (s != f) && 
        (this.tree.compare(e.key, s.key) < 0))
      {
        this.side = 1;
        return new KeySpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
      }
      return null;
    }
    
    public void forEachRemaining(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> f = this.fence;
      TreeMap.Entry<K, V> e;
      if (((e = this.current) != null) && (e != f))
      {
        this.current = f;
        TreeMap.Entry<K, V> p;
        do
        {
          action.accept(e.key);
          if ((p = e.right) != null)
          {
            TreeMap.Entry<K, V> pl;
            while ((pl = p.left) != null) {
              p = pl;
            }
          }
          while (((p = e.parent) != null) && (e == p.right)) {
            e = p;
          }
        } while (((e = p) != null) && (e != f));
        if (this.tree.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> e;
      if (((e = this.current) == null) || (e == this.fence)) {
        return false;
      }
      this.current = TreeMap.successor(e);
      action.accept(e.key);
      if (this.tree.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      return true;
    }
    
    public int characteristics()
    {
      return (this.side == 0 ? 64 : 0) | 0x1 | 0x4 | 0x10;
    }
    
    public final Comparator<? super K> getComparator()
    {
      return this.tree.comparator;
    }
  }
  
  static final class DescendingKeySpliterator<K, V>
    extends TreeMap.TreeMapSpliterator<K, V>
    implements Spliterator<K>
  {
    DescendingKeySpliterator(TreeMap<K, V> tree, TreeMap.Entry<K, V> origin, TreeMap.Entry<K, V> fence, int side, int est, int expectedModCount)
    {
      super(origin, fence, side, est, expectedModCount);
    }
    
    public DescendingKeySpliterator<K, V> trySplit()
    {
      if (this.est < 0) {
        getEstimate();
      }
      int d = this.side;
      TreeMap.Entry<K, V> e = this.current;TreeMap.Entry<K, V> f = this.fence;
      



      TreeMap.Entry<K, V> s = (d > 0) && (f != null) ? f.right : d < 0 ? e.left : d == 0 ? this.tree.root : (e == null) || (e == f) ? null : null;
      if ((s != null) && (s != e) && (s != f) && 
        (this.tree.compare(e.key, s.key) > 0))
      {
        this.side = 1;
        return new DescendingKeySpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
      }
      return null;
    }
    
    public void forEachRemaining(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> f = this.fence;
      TreeMap.Entry<K, V> e;
      if (((e = this.current) != null) && (e != f))
      {
        this.current = f;
        TreeMap.Entry<K, V> p;
        do
        {
          action.accept(e.key);
          if ((p = e.left) != null)
          {
            TreeMap.Entry<K, V> pr;
            while ((pr = p.right) != null) {
              p = pr;
            }
          }
          while (((p = e.parent) != null) && (e == p.left)) {
            e = p;
          }
        } while (((e = p) != null) && (e != f));
        if (this.tree.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> e;
      if (((e = this.current) == null) || (e == this.fence)) {
        return false;
      }
      this.current = TreeMap.predecessor(e);
      action.accept(e.key);
      if (this.tree.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      return true;
    }
    
    public int characteristics()
    {
      return (this.side == 0 ? 64 : 0) | 0x1 | 0x10;
    }
  }
  
  static final class ValueSpliterator<K, V>
    extends TreeMap.TreeMapSpliterator<K, V>
    implements Spliterator<V>
  {
    ValueSpliterator(TreeMap<K, V> tree, TreeMap.Entry<K, V> origin, TreeMap.Entry<K, V> fence, int side, int est, int expectedModCount)
    {
      super(origin, fence, side, est, expectedModCount);
    }
    
    public ValueSpliterator<K, V> trySplit()
    {
      if (this.est < 0) {
        getEstimate();
      }
      int d = this.side;
      TreeMap.Entry<K, V> e = this.current;TreeMap.Entry<K, V> f = this.fence;
      



      TreeMap.Entry<K, V> s = (d < 0) && (f != null) ? f.left : d > 0 ? e.right : d == 0 ? this.tree.root : (e == null) || (e == f) ? null : null;
      if ((s != null) && (s != e) && (s != f) && 
        (this.tree.compare(e.key, s.key) < 0))
      {
        this.side = 1;
        return new ValueSpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
      }
      return null;
    }
    
    public void forEachRemaining(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> f = this.fence;
      TreeMap.Entry<K, V> e;
      if (((e = this.current) != null) && (e != f))
      {
        this.current = f;
        TreeMap.Entry<K, V> p;
        do
        {
          action.accept(e.value);
          if ((p = e.right) != null)
          {
            TreeMap.Entry<K, V> pl;
            while ((pl = p.left) != null) {
              p = pl;
            }
          }
          while (((p = e.parent) != null) && (e == p.right)) {
            e = p;
          }
        } while (((e = p) != null) && (e != f));
        if (this.tree.modCount != this.expectedModCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      if (this.est < 0) {
        getEstimate();
      }
      TreeMap.Entry<K, V> e;
      if (((e = this.current) == null) || (e == this.fence)) {
        return false;
      }
      this.current = TreeMap.successor(e);
      action.accept(e.value);
      if (this.tree.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      return true;
    }
    
    public int characteristics()
    {
      return (this.side == 0 ? 64 : 0) | 0x10;
    }
  }
}
