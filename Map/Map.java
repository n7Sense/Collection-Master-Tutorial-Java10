package java.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract interface Map<K, V>
{
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(Object paramObject);
  
  public abstract V put(K paramK, V paramV);
  
  public abstract V remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends V> paramMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Collection<V> values();
  
  public abstract Set<Map.Entry<K, V>> entrySet();
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
  
  public V getOrDefault(Object key, V defaultValue)
  {
    V v;
    return ((v = get(key)) != null) || (containsKey(key)) ? 
      v : 
      defaultValue;
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    Objects.requireNonNull(action);
    for (Map.Entry<K, V> entry : entrySet())
    {
      try
      {
        K k = entry.getKey();
        v = entry.getValue();
      }
      catch (IllegalStateException ise)
      {
        V v;
        throw new ConcurrentModificationException(ise);
      }
      V v;
      K k;
      action.accept(k, v);
    }
  }
  
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
  {
    Objects.requireNonNull(function);
    for (Map.Entry<K, V> entry : entrySet())
    {
      try
      {
        K k = entry.getKey();
        v = entry.getValue();
      }
      catch (IllegalStateException ise)
      {
        V v;
        throw new ConcurrentModificationException(ise);
      }
      K k;
      V v = function.apply(k, v);
      try
      {
        entry.setValue(v);
      }
      catch (IllegalStateException ise)
      {
        throw new ConcurrentModificationException(ise);
      }
    }
  }
  
  public V putIfAbsent(K key, V value)
  {
    V v = get(key);
    if (v == null) {
      v = put(key, value);
    }
    return v;
  }
  
  public boolean remove(Object key, Object value)
  {
    Object curValue = get(key);
    if ((!Objects.equals(curValue, value)) || ((curValue == null) && 
      (!containsKey(key)))) {
      return false;
    }
    remove(key);
    return true;
  }
  
  public boolean replace(K key, V oldValue, V newValue)
  {
    Object curValue = get(key);
    if ((!Objects.equals(curValue, oldValue)) || ((curValue == null) && 
      (!containsKey(key)))) {
      return false;
    }
    put(key, newValue);
    return true;
  }
  
  public V replace(K key, V value)
  {
    V curValue;
    if (((curValue = get(key)) != null) || (containsKey(key))) {
      curValue = put(key, value);
    }
    return curValue;
  }
  
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
  {
    Objects.requireNonNull(mappingFunction);
    V v;
    if ((v = get(key)) == null)
    {
      V newValue;
      if ((newValue = mappingFunction.apply(key)) != null)
      {
        put(key, newValue);
        return newValue;
      }
    }
    return v;
  }
  
  public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    V oldValue;
    if ((oldValue = get(key)) != null)
    {
      V newValue = remappingFunction.apply(key, oldValue);
      if (newValue != null)
      {
        put(key, newValue);
        return newValue;
      }
      remove(key);
      return null;
    }
    return null;
  }
  
  public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    V oldValue = get(key);
    
    V newValue = remappingFunction.apply(key, oldValue);
    if (newValue == null)
    {
      if ((oldValue != null) || (containsKey(key)))
      {
        remove(key);
        return null;
      }
      return null;
    }
    put(key, newValue);
    return newValue;
  }
  
  public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
  {
    Objects.requireNonNull(remappingFunction);
    Objects.requireNonNull(value);
    V oldValue = get(key);
    
    V newValue = oldValue == null ? value : remappingFunction.apply(oldValue, value);
    if (newValue == null) {
      remove(key);
    } else {
      put(key, newValue);
    }
    return newValue;
  }
  
  public static <K, V> Map<K, V> of()
  {
    return ImmutableCollections.Map0.instance();
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1)
  {
    return new ImmutableCollections.Map1(k1, v1);
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9 });
  }
  
  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10)
  {
    return new ImmutableCollections.MapN(new Object[] { k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10 });
  }
  
  @SafeVarargs
  public static <K, V> Map<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries)
  {
    if (entries.length == 0) {
      return ImmutableCollections.Map0.instance();
    }
    if (entries.length == 1) {
      return new ImmutableCollections.Map1(entries[0].getKey(), entries[0]
        .getValue());
    }
    Object[] kva = new Object[entries.length << 1];
    int a = 0;
    for (Map.Entry<? extends K, ? extends V> entry : entries)
    {
      kva[(a++)] = entry.getKey();
      kva[(a++)] = entry.getValue();
    }
    return new ImmutableCollections.MapN(kva);
  }
  
  public static <K, V> Map.Entry<K, V> entry(K k, V v)
  {
    return new KeyValueHolder(k, v);
  }
  
  public static <K, V> Map<K, V> copyOf(Map<? extends K, ? extends V> map)
  {
    if ((map instanceof ImmutableCollections.AbstractImmutableMap)) {
      return map;
    }
    return ofEntries((Map.Entry[])map.entrySet().toArray(new Map.Entry[0]));
  }
}
