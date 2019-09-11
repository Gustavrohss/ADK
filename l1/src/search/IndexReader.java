package search;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IndexReader implements Iterator<String>, Iterable<String> {
	
	private int lower;
	private int upper;
	private String inputword;
	
	List<Integer> indices;
	
	private RandomAccessFile indexfile;
	private RandomAccessFile dataFile;
	
	public IndexReader(int lower, int upper, String inputword,
	RandomAccessFile indexfile, RandomAccessFile korpus) throws IOException {
		
		this.lower = lower;
		this.upper = upper;
		this.inputword = inputword;
		this.indexfile = indexfile;
		this.dataFile = korpus;
		
	}
	
	/**
	 * Build the list of indices to read in korpus
	 * @throws IOException
	 */
	public void build() throws IOException {
		this.indices = new LinkedList<>();
		this.readIndices();
	}
	
	private String readIndex(int index) throws IOException {
		StringBuilder sb = new StringBuilder();
		dataFile.seek(Math.max(index - 30, 0));
		
		for (int i = 0; i < 60 + inputword.length(); i++) {
			try {
				sb.append((char) dataFile.read());
			} catch (EOFException e) {
				break;
			}
		}
		
		return sb.toString().replace("\n", " ");
		
	}
	
	private void readIndices() throws IOException {
		this.indexfile.seek(lower);
		
		int index = -1;
		while(true) {
			index = this.indexfile.readInt();
			if (index == Integer.MAX_VALUE) break;
			this.indices.add(index);
		}
	}

	public int size() {
		return this.indices.size();
	}


	@Override
	public boolean hasNext() {
		return !this.indices.isEmpty();
	}


	@Override
	public String next() {
		try {
			String s = this.readIndex(this.indices.get(0));
			this.indices.remove(0);
			return s;
		} catch (IOException e) {
			return null;
		}
	}


	@Override
	public Iterator<String> iterator() {
		return this;
	}
}
