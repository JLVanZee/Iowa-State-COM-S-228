package edu.iastate.cs228.hw4;

/**
 * 
 * @author Jaret Van Zee
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	
	/**
	 * Main function, prompts the user for a .arch type file. Then reads in data,
	 * creates a binary tree from the data. Calls other functions to do the building
	 * process.
	 * 
	 * Also reads in a code string to be decoded. Calls other functions to do the 
	 * decoding process.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		String fileName = null;
		String treeBuild = "";
		String code = null;
		
		//array for reading in initial lines
		String[] fileLines = new String[3];
		
		Scanner scnr = new Scanner(System.in);
		Scanner fileScnr = null;
		
		System.out.print("Please enter filename to decode: ");
		fileName = scnr.next();
		scnr.close();
		System.out.println();
		
		File file = new File(fileName);
		try {
			fileScnr = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid file name. Program terminated.");
			System.exit(0);
		}
		
		//This block of code separates all the lines of the file into separate strings
		
		fileLines[0] = fileScnr.nextLine();
		fileLines[1] = fileScnr.nextLine();
		if (fileScnr.hasNext()) {
			fileLines[2] = fileScnr.nextLine();
			
			treeBuild = fileLines[0] + "\n" + fileLines[1];
			code = fileLines[2];
		} else {
			treeBuild = fileLines[0];
			code = fileLines[1];
		}
		
		//Constructs the tree
		MsgTree msgTree = new MsgTree(treeBuild);
		
		
		//prints out the codes for each character
		System.out.println("character  code");
		System.out.println("-------------------------");
		MsgTree.printCodes(msgTree, "");
		
		System.out.println();
		
		//prints out the message and statistics for the code
		System.out.println("MESSAGE:");
		decode(msgTree, code);
	}
	
	/**
	 * This function decodes a given string of 1's and 0's by iterating through the
	 * binary tree starting at the root every time. It then prints out what the message
	 * is and the statistics of the code and the decoded string.
	 * 
	 * 
	 * @param msgTree - The message tree to be traversed
	 * @param code - string of 1's and 0's to be decoded in the tree
	 */
	public static void decode(MsgTree msgTree, String code) {
		String decodedMessage = "";
		MsgTree root = msgTree;
		
		//main iterator
		for (int i = 0; i < code.length(); ++i) {
			if (code.charAt(i) == '0') {
				root = root.left;
			} else {
				root = root.right;
			}
			
			if (root.payloadChar != '^') {
				decodedMessage += Character.toString(root.payloadChar);	
				root = msgTree;
			}
		}
		
		System.out.println(decodedMessage);
		
		//prints out statistics
		double avg = (double)MsgTree.codeCounter/MsgTree.totalChars;
		System.out.println();
		
		System.out.println("STATISTICS: ");
		System.out.print("Avg bits/char:          ");
		System.out.println(Math.floor(avg * 10) / 10);
		System.out.println("Total characters:       "+ decodedMessage.length());
		System.out.println("Space savings:          " + 100*(1-(Math.floor((avg/16) * 10) / 10)) + "%");
	}
}
