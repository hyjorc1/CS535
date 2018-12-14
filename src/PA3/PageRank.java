package PA3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class PageRank {

    private double eplison;
    private double beta;

    private int numEdges;
    private Node[] nodes;
    private HashMap<String, Integer> urlToIdx;
    private int[][] sorted; // row 0 by rank, row 1 by outDegree, row 2 by inDegree.

    public PageRank(String fileName, double eplison, double beta) {
        this.eplison = eplison;
        this.beta = beta;
        this.numEdges = 0;
        urlToIdx = new HashMap<String, Integer>();
        constructGraph(fileName);
        calculatePageRank();
        updateSortedByDegree();
    }

    private void constructGraph(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            nodes = new Node[Integer.parseInt(line)];
            while ((line = br.readLine()) != null) {
                String[] edge = line.split(" ");
                for (String v : edge)
                    if (!urlToIdx.containsKey(v)) {
                        urlToIdx.put(v, urlToIdx.size());
                        nodes[urlToIdx.get(v)] = new Node(v);
                    }
                int idx1 = urlToIdx.get(edge[0]), idx2 = urlToIdx.get(edge[1]);
                nodes[idx1].outs.add(idx2);
                nodes[idx2].ins.add(idx1);
                numEdges++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void calculatePageRank() {
        double[] p = new double[nodes.length];
        for (int i = 0; i < p.length; i++)
            p[i] = 1.0 / p.length;
        int n = 0;
        boolean converged = false;
        while (!converged) {
            double[] p1 = calculatePNlusOne(p);
            if (norm(p1, p) <= this.eplison)
                converged = true;
            p = p1;
            n++;
        }
        for (int i = 0; i < nodes.length; i++)
            nodes[i].rank = p[i];
        System.out.println("Epsilon: " + this.eplison + ", Beta: " + this.beta + ", Num of Iterations: " + n);
    }

    private double norm(double[] p1, double[] p) {
        double sum = 0;
        for (int i = 0; i < p.length; i++)
            sum += Math.abs(p1[i] - p[i]);
        return sum;
    }

    private double[] calculatePNlusOne(double[] p) {
        double[] p1 = new double[p.length];
        double defaultVal = (1.0 - this.beta) / p.length;
        for (int i = 0; i < p.length; i++)
            p1[i] = defaultVal;
        for (int j = 0; j < nodes.length; j++) {
            Node node = nodes[j];
            if (!node.outs.isEmpty()) {
                for (int i : node.outs)
                    p1[i] = p1[i] + this.beta * (p[j] / node.outs.size());
            } else {
                for (int i = 0; i < nodes.length; i++)
                    p1[i] = p1[i] + this.beta * (p[j] / nodes.length);
            }
        }
        return p1;
    }

    private void updateSortedByDegree() {
        sorted = new int[3][nodes.length];
        Node[] arr = Arrays.copyOf(nodes, nodes.length);
        Arrays.sort(arr, (a, b) -> b.rank == a.rank ? 0 : b.rank > a.rank ? 1 : -1);
        for (int i = 0; i < nodes.length; i++)
            sorted[0][i] = Integer.parseInt(arr[i].name); // sorted in rank
        Arrays.sort(arr, (a, b) -> b.outs.size() == a.outs.size() ? 0 : b.outs.size() > a.outs.size() ? 1 : -1);
        for (int i = 0; i < nodes.length; i++)
            sorted[1][i] = Integer.parseInt(arr[i].name); // sorted in out degree
        Arrays.sort(arr, (a, b) -> b.ins.size() == a.ins.size() ? 0 : b.ins.size() > a.ins.size() ? 1 : -1);
        for (int i = 0; i < nodes.length; i++)
            sorted[2][i] = Integer.parseInt(arr[i].name); // sorted in in degree
    }

    public double pageRankOf(int v) {
        return nodes[urlToIdx.get(v + "")].rank;
    }

    public int outDegreeOf(int v) {
        return nodes[urlToIdx.get(v + "")].outs.size();
    }

    public int inDegreeOf(int v) {
        return nodes[urlToIdx.get(v + "")].ins.size();
    }

    public int numEdges() {
        return this.numEdges;
    }

    public int[] topKPageRank(int k) {
        return Arrays.copyOf(sorted[0], k);
    }

    public int[] topKOutDegree(int k) {
        return Arrays.copyOf(sorted[1], k);
    }

    public int[] topKInDegree(int k) {
        return Arrays.copyOf(sorted[2], k);
    }

    public Node getLowestRankedNode() {
        return nodes[urlToIdx.get(sorted[0][this.nodes.length - 1]+"")];
    }

}
