package PA2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static PA2.MinHash.nextPrime;

public class LSH {

	private ArrayList<HashMap<Integer, HashSet<String>>> hashTables;

	/**
	 * @param minHashMatrix is the MinHash matrix of the document collection.
	 * @param docNames      is an array of Strings consisting of names of
	 *                      documents/files in the document collection.
	 * @param bands         is the number of bands to be used to perform locality
	 *                      sensitive hashing.
	 */
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		int r = minHashMatrix.length / bands;
		int tableSize = (int) nextPrime(docNames.length * 8);
		hashTables = new ArrayList<HashMap<Integer, HashSet<String>>>();

		StringBuilder s = null;
		for (int bandIdx = 0; bandIdx < bands; bandIdx++) {
			HashMap<Integer, HashSet<String>> table = new HashMap<Integer, HashSet<String>>();
			int start = bandIdx * r, end = start + r;
			for (int col = 0; col < minHashMatrix[0].length; col++) {
				s = new StringBuilder().append("#");
				for (int row = start; row < end; row++)
					s.append(minHashMatrix[row][col] + "#");
				int h = s.toString().hashCode() % tableSize;
				if (!table.containsKey(h))
					table.put(h, new HashSet<String>());
				table.get(h).add(docNames[col]);
			}
			hashTables.add(table);
		}
	}

	/**
	 * Takes name of a document docName as parameter and returns an array list of
	 * names of the near duplicate documents. Return type is ArrayList of Strings
	 */
	public ArrayList<String> nearDuplicatesOf(String docName) {
		HashSet<String> result = new HashSet<String>();
		for (HashMap<Integer, HashSet<String>> table : hashTables) {
			for (HashSet<String> set : table.values()) {
				if (set.contains(docName)) {
					result.addAll(set);
				}
			}
		}
		if (!result.isEmpty())
			result.remove(docName);
		return new ArrayList<String>(result);
	}

}
