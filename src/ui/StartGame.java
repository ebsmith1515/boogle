package ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame extends JPanel {

	private StartGameController startGameController;

	JButton enterButton;
	JTextField addressField;
	JButton advancedButton;
	JTextField nameField;
	JLabel message;
	ImageIcon startImage;

	public StartGame(StartGameController controller) {
		this.startGameController = controller;
		initComponents();
		initLayout();
		initListeners();
	}

	private void initListeners() {
		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGameController.joinGame();
			}
		});
		advancedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addressField.setVisible(!addressField.isVisible());
			}
		});
	}

	private void initComponents() {
		setLayout(new MigLayout("fillx, filly"));
		enterButton = new JButton("Enter");
		advancedButton = new JButton("Advanced");
		addressField = new JTextField();
		addressField.setVisible(false);
		message = new JLabel();
		nameField = new JTextField();
		startImage = new ImageIcon(this.getClass().getResource("boogleImage.jpg"));
		setBackground(Color.WHITE);
	}

	private void initLayout() {
		add(new JLabel("", startImage, JLabel.CENTER), "span, center, wrap");
		add(enterButton, "span, wrap");
		add(new JLabel("Enter name (no spaces):"));
		add(nameField, "wrap, growx");
		add(message, "span, wrap, pushy, top");
		add(advancedButton);
		add(addressField);
	}
}
