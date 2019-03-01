package unipi.information_retrieval.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class SearchLimitOneCosine {
    private static IndexReader indexReader;
    private static IndexSearcher indexSearcher;
    private static Query q = null;
    private static TFIDFSimilarity tfidfSimilarity;

    public static Document getHit(String queryString,
                                  PorterStemAnalyzer porterStemAnalyzer,
                                  Directory leaderIndexDirectory) throws IOException, ParseException {
        initializeVariables(queryString, porterStemAnalyzer, leaderIndexDirectory);
        /*
         * With this method we execute a search which has only one result or zero results. It uses the TF IDF
         * similarity in order to get the relevant documents according to their cosine similarity with the query.
         */
        int hitsPerPage = 1;
        // we set the similartity of the IndexSearcher
        indexSearcher.setSimilarity(tfidfSimilarity);
        TopScoreDocCollector docCollector = TopScoreDocCollector.create(hitsPerPage);
        // we execute the search
        indexSearcher.search(q, docCollector);
        // we get the result
        ScoreDoc[] hit = docCollector.topDocs().scoreDocs;
        if(hit.length==1)
        {
            int docId = hit[0].doc;
            Document d = indexSearcher.doc(docId);
            return d;
        }
        return null;
    }
    private static void initializeVariables(String queryString,
                                            PorterStemAnalyzer porterStemAnalyzer,
                                            Directory leaderIndexDirectory) throws ParseException, IOException {
        q = new QueryParser("title", porterStemAnalyzer).parse(queryString);
        indexReader = DirectoryReader.open(leaderIndexDirectory);
        indexSearcher = new IndexSearcher(indexReader);
        /*
         * we crate the tf idf similarity object passing in the classing similarity class which has some specific
         * method definitions for the tf and idf.
         */

        tfidfSimilarity = new ClassicSimilarity();
    }
}
