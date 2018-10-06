package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import jdk.internal.misc.JavaObjectInputStreamAccess;
import jdk.internal.misc.SharedSecrets;

public class IdentityHashMap<K, V>   extends AbstractMap<K, V>   implements Map<K, V>, Serializable, Cloneable {
  private static final int DEFAULT_CAPACITY = 32;
  private static final int MINIMUM_CAPACITY = 4;
  private static final int MAXIMUM_CAPACITY = 536870912;
  transient Object[] table;
  int size;
  transient int modCount;
  static final Object NULL_KEY = new Object();
  private transient Set<Map.Entry<K, V>> entrySet;
  private static final long serialVersionUID = 8188218128353913216L;
  
  private static Object maskNull(Object key)
  {
    return key == null ? NULL_KEY : key;
  }
  
  static final Object unmaskNull(Object key)
  {
    return key == NULL_KEY ? null : key;
  }
  
  public IdentityHashMap()
  {
    init(32);
  }
  
  public IdentityHashMap(int expectedMaxSize)
  {
    if (expectedMaxSize < 0) {
      throw new IllegalArgumentException("expectedMaxSize is negative: " + expectedMaxSize);
    }
    init(capacity(expectedMaxSize));
  }
  
  private static int capacity(int expectedMaxSize)
  {
    return 
    
      expectedMaxSize <= 2 ? 4 : expectedMaxSize > 178956970 ? 536870912 : 
      Integer.highestOneBit(expectedMaxSize + (expectedMaxSize << 1));
  }
  
  private void init(int initCapacity)
  {
    this.table = new Object[2 * initCapacity];
  }
  
  public IdentityHashMap(Map<? extends K, ? extends V> m)
  {
    this((int)((1 + m.size()) * 1.1D));
    putAll(m);
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  private static int hash(Object x, int length)
  {
    int h = System.identityHashCode(x);
    
    return (h << 1) - (h << 8) & length - 1;
  }
  
  private static int nextKeyIndex(int i, int len)
  {
    return i + 2 < len ? i + 2 : 0;
  }
  
  public V get(Object key)
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    for (;;)
    {
      Object item = tab[i];
      if (item == k) {
        return tab[(i + 1)];
      }
      if (item == null) {
        return null;
      }
      i = nextKeyIndex(i, len);
    }
  }
  
