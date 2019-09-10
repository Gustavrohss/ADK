import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Generates low and high indices in wordfile in which to search for the user input word
 */
public class HashIndex {
	
	// Contains 3-character indices
	private int[] masterIndex;
	
	/**
	 * Generates all necessary data at instantiation time
	 * @param hashFile input hashfile
	 */
	public HashIndex(RandomAccessFile hashFile) {
		this.masterIndex = new int[30*30*30+30*30+30+1];
		this.buildIndex(hashFile);
	}

	/**
	 * Build the internal array of short hashes and index
	 * @param hashFile input hashfile
	 */
	private void buildIndex(RandomAccessFile hashFile) {
		try {
			// See parseLine documentation
			char[][] data;
			char[] chars;
			char[] index;
		
			while(true) {

				data = this.parseLine(hashFile);
				if (data == null) break;
				chars = data[0];
				index = data[1];
			
				int wordindex = 0;
				for (int i = 0; i < index.length; i++) {
					// Generate int from chars
					wordindex = wordindex << 8;
					wordindex += index[i];
				}
				this.saveToMasterIndex(chars, wordindex);
		
				if(new String(chars).equals("ööö")) break;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Reads a line from the hashfile and returns a char-array of length 2, 
	 * where the first index has the three character string of this hash, 
	 * and the second index has a four character representation of the int-index.
	 * The higher 8 bits in the index-characters are all zero, that is, it is a 
	 * two byte character representing each byte in the four byte int.
	 * If the end of file has been reached, return null.
	 * @param hashFile The RandomAccessFile object reading from the hashfile 
	 * @return a length two char[][]
	 * @throws IOException, if the file cannot be read.
	 */
	private char[][] parseLine(RandomAccessFile hashFile) throws IOException {
		char[] chars = new char[3];
		char[] index = new char[4];
		
		try {
			for(int i = 0; i < chars.length; i++) 
				chars[i] = hashFile.readChar();

			for(int i = 0; i < index.length; i++) 
				index[i] = (char) hashFile.readUnsignedByte();

		} catch(EOFException e) {
			return null;
		}
		
		return new char[][] {chars, index};
	}
	
	/**
	 * Saves a index to masterIndex, with its position calculated from key
	 * @param key
	 * @param index
	 */
	private void saveToMasterIndex(char[] key, int index) {
		this.masterIndex[charArrayToIndex(key)] = index;
	}

	/**
	 * 'Hashes' a three character key into an index in the hashfile. Key hash to be 0 < length <= 3, 
	 * and can only contain characters a-z, ä, å, ö.
	 * @param key The chars to hash
	 * @return an integer index in the range [0 27000)
	 * @throws IllegalArgumentException if an illegal character has been entered
	 */
	public int charArrayToIndex(char[] key) throws IllegalArgumentException{
		if (key.length != 3) {
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
	 * Returns an int-array of length 2, where the values gives the range in the wordfile where the 
	 * searched word should exist.
	 * @param searchWord the word to search for
	 * @return an array of length two, where the first index gives the lower bound, the second gives the upper buond.
	 */
	public int[] getRange(String searchWord) {
		char[] asArray = searchWord
		.toLowerCase()
		.substring(0, Math.min(searchWord.length(), 3))
		.toCharArray();
		char[] searchChars = new char[3];

		for (int i = 0; i < asArray.length; i++) {
            searchChars[i-(asArray.length-3)] = asArray[i];
        }
		return this.getRange(searchChars);
		
	}
	
	/**
	 * Returns an int-array of length 2, where the values gives the range in the wordfile where the 
	 * searched word should exist.
	 * @param searchChars First three letters of the word
	 * @return an array of length two, where the first index gives the lower bound, the second gives the upper buond.
	 */
	public int[] getRange(char[] searchChars) {
		
		CharHandler c1 = new CharHandler(searchChars);
		CharHandler c2 = c1.next();
		
		int i1 = charArrayToIndex(c1.getData());
		int i2 = charArrayToIndex(c2.getData());
		System.out.println(i1 + " " + i2);
		return new int[] {this.masterIndex[i1], this.masterIndex[i2]};
	}
	
	/**
	 * Get the int index associated with this char key.
	 * @param key The key to get
	 * @return the int index associated with this key.
	 */
	public int get(char[] key) {
		return this.masterIndex[charArrayToIndex(key)];
	}
	
	/**
	 * Calculates a char to an int-index with base 0. range a-z,ä,å,ö;
	 * @param c
	 * @return
	 * @throws IllegalArgumentException
	 */
	private int charToIndex(char c) throws IllegalArgumentException{
		if (c == 0) {
			return 0;
		}
		
		if ('a' <= c && c <= 'z') return c - 'a' + 1;
		if (c == 'ä') return 'z'-'a'+2;
		if (c == 'å') return 'z'-'a'+3;
		if (c == 'ö') return 'z'-'a'+4;
		
		throw new IllegalArgumentException(String.format("Illegal char '%s' given", c));
	}
}