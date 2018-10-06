	java.lang.Comparable interface
	===============================


1. if we are depending on default sorting order then compalsory that object shuld be Homogenious and Comparable otherwise
	we will get RuntimeException saying ClassCastException.

2. an Object is said to be Compairable if and only if corresponding class implements Compairable interface.

3. String class and all Wrapper classes already implements Compairable interface. but StringBuffer class doesent implements
	Comparable interface. otherwise we will get 
	RE: Exception in thread "main" java.lang.ClassCastException: java.base/java.lang.StringBuffer 
	cannot be cast to java.base/java.lang.Comparable

4. Comparable contain only one method i.e 
	
	public int compareTo(Object t)

	obj1.compareTo(obj2);
		-> return -ve if obj1 has to come before obj2.
		-> return +ve if obj1 has to come after obj2.
		-> return 0 obj1 and obj2 are equal (duplicate).



