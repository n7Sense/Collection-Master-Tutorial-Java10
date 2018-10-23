IdentityHashMap

1. it is exactly same as HashMap including Methods and Constructor except the following difference.
2. In the case of normal HashMap JVM will use equals() methods to identify Duplicate keys, which is ment for content cmparison
	but in the cae of IdentityHashMap JVM will use (==) operator to identify Duplicate Keys, which is ment for Reference 
	Comparison (Address Comparison).
3. Example
		Integer i1 = new Integer(10);
		Integer i2 = new Integer(10);
		IdentityHashMap ihm =new IdentityHashMap();
		ihm.put(i1, "Rahul");
		System.out.println(ihm.put(i2, "Sunita"));

	out/put:  null

	Note: beside this code I1 and I2 are duplicate keys, because I1.equals(I2) retun true.
	if we replace HashMap with IdentityHashMap then I1 & I2 are not duplicate keys, 
	because (I1 == I2) return false, in trhis case 
	
	out/put: 
		null
		{10=Rahul, 10=Sunita}
