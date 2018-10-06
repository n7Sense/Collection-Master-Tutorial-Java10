package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import jdk.internal.misc.JavaObjectInputStreamAccess;
import jdk.internal.misc.SharedSecrets;

public class Hashtable<K, V>  extends Dictionary<K, V>   implements Map<K, V>, Cloneable, Serializable {
  private transient Entry<?, ?>[] table;
  private transient int count;
  private int threshold;
  private float loadFactor;
  private transient int modCount = 0;
  private static final long serialVersionUID = 1421746759512286392L;
  private static final int MAX_ARRAY_SIZE = 2147483639;
  private volatile transient Set<K> keySet;
  private volatile transient Set<Map.Entry<K, V>> entrySet;
  private volatile transient Collection<V> values;
  private static final int KEYS = 0;
  private static final int VALUES = 1;
  private static final int ENTRIES = 2;
  
  public Hashtable(int initialCapacity, float loadFactor)
  {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
    if ((loadFactor <= 0.0F) || (Float.isNaN(loadFactor))) {
      throw new IllegalArgumentException("Illegal Load: " + loadFactor);
    }
    if (initialCapacity == 0) {
      initialCapacity = 1;
    }
    this.loadFactor = loadFactor;
    this.table = new Entry[initialCapacity];
    this.threshold = ((int)Math.min(initialCapacity * loadFactor, 2.147484E+009F));
  }
  
  public Hashtable(int initialCapacity)
  {
    this(initialCapacity, 0.75F);
  }
  
  public Hashtable()
  {
    this(11, 0.75F);
  }
  
  public Hashtable(Map<? extends K, ? extends V> t)
  {
    this(Math.max(2 * t.size(), 11), 0.75F);
    putAll(t);
  }
  
  Hashtable(Void dummy) {}
  
  public synchronized int size()
  {
    return this.count;
  }
  
  public synchronized boolean isEmpty()
  {
    return this.count == 0;
  }
  
  public synchronized Enumeration<K> keys()
  {
    return getEnumeration(0);
  }
  
  public synchronized Enumeration<V> elements()
  {
    return getEnumeration(1);
  }
  
  public synchronized boolean contains(Object value)
  {
    if (value == null) {
      throw new NullPointerException();
    }
    Entry<?, ?>[] tab = this.table;
    for (int i = tab.length; i-- > 0;) {
      for (Entry<?, ?> e = tab[i]; e != null; e = e.next) {
        if (e.value.equals(value)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean containsValue(Object value)
  {
    return contains(value);
  }
  
  public synchronized boolean containsKey(Object key)
  {
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?, ?> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key))) {
        return true;
      }
    }
    return false;
  }
  
