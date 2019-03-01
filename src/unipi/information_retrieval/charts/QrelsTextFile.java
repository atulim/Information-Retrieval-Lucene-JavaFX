package unipi.information_retrieval.charts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class QrelsTextFile {
    private Map<String, List<String>> queries = new HashMap<>();
    private String currLine;
    private String id = new String();
    private String reference = new String();
    private BufferedReader buffR = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/qrels.text")));

    public QrelsTextFile() throws FileNotFoundException {
        /*
         * we extract data from the qrels file
         */
        try {
            currLine = buffR.readLine();
            while (currLine != null){
                currLine = currLine.replaceAll("[ ]+"," ");
                // we extract the id
                extractId();
                // we extract the reference
                extractReference();
                // we set the query HashMap which holds all the data
                setQueriesHashMap();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractId(){
        id = currLine.substring(0,currLine.indexOf(" "));
        if(id.charAt(0) == '0'){
            id=id.substring(1);
        }
    }

    private void extractReference() throws IOException {
        currLine = currLine.substring(currLine.indexOf(" ")).trim();
        reference = currLine.substring(0,currLine.indexOf(" "));
        currLine = buffR.readLine();
    }
    private void setQueriesHashMap(){
        // if the current if is not contained in the HashMap we add it
        if(queries.get(id) == null)
        {
            queries.put(id,  new ArrayList<>(Arrays.asList(reference)));
        }
        else // if the id is already inside the HashMap we add the reference to it
        {
            queries.get(id).add(reference);
        }
    }
    // getter
    public Map<String, List<String>> getQueries() {
        return queries;
    }

}
