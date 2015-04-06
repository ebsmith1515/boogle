package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import server.BoggleServer;

public class BoggleClient extends Thread {

	private BufferedReader in;
	private PrintWriter out;


	@Override
	public void run() {
		connect(null);

		try {
			while (true) {
				String line = in.readLine();
				if (line.equals("WELCOME")) {
					System.out.println("Got response from server");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void connect(String serverAddress) {
		if (serverAddress == null) {
			serverAddress = "localhost";
		}
		try {
			Socket socket = new Socket(serverAddress, BoggleServer.PORT);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pingServer() {
		out.println("PING");
	}

	public static void main(String[] args) {
		BoggleServer server = new BoggleServer();
		server.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BoggleClient client = new BoggleClient();
		BoggleClient client2 = new BoggleClient();
		client.start();
		client2.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.pingServer();
		client2.pingServer();
	}
}
