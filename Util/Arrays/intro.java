	Arrays 
==============

1. Arrays class is an utility class, to define several utility methods for Array Object.
2. Arrays class define the following Sort methods to sort elements of Primitive and Object type Array.
	Methods:

		1. public static void sort(primitive[	] p)
			to sort according to Natural sorting order.

		2.	public static void sort(Object[] o)
			to sort according to Natural sorting order.

		3.  public static void sort(Object[] o, Comparator c)
			to sort according to customized sorting order.

3. NOTE: we can sort primitive Array only based on Default Natural Sorting order, where as we can sorting Object 
	Array either based on default natural sorting order or based on customized sorting order.

4. Searching Elements Of Array:
		Arrays class define the following binary search methods.

		1.	public static int binarySearch(primitive[] p, primitive target)
		2. 	public static int binarySearch(Object[] o, Object objTarget)
		3.	public static int binarySearch(Object[] o, Object objTarget, Comparator c)
		NOTE: all rules of Arrays class binary search methods are exactly same as Collections class binarySearch methods.

5. Conversion of Array to List:

		+++++++++++++++++++++++++++++++++++++++++++++++++++++
		|		public static List asList(Object obj)		|
		+++++++++++++++++++++++++++++++++++++++++++++++++++++
		strictly speaking this method wont create an INDEPENDENT List Object, for the existing Array we are getting
		List VIEW.

		String [] s = {"A", "Z", "B"};
		List l = Arrays.asList(s);
										  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
										 /									 \			
										/									  \				
		String [] s  ------------------>|									  |	
										|		+++++++++++++++++++++++++	  |
										|		|	A	|	Z	|	B	|	  |
				just reference to Array	|		+++++++++++++++++++++++++	  |
		List l 	----------------------->|									  |
										\ 									  /
										 \_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _/

		1.	By using Array REFERENCE if we perform any CHANGE, autometically that change will be reflected to the List.
			similarly by using List REFERENCE if we perform any change that change will be reflected autometically
			to the array.

		2.	By using List reference we cant perform any operation which varies the size, otherwise we will get
			RE: UnsupportedOperationException.
			example that throws UnsupportedOperationException
			l.add("M");
			l.remove(1);

			example that not throws UnsupportedOperationException
			l.set(1, "Z");

		3.	By using List reference we are not allow to replace with Hetrogenious Object, otherwise we will get
			RE: ArrayStoreException.
			l.set(1, new Integer(10)); throws ArrayStoreException


		

