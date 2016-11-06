package week3;

public class LinkedList<T> implements LinkedListInterface<T> {
	Node<T> head = null;
	Node<T> tail = null;
	int size = 0;

	/**
	 * Places the given element at the beginning of the linked list O(n)=1
	 **/
	public void addFirst(T newElement) {
		Node<T> newHead = new Node<T>(newElement);

		if (size == 0) {
			tail = newHead;
		} else {
			newHead.setRight(head);
		}
		head = newHead;

		size++;
	}

	/**
	 * Places the given element at the end of the linked list 
	 * O(n) = 1
	 */
	public void addLast(T newElement) {
		Node<T> newTail = new Node<T>(newElement);

		if (size == 0) {
			head = newTail;
		} else {
			tail.right = newTail;
		}
		tail = newTail;

		size++;
	}

	/**
	 * Places the given element at the given index Counting starts from 0
	 * Example: add(5,2) ---> 5 will be the 3rd element in the linked list
	 * add(5,0) ---> 5 will be the 1st element(head) in the linked list 
	 * O(n) = n
	 */
	public void add(T newElement, int index) {
		if (index > size) {
			System.out.println("Invalid index! Max value = " + size);
		} else if (index == 0) {
			addFirst(newElement);
		} else if (index == size) {
			addLast(newElement);
		} else {
			Node<T> newEl = new Node<T>(newElement);
			Node<T> current = head;

			int counter = 1;
			while (counter != index) {
				current = current.right;
				counter++;
			}

			newEl.right = current.right;
			current.right = newEl;
			size++;
		}
	}

	/**
	 * Returns the head 
	 * O(n) = 1
	 */
	public T getFirst() {
		if (size != 0) {
			return head.getValue();
		}
		return null;
	}

	/**
	 * Returns the tail 
	 * O(n) = 1
	 */
	public T getLast() {
		if (size != 0) {
			return tail.getValue();
		}
		return null;
	}

	/**
	 * Return the element at the given index if such index exists
	 * O(n) = n
	 */
	public T get(int index) {
		if (index > size - 1) {
			return null;
		} else if (index == 0) {
			return head.getValue();
		} else if (index == size - 1) {
			return tail.getValue();
		} else {

			Node<T> current = head;

			int counter = 0;
			while (counter != index) {
				current = current.right;
				counter++;
			}

			return current.getValue();
		}
	}

	/**
	 * Returns the size of the linked list
	 * O(n) = 1
	 */
	public int size() {
		return size;
	}

	/**
	 * Removes the element at the given index if such index exist
	 * Index count starts from 0
	 * O(n) = n
	 */
	public void remove(int index) {
		if (index > size - 1) {
			System.out.println("Invalid index! Max value = " + size);
		} else if (index == 0) {
			head = head.right;
			size--;
		} else {
			Node<T> current = head;

			for (int i = 0; i < index - 1; i++) {
				current = current.right;
			}
			
			if (index == size - 1) {
				current.right = null;
				tail = current;
			} else {
				current.right = current.right.right;				
			}
			size--;
		}
	}

	/**
	 * Concatenates the given linked
	 * O(n) = n  //the given list's length
	 */
	public void addList(LinkedListInterface<T> list) {
		for (int i = 0; i < list.size(); i++) {
			Node<T> current = new Node<T>( list.get(i) );
			tail.right = current;
			tail = current;
			size++;
		}
	}

	public String toString() {
		if (size == 0) {
			return "empty";
		}

		String result = "";

		Node<T> current = head;
		while (current != null) {
			result += current.value + "->";
			current = current.right;
		}

		return result + "null";
	}

	public static void main(String[] args) {
		LinkedList<Integer> test = new LinkedList<Integer>();
		test.addFirst(2);
		test.addFirst(1);
		test.addLast(3);
		test.addLast(4);
		test.add(5, 2);
		test.add(0, 0);
		test.add(10, 6);
		System.out.println(test);
		System.out.println(test.getFirst());
		System.out.println(test.getLast());
		System.out.println(test.get(0));
		System.out.println(test.get(3));
		System.out.println(test.get(6));
		System.out.println(test.size());
		test.remove(0);
		System.out.println(test);
		System.out.println(test.size());
		test.remove(2);
		System.out.println(test);
		System.out.println(test.size());
		test.remove(4);
		System.out.println(test);
		System.out.println(test.size());
		
		LinkedList<Integer> test2 = new LinkedList<Integer>();
		test2.addFirst(5);
		test2.addLast(6);
		test2.addLast(7);
		test2.addLast(8);
		System.out.println(test2);
		System.out.println(test2.get(0));
		System.out.println(test2.get(1));
		System.out.println(test2.get(2));
		System.out.println(test2.get(3));
		test.addList(test2);
		System.out.println(test);
	}

}