package unipi.information_retrieval.main;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import unipi.information_retrieval.utility.ChangeScene;

import java.io.IOException;


public class MainController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void mousePressedSearch() throws IOException {
        // we change to the search scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/search/Search.fxml"
                ,"Simple Search");
    }

    @FXML
    public void mousePressedCharts() throws IOException{
        // we change to the charts scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/charts/Charts.fxml"
                ,"Charts");
    }
    @FXML
    public void mousePressedClusterPruning() throws IOException{
        // we change to the cluster pruning scene
        ChangeScene.change(getClass(),
                (Stage) anchorPane.getScene().getWindow(),
                "/unipi/information_retrieval/cluster_pruning/ClusterPruning.fxml"
                ,"Cluster Pruning");
    }
}
