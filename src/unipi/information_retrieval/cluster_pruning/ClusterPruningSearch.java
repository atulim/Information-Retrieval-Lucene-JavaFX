package unipi.information_retrieval.cluster_pruning;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import unipi.information_retrieval.lucene.PorterStemAnalyzer;
import unipi.information_retrieval.lucene.SearchLimitOneCosine;
import unipi.information_retrieval.lucene.SearchResult;

import java.io.FileNotFoundException;
import java.io.IOException;

import static unipi.information_retrieval.lucene.SimpleSearch.getSearchResult;

public class ClusterPruningSearch {
    private PorterStemAnalyzer porterStemAnalyzer;
    private Directory leaderIndexDirectory;
    private Directory clusterIndexDirectory;
    private SearchResult searchResult;
    public ClusterPruningSearch() throws FileNotFoundException {
        // initialize the variables after running the ClusterPruningIndex getLeaderIndex and getClusterIndex
        ClusterPruningIndex clusterPruningIndex= new ClusterPruningIndex();
        porterStemAnalyzer = clusterPruningIndex.getPorterStemAnalyzer();
        leaderIndexDirectory = clusterPruningIndex.getLeaderIndex();
        clusterIndexDirectory = clusterPruningIndex.getClusterIndex();
        searchResult = new SearchResult();
    }

    private String executeLeaderQuery(String queryString) throws IOException, ParseException {
        // we create the queryString for the leaderIndex cosine search
        queryString = "title: " +queryString + " OR summary:" + queryString + " OR authors:" + queryString;
        // we execute the cosine search and get back a single document
        Document d = SearchLimitOneCosine.getHit(queryString,porterStemAnalyzer,leaderIndexDirectory);
        if(d!=null) {
            return d.get("id");
        }
        return null;
    }

    public SearchResult executeQuery(String query) throws IOException, ParseException {
        String clusterid = executeLeaderQuery(query);
        if(clusterid != null) {
            String queryString = "(title:" + query + " OR summary:" + query + " OR authors:" + query + ") AND cluster:" + clusterid;
            Query q = null;
            q = new QueryParser("title", porterStemAnalyzer).parse(queryString);
            // The search
            return getSearchResult(q, clusterIndexDirectory, searchResult);
        }
        else return null;
    }
}


