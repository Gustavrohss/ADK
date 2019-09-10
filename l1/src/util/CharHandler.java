package util;

/**
 * Wrapper class for handling 3-character arrays
 */
public class CharHandler implements Comparable<CharHandler> {

	private char[] data = new char[3];
	
	public CharHandler(String seed) {
		this(seed.substring(0, Math.min(seed.length(), 3)).toCharArray());
	}

	public CharHandler(char[] seed) {

		if (seed.length == 0) {
			// If a String of length 0 has been passed here, something is terribly wrong
			new Exception("Seed length 0 in CharHandler").printStackTrace();
			System.exit(1);
		}

		for (int i = 0; i < seed.length; i++) {
			data[i-(seed.length-3)] = seed[i];
		}
	}

	/**
	 * Generates the "next" CharHandler
	 * For example, if (data == [a, a, a]) next = [a, a, b]
	 * @return
	 */
	public CharHandler next() {
		CharHandler next = new CharHandler(this.data);

		for (int i = 0; i < next.data.length; i++) {
			if (next.data[i] == 0){
				next.leftshift();
				next.data[2] = 'a';
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
	
		else if(this.data[i] == 'ö'){
			this.incrementIndex(i-1);
			this.rightshift();
		}
		else if (this.data[i] == 'å') this.data[i] = 'ö';
		else if (this.data[i] == 'ä') this.data[i] = 'å';
		else if (this.data[i] == 'z') this.data[i] = 'ä';
		else this.data[i]++;
	}

	/**
	 * Shifts the data contents "to the left"
	 * "Rightmost" character set to 0
	 * [*, a, a] -> [a, a, 0]
	 */
	private void leftshift(){
		this.data[0] = this.data[1];
		this.data[1] = this.data[2];
		this.data[2] = 0;
	}

	/**
	 * As leftshift, but to the right
	 */
	private void rightshift(){
		this.data[2] = this.data[1];
		this.data[1] = this.data[0];
		this.data[0] = 0;
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
		for (int i = 0; i < 3; i++) {
			if (data[i] != other.data[i]) return data[i] - other.data[i];
		}
		return 0;
	}

	public char[] getData() { 
		return data; 
	}
}