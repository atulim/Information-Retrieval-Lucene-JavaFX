package unipi.information_retrieval.lucene;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public class SearchResult {
    /*
     * This is class which creates objects which hold the results of a search and its IndexSearcher
     */
    private IndexSearcher indexSearcher;
    private ScoreDoc[] hits;

    public void setHits(ScoreDoc[] hits) {
        this.hits = hits;
    }

    public void setIndexSearcher(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public ScoreDoc[] getHits() {
        return hits;
    }
}
