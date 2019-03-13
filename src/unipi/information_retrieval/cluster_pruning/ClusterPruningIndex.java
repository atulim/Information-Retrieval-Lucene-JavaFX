package unipi.information_retrieval.cluster_pruning;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import unipi.information_retrieval.lucene.ExtractFromCacmAll;
import unipi.information_retrieval.lucene.PorterStemAnalyzer;
import unipi.information_retrieval.lucene.SearchLimitOneCosine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static unipi.information_retrieval.utility.PickRandomListElements.pickNRandomElements;

public class ClusterPruningIndex extends ExtractFromCacmAll {
    private List<Document> documents = new ArrayList<>();
    List<Document> leaders;
    private PorterStemAnalyzer porterStemAnalyzer;
    private Directory leaderIndex;
    private Directory clusterIndex;
    private IndexWriter indexWriter;

    public ClusterPruningIndex() throws FileNotFoundException {
        /* We take N random documents off the index. We create a leader index with the tf idf similarity. We get the
         * all the documents and assign to the a leader by asking the leader index which leader has the best cosine
         * similarity. We add the cluster id field to each document.
         */
        super();
        try {
        readIndexFile();
        createLeaderIndex();
        findClusterOfAll();
        createClusterIndex();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("deprecation")
    private void createLeaderIndex() throws IOException {
        // we create an index with the tf idf similarity and populate it with N random documents off the index.
            porterStemAnalyzer = new PorterStemAnalyzer();
            leaderIndex = new RAMDirectory();
            IndexWriterConfig myConfig = new IndexWriterConfig(porterStemAnalyzer);
            TFIDFSimilarity d = new ClassicSimilarity();
            myConfig.setSimilarity(d);
            indexWriter = new IndexWriter(leaderIndex, myConfig);
            // we get N random documents off the documents List
            leaders = pickNRandomElements(documents, (int) Math.round(Math.sqrt(documents.size())));
            // we add them to the LeaderIndex
            for(Document doc: leaders){
                indexWriter.addDocument(doc);
            }
            indexWriter.commit();
    }
    @SuppressWarnings("deprecation")
    private void createClusterIndex() throws IOException {
        // we create the cluster Index on which we also hold on which cluster every document belongs
        porterStemAnalyzer = new PorterStemAnalyzer();
        clusterIndex = new RAMDirectory();
        IndexWriterConfig myConfig = new IndexWriterConfig(porterStemAnalyzer);
        indexWriter = new IndexWriter(clusterIndex, myConfig);
        // we add every document in the document List to the ClusterIndex
        for(Document doc: documents){
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
    }
    private void findClusterOfAll() throws ParseException, IOException {
        // this is the method on which we assign a leader to each document in the ClusterIndex
        for(int i = 0; i<documents.size(); i++) {
                // we create the queryString for each document in the List documents
                String queryString = "title:" + documents.get(i).get("title") + " OR summary:" + documents.get(i).get("summary") + " OR authors:" + documents.get(i).get("authors");
                // we execute the cosine similarity search and get back one document
                Document d = SearchLimitOneCosine.getHit(queryString,porterStemAnalyzer,leaderIndex);
                // we set the field <<cluster>> of the document
                if(d!=null) {
                    Field clusterField = new TextField("cluster", d.get("id"), Field.Store.YES);
                    // we add the field to the documents List
                    documents.get(i).add(clusterField);
                }
        }
    }

    @Override
    protected void addDocument(String id, String title,String summary, String authors) throws IOException {
        super.addDocument(id,title,summary,authors);
        // we add the document to the documents List
        documents.add(doc);
    }


    //getters:

    public Directory getLeaderIndex() {
        return leaderIndex;
    }
    public Directory getClusterIndex() {
        return clusterIndex;
    }
    public PorterStemAnalyzer getPorterStemAnalyzer() {
        return porterStemAnalyzer;
    }
}
