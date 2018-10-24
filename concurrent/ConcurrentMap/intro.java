	ConcurrentMap(I)
=====================
				
1. ConcurrentMap interface present in java.util.concurrent package.
2. by default it override all the methods of Map interface, but also it provide 3- special methods
	a. putIfAbsent()
	b. remove()
	c. replace()

3. Methods 
	a. putIfAbsent(Object key, Object value)
		If the keys are not available then only add. if keys are available then dont do any thing.

		eg: with normal put() method

		m.put(101, "Rahul");
		m.put(101, "Sunita");
		S.o.pl(m);
		O/P:> {101, Sunita}

		eg. with putIfAbsent() method.
		m.put(101, "Rahul");
		m.put(101, "Sunita");
		S.o.pl(m);
		O/P:> {101, Rahul}

	b. boolean remove(Object key, object value)
		if the value are associated with the specified key then remove else dont do any thing.

		eg: with normal remove() method

		m.put(101, "Rahul");
		m.remove(101);
		S.o.pl(m);
		O/P:> {}

		eg. with remove() method.
		m.put(101, "Rahul");
		m.remove(101, "Sunita");
		S.o.pl(m);
		O/P:> {101, Rahul}

	c. replace(Object key, Object oldValue, Object newValue)
		if the specified key with oldValue  are present thent oldValue replace with newValue.

		eg: 
			m.put(101, "Rahul");
			m.replace(101, "Sunita", "Ravi");
			S.o.pl(m);  O/P: {101, Rahul}
			m.replace(101, "Rahul", "Ravi");
			S.o.pl(m);  O/P: {101, Ravi}
