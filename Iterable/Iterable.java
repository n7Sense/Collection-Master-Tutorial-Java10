package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * Implementing this interface allows an object to be the target of
 * the "for-each loop" statement. See
 * @param <T> the type of elements returned by the iterator
 * @since 1.5
 * @jls 14.14.2 The enhanced for statement
 */

public interface Iterable<T> {

		public abstract Iterator<T> iterator();
  
	  public void forEach(Consumer<? super T> paramConsumer)
	  {
	    Objects.requireNonNull(paramConsumer);
	    for (Object localObject : this) {
	      paramConsumer.accept(localObject);
	    }
	  }
	  
	  public Spliterator<T> spliterator()
	  {
	    return Spliterators.spliteratorUnknownSize(iterator(), 0);
	  }
}
