package view;

import ai.Bot;
import ai.RandomBot;
import game.BattleshipGame;
import game.Coordinates;
import game.Player;
import game.logic.RestBattleshipGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private int size = 10;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(e->System.exit(0));

        String playerA = "jeden";
        String playerB = "bot";
        Player a = new Player(playerA, getBoard());
        Player b = new Player(playerB, getBoard());
        BattleshipGame battleshipGame = new RestBattleshipGame(a,b, size);

        Thread thread = new Thread(new RandomBot(battleshipGame, playerA));
        Bot bot = new Bot(battleshipGame, playerB);
        Thread thread2 = new Thread(bot);
        thread.start();
        thread2.start();

        battleshipGame.addBoardObserver(playerB, bot);

        showForBots(playerA, playerB, battleshipGame);

//        show(playerA,battleshipGame);
    }

    private void show(String playerName, BattleshipGame battleshipGame) throws java.io.IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
        Parent root = loader.load();
        GameController gameController = loader.getController();
        gameController.init(battleshipGame, playerName);

        stage.setTitle(":D");
        stage.setOnCloseRequest(e->System.exit(0));

        stage.setScene(new Scene(root, 1000, 500));
        stage.show();
    }

    private void showForBots(String playerAName, String playerBName, BattleshipGame battleshipGame) throws java.io.IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("botsGameView.fxml"));
        Parent root = loader.load();
        BotsBattleController gameController = loader.getController();
        gameController.init(battleshipGame, playerAName, playerBName);

        stage.setTitle(":D");
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.setScene(new Scene(root, 1000, 500));
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
        for (int i = 2; i < 8; i += 2) {
            List<Coordinates> ship = new ArrayList<>();
            for (int j = 5; j < i + 4; j++) {
                Coordinates c = new Coordinates(i, j);
                ship.add(c);
            }
            list.add(ship);
        }
        return list;
    }
}
