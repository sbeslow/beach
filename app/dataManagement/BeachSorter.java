package dataManagement;
// TODO: Remove this object
import models.Beach;

import java.util.*;

public abstract class BeachSorter {


    public static List<Beach> sortByShittiest(List<Beach> beachList) {
        HashMap<Beach, Double> map = new HashMap<>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<Beach, Double> sorted_map = new TreeMap<>(bvc);

        for (Beach beach : beachList) {
//            map.put(beach, beach.getSeasonalStats().score());
        }

        sorted_map.putAll(map);
        return new ArrayList<>(sorted_map.keySet());
    }
}

class ValueComparator implements Comparator<Beach> {

    Map<Beach, Double> base;
    public ValueComparator(Map<Beach, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(Double a, Double b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

    @Override
    public int compare(Beach o1, Beach o2) {
        if (base.get(o1) >= base.get(o2)) {
            return -1;
        } else {
            return 1;
        }
    }
}
