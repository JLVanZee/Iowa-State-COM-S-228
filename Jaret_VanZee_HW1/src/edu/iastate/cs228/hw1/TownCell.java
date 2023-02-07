package edu.iastate.cs228.hw1;

/**
 * 
 * @author Jaret Van Zee
 *
 */
public abstract class TownCell {

	protected Town plain;
	protected int row;
	protected int col;
	
	
	// constants to be used as indices.
	protected static final int RESELLER = 0;
	protected static final int EMPTY = 1;
	protected static final int CASUAL = 2;
	protected static final int OUTAGE = 3;
	protected static final int STREAMER = 4;
	
	public static final int NUM_CELL_TYPE = 5;
	
	//Use this static array to take census.
	public static final int[] nCensus = new int[NUM_CELL_TYPE];

	public TownCell(Town p, int r, int c) {
		plain = p;
		row = r;
		col = c;
	}
	
	/**
	 * Checks all neighborhood cell types in the neighborhood.
	 * Refer to homework pdf for neighbor definitions (all adjacent
	 * neighbors excluding the center cell).
	 * Use who() method to get who is present in the neighborhood
	 *  
	 * @param counts of all customers
	 */
	public void census(int nCensus[]) {
		// zero the counts of all customers
		nCensus[RESELLER] = 0; 
		nCensus[EMPTY] = 0; 
		nCensus[CASUAL] = 0; 
		nCensus[OUTAGE] = 0; 
		nCensus[STREAMER] = 0; 
		
		/*
		 * Counts up each of the neighbors
		 * A for loop is used to iterate through
		 * Skips if it is on the cell itself, or if one of the indices is going to be
		 * out of bounds
		 * Then counts each into the nCensus array
		 */
	
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				if (i == 0 && j == 0) {
					continue;
				}

				try {
					if (plain.grid[row+i][col+j].who() == State.RESELLER) {
						++nCensus[RESELLER];
					} else if (plain.grid[row+i][col+j].who() == State.EMPTY) {
						++nCensus[EMPTY];
					} else if (plain.grid[row+i][col+j].who() == State.CASUAL) {
						++nCensus[CASUAL];
					} else if (plain.grid[row+i][col+j].who() == State.OUTAGE) {
						++nCensus[OUTAGE];
					} else if (plain.grid[row+i][col+j].who() == State.STREAMER) {
						++nCensus[STREAMER];
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//Do nothing
				}
			}
		}
	}

	/**
	 * Gets the identity of the cell.
	 * 
	 * @return State
	 */
	public abstract State who();
	
	/**
	 * Returns the a single letter string to be used for concatenation in the
	 * toString() method
	 * 
	 * @return string
	 */
	public abstract String cellIdentity();
	
	/**
	 * Determines the cell type in the next cycle.
	 * 
	 * @param tNew: town of the next cycle
	 * @return TownCell
	 */
	public abstract TownCell next(Town tNew);
}
