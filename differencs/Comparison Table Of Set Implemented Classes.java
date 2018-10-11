Comparison Table Of Set Implemented Classes
============================================


Property				+	HashSet 				+	LinkedHashSet				+	TreeSet
----------------------- + ------------------------- + ----------------------------- + -------------------------
1. Underlying Data		+	HashTable				+	LinkedList + HashTable		+	Balanced Tree
	Structure			+							+								+
						+							+								+
						+							+								+
2.	Duplicate Object 	+	Not Allowed				+	Not Allowed					+	Not Allowed
						+							+								+
						+							+								+
3. 	Insertion Order		+  	Not Preserved			+	Preserved					+  	Not Preserved	
						+							+								+
						+							+								+
4. 	Sorting Order		+	Not Allowed				+	Not Allowed					+	Applicable
						+							+								+
						+							+								+
5. 	Hetrogenious		+	Allowed					+	Allowed						+ 	Not Allowed by default
	Object				+							+								+	But possible by Comparator
						+							+								+
						+							+								+
6. 	NULL Acceptence		+ 	Possible only once		+	Allowed only once			+ 	First Empty TreeSet as 
						+							+								+	First element NULL is allowed
						+							+								+	until java 1.6 but after java 1.7
						+							+								+	NULL is not allowed.
						+							+								+
-------------------------------------------------------------------------------------------------------------------	
					
						