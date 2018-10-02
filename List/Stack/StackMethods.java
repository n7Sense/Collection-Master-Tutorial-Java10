package stack;
import java.util.Stack;

public class StackMethods{

	public static void main(String[] args) {
		  
        Stack s = new Stack();
        for(int i=0; i<10;i++)
            s.push(""+(char)(i+65));
        
        System.out.println("After Add 10 Elements Capacity : "+s.capacity());
        s.add("Rahul");
        System.out.println("After Add 1 Elements Capacity : "+s.capacity());
        System.out.println(s);
        System.out.println("offset of A : "+s.search("A"));
        System.out.println("offset of J : "+s.search("J"));
        System.out.println("offset of Rahul : "+s.search("Rahul"));
        System.out.println("offset of k : "+s.search("K"));
        System.out.println("Value At Index 10 : "+s.get(10));
        System.out.println("Pop : "+s.pop());
        System.out.println(s);
        System.out.println("Peek : "+s.peek());
        System.out.println(s);
	}
}