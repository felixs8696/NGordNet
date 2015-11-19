package ngordnet;
import edu.princeton.cs.introcs.In;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

public class NGramMap {
    YearlyRecord yRecord = new YearlyRecord();
    TimeSeries<Long> totalHistory = new TimeSeries<Long>();
    Map<Integer, YearlyRecord> yearMap = new HashMap<Integer, YearlyRecord>();
    Map<String, TimeSeries<Integer>> wordMap = new TreeMap<String, TimeSeries<Integer>>();
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);
        while (counts.hasNextLine()) {
            String[] countLine = counts.readLine().split(",");
            Integer countYear = Integer.valueOf(countLine[0]);
            Long wordCount = Long.valueOf(countLine[1]);
            yearMap.put(countYear, new YearlyRecord());
            if (totalHistory.keySet().contains(countYear)) {
                Long sum = wordCount + totalHistory.get(countYear);
                totalHistory.put(countYear, sum);
            } else {
                totalHistory.put(countYear, wordCount);
            }
        }
        while (words.hasNextLine()) {
            String[] wordLine = words.readLine().split("\t");
            String word = wordLine[0];
            Integer wordYear = Integer.valueOf(wordLine[1]);
            Integer wordCount = Integer.valueOf(wordLine[2]);
            YearlyRecord year = yearMap.get(wordYear);
            year.put(word, wordCount);
            TimeSeries<Integer> yearCount = new TimeSeries<Integer>();
            if (wordMap.keySet().contains(word)) {
                yearCount = wordMap.get(word);
            }
            yearCount.put(wordYear, wordCount);
            wordMap.put(word, yearCount);
        }
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        YearlyRecord wordRecord = yearMap.get(year);
        int wordCount = wordRecord.count(word);
        return wordCount;
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord wordRecord = new YearlyRecord();
        YearlyRecord temp = yearMap.get(year);
        for (String word : temp.words()) {
            wordRecord.put(word, temp.count(word));
        }
        return wordRecord;
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return totalHistory;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        if (wordMap.get(word) != null) {
            TimeSeries<Integer> interval = new TimeSeries(wordMap.get(word), startYear, endYear);
            return interval;
        }
        return null;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        return wordMap.get(word);
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        if (countHistory(word, startYear, endYear) == null) {
            return null;
        }
        TimeSeries<Double> rel = countHistory(word, startYear, endYear)
                                        .dividedBy(totalCountHistory());
        return rel;
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Double> rel = countHistory(word).dividedBy(totalCountHistory());
        return rel;
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        Iterator<String> iter = words.iterator();
        int count = 0;
        TimeSeries<Double> sum = new TimeSeries<Double>();
        while (iter.hasNext()) {
            String temp = iter.next();
            if (weightHistory(temp, startYear, endYear) != null && count == 0) {
                sum = weightHistory(temp, startYear, endYear);
                count += 1;
            } else {
                sum = sum.plus(weightHistory(temp, startYear, endYear));
            }
        }
        return sum;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        Iterator<String> iter = words.iterator();
        int count = 0;
        TimeSeries<Double> sum = new TimeSeries<Double>();
        while (iter.hasNext()) {
            String temp = iter.next();
            if (weightHistory(temp) == null) {
                System.out.println("null: " + temp);
            }
            if (weightHistory(temp) != null && count == 0) {
                sum = weightHistory(temp);
                count += 1;
            } else {
                sum = sum.plus(weightHistory(temp));
            }
        }
        return sum;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp) {
        return new TimeSeries(processedHistory(yrp), startYear, endYear);
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp)  {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        for (Integer year : yearMap.keySet()) {
            YearlyRecord yR = yearMap.get(year);
            processed.put(year, yrp.process(yR));
        }
        return processed;
    }
}
