 package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.JavaOISAccess;
import sun.misc.SharedSecrets;

public class ArrayList<E>  extends AbstractList<E>   implements List<E>, RandomAccess, Cloneable, Serializable{
  		
  		//Constructor
  		public ArrayList(int paramInt)
  		public ArrayList()
  		public ArrayList(Collection<? extends E> paramCollection) {
  			Create equivalent ArrayList object for the given Collection.
  			Note: Imp whenever intercommunication between collection object.
  			from one collection to another collection i want to dance then we shuld go for this Constructor
  		}
  }