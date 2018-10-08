	PriorityQueue   1.5
=======================

1. if we want to represent a group of individual Object Prior To Processing accrodingto some priority then we shuld go for PriorityQueue.
2. The priority can be either default natural sorting order or customized sorting order defiend by Comparator.
3. Insertion order is not preserved and it is based on some priority.
4. duplicates Object are not allowed.
5. if we are depending on default natural sorting order compalsory that object shuld be Homogenious and Comparable, otherwise 
	we will gert RE: ClassCastException.
6. If we are defioning our own sorting by Comparator, then object need not be Homogenious nd Comparable.
7. NULL is not allowed even as the first element also.

8. Constructor:
			
			creates an empty priority queue with default initial capacity 11 & all objects will be inserted according to
			D.N.S.O
		1.	public PriorityQueue() { this(11, null);   }

			creates an empty priority queue with speciaed initialCapacity
		2.  public PriorityQueue(int initialCapacity) { this(initialCapacity, null);   }
		

		3.	public PriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
		        if (initialCapacity < 1) {
		            throw new IllegalArgumentException();
		        }
		        this.queue = new Object[initialCapacity];
		        this.comparator = comparator;
		    }


		4.  public PriorityQueue(Comparator<? super E> comparator) {
		        this(11, comparator);
		    }

		    
		    inter communication between Sorted set object.
		5.  public PriorityQueue(SortedSet<? extends E> c) {
		        this.comparator = c.comparator();
		        initElementsFromCollection(c);
		    }

		    inter communication between Collection object set.
		6.  public PriorityQueue(Collection<? extends E> c) {
		        if ((c instanceof SortedSet)) {
		            SortedSet<? extends E> ss = (SortedSet) c;
		            this.comparator = ss.comparator();
		            initElementsFromCollection(ss);
		        } else if ((c instanceof PriorityQueue)) {
		            PriorityQueue<? extends E> pq = (PriorityQueue) c;
		            this.comparator = pq.comparator();
		            initFromPriorityQueue(pq);
		        } else {
		            this.comparator = null;
		            initFromCollection(c);
		        }
		    }


		7.  public PriorityQueue(PriorityQueue<? extends E> c) {
		        this.comparator = c.comparator();
		        initFromPriorityQueue(c);
		    }

9. according to in example PriorityQueueDemo.java we get different 
	out/put: i.e
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		0
		[1, 3, 2, 7, 4, 5, 6, 10, 8, 9]

		example 2: 
			
	Some platform wont provide proper support for ThreadPriority and PriorityQueue