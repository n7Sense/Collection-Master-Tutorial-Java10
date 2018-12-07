 	ConcurrentHashMap
 =======================
 	ConcurrentHashMap  is the best choice if the huge no. of Threads are there in our application.
 	
 1. the initial capacity of ConcurrentHashMap is 16, (i.e 16-buckets by default present ConcurrentHashMap)
 2. to perform read operation any no. of thread are allowed to perform read operation simaltaneiously.
 3. to perform Update/Write operation compalsory thread required Lock.
 4. Bucket level locking are there also called Segment level locking, if default initial 16 capacity are there the 16
 	Lock are mentained in each Segment. hence at a time 16-thread are allowed to perform Update/Write operation
 	simaltaneiously.

 4. Total ConcurrentHashMap will be divided into specified no. of parts by default 16-parts are present.
 	this number is called 16-Currency Level.

 5. Concurrency Level initial capacity most of the time same but need not be same also in some cases,
 	if both are same Bucket-Level locking,
 	If both are not same, the based on Concurrency Level  and no. of Bucket will be existed.

 	
 	if(initialCapacity ==16 && Concurrency Level == 16 )
 		Bucket-Level locking

 	if(initialCapacity ==16 && Concurrency Level == 8 )
 		hear 8-Locks are only available
 		so for every 2-Bucket 1-lock mentained
 		in 2-Bucket at a time only 1 thread are allowed to perform Update operation.

 	if(initialCapacity ==16 && Concurrency Level == 32 )
 		so in this scenario for every Bucket-Level 2-Locks will be mentained.

 6. the underlying Data Structure is HashTable.
 7. ConcurrentHashMap allow concurrent read operation, and thread-Safe update operation.
 8. to perform READ operation thread wont required any lock, but to perform UPDATE operation thread required lock
 	but it is lock of particular part of Map object i.e called Segment-Level Lock or Bucket-Level Lock

 9. Concurrent Update are achived by internally dividing Map into smaller portions(parts/Segment/Bucket) which is
 	defiend by Concurrency-Level
 10. the  default concurrency level is 16 
 11. ConcurrentHashMap allowed any no. of read operation, but 16 update operation at a time by default
 		because of Concurrency-Level.
 12. Null is not allowed for both key and values.
 13. while one thread iterating the other thread can perform Update operation and ConcurrentHashMap or any Concurrent class never 
 	throw ConcurrentModificationException 

 14. Constructor:
 		Total 5 constructor are there in ConcurrentHashMap