package PA3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {

    private String seedUrl;
    private List<String> keywords;
    private int max;
    private String fileName;
    private boolean isTopicSensitive;

    private HashMap<String, Integer> visited;
    private HashSet<String> disallowedSet;

    public static final String BASE_URL = "https://en.wikipedia.org";

    public WikiCrawler(String seedUrl, String[] keywords, int max, String fileName, boolean isTopicSensitive) {
        this.seedUrl = seedUrl;
        this.max = max;
        this.fileName = fileName;
        if (keywords != null) {
            this.keywords = new ArrayList<String>();
            for (String keyword : keywords)
                this.keywords.add(keyword);
        }
        this.isTopicSensitive = (keywords == null || keywords.length == 0) ? false : isTopicSensitive;
        this.visited = new HashMap<String, Integer>();
        this.disallowedSet = new HashSet<String>();
        initailizeDisallowedSet();
    }

    public void crawl() {
        WeightedQ q = new WeightedQ();
        ArrayList<String> edgesBySite = new ArrayList<String>();
        ArrayList<String> edgesByIdx = new ArrayList<String>();
        visit(seedUrl, q);
        while (!q.isEmpty()) {
            Tuple t = q.extract();
            String link = t.getLink();
            if (visited.size() < max + 1 && !visited.containsKey(link))
                visit(link, q);
            if (visited.containsKey(link)) {
                edgesBySite.add(t.getEdge());
                edgesByIdx.add(visited.get(t.getPage()) + " " + visited.get(t.getLink()));
            }
        }
        writeGraph(edgesBySite, edgesByIdx);
    }

    public void visit(String urlStr, WeightedQ q) {
        checkVisitedCount();
        try {
            URL url = new URL(BASE_URL + urlStr);
            InputStream is = url.openStream();
            visited.put(urlStr, visited.size()); // added to visited
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // group 2 is http address, group 4 is anchor text
            Pattern p = Pattern.compile("(<a href=\")(/wiki/.*?)(\".*?>)(.*?)(</a>)");
            String line;
            while ((line = br.readLine()) != null)
                if (line.contains("<p>"))
                    break;

            do {
                Matcher m = p.matcher(line);
                String curLink;
                // actual text w/o tags (lower case)
                String text = line.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "").toLowerCase();
                while (m.find()) {
                    curLink = m.group(2);
                    // 1. The link should not contain the characters “#” or “:”.
                    // 2. The graph you constructed should not have self loops nor it should have
                    // multiple edges.
                    // 3. The link should not in disallowed set.
                    if (curLink.contains("#") || curLink.contains(":") || urlStr.equals(curLink)
                            || disallowedSet.contains(curLink))
                        continue;
                    double w = calculateWeight(m, text);
                    q.add(urlStr + " " + curLink, w);
                }
            } while ((line = br.readLine()) != null);

            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateWeight(Matcher m, String text) {
        if (!isTopicSensitive)
            return 0.0;
        // 1. check httpAddress and anchorText
        String httpAddress = m.group(2).toLowerCase();
        String anchorText = m.group(4).replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "").toLowerCase();
        for (String keyword : keywords) {
            if (httpAddress.contains(keyword) || anchorText.contains(keyword))
                return 1.0;
        }

        // 2. check frontStr and backStr
        int idx1 = text.indexOf(anchorText), idx2 = idx1 + anchorText.length();
        String frontStr = text.substring(idx1 - 17 >= 0 ? idx1 : 0, idx1);
        String backStr = idx2 == text.length() ? ""
                : text.substring(idx2, idx2 + 17 >= text.length() ? text.length() : idx2 + 17);
        int dist = Integer.MAX_VALUE;
        for (String keyword : keywords) {
            if ((idx1 = frontStr.indexOf(keyword)) != -1)
                dist = Math.min(dist, frontStr.length() - idx1);
            if ((idx2 = backStr.indexOf(keyword)) != -1)
                dist = Math.min(dist, idx2);
        }
        return dist == Integer.MAX_VALUE ? 0.0 : 1.0 / (dist + 2);
    }

    private void checkVisitedCount() {
        if (visited.size() % 10 == 0)
            try {
                Thread.sleep(2100);
                // System.out.println("sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    private void initailizeDisallowedSet() {
        try {
            URL url = new URL(BASE_URL + "/robots.txt");
            InputStream is = url.openStream();
            visited.put("/robots.txt", visited.size()); // added to visited
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Pattern p = Pattern.compile("^(Disallow: )(/wiki/.*)");
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.find())
                    disallowedSet.add(m.group(2));
            }
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeGraph(List<String> edgesBySite, ArrayList<String> edgesByIdx) {
        try {
            FileWriter fw = new FileWriter(this.fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.max + "");
            bw.newLine();
            for (String line : edgesBySite) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();

            fw = new FileWriter(this.fileName.substring(0, this.fileName.indexOf(".txt")) + "_Idx.txt");
            bw = new BufferedWriter(fw);
            bw.write(this.max + "");
            bw.newLine();
            for (String line : edgesByIdx) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
