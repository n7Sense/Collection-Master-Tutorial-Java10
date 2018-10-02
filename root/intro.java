*****************
1.Collection 1.2
*****************
i.  it consider as Root interface.
ii. there is no Concrete class which implement Collection interface directly.
iii. when we want to represent group of an indiviadual object as a single entity the go for Collection.
iv. Collection are growable i nature, 
v. W.R.T memory Collection are recommended to use.
vi. W.R.T performnece Collection are not recommended
vii. Collection can hold only Non-Primitive or reference.

imp: Usually we can use Collection(container) to hold and transfer object from one location to another location
	to provide support for this requirement every Collection class by default implements Serializable and the Cloenable interface.
	


				JAVA 										C++
=================================== 		===================================
			Colection 									Container

		Collection Framework				STL (Standard Template Library)
_______________________________________________________________________________


++++++++++++++
2.Collections
++++++++++++++
i. Collections is an UTILITY class present in java.util package to define several UTILITY METHODS for
Collection object like Sorting, Searching etc.


***************************************
3.java.list.List extends Collection 1.2
***************************************
iF we want to represent a group of an indiviadual Object as a Silngle entity 
where duplicate are allowed and insertion Order must be preserved then we shuld
go for List interface.

	{ArrayList 1.2, LinkedList 1.2, Vector 1.0{Stack 1.0 called Legacy}}


******************
java.util.Set 1.2
******************
If we want to represent a group of indiviadual Object as a Silngle entity
where duplicates are not allowed and insertion order not required
then we shuld go for Set interface.

*********************
java.util.SortedSet 1.2
*********************
If we want to represent a group of indiviadual Object as a Silngle entity
where duplicates are not allowed and all Object shuld be inserted according 
to some sorting order then we shuld go for SortedSet interface

Insertion order may be based on some hashCode() or by some Sorting order the object will be save
we cant accept.


***********************
java.util.NavigableSet 1.6
***********************
it contains several methods for Navigable purpose


*******
Queue
*******
if we want to represent a group of indiviadual object PRIOR TO PROCESSING then we shuld go for Queue
usually Queue follows First In First Out order but based on our requirement we can implement our own
priority order also.
EXAMPLE:
Before sending a mail all mail id we have to store in some Data Structure in which order we added Mail ID 
in the same order only mail shuld be diliver. for this requirement Queue is best choice.


**Note: all above the interface i.e  Colection, List, Set, SortedSet, NavigableSet, Queue whenever representing a group of
an indiviadual object.
If we want to represent a group of Object as <Key, Value> pair then we shuld go for Map interface.


	Map
++++++++++

	If we want to represent a group of object as <Key, Value> pair then we shuld go for Map interface.
	both <Key, Value> are Object only.
	duplicates <Key> are not allowed but values can be duplicated.

-------------
SortedMap
+++++++++++++
If we want to represent  a group of <key, values> pair according to SOME SORDING ORDER OF <KEY> then weshuld go
for SortedMap.
In SortedMap the sorting shuld be based on <KEY> but NOT based on <Value>

-------------
NavigableMap
+++++++++++++
it is child interface of SortedMap, it define several methods for navigable purpose
{TreeMap}