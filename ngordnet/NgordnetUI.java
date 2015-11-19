package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Felix Su
 */
public class NgordnetUI {
    public static void main(String[] args) {

        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");

        System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");
        NGramMap ngMap = new NGramMap(wordFile, countFile);
        WordNet words = new WordNet(synsetFile, hyponymFile);
        int startDate = 1505;
        int endDate = 2008;
        while (true) {
            System.out.print("> ");
                String line = StdIn.readLine();
                String[] rawTokens = line.split(" ");
                String command = rawTokens[0];
                String[] tokens = new String[rawTokens.length - 1];
                System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
                switch (command) {
                    case "quit": 
                        return;
                    case "help":
                        In help = new In("./ngordnet/help.txt");
                        String helpStr = help.readAll();
                        System.out.println(helpStr);
                        break;
                    case "range": 
                        startDate = Integer.parseInt(tokens[0]); 
                        endDate = Integer.parseInt(tokens[1]);
                        System.out.println("Start date: " + startDate);
                        System.out.println("End date: " + endDate);
                        break;
                    case "count":
                    	System.out.println(ngMap.countInYear(tokens[0], Integer.valueOf(tokens[1])));
                    	break;
                    case "hyponyms":
                    	System.out.println(words.hyponyms(tokens[0]));
                        break;
                    case "history":
                    	Plotter.plotAllWords(ngMap, tokens, startDate, endDate);
                    	break;
                    case "hypohist":
                    	Plotter.plotCategoryWeights(ngMap, words, tokens, startDate, endDate);
                    	break;
                    case "wordlength":
                        Plotter.plotProcessedHistory(ngMap, startDate, endDate,
                                            new WordLengthProcessor());
                        break;
                    case "zipf":
                        Plotter.plotZipfsLaw(ngMap, Integer.valueOf(tokens[0]));
                        break;
                    default:
                        System.out.println("Invalid command.");  
                        break;
            }
        }
    }
} 
