package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


/**
 *  @author Jaret Van Zee
 *
 */
public class Town {
	
	private int length, width;  //Row and col (first and second indices)
	public TownCell[][] grid;
	
	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) {
		this.length = length;
		this.width = width;
		grid = new TownCell[length][width];
	}
	
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException {
		File userFile = null;
		
		userFile = new File(inputFileName);
		Scanner scnrFile = new Scanner(userFile);
		
		length = scnrFile.nextInt();
		width = scnrFile.nextInt();
		scnrFile.nextLine();
		
		/*
		 * Reads the first line as integers, then reads the rest of the lines as Strings.
		 * Proceeds to parse the Strings into a 2d array
		 */
		String[] tempArr = new String[length];
		grid = new TownCell[length][width];
		char check;
		for(int i = 0; i < length; ++i) {
			tempArr[i] = scnrFile.nextLine();
		}
		
		for(int i = 0; i < length; ++i) {
			for (int j = 0; j < width; ++j) {
				check = tempArr[i].charAt(2*j);
				if (check == 'C') {
					grid[i][j] = new Casual(this, i, j);
				} else if (check == 'S') {
					grid[i][j] = new Streamer(this, i, j);
				} else if (check == 'R') {
					grid[i][j] = new Reseller(this, i, j);
				} else if (check == 'O') {
					grid[i][j] = new Outage(this, i, j);
				} else if (check == 'E') {
					grid[i][j] = new Empty(this, i, j);
				}
			}
		}
	}
	
	/**
	 * Returns width of the grid.
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns length of the grid.
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 */
	public void randomInit(int seed) {
		Random rand = new Random(seed);
		int randNum;
		
		//grid = new TownCell[length][width];
		rand.setSeed(seed);
		
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j < width; ++j) {
				randNum = rand.nextInt(5);
				if (randNum == 0) {
					grid[i][j] = new Reseller(this, i, j);
				} else if (randNum == 1) {
					grid[i][j] = new Empty(this, i, j);
				} else if (randNum == 2) {
					grid[i][j] = new Casual(this, i, j);
				} else if (randNum == 3) {
					grid[i][j] = new Outage(this, i, j);
				} else if (randNum == 4) {
					grid[i][j] = new Streamer(this, i, j);
				}
			}
		}
	}
	
	/**
	 * Output the town grid. For each square, output the first letter of the cell type.
	 * Each letter should be separated either by a single space or a tab.
	 * And each row should be in a new line. There should not be any extra line between 
	 * the rows.
	 */
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < length-1; ++i) {
			for (int j = 0; j < width; ++j) {
				s += grid[i][j].cellIdentity() + " ";
			}
			s += "\n";
		}
		for (int i = 0; i < width; ++i) {
			s += grid[length-1][i].cellIdentity() + " ";
		}
		return s;
	}
}
