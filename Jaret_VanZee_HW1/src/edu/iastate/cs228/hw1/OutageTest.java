package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OutageTest {

	@Test
	void test() {
		//fail("Not yet implemented");
		Town t = new Town(4, 4);
		Outage o = new Outage(t, 0, 0);
		TownCell u;
		u = o.next(t);
		
		Assertions.assertEquals(o.cellIdentity(), o.cellIdentity());
		Assertions.assertEquals(State.OUTAGE, o.who());
		Assertions.assertEquals(State.EMPTY, u.who());
		
	}

}
