package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Jaret Van Zee
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{

	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(points);		
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int length = pts.length;
		if (length <= 1) {
			return;
		}
		
		int mid = pts.length / 2;
		int j = 0;
		
		Point[] leftHalf = new Point[mid];
		Point[] rightHalf = new Point[length-mid];
		
		for (int i = 0; i < mid; ++i) {
			leftHalf[i] = pts[i];
		}
		for (int i = mid; i < length; ++i) {
			rightHalf[j] = pts[i];
			++j;
		}
		
		mergeSortRec(leftHalf);
		mergeSortRec(rightHalf);
		
		//Merging of two arrays
		int leftLength = leftHalf.length;
		int rightLength = rightHalf.length;
		
		int i = 0, n = 0, k = 0;
		
		while(i < leftLength && n < rightLength) {
			if (leftHalf[i].compareTo(rightHalf[n]) == -1) {
				pts[k] = leftHalf[i];
				++i;
				++k;
			} else {
				pts[k] = rightHalf[n];
				++n;
				++k;
			}
		}
		
		if (i >= leftLength) {
			for (int l = n; l < rightHalf.length; ++l) {
				pts[k] = rightHalf[l];
				++k;
			}
		} else {
			for (int l = i; l < leftHalf.length; ++l) {
				pts[k] = leftHalf[l];
				++k;
			}
		}
	}
}
