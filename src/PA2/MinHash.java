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
	private int[][] pairs;
	private int prime;
	private HashMap<String, Integer> fileNameToIdxMap;
	private List<HashSet<Integer>> docTermsList; // terms in each doc
	private HashMap<String, Integer> termToIdxMap; // union of terms in the docs

	/**
	 * @param folder          is the name of a folder containing our document
	 *                        collection for which we wish to construct MinHash
	 *                        matrix.
	 * @param numPermutations denotes the number of permutations to be used in
	 *                        creating the MinHash matrix.
	 */
	public MinHash(String folder, int numPermutations) {
		this.numPermutations = numPermutations;
		reprocess(folder);
		this.pairs = new int[numPermutations][2];
		this.prime = (int) nextPrime(this.termToIdxMap.size());
		Random r = new Random();
		for (int[] pair : pairs) {
			pair[0] = r.nextInt(this.prime);
			pair[1] = r.nextInt(this.prime);
		}
	}

	private void reprocess(String folder) {
		File[] files = new File(folder).listFiles();
		docsArray = new String[files.length];
		docTermsList = new ArrayList<HashSet<Integer>>();
		termToIdxMap = new HashMap<String, Integer>();
		fileNameToIdxMap = new HashMap<String, Integer>();
		for (int i = 0; i < files.length; i++) {
			docsArray[i] = files[i].getName();
			fileNameToIdxMap.put(docsArray[i], i);
			docTermsList.add(new HashSet<Integer>());
			processDoc(files[i], docTermsList.get(i));
		}
	}

	private void processDoc(File file, HashSet<Integer> terms) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase().replaceAll("[.,:;']", ""); // lower case & remove .,:;'
				String[] words = line.split("\\s+");
				for (String word : words) {
					if (word.length() > 2 && !word.equals("the")) { // remove word length <= 2 & "the"
						if (!termToIdxMap.containsKey(word))
							termToIdxMap.put(word, termToIdxMap.size());
						terms.add(termToIdxMap.get(word));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the term document matrix of the collection.
	 * 
	 * @return Return type is 2D array of ints
	 */
	public int[][] termDocumentMatrix() {
		int[][] matrix = new int[termToIdxMap.size()][docsArray.length];
		for (int col = 0; col < docsArray.length; col++)
			for (Integer row : docTermsList.get(col))
				matrix[row][col] = 1;
		return matrix;
	}

	/**
	 * Returns the MinHash Matrix of the collection.
	 * 
	 * @return Return type is 2D array of ints.
	 */
	public int[][] minHashMatrix() {
		int[][] matrix = new int[this.numPermutations][docsArray.length]; // row: perm, col: doc
		for (int col = 0; col < docsArray.length; col++) {
			int[] sig = minHashSig(docTermsList.get(col), col);
			for (int row = 0; row < sig.length; row++)
				matrix[row][col] = sig[row];
		}
		return matrix;
	}

	private int[] minHashSig(HashSet<Integer> terms, int docIdx) {
		int[] sig = new int[this.numPermutations];
		for (int i = 0; i < sig.length; i++) {
			int min = Integer.MAX_VALUE;
			for (int termIdx : terms) {
				int perm = ranPermutation(termIdx, pairs[i]);
				min = Math.min(perm, min);
			}
			sig[i] = min;
		}
		return sig;
	}

	public static long nextPrime(long n) {
		BigInteger b = new BigInteger(String.valueOf(n));
		return Long.parseLong(b.nextProbablePrime().toString());
	}

	private int ranPermutation(Integer termIdx, int[] pair) {
		long h = (pair[0] * termIdx + pair[1]) % this.prime;
		// return (int) (Math.abs(h) % this.termToIdxMap.size());
		return (int) h;
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
		return this.termToIdxMap.size();
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

}
