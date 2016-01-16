package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Main {

	public static final int VERSION = 3;

    public static void main(String[] args) {
		//http://stackoverflow.com/questions/2061194/swing-on-osx-how-to-trap-command-q
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
        JFrame frame = new JFrame();
		frame.setSize(800, 600);
		final MainController mainController = new MainController();
		MainWindow mainWindow = mainController.mainWindow;
		frame.setContentPane(mainWindow);
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				mainController.windowClosing();
				System.exit(0);
			}
		});
		mainController.finalInitUI();
    }
}
