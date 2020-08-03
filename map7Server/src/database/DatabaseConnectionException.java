package database;


/**
 * Classe eccezione che modella il caso di fallimento di connessione al DB
 *
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {

	public DatabaseConnectionException (String msgError){
		super (msgError);
	}
}
