package unipi.information_retrieval.cluster_pruning;

public class ClusterPruningViewModel {
    /*
     * the view model which holds one row of the data returned by the ClusterPruningSearch
     */
    private String id;
    private String title;
    private String authors;
    private String summary;
    private String cluster;
    public String getId() {
        return id;
    }

    public ClusterPruningViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ClusterPruningViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthors() {
        return authors;
    }

    public ClusterPruningViewModel setAuthors(String authors) {
        this.authors = authors;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public ClusterPruningViewModel setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getCluster() {
        return cluster;
    }

    public ClusterPruningViewModel setCluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

}
