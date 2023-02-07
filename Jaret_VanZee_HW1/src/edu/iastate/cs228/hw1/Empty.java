package edu.iastate.cs228.hw1;

public class Empty extends TownCell{

	/**
	 * @param p
	 * @param r
	 * @param c
	 * 
	 * calls the constructor of the TownCell class with the same parameters
	 */
	public Empty(Town p, int r, int c) {
		super(p, r, c);
	}

	/**
	 * Returns the identity of the cell
	 * 
	 * @return State.EMPTY
	 */
	@Override
	public State who() {
		return State.EMPTY;
	}

	/**
	 * Returns the string identity of the cell, used for the toString() method
	 * 
	 * @return "E"
	 */
	@Override
	public String cellIdentity() {
		return "E";
	}

	/**
	 * @param tNew
	 * Hierarchy of update rules for this given cell
	 * 
	 * 1. Empty cell updates to reseller cell if empty + outage neighbors in total
	 * <= 1 in next month cycle
	 * 2. Empty cell updates to casual cell in next month cycle
	 * 
	 * @return updated TownCell
	 */
	@Override
	public TownCell next(Town tNew) {
		census(nCensus);
		if ((nCensus[EMPTY] + nCensus[OUTAGE]) <= 1) {
			tNew.grid[row][col]= new Reseller(tNew, row, col);
			return tNew.grid[row][col];
		}
		tNew.grid[row][col]= new Casual(tNew, row, col);
		return tNew.grid[row][col];
	}

}
