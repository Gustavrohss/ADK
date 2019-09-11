package search;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.util.Scanner;

public class Main {
	
	public final static String charset = "ISO-8859-1";
	public final static String filePath = "files/";
	
	public static void main(String[] args) {
		
		try (
			RandomAccessFile hashfile = new RandomAccessFile(new File(filePath + "hashfile"), "r");
			RandomAccessFile wordfile = new RandomAccessFile(new File(filePath + "wordfile"), "r");
			RandomAccessFile indexfile = new RandomAccessFile(new File(filePath + "indexfile"), "r");
			RandomAccessFile korpus = new RandomAccessFile(new File(filePath + "korpus"), "r");
		) {
			
			String word = args[0];
			HashIndex i = new HashIndex(hashfile);
			int[] index = i.getRange(word);
			
			WordFile f = new WordFile(word, wordfile);
			int[] indexRange = f.binSearch(index[0], index[1]);
			
			if(indexRange == null) {
				System.out.println("Word does not exist");
				return;
			}
			
			IndexReader reader = new IndexReader(indexRange[0], indexRange[1]);
			reader.build();
			System.out.println(reader.size());
			
			if(reader.size() > 30) {
				System.out.printf("Found %d entries, print them all? [N/y]\n", reader.size());
				Scanner sc = new Scanner(System.in);
				if(!sc.next().toLowerCase().equals("y")) {
					sc.close();
					return;
				}
				sc.close();
			}
			
			while(reader.hasNext()) {
				System.out.println(reader.next());
			}
			
		} catch (IOException e) {
			System.out.println("Error in search.Main.main(String[])");
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}
