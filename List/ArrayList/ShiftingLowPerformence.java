
public class ShiftingLowPerformence{

    public static void main(String[] args) {
        
        ArrayList planets = new ArrayList<String>();
        planets.add("Zebra");
        planets.add("Earth");
        planets.add("Maars");
        planets.add("Moon");
        planets.add(null);
        planets.add(10);
        planets.add(0, "hero"); //Shifting
        planets.add(0, "hero"); //Shifting
        System.out.println("Planets 3 "+planets);
        System.out.println("Size :"+planets.size());
        
    }
}