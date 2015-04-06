package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GameBoardController implements Initializable {

	public TextField textEnter;
	public TextArea enteredWords;
	public GridPane boggleGrid;

	public static final int BOARD_SIZE = 4;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		testData();
	}

	private void testData() {
		for (int i=0; i < BOARD_SIZE; i++) {
			for (int j=0; j < BOARD_SIZE; j++) {
				boggleGrid.add(letterImage(), i, j);
			}
		}

		addWord("hello");
		addWord("world");
	}

	private void addWord(String word) {
		enteredWords.appendText(word + "\n");
	}

	private TextArea letterImage() {
		TextArea textArea = new TextArea("A");
		textArea.setEditable(false);
		textArea.setPrefSize(30, 30);
		return textArea;
	}
}
