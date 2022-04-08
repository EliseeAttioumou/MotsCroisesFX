package motCroisee.v2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import motCroisee.v2.MainTP6;
import motCroisee.v2.controller.BaseController;
import motCroisee.v2.controller.ControleurTP6;
import motCroisee.v2.controller.MainMenuController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class ViewFactory {

    Stage stage;


    public ViewFactory(Stage stage) throws IOException, SQLException {
        this.stage = stage;
        setScene("MainMenu");
    }

    public void setScene(String sceneToLoad) throws IOException {
        this.setSceneBack(sceneToLoad,null);
    }

    public void setScene(String sceneToLoad, Map<String,String> params) throws IOException {
        this.setSceneBack(sceneToLoad,params);
    }

    private void setSceneBack(String sceneToLoad, Map<String,String> params) throws IOException {

        FXMLLoader loader = new FXMLLoader() ;

        //Chargement de la vue et du controlleur
        if ( sceneToLoad.equals("MainMenu") ){
            stage.setTitle("Menu principal : Projet mots croisés");
            MainMenuController mainMenuController = new MainMenuController("view/MainMenu.fxml",this);
            loader.setLocation(MainTP6.class.getResource(mainMenuController.getViewPath()));
            loader.setController(mainMenuController);

        } else if ( sceneToLoad.equals("VueTP6") ){
            stage.setTitle("En jeu : Projet mots croisés");
            ControleurTP6 controleurTP6 = new ControleurTP6("view/VueTP6.fxml",this,Integer.valueOf(params.get("gridNumber")));
            loader.setLocation(MainTP6.class.getResource(controleurTP6.getViewPath()));
            loader.setController(controleurTP6);

        } else {
            System.out.println("Vue non definie");
        }

        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        BaseController baseController = loader.getController();
        baseController.setScene(scene);
        baseController.afterInit();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

}
