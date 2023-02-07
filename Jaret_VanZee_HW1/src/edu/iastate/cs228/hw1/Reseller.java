package edu.iastate.cs228.hw1;

public class Reseller extends TownCell{

	/**
	 * @param p
	 * @param r
	 * @param c
	 * 
	 * calls the constructor of the TownCell class with the same parameters
	 */
	public Reseller(Town p, int r, int c) {
		super(p, r, c);
	}

	/**
	 * Returns the identity of the cell
	 * 
	 * @return State.RESELLER
	 */
	@Override
	public State who() {
		return State.RESELLER;
	}

	/**
	 * Returns the string identity of the cell, used for the toString() method
	 * 
	 * @return "R"
	 */
	@Override
	public String cellIdentity() {
		return "R";
	}

	/**
	 * @param tNew
	 * Hierarchy of update rules for this given cell
	 * 
	 * 1. Reseller cell updates to empty if casual <= 3 or empty >= 3 in neighborhood
	 * in next month cycle
	 * 2. Reseller cell updates to streamer if 5>= casual in neighborhood in next month
	 * cycle
	 * 3. Reseller cell does not update if the above rules are not satisfied
	 * 
	 * @return updated TownCell
	 */
	@Override
	public TownCell next(Town tNew) {
		census(nCensus);
		if (nCensus[CASUAL] <= 3 || nCensus[EMPTY] >= 3) {
			tNew.grid[row][col]= new Empty(tNew, row, col);
			return tNew.grid[row][col];
		} else if (nCensus[CASUAL] >= 5) {
			tNew.grid[row][col]= new Streamer(tNew, row, col);
			return tNew.grid[row][col];
		}
		tNew.grid[row][col]= new Reseller(tNew, row, col);
		return tNew.grid[row][col];
	}

}
