
	java.util.Comparator 1.2
============================

1. Comparator present in java.

2.Methods
	
	1. int compare(T o1, T o2);	

    2. boolean equals(Object obj);

    3. default Comparator<T> reversed()

    4. default Comparator<T> thenComparing(Comparator<? super T> other)

    5. default <U> Comparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)

    6. default <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor)

    7. default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor)

    8. default Comparator<T> thenComparingLong(ToLongFunction<? super T> keyExtractor)

    9. default Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> keyExtractor)

    10. public static <T extends Comparable<? super T>> Comparator<T> reverseOrder()

    11. public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()

    12. public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator)

    13. public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator)

    14. public static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)

    15. public static <T, U extends Comparable<? super U>> Comparator<T> comparing( Function<? super T, ? extends U> keyExtractor)

    16. public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) 

    17. public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor)

    18. public static<T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor)

4. Wherever we are implementing Comparator interface then compalsory we shuld provide implementation for compare() method,
	we are not required to provide an implementation for equals() because its already provided it's implementation from Object class through
	Inheritence.

5. Write a program to insert Integer object into the TreeSet where the sorting order is Descending Order.
	that program are available in {TreeSetWithComparator.java, MyComparator.java}

	at line no.12 if we are not passing Comparator Object  then internally JVM will call compareTo() method which is ment for 
	Default Natural Sorting order, 
	at line.12 if we are passing Comparator object then JVM will call compare method which is meant for customized sorting
	in this case
	Out/Put is: [10, 9, 8, 5, 0]

	Killer Thing inside this.

	public int compare(Object obj1, Object obj2){

		Integer i1  = (Integer)o1;
        Integer i2  = (Integer)o2;

	     1. return i1.compareTo(i2);  // default natural sorting order [0, 5, 8, 9, 10]
	     2. return -i1.compareTo(i2);  // Descending [10, 9, 8, 5, 0]
	     3. return i2.compareTo(i1);  // Descending [10, 9, 8, 5, 0]
	     4. return -i2.compareTo(i1);  // Ascending [0, 5, 8, 9, 10]
	     5. return +1; [Insertion Order] [10, 9, 9, 8, 5, 5, 0]
	     6. return -1; [reverse of Insertion Order] [0, 5, 5, 8, 9, 9, 10]
	     7. return 0; [Only first element will be inserted and all remaining will be duplicated]

     
	}

6. Writea program to insert the StringBuffer object into the TreeSet where sorting order is Alphabaticle order.

7. If we are depending on Default natural sorting order then compalsory that object shuld be homogenious and Comparable othervise
	we will get RuntimeException saying ClassCastException.

8. If we are defining our own sorting by comparator then Objects need not be Comparable and homogenious i.e we can add
	hetrogenious non Comparable Objects also.

9. Writea a program to insert String and StringBuffer objects into TreeSet where sorting order is incresing length order.
	if two Object having same length consider there Alphabaticle order.

10. 