package ngordnet;
public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
    	double total = 0;
    	double size = 0;
    	for (String word : yearlyRecord.words()) {
    		double product = word.length() * yearlyRecord.count(word);
    		size += yearlyRecord.count(word);
    		total += product;
    	}
    	return total/size;
    }
}