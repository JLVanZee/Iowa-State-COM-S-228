package edu.iastate.cs228.hw1;

/**
 * 
 * @author Jaret Van Zee
 *
 */
public class Casual extends TownCell{

	/**
	 * @param p
	 * @param r
	 * @param c
	 * 
	 * calls the constructor of the TownCell class with the same parameters
	 */
	public Casual(Town p, int r, int c) {
		super(p, r, c);
	}

	/**
	 * Returns the identity of the cell
	 * 
	 * @return State.CASUAL
	 */
	@Override
	public State who() {
		return State.CASUAL;
	}

	
	/**
	 * Returns the string identity of the cell, used for the toString() method
	 * 
	 * @return "C"
	 */
	@Override
	public String cellIdentity() {
		return "C";
	}

	
	/**
	 * @param tNew
	 * Hierarchy of update rules for this given cell
	 * 
	 * 1. Casual cell updates to reseller cell if empty + outage neighbors in total
	 * <= 1 in next month cycle
	 * 2. Casual cell updates to outage if >0 reseller neighbors in next month cycle
	 * 3. Casual cell updates to streamer if >0 streamer neighbors in next month cycle
	 * 4. Casual cell updates to streamer if >=5 casual neighbors in the next month
	 * cycle
	 * 5. Casual cell remains the same if rules above are not satisfied
	 * 
	 * @return updated TownCell
	 */
	@Override
	public TownCell next(Town tNew) {
		census(nCensus);		
		if ((nCensus[EMPTY] + nCensus[OUTAGE]) <= 1) {
			tNew.grid[row][col]= new Reseller(tNew, row, col);
			return tNew.grid[row][col];
		} else if(nCensus[RESELLER] > 0) {
			tNew.grid[row][col]= new Outage(tNew, row, col);
			return tNew.grid[row][col];
		} else if (nCensus[STREAMER] > 0){
			tNew.grid[row][col]= new Streamer(tNew, row, col);
			return tNew.grid[row][col];
		} else if (nCensus[CASUAL] >= 5) {
			tNew.grid[row][col]= new Streamer(tNew, row, col);
			return tNew.grid[row][col];
		} else {
			tNew.grid[row][col]= new Casual(tNew, row, col);
			return tNew.grid[row][col];
		}
	}

}
