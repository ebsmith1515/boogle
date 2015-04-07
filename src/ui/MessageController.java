package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by esmith on 4/6/15.
 */
public class MessageController {

	@FXML
	private Label message;

	public static enum Duration {
		SHORT(3), LONG(10);

		private int seconds;

		Duration(int seconds) {
			this.seconds = seconds;
		}
	}

	public void message(String text, final Duration duration) {
		message.setText(text);
	}
}
