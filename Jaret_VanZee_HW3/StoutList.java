package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Jaret Van Zee
 */

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Current node iterating through the initial list
   */
  private Node currentNode = null;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }
  
  /**
   * Returns the total elements in the list
   */
  @Override
  public int size()
  {
      return size;
  }
  
  /**
   * Adds an element to end of the list
   * 
   * @param item - item to be added
   */
  @Override
  public boolean add(E item)
  {
	  currentNode = tail.previous;
	
	  //Initializes the first Node, connected to the head and tail
	  if (size == 0) {
		  currentNode = new Node();
		
		  head.next = currentNode;
		  currentNode.previous = head;
		
		  currentNode.next = tail;
		  tail.previous = currentNode;
	  }
	
	  //Checks if the last node is full
	  //If the last node is full, it creates a new node
	  //linking it to the previous and the tail nodes
	  else if (currentNode.count == nodeSize) {
		  Node temp = currentNode;
		  currentNode.next = new Node();
		  currentNode = currentNode.next;
		
		  //temp.next = currentNode;
		  currentNode.previous = temp;
		  currentNode.next = tail;
		  tail.previous = currentNode;
	  }
	
	  currentNode.addItem(item);
	  ++size;
	  
      return true;
  }
  
  /**
   * Adds an item at a specified element while also following specific addition rules
   * 
   * @param pos - position for the element to be added
   * @param item - Item to be added
   */
  @Override
  public void add(int pos, E item)
  {
	  NodeInfo n = findIndice(pos);
	  Node node = n.getNode();
	  int offset = n.getOffset();
	  
	  
	  if (offset == 0) {
		  if (node.previous != head && node.count > node.previous.count) {
			  //node.previous.data[node.previous.count] = item;
			  node.addItem(node.previous.count, item);
		  } else if (node == tail) {
			  Node temp = node;
			  node = new Node();
			  
			  node.previous = temp;
			  temp.next = node;
			  tail.previous = node;
			  node.next = tail;
			  
			  node.addItem(node.count, item);
		  }
	  } else if(node.count < node.data.length) { 
		  for (int i = offset; i < node.count; ++i) {
			  node.data[i+1] = node.data[i];
		  }
		  node.addItem(offset, item);
	  } else {
		  Node nextNode = new Node();
		  nextNode.next = node.next;
		  node.next.previous = nextNode;
		  node.next = nextNode;
		  nextNode.previous = node;
		  
		  int j = 0;
		  for (int i = nodeSize / 2; i < nodeSize; ++i) {
			  nextNode.data[j] = node.data[i];
			  nextNode.count++;
			  node.data[i] = null;
			  node.count--;
			  ++j;
		  }
		  
		  if (offset <= nodeSize / 2) {
			  node.data[offset] = item;
			  node.count++;
		  } else {
			  node.data[offset - (nodeSize / 2)] = item;
			  node.count++;
		  }
	  }
  }

  /**
   * Removes the element at a given position while following specific removal rules
   * 
   * @param pos - position of the element to be removed
   */
  @Override
  public E remove(int pos)
  {
    
	  NodeInfo n = findIndice(pos);
	  Node node = n.getNode();
	  int offset = n.getOffset();
	  
	  if (node.count == 1) {
		  node.previous.next = node.next;
		  node.next.previous = node.previous;
	  } else if (node.next == tail || node.count > (nodeSize / 2)) {
		 node.removeItem(offset);
	  } else {
		  if (node.next.count > (nodeSize / 2)) {
			  node.removeItem(offset);
			  node.addItem(offset, node.next.data[0]);
			  node.next.removeItem(0);
		  } else {
			  node.removeItem(offset);
			  for (int i = 0; i < node.next.count; ++i) {
				  node.addItem(node.next.data[i]);
			  }
			  Node temp = node.next;
			  
			  node.next = temp.next;
			  temp.next.previous = node;
		  }
	  }
	  
	  
	  return null;
  }
  
  private NodeInfo findIndice(int pos) {
	  
	  /*
	   * Node traversing through;
	   */
	  Node current = head.next;
	  
	  if (size == 0) {
		  return null;
	  }
	  
	  /*
	   * Node with the correct position
	   */
	  Node innerNode;
	  NodeInfo nInfo;
	  int totalIndex = 0;
	  int testIndex = 0;
	  int offset;
	  
	  //Checks if there is pos is in the first node
	  if (pos < current.data.length) {
		  innerNode = current;
		  nInfo = new NodeInfo(innerNode, pos);
		  
		  return nInfo;
	  }
	  
	  totalIndex += current.data.length;
	  current = current.next;
	 
	  while(current != tail) {
		  testIndex = current.data.length + totalIndex;
		  if (pos < testIndex && pos >= totalIndex) {
			  offset = pos - totalIndex;
			  nInfo = new NodeInfo(current, offset);
			  return nInfo;
		  }
		  totalIndex += current.data.length;
		  current = current.next;
	  }
	  
	  
	  return null;
  }
  
  private class NodeInfo {
	  
	  /**
	   * Referenced node for the index
	   */
	  public Node node;
	  
	  /**
	   * the offset within the node
	   */
	  public int offset;
	  
	  /**
	   * 
	   * @param node 
	   * @param offset
	   */
	  public NodeInfo(Node node, int offset) {
		  this.node = node;
		  this.offset = offset;
	  }
	  
	  /**
	   * Returns the given node
	   * 
	   * @return node
	   */
	  public Node getNode() {
		  return node;
	  }
	  
	  /**
	   * Returns the offset in the node
	   * 
	   * @return offset
	   */
	  public int getOffset() {
		  return offset;
	  }
  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  Node current = head.next;
	  int i = 0;
	  
	  int totalNodes;
	  
	  E[] arr = (E[])new Comparable[size];
	  Comparator<E> comp = new Comparator<E>() {
		  @Override
		  public int compare(E lhs, E rhs) {
			  return lhs.compareTo(rhs);
		  }
	  };
	  
	 while(current != tail) {
		 for (int j = 0; j < current.count; ++j) {
			 arr[i] = current.data[j];
			 ++i;
		 }
		 
		 current = current.next;
	 }
	  
	  
	  insertionSort(arr, comp);
	  
	  //Amount of full nodes
	  totalNodes = arr.length / nodeSize;
	  
	  //Possible last node with left over elements
	  int leftOver = arr.length % nodeSize;
	  
	  //Reset the linked list to enter elements back into
	  head.next = tail;
	  tail.previous = head;
	  Node curr = head;
	  int iter = 0;
	  
	  
	  //Copies the array back into the linked list
	  for (int j = 0; j < totalNodes; ++j) {
		  Node temp = curr;
		  curr.next = new Node();
		  curr = curr.next;
		  
		  curr.next = tail;
		  curr.previous = temp;
		  temp.next = curr;
		  tail.previous = curr;
		  
		  for (int k = 0; k < nodeSize; ++k) {
			  curr.data[k] = arr[iter];
			  ++iter;
		  }
	  }
	  
	  if (leftOver != 0) {
		  Node temp = curr;
		  curr.next = new Node();
		  curr = curr.next;
		  
		  curr.next = tail;
		  curr.previous = temp;
		  temp.next = curr;
		  tail.previous = curr;
		  
		  for (int j = 0; j < leftOver; ++j) {
			  curr.data[j] = arr[iter];
		  }
	  }
	  
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  Node current = head.next;
	  int i = 0;
	  
	  E[] arr = (E[])new Comparable[size];
	  
	  //Copies all the elements into an array
	  while(current != tail) {
		  for (int j = 0; j < current.count; ++j) {
			  arr[i] = current.data[j];
			  ++i;
		  }
		 
		  current = current.next;
	  }
	  
	  bubbleSort(arr);
	  
	  //Resets the linked list
	  head.next = tail;
	  tail.previous = head;
	  Node curr = head;
	  int iter = 0;
	  //Amount of full nodes required
	  int totalNodes = arr.length / nodeSize;
	  
	  //Possible last node with left over elements
	  int leftOver = arr.length % nodeSize;
	  
	  
	  //Copies the array back into the linked list
	  for (int j = 0; j < totalNodes; ++j) {
		  Node temp = curr;
		  curr.next = new Node();
		  curr = curr.next;
		  
		  curr.next = tail;
		  curr.previous = temp;
		  temp.next = curr;
		  tail.previous = curr;
		  
		  for (int k = 0; k < nodeSize; ++k) {
			  curr.data[k] = arr[iter];
			  ++iter;
		  }
	  }
	  
	  if (leftOver != 0) {
		  Node temp = curr;
		  curr.next = new Node();
		  curr = curr.next;
		  
		  curr.next = tail;
		  curr.previous = temp;
		  temp.next = curr;
		  tail.previous = curr;
		  
		  for (int j = 0; j < leftOver; ++j) {
			  curr.data[j] = arr[iter];
		  }
	  }
  }
  
  /**
   * @return - returns an instance of the StoutIterator
   */
  @Override
  public Iterator<E> iterator()
  {
    return new StoutIterator();
  }
  
  /**
   * @return returns an instance of the StoutListIterator
   */
  @Override
  public ListIterator<E> listIterator()
  {
    return new StoutListIterator();
  }

  /**
   * @return returns an instance of the StoutListIterator at a start position
   */
  @Override
  public ListIterator<E> listIterator(int index)
  {
    return new StoutListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
  
  private class StoutIterator implements Iterator<E> {

	/**
	 * Current node selected
	 */
	private Node cursor = head.next;
	
	/**
	 * Inner index of each node
	 */
	int index = -1;
	
	/**
	 * Returns true if there is a next element in the list, otherwise returning false
	 * 
	 * @returns true or false
	 */
	@Override
	public boolean hasNext() {
		//Cannot have a next element if the list is empty
		if (size == 0) {
			return false;
		}
		
		/*
		 * FirstCheck: 
		 * Checks if there is a next index within the node, if there is it returns
		 * true.
		 * 
		 * Second Check:
		 * Since there cannot be an empty node, if the current node is full,
		 * it checks for the existence of a next node that is not the tail node.
		 * If there is a next node, then there must be an element within that node,
		 * meaning that it returns true.
		 */
		if ((index+1) < cursor.count) {
			return true;
		} else if (cursor.next != tail) {
			return true;
		}
		
		return false;
	}

	/**
	 * Returns the next in the list
	 * 
	 * @throws NoSuchElementException - if there is no next element
	 * @return returnVal 
	 */
	@Override
	public E next() {
		E returnVal = null;
		
		/*
		 * cursor is initialized to the be the next node after head
		 * meaning that the linked list is empty if the next node
		 * is the tail, meaning there is no next
		 */
		if (cursor == tail) {
			throw new NoSuchElementException();
		}
		
		if ((index+1) < cursor.count) {
			returnVal = cursor.data[index+1];
			++index;
		} else if (cursor.next != tail) { //There cannot be an empty node
			index = -1;
			cursor = cursor.next;
			
			returnVal = cursor.data[index+1];
		}
		
		if (returnVal == null) {
			throw new NoSuchElementException();
		}
		return returnVal;
	}
	  
  }
  
  
  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	  
	// instance variables ... 
	  /**
	   * Keeps track of the overall index of each element across the whole list
	   */
	  private int masterIndex;
	  
	  /**
	   * Keeps track of the index of each element within a node
	   */
	  private int subIndex;
	  
	  /**
	   * Keeps track if the user is allowed to use set(), add(), or remove()
	   */
	  boolean canModify = false;
	  
	  /**
	   *  Current node selected
	   */
	  private Node cursor;
	  
	  /**
	   *  Is true if the user is incrementing through the list
	   *  
	   *  Is false if the user is decrementing through the list
	   */
	  private boolean isNext;
	  
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	/*
    	 * Sets up with default settings
    	 * the subIndex starts at -1 (this is before the first element)
    	 * The cursor is set to the first node
    	 * You are not able to modify the list
    	 */
    	
    	cursor = head.next;
    	subIndex = -1;
    	masterIndex = -1;
    	isNext = true;
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public StoutListIterator(int pos)
    {
    	cursor = head.next;
    	//cursor = cursor.next;
    	subIndex = -1;
    	masterIndex = -1;
    	isNext = true;
    	
    	//Calls next() to iterate to the correct start position
    	for (int i = 1; i <= pos; ++i) {
    		next();
    	}
    }

    /**
     * Returns true if there is another element next of the cursor, otherwise returns false
     * 
     * @returns true or false
     */
    @Override
    public boolean hasNext()
    {
    	//Cannot have a next element if the list is empty
		if (size == 0) {
			return false;
		}
		
		/*
		 * FirstCheck: 
		 * Checks if there is a next index within the node, if there is it returns
		 * true.
		 * 
		 * Second Check:
		 * Since there cannot be an empty node, if the current node is full,
		 * it checks for the existence of a next node that is not the tail node.
		 * If there is a next node, then there must be an element within that node,
		 * meaning that it returns true.
		 */
		if ((subIndex+1) < cursor.count) {
			return true;
		} else if (cursor.next != tail) {
			return true;
		}
		
		return false;
    }

    /**
     * Returns the next value in the list
     * 
     * @throws NoSuchElementException - if there is no next element
     * @return returnVal
     */
    @Override
    public E next()
    {
    	E returnVal = null;
		
		/*
		 * cursor is initialized to the be the next node after head
		 * meaning that the linked list is empty if the next node
		 * is the tail, meaning there is no next
		 */
		if (cursor == tail) {
			throw new NoSuchElementException();
		}
		
		if (!isNext) {
			++subIndex;
			++masterIndex;
			
			returnVal = cursor.data[subIndex];
			
			return returnVal;
		}
		
		if ((subIndex+1) < cursor.count) {
			returnVal = cursor.data[subIndex+1];
			
			++subIndex;
			++masterIndex;
		} else if (cursor.next != tail) { //There cannot be an empty node
			subIndex = -1;
			cursor = cursor.next;
			
			returnVal = cursor.data[subIndex+1];
			
			++subIndex;
			++masterIndex;
		}
		
		if (returnVal == null) {
			throw new NoSuchElementException();
		}
		
		canModify = true;
		isNext = true;
		
		return returnVal;
    }

    /**
     * Removes the element that the cursor has just served
     * 
     * @throws IllegalStateException - if not allowed to edit the list
     */
    @Override
    public void remove()
    {
    	if (!canModify) {
    		throw new IllegalStateException();
    	}
    	int pos;
		if (isNext) {
			pos = subIndex;
		} else {
			pos = subIndex+1;
		}
    	
		NodeInfo n = findIndice(pos);
		  Node node = n.getNode();
		  int offset = n.getOffset();
		  
		  if (node.count == 1) {
			  node.previous.next = node.next;
			  node.next.previous = node.previous;
		  } else if (node.next == tail || node.count > (nodeSize / 2)) {
			 node.removeItem(offset);
		  } else {
			  if (node.next.count > (nodeSize / 2)) {
				  node.removeItem(offset);
				  node.addItem(offset, node.next.data[0]);
				  node.next.removeItem(0);
			  } else {
				  node.removeItem(offset);
				  for (int i = 0; i < node.next.count; ++i) {
					  node.addItem(node.next.data[i]);
				  }
				  Node temp = node.next;
				  
				  node.next = temp.next;
				  temp.next.previous = node;
			  }
		  }
    	
    	
    	canModify = false;
    }

    /**
     * Returns if there is a previous element in the list
     * 
     * @return true or false
     */
	@Override
	public boolean hasPrevious() {
		//Cannot have a next element if the list is empty
		if (size == 0) {
			return false;
		}
		
		/*
		 * FirstCheck: 
		 * Checks if there is a next index within the node, if there is it returns
		 * true.
		 * 
		 * Second Check:
		 * Since there cannot be an empty node, if the current node is full,
		 * it checks for the existence of a next node that is not the tail node.
		 * If there is a next node, then there must be an element within that node,
		 * meaning that it returns true.
		 */
		if ((subIndex-1) > -1) {
			return true;
		} else if (cursor.previous != head) {
			return true;
		}
		
		return false;
	}

	/**
	 * Returns the previous element in the cursor
	 * 
	 * @return returnVal
	 */
	@Override
	public E previous() {
		E returnVal = null;
		
		/*
		 * cursor is initialized to the be the next node after head
		 * meaning that the linked list is empty if the next node
		 * is the tail, meaning there is no next
		 */
		if (cursor == tail) {
			throw new NoSuchElementException();
		}
		
		if (!isNext) {
			--subIndex;
			--masterIndex;
			
			returnVal = cursor.data[subIndex];
			
			return returnVal;
		}
		
		if ((subIndex-1) >= -1) {
			returnVal = cursor.data[subIndex];
			--subIndex;
			--masterIndex;
		} else if (cursor.previous != head) { //There cannot be an empty node
			cursor = cursor.previous;
			subIndex = cursor.count;
			
			returnVal = cursor.data[subIndex-1];
			--subIndex;
			--masterIndex;
		}
		
		if (returnVal == null) {
			throw new NoSuchElementException();
		}
		
		canModify = true;
		isNext = false;
		
		return returnVal;
	}

	/**
	 * returns the next index
	 * 
	 * @return next index
	 */
	@Override
	public int nextIndex() {
		if ((masterIndex+1) >= size) {
			return size;
		} else {
			return masterIndex+1;
		}
	}

	/**
	 * returns the previous index
	 * 
	 * @return previous index
	 */
	@Override
	public int previousIndex() {
		if ((masterIndex-1) < 0) {
			return -1;
		} else {
			return masterIndex-1;
		}
	}

	/**
	 * Sets the current index to the given item
	 * 
	 * @param e 
	 * @throws IllegalStateException
	 */
	@Override
	public void set(E e) {
		//Checks to see if you are able to modify the list
		if (!canModify) {
			throw new IllegalStateException();
		}
		
		if (isNext) {
			if (subIndex > -1){ //next
				cursor.data[subIndex] = e;
			} else {
				cursor.previous.data[cursor.previous.count-1] = e;
			}
		} else if (!isNext) { //previous
			if (subIndex+1 < cursor.count) {
				cursor.data[subIndex+1] = e;
			} else {
				cursor.previous.data[cursor.previous.count-1] = e;
			}
		}
		
		
	}

	/**
	 * Adds the element to the position the cursor is at
	 * 
	 * @param e
	 * @throws IllegalStateException
	 */
	@Override
	public void add(E e) {
		if (!canModify) {
			throw new IllegalStateException();
		}
		
		int pos;
		if (isNext) {
			pos = subIndex;
		} else {
			pos = subIndex+1;
		}
		
		 NodeInfo n = findIndice(pos);
		  Node node = n.getNode();
		  int offset = n.getOffset();
		  
		  
		  if (offset == 0) {
			  if (node.previous != head && node.count > node.previous.count) {
				  //node.previous.data[node.previous.count] = item;
				  node.addItem(node.previous.count, e);
			  } else if (node == tail) {
				  Node temp = node;
				  node = new Node();
				  
				  node.previous = temp;
				  temp.next = node;
				  tail.previous = node;
				  node.next = tail;
				  
				  node.addItem(node.count, e);
			  }
		  } else if(node.count < node.data.length) { 
			  for (int i = offset; i < node.count; ++i) {
				  node.data[i+1] = node.data[i];
			  }
			  node.addItem(offset, e);
		  } else {
			  Node nextNode = new Node();
			  nextNode.next = node.next;
			  node.next.previous = nextNode;
			  node.next = nextNode;
			  nextNode.previous = node;
			  
			  int j = 0;
			  for (int i = nodeSize / 2; i < nodeSize; ++i) {
				  nextNode.data[j] = node.data[i];
				  nextNode.count++;
				  node.data[i] = null;
				  node.count--;
				  ++j;
			  }
			  
			  if (offset <= nodeSize / 2) {
				  node.data[offset] = e;
				  node.count++;
			  } else {
				  node.data[offset - (nodeSize / 2)] = e;
				  node.count++;
			  }
		  }
		
		
		//add(e, masterIndex);
		//add(masterIndex, e)
		
		
		
		canModify = false;
	}
    
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }
  

  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  //A list of 0 or 1 elements is already sorted
	  if (arr.length == 0 || arr.length == 1) {
		  return;
	  }
	  
	  E temp;
	  int j;
	  for (int i = 1; i < arr.length; ++i) {
		  j = i;
		  while(j > 0 && comp.compare(arr[j-1], arr[j]) == 1) {
			  temp = arr[j-1];
			  arr[j-1] = arr[j];
			  arr[j] = temp;
			  
			  --j;
		  }
	  }
	  
	  return;
  }
  

  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  //A list of 0 or 1 elements is already sorted
	  if (arr.length == 0 || arr.length == 1) {
		  return;
	  }
	  
	  E temp;
	  for (int i = 1; i < arr.length; ++i) {
		  for (int j = 0; j < arr.length-1; ++j) {
			  if (arr[j].compareTo(arr[j+1]) == -1) {
				  temp = arr[j];
				  arr[j] = arr[j+1];
				  arr[j+1] = temp;
			  }
		  }
	  }
	  
	  return;
  }
 

}