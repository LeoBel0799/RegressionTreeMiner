package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Realizza l'accesso alla base di dati.
 * 
 */

public class DbAccess {
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; //(Per utilizzare questo Driver scaricare e aggiungere al classpath il connettore mysql connector)
	private final String DBMS = "jdbc:mysql";
	private final String SERVER="localhost"; //contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost)
	private String DATABASE = "MapDB"; //contiene il nome della base di dati
	private final int PORT=3306; //La porta su cui il DBMS MySQL accetta le connessioni
	private String USER_ID = "MapUser"; //contiene il nome dell’utente per l’accesso alla base di dati
	private String PASSWORD = "map";// contiene la password di autenticazione per l’utente identificato da USER_ID

	private Connection conn; // gestisce una connessione


	/**
	 * Impartisce al class loader l'ordine di caricare il driver mysql , e
	 * inizializza la connessione riferita da conn.
	 * 
	 * @throws DatabaseConnectionException
	 *             Eccezione sollevata in caso di fallimento nella connessione
	 *             al database.
	 */

	public void initConnection() throws DatabaseConnectionException {

		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("[!] Driver not found: " + e.getMessage());
			throw new DatabaseConnectionException("Impossibile carcicare la classe driver!");
		} catch(InstantiationException e){
			System.out.println("[!] Error during the instantiation : " + e.getMessage());
			throw new DatabaseConnectionException("Problema di accesso durante il caricamento!");
		} catch(IllegalAccessException e){
			System.out.println("[!] Cannot access the driver : " + e.getMessage());
			throw new DatabaseConnectionException("Impossibile istanziare il driver!");
		}

		try {

			String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
					+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";


			System.out.println("Connection's String: " + connectionString);
			conn = DriverManager.getConnection(connectionString);
		} catch(SQLException e) {
			System.out.println("[!] SQLException: " + e.getMessage());
			System.out.println("[!] SQLState: " + e.getSQLState());
			System.out.println("[!] VendorError: " + e.getErrorCode());
			e.printStackTrace();
			throw new DatabaseConnectionException("Errore durante la connessione al database!");
			
		}
	}


	/**
	 * Restituisce la connessione al database.
	 * 
	 * @return conn 
	 * 				Attuale connessione al database.
	 */
	public Connection getConnection() {
		return conn;
	}

	
	/**
	 * Chiude la connessione con il database.
	 * 
	 * @throws SQLException
	 *             Eccezione lanciata in caso di errori durante la chiusa del
	 *             database.
	 */
	public void closeConnection() throws SQLException{
		conn.close();
	}

}

