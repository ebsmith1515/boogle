package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Thread {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	public Player(Socket socket) {
		this.socket = socket;
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Starting player thread.");
		try {
			while (true) {
					String line = input.readLine();
					if (line.startsWith("PING")) {
						output.println("WELCOME");
						output.flush();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException ignored) {}
		}
	}
}
