package unipi.information_retrieval.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleIndex extends ExtractFromCacmAll{
    private PorterStemAnalyzer porterStemAnalyzer;
    private Directory index;
    private IndexWriter indexWriter;

    @SuppressWarnings("deprecation")
    public SimpleIndex() throws FileNotFoundException {
        super();
        try {
            // we create the porter stem filter
            porterStemAnalyzer = new PorterStemAnalyzer();
            // we use the ram directory for our index
            index = new RAMDirectory();
            IndexWriterConfig myConfig = new IndexWriterConfig(porterStemAnalyzer);
            indexWriter = new IndexWriter(index, myConfig);
            // we run the readIndexFile from the base class ExtractFromCacmAll
            readIndexFile();
            // we commit the documents to the database
            indexWriter.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // getters
    public Directory getIndex() {
        return index;
    }

    public PorterStemAnalyzer getPorterStemAnalyzer() {
        return porterStemAnalyzer;
    }

    @Override
    protected void addDocument(String id, String title, String summary, String authors) throws IOException {
        // we run the overriden addDocument method in order to create the document and add it to the index
        super.addDocument(id,title,summary,authors);
        indexWriter.addDocument(doc);
    }

}
