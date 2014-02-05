/**
 * ByteOperations.java
 * 
 * Copyright 2013 Joshua Dickson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package byteoperations;

import java.util.List;

/**
 * A class that provides byte operations for working with bytes, byte arrays, and hexidecimal 
 * strings
 * @author Joshua Dickson
 * @version March 4, 2013
 * @version July 20, 2013
 * 				Fixed a bug in the getIndexOfByteListInByteList function that produced false 
 * 				returns when working with delimeter byte arrays greater than or equal to two 
 * 				bytes in length
 * @version July 29, 2013
 * 				Modification to the logic for ASCII conversions to rely on the native specification.
 * 				Minor other refactoring.
 * @version September 23, 2013
 * 			 - Updated licensing information
 */
public class ByteOperations {
	
	/**
	 * Private constructor
	 * @throws Exception 
	 */
	private ByteOperations() throws Exception {
		throw new IllegalStateException();
	}
	
	/**
	 * Create a hexidecimal string of length 2*n, where n is the length of a given array of bytes
	 * @param byteArray the array of bytes to convert
	 * @return the hexidecimal string
	 */
	public static String convertByteArrayToHexString(byte[] byteArray){
        String finalHexString = "";
        for(byte b : byteArray) {
        	finalHexString = finalHexString.concat(convertByteToHexString(b));
        }
        return finalHexString.toUpperCase();
    }

	/**
	 * Create a hexidecimal string of length two representing a single byte.
	 * @param aByte the byte to convert
	 * @return the hexidecimal string representation
	 */
	public static String convertByteToHexString(byte aByte){
        int i = aByte & 0xFF;
        if (i < 16) {
        	return "0".concat(Integer.toHexString(i)).toUpperCase();
        } 
        return Integer.toHexString(i).toUpperCase();
    }
	
	/**
	 * Create a signed byte from a hexidecimal string
	 * @param hexString the string to convert
	 * @return the byte representation of the given string
	 */
	public static byte convertHexStringToByte(String hexString) {
		return new Integer(Integer.parseInt(hexString, 16)).byteValue();
	}
	
	
	/**
	 * Create an array of bytes from a long hexidecimal string
	 * @param hexString the string to convert
	 * @return the array of bytes representing the string
	 */
	public static byte[] convertHexStringToByteArray(String hexString) {
		int arraySize = hexString.length() / 2;
		byte[] byteArray = new byte[arraySize];
		int counter = 0;
		for(int i = 0; i <= hexString.length() - 2; i += 2) {
			byteArray[counter] = convertHexStringToByte(hexString.substring(i, i + 2));
			counter++;
		}
		return byteArray;
	}
	
	/**
	 * Return a subarray of length two of an array
	 * @param fullArray the full array
	 * @param start the index to begin copying
	 * @param subArraySize the number of bytes to copy
	 * @return the subarray of bytes
	 */
	public static byte[] subArray(byte[] fullArray, int start, int subArraySize) {
		byte[] byteArray = new byte[subArraySize];
		for(int i = 0; i < subArraySize; i++) {
			byteArray[i] = fullArray[start + i];
		}
		return byteArray;
	}
	
	/**
	 * Reverse the endianness of a given String representation of bytes
	 * @param input the String representation of an array of bytes on which to reverse 
	 * the endianness, divisible by 4
	 * @return the String byte representation with endianness reversed
	 */
	public static String reverseEndianness(String input) {
		if(input == null || input.length() % 4 != 0 || input.length() < 1) {
			return null;
		}
		String reverseEndiannessString = "";
		for(int i = 0; i < input.length(); i += 4) {			
			reverseEndiannessString = reverseEndiannessString.concat(input.substring(i+2, i+4));
			reverseEndiannessString = reverseEndiannessString.concat(input.substring(i, i+2));
		}
		return reverseEndiannessString;
	}
	
	/**
	 * Convert a String of ASCII data into its byte array representation
	 * @param asciiString the string to convert
	 * @return the array of byte indicies for the ASCII values
	 */
	public static byte[] convertASCIIStringToByteArray(String asciiString) {	
		try {
			return asciiString.getBytes("US-ASCII");
		} catch(Exception ex) {
			return null;
		}
	}

	/**
	 * Convert a byte array to a string representation of its ASCII value
	 * @param byteArray the byte representation of the ASCII string
	 * @return the ASCII string
	 */
	public static String convertByteArrayToASCIIString(byte[] byteArray) {
		try {
			String asciiString = new String(byteArray, "US-ASCII");
			return asciiString;
		} catch(Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * Get the index at which the bytes in the data buffer match those in the delimeter
	 * @param dataBuffer the List of Bytes to search
	 * @param delimiter the Byte List to find in the data buffer
	 * @param offset the position in the data buffer at which to begin searching
	 * @return the offset of the index at which the match begins, or -1 if the match is not found
	 */
	public static int getIndexOfByteListInByteList(List<Byte> dataBuffer, List<Byte> delimiter, 
			int offset) {
		if(dataBuffer.size() < delimiter.size() || dataBuffer.size() < 1) {
			return -1;
		}
		for(int dataBufferIndex = offset; dataBufferIndex < (dataBuffer.size() - delimiter.size()); 
				dataBufferIndex++) {
			for(int delimeterIndex = 0; delimeterIndex < delimiter.size(); delimeterIndex++) {
				if(!dataBuffer.get(dataBufferIndex + delimeterIndex).equals(
						delimiter.get(delimeterIndex))) {
					break;
				} else if (delimeterIndex == delimiter.size() - 1) {
					return dataBufferIndex;
				}
			}	
		}
		return -1;
	}

}
