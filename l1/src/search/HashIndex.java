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
			HashIndex i = new HashIndex(new RandomAccessFile(new File("files/hashfile"), "r"));
			int[] index = i.getRange("a");
			System.out.println(index[1] - index[0]);
			System.out.println(Arrays.toString(index));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int[] indices;
	
	public HashIndex(RandomAccessFile hashfile) throws IOException {
		this.indices = new int[30*30*30+30*30+30+1];
		this.buildIndices(hashfile);
	}
	
	/**
	 * Build the internal array of short hashes and index
	 * @param hashfile 
	 * @throws IOException
	 */
	private void buildIndices(RandomAccessFile hashfile) throws IOException {
		char[][] data;
		char[] chars;
		char[] index;
		
		while(true) {
			data = this.readline(hashfile);
			if(data == null) {
				break;
			}
			chars = data[0];
			index = data[1];
			
			int wordindex = 0;
			for(int i = 0; i < index.length; i++) {
				wordindex = wordindex << 8;
				wordindex += index[i];
			}

			this.addToIndices(chars, wordindex);
			
			if(new String(chars).equals("ööö")) {
				break;
			}
		}
		
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
			for (int i = 0; i < chars.length; i++) {
				chars[i] = reader.readChar();
			}
			for (int i = 0; i < index.length; i++) {
				index[i] = (char) reader.readUnsignedByte();
			}
		} catch (EOFException e) {
			System.out.println("Error in search.HashIndex.readline(RandomAccessFile)");
			return null;
		}
		
		return new char[][] {chars, index};
		
	}
	
	private void addToIndices(char[] key, int index) {
		this.indices[HashIndex.charArrayToIndex(key)] = index;
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
		
		for (int i = 0; i < 3; i++) {
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
		
		if(c == 0) {
			return 0;
		}
		 else if ('a' <= c && c <= 'z') {
			return c - 'a'+1;
		}
		else if (c == 'ä') {
			return 'z'-'a'+2;
		}
		else if (c == 'å') {
			return 'z'-'a'+3;
		}
		else if (c == 'ö') {
			return 'z'-'a'+4;
		}
		
		throw new IllegalArgumentException(String.format("Illegal char '%s' given", c));
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
		
		return new int[] {this.indices[i1], this.indices[i2]};
		
	}
	
	/**
	 * Get the int index associated with this char key.
	 * @param key The key to get
	 * @return the int index associated with this key.
	 */
	public int get(char[] key) {
		return this.indices[HashIndex.charArrayToIndex(key)];
	}
	
	
	
	
	
}
