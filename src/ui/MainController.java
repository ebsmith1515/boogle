package ui;

import java.awt.CardLayout;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * Created by esmith on 4/6/15.
 */
public class MainController {

	protected MainWindow mainWindow;

	public MainController() {
		mainWindow = new MainWindow(this);
	}

	public void showGameBoard() {
		((CardLayout)mainWindow.getLayout()).show(mainWindow, "GameBoard");
	}
}
