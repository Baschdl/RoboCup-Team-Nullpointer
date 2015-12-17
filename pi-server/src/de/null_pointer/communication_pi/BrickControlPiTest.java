package de.null_pointer.communication_pi;

import static org.junit.Assert.*;

import org.junit.Test;

public class BrickControlPiTest {

	BrickControlPi classUnderTest = new BrickControlPi();

	@Test
	public void testCheckStringCorrect() {
		String stringToTest = "*123;123;123;123#";
		float[] resultCorrect = { 123, 123, 123, 123 };
		float[] result = classUnderTest.checkString(stringToTest);

		assertArrayEquals(resultCorrect, result, 0);
	}
	
	@Test
	public void testCheckStringFalseOnlyHash() {
		String stringToTest = "123;123;123;123#";
		float[] result = classUnderTest.checkString(stringToTest);
		
		assertArrayEquals(null, result, 0);
	}
	
	@Test
	public void testCheckStringFalseOnlyStar() {
		String stringToTest = "*123;123;123;123";
		float[] result = classUnderTest.checkString(stringToTest);
		
		assertArrayEquals(null, result, 0);
	}
	
	@Test
	public void testCheckStringWithLetter() {
		String stringToTest = "*123ab;1ac23;12dc3;123hb#";
		float[] result = classUnderTest.checkString(stringToTest);

		assertArrayEquals(null, result, 0);
	}
	
	@Test
	public void testCheckStringFalseTwoStars() {
		String stringToTest = "**123;123;123;123#";
		float[] result = classUnderTest.checkString(stringToTest);
		
		assertArrayEquals(null, result, 0);
	}
	
	@Test
	public void testCheckStringFalseTwoHashes() {
		String stringToTest = "123;123;123;123##";
		float[] result = classUnderTest.checkString(stringToTest);
		
		assertArrayEquals(null, result, 0);
	}
	
	@Test
	public void testCheckStringFalseCommaAsSeparator() {
		String stringToTest = "*123,123,123,123#";
		float[] result = classUnderTest.checkString(stringToTest);
		
		assertArrayEquals(null, result, 0);
	}

}
