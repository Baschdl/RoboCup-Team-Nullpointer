package de.null_pointer.navigation.map;

import org.junit.Test;

import de.null_pointer.pi_server.InitializeProgram;

public class NavigationTest {
	private InitializeProgram initProgram;
	private Navigation testNavigation;

	public NavigationTest() {
		initProgram = new InitializeProgram(null);
		initProgram.initializeProperties();
		testNavigation = new Navigation(initProgram.getPropPiServer());
	}

	@Test
	public void testTremauxAlgorithm0() {
		testNavigation.tremauxAlgorithm(0, false);
	}

	@Test
	public void testTremauxAlgorithm1() {
		testNavigation.tremauxAlgorithm(1, false);
	}

	@Test
	public void testTremauxAlgorithm2() {
		testNavigation.tremauxAlgorithm(2, false);
	}

	@Test
	public void testTremauxAlgorithm3() {
		testNavigation.tremauxAlgorithm(3, false);
	}
}
