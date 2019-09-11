package util;

/**
 * Wrapper class for handling 3-character arrays
 */
public class CharHandler implements Comparable<CharHandler> {

	private char[] chars = new char[3];
	
	public CharHandler(String seed) {
		this(seed.substring(0, Math.min(seed.length(), 3)).toCharArray());
	}

	public CharHandler(char[] seed) {

		if (seed.length == 0) {
			new Exception("Seed length 0 in util.CharHandler(char[])").printStackTrace();
			System.exit(1);
		}

		for (int i = 0; i < seed.length; i++) {
			chars[i-(seed.length-3)] = seed[i];
		}
	}

	/**
	 * Generates the "next" CharHandler
	 * For example, if (data == [a, a, a]) next = [a, a, b]
	 * @return
	 */
	public CharHandler next() {
		CharHandler next = new CharHandler(this.chars);

		for (int i = 0; i < next.chars.length; i++) {
			if (next.chars[i] == 0) {
				next.leftshift();
				next.chars[2] = 'a';
				return next;
			}
		}
		next.incrementIndex(2);
		return next;

	}

	/**
	 * Increments data at position i
	 * @param i Must be 0, 1, or 2
	 */
	private void incrementIndex(int i) {
		if (i < 0 || i > 2) return;
	
		else if(this.chars[i] == 'ö'){
			this.incrementIndex(i-1);
			this.rightshift();
		}
		else if (this.chars[i] == 'å') this.chars[i] = 'ö';
		else if (this.chars[i] == 'ä') this.chars[i] = 'å';
		else if (this.chars[i] == 'z') this.chars[i] = 'ä';
		else this.chars[i]++;
	}

	/**
	 * Shifts the data contents "to the left"
	 * "Rightmost" character set to 0
	 * [*, a, a] -> [a, a, 0]
	 */
	private void leftshift(){
		this.chars[0] = this.chars[1];
		this.chars[1] = this.chars[2];
		this.chars[2] = 0;
	}

	/**
	 * As leftshift, but to the right
	 */
	private void rightshift(){
		this.chars[2] = this.chars[1];
		this.chars[1] = this.chars[0];
		this.chars[0] = 0;
	}

	@Override
	public boolean equals(Object o){
		if (o.getClass() != this.getClass()) {
			return false;
		}
		CharHandler ch = (CharHandler) o;
		return this.compareTo(ch) == 0;
	}

	@Override
	public int compareTo(CharHandler other) {
		return new String(this.chars).trim().compareTo(new String(other.chars).trim());
	}

	public char[] getChars() { 
		return chars; 
	}
}