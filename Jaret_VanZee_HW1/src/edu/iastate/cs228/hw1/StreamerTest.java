package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StreamerTest {

	@Test
	void test() {
		Town t = new Town(4, 4);
		t.randomInit(10);
		
		Streamer s = new Streamer(t, 0, 0);
		
		ISPBusiness.updatePlain(t);
		
		Assertions.assertEquals("S", s.cellIdentity());
		Assertions.assertEquals(State.STREAMER, s.who());
		Assertions.assertEquals(State.OUTAGE, t.grid[2][1].who());
	}

}
