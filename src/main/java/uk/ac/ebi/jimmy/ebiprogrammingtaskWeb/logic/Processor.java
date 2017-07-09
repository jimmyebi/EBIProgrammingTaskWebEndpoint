package uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service("processorService")
public class Processor {

    // tokenizedInputArray will receive the array of strings to be processed 
    // e.g. {"ERR000111, ERR000112, ERR000113, ERR000115"}
    private String[] tokenizedInputArray;

    private List<String> filteredArrayList;
    private List<String> parseArray;
    private Storage storage;
    private Pattern filterPattern;
    private Pattern parsePattern;

    public Processor() {

        // The regex will match the L..LN..N pattern found in the input
        filterPattern = Pattern.compile("^[a-zA-Z]+[0-9]+$");

        // This other regex will help separate every single L..LN..N entry
        // into L..L and N..N for further processing down the program logic...
        parsePattern = Pattern.compile("[a-zA-Z]+|[0-9]+");
        filteredArrayList = new ArrayList<>();
        parseArray = new ArrayList<>();
        storage = new Storage();
    }

    // Allow only valid L..LN..N patterns without strange symbols
    public String filter(String str) {

        Matcher match = filterPattern.matcher(str);
        while (match.find()) {
            return match.group();
        }
        return "";
    }

    // Once the string is "clean", separate letters from numbers
    public List<String> lettersDigitSeparator(String str) {
        parseArray.clear();
        Matcher match = parsePattern.matcher(str);
        while (match.find()) {
            parseArray.add(match.group());
        }
        return parseArray;
    }

    // Read from std input and separate tokens by comma
    public void readStdInput() {
        BufferedReader in = null;
        String line = "";
        String cvsSplitBy = ",";
        try {

            in = new BufferedReader(new InputStreamReader(System.in));
            while ((line = in.readLine()) != null) {

                // use comma as separator
                tokenizedInputArray = line.split(cvsSplitBy);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // Read from a String
    public void readStringInput(String input) {
        String cvsSplitBy = ",";
        tokenizedInputArray = null;

        // use comma as separator
        tokenizedInputArray = input.split(cvsSplitBy);

    }

    public void startProcess() {
        
        filteredArrayList.clear();
        storage.clear();
        
        try {

            Stream<String> stream1 = Arrays.stream(tokenizedInputArray);
            stream1.forEach(x -> filteredArrayList.add((filter(x.trim()))));

            Stream<String> stream2 = filteredArrayList.stream();
            stream2.forEach(x -> storage.add((lettersDigitSeparator(x))));

            storage.sortNN();
            storage.rangedOutput();

        } catch (Exception ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Storage getStorage() {
        return storage;
    }

    public String[] getTokenizedInputArray() {
        return tokenizedInputArray;
    }

    public void setTokenizedInputArray(String[] tokenizedInputArray) {
        this.tokenizedInputArray = tokenizedInputArray;
    }
}
