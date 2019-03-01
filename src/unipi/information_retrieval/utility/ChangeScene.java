package unipi.information_retrieval.utility;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ChangeScene {
    public static void change(Class callingClass, Stage stage, String fxml, String title) throws IOException{
        AnchorPane anchorPane = null;
        // we get a reference to the fxml file.
        URL url = callingClass.getResource(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        // we load the file into the anchorPane Object.
        anchorPane = fxmlLoader.load();
        setSceneAndStage(anchorPane,stage,title);
    }

    private static void setSceneAndStage(AnchorPane anchorPane,Stage stage,String title){
        stage.setResizable(true);
        // we get the screen dimensions
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        // we create the scene
        Scene scene = new Scene(anchorPane,bounds.getWidth(),bounds.getHeight());
        // we display the program icon
        stage.getIcons().add(new Image("file:Information_Retrieval_Icon.png"));
        scene.setFill(Color.TRANSPARENT);
        // we set the title
        stage.setTitle(title);
        // we set the scene on the stage
        stage.setScene(scene);
        // we set the window to maximized
        stage.setMaximized(true);
    }

}
