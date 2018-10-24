Need of Concurrent Collections
=================================


1. Traditional Collection Object like (ArrayList, HashMap, etc.) can be accessed by multiple threads simultaneously and there
 	may be a chance of Data Inconsistency  Problems and hence this is a not Thread Safe.

2.  already existing thread-safe Collections like Vector, HashTable synchronizedList(), synchronizedSet(), synchronizedMap()
	performance wise not upto the mark.

3.  Because for every operation even for read operation also total collection will be loaded by only one thread at a time
	and it increases  waiting time of threads.

	public class nSense {

	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String[] args) {

	        ArrayList al = new ArrayList();
	        al.add(10);
	        al.add(20);
	        al.add(50);
	        
	        Iterator i= al.iterator();
	        while(i.hasNext()){
	            String s = i.next().toString();
	            System.out.println(s);
	            al.add(100);  //throws java.util.ConcurrentModificationException
	        }
	        
	    }
	}

1.	Another big problem with traditional collections is while one thread  iterating collection object,
	The Other Threads are not allowed to modify collections object simultaneously if we are trying to modify underling
	collection object, then we will get Concurrentmodificationexception.
	
2.	hence these traditional collection object or not suitable for scalable multi threaded applications.
3.	to overcome this is problem Sun people introduced  concurrent collections in 1.5 version 

4. Importent Concurrent Collection Classes
	1. 	ConcurrentHashMap.
	2. 	CopyOnWriteArrayList.
	3.	CopyOnWriteArraySet
