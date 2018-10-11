	Comparable Vs Comparator
==============================

1. For predefiend Comparable classes Default Narural Sorting order already available. if we are not satis with 
	Default Natural Sorting order then we can define our own Sorting order by using Comparator.

2. For pre-defiend Non-Comparable classes (like StringBuffer) default Natural Sorting order not already available
	we can define our own Sorting by using Comparator interface.

3. For our own classes like Employee, the person who is writing that class is responsible to define default Natural Sorting order
	by implementing Comparable interface.

4. the person who is using our class, if he is not satisfied with the default natural sorting order
	then he can define his own sorting by comparator.


+++++++++++++++++++++++++++ 
| Pre-define Comparable   +
| 		classes           +	 			---------------------------------------> Comparable
|		String            +
|________________________ +

+++++++++++++++++++++++++++++++
| Pre-define Non-Comparable   +
| 			classes           +			--------------------------------------->  Comparator
|			String            +		
|_____________________________+



+++++++++++++++++++++++++++++++			the person who is written this class
| 		Our Own Class   	  +			---------------------------------------->	Comparable (D.N.S.O)
| 		 e.g like             +			
|		( Employee )          +	
|					 	      +			---------------------------------------->	Comparator (Customized Sorting Order)
|_____________________________+			the person who is using this class