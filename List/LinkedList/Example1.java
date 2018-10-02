

public class Example1{

	public static void main(String[] args) {
		LinkedList l = new LinkedList();
       l.add("a");
       l.add(10);
       l.add("Sunita");
       l.add("prashad");
       l.addLast("abc");
       l.addFirst("KlRahul");
       l.addFirst("KlRahul");
       l.addLast("Saroj");
       l.removeLast();
       l.removeFirst();
       System.out.println(l);
	}
}
/*
	OUt/PUt: [KlRahul, a, 10, Sunita, prashad, abc]
*/