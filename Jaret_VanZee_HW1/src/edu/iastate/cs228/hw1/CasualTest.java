package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CasualTest {

	@Test
	void test() {
		Town t = new Town(4, 4);
		t.randomInit(10);
		Casual c = new Casual(t, 0, 0);
		ISPBusiness.updatePlain(t);
		
		Assertions.assertEquals(c.who(), State.CASUAL);
		Assertions.assertEquals(c.cellIdentity(), "C");
		Assertions.assertEquals(t.grid[1][0].who(), State.CASUAL);
		
		
	}

}
