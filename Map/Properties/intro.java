
Properties
==========

1. In our program if anything which changes frequently (like User name, Password, mail id, mobile no. etc.) or not recommended to
	hardcode in java program, because if there is any change to reflect that change Recompilation, Re-Build and Re-Deploy
	application are requored even some time Server Restart also requored which create a big Business impact to the client.

2. 	we can overcome this problem by using Properties file, such type of veriable thing we have to configure in the properties files
	from that properties file we have to red into java program and we can use those properties.

3. The main advantages of this aproach is, if there is a change in properties file to reflect that change just Re-Deployment
	is enough which wont create any business impact to the client.

4. we can use java Properties object to hold properties which are coming from properties file.

5. in normal Map (like HashMap, HashTanle, TreeMap <key & value> can be any type) but in the case of Properties <key & value>
	shuld be String type.

6. Constructor:

	1.	public Properties() {
    		this(null, 8);
  		}
  
  	2.	public Properties(int initialCapacity) {
    		this(null, initialCapacity);
  		}
  
  	3. 	public Properties(Properties defaults) {
    		this(defaults, 8);
  		}
  
  	4.	private Properties(Properties defaults, int initialCapacity) {
    		super((Void)null);
    		this.map = new ConcurrentHashMap(initialCapacity);
    		this.defaults = defaults;
  		}
  		
 7. Methods :

 		to set a new property, if the specified property already available then old
 		value will be replace with new value and return old value of String property name
	1.	public String setProperty(String key, String value) {
			return put(key, value);
		}

		to get value associated with specified property. if specified property not available then it will return null.
	2.	public String setProperty(String key) {
			return put(key, value);
		}

	3.	Enumeration propertyNames()
		return all property name preent in property Object.

	4.	public void load()
		to load properties from property file into java Properties object.

	5. public void store(OutputStream os, String comment)
		to store properties from java properties object into property file.
