package view;

import ai.RandomBoardGenerator;
import ai.RandomBot;
import game.BattleshipGame;
import game.Coordinates;
import game.Player;
import game.logic.RestBattleshipGame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class MainMenuController implements Initializable {

    private ObservableList<String> graphics = FXCollections.observableArrayList("dev");
    private boolean canActivateButtons = false;
    private GameType gameType;

    @FXML
    ComboBox<String> graphicChoice;

    @FXML
    Button single;

    @FXML
    Button multi;

    @FXML
    TextField size;

    @FXML
    Label error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graphicChoice.setItems(graphics);
        error.setDisable(false);
        graphicChoice.valueProperty().addListener((l, s, n) -> {
            activateButtons();
            this.canActivateButtons = true;
        });
        addButtonsListeners();
        size.textProperty().addListener((o, old, newValue) -> {
            if (!newValue.matches("\\d*")) {
                size.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (size.getText().length() > 0) {
                String value = size.getText();
                if (Integer.parseInt(value) > 16) {
                    error.setText("To big");
                    error.setVisible(true);
                    disableButtons();
                    return;
                } else if (Integer.parseInt(value) < 5) {
                    error.setText("To small");
                    error.setVisible(true);
                    disableButtons();
                    return;
                }
                if (canActivateButtons) {
                    activateButtons();
                }
                error.setVisible(false);
            }
        });
    }

    private void activateButtons() {
        single.setDisable(false);
        multi.setDisable(false);
    }

    private void disableButtons() {
        single.setDisable(true);
        multi.setDisable(true);
    }

    private void addButtonsListeners() {
        single.setOnAction(actionEvent -> singlePlayerGame());
        multi.setOnAction(actionEvent -> multiPlayerGame());
    }

    private void singlePlayerGame() {
        gameType = GameType.SINGLE;
        List<Integer> list = Arrays.asList(1, 5, 3, 2, 0);
        Stack<Integer> stack = new Stack<>();
        list.forEach(stack::push);
        String graphic = graphicChoice.getSelectionModel().getSelectedItem();
        int size = Integer.parseInt(this.size.getText());
        new ShipChoice(graphic, stack, size, this);
    }

    private void multiPlayerGame() {
        gameType = GameType.MULTI;
    }

    void playerReady(String name, List<List<Coordinates>> setShips) {
        Player player = new Player(name, setShips);
        if (gameType == GameType.SINGLE) {
            String botName = "bot";
            Player bot = new Player(botName, RandomBoardGenerator.getTestBoard());
            BattleshipGame game = new RestBattleshipGame(player, bot, Integer.parseInt(size.getText()));
            Thread thread = new Thread(new RandomBot(game, botName));
            thread.start();
            try {
                show(name, game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void show(String playerName, BattleshipGame battleshipGame) throws java.io.IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
        Parent root = loader.load();
        GameController gameController = loader.getController();
        gameController.init(battleshipGame, playerName);

        stage.setTitle(":D");
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.setScene(new Scene(root, 1000, 500));
        stage.show();
    }

    enum GameType {
        SINGLE, MULTI
    }
}
