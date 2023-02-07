package edu.iastate.cs228.hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Jaret Van Zee
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		
		for (int i = 0; i < tOld.getLength(); ++i) {
			for (int j = 0; j < tOld.getWidth(); ++j) {
				tNew.grid[i][j] = tOld.grid[i][j].next(tNew);
			}
		}
		return tNew;
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) {
		int profit = 0;
		//Counts all the casual cells
		for (int i = 0; i < town.getLength(); ++i) {
			for (int j = 0; j < town.getWidth(); ++j) {
				if (town.grid[i][j].who() == State.CASUAL) {
					profit++;
				}
			}
		}
		
		return profit;
	}
	

	/**
	 *  Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should print the profit percentage
	 *  with two digits after the decimal point:  Example if profit is 35.5600004, your output
	 *  should be:
	 *
	 *	35.56%
	 *  
	 * Note that this method does not throw any exception, so you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String []args) {
		Scanner scnr = new Scanner(System.in);
		int userChoice = 0;
		
		System.out.println("How to populate grid (type 1 or 2): 1: from a file. 2: randomly with seed");
		try {
			userChoice = scnr.nextInt();
		} catch (InputMismatchException e) { //terminates the program
			System.out.println("Invalid format, please enter the Integer 1 or 2");
			System.out.println("Program will now be terminated.");
			System.exit(0);
		}
		
		if (userChoice == 1) {
			//From file choice
			Town userTown = fileGeneration();
			simulateYear(userTown);
		} else if (userChoice == 2) {
			//Random seed choice
			Town userTown = randomGeneration();
			simulateYear(userTown);
		} else {
			System.out.println("Please enter 1 or 2");
		}
		scnr.close();
	}
	

	/**
	 * Scans in a row, col, and seed from the user
	 * Proceeds to set up the town with the random constructor
	 * then fills the town with the random seed
	 * 
	 * @return randTown - the new town set up with the random constructor
	 */
	private static Town randomGeneration() {
		Scanner scnr = new Scanner(System.in);
		int row, col, seed;
		
		System.out.println("Provide rows, cols, and seed integer separated by spaces: ");

		String userInput = scnr.nextLine();
		Scanner parse = new Scanner(userInput);
		row = parse.nextInt();
		col = parse.nextInt();
		seed = parse.nextInt();
		
		Town randTown = new Town(row, col);
		randTown.randomInit(seed);
		scnr.close();
		parse.close();
		
		return randTown;
	}
	
	/**
	 * 
	 * @return userTown - A created town based on a user file
	 */
	private static Town fileGeneration() {
		Scanner scnr = new Scanner(System.in);
		File userFile = null;
		String filePath;
		Town userTown = null;
		
		System.out.println("Please enter file path:");
		try {
			filePath = scnr.next();
			userTown = new Town(filePath);
		} catch (FileNotFoundException e) { //terminates the program
			System.out.println("The file you entered could not be found");
			System.out.println("Program will now be terminated.");
			System.exit(0);
		}
		
		return userTown;
	}
	
	/*
	 * Simulates one year of customers, displaying grid and profit from each month.
	 * Then calculating an overall possible profit at the end of the year
	 */
	private static void simulateYear(Town userTown) {
		int profit = 0;
		double detProfit = 0.0;
		double area = 0.0;
		/*
		 * Initial set up, calculates the profit from the first neighborhood,
		 * proceeds to then calculate the neighborhood area
		 */
		System.out.println(" Start:");
		System.out.println();
		System.out.println(userTown.toString());
		profit = getProfit(userTown);
		area = userTown.getLength() * userTown.getWidth();
		detProfit += (100 * profit) / area;
		System.out.println(" Profit: " + getProfit(userTown));
		System.out.println();
		
		
		/*
		 * 11 month simulation, this simulates the rest of the year calculating the profit
		 * then figuring the overall percentage of potential profit.
		 */
		for (int i = 1; i < 12; ++i) {
			userTown = updatePlain(userTown);
			System.out.println(" After itr: " + i);
			System.out.println();
			System.out.println(userTown.toString());
			System.out.println("Profit: " + getProfit(userTown));
			System.out.println();
			
			profit = getProfit(userTown);
			detProfit += (100 * profit) / area;
		}
		detProfit /= 12.0; //Averages them across all months
		
		/*
		 * Formats the output to two decimal points
		 * However leaves out a 0, this could be solved with
		 * printf however it for some reason does not allow for 
		 * the program to compile.
		 */
		System.out.println(Math.floor(detProfit * 100) / 100 + "%");
	}
}
