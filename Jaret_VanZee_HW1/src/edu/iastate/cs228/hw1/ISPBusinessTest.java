package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ISPBusinessTest {

	@Test
	void test() {
		String test = "E R R R \n"
				+ "R O R R \n"
				+ "R O R R \n"
				+ "E R E E ";
		/*
		 * Main method and random generation method are not tested because Main is already
		 * incorporated in and randomGeneration is incorporated within the main method
		 */
		Town t = new Town(4, 4);
		t.randomInit(10);
		
		Assertions.assertEquals(1, ISPBusiness.getProfit(t));
		
		//Runs the update cycle for simulate one year
		for (int i = 0; i < 11; ++i) {
			t = ISPBusiness.updatePlain(t);
		}
		Assertions.assertEquals(test, t.toString());
		
	}

}
