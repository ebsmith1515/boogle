package ui;

import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by esmith on 4/8/15.
 */
public class StartGame extends JPanel {

	private StartGameController startGameController;

	JButton startButton;
	JButton joinButton;
	JTextField ipField;
	JTextField nameField;
	JLabel numPlayers;
	JLabel message;


	public StartGame(StartGameController controller) {
		this.startGameController = controller;
		initComponents();
		initLayout();
		initListeners();
	}

	private void initListeners() {
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGameController.initServer();
			}
		});
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGameController.joinGame();
			}
		});
	}

	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		startButton = new JButton("Start Game");
		joinButton = new JButton("Join Game");
		ipField = new JTextField();
		numPlayers = new JLabel();
		numPlayers.setVisible(false);
		message = new JLabel();
		nameField = new JTextField();
	}

	private void initLayout() {
		add(new Label("Start Game"));
		add(startButton);
		add(joinButton);
		add(new JLabel("Enter name (no spaces):"));
		add(nameField);
		add(ipField);
		add(numPlayers);
		add(message);
		message.setPreferredSize(new Dimension(600, 600));
	}
}
