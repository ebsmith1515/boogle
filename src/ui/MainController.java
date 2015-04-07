package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * Created by esmith on 4/6/15.
 */
public class MainController	implements Initializable {
	@FXML
	private VBox gameBoard;
	@FXML
	private VBox startGame;
	@FXML
	private GameBoardController gameBoardController;
	@FXML
	private StartGameController startGameController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startGameController.setMainController(this);
	}

	public void showGameBoard() {
		startGame.setVisible(false);
		gameBoard.setVisible(true);
	}
}
