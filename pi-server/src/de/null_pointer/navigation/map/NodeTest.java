package de.null_pointer.navigation.map;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {
	private Node nodeToTest = new Node(0, 0);

	@Test
	public void testInvertOrientation0() {
		assertEquals(2, nodeToTest.invertOrientation(0), 0);
	}

	@Test
	public void testInvertOrientation1() {
		assertEquals(3, nodeToTest.invertOrientation(1), 0);
	}

	@Test
	public void testInvertOrientation2() {
		assertEquals(0, nodeToTest.invertOrientation(2), 0);
	}

	@Test
	public void testInvertOrientation3() {
		assertEquals(1, nodeToTest.invertOrientation(3), 0);
	}
}