  public synchronized V get(Object key)
  {
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?, ?> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key))) {
        return e.value;
      }
    }
    return null;
  }
  
  protected void rehash()
  {
    int oldCapacity = this.table.length;
    Entry<?, ?>[] oldMap = this.table;
    

    int newCapacity = (oldCapacity << 1) + 1;
    if (newCapacity - 2147483639 > 0)
    {
      if (oldCapacity == 2147483639) {
        return;
      }
      newCapacity = 2147483639;
    }
    Entry<?, ?>[] newMap = new Entry[newCapacity];
    
    this.modCount += 1;
    this.threshold = ((int)Math.min(newCapacity * this.loadFactor, 2.147484E+009F));
    this.table = newMap;
    for (int i = oldCapacity; i-- > 0;) {
      for (old = oldMap[i]; old != null;)
      {
        Entry<K, V> e = old;
        old = old.next;
        
        int index = (e.hash & 0x7FFFFFFF) % newCapacity;
        e.next = newMap[index];
        newMap[index] = e;
      }
    }
    Entry<K, V> old;
  }
  
  private void addEntry(int hash, K key, V value, int index)
  {
    Entry<?, ?>[] tab = this.table;
    if (this.count >= this.threshold)
    {
      rehash();
      
      tab = this.table;
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
    }
    Entry<K, V> e = tab[index];
    tab[index] = new Entry(hash, key, value, e);
    this.count += 1;
    this.modCount += 1;
  }
  
  public synchronized V put(K key, V value)
  {
    if (value == null) {
      throw new NullPointerException();
    }
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K, V> entry = tab[index]; entry != null; entry = entry.next) {
      if ((entry.hash == hash) && (entry.key.equals(key)))
      {
        V old = entry.value;
        entry.value = value;
        return old;
      }
    }
    addEntry(hash, key, value, index);
    return null;
  }
  
  public synchronized V remove(Object key)
  {
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    Entry<K, V> e = tab[index];
    for (Entry<K, V> prev = null; e != null; e = e.next)
    {
      if ((e.hash == hash) && (e.key.equals(key)))
      {
        if (prev != null) {
          prev.next = e.next;
        } else {
          tab[index] = e.next;
        }
        this.modCount += 1;
        this.count -= 1;
        V oldValue = e.value;
        e.value = null;
        return oldValue;
      }
      prev = e;
    }
    return null;
  }
  
  public synchronized void putAll(Map<? extends K, ? extends V> t)
  {
    for (Map.Entry<? extends K, ? extends V> e : t.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  public synchronized void clear()
  {
    Entry<?, ?>[] tab = this.table;
    int index = tab.length;
    for (;;)
    {
      index--;
      if (index < 0) {
        break;
      }
      tab[index] = null;
    }
    this.modCount += 1;
    this.count = 0;
  }
  
  public synchronized Object clone()
  {
    Hashtable<?, ?> t = cloneHashtable();
    t.table = new Entry[this.table.length];
    for (int i = this.table.length; i-- > 0;) {
      t.table[i] = (this.table[i] != null ? (Entry)this.table[i].clone() : null);
    }
    t.keySet = null;
    t.entrySet = null;
    t.values = null;
    t.modCount = 0;
    return t;
  }
  
  final Hashtable<?, ?> cloneHashtable()
  {
    try
    {
      return (Hashtable)super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError(e);
    }
  }
  
  public synchronized String toString()
  {
    int max = size() - 1;
    if (max == -1) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder();
    Iterator<Map.Entry<K, V>> it = entrySet().iterator();
    
    sb.append('{');
    for (int i = 0;; i++)
    {
      Map.Entry<K, V> e = (Map.Entry)it.next();
      K key = e.getKey();
      V value = e.getValue();
      sb.append(key == this ? "(this Map)" : key.toString());
      sb.append('=');
      sb.append(value == this ? "(this Map)" : value.toString());
      if (i == max) {
        return '}';
      }
      sb.append(", ");
    }
  }
  
  private <T> Enumeration<T> getEnumeration(int type)
  {
    if (this.count == 0) {
      return Collections.emptyEnumeration();
    }
    return new Enumerator(type, false);
  }
  
  private <T> Iterator<T> getIterator(int type)
  {
    if (this.count == 0) {
      return Collections.emptyIterator();
    }
    return new Enumerator(type, true);
  }
  
  public Set<K> keySet()
  {
    if (this.keySet == null) {
      this.keySet = Collections.synchronizedSet(new KeySet(null), this);
    }
    return this.keySet;
  }
  
  private class KeySet
    extends AbstractSet<K>
  {
    private KeySet() {}
    
    public Iterator<K> iterator()
    {
      return Hashtable.this.getIterator(0);
    }
    
    public int size()
    {
      return Hashtable.this.count;
    }
    
    public boolean contains(Object o)
    {
      return Hashtable.this.containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      return Hashtable.this.remove(o) != null;
    }
    
    public void clear()
    {
      Hashtable.this.clear();
    }
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    if (this.entrySet == null) {
      this.entrySet = Collections.synchronizedSet(new EntrySet(null), this);
    }
    return this.entrySet;
  }
  
  private class EntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    private EntrySet() {}
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return Hashtable.this.getIterator(2);
    }
    
    public boolean add(Map.Entry<K, V> o)
    {
      return super.add(o);
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      Object key = entry.getKey();
      Hashtable.Entry<?, ?>[] tab = Hashtable.this.table;
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Hashtable.Entry<?, ?> e = tab[index]; e != null; e = e.next) {
        if ((e.hash == hash) && (e.equals(entry))) {
          return true;
        }
      }
      return false;
    }
    
    public boolean remove(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> entry = (Map.Entry)o;
      Object key = entry.getKey();
      Hashtable.Entry<?, ?>[] tab = Hashtable.this.table;
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      

      Hashtable.Entry<K, V> e = tab[index];
      for (Hashtable.Entry<K, V> prev = null; e != null; e = e.next)
      {
        if ((e.hash == hash) && (e.equals(entry)))
        {
          if (prev != null) {
            prev.next = e.next;
          } else {
            tab[index] = e.next;
          }
          e.value = null;
          Hashtable.access$508(Hashtable.this);
          Hashtable.access$210(Hashtable.this);
          return true;
        }
        prev = e;
      }
      return false;
    }
    
    public int size()
    {
      return Hashtable.this.count;
    }
    
    public void clear()
    {
      Hashtable.this.clear();
    }
  }
  
  public Collection<V> values()
  {
    if (this.values == null) {
      this.values = Collections.synchronizedCollection(new ValueCollection(null), this);
    }
    return this.values;
  }
  
  private class ValueCollection
    extends AbstractCollection<V>
  {
    private ValueCollection() {}
    
    public Iterator<V> iterator()
    {
      return Hashtable.this.getIterator(1);
    }
    
    public int size()
    {
      return Hashtable.this.count;
    }
    
    public boolean contains(Object o)
    {
      return Hashtable.this.containsValue(o);
    }
    
    public void clear()
    {
      Hashtable.this.clear();
    }
  }
  
  public synchronized boolean equals(Object o)
  {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Map)) {
      return false;
    }
    Map<?, ?> t = (Map)o;
    if (t.size() != size()) {
      return false;
    }
    try
    {
      for (Map.Entry<K, V> e : entrySet())
      {
        K key = e.getKey();
        V value = e.getValue();
        if (value == null)
        {
          if ((t.get(key) != null) || (!t.containsKey(key))) {
            return false;
          }
        }
        else if (!value.equals(t.get(key))) {
          return false;
        }
      }
    }
    catch (ClassCastException unused)
    {
      return false;
    }
    catch (NullPointerException unused)
    {
      return false;
    }
    return true;
  }
  
  public synchronized int hashCode()
  {
    int h = 0;
    if ((this.count == 0) || (this.loadFactor < 0.0F)) {
      return h;
    }
    this.loadFactor = (-this.loadFactor);
    Entry<?, ?>[] tab = this.table;
    for (Entry<?, ?> entry : tab) {
      while (entry != null)
      {
        h += entry.hashCode();
        entry = entry.next;
      }
    }
    this.loadFactor = (-this.loadFactor);
    
    return h;
  }
  
  public synchronized V getOrDefault(Object key, V defaultValue)
  {
    V result = get(key);
    return null == result ? defaultValue : result;
  }
  
  public synchronized void forEach(BiConsumer<? super K, ? super V> action)
  {
    Objects.requireNonNull(action);
    
    int expectedModCount = this.modCount;
    
    Entry<?, ?>[] tab = this.table;
    for (Entry<?, ?> entry : tab) {
      while (entry != null)
      {
        action.accept(entry.key, entry.value);
        entry = entry.next;
        if (expectedModCount != this.modCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    Objects.requireNonNull(function);
    
    int expectedModCount = this.modCount;
    
    Entry<K, V>[] tab = this.table;
    for (Entry<K, V> entry : tab) {
      while (entry != null)
      {
        entry.value = Objects.requireNonNull(function
          .apply(entry.key, entry.value));
        entry = entry.next;
        if (expectedModCount != this.modCount) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  public synchronized V putIfAbsent(K key, V value)
  {
    Objects.requireNonNull(value);
    

    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K, V> entry = tab[index]; entry != null; entry = entry.next) {
      if ((entry.hash == hash) && (entry.key.equals(key)))
      {
        V old = entry.value;
        if (old == null) {
          entry.value = value;
        }
        return old;
      }
    }
    addEntry(hash, key, value, index);
    return null;
  }
  
  public synchronized boolean remove(Object key, Object value)
  {
    Objects.requireNonNull(value);
    
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    Entry<K, V> e = tab[index];
    for (Entry<K, V> prev = null; e != null; e = e.next)
    {
      if ((e.hash == hash) && (e.key.equals(key)) && (e.value.equals(value)))
      {
        if (prev != null) {
          prev.next = e.next;
        } else {
          tab[index] = e.next;
        }
        e.value = null;
        this.modCount += 1;
        this.count -= 1;
        return true;
      }
      prev = e;
    }
    return false;
  }
  
  public synchronized boolean replace(K key, V oldValue, V newValue)
  {
    Objects.requireNonNull(oldValue);
    Objects.requireNonNull(newValue);
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K, V> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key)))
      {
        if (e.value.equals(oldValue))
        {
          e.value = newValue;
          return true;
        }
        return false;
      }
    }
    return false;
  }
  
  public synchronized V replace(K key, V value)
  {
    Objects.requireNonNull(value);
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K, V> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key)))
      {
        V oldValue = e.value;
        e.value = value;
        return oldValue;
      }
    }
    return null;
  }
  
  public synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
  {
    Objects.requireNonNull(mappingFunction);
    
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K, V> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key))) {
        return e.value;
      }
    }
    int mc = this.modCount;
    V newValue = mappingFunction.apply(key);
    if (mc != this.modCount) {
      throw new ConcurrentModificationException();
    }
    if (newValue != null) {
      addEntry(hash, key, newValue, index);
    }
    return newValue;
  }
  
  public synchronized V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    Entry<K, V> e = tab[index];
    for (Entry<K, V> prev = null; e != null; e = e.next)
    {
      if ((e.hash == hash) && (e.key.equals(key)))
      {
        int mc = this.modCount;
        V newValue = remappingFunction.apply(key, e.value);
        if (mc != this.modCount) {
          throw new ConcurrentModificationException();
        }
        if (newValue == null)
        {
          if (prev != null) {
            prev.next = e.next;
          } else {
            tab[index] = e.next;
          }
          this.modCount = (mc + 1);
          this.count -= 1;
        }
        else
        {
          e.value = newValue;
        }
        return newValue;
      }
      prev = e;
    }
    return null;
  }
  
  public synchronized V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    Entry<K, V> e = tab[index];
    for (Entry<K, V> prev = null; e != null; e = e.next)
    {
      if ((e.hash == hash) && (Objects.equals(e.key, key)))
      {
        int mc = this.modCount;
        V newValue = remappingFunction.apply(key, e.value);
        if (mc != this.modCount) {
          throw new ConcurrentModificationException();
        }
        if (newValue == null)
        {
          if (prev != null) {
            prev.next = e.next;
          } else {
            tab[index] = e.next;
          }
          this.modCount = (mc + 1);
          this.count -= 1;
        }
        else
        {
          e.value = newValue;
        }
        return newValue;
      }
      prev = e;
    }
    int mc = this.modCount;
    V newValue = remappingFunction.apply(key, null);
    if (mc != this.modCount) {
      throw new ConcurrentModificationException();
    }
    if (newValue != null) {
      addEntry(hash, key, newValue, index);
    }
    return newValue;
  }
  
  public synchronized V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    
    Entry<?, ?>[] tab = this.table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    Entry<K, V> e = tab[index];
    for (Entry<K, V> prev = null; e != null; e = e.next)
    {
      if ((e.hash == hash) && (e.key.equals(key)))
      {
        int mc = this.modCount;
        V newValue = remappingFunction.apply(e.value, value);
        if (mc != this.modCount) {
          throw new ConcurrentModificationException();
        }
        if (newValue == null)
        {
          if (prev != null) {
            prev.next = e.next;
          } else {
            tab[index] = e.next;
          }
          this.modCount = (mc + 1);
          this.count -= 1;
        }
        else
        {
          e.value = newValue;
        }
        return newValue;
      }
      prev = e;
    }
    if (value != null) {
      addEntry(hash, key, value, index);
    }
    return value;
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    writeHashtable(s);
  }
  
  void writeHashtable(ObjectOutputStream s)
    throws IOException
  {
    Entry<Object, Object> entryStack = null;
    synchronized (this)
    {
      s.defaultWriteObject();
      

      s.writeInt(this.table.length);
      s.writeInt(this.count);
      for (Entry<?, ?> entry : this.table) {
        while (entry != null)
        {
          entryStack = new Entry(0, entry.key, entry.value, entryStack);
          
          entry = entry.next;
        }
      }
    }
    while (entryStack != null)
    {
      s.writeObject(entryStack.key);
      s.writeObject(entryStack.value);
      entryStack = entryStack.next;
    }
  }
  
  final void defaultWriteHashtable(ObjectOutputStream s, int length, float loadFactor)
    throws IOException
  {
    this.threshold = ((int)Math.min(length * loadFactor, 2.147484E+009F));
    this.loadFactor = loadFactor;
    s.defaultWriteObject();
  }
  
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    readHashtable(s);
  }
  
  void readHashtable(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    if ((this.loadFactor <= 0.0F) || (Float.isNaN(this.loadFactor))) {
      throw new StreamCorruptedException("Illegal Load: " + this.loadFactor);
    }
    int origlength = s.readInt();
    int elements = s.readInt();
    if (elements < 0) {
      throw new StreamCorruptedException("Illegal # of Elements: " + elements);
    }
    origlength = Math.max(origlength, (int)(elements / this.loadFactor) + 1);
    




    int length = (int)((elements + elements / 20) / this.loadFactor) + 3;
    if ((length > elements) && ((length & 0x1) == 0)) {
      length--;
    }
    length = Math.min(length, origlength);
    if (length < 0) {
      length = origlength;
    }
    SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, [Ljava.util.Map.Entry.class, length);
    this.table = new Entry[length];
    this.threshold = ((int)Math.min(length * this.loadFactor, 2.147484E+009F));
    this.count = 0;
    for (; elements > 0; elements--)
    {
      K key = s.readObject();
      
      V value = s.readObject();
      
      reconstitutionPut(this.table, key, value);
    }
  }
  
  private void reconstitutionPut(Entry<?, ?>[] tab, K key, V value)
    throws StreamCorruptedException
  {
    if (value == null) {
      throw new StreamCorruptedException();
    }
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?, ?> e = tab[index]; e != null; e = e.next) {
      if ((e.hash == hash) && (e.key.equals(key))) {
        throw new StreamCorruptedException();
      }
    }
    Entry<K, V> e = tab[index];
    tab[index] = new Entry(hash, key, value, e);
    this.count += 1;
  }
  
  private static class Entry<K, V>
    implements Map.Entry<K, V>
  {
    final int hash;
    final K key;
    V value;
    Entry<K, V> next;
    
    protected Entry(int hash, K key, V value, Entry<K, V> next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
    
    protected Object clone()
    {
      return new Entry(this.hash, this.key, this.value, 
        this.next == null ? null : (Entry)this.next.clone());
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
      if (value == null) {
        throw new NullPointerException();
      }
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
      
      return (this.key == null ? e.getKey() == null : this.key.equals(e.getKey())) && (this.value == null ? e
        .getValue() == null : this.value.equals(e.getValue()));
    }
    
    public int hashCode()
    {
      return this.hash ^ Objects.hashCode(this.value);
    }
    
    public String toString()
    {
      return this.key.toString() + "=" + this.value.toString();
    }
  }
  
  private class Enumerator<T>
    implements Enumeration<T>, Iterator<T>
  {
    final Hashtable.Entry<?, ?>[] table = Hashtable.this.table;
    int index = this.table.length;
    Hashtable.Entry<?, ?> entry;
    Hashtable.Entry<?, ?> lastReturned;
    final int type;
    final boolean iterator;
    protected int expectedModCount = Hashtable.this.modCount;
    
    Enumerator(int type, boolean iterator)
    {
      this.type = type;
      this.iterator = iterator;
    }
    
    public boolean hasMoreElements()
    {
      Hashtable.Entry<?, ?> e = this.entry;
      int i = this.index;
      Hashtable.Entry<?, ?>[] t = this.table;
      while ((e == null) && (i > 0)) {
        e = t[(--i)];
      }
      this.entry = e;
      this.index = i;
      return e != null;
    }
    
    public T nextElement()
    {
      Hashtable.Entry<?, ?> et = this.entry;
      int i = this.index;
      Hashtable.Entry<?, ?>[] t = this.table;
      while ((et == null) && (i > 0)) {
        et = t[(--i)];
      }
      this.entry = et;
      this.index = i;
      if (et != null)
      {
        Hashtable.Entry<?, ?> e = this.lastReturned = this.entry;
        this.entry = e.next;
        return this.type == 1 ? e.value : this.type == 0 ? e.key : e;
      }
      throw new NoSuchElementException("Hashtable Enumerator");
    }
    
    public boolean hasNext()
    {
      return hasMoreElements();
    }
    
    public T next()
    {
      if (Hashtable.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      return nextElement();
    }
    
    public void remove()
    {
      if (!this.iterator) {
        throw new UnsupportedOperationException();
      }
      if (this.lastReturned == null) {
        throw new IllegalStateException("Hashtable Enumerator");
      }
      if (Hashtable.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      synchronized (Hashtable.this)
      {
        Hashtable.Entry<?, ?>[] tab = Hashtable.this.table;
        int index = (this.lastReturned.hash & 0x7FFFFFFF) % tab.length;
        

        Hashtable.Entry<K, V> e = tab[index];
        for (Hashtable.Entry<K, V> prev = null; e != null; e = e.next)
        {
          if (e == this.lastReturned)
          {
            if (prev == null) {
              tab[index] = e.next;
            } else {
              prev.next = e.next;
            }
            this.expectedModCount += 1;
            this.lastReturned = null;
            Hashtable.access$508(Hashtable.this);
            Hashtable.access$210(Hashtable.this); return;
          }
          prev = e;
        }
        throw new ConcurrentModificationException();
      }
    }
  }
}
