	Collections
===================

1. Collections class defines svelar utility methods for collection object like sorting, searching, reversing etc.
2. Collections class defines the following 2-sort methods.
		
		to sort based on default natural sorting order
		in this case List shuld compalsory contain Homogenious and Comparable object otherwise we will get
		RuntimeException: ClassCastException
		List shuld not contain Null, otherwise we will get NullPointerException.
	1. 	public static <T extends Comparable<? super T>> void sort(List<T> list)   {     
			list.sort(null);   
		}


		to sort based on customized sorting order.
	2.	public static <T> void sort(List<T> list, Comparator<? super T> c) {
		    list.sort(c);
		}

		a.Example: demo program for sorting elements of list according to default natural sorting order. 
			see CollectionsSortingDemo.java

		b.Example: demo program to sort elements of list according to customized sorting order. 
			see CollectionsSortingWithComparator.java

3. Collections class defines the following Binary Search methods
		
		if the List is sorted according to default natural sorting order then we have to use this method.
	1.	public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T keyTargetObject) {
		    if (((list instanceof RandomAccess)) || (list.size() < 5000)) {
		      	return indexedBinarySearch(list, keyTargetObject);
		    }

		    return iteratorBinarySearch(list, keyTargetObject);
		}

		we have to use this methods if the List is sorted according to customized sorting order.
	2. 	public static <T> int binarySearch(List<? extends T> list, T keyTargetObject, Comparator<? super T> c) {
		    if (c == null) {
		      return binarySearch(list, keyTargetObject);
		    }
		    if (((list instanceof RandomAccess)) || (list.size() < 5000)) {
		      return indexedBinarySearch(list, keyTargetObject, c);
		    }
		    return iteratorBinarySearch(list, keyTargetObject, c);
		}
	
	Conclusion:
		1. The above search methods internally will use binarySearch Algorithm.
		2. Successfull search return Index.
		3. Unsuccessfull search return INSERTION POINT (e.g , -1, -2, -3)
		4. INSERTION POINT is the location where we can place target element in sorted List
		5. before calling binarySearch methods compalsory List shuld be sorted, otherwise we will get UnPredictable result.
		6. If the List is sorted according to Comparator, then at the time of search operation also we have to pass same
			Comparator objct otherwise we will get UnPredictable result.

		7.Example:1 BinarySearchDemo.java  see program according to program the output is 

			    Before Sorting Element In ArrayList
			-----------------------------------------------

			INSERTION POINT -->         -1      -2      -3      -4      -5      -6      -7      -8          
			                        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			                        |   A   |    D   |   C   |   E   |   K   |   Z   |    N   |   P   |  
			                        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			INDEX -->                   0        1       2       3       4       5       6       7     


			    After Sorting Element In ArrayList
			-----------------------------------------------

			INSERTION POINT -->         -1      -2      -3      -4      -5      -6      -7      -8      -9
			                        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			                        |   A   |    C   |   D   |   E   |   K   |   N   |   P   |   Z   |  
			                        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			INDEX -->                   0        1       2       3       4       5       6       7     



			run:
			Before Sorting : [A, D, C, E, K, Z, N, P]
			After Sorting : [A, C, D, E, K, N, P, Z]
			Binary Search for Z : 7
			Binary Search for O : -7
			Binary Search for a : -9
			Binary Search for b : -9
			Binary Search for B : -2
			BUILD SUCCESSFUL (total time: 0 seconds)

		8.Example2: BinarySearchWithComparator.java Out/Put is

			run:
			Before Sorting : [10, 0, 15, 6, 8, 8, 7, 20, -1]
			After Sorting : [20, 15, 10, 8, 8, 7, 6, 0, -1]
			Binary Search for 10 : 2
			Binary Search for 13 : -3
			Binary Search for 9 : -4
			Binary Search for 15 : 1
			Binary Search for 19 : -2
			Binary Search for 20 : 0
			Binary Search for 21 : -1
			Binary Search for 22 : -1
			Binary Search for 0 : 7
			Binary Search for -1 : 8
			Binary Search for -2 : -10
			Binary Search for -3 : -10
			Binary Search for 16 UnPredictable Output : -10
			BUILD SUCCESSFUL (total time: 0 seconds)

		9. for the list of n-element in the case of binarySearch methods for
				Successfull search result range		: 0 to n-1
				UnSuccessfull Search result range	: -(n+1) to -1
				total result range					: -(n+1) to n-1

				Example: 

					INSERTION POINT -->         -1      -2      -3   	-4
					                        ++++++++++++++++++++++++++
					                        |   A   |    C   |   D   |
					                        ++++++++++++++++++++++++++
					INDEX -->                   0        1       2    

					Successfull search result range		: 0    to 	2
					UnSuccessfull Search result range	: -4   to 	-1
					total result range					: -4   to  	2

4. Collections class defines the following Reverse methods to reverse elements of List.
		
	1.	public static void reverse(List<?> list)

	2.	public static <T> Comparator<T> reverseOrder(Comparator<T> cmp)

	Note: reverse() vs reverseOrder()
		we can use reverse method to reverse order of elements of List
		where as we can use reverseOrder method to get REVERSED COMPARATOR. 
		i.e
		
		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		|	Comparator  c  = Collections.reverseOrder(Comparator  c ); 	|
		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
			   	^										  ^
			   	| 2nd then this will talk to 			  |	 1st if  this Comparator talk for	
			   	| Descending order 						  |	 Ascending Order
