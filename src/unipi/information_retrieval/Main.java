package unipi.information_retrieval;

import javafx.application.Application;
import javafx.stage.Stage;
import unipi.information_retrieval.utility.ChangeScene;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        // We change to the main scene
        ChangeScene.change(getClass(),
                stage,
                "/unipi/information_retrieval/main/Main.fxml"
                ,"Information Retrieval");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
