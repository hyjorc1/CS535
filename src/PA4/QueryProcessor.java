package PA4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import static PA4.PositionalIndex.getWordsFrom;

public class QueryProcessor {

	private PositionalIndex pi;

	public QueryProcessor(String folder) {
		this.pi = new PositionalIndex(folder);
	}

	public ArrayList<String> topKDocs(String query, int k) {
		HashMap<String, Double> map = new HashMap<String, Double>();
		String[] words = getWordsFrom(query);
		for (String word : words) {
			HashMap<String, ArrayList<Integer>> postings = pi.getPostings(word);
			System.out.println("Term: " + word + ", docFreq: " + postings.keySet().size());
			for (String doc : postings.keySet()) {
				if (!map.containsKey(doc))
					map.put(doc, pi.Relevance(query, doc));
			}
		}
		ArrayList<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(map.entrySet());
		Collections.sort(list, (e1, e2) -> e2.getValue() >= e1.getValue() ? 1 : -1);
		ArrayList<String> topK = new ArrayList<String>();
		for (int i = 0; i < k; i++) {
			String docName = list.get(i).getKey();
			double score = list.get(i).getValue();
			topK.add(docName);
			System.out.println(
					docName + " " + score + " " + pi.TPScore(query, docName) + " " + pi.VSScore(query, docName));
		}
		return topK;
	}

	public PositionalIndex getPI() {
		return this.pi;
	}
}
