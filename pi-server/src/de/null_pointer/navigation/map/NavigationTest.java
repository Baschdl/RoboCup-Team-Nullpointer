package de.null_pointer.navigation.map;

import static org.junit.Assert.*;

import org.junit.Test;

public class NavigationTest {
	private Navigation testNavigation = new Navigation(7, 7);

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
