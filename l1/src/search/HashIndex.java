package search;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import util.CharHandler;

public class HashIndex {
	
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
		String line;
		String[] data;
		String word;
		int index;
		
		while(true) {
			line = Main.readLine(hashfile);

			if (line == null || line.length() == 0) {
				break;
			}
			data = line.split(" ");
			word = data[0];
			index = Integer.parseInt(data[1]);

			addToIndices(new CharHandler(word).getChars(), index);
			if (word.equals("ööö")) break;
		}
	}
	
	private void addToIndices(char[] key, int index) {
		this.indices[HashIndex.charArrayToIndex(key)] = index;
	}

	/**
	 * 'Hashes' a three character key into an index in the hashfile. Key hash to be 0 < length <= 3, 
	 * and can only contain characters a'z,ä,å,ö.
	 * @param key The chars to hash
	 * @return an integer index in the range [0 27000)
	 */
	public static int charArrayToIndex(char[] key) {
		int val = 0;
		
		for (int i = 0; i < 3; i++) {
			val += charToIndex(key[i]) * Math.pow(30, 2-i);
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
		return -1;
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
		if (Arrays.equals(new char[] {0, 0, 0}, c2.getChars())) {
			return new int[] {this.indices[i1], -1};
		}
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
