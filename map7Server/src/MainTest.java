import java.io.IOException;
import java.sql.SQLException;


import server.MultiServer;

class MainTest {
	private static int port = 8080;

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
		new MultiServer(port);
	}
}


