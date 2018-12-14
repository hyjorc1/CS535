package PA3;

import java.util.ArrayList;
import java.util.List;

/**
 * Node
 */
public class Node {

    public String name;
    public double rank;
    public List<Integer> outs;
    public List<Integer> ins;

    public Node(String name) {
        this.name = name;
        this.rank = -1;
        this.outs = new ArrayList<Integer>();
        this.ins = new ArrayList<Integer>();
    }
    
}