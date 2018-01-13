package view;

import ai.RandomBot;
import game.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.BattleshipGame;
import game.Player;
import game.logic.RestBattleshipGame;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private int size = 6;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(e->System.exit(0));

        String asd = "jeden";
        Player a = new Player(asd,getBoard());
        Player b = new Player("bot",getBoard());
        BattleshipGame battleshipGame = new RestBattleshipGame(a,b, size);

        Thread thread = new Thread(new RandomBot(battleshipGame,"bot"));
        thread.start();
        show(asd, a, b, battleshipGame);



        //show(asd, a, b, battleshipGame);
    }

    private void show(String asd, Player a, Player b, BattleshipGame battleshipGame) throws java.io.IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
        Parent root = loader.load();
        GameController gameController = loader.getController();
        gameController.init(battleshipGame, asd);

        stage.setTitle("Hello World");
        stage.setOnCloseRequest(e->System.exit(0));

        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private List<List<Coordinates>> getBoard() {
        List<List<Coordinates>> list = new ArrayList<>();

        for (int i = 1; i < 7; i+=2) {
            List<Coordinates> ship = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Coordinates c = new Coordinates(i,j);
                ship.add(c);
            }
            list.add(ship);
        }
        return list;
    }
}
