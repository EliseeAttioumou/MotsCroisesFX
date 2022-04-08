package motCroisee.v2.controller;

import javafx.scene.Scene;
import motCroisee.v2.modele.ChargerGrille;
import motCroisee.v2.view.ViewFactory;

public class BaseController {


    protected String viewPath;
    protected ViewFactory viewFactory;
    protected ChargerGrille chargerGrille;
    protected Scene scene;

    public BaseController(String viewPath, ViewFactory viewFactory) {
        this.viewPath = viewPath;
        this.viewFactory = viewFactory;
        this.chargerGrille = new ChargerGrille();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void afterInit() {

    }


    public String getViewPath() {
        return viewPath;
    }


}
