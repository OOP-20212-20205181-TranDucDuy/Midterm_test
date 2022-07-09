package group11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartProject extends Application {
	public void start(Stage stage){
		try {
			Parent root  = FXMLLoader.load(getClass().getResource("HelloScene.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Start");
			stage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}

}
