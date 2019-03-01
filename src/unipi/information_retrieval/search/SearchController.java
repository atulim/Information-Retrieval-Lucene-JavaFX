package unipi.information_retrieval.search;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import unipi.information_retrieval.lucene.SearchResult;
import unipi.information_retrieval.lucene.SimpleSearch;
import unipi.information_retrieval.utility.ChangeScene;

import java.io.IOException;

public class SearchController {
    @FXML
    private TableView<SearchViewModel> tableView;
    @FXML
    private TableColumn<SearchViewModel,String> id;
    @FXML
    private TableColumn<SearchViewModel,String> title;
    @FXML
    private TableColumn<SearchViewModel,String> authors;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField queryTextField;
    @FXML
    private TextArea summaryTextArea;

    @FXML
    public void initialize(){
        /*
         * we initialize the table view gui elements to
         * in order to show the right data in the right column
         */
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        authors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        /*
         * We create a listener on the table view in order to listen to
         * mouse clicks on the table view rows. When clicked we show
         * the summary text of the row in the textArea
         */
        tableView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                String summary = null;
                if(newValue!=null){
                    summary =  newValue.getSummary();
                }
                summaryTextArea.setText(summary);
            });
    }

    @FXML
    public void backToMain() throws IOException{
        // We change back to the main scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/main/Main.fxml"
                ,"Information Retrieval");
    }
    @FXML
    public void searchEvent(){
        /*
         * We first get the query text off the queryTextField. We then create a SimpleSearch Object
         * in order to execute the basic search on the data. We search with the title, summary and authors
         * fields. We subsequently populate the ObservableList Object in order to show the data on the TableView.
         */
        try {
        String query= queryTextField.getText();
        SearchResult simpleSearchResult = new SimpleSearch().executeQuery(query);
        ObservableList<SearchViewModel> observableList = FXCollections.observableArrayList();
        populateObservableList(simpleSearchResult,observableList);
        tableView.setItems(observableList);
        tableView.getColumns().addAll();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateObservableList(SearchResult simpleSearchResult, ObservableList<SearchViewModel> observableList) throws IOException {
        /*
         * This is the method which we use to populate the ObservableList. The ObservableList class is generic
         * and takes as an argument the SearchViewModel class. This way when we populate the ObservableList with data
         * and then show it with the TableView, the instance attributes of the SearchViewModel object are put into the
         * correct columns. Before we add elements to the ObservableList we create the SearchViewModel object using
         * the constructor.
         */
        ScoreDoc[] hits = simpleSearchResult.getHits();
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = simpleSearchResult.getIndexSearcher().doc(docId);
            observableList.add(new SearchViewModel(d.get("id"),d.get("title"),d.get("authors"),d.get("summary")));
        }
    }
}
