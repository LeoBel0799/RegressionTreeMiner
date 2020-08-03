package database;


/**
 * Modella la restituzione di un resultset vuoto.
 * 
 */

@SuppressWarnings("serial")
public class EmptySetException extends Exception{
	
	/**
	 * Richiama il costruttore della superclasse per inizializzare il messaggio
	 * di errore.
	 * 
	 * @param message
	 *            Messaggio di errore.
	 */
	public EmptySetException (String msgError){
		super (msgError);
	}

}
