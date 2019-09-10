import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Finds the position in indexfile where a certain word has its korpus-indices
 */
public class WordFileProcessor {
	
	private RandomAccessFile wordFile;
	private String searchWord;
	
	public WordFileProcessor(String word, RandomAccessFile wordFile) {
		this.wordFile = wordFile;
		this.searchWord = word.toLowerCase();
	}
	
	public int binSearch(int lower, int upper) throws IOException {
		int mid = (lower + upper)/2;
		if (mid % 2 != 0) mid--;
		
		wordFile.seek(mid);
		this.seekNext();

		String[] array = this.readNext().split(" ", 2);
		String word = array[0];
		char[] index = array[1].trim().toCharArray();
		
		int wordindex = 0;
		for (int i = 0; i < index.length; i++) {
			wordindex = wordindex << 16;
			wordindex += index[i];
		}
		
		System.out.println(lower + " " + mid + " " + upper);
		try {Thread.sleep(10);}catch(Exception e){}
		// word has been found, return its index
		// System.out.println(lower + " " + upper);
		if (word.equals(this.searchWord)) {
			return wordindex;
		} else if (this.searchWord.compareTo(word) < 0) {
			return this.binSearch(lower, mid);
		} else {
			return this.binSearch(mid, upper);
		}
	}

	private void seekNext() throws IOException {
		char[] c = new char[4];
		
		while (!this.applyMask(c)) {
			c = this.shiftLeft(c);
			c[c.length-1] = wordFile.readChar();
		}
	}

	private String readNext() throws IOException {
		StringBuilder sb = new StringBuilder();
		char[] c = new char[4];
		
		while (!this.applyMask(c)) {
			c = this.shiftLeft(c);
			c[c.length-1] = wordFile.readChar();
			sb.append(c[c.length-1]);
		}
		return sb.toString();
	}
	
	private boolean applyMask(char[] data) {
		if (data.length != 4) {
			throw new IllegalArgumentException(
				"char[] data must be length 4, but received length "
				+ String.valueOf(data.length));
		}
		return data[0] == ' ' && data[3] == '\n';
	}
	
	private char[] shiftLeft(char[] c) {
		for (int i = 0; i < c.length-1; i++) c[i] = c[i+1];
		c[c.length-1] = 0;
		return c;
	}
}