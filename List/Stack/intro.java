
Stack
===========

1. It is a child class of Vector
2. It is specially design for Last In first Out(LIFO)
3. Constructor has only one constructor i.e Default Constructor.
4. Methods:
	1. void push(Object obj)
	2. Object pop()  return and then remove that pertoicular object.
	3. Object peek() return object without removing.
	4. boolean empty() 
	5. int search(Object obj); if object found then it return Offset if not then return -1.
		mean if we have Stack{ [A], [B], [C], } there for index{0, 1, 2} but offse{1 for C, 2 for B, 3 for A} because LIFO
		
