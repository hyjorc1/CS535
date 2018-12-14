package PA4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PositionalIndex {

    private HashMap<String, HashMap<String, ArrayList<Integer>>> termToPostings;
    private ArrayList<String> terms;
    private ArrayList<String> docs;

    public PositionalIndex(String folder) {
        termToPostings = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
        processFolder(folder);
    }

    private void processFolder(String folder) {
        File[] files = new File(folder).listFiles();
        System.out.println("Num of files: " + files.length);
        this.docs = new ArrayList<String>();
        for (File file : files) {
            int pos = 1;
            String doc = file.getName();
            docs.add(doc);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    for (String word : getWordsFrom(line)) {
                        if (!termToPostings.containsKey(word))
                            termToPostings.put(word, new HashMap<String, ArrayList<Integer>>());
                        if (!termToPostings.get(word).containsKey(doc))
                            termToPostings.get(word).put(doc, new ArrayList<Integer>());
                        termToPostings.get(word).get(doc).add(pos++);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        terms = new ArrayList<String>(termToPostings.keySet());
        System.out.println("processFoldder done");
    }

    public static String[] getWordsFrom(String line) {
        line = line.toLowerCase().replaceAll("[,\"?'{}:;()]", "").replaceAll("\\[|\\]", ""); // remove punctuations
        line = line.replaceAll("\\.(?!\\d)", ""); // remove period
        return line.split("\\s+"); // split by space+
    }

    public int termFrequency(String term, String Doc) {
        if (termToPostings.get(term) == null || termToPostings.get(term).get(Doc) == null)
            return 0;
        return termToPostings.get(term).get(Doc).size();
    }

    public int docFrequency(String term) {
        if (termToPostings.get(term) == null)
            return 0;
        return termToPostings.get(term).size();
    }

    public HashMap<String, ArrayList<Integer>> getPostings(String t) {
        return this.termToPostings.get(t);
    }

    public String postingList(String t) {
    		t = t.toLowerCase();
        String output = "[";
        if (termToPostings.containsKey(t)) {
		    	for (Entry<String, ArrayList<Integer>> posting : termToPostings.get(t).entrySet()) {
		    		output += "<" + posting.getKey() + " : ";
		    		for (int pos : posting.getValue())
		    			output += pos + ", ";
		    		output = output.substring(0, output.length() - 2) + ">, ";
		    	}
		    	output = output.substring(0, output.length() - 2);
        }
        return output + "]";
    }

    public double weight(String t, String d) {
        int termFreq = termFrequency(t, d);
        int docFreq = docFrequency(t);
        return docFreq == 0 ? 0 : Math.sqrt(termFreq) * Math.log10((double) docs.size() / docFreq);
    }

    public double TPScore(String query, String doc) {
        String[] q = getWordsFrom(query);
        if (q.length == 1)
            return 0;
        double sum = 0;
        for (int i = 0; i < q.length - 1; i++)
            sum += distance(q[i], q[i + 1], doc);
        return sum == 0 ? 0 : q.length / sum;
    }

    public double distance(String t1, String t2, String doc) {
        HashMap<String, ArrayList<Integer>> posting1 = termToPostings.get(t1);
        HashMap<String, ArrayList<Integer>> posting2 = termToPostings.get(t2);
        if (posting1.containsKey(doc) && posting2.containsKey(doc)) {
            ArrayList<Integer> positions1 = posting1.get(doc);
            ArrayList<Integer> positions2 = posting2.get(doc);
            if (positions2.get(positions2.size() - 1) < positions1.get(0))
                return 17;
            int min = 17;
            for (int i : positions2)
                for (int j : positions1) {
	                	min = i > j ? Math.min(i - j, min) : min;
                }
            return min;
        }
        return 17;
    }

    public double VSScore(String query, String doc) {
        List<Integer> vq = vectorQ(query);
        List<Double> vd = vectorD(doc);
        return cosineSim(vq, vd);
    }

    private double cosineSim(List<Integer> vq, List<Double> vd) {
        double Lq = 0, Ld = 0, Tqd = 0;
        for (int i = 0; i < vq.size(); i++) {
            Lq += Math.pow(vq.get(i), 2);
            Ld += Math.pow(vd.get(i), 2);
            Tqd += vq.get(i) * vd.get(i);
            // if (vq.get(i) == 1)
            //     System.out.println(vd.get(i));
        }
        // System.out.println("top " + Tqd);
        // System.out.println(("bot " + Math.sqrt(Lq) * Math.sqrt(Ld)));
        if (Lq != 0 && Ld != 0)
            return Tqd / (Math.sqrt(Lq) * Math.sqrt(Ld));
        return 0;
    }

    private List<Integer> vectorQ(String query) {
        HashMap<String, Integer> termFreq = new HashMap<String, Integer>();
        for (String word : getWordsFrom(query)) {
            if (!termFreq.containsKey(word))
                termFreq.put(word, 0);
            termFreq.replace(word, termFreq.get(word) + 1);
        }
        ArrayList<Integer> vq = new ArrayList<Integer>();
        for (String term : terms) {
            vq.add(termFreq.get(term) == null ? 0 : termFreq.get(term));
        }
        return vq;
    }

    private List<Double> vectorD(String doc) {
        ArrayList<Double> vd = new ArrayList<Double>();
        for (String term : terms) {
            double w = weight(term, doc);
            // if (term.equals("history"))
            //     System.out.println(w);
            vd.add(w);
        }
        return vd;
    }

    public double Relevance(String query, String doc) {
        return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc);
    }

    public List<String> getDocs() {
        return this.docs;
    }
}
