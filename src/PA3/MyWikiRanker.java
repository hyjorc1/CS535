package PA3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MyWikiRanker
 */
public class MyWikiRanker {

    public static void main(String[] args) {

        // crawl pages
        String[] topics = { "racket", "court", "game" };
        String seedUrl = "/wiki/Tennis";
        int max = 500;
        long s = System.currentTimeMillis();
        WikiCrawler wc = new WikiCrawler(seedUrl, topics, max, "WikiTennisGraph.txt",
        true);
        wc.crawl();
        long e = System.currentTimeMillis();
        System.out.println((e-s)/1000);

        // page rank
        double[] eplisons = { 0.01, 0.005, 0.001 };
        double[] betas = { 0.25, 0.85 };
        int topK = 20;
        String file = "WikiTennisGraph_Idx.txt";

        PageRank pr = new PageRank(file, eplisons[0], betas[1]);
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String[] letters = { "A", "B", "C", "D", "E" };
        for (int i = 0; i < 5; i++)
            map.put(i, letters[i]);
        int[][] data = new int[5][topK];

        data[0] = pr.topKOutDegree(topK); // A
        data[1] = pr.topKInDegree(topK); // B
        data[2] = pr.topKPageRank(topK); // C
        pr = new PageRank(file, eplisons[1], betas[1]);
        data[3] = pr.topKPageRank(topK); // D
        pr = new PageRank(file, eplisons[2], betas[1]);
        data[4] = pr.topKPageRank(topK); // E

        for (double eplison : eplisons)
            new PageRank(file, eplison, betas[0]);

        System.out.println();
        System.out.println("SeedUrl: " + seedUrl + ", Max Pages: " + max);
        System.out.println("Topics: " + Arrays.toString(topics) + "\n");
        System.out.println("Top " + topK + " page rank: " + Arrays.toString(data[2]));
        System.out.println("Top " + topK + " in degree: " + Arrays.toString(data[1]));
        System.out.println("Top " + topK + " out degree: " + Arrays.toString(data[0]) + "\n");

        System.out.println("Jaccard Similarities: ");
        for (int i = 0; i < data.length; i++)
            for (int j = i + 1; j < data.length; j++) {
                System.out.println("Sets " + map.get(i) + ", " + map.get(j) + " exactJaccard: "
                        + exactJaccard(Arrays.stream(data[i]).boxed().collect(Collectors.toList()),
                                Arrays.stream(data[j]).boxed().collect(Collectors.toList())));
            }

        // spam farm
        System.out.println("\n" + "Spam Farm");
        PageRank pr1 = new PageRank(file, eplisons[2], betas[1]);
        Node ln = pr1.getLowestRankedNode();
        int v = Integer.parseInt(ln.name);
        double prevRank = ln.rank;
        System.out.println("Before Spam Farm");
        System.out.println("Lowest Ranked Node: " + v + " with Rank: " + prevRank + "\n");
        SpamFarm sf = new SpamFarm(file, v);
        String sfFile = "WikiTennisGraph_Idx_SF.txt";
        sf.CreateSpam(sfFile);
        
        PageRank prSpam = new PageRank(sfFile, eplisons[2], betas[1]);
        double curRank = prSpam.pageRankOf(v);
        System.out.println("After Spam Farm");
        System.out.println("Node: " + v + " with Rank: " + curRank);
        System.out.println("Difference: " + (curRank - prevRank));
    }

    public static double exactJaccard(List<Integer> a, List<Integer> b) {
        HashSet<Integer> intersection = new HashSet<Integer>(a);
        HashSet<Integer> union = new HashSet<Integer>(a);
        intersection.retainAll(b);
        union.addAll(b);
        return (intersection.size() * 1.0) / union.size();
    }
}