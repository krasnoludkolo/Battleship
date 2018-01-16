package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = loadParent();
        stage.setTitle(":D");
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(new Scene(root, 1000, 500));
        stage.show();
    }

    private Parent loadParent() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        return loader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
