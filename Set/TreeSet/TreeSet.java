package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TreeSet<E>  extends AbstractSet<E>  implements NavigableSet<E>, Cloneable, Serializable
{
  private transient NavigableMap<E, Object> m;
  private static final Object PRESENT = new Object();
  private static final long serialVersionUID = -2479143000061671589L;
  
  public TreeSet()

  TreeSet(NavigableMap<E, Object> paramNavigableMap)

  public TreeSet(Comparator<? super E> paramComparator)
  
  public TreeSet(SortedSet<E> paramSortedSet)

  public TreeSet(Collection<? extends E> paramCollection)
  
}