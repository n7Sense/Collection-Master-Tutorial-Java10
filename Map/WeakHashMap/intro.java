

WeakHashMap
=============

It is exactly same as HashMap except the following difference

1. In the case of HashMap even thoe the object doesent have any reference it is not elgible for Garbage Collector if it 
	is associtaed with HashMap, i.e HashMap dominate Garbage Collector.

	But int the case of WeakHashMap, if object doesent contain any references it is eligible for GC even thoe object
	associtaed with WeakHashMap i.e Garbage Collector dominates WeakHashMap.

2. Example: see program :
	WeakHashMapWeakerThanGarbageCollector.java

3. In the above Example  the NotProtectedByHashMap object eligible for GC because it is assocoated with WeakHashMap.
	int this case out put is 
	
	Before GC : {NotProtectedByHashMap{id=2, name=Sunita}=Sunita, NotProtectedByHashMap{id=1, name=Rahul}=Rahul}
	finalize method called
	After GC : {NotProtectedByHashMap{id=2, name=Sunita}=Sunita}