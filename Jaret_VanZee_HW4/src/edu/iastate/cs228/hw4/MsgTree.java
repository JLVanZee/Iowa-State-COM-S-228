package edu.iastate.cs228.hw4;


/**
 * 
 * @author Jaret Van Zee
 *
 */
public class MsgTree {

	/**
	 * The char data stored in each node
	 */
	public char payloadChar;
	
	/**
	 * left child of the node
	 */
	public MsgTree left;
	
	/**
	 * right child of the node
	 */
	public MsgTree right;
	
	/**
	 * Keeps track of the total length of the character's code
	 */
	public static int codeCounter = 0;
	
	/**
	 * Keeps track of the total amount of characters
	 */
	public static int totalChars = 0;
	
	/**
	 * Iterators through to keep track of position in the tree building string
	 */
	private static int staticCharIdx = 0;
	
	
	/**
	 * Recursively builds a tree with preorder. Sets the payload of each different node
	 * throughout the tree. If a payload has a non '^' character, it is a leaf and has
	 * no children.
	 * 
	 * @param encodingString string used to build the tree
	 */
	public MsgTree(String encodingString) {
		if (encodingString.charAt(staticCharIdx) != '^') {
			++totalChars;
			if (encodingString.charAt(staticCharIdx) == '\\' && //special case needed for a new line character
					encodingString.charAt(staticCharIdx+1) == 'n') {
				this.payloadChar = '\n';
				staticCharIdx += 2;
				return;
			}
			this.payloadChar = encodingString.charAt(staticCharIdx);
			++staticCharIdx;
			return;
		} else {
			this.payloadChar = '^';
			++staticCharIdx;
		}
		
		
		left = new MsgTree(encodingString);
		right = new MsgTree(encodingString);
	}
	
	/**
	 * Constructor for a specific character to be used
	 * 
	 * @param payloadChar 
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
	}
	
	
	/**
	 * Prints each of the individual codes for each of the characters, iterates through with
	 * preorder recursively. Also sets the codeCounter static variable to be used for
	 * statistics in the end.
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {
		if (root.payloadChar != '^') {
			codeCounter += code.length();
			//special cases for a new line character or space character needed
			if (root.payloadChar == ' ') {
				System.out.println(" ' '      " + code);
				return;
			}
			
			if (root.payloadChar == '\n') {
				System.out.println("  \\n      " + code);
				return;
			}
			
			System.out.println("  " + root.payloadChar + "       " + code);
			return;
		} 
		
		printCodes(root.left, code + "0");
		printCodes(root.right, code + "1");
	}
	
}
