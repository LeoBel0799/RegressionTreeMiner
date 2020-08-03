package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

	private int PORT;

	/**
	 * Costruttore di classe. Inizializza la porta ed interrgoa run()
	 * @param port
	 * @throws IOException
	 */
	public MultiServer(int port) throws IOException {
		PORT = port;
		run();
	}

	/**
	 * Istanzia un oggetto istanza della classe ServerSocket, che pone in attesta di 
	 * richiesta di connessioni da parte del client. Ad ogni nuova richiesta di connessione, si 
	 * istanzia ServerOneClient
	 * 
	 * @throws IOException
	 */
	private void run() throws IOException {

		ServerSocket serverSocket = new ServerSocket();
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", PORT);
		serverSocket.bind(serverAddr);

		System.out.println(serverSocket.toString());

		while(true){
			Socket s = serverSocket.accept();
			new ServerOneClient(s);
		}

	}

}