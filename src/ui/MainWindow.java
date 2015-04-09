package ui;

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Created by esmith on 4/6/15.
 */
public class MainWindow extends JPanel {
	GameBoard gameBoard;
	StartGame startGame;

	public MainWindow(MainController mainController) {
		gameBoard = new GameBoardController().gameBoard;
		startGame = new StartGameController(mainController).startGame;
		setLayout(new CardLayout());
		add(startGame, "StartGame");
		add(gameBoard, "GameBoard");
		((CardLayout)getLayout()).show(this, "StartGame");
	}
}
