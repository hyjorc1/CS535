package PA2;

import java.util.ArrayList;
import java.util.Arrays;

public class NearDuplicates {

	private LSH lsh;

	/**
	 * @param folder          Name of the folder containing documents
	 * @param numPermutations Number of Permutations to be used for MinHash
	 * @param threshold       Similarity threshold s, which is a double
	 */
	public NearDuplicates(String folder, int numPermutations, double threshold) {
		MinHash mh = new MinHash(folder, numPermutations);
		System.out.println(Arrays.asList(mh.allDocs()) + " DOn!!");
		lsh = new LSH(mh.minHashMatrix(), mh.allDocs(), calculateNumBands(numPermutations, threshold));
	}

	/**
	 * gets (name of ) a document as parameter, which is a string. Then this method
	 * returns a list of documents that are at least s-similar to docN ame, by
	 * calling the method nearDuplicatesOf from LSH. Note that this list may contain
	 * some False Positives— Documents that are less than s-similar to docN ame.
	 */
	public ArrayList<String> nearDuplciateDetector(String docName) {
		return lsh.nearDuplicatesOf(docName);
	}

	private static int calculateNumBands(int k, double threshold) {
		int bands = 1, r = -1;
		do {
			bands++;
			r = k % bands == 0 ? k / bands : k / bands + 1;
		} while (Math.pow((1.0 / bands), (1.0 / r)) > threshold);
		return bands;
	}
}
