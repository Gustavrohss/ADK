package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IndexReader implements Iterator<String>, Iterable<String> {
	
	private int lower;
	private int upper;
	
	
	List<Integer> indices;
	
	private RandomAccessFile indexFile;
	private RandomAccessFile dataFile;
	
	
	public IndexReader(int lower, int upper) throws IOException {
		
		this.lower = lower;
		this.upper = upper;
		
		this.indexFile = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/files/indexfile"), "r");
		this.dataFile = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/korpus"), "r");
		
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
		dataFile.seek(index-15*2);
		
		for(int i=0; i<60; i++) {
			sb.append((char) dataFile.read());
		}
		
		return sb.toString().replace("\n", " ");
		
	}
	
	
	
	private void readIndices() throws IOException {
		
		this.indexFile.seek(lower);
		
		while(this.indexFile.getFilePointer() < this.upper) {
			indices.add(this.indexFile.readInt());
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
