package ngordnet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Collection;
import java.util.TreeMap;
public class YearlyRecord {
    private int size;
    private HashMap<String, Integer> hMap;
    private Map<Integer, String> inversehMap = new TreeMap<Integer, String>();
    private List<String> ascendList = new ArrayList<String>();
    private List<Integer> rankList;
    private Set<Number> countSet = new TreeSet<Number>();

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        size = 0;
        hMap = new HashMap<String, Integer>();
        rankList = new ArrayList<Integer>();
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        rankList = new ArrayList<Integer>();
        hMap = otherCountMap;
        size = otherCountMap.size();
        for (Integer val : otherCountMap.values()) {
            rankList.add(val);
            countSet.add(val);
        }
        Collections.sort(rankList, Collections.reverseOrder());
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        if (hMap == null || hMap.get(word) == null) {
            return 0;
        }
        return hMap.get(word);
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        hMap.put(word, count);
        inversehMap.put(count, word);
        rankList.add(count);
        Collections.sort(rankList, Collections.reverseOrder());
        countSet.add(count);
        size += 1;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return size;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        for (Integer i : rankList) {
            ascendList.add(0, inversehMap.get(i));
        }
        return ascendList;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        Collection<Number> countList = countSet;
        return countList;
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        return rankList.indexOf(hMap.get(word)) + 1;
    }
}
