package search;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
	
	public final static String filePath = "../files/";
	
	public static void main(String[] args) {
		
		try (
			RandomAccessFile korpus = new RandomAccessFile(new File(filePath + "korpus"), "r");
			RandomAccessFile indexFile = new RandomAccessFile(new File(filePath + "indexfile"), "r");
			RandomAccessFile hashFile = new RandomAccessFile(new File(filePath + "hashfile"), "r");
			RandomAccessFile wordFile = new RandomAccessFile(new File(filePath + "wordfile"), "r");
		) {
			String searchWord = args[0];

			HashIndex hashIndex = new HashIndex(hashFile);
			int[] searchRange = hashIndex.getRange(searchWord);
			
			WordFileProcessor wordFileProcessor = new WordFileProcessor(searchWord, wordFile);
			int indicesPosition = wordFileProcessor.binSearch(searchRange[0], searchRange[1]);
			
			indexFile.seek(indicesPosition);
			int korpusindex = indexFile.readInt();
			
			korpus.seek(korpusindex);
			System.out.println(korpus.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}
