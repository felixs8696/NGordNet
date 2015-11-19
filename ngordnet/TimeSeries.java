package ngordnet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.TreeSet;
import java.util.ArrayList;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /* Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    // private NavigableSet<Integer> validYears(int startYear, int endYear) {
      // return new HashSet<Integer>();
    // }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        for (Integer key: ts.keySet()) {
            if (key >= startYear && key <= endYear) {
                this.put(key, ts.get(key));
            }
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        for (Integer key: ts.keySet()) {
            this.put(key, ts.get(key));
        }
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> tQuotient = new TimeSeries<Double>();
        for (Integer key: this.keySet()) {
            if (ts.get(key) == null) {
                throw new IllegalArgumentException();
            } else {
                Double q = this.get(key).doubleValue() / ts.get(key).doubleValue();
                tQuotient.put(key, q);
            }
        }
        return tQuotient;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> tSum = new TimeSeries<Double>();
        if (ts == null || ts.keySet().size() == 0) {
            for (Integer key: this.keySet()) {
                tSum.put(key, this.get(key).doubleValue());
            }
            return tSum;
        } else if (this.keySet().size() == 0) {
            for (Integer key: ts.keySet()) {
                tSum.put(key, ts.get(key).doubleValue());
            }
            return tSum;
        } else {
            for (Integer key : ts.keySet()) {
                Double s = 0.0;
                if (!this.containsKey(key)) {
                    s = ts.get(key).doubleValue();
                } else {
                    s = this.get(key).doubleValue() + ts.get(key).doubleValue();
                }
                tSum.put(key, s);
            }
            for (Integer key : this.keySet()) {
                Double s = 0.0;
                if (!ts.containsKey(key)) {
                    s = this.get(key).doubleValue();
                    tSum.put(key, s);
                }
            }
        }
        return tSum;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> yearSet = new TreeSet<Number>();
        for (Integer key : this.keySet()) {
            yearSet.add(key);
        }
        return yearSet;
    }

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
        Collection<Number> dataSet = new ArrayList<Number>();
        for (Number key : this.years()) {
            dataSet.add(this.get(key));
        }
        return dataSet;
    }
}
