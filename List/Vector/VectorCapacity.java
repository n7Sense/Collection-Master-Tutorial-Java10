

public class VectorCapacity{

	public static void main(String[] args) {
		
		Vector v = new Vector();
        System.out.println("Default Vector Capacity : "+v.capacity());
        for(int i=0; i<10;i++)
            v.add(i);
        System.out.println("After Add 10 Elements Capacity : "+v.capacity());
        v.add("Rahul");
        System.out.println("After Add 1 Elements Capacity : "+v.capacity());
        
	}
}