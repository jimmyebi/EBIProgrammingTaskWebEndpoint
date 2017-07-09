package uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Storage {

    // outputRangedStringBuilder will hold the final sorted and range collapsed results
    private StringBuilder outputRangedStringBuilder;
    
    private Map<String, List<String>> entries;
    private String LL;
    private String NNrepresentation;
    private int NNLenght;
    private String key;
    private List<String> outputArray;
    
    public Storage() {

        entries = new HashMap();
        outputArray = new ArrayList<>();
        outputRangedStringBuilder = new StringBuilder();
    }

    // The incoming list parameter should look like this: [L..L, N..N]
    // which contains the Original string separated Letters from Numeric sequence.
    public void add(List<String> list) {

        // if the list comes in empty, for now just discard it.
        if (list.size() != 0) {

            LL = list.get(0);
            NNrepresentation = list.get(1);
            NNLenght = list.get(1).length();
            key = LL + NNLenght;

            // Check if there is already previous entries with same key
            if (entries.containsKey(key)) {

                entries.get(key).add(NNrepresentation);
            } else {

                entries.put((key), Stream.of(NNrepresentation).collect(Collectors.toList()));
            }
        }
    }

    public void display() {

        entries.forEach((key, value) -> {
            System.out.println("Key : " + key + " Value : " + value);
        });
    }

    // Sort the content of N..N
    public void sortNN() {

        entries.forEach((String key, List<String> value) -> {

            Collections.sort(value, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return extractInt(o1) - extractInt(o2);
                }

                int extractInt(String s) {

                    // Just incase digits are not found...
                    return s.isEmpty() ? 0 : Integer.parseInt(s);
                }
            });
        });
    }

    // Collapsed ranged output
    public void rangedOutput() {

        entries.forEach((key, value) -> {
            getRanges(value.stream().mapToInt(Integer::parseInt).toArray(), key)
                    .forEach(x -> outputArray.add(x));
        });

        Collections.sort(outputArray, (String o1, String o2) -> {
            String sub1 = o1;
            String sub2 = o2;
            return sub1.compareTo(sub2);
        });

        Iterator iterator = outputArray.iterator();
        while (iterator.hasNext()) {

            String element = (String) iterator.next();
            outputRangedStringBuilder.append(element);

            if (iterator.hasNext()) {
                outputRangedStringBuilder.append(", ");
            }
        }
    }

    public String getRangedResult() {

        return outputRangedStringBuilder.toString();
    }

    public List<String> getRanges(int[] list, String key) {

        String pushValue = "";
        String LLKeyOnly = key.replaceAll("[0-9]+", "");
        String NNLength = key.replaceAll("[^0-9]+", "");

        int rangeStart, rangeEnd = 0;
        List<String> rangedList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            rangeStart = list[i];
            rangeEnd = rangeStart;
            while ((i < list.length - 1) && (list[i + 1] - list[i] == 1)) {
                rangeEnd = list[i + 1]; // increment the index if the number is sequential
                i++;
            }

            String rangeStartOriginalNNFormat = String.format("%0" + NNLength + "d", rangeStart);
            String rangeEndOriginalNNFormat = String.format("%0" + NNLength + "d", rangeEnd);

            if (rangeStart == rangeEnd) {
                pushValue = LLKeyOnly + rangeStartOriginalNNFormat;
            } else {
                pushValue = LLKeyOnly + rangeStartOriginalNNFormat + "-" + LLKeyOnly + rangeEndOriginalNNFormat;
            }
            rangedList.add(pushValue);
        }

        return rangedList;
    }
    
        public void clear(){
    
        outputRangedStringBuilder.setLength(0);
        entries.clear();
        LL = null;
        NNrepresentation = null;
        NNLenght = 0;
        key = null;
        outputArray.clear();
    }
}
