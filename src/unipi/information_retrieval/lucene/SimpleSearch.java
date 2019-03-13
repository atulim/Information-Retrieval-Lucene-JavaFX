package unipi.information_retrieval.lucene;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleSearch {
    private PorterStemAnalyzer porterStemAnalyzer;
    private Directory indexDirectory;
    private SearchResult searchResult;
    public SimpleSearch() throws FileNotFoundException {
        // we create the SimpleIndex object and get the appropriate variables
        SimpleIndex simpleIndex = new SimpleIndex();
        porterStemAnalyzer = simpleIndex.getPorterStemAnalyzer();
        indexDirectory = simpleIndex.getIndex();
        // we instantiate the SearchResult object which holds the results of this search
        searchResult = new SearchResult();
    }

    public SearchResult executeQuery(String query) throws IOException, ParseException {
        if(!query.equals("")) {
            // we create the query string
            String queryString = QueryParser.escape(query);
            Query q = null;
            // we parse the query using the PorterStemAnalyzer
            q = new QueryParser("title", porterStemAnalyzer).parse(queryString);
            // The search
            // we return the search results
            return getSearchResult(q, indexDirectory, searchResult);
        }
        return null;
    }

    public static SearchResult getSearchResult(Query q, Directory indexDirectory, SearchResult searchResult) throws IOException {
        /*
         * We open the index. We execute the search and get the top scores. We set the searchResult object and return
         * it.
         */
        int hitsPerPage = 50;           // return 20 top documents
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        searchResult.setIndexSearcher(new IndexSearcher(indexReader));
        TopScoreDocCollector docCollector = TopScoreDocCollector.create(hitsPerPage);
        searchResult.getIndexSearcher().search(q, docCollector);
        searchResult.setHits(docCollector.topDocs().scoreDocs);
        return searchResult;
    }
}
