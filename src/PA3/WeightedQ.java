package PA3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.TreeSet;

/**
 * WeightedQ
 */
public class WeightedQ {

    private HashMap<String, Tuple> map;
    private TreeSet<Tuple> set;

    public WeightedQ() {
        this.map = new HashMap<String, Tuple>();
        this.set = new TreeSet<Tuple>((t1, t2) -> t2.getWeight() >= t1.getWeight() ? 1 : -1);
    }

    public void add(String edge, double weight) {
        Tuple t = new Tuple(edge, weight);
        if (map.containsKey(edge)) {
            if (weight > map.get(edge).getWeight()) {
                Iterator<Tuple> i = set.iterator();
                while (i.hasNext()) {
                    if (i.next().getEdge().equals(edge)) {
                        i.remove();
                        break;
                    }
                }
                set.add(t);
                map.replace(edge, t);
            }
        } else {
            set.add(t);
            map.put(edge, t);
        }
    }

    public Tuple extract() {
        if (set.isEmpty())
            return null;
        Iterator<Tuple> i = set.iterator();
        Tuple t = i.next();
        i.remove();
        map.remove(t.getEdge());
        return t;
    }

    public void forEach(Consumer<? super Tuple> consumer) {
        set.forEach(consumer);
    }

    public void mapforEach(Consumer<? super Map.Entry<String, Tuple>> consumer) {
        map.entrySet().forEach(consumer);
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

}