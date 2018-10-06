package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import jdk.internal.misc.JavaObjectInputStreamAccess;
import jdk.internal.misc.SharedSecrets;

public class HashMap<K, V>
  extends AbstractMap<K, V>
  implements Map<K, V>, Cloneable, Serializable
{
  private static final long serialVersionUID = 362498820763181265L;
  static final int DEFAULT_INITIAL_CAPACITY = 16;
  static final int MAXIMUM_CAPACITY = 1073741824;
  static final float DEFAULT_LOAD_FACTOR = 0.75F;
  static final int TREEIFY_THRESHOLD = 8;
  static final int UNTREEIFY_THRESHOLD = 6;
  static final int MIN_TREEIFY_CAPACITY = 64;
  transient Node<K, V>[] table;
  transient Set<Map.Entry<K, V>> entrySet;
  transient int size;
  transient int modCount;
  int threshold;
  final float loadFactor;
  
  static class Node<K, V>
    implements Map.Entry<K, V>
  {
    final int hash;
    final K key;
    V value;
    Node<K, V> next;
    
    Node(int hash, K key, V value, Node<K, V> next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
    
    public final K getKey()
    {
      return this.key;
    }
    
    public final V getValue()
    {
      return this.value;
    }
    
    public final String toString()
    {
      return this.key + "=" + this.value;
    }
    
    public final int hashCode()
    {
      return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
    }
    
    public final V setValue(V newValue)
    {
      V oldValue = this.value;
      this.value = newValue;
      return oldValue;
    }
    
    public final boolean equals(Object o)
    {
      if (o == this) {
        return true;
      }
      if ((o instanceof Map.Entry))
      {
        Map.Entry<?, ?> e = (Map.Entry)o;
        if ((Objects.equals(this.key, e.getKey())) && 
          (Objects.equals(this.value, e.getValue()))) {
          return true;
        }
      }
      return false;
    }
  }
  
  static final int hash(Object key)
  {
    int h;
    return key == null ? 0 : (h = key.hashCode()) ^ h >>> 16;
  }
  
  static Class<?> comparableClassFor(Object x)
  {
    if ((x instanceof Comparable))
    {
      Class<?> c;
      if ((c = x.getClass()) == String.class) {
        return c;
      }
      Type[] ts;
      if ((ts = c.getGenericInterfaces()) != null) {
        for (Type t : ts)
        {
          ParameterizedType p;
          if (((t instanceof ParameterizedType)) && 
            ((p = (ParameterizedType)t).getRawType() == Comparable.class))
          {
            Type[] as;
            if (((as = p.getActualTypeArguments()) != null) && (as.length == 1) && (as[0] == c)) {
              return c;
            }
          }
        }
      }
    }
    return null;
  }
  
  static int compareComparables(Class<?> kc, Object k, Object x)
  {
    return (x == null) || (x.getClass() != kc) ? 0 : 
      ((Comparable)k).compareTo(x);
  }
  
  static final int tableSizeFor(int cap)
  {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return n >= 1073741824 ? 1073741824 : n < 0 ? 1 : n + 1;
  }
  
  public HashMap(int initialCapacity, float loadFactor)
  {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }
    if (initialCapacity > 1073741824) {
      initialCapacity = 1073741824;
    }
    if ((loadFactor <= 0.0F) || (Float.isNaN(loadFactor))) {
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    }
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
  }
  
  public HashMap(int initialCapacity)
  {
    this(initialCapacity, 0.75F);
  }
  
  public HashMap()
  {
    this.loadFactor = 0.75F;
  }
  
  public HashMap(Map<? extends K, ? extends V> m)
  {
    this.loadFactor = 0.75F;
    putMapEntries(m, false);
  }
  
  final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict)
  {
    int s = m.size();
    float ft;
    if (s > 0)
    {
      if (this.table == null)
      {
        ft = s / this.loadFactor + 1.0F;
        
        int t = ft < 1.073742E+009F ? (int)ft : 1073741824;
        if (t > this.threshold) {
          this.threshold = tableSizeFor(t);
        }
      }
      else if (s > this.threshold)
      {
        resize();
      }
      for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
      {
        K key = e.getKey();
        V value = e.getValue();
        putVal(hash(key), key, value, false, evict);
      }
    }
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  public V get(Object key)
  {
    Node<K, V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
  }
  
  final Node<K, V> getNode(int hash, Object key)
  {
    Node<K, V>[] tab;
    int n;
    Node<K, V> first;
    if (((tab = this.table) != null) && ((n = tab.length) > 0) && ((first = tab[(n - 1 & hash)]) != null))
    {
      K k;
      if ((first.hash == hash) && (((k = first.key) == key) || ((key != null) && 
        (key.equals(k))))) {
        return first;
      }
      Node<K, V> e;
      if ((e = first.next) != null)
      {
        if ((first instanceof TreeNode)) {
          return ((TreeNode)first).getTreeNode(hash, key);
        }
        do
        {
          K k;
          if ((e.hash == hash) && (((k = e.key) == key) || ((key != null) && 
            (key.equals(k))))) {
            return e;
          }
        } while ((e = e.next) != null);
      }
    }
    return null;
  }
  
  public boolean containsKey(Object key)
  {
    return getNode(hash(key), key) != null;
  }
  
  public V put(K key, V value)
  {
    return putVal(hash(key), key, value, false, true);
  }
  
  final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)
  {
    Node<K, V>[] tab;
    int n;
    int n;
    if (((tab = this.table) == null) || ((n = tab.length) == 0)) {
      n = (tab = resize()).length;
    }
    int i;
    Node<K, V> p;
    if ((p = tab[(i = n - 1 & hash)]) == null)
    {
      tab[i] = newNode(hash, key, value, null);
    }
    else
    {
      K k;
      Node<K, V> e;
      Node<K, V> e;
      if ((p.hash == hash) && (((k = p.key) == key) || ((key != null) && 
        (key.equals(k)))))
      {
        e = p;
      }
      else
      {
        Node<K, V> e;
        if ((p instanceof TreeNode)) {
          e = ((TreeNode)p).putTreeVal(this, tab, hash, key, value);
        } else {
          for (int binCount = 0;; binCount++)
          {
            if ((e = p.next) == null)
            {
              p.next = newNode(hash, key, value, null);
              if (binCount < 7) {
                break;
              }
              treeifyBin(tab, hash); break;
            }
            K k;
            if ((e.hash == hash) && (((k = e.key) == key) || ((key != null) && 
              (key.equals(k))))) {
              break;
            }
            p = e;
          }
        }
      }
      if (e != null)
      {
        V oldValue = e.value;
        if ((!onlyIfAbsent) || (oldValue == null)) {
          e.value = value;
        }
        afterNodeAccess(e);
        return oldValue;
      }
    }
    this.modCount += 1;
    if (++this.size > this.threshold) {
      resize();
    }
    afterNodeInsertion(evict);
    return null;
  }
  
  final Node<K, V>[] resize()
  {
    Node<K, V>[] oldTab = this.table;
    int oldCap = oldTab == null ? 0 : oldTab.length;
    int oldThr = this.threshold;
    int newThr = 0;
    int newCap;
    if (oldCap > 0)
    {
      if (oldCap >= 1073741824)
      {
        this.threshold = 2147483647;
        return oldTab;
      }
      int newCap;
      if (((newCap = oldCap << 1) < 1073741824) && (oldCap >= 16)) {
        newThr = oldThr << 1;
      }
    }
    else
    {
      int newCap;
      if (oldThr > 0)
      {
        newCap = oldThr;
      }
      else
      {
        newCap = 16;
        newThr = 12;
      }
    }
    if (newThr == 0)
    {
      float ft = newCap * this.loadFactor;
      
      newThr = (newCap < 1073741824) && (ft < 1.073742E+009F) ? (int)ft : 2147483647;
    }
    this.threshold = newThr;
    
    Node<K, V>[] newTab = new Node[newCap];
    this.table = newTab;
    if (oldTab != null) {
      for (int j = 0; j < oldCap; j++)
      {
        Node<K, V> e;
        if ((e = oldTab[j]) != null)
        {
          oldTab[j] = null;
          if (e.next == null)
          {
            newTab[(e.hash & newCap - 1)] = e;
          }
          else if ((e instanceof TreeNode))
          {
            ((TreeNode)e).split(this, newTab, j, oldCap);
          }
          else
          {
            Node<K, V> loHead = null;Node<K, V> loTail = null;
            Node<K, V> hiHead = null;Node<K, V> hiTail = null;
            Node<K, V> next;
            do
            {
              next = e.next;
              if ((e.hash & oldCap) == 0)
              {
                if (loTail == null) {
                  loHead = e;
                } else {
                  loTail.next = e;
                }
                loTail = e;
              }
              else
              {
                if (hiTail == null) {
                  hiHead = e;
                } else {
                  hiTail.next = e;
                }
                hiTail = e;
              }
            } while ((e = next) != null);
            if (loTail != null)
            {
              loTail.next = null;
              newTab[j] = loHead;
            }
            if (hiTail != null)
            {
              hiTail.next = null;
              newTab[(j + oldCap)] = hiHead;
            }
          }
        }
      }
    }
    return newTab;
  }
  
  final void treeifyBin(Node<K, V>[] tab, int hash)
  {
    int n;
    if ((tab == null) || ((n = tab.length) < 64))
    {
      resize();
    }
    else
    {
      int n;
      int index;
      Node<K, V> e;
      if ((e = tab[(index = n - 1 & hash)]) != null)
      {
        TreeNode<K, V> hd = null;TreeNode<K, V> tl = null;
        do
        {
          TreeNode<K, V> p = replacementTreeNode(e, null);
          if (tl == null)
          {
            hd = p;
          }
          else
          {
            p.prev = tl;
            tl.next = p;
          }
          tl = p;
        } while ((e = e.next) != null);
        if ((tab[index] =  = hd) != null) {
          hd.treeify(tab);
        }
      }
    }
  }
  
  public void putAll(Map<? extends K, ? extends V> m)
  {
    putMapEntries(m, true);
  }
  
  public V remove(Object key)
  {
    Node<K, V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ? 
      null : e.value;
  }
  
  final Node<K, V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable)
  {
    Node<K, V>[] tab;
    int n;
    int index;
    Node<K, V> p;
    if (((tab = this.table) != null) && ((n = tab.length) > 0) && ((p = tab[(index = n - 1 & hash)]) != null))
    {
      Node<K, V> node = null;
      K k;
      if ((p.hash == hash) && (((k = p.key) == key) || ((key != null) && 
        (key.equals(k)))))
      {
        node = p;
      }
      else
      {
        Node<K, V> e;
        if ((e = p.next) != null) {
          if ((p instanceof TreeNode)) {
            node = ((TreeNode)p).getTreeNode(hash, key);
          } else {
            do
            {
              if (e.hash == hash)
              {
                K k;
                if ((k = e.key) != key)
                {
                  if (key != null) {
                    if (!key.equals(k)) {}
                  }
                }
                else
                {
                  node = e;
                  break;
                }
              }
              p = e;
            } while ((e = e.next) != null);
          }
        }
      }
      V v;
      if ((node != null) && ((!matchValue) || ((v = node.value) == value) || ((value != null) && 
        (value.equals(v)))))
      {
        if ((node instanceof TreeNode)) {
          ((TreeNode)node).removeTreeNode(this, tab, movable);
        } else if (node == p) {
          tab[index] = node.next;
        } else {
          p.next = node.next;
        }
        this.modCount += 1;
        this.size -= 1;
        afterNodeRemoval(node);
        return node;
      }
    }
    return null;
  }
  
  public void clear()
  {
    this.modCount += 1;
    Node<K, V>[] tab;
    if (((tab = this.table) != null) && (this.size > 0))
    {
      this.size = 0;
      for (int i = 0; i < tab.length; i++) {
        tab[i] = null;
      }
    }
  }
  
  public boolean containsValue(Object value)
  {
    Node<K, V>[] tab;
    if (((tab = this.table) != null) && (this.size > 0))
    {
      Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
      for (int j = 0; j < i; j++) {
        for (Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next)
        {
          V v;
          if (((v = e.value) == value) || ((value != null) && 
            (value.equals(v)))) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public Set<K> keySet()
  {
    Set<K> ks = this.keySet;
    if (ks == null)
    {
      ks = new KeySet();
      this.keySet = ks;
    }
    return ks;
  }
  
  final class KeySet
    extends AbstractSet<K>
  {
    KeySet() {}
    
    public final int size()
    {
      return HashMap.this.size;
    }
    
    public final void clear()
    {
      HashMap.this.clear();
    }
    
    public final Iterator<K> iterator()
    {
      return new HashMap.KeyIterator(HashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      return HashMap.this.containsKey(o);
    }
    
    public final boolean remove(Object key)
    {
      return HashMap.this.removeNode(HashMap.hash(key), key, null, false, true) != null;
    }
    
    public final Spliterator<K> spliterator()
    {
      return new HashMap.KeySpliterator(HashMap.this, 0, -1, 0, 0);
    }
    
    public final void forEach(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab;
      if ((HashMap.this.size > 0) && ((tab = HashMap.this.table) != null))
      {
        int mc = HashMap.this.modCount;
        HashMap.Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
        for (int j = 0; j < i; j++) {
          for (HashMap.Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next) {
            action.accept(e.key);
          }
        }
        if (HashMap.this.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
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
  
  final class Values
    extends AbstractCollection<V>
  {
    Values() {}
    
    public final int size()
    {
      return HashMap.this.size;
    }
    
    public final void clear()
    {
      HashMap.this.clear();
    }
    
    public final Iterator<V> iterator()
    {
      return new HashMap.ValueIterator(HashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      return HashMap.this.containsValue(o);
    }
    
    public final Spliterator<V> spliterator()
    {
      return new HashMap.ValueSpliterator(HashMap.this, 0, -1, 0, 0);
    }
    
    public final void forEach(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab;
      if ((HashMap.this.size > 0) && ((tab = HashMap.this.table) != null))
      {
        int mc = HashMap.this.modCount;
        HashMap.Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
        for (int j = 0; j < i; j++) {
          for (HashMap.Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next) {
            action.accept(e.value);
          }
        }
        if (HashMap.this.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es;
    return (es = this.entrySet) == null ? (this.entrySet = new EntrySet()) : es;
  }
  
  final class EntrySet
    extends AbstractSet<Map.Entry<K, V>>
  {
    EntrySet() {}
    
    public final int size()
    {
      return HashMap.this.size;
    }
    
    public final void clear()
    {
      HashMap.this.clear();
    }
    
    public final Iterator<Map.Entry<K, V>> iterator()
    {
      return new HashMap.EntryIterator(HashMap.this);
    }
    
    public final boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      HashMap.Node<K, V> candidate = HashMap.this.getNode(HashMap.hash(key), key);
      return (candidate != null) && (candidate.equals(e));
    }
    
    public final boolean remove(Object o)
    {
      if ((o instanceof Map.Entry))
      {
        Map.Entry<?, ?> e = (Map.Entry)o;
        Object key = e.getKey();
        Object value = e.getValue();
        return HashMap.this.removeNode(HashMap.hash(key), key, value, true, true) != null;
      }
      return false;
    }
    
    public final Spliterator<Map.Entry<K, V>> spliterator()
    {
      return new HashMap.EntrySpliterator(HashMap.this, 0, -1, 0, 0);
    }
    
    public final void forEach(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab;
      if ((HashMap.this.size > 0) && ((tab = HashMap.this.table) != null))
      {
        int mc = HashMap.this.modCount;
        HashMap.Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
        for (int j = 0; j < i; j++) {
          for (HashMap.Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next) {
            action.accept(e);
          }
        }
        if (HashMap.this.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
  }
  
  public V getOrDefault(Object key, V defaultValue)
  {
    Node<K, V> e;
    return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
  }
  
  public V putIfAbsent(K key, V value)
  {
    return putVal(hash(key), key, value, true, true);
  }
  
  public boolean remove(Object key, Object value)
  {
    return removeNode(hash(key), key, value, true, true) != null;
  }
  
  public boolean replace(K key, V oldValue, V newValue)
  {
    Node<K, V> e;
    V v;
    if (((e = getNode(hash(key), key)) != null) && (((v = e.value) == oldValue) || ((v != null) && 
      (v.equals(oldValue)))))
    {
      e.value = newValue;
      afterNodeAccess(e);
      return true;
    }
    return false;
  }
  
  public V replace(K key, V value)
  {
    Node<K, V> e;
    if ((e = getNode(hash(key), key)) != null)
    {
      V oldValue = e.value;
      e.value = value;
      afterNodeAccess(e);
      return oldValue;
    }
    return null;
  }
  
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
  {
    if (mappingFunction == null) {
      throw new NullPointerException();
    }
    int hash = hash(key);
    
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    Node<K, V>[] tab;
    int n;
    Node<K, V>[] tab;
    int n;
    if ((this.size > this.threshold) || ((tab = this.table) == null) || ((n = tab.length) == 0)) {
      n = (tab = resize()).length;
    }
    int i;
    Node<K, V> first;
    if ((first = tab[(i = n - 1 & hash)]) != null)
    {
      if ((first instanceof TreeNode))
      {
        old = (t = (TreeNode)first).getTreeNode(hash, key);
      }
      else
      {
        Node<K, V> e = first;
        do
        {
          K k;
          if ((e.hash == hash) && (((k = e.key) == key) || ((key != null) && 
            (key.equals(k)))))
          {
            old = e;
            break;
          }
          binCount++;
        } while ((e = e.next) != null);
      }
      V oldValue;
      if ((old != null) && ((oldValue = old.value) != null))
      {
        afterNodeAccess(old);
        return oldValue;
      }
    }
    int mc = this.modCount;
    V v = mappingFunction.apply(key);
    if (mc != this.modCount) {
      throw new ConcurrentModificationException();
    }
    if (v == null) {
      return null;
    }
    if (old != null)
    {
      old.value = v;
      afterNodeAccess(old);
      return v;
    }
    if (t != null)
    {
      t.putTreeVal(this, tab, hash, key, v);
    }
    else
    {
      tab[i] = newNode(hash, key, v, first);
      if (binCount >= 7) {
        treeifyBin(tab, hash);
      }
    }
    this.modCount = (mc + 1);
    this.size += 1;
    afterNodeInsertion(true);
    return v;
  }
  
  public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    if (remappingFunction == null) {
      throw new NullPointerException();
    }
    int hash = hash(key);
    Node<K, V> e;
    V oldValue;
    if (((e = getNode(hash, key)) != null) && ((oldValue = e.value) != null))
    {
      int mc = this.modCount;
      V v = remappingFunction.apply(key, oldValue);
      if (mc != this.modCount) {
        throw new ConcurrentModificationException();
      }
      if (v != null)
      {
        e.value = v;
        afterNodeAccess(e);
        return v;
      }
      removeNode(hash, key, null, false, true);
    }
    return null;
  }
  
  public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    if (remappingFunction == null) {
      throw new NullPointerException();
    }
    int hash = hash(key);
    
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    Node<K, V>[] tab;
    int n;
    Node<K, V>[] tab;
    int n;
    if ((this.size > this.threshold) || ((tab = this.table) == null) || ((n = tab.length) == 0)) {
      n = (tab = resize()).length;
    }
    int i;
    Node<K, V> first;
    if ((first = tab[(i = n - 1 & hash)]) != null) {
      if ((first instanceof TreeNode))
      {
        old = (t = (TreeNode)first).getTreeNode(hash, key);
      }
      else
      {
        Node<K, V> e = first;
        do
        {
          K k;
          if ((e.hash == hash) && (((k = e.key) == key) || ((key != null) && 
            (key.equals(k)))))
          {
            old = e;
            break;
          }
          binCount++;
        } while ((e = e.next) != null);
      }
    }
    V oldValue = old == null ? null : old.value;
    int mc = this.modCount;
    V v = remappingFunction.apply(key, oldValue);
    if (mc != this.modCount) {
      throw new ConcurrentModificationException();
    }
    if (old != null)
    {
      if (v != null)
      {
        old.value = v;
        afterNodeAccess(old);
      }
      else
      {
        removeNode(hash, key, null, false, true);
      }
    }
    else if (v != null)
    {
      if (t != null)
      {
        t.putTreeVal(this, tab, hash, key, v);
      }
      else
      {
        tab[i] = newNode(hash, key, v, first);
        if (binCount >= 7) {
          treeifyBin(tab, hash);
        }
      }
      this.modCount = (mc + 1);
      this.size += 1;
      afterNodeInsertion(true);
    }
    return v;
  }
  
  public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
  {
    if (value == null) {
      throw new NullPointerException();
    }
    if (remappingFunction == null) {
      throw new NullPointerException();
    }
    int hash = hash(key);
    
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    Node<K, V>[] tab;
    int n;
    Node<K, V>[] tab;
    int n;
    if ((this.size > this.threshold) || ((tab = this.table) == null) || ((n = tab.length) == 0)) {
      n = (tab = resize()).length;
    }
    int i;
    Node<K, V> first;
    if ((first = tab[(i = n - 1 & hash)]) != null) {
      if ((first instanceof TreeNode))
      {
        old = (t = (TreeNode)first).getTreeNode(hash, key);
      }
      else
      {
        Node<K, V> e = first;
        do
        {
          K k;
          if ((e.hash == hash) && (((k = e.key) == key) || ((key != null) && 
            (key.equals(k)))))
          {
            old = e;
            break;
          }
          binCount++;
        } while ((e = e.next) != null);
      }
    }
    if (old != null)
    {
      V v;
      if (old.value != null)
      {
        int mc = this.modCount;
        V v = remappingFunction.apply(old.value, value);
        if (mc != this.modCount) {
          throw new ConcurrentModificationException();
        }
      }
      else
      {
        v = value;
      }
      if (v != null)
      {
        old.value = v;
        afterNodeAccess(old);
      }
      else
      {
        removeNode(hash, key, null, false, true);
      }
      return v;
    }
    if (value != null)
    {
      if (t != null)
      {
        t.putTreeVal(this, tab, hash, key, value);
      }
      else
      {
        tab[i] = newNode(hash, key, value, first);
        if (binCount >= 7) {
          treeifyBin(tab, hash);
        }
      }
      this.modCount += 1;
      this.size += 1;
      afterNodeInsertion(true);
    }
    return value;
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    if (action == null) {
      throw new NullPointerException();
    }
    Node<K, V>[] tab;
    if ((this.size > 0) && ((tab = this.table) != null))
    {
      int mc = this.modCount;
      Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
      for (int j = 0; j < i; j++) {
        for (Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next) {
          action.accept(e.key, e.value);
        }
      }
      if (this.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    if (function == null) {
      throw new NullPointerException();
    }
    Node<K, V>[] tab;
    if ((this.size > 0) && ((tab = this.table) != null))
    {
      int mc = this.modCount;
      Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
      for (int j = 0; j < i; j++) {
        for (Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next) {
          e.value = function.apply(e.key, e.value);
        }
      }
      if (this.modCount != mc) {
        throw new ConcurrentModificationException();
      }
    }
  }
  
  public Object clone()
  {
    try
    {
      result = (HashMap)super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      HashMap<K, V> result;
      throw new InternalError(e);
    }
    HashMap<K, V> result;
    result.reinitialize();
    result.putMapEntries(this, false);
    return result;
  }
  
  final float loadFactor()
  {
    return this.loadFactor;
  }
  
  final int capacity()
  {
    return 
      this.threshold > 0 ? this.threshold : this.table != null ? this.table.length : 
      16;
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    int buckets = capacity();
    
    s.defaultWriteObject();
    s.writeInt(buckets);
    s.writeInt(this.size);
    internalWriteEntries(s);
  }
  
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    reinitialize();
    if ((this.loadFactor <= 0.0F) || (Float.isNaN(this.loadFactor))) {
      throw new InvalidObjectException("Illegal load factor: " + this.loadFactor);
    }
    s.readInt();
    int mappings = s.readInt();
    if (mappings < 0) {
      throw new InvalidObjectException("Illegal mappings count: " + mappings);
    }
    if (mappings > 0)
    {
      float lf = Math.min(Math.max(0.25F, this.loadFactor), 4.0F);
      float fc = mappings / lf + 1.0F;
      



      int cap = fc >= 1.073742E+009F ? 1073741824 : fc < 16.0F ? 16 : tableSizeFor((int)fc);
      float ft = cap * lf;
      
      this.threshold = ((cap < 1073741824) && (ft < 1.073742E+009F) ? (int)ft : 2147483647);
      


      SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, [Ljava.util.Map.Entry.class, cap);
      
      Node<K, V>[] tab = new Node[cap];
      this.table = tab;
      for (int i = 0; i < mappings; i++)
      {
        K key = s.readObject();
        
        V value = s.readObject();
        putVal(hash(key), key, value, false, false);
      }
    }
  }
  
  abstract class HashIterator
  {
    HashMap.Node<K, V> next;
    HashMap.Node<K, V> current;
    int expectedModCount;
    int index;
    
    HashIterator()
    {
      this.expectedModCount = HashMap.this.modCount;
      HashMap.Node<K, V>[] t = HashMap.this.table;
      this.current = (this.next = null);
      this.index = 0;
      while ((t != null) && (HashMap.this.size > 0) && 
        (this.index < t.length) && ((this.next = t[(this.index++)]) == null)) {}
    }
    
    public final boolean hasNext()
    {
      return this.next != null;
    }
    
    final HashMap.Node<K, V> nextNode()
    {
      HashMap.Node<K, V> e = this.next;
      if (HashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (e == null) {
        throw new NoSuchElementException();
      }
      HashMap.Node<K, V>[] t;
      while (((this.next = (this.current = e).next) == null) && ((t = HashMap.this.table) != null) && 
        (this.index < t.length) && ((this.next = t[(this.index++)]) == null)) {}
      return e;
    }
    
    public final void remove()
    {
      HashMap.Node<K, V> p = this.current;
      if (p == null) {
        throw new IllegalStateException();
      }
      if (HashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.current = null;
      HashMap.this.removeNode(p.hash, p.key, null, false, false);
      this.expectedModCount = HashMap.this.modCount;
    }
  }
  
  final class KeyIterator
    extends HashMap<K, V>.HashIterator
    implements Iterator<K>
  {
    KeyIterator()
    {
      super();
    }
    
    public final K next()
    {
      return nextNode().key;
    }
  }
  
  final class ValueIterator
    extends HashMap<K, V>.HashIterator
    implements Iterator<V>
  {
    ValueIterator()
    {
      super();
    }
    
    public final V next()
    {
      return nextNode().value;
    }
  }
  
  final class EntryIterator
    extends HashMap<K, V>.HashIterator
    implements Iterator<Map.Entry<K, V>>
  {
    EntryIterator()
    {
      super();
    }
    
    public final Map.Entry<K, V> next()
    {
      return nextNode();
    }
  }
  
  static class HashMapSpliterator<K, V>
  {
    final HashMap<K, V> map;
    HashMap.Node<K, V> current;
    int index;
    int fence;
    int est;
    int expectedModCount;
    
    HashMapSpliterator(HashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
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
        HashMap<K, V> m = this.map;
        this.est = m.size;
        this.expectedModCount = m.modCount;
        HashMap.Node<K, V>[] tab = m.table;
        hi = this.fence = tab == null ? 0 : tab.length;
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
    extends HashMap.HashMapSpliterator<K, V>
    implements Spliterator<K>
  {
    KeySpliterator(HashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public KeySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return (lo >= mid) || (this.current != null) ? null : 
        new KeySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap<K, V> m = this.map;
      HashMap.Node<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab == null ? 0 : tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab != null) && (tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        HashMap.Node<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            action.accept(p.key);
            p = p.next;
          }
        } while ((p != null) || (i < hi));
        if (m.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super K> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab = this.map.table;
      int hi;
      if ((tab != null) && (tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            K k = this.current.key;
            this.current = this.current.next;
            action.accept(k);
            if (this.map.modCount != this.expectedModCount) {
              throw new ConcurrentModificationException();
            }
            return true;
          }
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
    extends HashMap.HashMapSpliterator<K, V>
    implements Spliterator<V>
  {
    ValueSpliterator(HashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public ValueSpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return (lo >= mid) || (this.current != null) ? null : 
        new ValueSpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap<K, V> m = this.map;
      HashMap.Node<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab == null ? 0 : tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab != null) && (tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        HashMap.Node<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            action.accept(p.value);
            p = p.next;
          }
        } while ((p != null) || (i < hi));
        if (m.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super V> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab = this.map.table;
      int hi;
      if ((tab != null) && (tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            V v = this.current.value;
            this.current = this.current.next;
            action.accept(v);
            if (this.map.modCount != this.expectedModCount) {
              throw new ConcurrentModificationException();
            }
            return true;
          }
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
    extends HashMap.HashMapSpliterator<K, V>
    implements Spliterator<Map.Entry<K, V>>
  {
    EntrySpliterator(HashMap<K, V> m, int origin, int fence, int est, int expectedModCount)
    {
      super(origin, fence, est, expectedModCount);
    }
    
    public EntrySpliterator<K, V> trySplit()
    {
      int hi = getFence();int lo = this.index;int mid = lo + hi >>> 1;
      return (lo >= mid) || (this.current != null) ? null : 
        new EntrySpliterator(this.map, lo, this.index = mid, this.est >>>= 1, this.expectedModCount);
    }
    
    public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap<K, V> m = this.map;
      HashMap.Node<K, V>[] tab = m.table;
      int hi;
      int mc;
      if ((hi = this.fence) < 0)
      {
        int mc = this.expectedModCount = m.modCount;
        hi = this.fence = tab == null ? 0 : tab.length;
      }
      else
      {
        mc = this.expectedModCount;
      }
      int i;
      if ((tab != null) && (tab.length >= hi) && ((i = this.index) >= 0) && ((i < (this.index = hi)) || (this.current != null)))
      {
        HashMap.Node<K, V> p = this.current;
        this.current = null;
        do
        {
          if (p == null)
          {
            p = tab[(i++)];
          }
          else
          {
            action.accept(p);
            p = p.next;
          }
        } while ((p != null) || (i < hi));
        if (m.modCount != mc) {
          throw new ConcurrentModificationException();
        }
      }
    }
    
    public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action)
    {
      if (action == null) {
        throw new NullPointerException();
      }
      HashMap.Node<K, V>[] tab = this.map.table;
      int hi;
      if ((tab != null) && (tab.length >= (hi = getFence())) && (this.index >= 0)) {
        while ((this.current != null) || (this.index < hi)) {
          if (this.current == null)
          {
            this.current = tab[(this.index++)];
          }
          else
          {
            HashMap.Node<K, V> e = this.current;
            this.current = this.current.next;
            action.accept(e);
            if (this.map.modCount != this.expectedModCount) {
              throw new ConcurrentModificationException();
            }
            return true;
          }
        }
      }
      return false;
    }
    
    public int characteristics()
    {
      return ((this.fence < 0) || (this.est == this.map.size) ? 64 : 0) | 0x1;
    }
  }
  
  Node<K, V> newNode(int hash, K key, V value, Node<K, V> next)
  {
    return new Node(hash, key, value, next);
  }
  
  Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next)
  {
    return new Node(p.hash, p.key, p.value, next);
  }
  
  TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K, V> next)
  {
    return new TreeNode(hash, key, value, next);
  }
  
  TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next)
  {
    return new TreeNode(p.hash, p.key, p.value, next);
  }
  
  void reinitialize()
  {
    this.table = null;
    this.entrySet = null;
    this.keySet = null;
    this.values = null;
    this.modCount = 0;
    this.threshold = 0;
    this.size = 0;
  }
  
  void afterNodeAccess(Node<K, V> p) {}
  
  void afterNodeInsertion(boolean evict) {}
  
  void afterNodeRemoval(Node<K, V> p) {}
  
  void internalWriteEntries(ObjectOutputStream s)
    throws IOException
  {
    Node<K, V>[] tab;
    if ((this.size > 0) && ((tab = this.table) != null))
    {
      Node<K, V>[] arrayOfNode1 = tab;int i = arrayOfNode1.length;
      for (int j = 0; j < i; j++) {
        for (Node<K, V> e = arrayOfNode1[j]; e != null; e = e.next)
        {
          s.writeObject(e.key);
          s.writeObject(e.value);
        }
      }
    }
  }
  
  static final class TreeNode<K, V>
    extends LinkedHashMap.Entry<K, V>
  {
    TreeNode<K, V> parent;
    TreeNode<K, V> left;
    TreeNode<K, V> right;
    TreeNode<K, V> prev;
    boolean red;
    
    TreeNode(int hash, K key, V val, HashMap.Node<K, V> next)
    {
      super(key, val, next);
    }
    
    final TreeNode<K, V> root()
    {
      TreeNode<K, V> r = this;
      for (;;)
      {
        TreeNode<K, V> p;
        if ((p = r.parent) == null) {
          return r;
        }
        r = p;
      }
    }
    
    static <K, V> void moveRootToFront(HashMap.Node<K, V>[] tab, TreeNode<K, V> root)
    {
      int n;
      if ((root != null) && (tab != null) && ((n = tab.length) > 0))
      {
        int index = n - 1 & root.hash;
        TreeNode<K, V> first = (TreeNode)tab[index];
        if (root != first)
        {
          tab[index] = root;
          TreeNode<K, V> rp = root.prev;
          HashMap.Node<K, V> rn;
          if ((rn = root.next) != null) {
            ((TreeNode)rn).prev = rp;
          }
          if (rp != null) {
            rp.next = rn;
          }
          if (first != null) {
            first.prev = root;
          }
          root.next = first;
          root.prev = null;
        }
        assert (checkInvariants(root));
      }
    }
    
    final TreeNode<K, V> find(int h, Object k, Class<?> kc)
    {
      TreeNode<K, V> p = this;
      do
      {
        TreeNode<K, V> pl = p.left;TreeNode<K, V> pr = p.right;
        int ph;
        if ((ph = p.hash) > h)
        {
          p = pl;
        }
        else if (ph < h)
        {
          p = pr;
        }
        else
        {
          K pk;
          if (((pk = p.key) == k) || ((k != null) && (k.equals(pk)))) {
            return p;
          }
          if (pl == null)
          {
            p = pr;
          }
          else if (pr == null)
          {
            p = pl;
          }
          else
          {
            int dir;
            if (((kc != null) || 
              ((kc = HashMap.comparableClassFor(k)) != null)) && 
              ((dir = HashMap.compareComparables(kc, k, pk)) != 0))
            {
              p = dir < 0 ? pl : pr;
            }
            else
            {
              TreeNode<K, V> q;
              if ((q = pr.find(h, k, kc)) != null) {
                return q;
              }
              p = pl;
            }
          }
        }
      } while (p != null);
      return null;
    }
    
    final TreeNode<K, V> getTreeNode(int h, Object k)
    {
      return (this.parent != null ? root() : this).find(h, k, null);
    }
    
    static int tieBreakOrder(Object a, Object b)
    {
      int d;
      int d;
      if ((a == null) || (b == null) || 
      
        ((d = a.getClass().getName().compareTo(b.getClass().getName())) == 0)) {
        d = System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1;
      }
      return d;
    }
    
    final void treeify(HashMap.Node<K, V>[] tab)
    {
      TreeNode<K, V> root = null;
      TreeNode<K, V> next;
      for (TreeNode<K, V> x = this; x != null; x = next)
      {
        next = (TreeNode)x.next;
        x.left = (x.right = null);
        if (root == null)
        {
          x.parent = null;
          x.red = false;
          root = x;
        }
        else
        {
          K k = x.key;
          int h = x.hash;
          Class<?> kc = null;
          TreeNode<K, V> p = root;
          for (;;)
          {
            K pk = p.key;
            int ph;
            int dir;
            int dir;
            if ((ph = p.hash) > h)
            {
              dir = -1;
            }
            else
            {
              int dir;
              if (ph < h)
              {
                dir = 1;
              }
              else
              {
                int dir;
                if (((kc == null) && 
                  ((kc = HashMap.comparableClassFor(k)) == null)) || 
                  ((dir = HashMap.compareComparables(kc, k, pk)) == 0)) {
                  dir = tieBreakOrder(k, pk);
                }
              }
            }
            TreeNode<K, V> xp = p;
            if ((p = dir <= 0 ? p.left : p.right) == null)
            {
              x.parent = xp;
              if (dir <= 0) {
                xp.left = x;
              } else {
                xp.right = x;
              }
              root = balanceInsertion(root, x);
              break;
            }
          }
        }
      }
      moveRootToFront(tab, root);
    }
    
    final HashMap.Node<K, V> untreeify(HashMap<K, V> map)
    {
      HashMap.Node<K, V> hd = null;HashMap.Node<K, V> tl = null;
      for (HashMap.Node<K, V> q = this; q != null; q = q.next)
      {
        HashMap.Node<K, V> p = map.replacementNode(q, null);
        if (tl == null) {
          hd = p;
        } else {
          tl.next = p;
        }
        tl = p;
      }
      return hd;
    }
    
    final TreeNode<K, V> putTreeVal(HashMap<K, V> map, HashMap.Node<K, V>[] tab, int h, K k, V v)
    {
      Class<?> kc = null;
      boolean searched = false;
      TreeNode<K, V> root = this.parent != null ? root() : this;
      TreeNode<K, V> p = root;
      for (;;)
      {
        int ph;
        int dir;
        int dir;
        if ((ph = p.hash) > h)
        {
          dir = -1;
        }
        else
        {
          int dir;
          if (ph < h)
          {
            dir = 1;
          }
          else
          {
            K pk;
            if (((pk = p.key) == k) || ((k != null) && (k.equals(pk)))) {
              return p;
            }
            int dir;
            if (((kc == null) && 
              ((kc = HashMap.comparableClassFor(k)) == null)) || 
              ((dir = HashMap.compareComparables(kc, k, pk)) == 0))
            {
              if (!searched)
              {
                searched = true;
                TreeNode<K, V> ch;
                TreeNode<K, V> q;
                TreeNode<K, V> q;
                if (((ch = p.left) == null) || 
                  ((q = ch.find(h, k, kc)) == null))
                {
                  if ((ch = p.right) != null) {
                    if ((q = ch.find(h, k, kc)) == null) {}
                  }
                }
                else {
                  return q;
                }
              }
              dir = tieBreakOrder(k, pk);
            }
          }
        }
        TreeNode<K, V> xp = p;
        if ((p = dir <= 0 ? p.left : p.right) == null)
        {
          HashMap.Node<K, V> xpn = xp.next;
          TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
          if (dir <= 0) {
            xp.left = x;
          } else {
            xp.right = x;
          }
          xp.next = x;
          x.parent = (x.prev = xp);
          if (xpn != null) {
            ((TreeNode)xpn).prev = x;
          }
          moveRootToFront(tab, balanceInsertion(root, x));
          return null;
        }
      }
    }
    
    final void removeTreeNode(HashMap<K, V> map, HashMap.Node<K, V>[] tab, boolean movable)
    {
      int n;
      if ((tab == null) || ((n = tab.length) == 0)) {
        return;
      }
      int n;
      int index = n - 1 & this.hash;
      TreeNode<K, V> first = (TreeNode)tab[index];TreeNode<K, V> root = first;
      TreeNode<K, V> succ = (TreeNode)this.next;TreeNode<K, V> pred = this.prev;
      if (pred == null)
      {
        TreeNode<K, V> tmp62_60 = succ;first = tmp62_60;tab[index] = tmp62_60;
      }
      else
      {
        pred.next = succ;
      }
      if (succ != null) {
        succ.prev = pred;
      }
      if (first == null) {
        return;
      }
      if (root.parent != null) {
        root = root.root();
      }
      TreeNode<K, V> rl;
      if ((root == null) || ((movable) && ((root.right == null) || ((rl = root.left) == null) || (rl.left == null))))
      {
        tab[index] = first.untreeify(map);
        return;
      }
      TreeNode<K, V> p = this;TreeNode<K, V> pl = this.left;TreeNode<K, V> pr = this.right;
      TreeNode<K, V> replacement;
      TreeNode<K, V> replacement;
      if ((pl != null) && (pr != null))
      {
        TreeNode<K, V> s = pr;
        TreeNode<K, V> sl;
        while ((sl = s.left) != null) {
          s = sl;
        }
        boolean c = s.red;s.red = p.red;p.red = c;
        TreeNode<K, V> sr = s.right;
        TreeNode<K, V> pp = p.parent;
        if (s == pr)
        {
          p.parent = s;
          s.right = p;
        }
        else
        {
          TreeNode<K, V> sp = s.parent;
          if ((p.parent = sp) != null) {
            if (s == sp.left) {
              sp.left = p;
            } else {
              sp.right = p;
            }
          }
          if ((s.right = pr) != null) {
            pr.parent = s;
          }
        }
        p.left = null;
        if ((p.right = sr) != null) {
          sr.parent = p;
        }
        if ((s.left = pl) != null) {
          pl.parent = s;
        }
        if ((s.parent = pp) == null) {
          root = s;
        } else if (p == pp.left) {
          pp.left = s;
        } else {
          pp.right = s;
        }
        TreeNode<K, V> replacement;
        if (sr != null) {
          replacement = sr;
        } else {
          replacement = p;
        }
      }
      else
      {
        TreeNode<K, V> replacement;
        if (pl != null)
        {
          replacement = pl;
        }
        else
        {
          TreeNode<K, V> replacement;
          if (pr != null) {
            replacement = pr;
          } else {
            replacement = p;
          }
        }
      }
      if (replacement != p)
      {
        TreeNode<K, V> pp = replacement.parent = p.parent;
        if (pp == null) {
          root = replacement;
        } else if (p == pp.left) {
          pp.left = replacement;
        } else {
          pp.right = replacement;
        }
        p.left = (p.right = p.parent = null);
      }
      TreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);
      if (replacement == p)
      {
        TreeNode<K, V> pp = p.parent;
        p.parent = null;
        if (pp != null) {
          if (p == pp.left) {
            pp.left = null;
          } else if (p == pp.right) {
            pp.right = null;
          }
        }
      }
      if (movable) {
        moveRootToFront(tab, r);
      }
    }
    
    final void split(HashMap<K, V> map, HashMap.Node<K, V>[] tab, int index, int bit)
    {
      TreeNode<K, V> b = this;
      
      TreeNode<K, V> loHead = null;TreeNode<K, V> loTail = null;
      TreeNode<K, V> hiHead = null;TreeNode<K, V> hiTail = null;
      int lc = 0;int hc = 0;
      TreeNode<K, V> next;
      for (TreeNode<K, V> e = b; e != null; e = next)
      {
        next = (TreeNode)e.next;
        e.next = null;
        if ((e.hash & bit) == 0)
        {
          if ((e.prev = loTail) == null) {
            loHead = e;
          } else {
            loTail.next = e;
          }
          loTail = e;
          lc++;
        }
        else
        {
          if ((e.prev = hiTail) == null) {
            hiHead = e;
          } else {
            hiTail.next = e;
          }
          hiTail = e;
          hc++;
        }
      }
      if (loHead != null) {
        if (lc <= 6)
        {
          tab[index] = loHead.untreeify(map);
        }
        else
        {
          tab[index] = loHead;
          if (hiHead != null) {
            loHead.treeify(tab);
          }
        }
      }
      if (hiHead != null) {
        if (hc <= 6)
        {
          tab[(index + bit)] = hiHead.untreeify(map);
        }
        else
        {
          tab[(index + bit)] = hiHead;
          if (loHead != null) {
            hiHead.treeify(tab);
          }
        }
      }
    }
    
    static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root, TreeNode<K, V> p)
    {
      TreeNode<K, V> r;
      if ((p != null) && ((r = p.right) != null))
      {
        TreeNode<K, V> rl;
        if ((rl = p.right = r.left) != null) {
          rl.parent = p;
        }
        TreeNode<K, V> pp;
        if ((pp = r.parent = p.parent) == null) {
          (root = r).red = false;
        } else if (pp.left == p) {
          pp.left = r;
        } else {
          pp.right = r;
        }
        r.left = p;
        p.parent = r;
      }
      return root;
    }
    
    static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root, TreeNode<K, V> p)
    {
      TreeNode<K, V> l;
      if ((p != null) && ((l = p.left) != null))
      {
        TreeNode<K, V> lr;
        if ((lr = p.left = l.right) != null) {
          lr.parent = p;
        }
        TreeNode<K, V> pp;
        if ((pp = l.parent = p.parent) == null) {
          (root = l).red = false;
        } else if (pp.right == p) {
          pp.right = l;
        } else {
          pp.left = l;
        }
        l.right = p;
        p.parent = l;
      }
      return root;
    }
    
    static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x)
    {
      x.red = true;
      for (;;)
      {
        TreeNode<K, V> xp;
        if ((xp = x.parent) == null)
        {
          x.red = false;
          return x;
        }
        TreeNode<K, V> xpp;
        if ((!xp.red) || ((xpp = xp.parent) == null)) {
          return root;
        }
        TreeNode<K, V> xpp;
        TreeNode<K, V> xppl;
        if (xp == (xppl = xpp.left))
        {
          TreeNode<K, V> xppr;
          if (((xppr = xpp.right) != null) && (xppr.red))
          {
            xppr.red = false;
            xp.red = false;
            xpp.red = true;
            x = xpp;
          }
          else
          {
            if (x == xp.right)
            {
              root = rotateLeft(root, x = xp);
              xpp = (xp = x.parent) == null ? null : xp.parent;
            }
            if (xp != null)
            {
              xp.red = false;
              if (xpp != null)
              {
                xpp.red = true;
                root = rotateRight(root, xpp);
              }
            }
          }
        }
        else if ((xppl != null) && (xppl.red))
        {
          xppl.red = false;
          xp.red = false;
          xpp.red = true;
          x = xpp;
        }
        else
        {
          if (x == xp.left)
          {
            root = rotateRight(root, x = xp);
            xpp = (xp = x.parent) == null ? null : xp.parent;
          }
          if (xp != null)
          {
            xp.red = false;
            if (xpp != null)
            {
              xpp.red = true;
              root = rotateLeft(root, xpp);
            }
          }
        }
      }
    }
    
    static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x)
    {
      for (;;)
      {
        if ((x == null) || (x == root)) {
          return root;
        }
        TreeNode<K, V> xp;
        if ((xp = x.parent) == null)
        {
          x.red = false;
          return x;
        }
        if (x.red)
        {
          x.red = false;
          return root;
        }
        TreeNode<K, V> xpl;
        if ((xpl = xp.left) == x)
        {
          TreeNode<K, V> xpr;
          if (((xpr = xp.right) != null) && (xpr.red))
          {
            xpr.red = false;
            xp.red = true;
            root = rotateLeft(root, xp);
            xpr = (xp = x.parent) == null ? null : xp.right;
          }
          if (xpr == null)
          {
            x = xp;
          }
          else
          {
            TreeNode<K, V> sl = xpr.left;TreeNode<K, V> sr = xpr.right;
            if (((sr == null) || (!sr.red)) && ((sl == null) || (!sl.red)))
            {
              xpr.red = true;
              x = xp;
            }
            else
            {
              if ((sr == null) || (!sr.red))
              {
                if (sl != null) {
                  sl.red = false;
                }
                xpr.red = true;
                root = rotateRight(root, xpr);
                
                xpr = (xp = x.parent) == null ? null : xp.right;
              }
              if (xpr != null)
              {
                xpr.red = (xp == null ? false : xp.red);
                if ((sr = xpr.right) != null) {
                  sr.red = false;
                }
              }
              if (xp != null)
              {
                xp.red = false;
                root = rotateLeft(root, xp);
              }
              x = root;
            }
          }
        }
        else
        {
          if ((xpl != null) && (xpl.red))
          {
            xpl.red = false;
            xp.red = true;
            root = rotateRight(root, xp);
            xpl = (xp = x.parent) == null ? null : xp.left;
          }
          if (xpl == null)
          {
            x = xp;
          }
          else
          {
            TreeNode<K, V> sl = xpl.left;TreeNode<K, V> sr = xpl.right;
            if (((sl == null) || (!sl.red)) && ((sr == null) || (!sr.red)))
            {
              xpl.red = true;
              x = xp;
            }
            else
            {
              if ((sl == null) || (!sl.red))
              {
                if (sr != null) {
                  sr.red = false;
                }
                xpl.red = true;
                root = rotateLeft(root, xpl);
                
                xpl = (xp = x.parent) == null ? null : xp.left;
              }
              if (xpl != null)
              {
                xpl.red = (xp == null ? false : xp.red);
                if ((sl = xpl.left) != null) {
                  sl.red = false;
                }
              }
              if (xp != null)
              {
                xp.red = false;
                root = rotateRight(root, xp);
              }
              x = root;
            }
          }
        }
      }
    }
    
    static <K, V> boolean checkInvariants(TreeNode<K, V> t)
    {
      TreeNode<K, V> tp = t.parent;TreeNode<K, V> tl = t.left;TreeNode<K, V> tr = t.right;
      TreeNode<K, V> tb = t.prev;TreeNode<K, V> tn = (TreeNode)t.next;
      if ((tb != null) && (tb.next != t)) {
        return false;
      }
      if ((tn != null) && (tn.prev != t)) {
        return false;
      }
      if ((tp != null) && (t != tp.left) && (t != tp.right)) {
        return false;
      }
      if ((tl != null) && ((tl.parent != t) || (tl.hash > t.hash))) {
        return false;
      }
      if ((tr != null) && ((tr.parent != t) || (tr.hash < t.hash))) {
        return false;
      }
      if ((t.red) && (tl != null) && (tl.red) && (tr != null) && (tr.red)) {
        return false;
      }
      if ((tl != null) && (!checkInvariants(tl))) {
        return false;
      }
      if ((tr != null) && (!checkInvariants(tr))) {
        return false;
      }
      return true;
    }
  }
}
