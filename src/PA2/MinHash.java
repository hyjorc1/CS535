package PA2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MinHash {

    private int numPermutations;
    private String[] docsArray;
    private List<HashSet<String>> termsList;
    private HashSet<String> allTerms;
    private HashMap<String, Integer> fileNameToIdxMap;
    private int[][] pairs;
    private int prime;
    private int[][] termDocMat;

    /**
     * @param folder          is the name of a folder containing our document
     *                        collection for which we wish to construct MinHash
     *                        matrix.
     * @param numPermutations denotes the number of permutations to be used in
     *                        creating the MinHash matrix.
     */
    public MinHash(String folder, int numPermutations) {
        this.numPermutations = numPermutations;
        this.allTerms = new HashSet<String>();
        reprocess(folder);
        this.pairs = new int[numPermutations][2];
        this.prime = (int) nextPrime(this.allTerms.size());
        Random r = new Random();
        for (int[] pair : pairs) {
            pair[0] = r.nextInt(this.prime);
            pair[1] = r.nextInt(this.prime);
        }
    }

    private void reprocess(String folder) {
        File[] files = new File(folder).listFiles();
        docsArray = new String[files.length];
        termsList = new ArrayList<HashSet<String>>(files.length);
        fileNameToIdxMap = new HashMap<String, Integer>();
        for (int i = 0; i < files.length; i++) {
            docsArray[i] = files[i].getName();
            termsList.add(getWords(files[i]));
            fileNameToIdxMap.put(files[i].getName(), i);
        }
//        System.out.println("reprocessing done");
    }

    /**
     * Return the term document matrix of the collection.
     * 
     * @return Return type is 2D array of ints
     */
    public int[][] termDocumentMatrix() {
        int[][] matrix = new int[allTerms.size()][docsArray.length];
        for (int col = 0; col < docsArray.length; col++) {
	        	int row = 0;
            HashSet<String> terms = termsList.get(col);
            for (String word : allTerms) {
                matrix[row++][col] = terms.contains(word) ? 1 : 0;
//                System.out.println("getting termDocumentMatrix " + row + " " + col);
            }
        }
        this.termDocMat = matrix;
//        System.out.println("getting termDocumentMatrix done");
        return matrix;
    }

    /**
     * Returns the MinHash Matrix of the collection.
     * 
     * @return Return type is 2D array of ints.
     */
    public int[][] minHashMatrix() {
        int[][] permTermMat = new int[this.numPermutations][this.allTerms.size()]; // row: perm, col: term
        for (int row = 0; row < permTermMat.length; row++) {
            int col = 0;
            for (String word : allTerms) {
                permTermMat[row][col++] = ranHashCode(word, pairs[row]);
            }
        }
        int[][] termDocMat = this.termDocMat == null ? termDocumentMatrix() : this.termDocMat;
        int[][] matrix = new int[this.numPermutations][docsArray.length]; // row: perm, col: doc
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] = minPerm(termDocMat, permTermMat, row, col);
//                System.out.println("getting minHashMatrix "  + row + " " + col);
            }
        }
//        System.out.println("getting minHashMatrix done");
        return matrix;
    }

    private int minPerm(int[][] termDocMat, int[][] permTermMat, int permIdx, int docIdx) {
        int min = Integer.MAX_VALUE;
        for (int termIdx = 0; termIdx < termDocMat.length; termIdx++) {
            if (termDocMat[termIdx][docIdx] == 1) {
                min = Math.min(min, permTermMat[permIdx][termIdx]);
            }
        }
        return min;
    }

    /**
     * Returns an array of String consisting of all the names of files in the
     * document collection.
     */
    public String[] allDocs() {
        return this.docsArray;
    }

    /**
     * Returns the size of union of all documents (Note that each document is a
     * multiset of terms).
     */
    public int numTerms() {
        return this.allTerms.size();
    }

    /**
     * Returns the number of permutations used to construct the MinHash matrix.
     */
    public int numPermutations() {
        return this.numPermutations;
    }

    public int getFileIdx(String file) {
        return this.fileNameToIdxMap.getOrDefault(file, -1);
    }

    private HashSet<String> getWords(File file) {
        HashSet<String> terms = new HashSet<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.toLowerCase().replace("[.,:;']", ""); // lower case & remove .,:;'
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (word.length() > 2 && !word.equals("the")) { // remove word length <= 2 & "the"
                        allTerms.add(word);
                        terms.add(word);
                    }
                }
//                System.out.println("getting words");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("finish getting words!");
        return terms;
    }

    public static long nextPrime(long n) {
        BigInteger b = new BigInteger(String.valueOf(n));
        return Long.parseLong(b.nextProbablePrime().toString());
    }

    private int ranHashCode(String s, int[] pair) {
        long h = (pair[0] * s.hashCode() + pair[1]) % this.prime;
        return (int) (Math.abs(h) % this.allTerms.size());
    }
}
