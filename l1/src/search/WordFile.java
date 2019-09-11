package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WordFile {
	
	private static RandomAccessFile file;
	
	
	private String word;
	private int ubound;
	private int lbound;
	
	
	public WordFile(String word, int lbound, int ubound) throws FileNotFoundException {
		
		if (file == null) {
			file = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/files/wordfile"), "r");
		}
		
		this.word = word.toLowerCase();
		this.lbound = lbound;
		this.ubound = ubound;
		
	}
	
	/**
	 * Get the range of the indexfile between where index for this word exists
	 * @return
	 */
	public int[] getIndexRange() {
		
		try {
			return this.binSearch(lbound, ubound);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public int[] binSearch(int lower, int upper) throws IOException {
		
		int mid = (lower + upper)/2;
		if(mid %2 != 0) {
			mid -=1;
		}
		if(mid == lower || mid == upper) {
			return null;
		}
		
		file.seek(mid);
		this.seekLineStart();
		
		String data = this.readline();
		String word = this.getWordFromData(data);
		
		
		// word has been found, return its index
		if(word.equals(this.word)) {
			
			System.out.println(word);
			
			int startindex = this.getIndexFromData(data);
			// read next line to get index file interval
			data = this.readline();
			int endindex = this.getIndexFromData(data);
			return new int[] {startindex, endindex};
		}
		
		if(this.word.compareTo(word) < 0) {
			return this.binSearch(lower, mid);
		}
		else {
			return this.binSearch(mid, upper);
		}
		
	}
	
	/**
	 * Reads the current line of data, until end of line mask is detected. Assumes that the filepointer is currently 
	 * at the start of the line.
	 * @return The String data of this line
	 * @throws IOException
	 */
	private String readline() throws IOException {
		
		StringBuilder sb = new StringBuilder();
		char[] c = new char[4];
		
		while(!this.applyMask(c)) {
			c = this.shiftLeft(c);
			c[c.length-1] = file.readChar();
			sb.append(c[c.length-1]);
		}
		
		return sb.toString();
		
	}
	
	private String getWordFromData(String data) {
		
		String[] array = data.split(" ", 2);
		String word = array[0];
		return word;
		
	}
	
	private int getIndexFromData(String data) {
		
		String[] array = data.split(" ", 2);
		char[] index = array[1].trim().toCharArray();
		
		int wordindex = 0;
		for(int i=0; i<index.length; i++) {
			wordindex = wordindex << 16;
			wordindex += index[i];
		}
		return wordindex;
		
	}
	
	/**
	 * Reads the current line backwards character by character, until the previous end of line has been reached,
	 * and then advances the file pointer to the start of the line.
	 * @throws IOException If the file cannot be read
	 */
	private void seekLineStart() throws IOException {
		
		char[] c = new char[4];
		
		while(!this.applyMask(c)) {
			if(file.getFilePointer() <= 4) {
				file.seek(0);
				return;
			}
			file.seek(file.getFilePointer()-4);
			c = this.shiftRight(c);
			c[0] = file.readChar();

		}
		
		file.seek(file.getFilePointer()+2*3);
		
	}
	
	
	
	private boolean applyMask(char[] data) {
		
		if(data.length != 4) {
			throw new IllegalArgumentException("char[] data must be length 4, but received length " + String.valueOf(data.length));
		}
		
		
		if(data[0] != ' ') {
			return false;
		}
		if(data[3] != '\n') {
			return false;
		}
		
		return true;
		
	}
	
	private char[] shiftLeft(char[] c) {
		
		for(int i=0; i<c.length-1; i++) {
			c[i] = c[i+1];
		}
		c[c.length-1] = 0;
		return c;
		
	}
	
	private char[] shiftRight(char[] c) {
		
		for(int i=c.length-1; i>0; i--) {
			c[i] = c[i-1];
		}
		c[0] = 0;
		return c;
	}
	

}
