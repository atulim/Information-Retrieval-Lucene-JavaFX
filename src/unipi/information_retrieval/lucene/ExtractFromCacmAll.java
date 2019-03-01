package unipi.information_retrieval.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ExtractFromCacmAll {
    protected Document doc;
    private BufferedReader buffR = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/cacm.all")));
    private String currLine;
    private String id = new String();
    private StringBuilder title = new StringBuilder();
    private StringBuilder summary = new StringBuilder();
    private StringBuilder authors = new StringBuilder();
    private boolean foundField = false;
    private StringBuilder temp = null;
    protected ExtractFromCacmAll() throws FileNotFoundException {
    }


    protected void readIndexFile(){
        /*
         * When reading the cacm.all file we start with this method.
         * When we find a line that matches the pattern
         * (.I[white space][series of numbers from 0 to 9][no other symbols])
         * then this means that we have found a document.
         */
        try {
            currLine = buffR.readLine();
            while (currLine != null)
                if (currLine.matches("\\.I [0-9]+$"))
                {
                    foundDocument();
                }
                else
                    // Continue to next line
                    currLine = buffR.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void addDocument(String id, String title,String summary, String authors) throws IOException{
        // we create a Lucene Document setting the fields of the document to the appropriate variables
        doc = new Document();
        Field idField = new TextField("id", id, Field.Store.YES);
        Field titleField = new TextField("title", title, Field.Store.YES);
        Field summaryField = new TextField("summary", summary, Field.Store.YES);
        Field authorsField = new TextField("authors", authors, Field.Store.YES);

        doc.add(idField);
        doc.add(titleField);
        doc.add(summaryField);
        doc.add(authorsField);
    }


    private void foundDocument() throws IOException {
        // We first extract the Id of the document.
        extractId();
        while(currLine!=null && !currLine.matches("\\.I [0-9]+$"))
        {
            foundField = false;
            // we check whether we have found a useful document field
            foundFieldTitle();
            if(foundField)
            {
                // if we did find a useful document field we extract it.
                extractField();
                continue;
            }
            currLine = buffR.readLine();
        }
        // Add the new document to the index
        addDocument(id, title.toString(), summary.toString(), authors.toString());
        // Clear the string builders
        clearStringBuilders();
    }


    private void extractId() throws IOException {
        id = currLine.substring(3);
        currLine = buffR.readLine();
    }

    private void extractField() throws IOException {
        /*
         * We run this code appending to the temp StingBuilder the contents of the field until we find the beginning
         * of another field or until with find the end of this specific document. The temp variable is a reference to
         * each one of the title, summary and authors StringBuilders.
         */
        currLine = buffR.readLine();
        while(currLine!= null && !currLine.matches("\\.[A-Z]$") && !currLine.matches("\\.I [0-9]+$"))
        {
            temp.append(currLine).append(" ");
            currLine = buffR.readLine();
        }
    }


    private void foundFieldTitle(){
        // we check whether we have found a useful field
        foundFieldWithTitleTitle();
        foundFieldWithTitleSummary();
        foundFieldWithTitleAuthors();
    }

    private void foundFieldWithTitleAuthors(){
        // we check whether we have found the authors field
        if(currLine.matches("\\.A$"))
        {
            temp = authors;
            foundField = true;
        }
    }


    private void foundFieldWithTitleSummary(){
        // we check whether we have found the summary field
        if(currLine.matches("\\.W$"))
        {
            temp = summary;
            foundField = true;
        }
    }

    private void foundFieldWithTitleTitle(){
        // we check whether we have found the title field
        if(currLine.matches("\\.T$"))
        {
            temp = title;
            foundField = true;
        }
    }

    private void clearStringBuilders(){
        // we empty the string builders
        title.delete(0, title.length());
        summary.delete(0, summary.length());
        authors.delete(0, authors.length());
    }
}
