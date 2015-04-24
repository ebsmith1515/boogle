package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
		frame.setSize(800, 600);
		final MainController mainController = new MainController();
		MainWindow mainWindow = mainController.mainWindow;
		frame.setContentPane(mainWindow);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				mainController.windowClosing();
			}
		});
		mainController.finalInitUI();
    }
}
