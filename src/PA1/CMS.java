package PA1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CMS {

	private int row;
	private int col;
	private int size;
	private int[][] multiset;
	private int p;
	private int a[];
	private int b[];
	private int numHash;
	ArrayList<String> store = new ArrayList<String>();// store distnict value

	public CMS(float epsilon, float delta, ArrayList<String> s) {
		this.row = (int) Math.abs((Math.log(1 / delta) / Math.log(2))); /// log2(epsilon)
		this.col = (int) Math.abs(2 / epsilon); /// 2/epsilon
		numHash = row;
		System.out.println("row" + row);
		System.out.println("col" + col);
		System.out.println("size" + row);
		this.multiset = new int[row][col];
		p = nextPrime(s.size());
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++)
				multiset[i][j] = 0;
		}

		size = s.size();
		a = new int[numHash];
		b = new int[numHash];
		Random random = new Random();
		for (int i = 0; i < numHash; i++) {
			a[i] = (random.nextInt(p));
			b[i] = (random.nextInt(p));
		} // we assume the possibility that a[i]== b[i] is low

		dist(s); /// store distinct value in store list

		for (int i = 0; i < s.size(); i++)
			add(s.get(i));

	}

	public int[] hash(String s) {
		int ranHash[] = new int[numHash];
		s = s.toLowerCase();
		for (int i = 0; i < a.length; i++) {
			int ran = 0;
			ran = (a[i] * s.hashCode() + b[i]) % p;
			ran = Math.abs(ran) % col;
			ranHash[i] = ran;
			// System.out.println(ran);
		}
		return ranHash;
	}

	void add(String s) {
		s = s.toLowerCase();
		int valueS[] = hash(s);
		for (int i = 0; i < valueS.length; i++) {
			++multiset[i][valueS[i]];
		}

	}

	private boolean isPrime(int num) {
		if (num == 2)
			return true;
		if (num % 2 == 0)
			return false;
		for (int i = 3; i * i <= num; i += 2)
			if (num % i == 0)
				return false;

		return true;
	}

	void dist(ArrayList<String> numbers) {
		for (int n = 0; n < numbers.size(); n++) {
			if (!store.contains(numbers.get(n))) { // if numbers[n] is not in store, then add it
				store.add(numbers.get(n));
			}
		}
	}

	public int nextPrime(int n) {
		int m = n;
		while (!isPrime(m)) {
			m++;
		}
		return m;
	}

	public int size() {
		return size;
	}

	public int getRows() {
		return this.row;
	}

	public int getColumns() {
		return this.col;
	}

	public int approximateFrequency(String x) {
		x = x.toLowerCase();
		int valueS[] = hash(x);
		int frequency = Integer.MAX_VALUE;

		for (int i = 0; i < valueS.length; i++) {
			if (multiset[i][valueS[i]] < frequency)
				frequency = multiset[i][valueS[i]];
		}
		return frequency;
	}

	public ArrayList<String> approximateHeavyHitter(float q, float r) {

		ArrayList<String> stringList = new ArrayList<String>();
		for (int i = 0; i < store.size(); i++) {
			int fx = approximateFrequency(store.get(i));
			if ((fx >= q * size) && !(fx < r * size))
				stringList.add(store.get(i));
		}

		return stringList;
	}

	public static void main(String[] args) throws IOException {

		// We need to provide file path as the parameter:
		// File file = new File("shakespear.txt");
		ArrayList<String> iteam = new ArrayList<String>();
		int counter = 0;
		Scanner sc2 = null;
		try {
			sc2 = new Scanner(new File(Differential.docPath + "/shakespear.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc2.hasNextLine()) {
			Scanner s2 = new Scanner(sc2.nextLine());
			while (s2.hasNext()) {
				String s = s2.next();// length is at least 3 and do not add the words "the" or "The"
				// s = s.toLowerCase();
				if (s.length() >= 3 && !s.equals("the") && !s.equals("The")) {
					++counter;
					iteam.add(s);
				}
			}
		}
		// First calculate the memory used before your code execution i.e. first line of
		// your code
		long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		float epsilon = (float) 0.01; // define epsilon = 1/100
		float delta = (float) Math.pow(2, -20); // define delta 2^-20.
		CMS cms = new CMS(epsilon, delta, iteam);

		float q = (float) 0.04;
		float r = (float) 0.03;
		ArrayList<String> L = cms.approximateHeavyHitter(q, r);

		System.out.println("lenth of L:" + L.size());

		System.out.println(counter + "	" + iteam.size());
		// Calculate the memory used after your code execution:-
		long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		long actualMemUsed = afterUsedMem - beforeUsedMem;

		System.out.println("An estimate of total memory used: " + actualMemUsed + "byte");
	}

}
