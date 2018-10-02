

 public class LinkedListListIterator{

    public static void main(String[] args) {
    

        LinkedList ll = new LinkedList();
        for (int i = 0; i <= 10; i++) {
            ll.add("" + (char) (i + 65));
        }

        System.out.println("Before All : " + ll);
        ListIterator ltr = ll.listIterator();

        while (ltr.hasNext()) {
            String s = ltr.next().toString();

            if (s.equals("B")) 
                ltr.remove();
            else if (s.equals("E"))
                ltr.add("ADD");
            else if (s.equals("I"))
                ltr.set("REPLACE");
            
        }
        System.out.println("After ALL : " + ll);
    }
}