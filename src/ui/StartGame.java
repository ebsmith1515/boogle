package ui;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

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
		setLayout(new MigLayout("fillx"));
		startButton = new JButton("Start Game");
		joinButton = new JButton("Join Game");
		ipField = new JTextField();
		numPlayers = new JLabel();
		numPlayers.setVisible(false);
		message = new JLabel();
		nameField = new JTextField();
	}

	private void initLayout() {
		add(new Label("Start Game"), "span, center, wrap");
		add(startButton, "split, span");
		add(joinButton, "wrap");
		add(new JLabel("Enter name (no spaces):"));
		add(nameField, "wrap, growx");
		add(new JLabel("IP"));
		add(ipField, "wrap, growx");
		add(numPlayers, "span, wrap");
		add(message, "span");
		//message.setPreferredSize(new Dimension(600, 600));
	}
}
