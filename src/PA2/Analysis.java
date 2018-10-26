package PA2;

/**
 * Analysis
 */
public class Analysis {

	private static String PATH = "/Users/yijiahuang/2018 Fall/535/hw/PA2/";
	private static String SPACE_PATH = PATH + "space";
	private static String F17PA2_PATH = PATH + "F17PA2";
	private static String ARTICLES_PATH = PATH + "articles";

	public static void main(String[] args) {
		
	
		
		// MinHashAccuracy
//		int[] numPermutations = { 400, 600, 800 };
//		double[] epsilons = { 0.04, 0.07, 0.09 };
//		MinHashAccuracy ma = new MinHashAccuracy();
//		for (int numPermutation : numPermutations) {
//			for (double epsilon : epsilons) {
//				int count = ma.accuracy(SPACE_PATH, numPermutation, epsilon);
//				System.out.println("numPermutation: " + numPermutation + " epsilon: " + epsilon + " count: " + count);
//			}
//		}

		// MinHashTime
//		MinHashTime.timer(SPACE_PATH, 600);
		
		// nearDuplicateDetector
		NearDuplicates nd = new NearDuplicates(F17PA2_PATH, 600, 0.8);
		System.out.println("space-713.txt.copy4 " + nd.nearDuplciateDetector("space-713.txt.copy4").size() + nd.nearDuplciateDetector("space-713.txt.copy4").toString());
	}
}