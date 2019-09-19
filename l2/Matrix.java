class Matrix {

    private int[][] matrix;
    private String mainWord;
    private String prevWord = "";

    public Matrix(String word) {
        mainWord = word;
        matrix = new int[40][word.length() + 1];
        for (int i = 0; i < matrix.length; i++) matrix[i][0] = i;
        for (int j = 0; j < matrix[0].length; j++) matrix[0][j] = j;
    }

    public int update(String word) {
        int n = mainWord.length();
        int m = word.length();
        int same = same(prevWord, word);

        if (m > matrix.length) makeBigger();
        
        for (int i = same + 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                int res = matrix[i - 1][j - 1] + ((word.charAt(i - 1) == mainWord.charAt(j - 1)) ? 0 : 1);
                int add = matrix[i][j - 1] + 1;
                int del = matrix[i - 1][j] + 1;
                matrix[i][j] = Math.min(Math.min(res, add), del);
            }
        }

        prevWord = word;
        return matrix[m][n];
    }

    private int same(String s1, String s2) {
        int count = 0;
        char[] arr1 = s1.toCharArray(); char[] arr2 = s2.toCharArray();
        while(count < arr1.length && count < arr2.length) {
            if (arr1[count] == arr2[count]) count++;
            else break;
        }
        return count;
    }

    private void makeBigger() {
        int wordSz = matrix[0].length;
        int[][] newm = new int[matrix.length + 20][wordSz];
        for (int i = 0; i < newm.length; i++) newm[i][0] = i;
        for (int j = 0; j < newm[0].length; j++) newm[0][j] = j;
        for (int i = 1; i < matrix.length; i++) 
            for (int j = 0; j < matrix[0].length; j++)
                newm[i][j] = matrix[i][j];
        matrix = newm;
    }
}