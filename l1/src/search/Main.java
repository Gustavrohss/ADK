package search;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	public final static String charset = "ISO-8859-1";
	
	public static void main(String[] args) {
		
		try {
			
			
			String word = "ö";
			HashIndex i = new HashIndex();
			int[] index = i.getRange(word);
			
			
			WordFile f = new WordFile(word, index[0], index[1]);
			int[] indexRange = f.getIndexRange();
			
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
			
			
//			RandomAccessFile indexfile = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/files/indexfile"), "r");
//			indexfile.seek(indexRange[0]);
//			int korpusindex = indexfile.readInt();
//			
//			RandomAccessFile korpus = new RandomAccessFile(new File("/home/jonas/Documents/Kurser/ADK/lab1/korpus"), "r");
//			korpus.seek(korpusindex);
//			System.out.println(korpusindex);
//			System.out.println(korpus.readLine());
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
