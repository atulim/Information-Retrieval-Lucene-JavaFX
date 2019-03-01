package unipi.information_retrieval.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class QueryTextFile {
    private ObservableList<ChartsViewModel> queries= FXCollections.observableArrayList();
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/query.text")));
    private String currLine;
    private String id = null;
    private StringBuilder query = new StringBuilder();
    boolean foundField = false;


    public QueryTextFile() throws FileNotFoundException {
        /*
         * we read the query file and extract the data from it.
         */
        try {
            currLine = bufferedReader.readLine();
            while (currLine != null)
                if (currLine.matches("\\.I [0-9]+$"))
                {   // we have found a document in the file
                    weHaveFoundADocument();
                }
                else
                    // Continue to next line
                    currLine = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void weHaveFoundADocument() throws IOException {
        // we extract the id
        extractId();
        while(currLine!=null && !currLine.matches("\\.I [0-9]+$"))
        {

            foundField = false;
            // under the id of the document we have found the title of a field (e.g. title, summary, authors)
            weHaveFoundADocumentFieldTitle();
            if(foundField)
            {
                //we extract the document field
                extractDocumentField();
                continue;
            }
            currLine = bufferedReader.readLine();
        }
        // Add the id and the query String to the ObservableList in order to show it at the TableView
        queries.add(new ChartsViewModel(id,query.toString()));
        // Clear the string builder
        query.delete(0, query.length());
    }


    private void extractId() throws IOException {
        id = currLine.substring(3).trim();
        currLine = bufferedReader.readLine();
    }


    private void weHaveFoundADocumentFieldTitle(){
        if(currLine.matches("\\.W$")
                || currLine.matches("\\.N$")
                || currLine.matches("\\.A$"))
        {
            foundField = true;
        }
    }


    private void extractDocumentField() throws IOException {
        currLine = bufferedReader.readLine();
        /*
         * this while runs while we are inside a document field meaning we have not found another field's title and
         * we haven't found the end of the file
         */
        while(currLine!= null && !currLine.matches("\\.[A-Z]$") && !currLine.matches("\\.I [0-9]+$"))
        {
            query.append(currLine).append(" ");
            currLine = bufferedReader.readLine();
        }
    }

    //getter:
    public ObservableList<ChartsViewModel> getQueries() {
        return queries;
    }
}
