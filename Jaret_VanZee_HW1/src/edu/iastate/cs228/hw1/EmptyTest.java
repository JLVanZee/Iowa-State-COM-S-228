package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmptyTest {

	@Test
	void test() {
		Town t = new Town(4, 4);
		t.randomInit(10);
		Empty e = new Empty(t, 0, 0);
		ISPBusiness.updatePlain(t);
		
		Assertions.assertEquals("E", e.cellIdentity());
		Assertions.assertEquals(State.EMPTY, e.who());
		Assertions.assertEquals(t.grid[1][0].who(), State.CASUAL);
		
	}

}
