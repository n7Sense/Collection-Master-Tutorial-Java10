
public class ArrayListThreadSafety{

	public static void main(String[] args) {
		ArrayList planets = new ArrayList<String>();
        planets.add("Zebra");
        planets.add("Earth");
        planets.add("Maars");
        planets.add("Moon");

        //to synchronized
        List l = Collections.synchronizedList(planets);
        
        System.out.println("Planets 3 "+planets);
        System.out.println("Size :"+planets.size());
	}
}