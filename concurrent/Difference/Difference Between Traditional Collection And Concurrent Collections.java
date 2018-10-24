	Difference Between Traditional Collection And Concurrent Collections
============================================================================

Traditional Collection
++++++++++++++++++++++++

	1.	Traditional collections are not thread safe.
	2.	Performance wise low.  
	3. 	While  one thread iterating collection object The remaining thread are not allowed to modify Underlying collection object.
		By mistake if you are trying to do we will get ConcurrentModificationException
 		




Concurrent Collection
++++++++++++++++++++++++

	1.	Concurrent collections are always thread safe.
	2. 	Concurrent collections performance wise relatively high because of different locking mechanism,
		instead of total collection lock, thread is going to get a particular lock a particular segment lock, 
		Such type of locking is called Segment Locking or Bucket Level Locking.
	3.	While  one thread iterating collection object The remaining thread are allowed to modify Underlying collection 
		in safe manner And  concurrent collection never throw ConcurrentModificationException.
	4. 	Concurrent collection best suitable for scalable Multithreaded application


	CopyOnWriteArrayList
	ConcurrentListDeque
	ConcurrentSkipListSet
	ConcurrentSkipListMap
	ConcurrentHashMap