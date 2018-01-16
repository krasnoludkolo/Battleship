package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Parent root = loader.load();

        stage.setTitle(":D");
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.setScene(new Scene(root, 1000, 500));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
