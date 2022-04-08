package motCroisee.v2;

/*
@author Christophe QUINIOU Elise ATTIOUMOU
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import motCroisee.v2.view.ViewFactory;

public class MainTP6 extends Application {


    @Override
    public void start(Stage primaryStage) {
        try
        {
            ViewFactory viewFactory = new ViewFactory(primaryStage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
