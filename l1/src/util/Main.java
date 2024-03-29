package util;

import java.io.*;
import java.util.*;

public class Main {

	public final static String charset = "ISO-8859-1";
	public final static String filePath = "files/";

	/**
	 * Files, filereaders.
	 * stk = Sorted Tokenized Korpus
	 */
	BufferedReader stk = null;
	RandomAccessFile hashfile = null;
	RandomAccessFile wordfile = null;
	RandomAccessFile indexfile = null;

	// The last (wrapped) 3-character combo to be checked
	CharHandler lastShorthand = new CharHandler(new char[3]);

	private int wordFilePointer = 0;
	private int indexFilePointer = 0;

	// Current word being worked with
	private String currentWord = null;

	public static void main(String[] args) {
		Main main = new Main();
		main.init();
		main.run();
		main.end();
	}

	private void init() {
		try {
			stk = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + "STK.txt"), charset));
			hashfile = new RandomAccessFile(new File(filePath + "hashfile"), "rw");
			wordfile = new RandomAccessFile(new File(filePath + "wordfile"), "rw");
			indexfile = new RandomAccessFile(new File(filePath + "indexfile"), "rw");

		} catch (IOException e) {
			System.out.println("Error in util.Main.init()");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void end() {
		try {
			if(stk != null) stk.close();
			if(hashfile != null) hashfile.close();
			if(wordfile != null) wordfile.close();
			if(indexfile != null) indexfile.close();
		} catch (Exception e) {
			System.out.println("Error in util.Main.end()");
			e.printStackTrace();
		}
	}

	private void run() {
		try {
			String in = stk.readLine();
			String[] arr = in.split(" ");
			List<Integer> indices = new LinkedList<>();
			currentWord = arr[0];

			while(stk.ready()) {
				currentWord = arr[0];

				do {
					indices.add(Integer.parseInt(arr[1]));
					arr = stk.readLine().split(" ");
				} while (arr[0].equals(currentWord));

				processWord(indices);
				writeHash();
				indices = new LinkedList<>();
			}

			indices.add(Integer.parseInt(arr[1]));
			currentWord = arr[0];
			processWord(indices);
			writeHash();

		} catch (IOException e) {
			System.out.println("Error in util.Main.run()");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Writes to wordfile and indexfile
	 * @param indices
	 * @throws IOException
	 */
	private void processWord(List<Integer> indices) throws IOException {
		indexFilePointer = (int) indexfile.getFilePointer();
		for (int i : indices) indexfile.writeInt(i);
		indexfile.writeInt(Integer.MAX_VALUE);
		wordFilePointer = (int) wordfile.getFilePointer();
		wordfile.writeChars(currentWord); 
		wordfile.writeChar(' ');
		wordfile.writeChars(String.valueOf(indexFilePointer));
		wordfile.writeChar('\n');

	}


	private void writeHash() throws IOException {

		CharHandler current = new CharHandler(currentWord);
		if (current.equals(lastShorthand)) {
			return;
		}

		do {
			lastShorthand = lastShorthand.next();
			for (char c : lastShorthand.getChars()) {
				if (c > 0) hashfile.writeChar(c);
			}
			hashfile.writeChar(' ');
			hashfile.writeChars(String.valueOf(wordFilePointer));
			hashfile.writeChar('\n');
		} while (!current.equals(lastShorthand));
	}
}
