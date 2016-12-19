package com.hatt.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

public class LightWebServer extends Thread {

	private int UNDEFINED = -1;
	private int port = UNDEFINED;
	private static final int DEFAULT_PORT_SERVER = 8188;

	private static ServerSocket serverSocket;

	public LightWebServer(int port) {
		this.port = port;
		start();
	}

	public int getPort() {
		if (port == UNDEFINED) {
			return DEFAULT_PORT_SERVER;
		}
		return port;
	}

	public void run() {
		try {
			System.out.println("Server component started..");
			serverSocket = new ServerSocket(getPort());
			boolean stop = false;
			while (!stop) {
				try {
					if (serverSocket.isClosed()) {
						break;
					}
					Socket s = serverSocket.accept();

					ClientHandler client = new ClientHandler(s);
					client.start();

				} catch (Exception x) {
					System.out.println(x);
					stop = true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class ClientHandler extends Thread {

		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Client component started..");
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

				String s = in.readLine();
				System.out.println("The S is:'" + s + "'");
				String interaction = s.substring(s.indexOf("/") + 1, s.indexOf("HTTP")).trim();
				// interaction=Utils.squeezeMultipleWhiteSpaceToOne(interaction);
				interaction = interaction.replaceAll("TAB", "\t");
				interaction = URLDecoder.decode(interaction, "UTF-8");

				out.println("HTTP/1.1 200 OK\r\n" + "Content-type: text/html\r\n\r\n"
						+ "<html><head></head><body>recorded</body></html>\n");
				out.close();
			} catch (IOException x) {
				System.out.println(x);
			}
		}
	}

}
