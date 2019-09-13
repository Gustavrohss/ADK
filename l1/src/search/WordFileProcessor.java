package search;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WordFileProcessor {
	
	private RandomAccessFile wordfile;
	private String word;
	
	public WordFileProcessor(String word, RandomAccessFile wordfile) {
		this.wordfile = wordfile;
		this.word = word.toLowerCase();
	}
	
	public int[] binSearch(int lower, int upper) throws IOException {
		
		if (upper == -1) upper = (int) this.wordfile.length();
				
		int mid = (lower + upper)/2;
		if (mid % 2 != 0) {
			mid--;
		}
		if (mid == lower || mid == upper) {
			return null;
		}
		
		wordfile.seek(mid);
		this.seekLineStart();
		
		String data = Main.readLine(wordfile);
		//System.out.println(data);
		String readWord = this.getWordFromData(data);
		
		// word has been found, return its index
		if (readWord.equals(this.word)) {
			
			//System.out.println(readWord);
			//System.out.println(this.word);
			
			int startindex = this.getIndexFromData(data);
			// read next line to get index file interval
			data = Main.readLine(wordfile);
			int endindex = this.getIndexFromData(data);
			return new int[] {startindex, endindex};
		}
		
		if (this.word.compareTo(readWord) < 0) {
			return this.binSearch(lower, mid);
		}
		else {
			return this.binSearch(mid, upper);
		}
		
	}

	private boolean applyMask(char[] chars) {
		if (chars.length != 4) {
			throw new IllegalArgumentException("char[] data must be length 4, but received length " + String.valueOf(chars.length));
		}
		if (chars[0] != ' ') {
			return false;
		}
		if (chars[3] != '\n') {
			return false;
		}
		
		return true;
	}
	
	private char[] shiftLeft(char[] chars) {
		for(int i = 0; i < chars.length-1; i++) {
			chars[i] = chars[i+1];
		}
		chars[chars.length-1] = 0;
		return chars;
	}
	
	private char[] shiftRight(char[] chars) {
		for(int i = chars.length - 1; i > 0; i--) {
			chars[i] = chars[i-1];
		}
		chars[0] = 0;
		return chars;
	}
	
	private String getWordFromData(String data) {
		// Word is kept in the first half of data.
		return data.split(" ", 2)[0];
	}
	
	private int getIndexFromData(String data) {
		try {
		return Integer.parseInt(data.split(" ", 2)[1]);
		} catch (Exception e) {return 0;}
	}
	
	/**
	 * Reads the current line backwards character by character, until the previous end of line has been reached,
	 * and then advances the file pointer to the start of the line.
	 * @throws IOException If the file cannot be read
	 */
	private void seekLineStart() throws IOException {
		char c = 0;

		while (c != '\n') {
			if (wordfile.getFilePointer() - 4 <= 0) {
				wordfile.seek(0);
				return;
			} else {
				wordfile.seek(wordfile.getFilePointer() - 4);
				c = wordfile.readChar();
			}
		}
	}
}
