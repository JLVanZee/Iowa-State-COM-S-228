package edu.iastate.cs228.hw1;

public class Streamer extends TownCell{

	/**
	 * @param p
	 * @param r
	 * @param c
	 * 
	 * calls the constructor of the TownCell class with the same parameters
	 */
	public Streamer(Town p, int r, int c) {
		super(p, r, c);
	}

	/**
	 * Returns the identity of the cell
	 * 
	 * @return State.STREAMER
	 */
	@Override
	public State who() {
		return State.STREAMER;
	}

	/**
	 * Returns the string identity of the cell, used for the toString() method
	 * 
	 * @return "s"
	 */
	@Override
	public String cellIdentity() {
		return "S";
	}

	/**
	 * @param tNew
	 * Hierarchy of update rules for this given cell
	 * 
	 * 1. Streamer cell updates to reseller cell if empty + outage neighbors in total
	 * <= 1 in next month cycle
	 * 2. Streamer cell updates to outage if there is >0 reseller neighbors in next
	 * month cycle
	 * 3. Streamer cell updates to empty if there is >0 outage neighbors in next month
	 * cycle
	 * 4. Streamer will not update if above rules are not matched
	 * 
	 * @return updated TownCell
	 */
	@Override
	public TownCell next(Town tNew) {
		census(nCensus);
		if (nCensus[EMPTY] + nCensus[OUTAGE] <= 1) {
			tNew.grid[row][col]= new Reseller(tNew, row, col);
			return tNew.grid[row][col];
		} else if (nCensus[RESELLER] > 0) {
			tNew.grid[row][col]= new Outage(tNew, row, col);
			return tNew.grid[row][col];
		} else if (nCensus[OUTAGE] > 0) {
			tNew.grid[row][col]= new Empty(tNew, row, col);
			return tNew.grid[row][col];
		} else {
			tNew.grid[row][col]= new Streamer(tNew, row, col);
			return tNew.grid[row][col];
		}
	}

}
