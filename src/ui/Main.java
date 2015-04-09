package ui;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
		frame.setSize(400, 400);
		MainWindow mainWindow = new MainController().mainWindow;
		frame.setContentPane(mainWindow);
		frame.setVisible(true);
    }
}
