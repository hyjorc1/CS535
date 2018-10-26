package PA2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static PA2.MinHash.nextPrime;

public class LSH {

	private ArrayList<HashMap<Integer, HashSet<String>>> hashTables;
	private int T;

	/**
	 * @param minHashMatrix is the MinHash matrix of the document collection.
	 * @param docNames      is an array of Strings consisting of names of
	 *                      documents/files in the document collection.
	 * @param bands         is the number of bands to be used to perform locality
	 *                      sensitive hashing.
	 */
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
//		for (int[] row : minHashMatrix){
//		    System.out.println(Arrays.toString(row));
//		   }
		Random random = new Random();
		int k = minHashMatrix.length;
		int r = k % bands == 0 ? k / bands : k / bands + 1;
		T = (int) nextPrime(docNames.length);
		hashTables = new ArrayList<HashMap<Integer, HashSet<String>>>(bands);
		for (int i = 0; i < bands; i++)
			hashTables.add(new HashMap<Integer, HashSet<String>>());
		int[][] pairs = new int[bands][2];
		for (int i = 0; i < bands; i++) {
			pairs[i][0] = (random.nextInt(T));
			pairs[i][1] = (random.nextInt(T));
		}
		System.out.println("bands & r & T: " + bands + " " + r + " " + T);
		for (int col = 0; col < minHashMatrix[0].length; col++) {
			int idx = 0, row = 0;
			while (idx < bands) {
				int next = r + idx;
				String s = "#";
				while (row < next && row < minHashMatrix.length)
					s.concat(minHashMatrix[row++][col] + "#");
				int h = ranHashCode(s, pairs[idx]);
//				System.out.println(h + " " + idx);
				if (!hashTables.get(idx).containsKey(h))
					hashTables.get(idx).put(h, new HashSet<String>());
				hashTables.get(idx++).get(h).add(docNames[col]);
			}
		}
	}

	private int ranHashCode(String s, int[] pair) {
		long h = (pair[0] * s.hashCode() + pair[1]) % this.T;
		return (int) (Math.abs(h) % this.T);
	}

	/**
	 * Takes name of a document docName as parameter and returns an array list of
	 * names of the near duplicate documents. Return type is ArrayList of Strings
	 */
	public ArrayList<String> nearDuplicatesOf(String docName) {
		HashSet<String> result = new HashSet<String>();
		for (HashMap<Integer, HashSet<String>> table : hashTables) {
			for (HashSet<String> set : table.values()) {
//				System.out.println(set);
				if (set.contains(docName)) {
//					System.out.println(set.size());
					result.addAll(set);
				}
			}
		}
		if (!result.isEmpty())
			result.remove(docName);
//		System.out.println(result.size());
		return new ArrayList<String>(result);
	}

}
