package java.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LinkedHashMap<K, V>
  extends HashMap<K, V>
  implements Map<K, V>
{
  private static final long serialVersionUID = 3801124242820219131L;
  transient Entry<K, V> head;
  transient Entry<K, V> tail;
  final boolean accessOrder;
  
  static class Entry<K, V>
    extends HashMap.Node<K, V>
  {
    Entry<K, V> before;
    Entry<K, V> after;
    
    Entry(int hash, K key, V value, HashMap.Node<K, V> next)
    {
      super(key, value, next);
    }
  }
  
  private void linkNodeLast(Entry<K, V> p)
  {
    Entry<K, V> last = this.tail;
    this.tail = p;
    if (last == null)
    {
      this.head = p;
    }
    else
    {
      p.before = last;
      last.after = p;
    }
  }
  
  private void transferLinks(Entry<K, V> src, Entry<K, V> dst)
  {
    Entry<K, V> b = dst.before = src.before;
    Entry<K, V> a = dst.after = src.after;
    if (b == null) {
      this.head = dst;
    } else {
      b.after = dst;
    }
    if (a == null) {
      this.tail = dst;
    } else {
      a.before = dst;
    }
  }
  
  void reinitialize()
  {
    super.reinitialize();
    this.head = (this.tail = null);
  }
  
  HashMap.Node<K, V> newNode(int hash, K key, V value, HashMap.Node<K, V> e)
  {
    Entry<K, V> p = new Entry(hash, key, value, e);
    
    linkNodeLast(p);
    return p;
  }
  
  HashMap.Node<K, V> replacementNode(HashMap.Node<K, V> p, HashMap.Node<K, V> next)
  {
    Entry<K, V> q = (Entry)p;
    Entry<K, V> t = new Entry(q.hash, q.key, q.value, next);
    
    transferLinks(q, t);
    return t;
  }
  
  HashMap.TreeNode<K, V> newTreeNode(int hash, K key, V value, HashMap.Node<K, V> next)
  {
    HashMap.TreeNode<K, V> p = new HashMap.TreeNode(hash, key, value, next);
    linkNodeLast(p);
    return p;
  }
  
  HashMap.TreeNode<K, V> replacementTreeNode(HashMap.Node<K, V> p, HashMap.Node<K, V> next)
  {
    Entry<K, V> q = (Entry)p;
    HashMap.TreeNode<K, V> t = new HashMap.TreeNode(q.hash, q.key, q.value, next);
    transferLinks(q, t);
    return t;
  }
  
  void afterNodeRemoval(HashMap.Node<K, V> e)
  {
    Entry<K, V> p = (Entry)e;
    Entry<K, V> b = p.before;Entry<K, V> a = p.after;
    p.before = (p.after = null);
    if (b == null) {
      this.head = a;
    } else {
      b.after = a;
    }
    if (a == null) {
      this.tail = b;
    } else {
      a.before = b;
    }
  }
  
  void afterNodeInsertion(boolean evict)
  {
    Entry<K, V> first;
    if ((evict) && ((first = this.head) != null) && (removeEldestEntry(first)))
    {
      K key = first.key;
      removeNode(hash(key), key, null, false, true);
    }
  }
  
  void afterNodeAccess(HashMap.Node<K, V> e)
  {
    Entry<K, V> last;
    if ((this.accessOrder) && ((last = this.tail) != e))
    {
      Entry<K, V> p = (Entry)e;
      Entry<K, V> b = p.before;Entry<K, V> a = p.after;
      p.after = null;
      if (b == null) {
        this.head = a;
      } else {
        b.after = a;
      }
      if (a != null) {
        a.before = b;
      } else {
        last = b;
      }
      if (last == null)
      {
        this.head = p;
      }
      else
      {
        p.before = last;
        last.after = p;
      }
      this.tail = p;
      this.modCount += 1;
    }
  }
  
  void internalWriteEntries(ObjectOutputStream s)
    throws IOException
  {
    for (Entry<K, V> e = this.head; e != null; e = e.after)
    {
      s.writeObject(e.key);
      s.writeObject(e.value);
    }
  }
  
  public LinkedHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    this.accessOrder = false;
  }
  
  public LinkedHashMap(int initialCapacity)
  {
    super(initialCapacity);
    this.accessOrder = false;
  }
  
  public LinkedHashMap()
  {
    this.accessOrder = false;
  }
  
  public LinkedHashMap(Map<? extends K, ? extends V> m)
  {
    this.accessOrder = false;
    putMapEntries(m, false);
  }
  
  public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
  {
    super(initialCapacity, loadFactor);
    this.accessOrder = accessOrder;
  }
  
  public boolean containsValue(Object value)
  {
    for (Entry<K, V> e = this.head; e != null; e = e.after)
    {
      V v = e.value;
      if ((v == value) || ((value != null) && (value.equals(v)))) {
        return true;
      }
    }
    return false;
  }
  
  public V get(Object key)
  {
    HashMap.Node<K, V> e;
    if ((e = getNode(hash(key), key)) == null) {
      return null;
    }
    if (this.accessOrder) {
      afterNodeAccess(e);
    }
    return e.value;
  }
  
  public V getOrDefault(Object key, V defaultValue)
  {
    HashMap.Node<K, V> e;
    if ((e = getNode(hash(key), key)) == null) {
      return defaultValue;
    }
    if (this.accessOrder) {
      afterNodeAccess(e);
    }
    return e.value;
  }
  
  public void clear()
  {
    super.clear();
    this.head = (this.tail = null);
  }
  
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
  {
    return false;
  }
  
  public Set<K> keySet()
  {
    Set<K> ks = this.keySet;
    if (ks == null)
    {
      ks = new LinkedKeySet();
      this.keySet = ks;
    }
    return ks;
  }
  
  final class LinkedKeySet
    extends AbstractSet<K>
  {
    LinkedKeySet() {}
    
    public final int size()
    {
      return LinkedHashMap.this.size;
    }
    
    public final void clear()
    {
      LinkedHashMap.this.clear();
    }
    
    public final Iterator<K> iterator()
    {
      return new LinkedHashMap.LinkedKeyIterator(LinkedHashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      return LinkedHashMap.this.containsKey(o);
    }
    
    public final boolean remove(Object key)
    {
      return LinkedHashMap.this.removeNode(HashMap.hash(key), key, null, false, true) != null;
    }
    
    public final Spliterator<K> spliterator()
    {
      return Spliterators.spliterator(this, 81);
    }
    
    public final void forEach(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      int mc = LinkedHashMap.this.modCount;
      for (LinkedHashMap.Entry<K, V> e = LinkedHashMap.this.head; e != null; e = e.after) {
        action.accept(e.key);
      }
      if (LinkedHashMap.this.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public Collection<V> values()
  {
    Collection<V> vs = this.values;
    if (vs == null)
    {
      vs = new LinkedValues();
      this.values = vs;
    }
    return vs;
  }
  
  final class LinkedValues
    extends AbstractCollection<V>
  {
    LinkedValues() {}
    
    public final int size()
    {
      return LinkedHashMap.this.size;
    }
    
    public final void clear()
    {
      LinkedHashMap.this.clear();
    }
    
    public final Iterator<V> iterator()
    {
      return new LinkedHashMap.LinkedValueIterator(LinkedHashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      return LinkedHashMap.this.containsValue(o);
    }
    
    public final Spliterator<V> spliterator()
    {
      return Spliterators.spliterator(this, 80);
    }
    
    public final void forEach(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      int mc = LinkedHashMap.this.modCount;
      for (LinkedHashMap.Entry<K, V> e = LinkedHashMap.this.head; e != null; e = e.after) {
        action.accept(e.value);
      }
      if (LinkedHashMap.this.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es;
    return (es = this.entrySet) == null ? (this.entrySet = new LinkedEntrySet()) : es;
  }
  
  final class LinkedEntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    LinkedEntrySet() {}
    
    public final int size()
    {
      return LinkedHashMap.this.size;
    }
    
    public final void clear()
    {
      LinkedHashMap.this.clear();
    }
    
    public final Iterator<Map.Entry<K, V>> iterator()
    {
      return new LinkedHashMap.LinkedEntryIterator(LinkedHashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      HashMap.Node<K, V> candidate = LinkedHashMap.this.getNode(HashMap.hash(key), key);
      return (candidate != null) && (candidate.equals(e));
    }
    
    public final boolean remove(Object o)
    {
      if ((o instanceof Map.Entry))
      {
        Map.Entry<?, ?> e = (Map.Entry)o;
        Object key = e.getKey();
        Object value = e.getValue();
        return LinkedHashMap.this.removeNode(HashMap.hash(key), key, value, true, true) != null;
      }
      return false;
    }
    
    public final Spliterator<Map.Entry<K, V>> spliterator()
    {
      return Spliterators.spliterator(this, 81);
    }
    
    public final void forEach(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      int mc = LinkedHashMap.this.modCount;
      for (LinkedHashMap.Entry<K, V> e = LinkedHashMap.this.head; e != null; e = e.after) {
        action.accept(e);
      }
      if (LinkedHashMap.this.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    if (action == null) {
      throw new NullPointerException();
    }
    int mc = this.modCount;
    for (Entry<K, V> e = this.head; e != null; e = e.after) {
      action.accept(e.key, e.value);
    }
    if (this.modCount != mc) {
      throw new ConcurrentModificationException();
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    if (function == null) {
      throw new NullPointerException();
    }
    int mc = this.modCount;
    for (Entry<K, V> e = this.head; e != null; e = e.after) {
      e.value = function.apply(e.key, e.value);
    }
    if (this.modCount != mc) {
      throw new ConcurrentModificationException();
    }
  }
  
  abstract class LinkedHashIterator
  {
    LinkedHashMap.Entry<K, V> next;
    LinkedHashMap.Entry<K, V> current;
    int expectedModCount;
    
    LinkedHashIterator()
    {
      this.next = LinkedHashMap.this.head;
      this.expectedModCount = LinkedHashMap.this.modCount;
      this.current = null;
    }
    
    public final boolean hasNext()
    {
      return this.next != null;
    }
    
    final LinkedHashMap.Entry<K, V> nextNode()
    {
      LinkedHashMap.Entry<K, V> e = this.next;
      if (LinkedHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (e == null) {
        throw new NoSuchElementException();
      }
      this.current = e;
      this.next = e.after;
      return e;
    }
    
    public final void remove()
    {
      HashMap.Node<K, V> p = this.current;
      if (p == null) {
        throw new IllegalStateException();
      }
      if (LinkedHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.current = null;
      LinkedHashMap.this.removeNode(p.hash, p.key, null, false, false);
      this.expectedModCount = LinkedHashMap.this.modCount;
    }
  }
  
  final class LinkedKeyIterator
    extends LinkedHashMap<K, V>.LinkedHashIterator
    implements Iterator<K>
  {
    LinkedKeyIterator()
    {
      super();
    }
    
    public final K next()
    {
      return nextNode().getKey();
    }
  }
  
  final class LinkedValueIterator
    extends LinkedHashMap<K, V>.LinkedHashIterator
    implements Iterator<V>
  {
    LinkedValueIterator()
    {
      super();
    }
    
    public final V next()
    {
      return nextNode().value;
    }
  }
  
  final class LinkedEntryIterator
    extends LinkedHashMap<K, V>.LinkedHashIterator
    implements Iterator<Map.Entry<K, V>>
  {
    LinkedEntryIterator()
    {
      super();
    }
    
    public final Map.Entry<K, V> next()
    {
      return nextNode();
    }
  }
}
