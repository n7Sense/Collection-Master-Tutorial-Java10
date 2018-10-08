	Queue 1.5
=================

1. it is the childe interface of Collection.
				
						Collection
							 |
						   Queue
						  /	 	\
						 /	 	 \
						/ 		  \
			PriorityQueue			BlockingQueue
										|
										|>PriorityBlockingQueue
										|
										|>LinkedBlockingQueue

2. If we want to represent a group of a individual Object 'PRIOR TO PROCESSING' then we shuld go for Queue.

3. Example:
		Before sending SMS message all mobile numbers we have to store all mobile numbers in some data structure,
		in which order we added mobile numbers in the same order only message shuld be send(deliver) for this 
		'First In First Out' requirement Queue is the best choice.

4. usualy Queue follows First In First Out order but based on our requirement we can implement our own 
	Priority Order (PriorityQueue) also.

5. from 1.5 version onwards LinkedList class also implements Queue interface.

6. LinkedList based implementation of Queue always follows First In First Out order.

7. Queue Specific Methods:
		
		1.  public abstract boolean offer(E paramE);
			to add an object into the Queue.
		  	
		2.  public abstract E peek();
			to return head element of the Queue.If Queue is empty then this methods return null.

		3.  public abstract E element();
			to return head element of the Queue. If Queue is empty then this methods throws RE: NoSuchElementException.
		  
		4.  public abstract E pool();
			to remove and return head element of the Queue. If Queue is empty then this methods return null.

		5.  public abstract E remove();
		  	to remove and return head element of the Queue. If Queue is empty then this methods throws RE: NoSuchElementException.

		6. 	public abstract boolean add(E paramE);
			Inserts the specified element into this queue if it is possible to do so immediately without violating capacity 
			restrictions, returning true upon success and throwing an IllegalStateException if no space is currently 
			available
		  