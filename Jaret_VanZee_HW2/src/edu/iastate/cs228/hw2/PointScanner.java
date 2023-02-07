package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author Jaret Van Zee
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}
		this.sortingAlgorithm = algo;
		this.points = new Point[pts.length];
		
		for (int i = 0; i < pts.length; ++i) {
			points[i] = new Point(pts[i]);
		}
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		this.sortingAlgorithm = algo;
		ArrayList<Integer> userInputs = new ArrayList<>();
		int filePointSize;
		int x = 0, y = 1;
		
		File userFile = null;
		Scanner scnrFile = null;
		
		try {
			userFile = new File(inputFileName);
			scnrFile = new Scanner(userFile);
			while(scnrFile.hasNextInt()) {
				userInputs.add(scnrFile.nextInt());
			}
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
		} finally {
			scnrFile.close();
		}
		
		//Throws an exception if there is an odd amount of integers in the file
		if (userInputs.size() % 2 == 1) {
			System.out.println("An odd amount of integers are in the file");
			throw new InputMismatchException();
		}
		
		//Initializes with size of half the amount of integers in the file
		filePointSize = userInputs.size() / 2;
		points = new Point[filePointSize];
		
		for (int i = 0; i < points.length; ++i) {
			points[i] = new Point(userInputs.get(x), userInputs.get(y));
			x += 2;
			y += 2;
		}

	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the medianCoordinatePoint
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
		
		long beforeTime, afterTime;
		AbstractSorter aSorter = null; 
		Point[] p = new Point[2];
		
		switch (sortingAlgorithm) {
			case SelectionSort:
				aSorter = new SelectionSorter(points);
				break;
			case InsertionSort:
				aSorter = new InsertionSorter(points);
				break;
			case MergeSort:
				aSorter = new MergeSorter(points);
				break;
			case QuickSort:
				aSorter = new QuickSorter(points);
				break;
		}
		scanTime = 0;
		
		//Does the sorting for the x and y coordinates
		//Scans in the time just before and after the sort to calculate the sort time
		for (int i = 0; i < 2; ++i) {	
			aSorter.setComparator(i);
			beforeTime = System.nanoTime();
			aSorter.sort();
			afterTime = System.nanoTime();
			
			scanTime += (afterTime - beforeTime);
			p[i] = aSorter.getMedian();
		}
		
		//Each index represents the median coordinate point for x and y respectively
		medianCoordinatePoint = new Point(p[0].getX(), p[1].getY());
		
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		int size = points.length;
		
		String s = String.format("%-18s", sortingAlgorithm);
		s += Integer.toString(size);
		s += "  ";
		s += Long.toString(scanTime);
		
		return s; 
	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		String MCP = "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")";
		return MCP; 
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		//creates file
		try {
			File file = new File("outputFileName.txt");
			if (file.createNewFile()) {
				//DO NOTHING
			} else {
				System.out.println("Unable to create file");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//writes median coordinate point to file
		try {
			FileWriter fileWriter = new FileWriter("outputFileName.txt");
			fileWriter.write(this.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
}
