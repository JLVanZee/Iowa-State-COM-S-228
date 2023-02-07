package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TownTest {

	@Test
	void test() {
		Town t = new Town(4, 4);
		t.randomInit(10);
		System.out.println(t.toString());
		System.out.println();
		String test = "O R O R \n"
				+ "E E C O \n"
				+ "E S O S \n"
				+ "E O R R ";
		
		Assertions.assertEquals(4, t.getLength());
		Assertions.assertEquals(4,  t.getWidth());
		
		//Checks to see if at least the first element is populated
		//meaning that the rest of the elements should be populated
		Assertions.assertEquals(State.OUTAGE, t.grid[0][0].who());
		
		//Checks the toString method
		Assertions.assertEquals(test, t.toString());
		
	}

}
