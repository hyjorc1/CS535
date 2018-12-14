package PA2;

/**
 * Analysis
 */
public class Analysis {

	private static String PATH = "/Users/yijiahuang/2018 Fall/535/hw/PA2/";
	private static String SPACE_PATH = PATH + "space";
	private static String SAMPLE_PATH = PATH + "sample";
	private static String F17PA2_PATH = PATH + "F17PA2";
	// private static String ARTICLES_PATH = PATH + "articles";

	public static void main(String[] args) {

		// MinHashAccuracy
		int[] numPermutations = { 400, 600, 800 };
		double[] epsilons = { 0.09 };
		MinHashAccuracy ma = new MinHashAccuracy();
		for (int numPermutation : numPermutations) {
			for (double epsilon : epsilons) {
				int count = ma.accuracy(SPACE_PATH, numPermutation, epsilon);
				System.out.println("numPermutation: " + numPermutation + " epsilon: " + epsilon + " count: " + count);
			}
		}

		// nearDuplicateDetector
		String[] files = { "hockey111.txt", "baseball1.txt", "hockey661.txt", "space-101.txt" };
		double[] thresholds = { 0.8, 0.9 };
		int[] numPermutations1 = { 400, 600 };

		for (String file : files) {
			for (double threshold : thresholds) {
				for (int numPermutation : numPermutations1) {
					System.out.println();
					NearDuplicates nd = new NearDuplicates(F17PA2_PATH, numPermutation, threshold);
					System.out.println("Inputs - file: " + file + ", numPermutation: " + numPermutation
							+ ", threshold: " + threshold);
					System.out.println("Similar docs:");
					for (String e : nd.nearDuplciateDetector(file))
						System.out.println(e);
				}
			}
		}

	}
}