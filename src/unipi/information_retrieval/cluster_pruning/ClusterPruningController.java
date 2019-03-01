package unipi.information_retrieval.cluster_pruning;

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
import unipi.information_retrieval.utility.ChangeScene;

import java.io.IOException;

public class ClusterPruningController {
    @FXML
    private TableView<ClusterPruningViewModel> tableView;
    @FXML
    private TableColumn<ClusterPruningViewModel,String> id;
    @FXML
    private TableColumn<ClusterPruningViewModel,String> title;
    @FXML
    private TableColumn<ClusterPruningViewModel,String> authors;
    @FXML
    private TableColumn<ClusterPruningViewModel,String> cluster;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField queryTextField;
    @FXML
    private TextArea summaryTextArea;
    @FXML
    public void initialize(){
        // We initialize the gui elements
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        authors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        cluster.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        /*
         * We create mouse click event on the TableView. When clicked we show the summary of the document on the
         * TextArea.
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
    public void backToMain() throws IOException {
        // we switch to the main scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/main/Main.fxml"
                ,"Information Retrieval");
    }
    @FXML
    public void searchEvent(){
        /*
         * We run the cluster pruning search and we then populate the ObservableList in order to show the results.
         */
        try {
            String query= queryTextField.getText();
            SearchResult searchResult = new ClusterPruningSearch().executeQuery(query);
            if(searchResult!=null) {
                ObservableList<ClusterPruningViewModel> observableList = FXCollections.observableArrayList();
                populateObservableList(searchResult,observableList);
                tableView.setItems(observableList);
                tableView.getColumns().addAll();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateObservableList(SearchResult searchResult, ObservableList<ClusterPruningViewModel> observableList) throws IOException {
        /*
         * We create the ClusterPruningViewModel object with the search result data and populate the TableView
         */
        ScoreDoc[] hits = searchResult.getHits();
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searchResult.getIndexSearcher().doc(docId);
            ClusterPruningViewModel clusterPruningViewModel = new ClusterPruningViewModel()
                    .setId(d.get("id"))
                    .setTitle(d.get("title"))
                    .setAuthors(d.get("authors"))
                    .setSummary(d.get("summary"))
                    .setCluster(d.get("cluster"));
            observableList.add(clusterPruningViewModel);
        }
    }
}
