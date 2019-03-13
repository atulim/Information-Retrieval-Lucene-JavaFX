package unipi.information_retrieval.charts;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import unipi.information_retrieval.lucene.PorterStemAnalyzer;
import unipi.information_retrieval.lucene.SearchResult;
import unipi.information_retrieval.lucene.SimpleIndex;
import unipi.information_retrieval.lucene.SimpleSearch;
import unipi.information_retrieval.utility.ChangeScene;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartsController {
    @FXML
    private TableView<ChartsViewModel> tableView;
    @FXML
    private TableColumn<ChartsViewModel,String> id;
    @FXML
    private TableColumn<ChartsViewModel,String> query;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private LineChart<Number,Number> recallPrecisionLineChart;

    private PorterStemAnalyzer porterStemAnalyzer;
    private Directory indexDirectory;

    @FXML
    public void backToMain() throws IOException {
        // we switch to the main scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/main/Main.fxml"
                ,"Information Retrieval");
    }
    public void initialize() throws FileNotFoundException {
        // we create a simple index from the data in the cacm.all file
        SimpleIndex simpleIndex = new SimpleIndex();
        //we initialize the variables from the SimpleIndex object
        porterStemAnalyzer = simpleIndex.getPorterStemAnalyzer();
        indexDirectory = simpleIndex.getIndex();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        query.setCellValueFactory(new PropertyValueFactory<>("query"));
        //We set the TableView with the data
        setTableView();
    }

    private void setTableView() throws FileNotFoundException {
        /*
         * we create an mouse click event listener so that when we press a row in the TableView the charts are being
         * shown.
         */

        tableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(newValue!=null){
                        try {
                            createCharts(newValue.getId(),newValue.getQuery());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // we set the TableView with the data from the QueryTestFile queries
        tableView.setItems(new QueryTextFile().getQueries());
        tableView.getColumns().addAll();
    }
    private void createCharts(String id,String query) throws FileNotFoundException {
        /*
         * We execute the lucene query for the specific query String and compute the precision and recall series
         * which we pass to the two LineCharts
         */
        //we execute the queries
        List<String> resultQuery = executeQuery(query);
        List<String> expectedResultIds = new QrelsTextFile().getQueries().get(id);
        XYChart.Series recallPrecisionSeries = new XYChart.Series();
        // we iterate over the results in order to compute the series
        iterateOverResultts(resultQuery,expectedResultIds,recallPrecisionSeries);
        removePreviousLines();
        // we add the data to the LineCharts
        recallPrecisionLineChart.getData().addAll(recallPrecisionSeries);
    }
    private void iterateOverResultts(List<String> resultQuery,List<String> expectedResultIds, XYChart.Series recallPrecisionSeries) {
        /*
         * We compute the precision and recall series.
         */
        int counterFoundMatchingIds = 0;
        int counterCheckedResultQueryIds = 0;

        if(resultQuery!=null)
        {
            int numberOfResultsQuery = resultQuery.size();
            /*
             * we check for every id that is present in the qrels file under the id of the query the user has
             * instructed us to compute.
             */
            for(String id: resultQuery){
                //if the id is contained in the qrels file
                if(expectedResultIds!=null && expectedResultIds.contains(id))
                {
                    counterFoundMatchingIds++;
                }
                counterCheckedResultQueryIds++;
                // we compute the precision and recall
                Float YPrecision = Float.valueOf((float)counterFoundMatchingIds/(float)counterCheckedResultQueryIds);
                Float XRecall = Float.valueOf((float)counterFoundMatchingIds/(float)numberOfResultsQuery);
                // we add the numbers to the series
                recallPrecisionSeries.getData().add(new XYChart.Data(XRecall,YPrecision));
            }
        }
    }

    private void removePreviousLines(){
        // we remove the last lines
        if (!recallPrecisionLineChart.getData().isEmpty()){
            recallPrecisionLineChart.getData().remove(0);
        }
    }
    private List<String> executeQuery(String query){
        /*
         * we execute a standard query on the index and add the results to the result list
         */
        List<String> result = new ArrayList<>();
        try {
            String queryString = "title:"+query+ " OR summary:" + query+ " OR authors:" + query;
            SearchResult searchResult = new SimpleSearch().executeQuery(query);
            // we get the result
            ScoreDoc[] hits = searchResult.getHits();
            // Copy results to list models
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searchResult.getIndexSearcher().doc(docId);
                // we add the result to the list
                result.add(d.get("id"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            return result;
        }
    }
}
