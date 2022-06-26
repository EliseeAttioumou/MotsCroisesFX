package motCroisee.v2;

import javafx.application.Application;
import javafx.stage.Stage;
import motCroisee.v2.view.ViewFactory;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			@SuppressWarnings("unused")
			ViewFactory viewFactory = new ViewFactory(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
