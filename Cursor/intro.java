

Cursor:

If we want to get Object one-by-one then we shuld go for Cursor
there are 3-types of Cursor available in java
	1. Enumeration
	2. Iterator
	3. ListIterator

A. Enumeration
	We can use Enumeration to get Object one-by-one From Legacy Collection Object (i.e Vector, Stacke. ...etc)
		e.g.1 
			Stack s = new Stack();
			Enumeration e = s.elements();
	        	while(e.hasMoreElements()){
	            String element = e.nextElement().toString();
	            System.out.println("One-By-One : "+element);
        	}
        	System.out.println("Get All In Once : "+s);
        e.g.2
        	Vector v = new Vector();
	        for(int i=0; i<10;i++)
	            v.add(""+(char)(i+65));
	        
	        Enumeration e = v.elements();
	        while(v.hasMoreElements()){
	            String element = e.nextElement().toString();
	            System.out.println("One-By-One : "+element);
	        }
          	System.out.println("Get All In Once : "+v);

	A.a.Limitation Of Enumeration
		1. We can apply Enumeration concept only for Legacy classes and it is not a Universal Cursor
		2. By using Enumeration we can get only Read access and we cant perform Remove operation.
		3. To over come above Limitation we shuld go for Iterator


B. Iterator
	1. We can apply Iterator concept for any Collection Object and hence it an Universal Cursor.
	2. By using Iterator we can perform both Read and Remove operations.
	e.g.1 to get Iterator Object by
		Iterator itr = collectionObject.iterator();

	B.a.Limitation Of Iterator :
		1. By usimg Enumeration and Iterator we can alway move only towards forward DIRECTION and cant move
			towards backwards direction thease are single direction Cursor but not bi diracctional cursor.
		2. by using iterator we can perform only READ and REMOVE operation and we can't perform REPLACEMENT and
			ADDITION of new Obect.
		3. To overcome above limitation we shuld go for ListIterator.

C. ListIterator
	1. By using ListIterator we can move either to the Forward Direction or to the backwards direction
		and hence it is BI-DIRECTIONAL cursor.
	2. By using ListIterator we perform REPLACEMENT and ADDITION of new Objects in addition to Read and Remove operations.
	2. To get ListIterator Object.
		ListIterator litr = collectionObject.listIterator();
	3. ListIterator is child interface of Iterator and hence ll methods of Iterator are available in ListIterator.
	4. ListIterator define 9 methods.
		1.public abstract boolean hasNext();
  		2.public abstract E next();
  		3.public abstract int nextIndex();
  		4.public abstract boolean hasPrevoius();
  		5.public abstract E previous();
  		6.public abstract index previousIndex();
  		7.public abstract void remove();
  		8.public abstract void set(E paramE);
  		9.public abstract void add(E paramE);

  	5. the most powerfull cursor is ListIterator, but it's Limitation is it is applicable only for List Objects

  		