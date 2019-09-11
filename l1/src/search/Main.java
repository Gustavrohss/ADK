package search;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.EOFException;
import java.io.File;
import java.util.Scanner;

public class Main {
	
	public final static String charset = "ISO-8859-1";
	public final static String filePath = "files/";

	public static String readLine(RandomAccessFile f) throws IOException {
		StringBuilder sb = new StringBuilder();
		char c = 0;
		
		while(true) {
			try {
				c = f.readChar();
				if(c == '\n') {
					break;
				}
				sb.append(c);
			} catch(EOFException e) {
				break;
			}
		}
		
		return sb.toString();
	}

	private static boolean accepted(String in) {
		for (char c : in.toCharArray()) {
			if ((c < 'a' || c > 'z') && c != 'å' && c != 'ä' && c != 'ö') return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		try (
			RandomAccessFile hashfile = new RandomAccessFile(new File(filePath + "hashfile"), "r");
			RandomAccessFile wordfile = new RandomAccessFile(new File(filePath + "wordfile"), "r");
			RandomAccessFile indexfile = new RandomAccessFile(new File(filePath + "indexfile"), "r");
			RandomAccessFile korpus = new RandomAccessFile(new File(filePath + "korpus"), "r");
		) {

			if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
				System.out.println("Usage: java search.Main <OPTIONS> <search word>");
				System.out.println("Please only use symbols from the Swedish alphabet.");
				System.out.println("Options:");
				System.out.println("	-h or --help	Prints this information");
				System.out.println("	-y		" +
				"Prints all occurences of the word, instead of asking permission" +
				" to print more than 30 words.");
				return;
			}
			
			String word = args[0];
			boolean unblock = word.equals("-y");
			if (unblock) word = args[1];

			word = word.toLowerCase().trim();

			if (!accepted(word)) {
				System.out.println("Please only use symbols found in the Swedish alphabet for your word.");
				return;
			}

			HashIndex hashindex = new HashIndex(hashfile);
			int[] index = hashindex.getRange(word);
			
			WordFileProcessor wordfileProcessor = new WordFileProcessor(word, wordfile);
			int[] indexRange = wordfileProcessor.binSearch(index[0], index[1]);
			
			if(indexRange == null) {
				System.out.println("Word does not exist");
				return;
			}
			
			IndexReader reader = new IndexReader(indexRange[0], indexRange[1], 
			word, indexfile, korpus);
			reader.build();
			
			if(reader.size() > 30 && !unblock) {
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
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter a word to search for.");
		}
	}
}
