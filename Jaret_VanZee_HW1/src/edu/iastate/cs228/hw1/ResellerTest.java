package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResellerTest {

	@Test
	void test() {
		Town t = new Town(4, 4);
		t.randomInit(10);
		Reseller r = new Reseller(t, 0, 0);
		ISPBusiness.updatePlain(t);
		
		Assertions.assertEquals("R", r.cellIdentity());
		Assertions.assertEquals(State.RESELLER, r.who());
		Assertions.assertEquals(State.EMPTY, t.grid[0][1].who());
		
		
	}

}
