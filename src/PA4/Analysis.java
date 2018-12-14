package PA4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Analysis
 */
public class Analysis {

	public QueryProcessor qp;
	public PositionalIndex pi;

	public static void main(String[] args) {

		String folder = "/Users/yijiahuang/2018 Fall/535/hw/PA4/IR";

		String q1 = "history";
		String q2 = "history museum";
		String q3 = "New York Yankees";
		String q4 = "1925 Chicago Cubs season";
		String q5 = "Robert Morris University for";

		String q = q1;
		Analysis als = new Analysis(folder);
		System.out.println("Query: " + q);
		als.qp.topKDocs(q, 10);

	}

	public Analysis(String folder) {
		this.qp = new QueryProcessor(folder);
		this.pi = this.qp.getPI();
	}

	public void topKTermFreq(String q1, int k) {
		ArrayList<pair> list = new ArrayList<pair>();
		for (Entry<String, ArrayList<Integer>> e : pi.getPostings(q1).entrySet())
			list.add(new pair(e.getKey(), e.getValue().size()));

		list.sort(new Comparator<pair>() {

			@Override
			public int compare(pair o1, pair o2) {
				int c = o2.value - o1.value;
				if (c == 0)
					return 0;
				return c > 0 ? 1 : -1;
			}

		});
		for (int i = 0; i < k; i++) {
			System.out.println(list.get(i).key + ", " + list.get(i).value + ", " + pi.TPScore(q1, list.get(i).key)
					+ ", " + pi.VSScore(q1, list.get(i).key));
		}
	}
}

class pair {
	String key;
	int value;

	public pair(String k, int v) {
		this.key = k;
		this.value = v;
	}
}