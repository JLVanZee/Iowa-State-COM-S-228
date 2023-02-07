package edu.iastate.cs228.hw1;

public class Outage extends TownCell{

	/**
	 * @param p
	 * @param r
	 * @param c
	 * 
	 * calls the constructor of the TownCell class with the same parameters
	 */
	public Outage(Town p, int r, int c) {
		super(p, r, c);
	}

	/**
	 * Returns the identity of the cell
	 * 
	 * @return State.OUTAGE
	 */
	@Override
	public State who() {
		return State.OUTAGE;
	}

	/**
	 * Returns the string identity of the cell, used for the toString() method
	 * 
	 * @return "O"
	 */
	@Override
	public String cellIdentity() {
		return "O";
	}

	/**
	 * @param tNew
	 * Hierarchy of update rules for this given cell
	 * 
	 * 1. Outage cell updates to empty cell in next month cycle
	 * 
	 * @return updated TownCell
	 */
	@Override
	public TownCell next(Town tNew) {
		census(nCensus);
		tNew.grid[row][col] = new Empty(tNew, row, col);
		return tNew.grid[row][col];
	}

}
