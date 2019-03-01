package unipi.information_retrieval.search;

public class SearchViewModel {
    /*
     * This is the view model which we use in order to populate the TableView GUI element. It has getters and setter
     * and a constructor which are used to do that.
     */
    private String id;
    private String title;
    private String authors;
    private String summary;
    SearchViewModel(String id,String title,String authors,String summary){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.summary = summary;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