  public boolean containsKey(Object key)
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    for (;;)
    {
      Object item = tab[i];
      if (item == k) {
        return true;
      }
      if (item == null) {
        return false;
      }
      i = nextKeyIndex(i, len);
    }
  }
  
  public boolean containsValue(Object value)
  {
    Object[] tab = this.table;
    for (int i = 1; i < tab.length; i += 2) {
      if ((tab[i] == value) && (tab[(i - 1)] != null)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean containsMapping(Object key, Object value)
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    for (;;)
    {
      Object item = tab[i];
      if (item == k) {
        return tab[(i + 1)] == value;
      }
      if (item == null) {
        return false;
      }
      i = nextKeyIndex(i, len);
    }
  }
  
  public V put(K key, V value)
  {
    Object k = maskNull(key);
    Object[] tab;
    int len;
    int i;
    int s;
    do
    {
      tab = this.table;
      len = tab.length;
      Object item;
      for (i = hash(k, len); (item = tab[i]) != null; i = nextKeyIndex(i, len)) {
        if (item == k)
        {
          V oldValue = tab[(i + 1)];
          tab[(i + 1)] = value;
          return oldValue;
        }
      }
      s = this.size + 1;
    } while ((s + (s << 1) > len) && (resize(len)));
    this.modCount += 1;
    tab[i] = k;
    tab[(i + 1)] = value;
    this.size = s;
    return null;
  }
  
  private boolean resize(int newCapacity)
  {
    int newLength = newCapacity * 2;
    
    Object[] oldTable = this.table;
    int oldLength = oldTable.length;
    if (oldLength == 1073741824)
    {
      if (this.size == 536870911) {
        throw new IllegalStateException("Capacity exhausted.");
      }
      return false;
    }
    if (oldLength >= newLength) {
      return false;
    }
    Object[] newTable = new Object[newLength];
    for (int j = 0; j < oldLength; j += 2)
    {
      Object key = oldTable[j];
      if (key != null)
      {
        Object value = oldTable[(j + 1)];
        oldTable[j] = null;
        oldTable[(j + 1)] = null;
        int i = hash(key, newLength);
        while (newTable[i] != null) {
          i = nextKeyIndex(i, newLength);
        }
        newTable[i] = key;
        newTable[(i + 1)] = value;
      }
    }
    this.table = newTable;
    return true;
  }
  
  public void putAll(Map<? extends K, ? extends V> m)
  {
    int n = m.size();
    if (n == 0) {
      return;
    }
    if (n > this.size) {
      resize(capacity(n));
    }
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  public V remove(Object key)
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    for (;;)
    {
      Object item = tab[i];
      if (item == k)
      {
        this.modCount += 1;
        this.size -= 1;
        
        V oldValue = tab[(i + 1)];
        tab[(i + 1)] = null;
        tab[i] = null;
        closeDeletion(i);
        return oldValue;
      }
      if (item == null) {
        return null;
      }
      i = nextKeyIndex(i, len);
    }
  }
  
  private boolean removeMapping(Object key, Object value)
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    for (;;)
    {
      Object item = tab[i];
      if (item == k)
      {
        if (tab[(i + 1)] != value) {
          return false;
        }
        this.modCount += 1;
        this.size -= 1;
        tab[i] = null;
        tab[(i + 1)] = null;
        closeDeletion(i);
        return true;
      }
      if (item == null) {
        return false;
      }
      i = nextKeyIndex(i, len);
    }
  }
  
  private void closeDeletion(int d)
  {
    Object[] tab = this.table;
    int len = tab.length;
    Object item;
    for (int i = nextKeyIndex(d, len); (item = tab[i]) != null; i = nextKeyIndex(i, len))
    {
      int r = hash(item, len);
      if (((i < r) && ((r <= d) || (d <= i))) || ((r <= d) && (d <= i)))
      {
        tab[d] = item;
        tab[(d + 1)] = tab[(i + 1)];
        tab[i] = null;
        tab[(i + 1)] = null;
        d = i;
      }
    }
  }
  
  public void clear()
  {
    this.modCount += 1;
    Object[] tab = this.table;
    for (int i = 0; i < tab.length; i++) {
      tab[i] = null;
    }
    this.size = 0;
  }
  
  public boolean equals(Object o)
  {
    if (o == this) {
      return true;
    }
    if ((o instanceof IdentityHashMap))
    {
      IdentityHashMap<?, ?> m = (IdentityHashMap)o;
      if (m.size() != this.size) {
        return false;
      }
      Object[] tab = m.table;
      for (int i = 0; i < tab.length; i += 2)
      {
        Object k = tab[i];
        if ((k != null) && (!containsMapping(k, tab[(i + 1)]))) {
          return false;
        }
      }
      return true;
    }
    if ((o instanceof Map))
    {
      Map<?, ?> m = (Map)o;
      return entrySet().equals(m.entrySet());
    }
    return false;
  }
  
  public int hashCode()
  {
    int result = 0;
    Object[] tab = this.table;
    for (int i = 0; i < tab.length; i += 2)
    {
      Object key = tab[i];
      if (key != null)
      {
        Object k = unmaskNull(key);
        
        result = result + (System.identityHashCode(k) ^ System.identityHashCode(tab[(i + 1)]));
      }
    }
    return result;
  }
  
  public Object clone()
  {
    try
    {
      IdentityHashMap<?, ?> m = (IdentityHashMap)super.clone();
      m.entrySet = null;
      m.table = ((Object[])this.table.clone());
      return m;
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError(e);
    }
  }
  
  private abstract class IdentityHashMapIterator<T>
    implements Iterator<T>
  {
    int index = IdentityHashMap.this.size != 0 ? 0 : IdentityHashMap.this.table.length;
    int expectedModCount = IdentityHashMap.this.modCount;
    int lastReturnedIndex = -1;
    boolean indexValid;
    Object[] traversalTable = IdentityHashMap.this.table;
    
    private IdentityHashMapIterator() {}
    
    public boolean hasNext()
    {
      Object[] tab = this.traversalTable;
      for (int i = this.index; i < tab.length; i += 2)
      {
        Object key = tab[i];
        if (key != null)
        {
          this.index = i;
          return this.indexValid = 1;
        }
      }
      this.index = tab.length;
      return false;
    }
    
    protected int nextIndex()
    {
      if (IdentityHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if ((!this.indexValid) && (!hasNext())) {
        throw new NoSuchElementException();
      }
      this.indexValid = false;
      this.lastReturnedIndex = this.index;
      this.index += 2;
      return this.lastReturnedIndex;
    }
    
    public void remove()
    {
      if (this.lastReturnedIndex == -1) {
        throw new IllegalStateException();
      }
      if (IdentityHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.expectedModCount = (++IdentityHashMap.this.modCount);
      int deletedSlot = this.lastReturnedIndex;
      this.lastReturnedIndex = -1;
      
      this.index = deletedSlot;
      this.indexValid = false;
      












      Object[] tab = this.traversalTable;
      int len = tab.length;
      
      int d = deletedSlot;
      Object key = tab[d];
      tab[d] = null;
      tab[(d + 1)] = null;
      if (tab != IdentityHashMap.this.table)
      {
        IdentityHashMap.this.remove(key);
        this.expectedModCount = IdentityHashMap.this.modCount;
        return;
      }
      IdentityHashMap.this.size -= 1;
      Object item;
      for (int i = IdentityHashMap.nextKeyIndex(d, len); (item = tab[i]) != null; i = IdentityHashMap.nextKeyIndex(i, len))
      {
        int r = IdentityHashMap.hash(item, len);
        if (((i < r) && ((r <= d) || (d <= i))) || ((r <= d) && (d <= i)))
        {
          if ((i < deletedSlot) && (d >= deletedSlot) && (this.traversalTable == IdentityHashMap.this.table))
          {
            int remaining = len - deletedSlot;
            Object[] newTable = new Object[remaining];
            System.arraycopy(tab, deletedSlot, newTable, 0, remaining);
            
            this.traversalTable = newTable;
            this.index = 0;
          }
          tab[d] = item;
          tab[(d + 1)] = tab[(i + 1)];
          tab[i] = null;
          tab[(i + 1)] = null;
          d = i;
        }
      }
    }
  }
  
  private class KeyIterator
    extends IdentityHashMap<K, V>.IdentityHashMapIterator<K>
  {
    private KeyIterator()
    {
      super(null);
    }
    
    public K next()
    {
      return IdentityHashMap.unmaskNull(this.traversalTable[nextIndex()]);
    }
  }
  
  private class ValueIterator
    extends IdentityHashMap<K, V>.IdentityHashMapIterator<V>
  {
    private ValueIterator()
    {
      super(null);
    }
    
    public V next()
    {
      return this.traversalTable[(nextIndex() + 1)];
    }
  }
  
  private class EntryIterator
    extends IdentityHashMap<K, V>.IdentityHashMapIterator<Map.Entry<K, V>>
  {
    private IdentityHashMap<K, V>.EntryIterator.Entry lastReturnedEntry;
    
    private EntryIterator()
    {
      super(null);
    }
    
    public Map.Entry<K, V> next()
    {
      this.lastReturnedEntry = new Entry(nextIndex(), null);
      return this.lastReturnedEntry;
    }
    
    public void remove()
    {
      this.lastReturnedIndex = (null == this.lastReturnedEntry ? -1 : this.lastReturnedEntry.index);
      super.remove();
      this.lastReturnedEntry.index = this.lastReturnedIndex;
      this.lastReturnedEntry = null;
    }
    
    private class Entry
      implements Map.Entry<K, V>
    {
      private int index;
      
      private Entry(int index)
      {
        this.index = index;
      }
      
      public K getKey()
      {
        checkIndexForEntryUse();
        return IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index]);
      }
      
      public V getValue()
      {
        checkIndexForEntryUse();
        return IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
      }
      
      public V setValue(V value)
      {
        checkIndexForEntryUse();
        V oldValue = IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
        IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)] = value;
        if (IdentityHashMap.EntryIterator.this.traversalTable != IdentityHashMap.this.table) {
          IdentityHashMap.this.put(IdentityHashMap.EntryIterator.this.traversalTable[this.index], value);
        }
        return oldValue;
      }
      
      public boolean equals(Object o)
      {
        if (this.index < 0) {
          return super.equals(o);
        }
        if (!(o instanceof Map.Entry)) {
          return false;
        }
        Map.Entry<?, ?> e = (Map.Entry)o;
        return (e.getKey() == IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index])) && 
          (e.getValue() == IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)]);
      }
      
      public int hashCode()
      {
        if (IdentityHashMap.EntryIterator.this.lastReturnedIndex < 0) {
          return super.hashCode();
        }
        return 
          System.identityHashCode(IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index])) ^ System.identityHashCode(IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)]);
      }
      
      public String toString()
      {
        if (this.index < 0) {
          return super.toString();
        }
        return IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index]) + "=" + IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
      }
      
      private void checkIndexForEntryUse()
      {
        if (this.index < 0) {
          throw new IllegalStateException("Entry was removed");
        }
      }
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
      return new IdentityHashMap.KeyIterator(IdentityHashMap.this, null);
    }
    
    public int size()
    {
      return IdentityHashMap.this.size;
    }
    
    public boolean contains(Object o)
    {
      return IdentityHashMap.this.containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      int oldSize = IdentityHashMap.this.size;
      IdentityHashMap.this.remove(o);
      return IdentityHashMap.this.size != oldSize;
    }
    
    public boolean removeAll(Collection<?> c)
    {
      Objects.requireNonNull(c);
      boolean modified = false;
      for (Iterator<K> i = iterator(); i.hasNext();) {
        if (c.contains(i.next()))
        {
          i.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public void clear()
    {
      IdentityHashMap.this.clear();
    }
    
    public int hashCode()
    {
      int result = 0;
      for (K key : this) {
        result += System.identityHashCode(key);
      }
      return result;
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] a)
    {
      int expectedModCount = IdentityHashMap.this.modCount;
      int size = size();
      if (a.length < size) {
        a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }
      Object[] tab = IdentityHashMap.this.table;
      int ti = 0;
      for (int si = 0; si < tab.length; si += 2)
      {
        Object key;
        if ((key = tab[si]) != null)
        {
          if (ti >= size) {
            throw new ConcurrentModificationException();
          }
          a[(ti++)] = IdentityHashMap.unmaskNull(key);
        }
      }
      if ((ti < size) || (expectedModCount != IdentityHashMap.this.modCount)) {
        throw new ConcurrentModificationException();
      }
      if (ti < a.length) {
        a[ti] = null;
      }
      return a;
    }
    
    public Spliterator<K> spliterator()
    {
      return new IdentityHashMap.KeySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
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
      return new IdentityHashMap.ValueIterator(IdentityHashMap.this, null);
    }
    
    public int size()
    {
      return IdentityHashMap.this.size;
    }
    
    public boolean contains(Object o)
    {
      return IdentityHashMap.this.containsValue(o);
    }
    
    public boolean remove(Object o)
    {
      for (Iterator<V> i = iterator(); i.hasNext();) {
        if (i.next() == o)
        {
          i.remove();
          return true;
        }
      }
      return false;
    }
    
    public void clear()
    {
      IdentityHashMap.this.clear();
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] a)
    {
      int expectedModCount = IdentityHashMap.this.modCount;
      int size = size();
      if (a.length < size) {
        a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }
      Object[] tab = IdentityHashMap.this.table;
      int ti = 0;
      for (int si = 0; si < tab.length; si += 2) {
        if (tab[si] != null)
        {
          if (ti >= size) {
            throw new ConcurrentModificationException();
          }
          a[(ti++)] = tab[(si + 1)];
        }
      }
      if ((ti < size) || (expectedModCount != IdentityHashMap.this.modCount)) {
        throw new ConcurrentModificationException();
      }
      if (ti < a.length) {
        a[ti] = null;
      }
      return a;
    }
    
    public Spliterator<V> spliterator()
    {
      return new IdentityHashMap.ValueSpliterator(IdentityHashMap.this, 0, -1, 0, 0);
    }
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es = this.entrySet;
    if (es != null) {
      return es;
    }
    return this.entrySet = new EntrySet(null);
  }
  
  private class EntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    private EntrySet() {}
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new IdentityHashMap.EntryIterator(IdentityHashMap.this, null);
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      return IdentityHashMap.this.containsMapping(entry.getKey(), entry.getValue());
    }
    
    public boolean remove(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      return IdentityHashMap.this.removeMapping(entry.getKey(), entry.getValue());
    }
    
    public int size()
    {
      return IdentityHashMap.this.size;
    }
    
    public void clear()
    {
      IdentityHashMap.this.clear();
    }
    
    public boolean removeAll(Collection<?> c)
    {
      Objects.requireNonNull(c);
      boolean modified = false;
      for (Iterator<Map.Entry<K, V>> i = iterator(); i.hasNext();) {
        if (c.contains(i.next()))
        {
          i.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] a)
    {
      int expectedModCount = IdentityHashMap.this.modCount;
      int size = size();
      if (a.length < size) {
        a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }
      Object[] tab = IdentityHashMap.this.table;
      int ti = 0;
      for (int si = 0; si < tab.length; si += 2)
      {
        Object key;
        if ((key = tab[si]) != null)
        {
          if (ti >= size) {
            throw new ConcurrentModificationException();
          }
          a[(ti++)] = new AbstractMap.SimpleEntry(IdentityHashMap.unmaskNull(key), tab[(si + 1)]);
        }
      }
      if ((ti < size) || (expectedModCount != IdentityHashMap.this.modCount)) {
        throw new ConcurrentModificationException();
      }
      if (ti < a.length) {
        a[ti] = null;
      }
      return a;
    }
    
    public Spliterator<Map.Entry<K, V>> spliterator()
    {
      return new IdentityHashMap.EntrySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
    }
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    s.defaultWriteObject();
    

    s.writeInt(this.size);
    

    Object[] tab = this.table;
    for (int i = 0; i < tab.length; i += 2)
    {
      Object key = tab[i];
      if (key != null)
      {
        s.writeObject(unmaskNull(key));
        s.writeObject(tab[(i + 1)]);
      }
    }
  }
  
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    

    int size = s.readInt();
    if (size < 0) {
      throw new StreamCorruptedException("Illegal mappings count: " + size);
    }
    int cap = capacity(size);
    SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, [Ljava.lang.Object.class, cap);
    init(cap);
    for (int i = 0; i < size; i++)
    {
      K key = s.readObject();
      
      V value = s.readObject();
      putForCreate(key, value);
    }
  }
  
  private void putForCreate(K key, V value)
    throws StreamCorruptedException
  {
    Object k = maskNull(key);
    Object[] tab = this.table;
    int len = tab.length;
    int i = hash(k, len);
    Object item;
    while ((item = tab[i]) != null)
    {
      if (item == k) {
        throw new StreamCorruptedException();
      }
      i = nextKeyIndex(i, len);
    }
    tab[i] = k;
    tab[(i + 1)] = value;
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    Objects.requireNonNull(action);
    int expectedModCount = this.modCount;
    
    Object[] t = this.table;
    for (int index = 0; index < t.length; index += 2)
    {
      Object k = t[index];
      if (k != null) {
        action.accept(unmaskNull(k), t[(index + 1)]);
      }
      if (this.modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    Objects.requireNonNull(function);
    int expectedModCount = this.modCount;
    
    Object[] t = this.table;
    for (int index = 0; index < t.length; index += 2)
    {
      Object k = t[index];
      if (k != null) {
        t[(index + 1)] = function.apply(unmaskNull(k), t[(index + 1)]);
      }
      if (this.modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  static class IdentityHashMapSpliterator<K, V>
  {
    final IdentityHashMap<K, V> map;
    int index;
    int fence;
    int est;
    int expectedModCount;
    
    IdentityHashMapSpliterator(IdentityHashMap<K, V> map, int origin, int fence, int est, int expectedModCount)
    {
      this.map = map;
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
        this.est = this.map.size;
        this.expectedModCount = this.map.modCount;
        hi = this.fence = this.map.table.length;
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
    extends IdentityHashMap.IdentityHashMapSpliterator<K, V>
    implements Spliterator<K>
  {
    KeySpliterator(IdentityHashMap<K, V> map, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public KeySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1 & 0xFFFFFFFE;
      return lo >= mid ? null : 
        new KeySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      IdentityHashMap<K, V> m;
      Object[] a;
      int i;
      int hi;
      if (((m = this.map) != null) && ((a = m.table) != null) && ((i = this.index) >= 0) && 
        ((this.index = hi = getFence()) <= a.length))
      {
        for (; i < hi; i += 2)
        {
          Object key;
          if ((key = a[i]) != null) {
            action.accept(IdentityHashMap.unmaskNull(key));
          }
        }
        if (m.modCount == this.expectedModCount) {
          return;
        }
      }
      throw new ConcurrentModificationException();
    }
    
    public boolean tryAdvance(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      Object[] a = this.map.table;
      int hi = getFence();
      while (this.index < hi)
      {
        Object key = a[this.index];
        this.index += 2;
        if (key != null)
        {
          action.accept(IdentityHashMap.unmaskNull(key));
          if (this.map.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
          }
          return true;
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return ((this.fence < 0) || (this.est == this.map.size) ? 64 : 0) | 0x1;
    }
  }
  
  static final class ValueSpliterator<K, V>
    extends IdentityHashMap.IdentityHashMapSpliterator<K, V>
    implements Spliterator<V>
  {
    ValueSpliterator(IdentityHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public ValueSpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1 & 0xFFFFFFFE;
      return lo >= mid ? null : 
        new ValueSpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      IdentityHashMap<K, V> m;
      Object[] a;
      int i;
      int hi;
      if (((m = this.map) != null) && ((a = m.table) != null) && ((i = this.index) >= 0) && 
        ((this.index = hi = getFence()) <= a.length))
      {
        for (; i < hi; i += 2) {
          if (a[i] != null)
          {
            V v = a[(i + 1)];
            action.accept(v);
          }
        }
        if (m.modCount == this.expectedModCount) {
          return;
        }
      }
      throw new ConcurrentModificationException();
    }
    
    public boolean tryAdvance(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      Object[] a = this.map.table;
      int hi = getFence();
      while (this.index < hi)
      {
        Object key = a[this.index];
        V v = a[(this.index + 1)];
        this.index += 2;
        if (key != null)
        {
          action.accept(v);
          if (this.map.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
          }
          return true;
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return (this.fence < 0) || (this.est == this.map.size) ? 64 : 0;
    }
  }
  
  static final class EntrySpliterator<K, V>
    extends IdentityHashMap.IdentityHashMapSpliterator<K, V>
    implements Spliterator<Map.Entry<K, V>>
  {
    EntrySpliterator(IdentityHashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public EntrySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1 & 0xFFFFFFFE;
      return lo >= mid ? null : 
        new EntrySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      IdentityHashMap<K, V> m;
      Object[] a;
      int i;
      int hi;
      if (((m = this.map) != null) && ((a = m.table) != null) && ((i = this.index) >= 0) && 
        ((this.index = hi = getFence()) <= a.length))
      {
        for (; i < hi; i += 2)
        {
          Object key = a[i];
          if (key != null)
          {
            K k = IdentityHashMap.unmaskNull(key);
            V v = a[(i + 1)];
            action
              .accept(new AbstractMap.SimpleImmutableEntry(k, v));
          }
        }
        if (m.modCount == this.expectedModCount) {
          return;
        }
      }
      throw new ConcurrentModificationException();
    }
    
    public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      Object[] a = this.map.table;
      int hi = getFence();
      while (this.index < hi)
      {
        Object key = a[this.index];
        V v = a[(this.index + 1)];
        this.index += 2;
        if (key != null)
        {
          K k = IdentityHashMap.unmaskNull(key);
          action
            .accept(new AbstractMap.SimpleImmutableEntry(k, v));
          if (this.map.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
          }
          return true;
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return ((this.fence < 0) || (this.est == this.map.size) ? 64 : 0) | 0x1;
    }
  }
}
