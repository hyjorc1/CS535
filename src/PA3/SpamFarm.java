package PA3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpamFarm {

    private int size;
    private int target;
    private List<String> edges;

    public SpamFarm(String fileName, int target) {
        this.edges = new ArrayList<String>();
        this.target = target;
        readGraph(fileName);
        constructSpam();
    }

    private void constructSpam() {
        int prevSize = size;
        size += size / 10;
        for (int i = prevSize + 1; i <= size; i++) {
            edges.add(i + " " + target);
            edges.add(target + " " + i);
        }
    }

    private void readGraph(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            this.size = Integer.parseInt(line);
            while ((line = br.readLine()) != null) 
                edges.add(line);
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateSpam(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.size + "");
            bw.newLine();
            for (String line : edges) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
