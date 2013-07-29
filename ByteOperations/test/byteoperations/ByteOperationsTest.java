/**************************************************************************************************
 * ByteOperationsTest.java
 * This file was developed by Joshua Dickson for Autoliv Inc. 
 * Duplication strictly prohibited. All rights reserved. 
 *************************************************************************************************/
package byteoperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * jUnit test cases for the ByteOperations class
 * @author Joshua Dickson
 * @version March 4, 2013	Initial build
 * @version May 13, 2013	Addition
 * @version July 20, 2013
 * 			Added a test that checked for bug when processing an array of Lidar data
 * 			that produced inaccurate return values for finding the index of an array
 * 			of bytes in another array of bytes
 */
public class ByteOperationsTest {

	/**
	 * Test the creation of bytes from hexidecimal strings and convert back to strings
	 */
	@Test
	public void createByteFromStringAndCopyBack() {
		
		String[] testList = { "00", "22", "34", "89", "AA", "D5", "FF" };
		
		for(int i = 0; i < testList.length; i++) {
			assertTrue(ByteOperations.convertByteToHexString(
					ByteOperations.convertHexStringToByte(testList[i])).equalsIgnoreCase(
							testList[i]));
		}
	}
	
	/**
	 * Test the creation of arrays of bytes from longer hexidecimal strings and convert back
	 */
	@Test
	public void createByteArrayFromLongString() {
		String[] hexStringArray = { "AAEEFF55", "00FF00FF00EE34", "AA44", "DD67235F" };
		for(int i = 0; i < hexStringArray.length; i++) {
			assertTrue(ByteOperations.convertByteArrayToHexString(
					ByteOperations.convertHexStringToByteArray(hexStringArray[i]))
					.equalsIgnoreCase(hexStringArray[i]));
		}
	}
	
	/**
	 * Test reversing the endianness of a String
	 */
	@Test
	public void testEndiannessReverse() {
		String beforeReverse = "5511551122CC";
		String afterReverse  = "11551155CC22";
				
		assertEquals(ByteOperations.reverseEndianness(beforeReverse), afterReverse);
		assertEquals(ByteOperations.reverseEndianness(afterReverse), beforeReverse);
		
		assertEquals(ByteOperations.reverseEndianness("1134923"), null);
	}
	
	/**
	 * Test creation of a sub array from an array of bytes
	 */
	@Test
	public void testMakeSubArray() {
		String[] testList = { "00", "22", "34", "89", "AA", "D5", "FF" };
		byte[] testListAsBytes = new byte[testList.length];
		
		for(int i = 0; i < testList.length; i++) {
			testListAsBytes[i] = ByteOperations.convertHexStringToByte(testList[i]);
		}
		
		String[] testSubList = { "89", "AA", "D5" };
		byte[] testSubListAsBytes = new byte[testSubList.length];
		
		for(int i = 0; i < testSubList.length; i++) {
			testSubListAsBytes[i] = ByteOperations.convertHexStringToByte(testSubList[i]);
		}
		
		byte[] subByteList = ByteOperations.subArray(testListAsBytes, 3, 3);
		
		for(int i = 0; i < subByteList.length; i++) {
			subByteList[i] = testSubListAsBytes[i];
		}
	}
	
	/**
	 * Test conversion to and from ASCII strings from byte arrays
	 */
	@Test
	public void convertASCIIStringToBytesAndBack() {
		String canMessage = "$CAN;S;D;115;8;40;32;0;50;0;128;176;114;#";
		String asciiMessage = ByteOperations.convertByteArrayToASCIIString(
				ByteOperations.convertASCIIStringToByteArray(
						"$CAN;S;D;115;8;40;32;0;50;0;128;176;114;#"));
		assertTrue(canMessage.equals(asciiMessage));
	}

	
	/**
	 * Test checking for the given sequence of bytes at the beginning of a given list of bytes
	 */
	@Test
	public void testGetByteIndexOfByteListInByteList() {
		byte[] delimeter = ByteOperations.convertASCIIStringToByteArray("$$");
		byte[] badDelimeter = ByteOperations.convertASCIIStringToByteArray("EE");
		byte[] searchArray = ByteOperations.convertASCIIStringToByteArray("$$2345$$123");
		
		ArrayList<Byte> searchArrayList = new ArrayList<Byte>(0);
		ArrayList<Byte> delimeterArrayList = new ArrayList<Byte>(0);
		ArrayList<Byte> badDelimeterArrayList = new ArrayList<Byte>(0);
		
		for(byte b : searchArray) {
			searchArrayList.add(b);
		}
		for(byte b : delimeter) {
			delimeterArrayList.add(b);
		}
		for(byte b : badDelimeter) {
			badDelimeterArrayList.add(b);
		}
		
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 0), 0);
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 2), 6);
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, badDelimeterArrayList, 0), -1);
		
	}
	
	@Test
	public void testFindByteSubListInByteList() {
		byte[] delimeter = ByteOperations.convertASCIIStringToByteArray("AAAA5555DDDD");
		byte[] searchArray = ByteOperations.convertASCIIStringToByteArray("AAAA5555DDDD00C605B4");
		
		ArrayList<Byte> searchArrayList = new ArrayList<Byte>(0);
		ArrayList<Byte> delimeterArrayList = new ArrayList<Byte>(0);
		
		for(byte b : searchArray) {
			searchArrayList.add(b);
		}
		for(byte b : delimeter) {
			delimeterArrayList.add(b);
		}
		
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 0), 0);
		
	}
	
	@Test
	public void testFindLidarSublistInFindIndexOfListInSubList() {
		byte[] searchArray = ByteOperations.convertHexStringToByteArray("FFEEF800A50110C"
				+ "3031FB1011456042BBF0113FE043DC5011A980536D30112000001CD0117E80518D601"
				+ "12000001DB0112E00709F20114CD072712021197073730021681072137020E0000066"
				+ "C024C0000AABA02196E0B06E7021C0000A2000324CC0B0F");
		byte[] delimeter = ByteOperations.convertHexStringToByteArray("FFEE");
		
		// Build List of Bytes
		List<Byte> searchArrayList = new ArrayList<Byte>(0);
		List<Byte> delimeterArrayList = new ArrayList<Byte>(0);
		
		// Load
		for(byte b : searchArray) {
			searchArrayList.add(b);
		}
		for(byte b : delimeter) {
			delimeterArrayList.add(b);
		}
		
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 0), 0);
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 10), -1);

	}
	
	@Test
	public void testBackAndFourthASCIIConversion() {
		String sampleString = "$CAN;S;D;382;8;22;22;00;00;0;128;176;47;#LF";
		assertEquals(ByteOperations.convertByteArrayToASCIIString(ByteOperations
				.convertASCIIStringToByteArray(sampleString)), sampleString);
	}
	
	@Test
	public void testLongDelimeterVsShortBuffer() {
		byte[] searchArray = ByteOperations.convertHexStringToByteArray("AA");
		byte[] delimeter = ByteOperations.convertHexStringToByteArray("FFEE");
		
		// Build List of Bytes
		List<Byte> searchArrayList = new ArrayList<Byte>(0);
		List<Byte> delimeterArrayList = new ArrayList<Byte>(0);
		
		// Load
		for(byte b : searchArray) {
			searchArrayList.add(b);
		}
		for(byte b : delimeter) {
			delimeterArrayList.add(b);
		}
		
		assertEquals(ByteOperations.getIndexOfByteListInByteList(searchArrayList, delimeterArrayList, 0), -1);
	}
	
}
