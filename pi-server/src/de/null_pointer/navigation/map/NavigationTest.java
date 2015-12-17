package de.null_pointer.navigation.map;

import org.junit.Test;

import static org.junit.Assert.*;

public class NavigationTest {

	private Navigation testNavigation;

	public NavigationTest() {
		testNavigation = new Navigation(9, 9);
	}

	// @Test
	// public void testTremauxAlgorithm0() {
	// testNavigation.tremauxAlgorithm(0, false);
	// }
	//
	// @Test
	// public void testTremauxAlgorithm1() {
	// testNavigation.tremauxAlgorithm(1, false);
	// }
	//
	// @Test
	// public void testTremauxAlgorithm2() {
	// testNavigation.tremauxAlgorithm(2, false);
	// }
	//
	// @Test
	// public void testTremauxAlgorithm3() {
	// testNavigation.tremauxAlgorithm(3, false);
	// }

	@Test
	public void testRightDirection0() {
		assertEquals(1, testNavigation.rightleftDirection(0, true));
	}

	@Test
	public void testRightDirection1() {
		assertEquals(2, testNavigation.rightleftDirection(1, true));
	}

	@Test
	public void testRightDirection2() {
		assertEquals(3, testNavigation.rightleftDirection(2, true));
	}

	@Test
	public void testRightDirection3() {
		assertEquals(0, testNavigation.rightleftDirection(3, true));
	}

	@Test
	public void testLeftDirection0() {
		assertEquals(3, testNavigation.rightleftDirection(0, false));
	}

	@Test
	public void testLeftDirection1() {
		assertEquals(0, testNavigation.rightleftDirection(1, false));
	}

	@Test
	public void testLeftDirection2() {
		assertEquals(1, testNavigation.rightleftDirection(2, false));
	}

	@Test
	public void testLeftDirection3() {
		assertEquals(2, testNavigation.rightleftDirection(3, false));
	}

	@Test
	public void testRightmostDirection() {
		for (int i = -1; i < 3; i++) {
			for (int j = -1; j < 3; j++) {
				for (int k = -1; k < 3; k++) {
					for (int l = -1; l < 3; l++) {
						int[] tremaux = { i, j, k, l };
						System.out.println("tremaux: "
								+ tremaux[0]
								+ tremaux[1]
								+ tremaux[2]
								+ tremaux[3]
								+ " | erg: "
								+ testNavigation.rightmostDirection(0, tremaux,
										0));
					}
				}
			}
		}
		// assertEquals(2, testNavigation.rightmostDirection(orientation,
		// tremaux, value));
	}
}
