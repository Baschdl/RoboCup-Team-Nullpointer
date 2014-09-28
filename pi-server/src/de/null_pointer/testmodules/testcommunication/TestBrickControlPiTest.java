package de.null_pointer.testmodules.testcommunication;

import static org.junit.Assert.*;

import org.junit.Test;

import de.null_pointer.testmodules.virtualhardware.VirtualMotor;

public class TestBrickControlPiTest {

	TestBrickControlPi classToTest = new TestBrickControlPi();
	
	@Test
	public void testResolveMotorporta() {
		VirtualMotor a = classToTest.resolveMotorport('a');
		assertEquals(classToTest.getMotorA(),a);
	}
	
	@Test
	public void testResolveMotorportA() {
		VirtualMotor a = classToTest.resolveMotorport('A');
		assertEquals(classToTest.getMotorA(),a);
	}
	
	@Test
	public void testResolveMotorportb() {
		VirtualMotor b = classToTest.resolveMotorport('b');
		assertEquals(classToTest.getMotorB(),b);
	}
	
	@Test
	public void testResolveMotorportB() {
		VirtualMotor b = classToTest.resolveMotorport('B');
		assertEquals(classToTest.getMotorB(),b);
	}
	
	@Test
	public void testResolveMotorportc() {
		VirtualMotor c = classToTest.resolveMotorport('c');
		assertEquals(classToTest.getMotorC(),c);
	}
	
	@Test
	public void testResolveMotorportC() {
		VirtualMotor c = classToTest.resolveMotorport('C');
		assertEquals(classToTest.getMotorC(),c);
	}
	
	@Test
	public void testResolveMotorportFalsed() {
		VirtualMotor a = classToTest.resolveMotorport('d');
		assertEquals(null,a);
	}

}
