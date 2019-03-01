package unipi.information_retrieval.charts;

public class ChartsViewModel {
    /*
     * a class which is view model for the data in the charts TableView
     */
    private String id;
    private String query;
    public ChartsViewModel(String id, String query){
        this.id = id;
        this.query =query;
    }

    public String getQuery() {
        return query;
    }

    public String getId() {
        return id;
    }
}
