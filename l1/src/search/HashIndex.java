package search;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import util.CharHandler;

public class HashIndex {
	
	
	public static void main(String[] args) {
		
		try {
			HashIndex i = new HashIndex();
			int[] index = i.getRange("a");
			System.out.println(index[1] - index[0]);
			System.out.println(Arrays.toString(index));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private int[] index;
	
	
	public HashIndex() throws IOException {
		
		RandomAccessFile reader = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/files/hashfile"), "r");
		this.index = new int[30*30*30+30*30+30+1];
		this.buildIndex(reader);
		reader.close();
		
	}
	
	/**
	 * Returns an int-array of length 2, where the values gives the range in the wordfile where the 
	 * searched word should exist.
	 * @param s the word to search for
	 * @return an array of length two, where the first index gives the lower bound, the second gives the upper buond.
	 */
	public int[] getRange(String s) {
		
		char[] c = s.toLowerCase().substring(0, Math.min(s.length(), 3)).toCharArray();
		char[] arr = new char[3];
		for (int i = 0; i < c.length; i++) {
            arr[i-(c.length-3)] = c[i];
        }
		
//		printStr(arr);
		
		return this.getRange(arr);
		
	}
	
	/**
	 * Returns an int-array of length 2, where the values gives the range in the wordfile where the 
	 * searched word should exist.
	 * @param c First three letters of the word
	 * @return an array of length two, where the first index gives the lower bound, the second gives the upper buond.
	 */
	public int[] getRange(char[] c) {
		
		CharHandler c1 = new CharHandler(c);
		CharHandler c2 = c1.next();
		
		int i1 = HashIndex.charArrayToIndex(c1.getChars());
		int i2 = HashIndex.charArrayToIndex(c2.getChars());
		
		return new int[] {this.index[i1], this.index[i2]};
		
	}
	
	/**
	 * Build the internal array of short hashes and index
	 * @param reader 
	 * @throws IOException
	 */
	private void buildIndex(RandomAccessFile reader) throws IOException {
		
		char[][] data;
		char[] chars;
		char[] index;
		
//		int iii=0; 
		
		while(true) {
//			iii++;
			data = this.readline(reader);
			if(data == null) {
				break;
			}
			chars = data[0];
			index = data[1];
			
			int wordindex = 0;
			for(int i=0; i<index.length; i++) {
				wordindex = wordindex << 8;
				wordindex += index[i];
			}

			this.put(chars, wordindex);
			
			if(new String(chars).equals("ööö")) {
				System.out.println("overflow");
				break;
			}
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param index
	 */
	private void put(char[] key, int index) {
		
		this.index[HashIndex.charArrayToIndex(key)] = index;
		
	}
	
	/**
	 * Get the int index associated with this char key.
	 * @param key The key to get
	 * @return the int index associated with this key.
	 */
	public int get(char[] key) {
		
		return this.index[HashIndex.charArrayToIndex(key)];
		
	}
	
	/**
	 * 'Hashes' a three character key into an index in the hashfile. Key hash to be 0 < length <= 3, 
	 * and can only contain characters a'z,ä,å,ö.
	 * @param key The chars to hash
	 * @return an integer index in the range [0 27000)
	 * @throws IllegalArgumentException if an illegal character has been entered
	 */
	public static int charArrayToIndex(char[] key) throws IllegalArgumentException{
		
		if(key.length != 3) {
			throw new IllegalArgumentException("Char key must be of length 3, but received "
					+ "key of length " + String.valueOf(key.length));
			
		}
		
		int val = 0;
		
		for (int i=0; i<3; i++) {
			try {
				val += charToIndex(key[i]) * Math.pow(30, 2-i);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage() + " in " + Arrays.toString(key));
			}
		}
		
		return val;
		
	}
	
	
	/**
	 * Calculates a char to an int-index with base 0. range a-z,ä,å,ö;
	 * @param c
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static int charToIndex(char c) throws IllegalArgumentException{
		
		if(c==0) {
			return 0;
		}
		
		if('a' <= c && c <= 'z') {
			return c - 'a'+1;
		}
		if(c == 'ä') {
			return 'z'-'a'+2;
		}
		if(c == 'å') {
			return 'z'-'a'+3;
		}
		if(c == 'ö') {
			return 'z'-'a'+4;
		}
		
		throw new IllegalArgumentException(String.format("Illegal char '%s' given", c));
		
	}
	
	
	/**
	 * Reads a line from the hashfile and returns a char-array of length 2, 
	 * where the first index has the three character string of this hash, 
	 * and the second index has a four character representation of the int-index.
	 * The higher 8 bits in the index-characters are all zero, that is, it is a 
	 * two byte character representing each byte in the four byte int.
	 * If the end of file has been reached, return null.
	 * @param reader The Bufferedreader object reading from the hashfile 
	 * @return a length two char[][]
	 * @throws IOException, if the file cannot be read.
	 */
	private char[][] readline(RandomAccessFile reader) throws IOException {
		
		char[] chars = new char[3];
		char[] index = new char[4];
		
		try {
			for(int i=0; i<chars.length; i++) {
				chars[i] = reader.readChar();
			}
			for(int i=0; i<index.length; i++) {
				index[i] = (char) reader.readUnsignedByte();
			}
		} catch(EOFException e) {
			System.out.println("EOF");
			return null;
		}
		
		return new char[][] {chars, index};
		
	}
	
	
	private static void printStr(char[] c) {
		
		System.out.print("[");
		for(int i=0; i<c.length; i++) {
			if(c[i] == 0) {
				System.out.print("\\0");
			}
			else if(c[i] == '\n') {
				System.out.print("\\n");
			}
			else {
				System.out.print(c[i]);
			}
			System.out.print(",");
		}
		System.out.println("]");
		
	}
	
	private static void printInt(char[] c) {
		
		System.out.print("[");
		for(int i=0; i<c.length; i++) {
			System.out.print(Integer.toString((int) c[i]));
			System.out.print(",");
		}
		System.out.println("]");
		
	}

}
